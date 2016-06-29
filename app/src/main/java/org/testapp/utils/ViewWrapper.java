package org.testapp.utils;

import android.view.View;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class ViewWrapper {

    private View mTarget;

    public ViewWrapper(View target) {
        mTarget = target;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }
}
