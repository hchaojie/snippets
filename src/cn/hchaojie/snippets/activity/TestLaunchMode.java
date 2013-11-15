package cn.hchaojie.snippets.activity;

import cn.hchaojie.snippets.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * see 
 * http://blog.csdn.net/liuhe688/article/details/6754323
 *
 */
public class TestLaunchMode extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_mode);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(this.toString());
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TestLaunchMode.this, TestLaunchMode.class);
		    	startActivity(intent);
			}
		});
        
    }
}