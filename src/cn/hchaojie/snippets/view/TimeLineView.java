package cn.hchaojie.snippets.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class TimeLineView extends View {
	private final static float TOP_WIDTH_SCALE = 0.37f;
	private final static int MARGIN_HORIZENTAL = 10;
	private final static int MARGIN_TOP = 250;
	private final static int MARGIN_BOTTOM = 100;
	private final static int PADDING_TIMELINE = 10;
	private final static float CORNER_RADIUS = 10f;
	
	private final static int COLOR_BACKGROUND = 0xfff1f1f1;
	private final static int COLOR_SHADER_START = 0x00f1f1f1;
	private final static int COLOR_SHADER_MID = 0xaaf1f1f1;
	private final static int COLOR_SHADER_END = 0xfff1f1f1;
	private final static int COLOR_SHADOW = 0x993c3c3c;
	private final static int COLOR_PATH_PAINT = 0xffffffff;
	
	private int mViewW = 0;
	private int mViewH = 0;
	private int mW = 0;
	private int mH = 0;
	
	private Point tl;
	private Point tr;
	private Point bl;
	private Point br;
	
	private float xyRatio;
	
	Paint mPaint1 = new Paint();
	Paint mPaint2 = new Paint();
	Paint mLinePaint = new Paint();
	Path mPath = new Path();
    Shader mShader;
        
	public TimeLineView(Context context) {
		this(context, null);
	}
	
	public TimeLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundColor(COLOR_BACKGROUND);
		setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mViewW == 0) initComponents();
		
        canvas.drawPath(mPath, mPaint1);
        canvas.drawPath(mPath, mPaint2);
        
        for (int i = 50; i < mH; i += 50) {
        	drawTimeLine(canvas, i);
        }
	}
	
	private void initComponents() {
		mViewH = getMeasuredHeight();
		mViewW = getMeasuredWidth();
		mH = mViewH - MARGIN_BOTTOM - MARGIN_TOP;
		mW = Math.round((mViewW - 2 * MARGIN_HORIZENTAL) * TOP_WIDTH_SCALE);
		
		tl = new Point((mViewW - mW) / 2, MARGIN_TOP);
		tr = new Point((mViewW + mW) / 2, MARGIN_TOP);
		bl = new Point(MARGIN_HORIZENTAL, mViewH - MARGIN_BOTTOM);
		br = new Point(mViewW - MARGIN_HORIZENTAL, mViewH - MARGIN_BOTTOM);
		
		xyRatio = (float)(mViewW - 2 * MARGIN_HORIZENTAL - mW) / (2 * mH);
		
		// init path and paint1
		mPath.moveTo(tl.x, tl.y);
		mPath.lineTo(tr.x, tr.y);
		mPath.lineTo(br.x, br.y);
		mPath.lineTo(bl.x, bl.y);
		mPath.lineTo(tl.x, tl.y);
		mPath.lineTo(tr.x, tr.y);	// add additional line to show top left corner
		
        mPaint1.setPathEffect(new CornerPathEffect(CORNER_RADIUS));
		mPaint1.setShadowLayer(10, 0, 20, COLOR_SHADOW);
        mPaint1.setColor(COLOR_PATH_PAINT);
        
		// init shader and paint2
		mShader = new LinearGradient(0, bl.y, 0, tl.y, 
				new int[] {COLOR_SHADER_START, COLOR_SHADER_MID, COLOR_SHADER_END}, null, Shader.TileMode.CLAMP);
        mPaint2.setShader(mShader);
        
        mLinePaint.setColor(COLOR_SHADOW);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(2);
	}
	
	private void drawTimeLine(Canvas c, int yOffset) {
		int xOffset = Math.round(yOffset * xyRatio);
		
		Point l = new Point(tl.x - xOffset + PADDING_TIMELINE, tl.y + yOffset);
		Point r = new Point(tr.x + xOffset - PADDING_TIMELINE, tr.y + yOffset);
		
        c.drawLine(l.x, l.y, r.x, r.y, mLinePaint);
	}
}