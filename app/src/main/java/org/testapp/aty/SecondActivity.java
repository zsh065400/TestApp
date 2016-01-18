package org.testapp.aty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.testapp.R;
import org.testapp.utils.MyConstants;
import org.testapp.utils.MyUtils;
import org.testapp.xu_lie_hua.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity {

	public static final String ACTION_1 = "org.testapp.t1";
	public static final String ACTION_2 = "org.testapp.t2";
	public static final String CATEGORY_1 = "org.testapp.category1";
	public static final String CATEGORY_2 = "org.testapp.category2";
	public static final String CATEGORY_3 = "org.testapp.category3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		recoverFromFile();
	}

	private void recoverFromFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				User user = null;
				File cacheFile = new File(MyConstants.CACHE_FILE_PATH);
				if (cacheFile.exists()) {
					ObjectInputStream objectInputStream = null;
					try {
						objectInputStream = new ObjectInputStream(new FileInputStream(cacheFile));
						user = (User) objectInputStream.readObject();
						Log.d("SeondActivity", "recover user :" + user);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} finally {
						MyUtils.close(objectInputStream);
					}
				}
			}
		}).start();
	}
}
