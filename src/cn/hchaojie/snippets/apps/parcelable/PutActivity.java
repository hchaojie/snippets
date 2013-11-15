package cn.hchaojie.snippets.apps.parcelable;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import cn.hchaojie.snippets.R;

/*
 * check the issue at:
 * http://stackoverflow.com/questions/12882833/why-can-i-successfully-pass-a-parcelable-but-not-use-putextra-getstringextra
 */
public class PutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_activity);
    }

    public void performGoto(View view) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
                Intent intent = new Intent(PutActivity.this, GetActivity.class);
                intent.putExtra(GetActivity.TEST_EXTRA, "testString");
                startActivity(intent);
            }
        }.execute();
    }
}
