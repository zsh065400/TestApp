package practice.design.principle.design_principle;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @author：Administrator
 * @version:1.1
 */
public class MemoryCache implements ImageCache {
	private LruCache<String, Bitmap> mLruCache;

	public MemoryCache() {
		initCache();
	}

	private void initCache() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 4;
		mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//转换成kb
				return value.getByteCount() / 1024;
			}
		};
	}

	@Override
	public void put(String key, Bitmap value) {
		mLruCache.put(key, value);
	}

	@Override
	public Bitmap get(String key) {
		return mLruCache.get(key);
	}


}
