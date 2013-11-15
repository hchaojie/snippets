package cn.hchaojie.snippets.event;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class TestInterceptTouchEvent extends Activity {
	public static final String TAG = "TestTouchEventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FrameLayout frame = new FrameLayout(this);
		frame.addView(new MyViewGroup(this), new FrameLayout.LayoutParams(400, 400));
		setContentView(frame);
    }
}