package practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.testapp.R;
import practice.view.SwipeBackFrameLayout;

/**
 * @author：Administrator
 * @version:1.0
 */
public class PracticeActivity extends AppCompatActivity {

	private SwipeBackFrameLayout mSwipeBack;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practice);
		mSwipeBack = (SwipeBackFrameLayout) findViewById(R.id.swipe_back);
		mSwipeBack.setCallBack(new SwipeBackFrameLayout.CallBack() {
			@Override
			public void onShouldFinish() {
				finish();
				overridePendingTransition(R.anim.no_anim, R.anim.out_to_right);
			}
		});
	}
}
