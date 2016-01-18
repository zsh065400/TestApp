package org.testapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import org.testapp.aidl.Book;
import org.testapp.aidl.IBookManager;
import org.testapp.aidl.IOnNewBookArrivedListener;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
	private List<Book> mBookList = new ArrayList<>();

	public MyService() {
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return mBinder;
	}

	private final IBookManager.Stub mBinder = new IBookManager.Stub() {
		@Override
		public List<Book> getBookList() throws RemoteException {
			synchronized (mBookList) {
				return mBookList;
			}
		}

		@Override
		public void addBook(Book book) throws RemoteException {
			synchronized (mBookList) {
				if (mBookList.contains(book)) {
					mBookList.add(book);
				}
			}
		}

		@Override
		public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

		}

		@Override
		public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {

		}
	};
}
