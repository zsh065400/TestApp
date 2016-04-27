package practice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.testapp.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public void test() {
        try {
            URL url = new URL("www.baidu.com");
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            final URLConnection urlConnection =  url.openConnection();
//            post请求，要先写后读
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setReadTimeout(10000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("user_name", "1");
            urlConnection.connect();
            final OutputStream outputStream = urlConnection.getOutputStream();
            final BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(outputStream));
            bufferedWriter.write("测试");
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
