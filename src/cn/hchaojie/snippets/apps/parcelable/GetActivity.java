package cn.hchaojie.snippets.apps.parcelable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import cn.hchaojie.snippets.R;

public class GetActivity extends Activity {
    public static final String TEST_EXTRA = "package_name.DisplayResponse.response";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);
        
        Log.d("PARCELABLE TEST", "CONTAINS EXTRA KEY?: " + getIntent().getExtras().containsKey(TEST_EXTRA));
        Log.d("PARCELABLE TEST", "EXTRA IS:" + getIntent().getStringExtra(TEST_EXTRA));
    }
}
