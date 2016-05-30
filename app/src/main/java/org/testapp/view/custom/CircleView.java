package org.testapp.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import org.testapp.R;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class CircleView extends android.view.View {
    /**
     * 代码中创建时回调的函数
     *
     * @param context
     */
    public CircleView(Context context) {
        super(context);
        init();
    }


    /**
     * XML布局中使用回调的函数
     *
     * @param context
     * @param attrs
     */
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 带style属性时回调的函数
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
        a.recycle();
        init();
    }

    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int mWidth = 100;
    private final int mHeight = 100;

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(getMeasuredWidth());
        int heightSpecMode = MeasureSpec.getMode(getMeasuredHeight());
        int widthSpecSize = MeasureSpec.getSize(getMeasuredHeight());
        int heightSpecSize = MeasureSpec.getSize(getMeasuredHeight());
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingTop = getPaddingTop();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
//      除内边距外的实际宽高
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
//      半径
        int radius = Math.min(width, height) / 2;
//      定位圆心坐标，圆画在中心区域
        final int rx = paddingLeft + width / 2;
        final int ry = paddingTop + height / 2;

        canvas.drawCircle(rx, ry, radius, mPaint);
    }
}
