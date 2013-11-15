package cn.hchaojie.snippets.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;

public class TestShadowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(new DrawCanvas(this));
    }
    
    class DrawCanvas extends View {
		public DrawCanvas(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			Paint p1 = new Paint();
			Paint p2 = new Paint();
			
			p1.setColor(0xffffff00);
			canvas.drawRect(30, 50, 130, 150, p1);
			
			p1.setShadowLayer(5, 3, 3, 0xffff00ff);
			canvas.drawRect(30, 200, 130, 300, p1);
			
			p2.setColor(0xffffff00);
			p2.setStyle(Style.STROKE);
			canvas.drawRect(200, 50, 300, 150, p2);
			
			p2.setShadowLayer(5, 3, 3, 0xffff00ff);
			canvas.drawRect(200, 200, 300, 300, p2);
		}
    }
}
