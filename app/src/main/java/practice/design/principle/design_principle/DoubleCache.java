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

	// TODO: 2016/2/17 双缓存put和get优化 
	@Override
	public void put(String url, Bitmap target) {
		Bitmap bitmap = get(url);
		if (bitmap == null) {
			mMemoryCache.put(url, target);
			mDiskCache.put(url, target);
		}
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
