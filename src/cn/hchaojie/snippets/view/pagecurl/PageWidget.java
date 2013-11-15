package cn.hchaojie.snippets.view.pagecurl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class PageWidget extends View {
	PointF mCorner = new PointF();
	PointF mTouch = new PointF();
	PointF mMid = new PointF();
	PointF mBezier1Control = new PointF();
	PointF mBezier2Control = new PointF();
	
	public PageWidget(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		updatePoints();
		//canvas.drawColor(0xFFAAAAAA);
		
		Path path = new Path();
		path.moveTo(mTouch.x, mTouch.y);
		path.lineTo(mBezier2Control.x, mBezier2Control.y);
		path.lineTo(mCorner.x, mCorner.y);
		path.lineTo(mBezier1Control.x, mBezier1Control.y);
		path.close();
		
		Paint paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setColor(0xFF333333);
        
		canvas.drawPath(path, paint);
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
	
	private void updatePoints() {
		mCorner.x = this.getWidth();
		mCorner.y = this.getHeight();
		
		mMid.x = (mTouch.x + mCorner.x) / 2;
		mMid.y = (mTouch.y + mCorner.y) / 2;
		mBezier1Control.x = mMid.x - (mCorner.y - mMid.y) * (mCorner.y - mMid.y) / (mCorner.x - mMid.x);
		mBezier1Control.y = mCorner.y;
		mBezier2Control.x = mCorner.x;
		mBezier2Control.y = mMid.y - (mCorner.x - mMid.x) * (mCorner.x - mMid.x) / (mCorner.y - mMid.y);
	}
}
