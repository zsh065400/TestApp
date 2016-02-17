package practice.design.principle.design_principle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author：Administrator
 * @version:1.0
 */
public class DiskCache implements ImageCache {
	private static String cachePath;
	private static final String TAG = DiskCache.class.getSimpleName();

	public DiskCache(Context context) {
		if (cachePath == null) {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				cachePath = context.getExternalCacheDir().getAbsolutePath() + File.separator;
			} else {
				Log.d(TAG, "External SD Card is not mounted, please mounting and try");
			}
		}
	}

	/**
	 * 缓存入外部存储
	 *
	 * @param url    图像url
	 * @param target 缓存的图像
	 */
	@Override
	public void put(String url, Bitmap target) {
		File file = new File(cachePath + url);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			target.compress(Bitmap.CompressFormat.WEBP, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, "bitmap cache error!");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Bitmap get(String url) {
		return BitmapFactory.decodeFile(cachePath + url);
	}

}
