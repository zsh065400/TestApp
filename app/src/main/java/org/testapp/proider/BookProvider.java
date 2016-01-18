package org.testapp.proider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

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
	@Override
	public boolean onCreate() {
		Log.d(TAG, "on Create, current thread:" + Thread.currentThread().getName());
		return false;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query, current thread:" + Thread.currentThread().getName());
		return null;
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
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		Log.d(TAG, "delete");
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "update");
		return 0;
	}

}
