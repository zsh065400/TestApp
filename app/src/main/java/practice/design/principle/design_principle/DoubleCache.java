package practice.design.principle.design_principle;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author：Administrator
 * @version:1.0
 */
public class DoubleCache implements ImageCache {
	private MemoryCache mMemoryCache;
	private DiskCache mDiskCache;

	public DoubleCache(Context context) {
		if (mMemoryCache == null) {
			mMemoryCache = new MemoryCache();
		}
		if (mDiskCache == null) {
			mDiskCache = new DiskCache(context);
		}
	}

	@Override
	public void put(String url, Bitmap target) {
		mMemoryCache.put(url, target);
		mDiskCache.put(url, target);
	}

	@Override
	public Bitmap get(String url) {
		Bitmap bitmap = mMemoryCache.get(url);
		if (bitmap == null) {
			bitmap = mDiskCache.get(url);
		}
		return bitmap;
	}
}
