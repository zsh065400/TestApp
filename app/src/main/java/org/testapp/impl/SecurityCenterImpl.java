package org.testapp.impl;

import android.os.RemoteException;

import org.testapp.aidl.ISecurityCenter;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/23 17:08
 * 修改人：Administrator
 * 修改时间：2016/1/23 17:08
 * 修改备注：
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {
	private static final char SECRET_CODE = '^';

	@Override
	public String encrypt(String content) throws RemoteException {
		char[] chars = content.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] ^= SECRET_CODE;
		}
		return new String(chars);
	}

	@Override
	public String decrypt(String password) throws RemoteException {
		return encrypt(password);
	}
}
