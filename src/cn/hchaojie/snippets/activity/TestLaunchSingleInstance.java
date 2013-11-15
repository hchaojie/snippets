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
public class TestLaunchSingleInstance extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_mode_single_instance);
        
        TextView textView = (TextView) findViewById(R.id.actInfo);
        textView.setText(this.toString());
        
        TextView textView2 = (TextView) findViewById(R.id.taskInfo);
        textView2.setText("task id:" + this.getTaskId());
        
        Button button = (Button) findViewById(R.id.button);
        button.setText("go to activity 2(single instance)");
        button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TestLaunchSingleInstance.this, SingleInstanceActivity.class);
		    	startActivity(intent);
			}
		});
        
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setText("go to activity 3 (single instance)");
        button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TestLaunchSingleInstance.this, SingleInstanceActivity2.class);
		    	startActivity(intent);
			}
		});
    }
}