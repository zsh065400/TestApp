package org.testapp.impl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import org.testapp.aidl.Book;

import java.util.List;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/14 20:29
 * 修改人：Administrator
 * 修改时间：2016/1/14 20:29
 * 修改备注：
 */
public interface IBookManager extends IInterface {
	static final String DESCRIPTOR = "org.testapp.impl.IBookManager";
	static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
	static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

	public List<Book> getBookList() throws RemoteException;

	public void addBook(Book book) throws RemoteException;

}
