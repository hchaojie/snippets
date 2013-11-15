package cn.hchaojie.snippets.view.pagecurl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class TestPageWidget2 extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
    	 FrameLayout layout = new FrameLayout(this);
    	 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	 layout.setLayoutParams(params);
    	 layout.addView(new PageWidget2(this), params);
    	 
    	 setContentView(layout);
    }
}
