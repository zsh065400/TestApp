package org.testapp.utils;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/13 16:46
 * 修改人：Administrator
 * 修改时间：2016/1/13 16:46
 * 修改备注：
 */

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class MyUtils {

	public static String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == pid) {
				return procInfo.processName;
			}
		}
		return null;
	}

	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void executeInThread(Runnable runnable) {
		new Thread(runnable).start();
	}

}

