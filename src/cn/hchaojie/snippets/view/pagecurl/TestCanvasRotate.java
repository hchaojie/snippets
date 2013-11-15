package cn.hchaojie.snippets.view.pagecurl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class TestCanvasRotate extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
    	 FrameLayout layout = new FrameLayout(this);
    	 FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	 layout.setLayoutParams(params);
    	 layout.addView(new MyView(this), params);
    	 
    	 setContentView(layout);
    }
}

class MyView extends View {
	public MyView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		GradientDrawable shadow = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, new int[] { 0xff111111, 0xcc111111});
		
		shadow.setBounds(200, 200, 300, 500);
		shadow.draw(canvas);
		
		canvas.rotate(30, 200, 500);
		shadow.draw(canvas);
	}
}
