package org.testapp.proider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.testapp.datebase.DbOpenHelper;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/18 18:14
 * 修改人：Administrator
 * 修改时间：2016/1/18 18:14
 * 修改备注：
 */
public class BookProvider extends ContentProvider {
	private static final String TAG = "BookProvider";
	public static final String AUTHORITIES = "org.testapp.zsh.provider";

	public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/book");
	public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/user");

	public static final int BOOK_URI_CODE = 0;
	public static final int BOOK_ONE_URI_CODE = 2;
	public static final int USER_URI_CODE = 1;
	public static final int USER_ONE_URI_CODE = 3;

	private static final UriMatcher sURI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		sURI_MATCHER.addURI(AUTHORITIES, "book", BOOK_URI_CODE);
		sURI_MATCHER.addURI(AUTHORITIES, "user", USER_URI_CODE);
		sURI_MATCHER.addURI(AUTHORITIES, "book/#", BOOK_ONE_URI_CODE);
		sURI_MATCHER.addURI(AUTHORITIES, "user/#", USER_ONE_URI_CODE);
	}

	private Context mContext;
	private SQLiteDatabase mDb;

	@Override
	public boolean onCreate() {
		Log.d(TAG, "on Create, current thread:" + Thread.currentThread().getName());
		mContext = getContext();
		initProviderData();
		return true;
	}

	private void initProviderData() {
		mDb = new DbOpenHelper(mContext).getWritableDatabase();
		mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
		mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
		mDb.execSQL("insert into book values (3, 'Android');");
		mDb.execSQL("insert into book values (4, 'IOS');");
		mDb.execSQL("insert into book values (5, 'HTML5');");
		mDb.execSQL("insert into user values (1, 'jake', 1);");
		mDb.execSQL("insert into user values (3, 'jasmine', 0);");
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query, current thread:" + Thread.currentThread().getName());
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported URI : " + uri.toString());
		}
		switch (sURI_MATCHER.match(uri)) {
			case BOOK_ONE_URI_CODE:
			case USER_ONE_URI_CODE:
				long id = ContentUris.parseId(uri);
				selection = "_id = ?";
				selectionArgs = new String[]{String.valueOf(id)};
				break;
			case BOOK_URI_CODE:
			case USER_URI_CODE:

				break;
		}
		Cursor cursor = mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
		return cursor;
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		Log.d(TAG, "getTyple");
		return null;
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "insert");
		switch (sURI_MATCHER.match(uri)) {
			case BOOK_ONE_URI_CODE:
			case USER_ONE_URI_CODE:
				throw new IllegalArgumentException("Unsupported URI : " + uri.toString());
		}
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported URI : " + uri.toString());
		}
		long rowId = mDb.insert(table, null, values);
		if (rowId > 0) {
			mContext.getContentResolver().notifyChange(uri, null);
			if (table.equals("book")) {
				return ContentUris.withAppendedId(uri, rowId);
			} else if (table.equals("user")) {
				return ContentUris.withAppendedId(uri, rowId);
			}
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported URI : " + uri.toString());
		}
		int count = mDb.delete(table, selection, selectionArgs);
		if (count > 0) {
			mContext.getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "update");
		String table = getTableName(uri);
		if (table == null) {
			throw new IllegalArgumentException("Unsupported URI : " + uri.toString());
		}
		int row = mDb.update(table, values, selection, selectionArgs);
		if (row > 0) {
			mContext.getContentResolver().notifyChange(uri, null);
		}
		return row;
	}


	private String getTableName(Uri uri) {
		String tableName = null;
		switch (sURI_MATCHER.match(uri)) {
			case BOOK_URI_CODE:
			case BOOK_ONE_URI_CODE:
				tableName = DbOpenHelper.BOOK_TABLE_NAME;
				break;
			case USER_URI_CODE:
			case USER_ONE_URI_CODE:
				tableName = DbOpenHelper.USER_TABLE_NAME;
				break;
			default:
				break;
		}
		return tableName;
	}

	/*
	* 自定义调用
	* */
	@Nullable
	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		Bundle data = new Bundle();
		switch (arg) {
			case "test":
				String s = test(extras.getInt("a"));
				data.putString("test", s);
				break;
			default:

				break;
		}
		super.call(method, arg, extras);
		return data;
	}

	public String test(int a) {
		a++;
		return String.valueOf(a);
	}
}
