package org.testapp.impl;

import android.os.RemoteException;

import org.testapp.aidl.ICompute;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/23 17:12
 * 修改人：Administrator
 * 修改时间：2016/1/23 17:12
 * 修改备注：
 */
public class ComputeImpl extends ICompute.Stub {
	@Override
	public int add(int a, int b) throws RemoteException {
		return a + b;
	}


}
