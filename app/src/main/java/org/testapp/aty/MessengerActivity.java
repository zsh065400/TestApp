package org.testapp.aty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.testapp.R;
import org.testapp.service.MessengerService;
import org.testapp.utils.MyConstants;

public class MessengerActivity extends AppCompatActivity {

	private static final String TAG = "MessengerActivity";
	private Messenger mService;
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = new Messenger(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

	private static class MessengerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MyConstants.MSG_FROM_SERVICE:
//					Log.i(TAG, "receive msg from Service :" + msg.getData().getString("reply"));
					TextView tv = (TextView) mRootView.findViewById(msg.arg1);
					tv.setText(tv.getText() + " ==>" + msg.arg2);
					break;

				default:
					super.handleMessage(msg);
					break;
			}
		}
	}

	private Button mBtnRandomCalculate;
	private static LinearLayout mRootView;
	private static Context mContext;
	private static int a = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger);
		mContext = this;
		Intent intent = new Intent(this, MessengerService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBtnRandomCalculate = (Button) findViewById(R.id.btnRandomCalculate);
		mRootView = (LinearLayout) findViewById(R.id.rootView);
		mBtnRandomCalculate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				a++;
				int b = (int) (Math.random() * 100);
				String content = "a is " + a + ", b is " + b + " calculate... ";
				TextView tv = new TextView(mContext);
				tv.setText(content);
				tv.setId(a);
				mRootView.addView(tv);
				Message msg = Message.obtain(null, MyConstants.MSG_FROM_CLIENT, a, b);
				msg.replyTo = mGetReplyMessenger;
				try {
					mService.send(msg);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		unbindService(mConnection);
		super.onDestroy();
	}
}
