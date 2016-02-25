package org.testapp.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class Scroller extends View {
	private static final String TAG = Scroller.class.getSimpleName();

	public Scroller(Context context) {
		super(context);
		init(context);
	}

	public Scroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mScroller = new android.widget.Scroller(context);
	}

	private int mLastX;
	private int mLastY;


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;

			case MotionEvent.ACTION_MOVE:
				int deltaX = x - mLastX;
				int deltaY = y - mLastY;
				Log.d(TAG, "onTouchEvent: move, deltaX:" + deltaX + " deltaY:" + deltaY);
				float translationX = (getTranslationX() + deltaX);
				float translationY = (getTranslationY() + deltaY);
				Log.d(TAG, "onTouchEvent: getTranslationX:" + getTranslationX() + " getTranslationY:" + getTranslationY());
				Log.d(TAG, "onTouchEvent: move, translationX:" + translationX + " translationY:" + translationY);
				setTranslationX(translationX);
				setTranslationY(translationY);
				break;

			case MotionEvent.ACTION_UP:

				break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}

	/**
	 * 属性动画
	 *
	 * @param from
	 * @param to
	 */
	private void translation(int from, int to) {
		ObjectAnimator.ofFloat(this, "translationX", from, to).setDuration(100).start();
	}

	private android.widget.Scroller mScroller;

	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int deltaX = destX - scrollX;
		mScroller.startScroll(scrollX, 0, deltaX, 0);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
}
