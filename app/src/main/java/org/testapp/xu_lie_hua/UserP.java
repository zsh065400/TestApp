package org.testapp.xu_lie_hua;

import android.os.Parcel;
import android.os.Parcelable;

import org.testapp.aidl.Book;

/**
 * 项目名称：TestApp
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/1/13 20:49
 * 修改人：Administrator
 * 修改时间：2016/1/13 20:49
 * 修改备注：
 */
public class UserP implements Parcelable {
	public int userId;
	public String userName;
	public boolean isMale;

	public Book book;

	public UserP(int userId, String userName, boolean isMale) {
		this.userId = userId;
		this.userName = userName;
		this.isMale = isMale;
	}

	/*
	* 返回当前对象的内容描述，如果含有文件描述符，返回1
	* 否则返回0，几乎所有情况都返回0
	* 标记位：CONTENTS_FILE_DESCRIPTOR
	*
	* */
	@Override
	public int describeContents() {
		return 0;
	}

	/*
	* 将当前对象写入序列化结构中，其中flags标识有两种值，0或1,
	* 为1时标识当前对象需要作为返回值返回，不能立即释放资源
	* 几乎所有情况都为0
	* 标记位：PARCELABLE_WRITE_RETURN_VALUE
	* */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(userId);
		dest.writeString(userName);
		dest.writeInt(isMale ? 1 : 0);
		dest.writeParcelable(book, 0);
	}

	public static final Parcelable.Creator<UserP> CREATOR = new Creator<UserP>() {

		/*
		 * 从序列化后的对象中创建原始对象
		 * @param source
		 * @return
		 */
		@Override
		public UserP createFromParcel(Parcel source) {
			return new UserP(source);
		}

		/*
		* 创建指定长度的原始对象数组
		* */
		@Override
		public UserP[] newArray(int size) {
			return new UserP[size];
		}
	};

	/*从序列化后的对象中创建原始对象*/
	private UserP(Parcel in) {
		userId = in.readInt();
		userName = in.readString();
		isMale = in.readInt() == 1;
		book = in.readParcelable(Thread.currentThread().getContextClassLoader());
	}
}
