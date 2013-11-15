package cn.hchaojie.snippets.view.graphic;

import cn.hchaojie.util.DensityUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ChromeHeader extends FrameLayout {
	private static final int CORNER_WIDTH = 20;
	private static final int BG_COLOR = 0xfff0f0f0;
	private static final int SHADOW_COLOR = 0xffcccccc;
	private static final int SHADOW_RADIUS = 5;
	private static final int SHADOW_DISTENCE = 4;
	int mBackground;
	
	public ChromeHeader(Context context) {
		this(context, null);
	}
	
	public ChromeHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setColor(0xff00ff00);
		
		paint.setShadowLayer(SHADOW_RADIUS, - SHADOW_DISTENCE, 0, SHADOW_COLOR);
		paint.setAntiAlias(true);
		
		int w = getWidth();
		int h = getHeight();
		float dx = DensityUtil.dip2px(getContext(), CORNER_WIDTH);
		
		Path p = new Path();
		p.moveTo(0.0f, (float) h);
		p.cubicTo(dx, (float)h, dx, 0.0f, dx * 2, 0.0f);
		p.lineTo(w - dx * 2, 0.0f);
		p.cubicTo(w - dx, 0, w - dx, h, (float) w, (float) h);
		p.close();
		canvas.drawPath(p, paint);
	}
}
