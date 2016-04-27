package org.testapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class HorizontalScrollViewEx extends ViewGroup {
	public HorizontalScrollViewEx(Context context) {
		super(context);
	}

	public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private static final String TAG = "HorizontalScrollViewEx";
	private int mChildrenSize;
	private int mChildWidth;
	private int mChildIndex;
	//分别记录上次滑动的坐标
	private int mLastX = 0;
	private int mLastY = 0;
	//分别记录上次拦截的滑动坐标
	private int mLastXIntercept = 0;
	private int mLastYIntercept = 0;


	private android.widget.Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//初始化控件
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void init() {
		mScroller = new Scroller(getContext());
		mVelocityTracker = VelocityTracker.obtain();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean intercepted = false;
		int x = (int) ev.getX();
		int y = (int) ev.getY();

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				intercepted = false;
				//若下次事件序列到来时动画未执行完，则继续由父控件拦截。
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
					intercepted = true;
				}
				break;

			case MotionEvent.ACTION_MOVE:
				int deltaX = x - mLastXIntercept;
				int deltaY = y - mLastYIntercept;
				if (Math.abs(deltaX) > Math.abs(deltaY)) {
					intercepted = true;
				} else {
					intercepted = false;
				}
				break;

			case MotionEvent.ACTION_UP:
				intercepted = false;
				break;
			default:
				break;
		}
		Log.d(TAG, "onInterceptTouchEvent: =" + intercepted);
		mLastX = x;
		mLastY = y;
		//一旦父控件拦截事件，则后续事件均有父控件处理
		mLastXIntercept = x;
		mLastYIntercept = y;
		return intercepted;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mVelocityTracker.addMovement(event);
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				break;

			case MotionEvent.ACTION_UP:
				int scrollX = getScrollX();
				int scrollToChildIndex = scrollX / mChildWidth;
				mVelocityTracker.computeCurrentVelocity(1000);
				float xVelocity = mVelocityTracker.getXVelocity();
				//根据滑动速率判断，当手指抬起后，自动滑向哪个位置
				if (Math.abs(xVelocity) >= 50) {
					mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
				} else {
					mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
				}
				//避免子View滑动溢出，最小为0，最大为总量-1（下标）
				mChildIndex = Math.max(0, Math.min(mChildIndex, mChildrenSize - 1));
				//计算增量并开始滑动
				int dx = mChildIndex * mChildWidth - scrollX;
				smoothScrollBy(dx, 0);
				//释放资源
				mVelocityTracker.clear();
				break;

			case MotionEvent.ACTION_MOVE:
				int deltaX = x - mLastX;
				int deltaY = y - mLastY;
				//当滑动像素为正，则代表手指向右，此时界面应向左移动，反之亦然
				scrollBy(-deltaX, 0);
				break;

			default:
				break;
		}
		mLastX = x;
		mLastY = y;
		return true;
	}

	private void smoothScrollBy(int dx, int i) {
		mScroller.startScroll(getScrollX(), 0, dx, 0, 500);
		invalidate();
//		requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
}
