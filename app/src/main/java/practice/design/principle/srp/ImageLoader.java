package practice.design.principle.srp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author：Administrator
 * @version:1.0
 */
public class ImageLoader {
	private LruCache<String, Bitmap> mLruCache;
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
		initCache();
	}

	private void initCache() {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		int cacheSize = maxMemory / 4;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
//				return value.getRowBytes() * value.getHeight() / 1024;
//				value.getRowBytes() * value.getHeight() == value.getByteCount
				return value.getByteCount() / 1024;
			}
		};
	}

	public void displayImage(final String url, final ImageView iv) {
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
				Bitmap bitmap = getBitmapFromCache(url);
				if (bitmap == null) {
					bitmap = downloadImage(url);
					if (bitmap == null) {
						return;
					}
				}
				if (iv.getTag().equals(url)) {
					iv.setImageBitmap(bitmap);
				}
				mLruCache.put(url, bitmap);
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

	private Bitmap getBitmapFromCache(String url) {
		return mLruCache.get(url);
	}
}
