package org.testapp.xu_lie_hua;

import org.testapp.aidl.Book;

import java.io.Serializable;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/13 20:49
 * 修改人：Administrator
 * 修改时间：2016/1/13 20:49
 * 修改备注：
 */
public class User implements Serializable {
	public static final Long serialVersionUID = 1L;
	public int userId;
	public String userName;
	public boolean isMale;

	public Book book;

	public User(int userId, String userName, boolean isMale) {
		this.userId = userId;
		this.userName = userName;
		this.isMale = isMale;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", userName='" + userName + '\'' +
				", isMale=" + isMale +
				", book=" + book +
				'}';
	}
}
