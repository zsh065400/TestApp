package org.testapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class StickyLayout extends LinearLayout {
	public StickyLayout(Context context) {
		super(context);
	}

	public StickyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int mTouchSlop;
	//记录滑动终点的坐标
	private int mLastX = 0;
	private int mLastY = 0;
	//滑动起始坐标
	private int mLastXIntercept = 0;
	private int mLastYIntercept = 0;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int intercepted = 0;
		int x = (int) ev.getX();
		int y = (int) ev.getY();

		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastXIntercept = x;
				mLastYIntercept = y;
				mLastX = x;
				mLastY = y;
				intercepted = 0;
				break;

			case MotionEvent.ACTION_MOVE:
				int deltaX = x - mLastXIntercept;
				int deltaY = y - mLastYIntercept;
				break;

			case MotionEvent.ACTION_UP:

				break;

			default:
				break;

		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
