package sf.hmg.turntest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class PageWidget extends View {
	/** SWIPE_START_AREA **/
	public static final int LEFT_TOP 	= 0;
	public static final int LEFT_BOTTOM 	= 1;
	public static final int RIGHT_TOP 	= 2;
	public static final int RIGHT_BOTTOM = 3;
	
	public static final int SWIPE_LEFT = 0;
	public static final int SWIPE_UP = 1; 
	
	private int mWidth = 480;
	private int mHeight = 800;
	private Path mPath0;
	private Path mPath1;
	Bitmap mCurPageBitmap = null; // 当前页
	Bitmap mNextPageBitmap = null;
	
	// drag from which corner(could be left-top, left-bottom, right-top, right-bottom)
	PointF mCorner	= new PointF(0, 0);	
	int mSwipeStart = LEFT_TOP;

	PointF mTouch	= new PointF(); // 拖拽点
	PointF mLStart 	= new PointF(); // 左侧贝塞尔曲线起始点
	PointF mLCtrl 	= new PointF(); // 左侧贝塞尔曲线控制点
	PointF mLVertex	= new PointF(); // 左侧贝塞尔曲线顶点
	PointF mLEnd 	= new PointF(); // 左侧贝塞尔曲线结束点

	PointF mRStart 	= new PointF(); // 右侧贝塞尔曲线
	PointF mRCtrl 	= new PointF();
	PointF mRVertex = new PointF();
	PointF mREnd 	= new PointF();
	
	float mDegrees;
	ColorMatrixColorFilter mColorMatrixFilter;
	Matrix mMatrix;
	float[] mMatrixArray = { 0, 0, 0, 0, 0, 0, 0, 0, 1.0f };

	boolean mIsRTandLB; // 是否属于右上左下
	float mMaxLength = (float) Math.hypot(mWidth, mHeight);
	int[] mBackShadowColors;
	int[] mFrontShadowColors;
	GradientDrawable mBackShadowDrawableLR;
	GradientDrawable mBackShadowDrawableRL;
	GradientDrawable mFolderShadowDrawableLR;
	GradientDrawable mFolderShadowDrawableRL;

	GradientDrawable mFrontShadowDrawableHBT;
	GradientDrawable mFrontShadowDrawableHTB;
	GradientDrawable mFrontShadowDrawableVLR;
	GradientDrawable mFrontShadowDrawableVRL;

	Paint mPaint;

	Scroller mScroller;

	public PageWidget(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mPath0 = new Path();
		mPath1 = new Path();
		createDrawable();

		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 0.2f, 0 };
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mMatrix = new Matrix();
		mScroller = new Scroller(getContext());

		mTouch.x = 0.01f; // 不让x,y为0,否则在点计算时会有问题
		mTouch.y = 0.01f;
	}

	public void calcCornerPoint(float x, float y) {
		mCorner.x = (x <= mWidth / 2) ? 0 : mWidth;
		mCorner.y = (y <= mHeight / 2) ? 0 : mHeight;
		
		int hMask = (mCorner.x == 0) ? 0 : 2;
		int vMask = (mCorner.y ==0 ) ? 0 : 1;
		
		mSwipeStart = 3 & (hMask + vMask);
		mIsRTandLB = (mSwipeStart == RIGHT_TOP || mSwipeStart == LEFT_BOTTOM);
	}

	public boolean doTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
			
			//TODO
			this.postInvalidate();
			// calcCornerXY(mTouch.x, mTouch.y);
			// this.postInvalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (canDragOver()) {
				startAnimation(2400);
			} else {
				mTouch.x = mCorner.x - 0.09f;
				mTouch.y = mCorner.y - 0.09f;
			}

			this.postInvalidate();
		}
		// return super.onTouchEvent(event);
		return true;
	}

	private void calcPoints() {
		PointF pTouch = mTouch;
		PointF pCorner = mCorner;
		
		// calculate left bezier control 
		PointF pLeftCtrl = getLeftBezierControlPoint(pTouch, pCorner);
		
		// calculate left bezier start point
		PointF pLeftStart = getLeftBezierStartPoint(pCorner, pLeftCtrl);

		// check whether start point exceeds screen
		//
		// bezier start x may < 0 (when flipping right-to-left)
		// or x may > SCREEN_WIDTH (when flipping left-to-right)
		// so if bezier start point exceeds screen size, just scale it fit screen.
		if (pTouch.x > 0 && pTouch.x < mWidth) {
			// this will happen when swiping from top-left or bottom-left corner
			if (pLeftStart.x < 0) {
				float scale = mWidth / (mWidth - pLeftStart.x);
				pLeftStart.x = 0;	

				// re-calculate left control point
				pLeftCtrl.x = (2 * pLeftStart.x + pCorner.x) / 3;
				pLeftCtrl.y = pCorner.y;
				
				// re-calculate touch point
				// (corner.x - touchNew.x) / (corner.x - touchOld.x) = scale
				// (corner.y - touchNew.y) / (corner.y - touchOld.y) = scale
				pTouch.x = pCorner.x - scale * (pCorner.x - pTouch.x);
				pTouch.y = pCorner.y - scale * (pCorner.y - pTouch.y);

			}
			
			// this will happen when swiping from top-right or bottom-right corner
			if (mLStart.x > mWidth) {
				
			}
		}
		
		// calculate left bezier end point
		PointF pLeftEnd = new PointF();
		pLeftEnd.x = (pTouch.x + pLeftCtrl.x) / 2;
		pLeftEnd.y = (pTouch.y + pLeftCtrl.y) / 2;
		
		// calculate left bezier vertex
		// ((p0.x + p2.x) / 2 + p1.x) / 2
		// or (p0.x + 2 * p1.x + p2.x) / 4
		PointF pLeftVertex = new PointF();
		pLeftVertex.x = (pLeftStart.x + 2 * pLeftCtrl.x + pLeftEnd.x) / 4;
		pLeftVertex.y = (2 * pLeftCtrl.y + pLeftStart.y + pLeftEnd.y) / 4;
		
		// calculate right bezier control point
		PointF pRightCtrl = getRightBezierControlPoint(pTouch, pCorner);
		
		// calculate right bezier start point
		PointF pRightStart = getRightBezierStartPoint(pCorner, pRightCtrl);

		// calculate right bezier end point
		PointF pRightEnd = new PointF();
		pRightEnd.x = (pTouch.x + pRightCtrl.x) / 2;
		pRightEnd.y = (pTouch.y + pRightCtrl.y) / 2;
		
		// calculate right bezier vertex
		PointF pRightVertex = new PointF();
		pRightVertex.x = (pRightStart.x + 2 * pRightCtrl.x + pRightEnd.x) / 4;
		pRightVertex.y = (2 * pRightCtrl.y + pRightStart.y + pRightEnd.y) / 4;
		
		mTouch = pTouch;
		mLStart = pLeftStart;
		mLEnd = pLeftEnd;
		mLCtrl = pLeftCtrl;
		mLVertex = pLeftVertex;
		mRStart = pRightStart;
		mREnd = pRightEnd;
		mRVertex = pRightVertex;
		mRCtrl = pRightCtrl;
	}

	/**
	 * calculate start point by corner point and control point
	 * 
	 * Corner.x - Control.x = 2 * (Control.x - Start.x)
	 */
	private PointF getLeftBezierStartPoint(PointF pCorner, PointF pCtrl) {
		PointF pStart = new PointF();
		
		pStart.x = pCtrl.x - (pCorner.x - pCtrl.x) / 2;
		pStart.y = pCorner.y;

		return pStart;
	}
	
	private PointF getRightBezierStartPoint(PointF pCorner, PointF pCtrl) {
		PointF p = new PointF();
		
		p.x = pCorner.x;
		p.y = pCtrl.y - (pCorner.y - pCtrl.y) / 2;

		return p;	
	}
	
	/**
	 * calculate control point by touch point and corner point
	 * 
	 * right-angled triangle: pMiddle(M), pCorner(C), pControl(Control))
	 * base on formula: (C.y - M.y) * (C.y - M.y) = (C.x - M.x) * (M.x - Control.x)
	 */
	private PointF getLeftBezierControlPoint(PointF pTouch, PointF pCorner) {
		PointF pMid = PageCaculator.getMiddlePoint(pTouch, pCorner);
		
		PointF pCtrl = new PointF();
		
		pCtrl.x = (pCorner.y - pMid.y) * (pCorner.y - pMid.y) / (pCorner.x - pMid.x);
		pCtrl.y = pCorner.y;
		
		return pCtrl;
	}
	
	private PointF getRightBezierControlPoint(PointF pTouch, PointF pCorner) {
		PointF pMid = PageCaculator.getMiddlePoint(pTouch, pCorner);
		
		PointF pCtrl = new PointF();
		pCtrl.x = pCorner.x;
		pCtrl.y = pMid.y - (pCorner.x - pMid.x) * (pCorner.x - pMid.x) / (pCorner.y - pMid.y);
		
		return pCtrl;
	}

	private void drawCurrentPageArea(Canvas canvas, Bitmap bitmap, Path path) {
		mPath0.reset();
		mPath0.moveTo(mLStart.x, mLStart.y);
		mPath0.quadTo(mLCtrl.x, mLCtrl.y, mLEnd.x, mLEnd.y);
		mPath0.lineTo(mTouch.x, mTouch.y);
		mPath0.lineTo(mREnd.x, mREnd.y);
		mPath0.quadTo(mRCtrl.x, mRCtrl.y, mRStart.x, mRStart.y);
		mPath0.lineTo(mCorner.x, mCorner.y);
		mPath0.close();

		canvas.save();
		canvas.clipPath(path, Region.Op.XOR);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.restore();
	}

	private void drawNextPageAreaAndShadow(Canvas canvas, Bitmap bitmap) {
		mPath1.reset();
		mPath1.moveTo(mLStart.x, mLStart.y);
		mPath1.lineTo(mLVertex.x, mLVertex.y);
		mPath1.lineTo(mRVertex.x, mRVertex.y);
		mPath1.lineTo(mRStart.x, mRStart.y);
		mPath1.lineTo(mCorner.x, mCorner.y);
		mPath1.close();

		mDegrees = (float) Math.toDegrees(Math.atan2(mLCtrl.x - mCorner.x, mRCtrl.y - mCorner.y));
		int leftx;
		int rightx;
		GradientDrawable shadow;
		
		float touchToCornerDis = (float) Math.hypot((mTouch.x - mCorner.x), (mTouch.y - mCorner.y));
		if (mIsRTandLB) {
			leftx = (int) (mLStart.x);
			rightx = (int) (mLStart.x + touchToCornerDis / 4);
			shadow = mBackShadowDrawableLR;
		} else {
			leftx = (int) (mLStart.x - touchToCornerDis / 4);
			rightx = (int) mLStart.x;
			shadow = mBackShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		canvas.drawBitmap(bitmap, 0, 0, null);
		shadow.setBounds(leftx, (int) mLStart.y, rightx, (int) (mMaxLength + mLStart.y));
		canvas.rotate(mDegrees, mLStart.x, mLStart.y);
		shadow.draw(canvas);
		canvas.restore();
	}

	public void setBitmaps(Bitmap bm1, Bitmap bm2) {
		mCurPageBitmap = bm1;
		mNextPageBitmap = bm2;
	}

	public void setScreen(int w, int h) {
		mWidth = w;
		mHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFAAAAAA);
		calcPoints();
		drawCurrentPageArea(canvas, mCurPageBitmap, mPath0);
		drawNextPageAreaAndShadow(canvas, mNextPageBitmap);
		drawCurrentPageShadow(canvas);
		drawCurrentBackArea(canvas, mCurPageBitmap);
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 创建阴影的GradientDrawable
	 */
	private void createDrawable() {
		int[] color = { 0x333333, 0xb0333333 };
		mFolderShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, color);
		mFolderShadowDrawableRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFolderShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, color);
		mFolderShadowDrawableLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowColors = new int[] { 0xff111111, 0xcc111111 };
		mBackShadowDrawableRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors);
		mBackShadowDrawableRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mBackShadowDrawableLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors);
		mBackShadowDrawableLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowColors = new int[] { 0x80111111, 0x111111 };
		mFrontShadowDrawableVLR = new GradientDrawable(
				GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors);
		mFrontShadowDrawableVLR
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		mFrontShadowDrawableVRL = new GradientDrawable(
				GradientDrawable.Orientation.RIGHT_LEFT, mFrontShadowColors);
		mFrontShadowDrawableVRL
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHTB = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors);
		mFrontShadowDrawableHTB
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		mFrontShadowDrawableHBT = new GradientDrawable(
				GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors);
		mFrontShadowDrawableHBT
				.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 绘制翻起页的阴影
	 */
	public void drawCurrentPageShadow(Canvas canvas) {
		double degree;
		if (mIsRTandLB) {
			degree = Math.PI / 4 - Math.atan2(mLCtrl.y - mTouch.y, mTouch.x - mLCtrl.x);
		} else {
			degree = Math.PI / 4 - Math.atan2(mTouch.y - mLCtrl.y, mTouch.x - mLCtrl.x);
		}
		// 翻起页阴影顶点与touch点的距离
		double d1 = (float) 25 * 1.414 * Math.cos(degree);
		double d2 = (float) 25 * 1.414 * Math.sin(degree);
		float x = (float) (mTouch.x + d1);
		float y;
		if (mIsRTandLB) {
			y = (float) (mTouch.y + d2);
		} else {
			y = (float) (mTouch.y - d2);
		}
		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mLCtrl.x, mLCtrl.y);
		mPath1.lineTo(mLStart.x, mLStart.y);
		mPath1.close();
		float rotateDegrees;
		canvas.save();

		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		int leftx;
		int rightx;
		GradientDrawable mCurrentPageShadow;
		if (mIsRTandLB) {
			leftx = (int) (mLCtrl.x);
			rightx = (int) mLCtrl.x + 25;
			mCurrentPageShadow = mFrontShadowDrawableVLR;
		} else {
			leftx = (int) (mLCtrl.x - 25);
			rightx = (int) mLCtrl.x + 1;
			mCurrentPageShadow = mFrontShadowDrawableVRL;
		}

		rotateDegrees = (float) Math.toDegrees(Math.atan2(mTouch.x
				- mLCtrl.x, mLCtrl.y - mTouch.y));
		canvas.rotate(rotateDegrees, mLCtrl.x, mLCtrl.y);
		mCurrentPageShadow.setBounds(leftx,
				(int) (mLCtrl.y - mMaxLength), rightx,
				(int) (mLCtrl.y));
		mCurrentPageShadow.draw(canvas);
		canvas.restore();

		mPath1.reset();
		mPath1.moveTo(x, y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mRCtrl.x, mRCtrl.y);
		mPath1.lineTo(mRStart.x, mRStart.y);
		mPath1.close();
		canvas.save();
		canvas.clipPath(mPath0, Region.Op.XOR);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);
		if (mIsRTandLB) {
			leftx = (int) (mRCtrl.y);
			rightx = (int) (mRCtrl.y + 25);
			mCurrentPageShadow = mFrontShadowDrawableHTB;
		} else {
			leftx = (int) (mRCtrl.y - 25);
			rightx = (int) (mRCtrl.y + 1);
			mCurrentPageShadow = mFrontShadowDrawableHBT;
		}
		rotateDegrees = (float) Math.toDegrees(Math.atan2(mRCtrl.y
				- mTouch.y, mRCtrl.x - mTouch.x));
		canvas.rotate(rotateDegrees, mRCtrl.x, mRCtrl.y);
		float temp;
		if (mRCtrl.y < 0)
			temp = mRCtrl.y - mHeight;
		else
			temp = mRCtrl.y;

		int hmg = (int) Math.hypot(mRCtrl.x, temp);
		if (hmg > mMaxLength)
			mCurrentPageShadow.setBounds((int) (mRCtrl.x - 25) - hmg, leftx,
							(int) (mRCtrl.x + mMaxLength) - hmg, rightx);
		else
			mCurrentPageShadow.setBounds( (int) (mRCtrl.x - mMaxLength), leftx,
					(int) (mRCtrl.x), rightx);

		mCurrentPageShadow.draw(canvas);
		canvas.restore();
	}

	/**
	 * Author : hmg25 Version: 1.0 Description : 绘制翻起页背面
	 */
	private void drawCurrentBackArea(Canvas canvas, Bitmap bitmap) {
		int i = (int) (mLStart.x + mLCtrl.x) / 2;
		float f1 = Math.abs(i - mLCtrl.x);
		int i1 = (int) (mRStart.y + mRCtrl.y) / 2;
		float f2 = Math.abs(i1 - mRCtrl.y);
		float f3 = Math.min(f1, f2);
		mPath1.reset();
		mPath1.moveTo(mRVertex.x, mRVertex.y);
		mPath1.lineTo(mLVertex.x, mLVertex.y);
		mPath1.lineTo(mLEnd.x, mLEnd.y);
		mPath1.lineTo(mTouch.x, mTouch.y);
		mPath1.lineTo(mREnd.x, mREnd.y);
		mPath1.close();
		GradientDrawable mFolderShadowDrawable;
		int left;
		int right;
		if (mIsRTandLB) {
			left = (int) (mLStart.x - 1);
			right = (int) (mLStart.x + f3 + 1);
			mFolderShadowDrawable = mFolderShadowDrawableLR;
		} else {
			left = (int) (mLStart.x - f3 - 1);
			right = (int) (mLStart.x + 1);
			mFolderShadowDrawable = mFolderShadowDrawableRL;
		}
		canvas.save();
		canvas.clipPath(mPath0);
		canvas.clipPath(mPath1, Region.Op.INTERSECT);

		mPaint.setColorFilter(mColorMatrixFilter);

		float dis = (float) Math.hypot(mCorner.x - mLCtrl.x,
				mRCtrl.y - mCorner.y);
		float f8 = (mCorner.x - mLCtrl.x) / dis;
		float f9 = (mRCtrl.y - mCorner.y) / dis;
		mMatrixArray[0] = 1 - 2 * f9 * f9;
		mMatrixArray[1] = 2 * f8 * f9;
		mMatrixArray[3] = mMatrixArray[1];
		mMatrixArray[4] = 1 - 2 * f8 * f8;
		mMatrix.reset();
		mMatrix.setValues(mMatrixArray);
		mMatrix.preTranslate(-mLCtrl.x, -mLCtrl.y);
		mMatrix.postTranslate(mLCtrl.x, mLCtrl.y);
		canvas.drawBitmap(bitmap, mMatrix, mPaint);
		// canvas.drawBitmap(bitmap, mMatrix, null);
		mPaint.setColorFilter(null);
		canvas.rotate(mDegrees, mLStart.x, mLStart.y);
		mFolderShadowDrawable.setBounds(left, (int) mLStart.y, right,
				(int) (mLStart.y + mMaxLength));
		mFolderShadowDrawable.draw(canvas);
		canvas.restore();
	}

	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			float x = mScroller.getCurrX();
			float y = mScroller.getCurrY();
			mTouch.x = x;
			mTouch.y = y;
			postInvalidate();
		}
	}

	private void startAnimation(int delayMillis) {
		int dx, dy;
		// dx 水平方向滑动的距离，负值会使滚动向左滚动
		// dy 垂直方向滑动的距离，负值会使滚动向上滚动
		if (mCorner.x > 0) {
			dx = -(int) (mWidth + mTouch.x);
		} else {
			dx = (int) (mWidth - mTouch.x + mWidth);
		}
		if (mCorner.y > 0) {
			dy = (int) (mHeight - mTouch.y);
		} else {
			dy = (int) (1 - mTouch.y); // 防止mTouch.y最终变为0
		}
		mScroller.startScroll((int) mTouch.x, (int) mTouch.y, dx, dy,
				delayMillis);
	}

	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	public boolean canDragOver() {
		float touchToCornerDis = (float) Math.hypot((mTouch.x - mCorner.x), (mTouch.y - mCorner.y));
		return (touchToCornerDis > mWidth / 10);
	}

	public boolean isSwipeRight() {
		return (mSwipeStart == LEFT_TOP || mSwipeStart == LEFT_BOTTOM);
	}
}
