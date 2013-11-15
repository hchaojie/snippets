package cn.hchaojie.snippets.view.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ShadowFrameLayout extends FrameLayout {
	private Paint mPaint;
	
	public ShadowFrameLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShadowFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	    mPaint = new Paint();
	    mPaint.setAntiAlias(true);
	    mPaint.setColor(Color.DKGRAY);
	    mPaint.setShadowLayer(5f, 5f, 5f, Color.GRAY);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    canvas.drawRect(15f, 15f, getWidth() - 15f, getHeight() - 15f, mPaint);
	    super.onDraw(canvas);
	}
}