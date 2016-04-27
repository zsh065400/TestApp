package org.testapp.aty;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import practice.other.JsonPractice;

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
        setContentView(R.layout.activity_test);
        JsonPractice.buildJson();
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
