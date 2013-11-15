package cn.hchaojie.snippets.view.custom;

import android.app.Activity;
import android.os.Bundle;
import cn.hchaojie.snippets.R;

public class CustomProgressBar extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.custom_progress_bar);
    }
}
