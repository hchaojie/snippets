package cn.hchaojie.snippets.view.graphic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class TestChromeHeader extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
    	 FrameLayout layout = new FrameLayout(this);
    	 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	 layout.setLayoutParams(params);
    	 layout.setBackgroundColor(0xffffffff);
    	 FrameLayout.LayoutParams p2 = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 50);
    	 layout.addView(new ChromeHeader(this), p2);
    	 
    	 setContentView(layout);
    }
}