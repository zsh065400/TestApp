package org.testapp.aty;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.testapp.R;
import org.testapp.aidl.Book;
import org.testapp.proider.BookProvider;
import org.testapp.xu_lie_hua.User;

public class ProviderActivity extends AppCompatActivity {

	private static final String TAG = "ProviderActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider);
//		Uri uri = Uri.parse("content://" + BookProvider.AUTHORITIES);
//		getContentResolver().query(uri, null, null, null, null);
//		getContentResolver().query(uri, null, null, null, null);
//		getContentResolver().query(uri, null, null, null, null);
		ContentValues values = new ContentValues();
		values.put("_id", 6);
		values.put("name", "程序的设计艺术");
		getContentResolver().insert(BookProvider.BOOK_CONTENT_URI, values);

		Cursor bookCursor = getContentResolver().query(BookProvider.BOOK_CONTENT_URI, new String[]{"_id", "name"}, null, null, null);
		if (bookCursor != null) {
			while (bookCursor.moveToNext()) {
				Book book = new Book();
				book.bookId = bookCursor.getInt(0);
				book.bookName = bookCursor.getString(bookCursor.getColumnIndex("name"));
				Log.d(TAG, "query book : " + book.toString());
			}
			bookCursor.close();
		}

		Cursor userCursor = getContentResolver().query(BookProvider.USER_CONTENT_URI, new String[]{"_id", "name", "sex"}, null, null, null, null);
		if (userCursor != null) {
			while (userCursor.moveToNext()) {
				User user = new User();
				user.userId = userCursor.getInt(0);
				user.userName = userCursor.getString(1);
				user.isMale = userCursor.getInt(2) == 1;
				Log.d(TAG, user.toString());
			}
			userCursor.close();
		}

	}
}
