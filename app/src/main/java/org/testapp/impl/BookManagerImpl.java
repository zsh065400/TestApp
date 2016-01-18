package org.testapp.impl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import org.testapp.aidl.Book;

import java.util.List;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/14 20:34
 * 修改人：Administrator
 * 修改时间：2016/1/14 20:34
 * 修改备注：
 */
public class BookManagerImpl extends Binder implements IBookManager {
	public BookManagerImpl() {
		this.attachInterface(this, DESCRIPTOR);
	}

	public static IBookManager asInterface(IBinder obj) {
		if (obj == null) {
			return null;
		}

		IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
		if ((iin != null) && (iin instanceof IBookManager)) {
			return (IBookManager) iin;
		}

		return new Proxy(obj);

	}

	@Override
	public IBinder asBinder() {
		return this;
	}

	public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
			throws RemoteException {
		switch (code) {
			case INTERFACE_TRANSACTION:
				reply.writeString(DESCRIPTOR);
				return true;
			case TRANSACTION_getBookList:
				data.enforceInterface(DESCRIPTOR);
				List<Book> result = this.getBookList();
				reply.writeNoException();
				reply.writeTypedList(result);
				return true;
			case TRANSACTION_addBook:
				data.enforceInterface(DESCRIPTOR);
				Book arg0;
				if (0 != data.readInt()) {
					arg0 = Book.CREATOR.createFromParcel(data);
				} else {
					arg0 = null;
				}
				this.addBook(arg0);
				reply.writeNoException();
				return true;
		}
		return super.onTransact(code, data, reply, flags);
	}


	@Override
	public List<Book> getBookList() throws RemoteException {
		// TODO: 2016/1/14 待实现
		return null;
	}

	@Override
	public void addBook(Book book) throws RemoteException {
		// TODO: 2016/1/14 待实现
	}

	private static class Proxy implements IBookManager {
		private IBinder mRemote;

		Proxy(IBinder mRemote) {
			this.mRemote = mRemote;
		}

		@Override
		public IBinder asBinder() {
			return mRemote;
		}

		public String getInterfaceDescriptor() {
			return DESCRIPTOR;
		}

		@Override
		public List<Book> getBookList() throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			List<Book> result;
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
				reply.readException();
				result = reply.createTypedArrayList(Book.CREATOR);
			} finally {
				data.recycle();
				reply.recycle();
			}
			return result;
		}

		@Override
		public void addBook(Book book) throws RemoteException {
			Parcel data = Parcel.obtain();
			Parcel reply = Parcel.obtain();
			try {
				data.writeInterfaceToken(DESCRIPTOR);
				if (book != null) {
					data.writeInt(1);
					book.writeToParcel(data, 0);
				} else {
					data.writeInt(0);
				}
				mRemote.transact(TRANSACTION_addBook, data, reply, 0);
				reply.readException();
			} finally {
				data.recycle();
				reply.recycle();
			}
		}


	}
}
