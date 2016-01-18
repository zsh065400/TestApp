package org.testapp.aidl;

import org.testapp.aidl.Book;

interface IOnNewBookArrivedListener{
	void onNewBookArrived(in Book book);

}