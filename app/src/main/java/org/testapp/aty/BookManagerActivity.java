package org.testapp.aty;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.testapp.R;
import org.testapp.aidl.Book;
import org.testapp.aidl.IBookManager;
import org.testapp.aidl.IOnNewBookArrivedListener;
import org.testapp.service.BookManagerService;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {


	private static final String TAG = "BookManagerActivity";
	private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

	private IBookManager mRemoteBookManager;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_NEW_BOOK_ARRIVED:
					Log.d(TAG, "receive new book:" + msg.obj);
					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
	};

	private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
		@Override
		public void onNewBookArrived(Book book) throws RemoteException {
			mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
		}
	};

	IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			Log.e(TAG, Thread.currentThread().getName());
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			IBookManager bookManager = IBookManager.Stub.asInterface(service);
			try {
				mRemoteBookManager = bookManager;
				List<Book> list = bookManager.getBookList();
				Log.i(TAG, "query book list, list type:" + list.getClass().getCanonicalName());
				Log.i(TAG, "query book list:" + list.toString());
				Book newBook = new Book(3, "Android开发艺术探索");
				bookManager.addBook(newBook);
				Log.i(TAG, "add book:" + newBook);
				List<Book> newList = bookManager.getBookList();
				Log.i(TAG, "query book list:" + newList.toString());
				bookManager.registerListener(mOnNewBookArrivedListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			try {
				mRemoteBookManager.asBinder().linkToDeath(recipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mRemoteBookManager.asBinder().unlinkToDeath(recipient, 0);
			mRemoteBookManager = null;
			Log.e(TAG, Thread.currentThread().getName());
			Log.e(TAG, "binder died.");
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_manager);
		Intent intent = new Intent(this, BookManagerService.class);
		//5.0以后需要添加该语句
		intent.setPackage("org.testapp.service");
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		findViewById(R.id.btn_do_remote).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						if (mRemoteBookManager != null) {
							try {
								mRemoteBookManager.getBookList();
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
	}

	@Override
	protected void onDestroy() {
		if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
			try {
				Log.i(TAG, "unregister listener:" + mOnNewBookArrivedListener);
				mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		unbindService(mConnection);
		super.onDestroy();
	}
}
