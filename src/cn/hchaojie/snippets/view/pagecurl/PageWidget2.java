package cn.hchaojie.snippets.view.pagecurl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class PageWidget2 extends View {
	PageCalculator mCalc;
	PointF mTouch = new PointF();
	
	public PageWidget2(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		
		if (mCalc == null) {
			mCalc = new PageCalculator(width, height);
		}
		
		mCalc.setTouchPoint(mTouch);
		
		PointF pLTCorner = new PointF(0, 0);			// left top corner point
		PointF pLBCorner = new PointF(0, height);		// left bottom
		PointF pRTCorner = new PointF(width, 0);		// right top
		PointF pRBCorner = new PointF(width, height);	// right bottom
		
		drawCorner(pLTCorner, canvas);
		drawCorner(pLBCorner, canvas);
		drawCorner(pRTCorner, canvas);
		drawCorner(pRBCorner, canvas);
	}
	
	private void drawCorner(PointF pCorner, Canvas canvas) {
		PointF[] pCtrls = PageCalculator.getBezierControls(mTouch, pCorner);
		PointF[] pStarts = PageCalculator.getBezierStarts(pCorner, pCtrls);
		
		PointF pLCtrl = pCtrls[0], pRCtrl = pCtrls[1];
		PointF pLStart = pStarts[0], pRStart = pStarts[1];
		PointF pLEnd = PageCalculator.getMiddlePoint(mTouch, pLCtrl);
		PointF pREnd = PageCalculator.getMiddlePoint(mTouch, pRCtrl);
		
		Path p = new Path();
		p.reset();
		p.moveTo(pLStart.x, pLStart.y);
		p.quadTo(pLCtrl.x, pLCtrl.y, pLEnd.x, pLEnd.y);
		p.lineTo(mTouch.x, mTouch.y);
		p.lineTo(pREnd.x, pREnd.y);
		p.quadTo(pRCtrl.x, pRCtrl.y, pRStart.x, pRStart.y);
		p.lineTo(pCorner.x, pCorner.y);
		p.close();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
        paint.setStrokeWidth(6);
        paint.setStyle(Style.STROKE);
        
        int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff};
        int d = PageCalculator.getCornerPosition(pCorner);
        paint.setColor(colors[d]);
        
		canvas.save();
		canvas.drawPath(p, paint);
		canvas.restore();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			this.postInvalidate();
		}
		
		return true;
	}
}
