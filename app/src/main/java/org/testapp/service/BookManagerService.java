package org.testapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import org.testapp.aidl.Book;
import org.testapp.aidl.IBookManager;
import org.testapp.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {
	private static final String TAG = "BMS";

	private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

	private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

//	private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListeners = new CopyOnWriteArrayList<>();

	//保存客户端的listener
	private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();


	private Binder mBinder = new IBookManager.Stub() {


		@Override
		public List<Book> getBookList() throws RemoteException {
			SystemClock.sleep(5000);
			return mBookList;
		}

		@Override
		public void addBook(Book book) throws RemoteException {
			mBookList.add(book);
		}

		@Override
		public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
//			if (!mListeners.contains(listener)) {
//				mListeners.add(listener);
//			} else {
//				Log.d(TAG, "already exists.");
//			}
			mListenerList.register(listener);
			Log.d(TAG, "registerListener, size:" + mListenerList.getRegisteredCallbackCount());
		}

		@Override
		public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
//			if (mListeners.contains(listener)) {
//				mListeners.remove(listener);
//				Log.d(TAG, "unregister listener succeed.");
//			} else {
//				Log.d(TAG, "not found, can not unregister");
//			}
			final boolean res = mListenerList.unregister(listener);
			if (res) {
				Log.d(TAG, "unregister listener succeed.");
				Log.d(TAG, "unregisterListener, current size:" + mListenerList.getRegisteredCallbackCount());
			} else {
				Log.d(TAG, "not found, can not unregister");
			}
		}
	};

	public BookManagerService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mBookList.add(new Book(1, "Android"));
		mBookList.add(new Book(2, "IOS"));
		new Thread(new ServiceWork()).start();
	}

	@Override
	public void onDestroy() {
		mIsServiceDestoryed.set(true);
		super.onDestroy();
	}

	private void onNewBookArrived(Book book) throws RemoteException {
		mBookList.add(book);
		final int N = mListenerList.beginBroadcast();
		for (int i = 0; i < N; i++) {
			IOnNewBookArrivedListener l = mListenerList.getBroadcastItem(i);
			if (l != null) {
				try {
					l.onNewBookArrived(book);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		mListenerList.finishBroadcast();
//		Log.d(TAG, "onNewBookArrived, notify listeners:" + mListeners.size());
//		for (IOnNewBookArrivedListener listener : mListeners) {
//			Log.d(TAG, "onNewBookArrived, notify listeners:" + listener);
//			listener.onNewBookArrived(book);
//		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}

	private class ServiceWork implements Runnable {
		@Override
		public void run() {
			//do background processing here...
			while (!mIsServiceDestoryed.get()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int bookId = mBookList.size() + 1;
				Book newBook = new Book(bookId, "new Book" + bookId);
				try {
					onNewBookArrived(newBook);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
