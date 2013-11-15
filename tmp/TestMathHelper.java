package sf.hmg.turntest;
import android.graphics.PointF;
import android.util.Log;

public class TestMathHelper {
	public static void run() {
		//testGetCornerPosition();
		//testGetControlPoints();
		testGetStartAndEndPoints();
	}
	
	private static void testGetCornerPosition() {
		Log.v("TEST", "" + PageCaculator.getCornerPosition(new PointF(0, 0)));
		Log.v("TEST", "" + PageCaculator.getCornerPosition(new PointF(0, 1)));
		Log.v("TEST", "" + PageCaculator.getCornerPosition(new PointF(1, 0)));
		Log.v("TEST", "" + PageCaculator.getCornerPosition(new PointF(1, 1)));
	}
	
	private static void testGetControlPoints() {
		PointF pTouch = new PointF(200, 350);
		PointF c0 = new PointF(0, 0);
		PointF c1 = new PointF(0, 800);
		PointF c2 = new PointF(480, 0);
		PointF c3 = new PointF(480, 800);
		
		PointF[] a = PageCaculator.getBezierControls(pTouch, c0);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", a[0].x, a[0].y, a[1].x, a[1].y));
		
		a = PageCaculator.getBezierControls(pTouch, c1);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", a[0].x, a[0].y, a[1].x, a[1].y));
		
		a = PageCaculator.getBezierControls(pTouch, c2);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", a[0].x, a[0].y, a[1].x, a[1].y));
		
		a = PageCaculator.getBezierControls(pTouch, c3);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", a[0].x, a[0].y, a[1].x, a[1].y));
	}
	
	private static void testGetStartAndEndPoints() {
		PointF pTouch = new PointF(200, 350);
		PointF c0 = new PointF(0, 0);
		PointF c1 = new PointF(0, 800);
		PointF c2 = new PointF(480, 0);
		PointF c3 = new PointF(480, 800);
		
		PointF[] c = PageCaculator.getBezierControls(pTouch, c0);
		PointF[] s = PageCaculator.getStartAndEndPoint(c0, c);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", s[0].x, s[0].y, s[1].x, s[1].y));
		
		c = PageCaculator.getBezierControls(pTouch, c1);
		s = PageCaculator.getStartAndEndPoint(c1, c);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", s[0].x, s[0].y, s[1].x, s[1].y));
		
		c = PageCaculator.getBezierControls(pTouch, c2);
		s = PageCaculator.getStartAndEndPoint(c2, c);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", s[0].x, s[0].y, s[1].x, s[1].y));
		
		c = PageCaculator.getBezierControls(pTouch, c3);
		s = PageCaculator.getStartAndEndPoint(c3, c);
		Log.v("TEST", String.format("[%s, %s], [%s, %s]", s[0].x, s[0].y, s[1].x, s[1].y));
	}
}
