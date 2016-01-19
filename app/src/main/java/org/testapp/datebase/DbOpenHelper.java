package org.testapp.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/19 18:03
 * 修改人：Administrator
 * 修改时间：2016/1/19 18:03
 * 修改备注：
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "book_provider.db";
	public static final String BOOK_TABLE_NAME = "book";
	public static final String USER_TABLE_NAME = "user";

	private static final int DB_VERSION = 1;

	private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME +
			" (_id INTEGER PRIMARY KEY," + " name TEXT)";

	private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
			" (_id INTEGER PRIMARY KEY, " + " name TEXT," + " sex INT)";

	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOK_TABLE);
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: 2016/1/19 ignored 
	}
}
