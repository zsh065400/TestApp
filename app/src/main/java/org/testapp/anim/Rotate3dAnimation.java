package org.testapp.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author 赵树豪
 * @version 1.0
 */
public class Rotate3dAnimation extends Animation {

    /*
    * final 修饰的变量必须在其需要的时候完成初始化
    *
    * 例如无static修饰的，需要在对象初始化时完成初始化
    * static修饰的需要在静态代码块中完成初始化
    *
    * */
    private final float mFromDegress;
    private final float mToDegress;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    public Rotate3dAnimation(float fromDegress,
                             float toDegress,
                             float centerX,
                             float centerY,
                             float depthZ,
                             boolean reverse) {
        mFromDegress = fromDegress;
        mToDegress = toDegress;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegress = mFromDegress;
        float degress = fromDegress + ((mToDegress - fromDegress) *
                interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;

        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();

        camera.save();

        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degress);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
