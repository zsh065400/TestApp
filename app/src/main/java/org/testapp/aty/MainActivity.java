package org.testapp.aty;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.testapp.R;
import org.testapp.aidl.IBookManager;
import org.testapp.utils.MyConstants;
import org.testapp.utils.MyUtils;
import org.testapp.view.Scroller;
import org.testapp.xu_lie_hua.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {
    private IBookManager mService;

    private volatile static MainActivity sInstance = null;

    public static final MainActivity getInstance() {
        MainActivity instance = sInstance;
        if (instance == null) {
            synchronized (MainActivity.class) {
                //避免延迟赋值
                instance = sInstance;
                if (instance == null) {
                    sInstance = new MainActivity();
                    instance = sInstance;
                }
            }
        }
        return instance;
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mService == null) {
                return;
            }

            mService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mService = null;

            //这里重新绑定远程Service
        }
    };

    private ServiceConnection scnn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public static final String TAG = "MainActivity";
    private TextView textView;

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;

    private int mCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL_TO:
                    mCount++;
                    if (mCount <= FRAME_COUNT) {
                        Log.d(TAG, "handleMessage: execute scroll");
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 1000);
                        mScroller.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private Scroller mScroller;
    TextView tv;
    private int size = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
        setContentView(R.layout.activity_main);


        testAnim();


//        JsonPractice.buildJson();

//        tv = (TextView) findViewById(R.id.t);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv.setTextSize(size += 10);
//            }
//        });
//		mScroller = (Scroller) findViewById(R.id.sl);
//		mHandler.sendEmptyMessage(MESSAGE_SCROLL_TO);
//		persistToFile();
//
//		findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
////				intent.setAction(SecondActivity.ACTION_1);
////				intent.setAction(SecondActivity.ACTION_2);
////				intent.setAction(SecondActivity.ACTION_3);
////				intent.addCategory(SecondActivity.CATEGORY_1);
////				intent.addCategory(SecondActivity.CATEGORY_2);
////				intent.addCategory(SecondActivity.CATEGORY_2);
////				intent.addCategory(SecondActivity.CATEGORY_3);
//				startActivity(intent);
//			}
//		});

    }

    private void testAnim() {
        Button btnAnim = (Button) findViewById(R.id.id_btn_anim);
        final Animation animation =
                AnimationUtils.loadAnimation(this, R.anim.animation_test);
        btnAnim.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        btnAnim.startAnimation(alphaAnimation);

        /*
        * LayoutAnimation
        * */

        ListView lv = new ListView(this);
        final Animation test = AnimationUtils.loadAnimation(this, R.anim.animation_test);
        final LayoutAnimationController controller = new LayoutAnimationController(test);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        lv.setLayoutAnimation(controller);


        /*
        *
        * */

        final int start = 0;
        final int end = 0;
        final View view = new View(this);
        final IntEvaluator evaluator = new IntEvaluator();
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final Object value = animation.getAnimatedValue();
                Log.d(TAG, "onAnimationUpdate: current progress----->" + value);
                final float fraction = animation.getAnimatedFraction();
                final int current = evaluator.evaluate(fraction, start, end);
                view.getLayoutParams().width = current;
                view.requestLayout();
            }
        });
        valueAnimator.setDuration(5000).start();
    }


    private void propertyAnimtor() {
        ObjectAnimator.ofFloat(null, "translationY", 2400f).start();

        final ObjectAnimator colorAnim = ObjectAnimator.ofInt(null, "backgroundColor", 0xFFFF8080, 0xFF8080FF);
        colorAnim.setDuration(3000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(null, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(null, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(null, "rotation", 0, -90),
                ObjectAnimator.ofFloat(null, "translationX", 0, 90),
                ObjectAnimator.ofFloat(null, "translationY", 0, 90),
                ObjectAnimator.ofFloat(null, "scaleX", 0, 1.5f),
                ObjectAnimator.ofFloat(null, "scaleY", 0, 0.5f),
                ObjectAnimator.ofFloat(null, "alphaX", 0, 0.25f, 1));
        set.setDuration(5 * 1000).start();


        final Animator animator = AnimatorInflater.loadAnimator(this, R.animator.property_animator);
        animator.setTarget(null);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    private void notfiyNormal() {
        Notification n = new Notification();
        n.tickerText = "点击重启";
        n.icon = R.mipmap.ic_launcher;
        n.when = System.currentTimeMillis();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        n.contentIntent = pi;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n);
    }

    private void notifyCustom() {
        Notification n = new Notification();
        n.tickerText = "点击重启";
        n.icon = R.mipmap.ic_launcher;
        n.when = System.currentTimeMillis();
        n.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews rvs = new RemoteViews(getPackageName(), R.layout.activity_main);
        rvs.setTextViewText(R.id.rootView, "adsda");
        PendingIntent restart = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        rvs.setOnClickPendingIntent(R.id.rootView, restart);
        n.contentView = rvs;
        n.contentIntent = pi;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, n);

    }


    private void persistToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(1, "hello world", false);
                File dir = new File(MyConstants.CHAPTER_2_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    //本地持久化存储对象，限制为Serializable
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(cacheFile));
                    objectOutputStream.writeObject(user);
                    Log.d(TAG, "persist user :" + user);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    MyUtils.close(objectOutputStream);
                }
            }
        }).start();
    }


}
