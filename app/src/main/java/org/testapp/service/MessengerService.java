package org.testapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import org.testapp.utils.MyConstants;

public class MessengerService extends Service {
	private static final String TAG = "MessengerService";

	private static class MessengerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Message msgToClient = Message.obtain(msg);
			switch (msg.what) {
				case MyConstants.MSG_FROM_CLIENT:
//					Log.i(TAG, "receiver msg from client :" + msg.getData().getString("msg"));
					try {
						Thread.sleep(2000);
						msgToClient.arg2 = msg.arg1 + msg.arg2;
						Messenger client = msg.replyTo;
						msgToClient.what = MyConstants.MSG_FROM_SERVICE;
//					bundle.putString("reply", "嗯，你的消息我已经收到，稍后会回复你");
						try {
							client.send(msgToClient);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
	}

	private final Messenger mMessenger = new Messenger(new MessengerHandler());

	public MessengerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
}
