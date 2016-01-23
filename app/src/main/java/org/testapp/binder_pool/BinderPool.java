package org.testapp.binder_pool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.testapp.aidl.IBinderPool;
import org.testapp.impl.ComputeImpl;
import org.testapp.impl.SecurityCenterImpl;
import org.testapp.service.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/23 17:15
 * 修改人：Administrator
 * 修改时间：2016/1/23 17:15
 * 修改备注：
 */
public class BinderPool {
	private static final String TAG = "BinderPool";
	public static final int BINDER_NONE = -1;
	public static final int BINDER_COMPUTE = 0;
	public static final int BINDER_SECURITY_CENTER = 1;

	private Context mContext;
	private IBinderPool mBinderPool;
	private static volatile BinderPool sInstance;
	private CountDownLatch mConnectBinderPoolCountDownLatch;

	private BinderPool(Context mContext) {
		this.mContext = mContext.getApplicationContext();
		connectBinderPoolService();
	}
	//提前进行初始化操作，以免业务模块调用时出现链接问题
	public static BinderPool getInstance(Context context) {
		if (sInstance == null) {
			synchronized (BinderPool.class) {
				if (sInstance == null) {
					sInstance = new BinderPool(context);
				}
			}
		}
		return sInstance;
	}

	private synchronized void connectBinderPoolService() {
		mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
		Intent service = new Intent(mContext, BinderPoolService.class);
		mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
		try {
			mConnectBinderPoolCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public IBinder queryBinder(int binderCode) {
		IBinder binder = null;
		try {
			if (mBinderPool != null) {
				binder = mBinderPool.queryBinder(binderCode);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return binder;
	}

	private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinderPool = IBinderPool.Stub.asInterface(service);
			try {
				mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mConnectBinderPoolCountDownLatch.countDown();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			//ignored
		}
	};

	private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			Log.w(TAG, "binder died.");
			mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
			mBinderPool = null;
			connectBinderPoolService();
		}
	};
	//可以单独提出
	public static class BinderPoolImpl extends IBinderPool.Stub {

		@Override
		public IBinder queryBinder(int binderCode) throws RemoteException {
			IBinder binder = null;
			switch (binderCode) {
				case BINDER_COMPUTE:
					binder = new ComputeImpl();
					break;
				case BINDER_SECURITY_CENTER:
					binder = new SecurityCenterImpl();
					break;
				default:
					break;
			}
			return binder;
		}
	}
}
