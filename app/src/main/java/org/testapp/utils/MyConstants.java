package org.testapp.utils;

import android.os.Environment;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/15 9:49
 * 修改人：Administrator
 * 修改时间：2016/1/15 9:49
 * 修改备注：
 */


public class MyConstants {
	public static final String CHAPTER_2_PATH = Environment
			.getExternalStorageDirectory().getPath()
			+ "/singwhatiwanna/chapter_2/";

	public static final String CACHE_FILE_PATH = CHAPTER_2_PATH + "usercache";

	public static final int MSG_FROM_CLIENT = 0;
	public static final int MSG_FROM_SERVICE = 1;

}
