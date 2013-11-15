package cn.hchaojie.snippets.view.pagecurl;

import android.graphics.PointF;

public class PageCalculator {
	public static final int LEFT_TOP 	= 0;
	public static final int LEFT_BOTTOM 	= 1;
	public static final int RIGHT_TOP 	= 2;
	public static final int RIGHT_BOTTOM = 3;
	
	private int mWidth;
	private int mHeight;
	
	// drag from which corner(could be left-top, left-bottom, right-top, right-bottom)
	PointF mCorner	= new PointF();	

	PointF mTouch	= new PointF(); // touch point
	
	// left bezier curve points
	PointF mLStart 	= new PointF(); // start point
	PointF mLCtrl 	= new PointF(); // control point
	PointF mLVertex	= new PointF(); // vertex
	PointF mLEnd 	= new PointF(); // end point

	// Right bezier curve points
	PointF mRStart 	= new PointF(); // start
	PointF mRCtrl 	= new PointF(); // control
	PointF mRVertex = new PointF();	// vertex
	PointF mREnd 	= new PointF();	// end
	
	public PageCalculator(int pageWidth, int pageHeigh) {
		this.mWidth = pageWidth;
		this.mHeight = pageHeigh;
	}
	
	public void setTouchPoint(PointF p) {
		this.mTouch = p;
		
		mCorner.x = (p.x <= mWidth / 2) ? 0 : mWidth;
		mCorner.y = (p.y <= mHeight / 2) ? 0 : mHeight;
	}
	
	public PointF getTouchPoint() {
		return this.mTouch;
	}
	
	/**
	 * Get intersection point of two line which is specified by each two points 
	 * formula y = kx + b
	 * a0x + b0y = c0
	 * a1x + b1y = c1
	 */
	public static PointF getIntersection(PointF p1, PointF p2, PointF q1, PointF q2) {
		float k1 = (p2.y - p1.y) / (p2.x - p1.x);
		float b1 = ((p1.x * p2.y) - (p2.x * p1.y)) / (p1.x - p2.x);

		float k2 = (q2.y - q1.y) / (q2.x - q1.x);
		float b2 = ((q1.x * q2.y) - (q2.x * q1.y)) / (q1.x - q2.x);
		
		PointF p = new PointF();
		p.x = (b2 - b1) / (k1 - k2);
		p.y = k1 * p.x + b1;
		return p;
	}
	
	/**
	 * get middle point of a line which specified by two points 
	 */
	public static PointF getMiddlePoint(PointF p1, PointF p2) {
		return new PointF((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
	/**
	 * get bezier control points by touch point and corner point
	 * 
	 * right triangle: pMiddle(M), pCorner(C), pControl(Control))
	 * base on formula: Math.pow(C.y - M.y, 2) = (C.x - M.x) * (M.x - Control.x)
	 * 
	 * @return the two control points of the bezier curve
	 * 		where PointF[0] is the left one, and PointF[1] is the right one 
	 */
	public static PointF[] getBezierControls(PointF pTouch, PointF pCorner) {
		PointF pMid = getMiddlePoint(pTouch, pCorner);
		
		PointF pCtrl1 = new PointF();
		pCtrl1.x = (float)Math.pow(pCorner.y - pMid.y, 2) / (pMid.x - pCorner.x) + pMid.x;
		pCtrl1.y = pCorner.y;
		
		PointF pCtrl2 = new PointF();
		pCtrl2.x = pCorner.x;
		pCtrl2.y = (float) Math.pow(pMid.x - pCorner.x, 2) / (pMid.y - pCorner.y) + pMid.y;
		
		int cornerPosition = getCornerPosition(pCorner);
		if (cornerPosition == RIGHT_TOP || cornerPosition == RIGHT_BOTTOM) {
			return new PointF[] {pCtrl1, pCtrl2};
		}
		
		if (cornerPosition == LEFT_TOP || cornerPosition == LEFT_BOTTOM) {
			return new PointF[] {pCtrl2, pCtrl1};
		}
		
		return null;
	}
	
	/**
	 * calculate start points by corner point and control point(left and right)
	 * 
	 * Corner.x - Control.x = 2 * (Control.x - Start.x)
	 * 
	 * @return PointF[] {left, right}
	 */
	public static PointF[] getBezierStarts(PointF pCorner, PointF pCtrls[]) {
		PointF p0 = new PointF();
		PointF p1 = new PointF();
		
		int p = getCornerPosition(pCorner);
		boolean isSwipeRight = (p == LEFT_BOTTOM || p == LEFT_TOP);
		
		PointF lCtrl, rCtrl; // left and right ctrol point
		lCtrl = isSwipeRight ? pCtrls[0] : pCtrls[1];
		rCtrl = isSwipeRight ? pCtrls[1] : pCtrls[0];
		
		p0.y = lCtrl.y - (pCorner.y - lCtrl.y) / 2;
		p0.x = pCorner.x;
		
		p1.x = rCtrl.x - (pCorner.x - rCtrl.x) / 2;
		p1.y = pCorner.y;
		
		return (isSwipeRight ? new PointF[] {p0, p1} : new PointF[] {p1, p0});
	}
	
	/**
	 * 
	 * @return LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
	 */
	public static int getCornerPosition(PointF pCorner) {
		int hMask = (pCorner.x > 0) ? 2 : 0;
		int vMask = (pCorner.y > 0 ) ? 1 : 0;
		
		return (3 & (hMask + vMask));
	}
}
