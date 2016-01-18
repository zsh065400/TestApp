package org.testapp.aty;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.testapp.R;
import org.testapp.proider.BookProvider;

public class ProviderActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_provider);
		Uri uri = Uri.parse("content://" + BookProvider.AUTHORITIES);
		getContentResolver().query(uri, null, null, null, null);
		getContentResolver().query(uri, null, null, null, null);
		getContentResolver().query(uri, null, null, null, null);
	}
}
