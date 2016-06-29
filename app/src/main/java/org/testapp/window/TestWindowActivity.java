package org.testapp.window;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class TestWindowActivity extends AppCompatActivity {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private Button mWindowButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWindowWidth = getWindowManager().getDefaultDisplay().getWidth();
    }

    private int mWindowWidth;

    @Override
    protected void onResume() {
        super.onResume();
        mWindowButton = new Button(this);
        mWindowButton.setText("测试");

        mWindowButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setWindowPosition(rawX, rawY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        setWindowPosition(rawX, rawY);
                        break;

                    case MotionEvent.ACTION_UP:
                        if (rawX <= mWindowWidth / 2) {
                            rawX = 0;
                        } else {
                            rawX = mWindowWidth;
                        }
                        setWindowPosition(rawX, rawY);
                        break;
                    default:

                        break;
                }
                return false;
            }
        });

        mWindowManager = getWindowManager();

        mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT, 0, PixelFormat.TRANSPARENT);
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mWindowManager.addView(mWindowButton, mLayoutParams);
    }

    private void setWindowPosition(int rawX, int rawY) {
        int btnWidht = mWindowButton.getLayoutParams().width;
        int btnHeight = mWindowButton.getLayoutParams().height;
        // TODO: 2016/6/29 解决滑动时中心点的问题
        mLayoutParams.x = rawX;
        mLayoutParams.y = rawY;
        mWindowManager.updateViewLayout(mWindowButton, mLayoutParams);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }
}
