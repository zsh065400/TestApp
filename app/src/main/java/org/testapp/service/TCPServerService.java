package org.testapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.testapp.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
	private boolean mIsServiceDestoryed = false;
	private String[] mDefineMessages = new String[]{
			"你好啊，哈哈",
			"请问你叫什么名字？",
			"现在的冬天很冷吧",
			"给我讲个笑话吧",
			"这个系统可以和多个人同时聊天哦",
			"我要让所有人知道，他们用的软件都有我写的痕迹"
	};

	@Override
	public void onCreate() {
		new Thread(new TcpServer()).start();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		mIsServiceDestoryed = true;
		super.onDestroy();
	}

	public TCPServerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class TcpServer implements Runnable {
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				//监听端口
				serverSocket = new ServerSocket(8688);
			} catch (IOException e) {
				System.err.println("establish tcp server failed, port:8688");
				e.printStackTrace();
				return;
			}

			while (!mIsServiceDestoryed) {
				//接收客户端请求
				try {
					final Socket clinet = serverSocket.accept();
					System.out.println("accpet");
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								responseClinet(clinet);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void responseClinet(Socket clinet) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(clinet.getInputStream()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clinet.getOutputStream())));
		out.println("欢迎来到聊天室");
		out.flush();
		while (!mIsServiceDestoryed) {
			String str = in.readLine();
			System.out.println("msg from clinet:" + str);
			if (str == null) {

				break;
			}
			int i = new Random().nextInt(mDefineMessages.length);
			String msg = mDefineMessages[i];
			out.println(msg);
			out.flush();
			System.out.println("send:" + msg);
		}
		System.out.println("clinet quit.");
		MyUtils.close(out);
		MyUtils.close(in);
		clinet.close();
	}
}
