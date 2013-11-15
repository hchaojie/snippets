package cn.hchaojie.snippets.view.drawable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.hchaojie.snippets.R;

public class TestGradientBox extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_gradient_box);
    }

    public void nextClicked(View v) {
//        Intent intent = new Intent(this, TitlesDefault.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
    }
}
