package org.testapp.aty;

import android.app.Application;
import android.os.Process;
import android.util.Log;

import org.testapp.utils.MyUtils;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/13 16:48
 * 修改人：Administrator
 * 修改时间：2016/1/13 16:48
 * 修改备注：
 */
public class MyApplication extends Application {

	private static final String TAG = "MyApplicatio";

	@Override
	public void onCreate() {
		super.onCreate();
		String proccessName = MyUtils.getProcessName(getApplicationContext(), Process.myPid());
		Log.d(TAG,"application start, proccess name: " + proccessName);
	}
}
