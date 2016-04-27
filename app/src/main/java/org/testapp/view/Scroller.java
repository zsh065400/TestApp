package org.testapp.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
//		mHandler.sendEmptyMessage(MESSAGE_SCROLL_TO);
//		executeAnim();
		smoothScrollTo(1000, 0);
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


	private static final int MESSAGE_SCROLL_TO = 1;
	private static final int FRAME_COUNT = 30;
	private static final int DELAYED_TIME = 33;

	private int mCount = 0;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_SCROLL_TO:
					Log.d(TAG, "handleMessage: execute scroll");
					mCount++;
					if (mCount <= FRAME_COUNT) {
						float fraction = mCount / (float) FRAME_COUNT;
						int scrollX = (int) (fraction * 1000);
						scrollTo(scrollX, 0);
						mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
						invalidate();
					}
					break;
				default:
					super.handleMessage(msg);
					break;
			}
		}
	};

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mCount++;
			if (mCount <= FRAME_COUNT) {
				float fraction = mCount / (float) FRAME_COUNT;
				int scrollX = (int) (fraction * 100);
				scrollTo(scrollX, 0);
				postDelayed(mRunnable, 100);
			}
//				scrollTo(getScrollX() + 20, 0);
		}
	};

	private void executeAnim() {
		//利用该方法完成渐进滑动
		postDelayed(mRunnable, 100);
		Log.d(TAG, "executeAnim: execute scroll");

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO: 2016/3/3 线程实现滑动 
			}
		});
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

	int startX = 0;
	int deltaX = 100;

	/**
	 * 利用动画完成渐进式效果
	 */
	private void anim() {
		ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//利用动画的执行完成度，实现View动画
				//原理类似Scroller
				float fraction = animation.getAnimatedFraction();
				scrollTo(startX + (int) (deltaX * fraction), 0);
			}
		});
	}


	private android.widget.Scroller mScroller;

	private void smoothScrollTo(int destX, int destY) {
		int scrollX = getScrollX();
		int deltaX = destX - scrollX;
		mScroller.startScroll(scrollX, 0, deltaX, 0);
//		重要代码，出发onDraw，执行computeScroll
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
