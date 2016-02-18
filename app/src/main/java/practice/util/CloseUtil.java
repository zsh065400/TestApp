package practice.util;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author：Administrator
 * @version:1.0
 */
public class CloseUtil {

	public static final void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				Log.e("Close TAG", "close failed......");
				e.printStackTrace();
			}
		} else {
			Log.e("Close TAG", "obj is null, can't to close it");
		}
	}

}
