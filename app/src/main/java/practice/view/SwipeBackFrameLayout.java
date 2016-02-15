package practice.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author：Administrator
 * @version:1.0
 */
public class SwipeBackFrameLayout extends FrameLayout {
	public interface CallBack {
		void onShouldFinish();
	}

	private CallBack mCallBack;

	public void setCallBack(CallBack callBack) {
		this.mCallBack = callBack;
	}

	private ViewDragHelper mViewDragHelper;
	private View mDividerView;
	private View mContentView;

	public SwipeBackFrameLayout(Context context) {
		super(context);
		init();
	}

	public SwipeBackFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private int mLastdx;
	private int mDividerWidth;

	public SwipeBackFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mViewDragHelper = ViewDragHelper.create(this
				, 1f, new ViewDragHelper.Callback() {
					@Override
					public boolean tryCaptureView(View child, int pointerId) {
						return false;
					}

					@Override
					public void onEdgeTouched(int edgeFlags, int pointerId) {
						super.onEdgeTouched(edgeFlags, pointerId);
						//触摸到左边缘，截住view视图
						mViewDragHelper.captureChildView(mContentView, pointerId);
					}

					@Override
					public int getViewHorizontalDragRange(View child) {
						return 1;
					}

					@Override
					public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
						super.onViewPositionChanged(changedView, left, top, dx, dy);
						float alpha = (float) (left * 1.0 / mDividerWidth);
						//越往右，被覆盖的view越清晰
						mDividerView.setAlpha(alpha);
					}

					@Override
					public int clampViewPositionHorizontal(View child, int left, int dx) {
						mLastdx = dx;
						int newLeft = Math.min(mDividerWidth, Math.max(left, 0));
						return newLeft;
					}

					@Override
					public void onViewReleased(View releasedChild, float xvel, float yvel) {
						//大于0代表关闭
						if (mLastdx > 0) {
							if (mDividerWidth != releasedChild.getLeft()) {
								//进行自动滑动，向右
								mViewDragHelper.settleCapturedViewAt(mDividerWidth, releasedChild.getTop());
								invalidate();
							} else {
								if (mCallBack != null) {
									mCallBack.onShouldFinish();
								}
							}
						} else {
							if (mDividerWidth != 0) {
								//进行自动滑动，向左
								mViewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
								invalidate();
							}
						}
					}

					/**
					 * 拖动状态改变
					 * @see ViewDragHelper#STATE_IDLE     0        停止
					 * STATE_DRAGGING                     1        正在被拖动
					 * STATE_SETTLING                     2        自动移动?
					 * @param state
					 */
					@Override
					public void onViewDragStateChanged(int state) {
						super.onViewDragStateChanged(state);

						if (mViewDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE &&
								mCallBack != null &&
								mDividerWidth == mContentView.getLeft() &&
								mLastdx > 0) {
							mCallBack.onShouldFinish();
						}
					}
				});
		//启用边缘滑动
		mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mDividerView = getChildAt(0);
		mDividerView.setAlpha(0f);
		mContentView = getChildAt(1);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mDividerWidth = mDividerView.getWidth();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mViewDragHelper.continueSettling(true)) {
			invalidate();
		}
	}
}
