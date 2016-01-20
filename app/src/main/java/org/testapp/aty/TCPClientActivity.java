package org.testapp.aty;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.testapp.R;
import org.testapp.service.TCPServerService;
import org.testapp.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPClientActivity extends AppCompatActivity {
	private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
	private static final int MESSAGE_SOCKET_CONNECTED = 2;

	private Button mBtnSend;
	private TextView mTvMessage;
	private EditText mEtMessage;

	private Socket mClinetSocket;
	private PrintWriter mPrintWriter;

	private boolean isFinished = false;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_RECEIVE_NEW_MSG:
					mTvMessage.setText(mTvMessage.getText() + (String) msg.obj);
					break;
				case MESSAGE_SOCKET_CONNECTED:
					mBtnSend.setEnabled(true);
					break;
				default:
					super.handleMessage(msg);
					break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tcpclient);

		initView();
		startCommunication();

		new Thread(new Runnable() {
			@Override
			public void run() {
				connectTCPServer();
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		isFinished = true;
//		stopService(new Intent(this, TCPServerService.class));
		if (mClinetSocket != null) {
			try {
				mClinetSocket.shutdownInput();
				//因mClinetSocket和socket引用的是同一个socket，所以涉及到并行关闭流的问题。一方关闭，另一方必然报异常
				mClinetSocket.shutdownOutput();
				mClinetSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDestroy();
	}

	private void startCommunication() {
		Intent intent = new Intent(this, TCPServerService.class);
		//5.0以后需要设置包名，跨应用可设置类名
//		intent.setPackage("org.testapp.service");
//		intent.setClassName("org.tes?tapp.service", "org.testapp.service.TCPServerService");
		startService(intent);
	}

	private void initView() {
		mTvMessage = (TextView) findViewById(R.id.tvMessage);
		mEtMessage = (EditText) findViewById(R.id.etMessage);
		mBtnSend = (Button) findViewById(R.id.btnSend);
		mBtnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btnSend) {
					final String msg = mEtMessage.getText().toString();
					if ((!TextUtils.isEmpty(msg)) && mPrintWriter != null) {
						mPrintWriter.println(msg);
						mPrintWriter.flush();
						mEtMessage.setText("");
						String time = formatDateTime(System.currentTimeMillis());
						final String showedMsg = "self " + time + ":" + msg + "\n";
						mTvMessage.setText(mTvMessage.getText().toString() + showedMsg);
					}
				}
			}
		});
	}

	private String formatDateTime(long l) {
		return new SimpleDateFormat("(HH:mm:ss)").format(new Date(l));
	}

	private void connectTCPServer() {
		Socket socket = null;
		while (socket == null) {
			try {
				socket = new Socket("localhost", 8688);
				mClinetSocket = socket;
				mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
				System.out.println("connect server success");
			} catch (IOException e) {
				SystemClock.sleep(2500);
				System.out.println("connect tcp server failed, retry...");
				e.printStackTrace();
			}
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (!isFinished) {
				String msg = br.readLine();
				if (msg != null) {
					String time = formatDateTime(System.currentTimeMillis());
					final String showedMg = "server " + time + ":" + msg + "\n";
					System.out.println("receive :" + msg);
					mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMg).sendToTarget();
				}
			}
			System.out.println("quit...");
			//并行关闭，注意异常处理 // TODO: 2016/1/20
			MyUtils.close(mPrintWriter);
			MyUtils.close(br);
			socket.close();
		} catch (IOException e) {
			//可以捕获异常，通知客户端，服务端异常退出
			e.printStackTrace();
		}
	}
}
