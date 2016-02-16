package practice.design.principle.srp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author：Administrator
 * @version:1.1
 */
public class ImageLoader {
	private ImageCache mCache;
	private ExecutorService mThreadPool =
			Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private ImageLoader mInstance;

	public ImageLoader getInstance() {
		if (mInstance == null) {
			synchronized (ImageLoader.class) {
				if (mInstance == null) {
					mInstance = new ImageLoader();
				}
			}
		}
		return mInstance;
	}


	private ImageLoader() {
		mCache = new ImageCache();
	}


	public void displayImage(final String url, final ImageView iv) {
		Bitmap bitmap = mCache.get(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			return;
		}

		iv.setTag(url);

		mThreadPool.submit(new Runnable() {
			@Override
			public void run() {
//				该方法最大程度的避免下载操作。
//				if (iv.getTag().equals(url)) {
//					final Bitmap bitmap = downloadImage(url);
//					if (bitmap != null) {
//						iv.setImageBitmap(bitmap);
//						mLruCache.put(url, bitmap);
//					}
//				}
//              该方法最大程度的下载/缓存图片，最大程度利用线程资源
				Bitmap bitmap = downloadImage(url);
				if (bitmap == null) {
					return;
				}
				if (iv.getTag().equals(url)) {
					iv.setImageBitmap(bitmap);
				}
				mCache.put(url, bitmap);
			}
		});
	}

	public Bitmap downloadImage(String imageUrl) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			bitmap = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
