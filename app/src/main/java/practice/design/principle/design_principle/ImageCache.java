package practice.design.principle.design_principle;

import android.graphics.Bitmap;

/**
 * @author：Administrator
 * @version:1.0
 */
public interface ImageCache {

	void put(String url, Bitmap target);

	Bitmap get(String url);
}
