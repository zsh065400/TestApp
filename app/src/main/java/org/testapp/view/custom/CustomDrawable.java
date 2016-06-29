package org.testapp.view.custom;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class CustomDrawable extends Drawable {
    private Paint mPaint;

    public CustomDrawable(int color) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect r = getBounds();
        final float cx = r.exactCenterX();
        final float cy = r.exactCenterY();
        canvas.drawCircle(cx, cy, Math.min(cx, cy), mPaint);
    }

    /**
     * 获取模糊程度
     *
     * @return
     */
    @Override
    public int getOpacity() {
        /*
        * TRANSLUCENT：半透明
        * TRANSPARENT：透明
        * */
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    /*
    * 当自定义Drawable有固定大小时最好重写这两个方法，以避免影响View的wrap_content
    * 布局。
    *
    * 绘制图片时可以选用图片大小作为固定大小
    *
    *
    * */
    @Override
    public int getIntrinsicHeight() {
        return super.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return super.getIntrinsicWidth();
    }
}
