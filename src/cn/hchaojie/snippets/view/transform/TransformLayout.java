package cn.hchaojie.snippets.view.transform;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

public class TransformLayout extends FrameLayout {
	private Camera mCamera = new Camera();
	
	public TransformLayout (Context context) {
		this(context, null);
	}
	
	public TransformLayout (Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TransformLayout (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStaticTransformationsEnabled(true);
	}
	
    /**
     * Returns the index of the child to draw for this iteration. Override this
     * if you want to change the drawing order of children. By default, it
     * returns i.
     * <p>
     * NOTE: In order for this method to be called, you must enable child ordering
     * first by calling {@link #setChildrenDrawingOrderEnabled(boolean)}.
     *
     * @param i The current iteration.
     * @return The index of the child to draw this iteration.
     * 
     * @see #setChildrenDrawingOrderEnabled(boolean)
     * @see #isChildrenDrawingOrderEnabled()
     */
    protected int getChildDrawingOrder(int childCount, int i) {
    	return getChildCount() - i;
        //return i;
    }
    
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		t.setTransformationType(Transformation.TYPE_MATRIX);

		float centerX = (float) child.getWidth() / 2;
		float centerY = (float) child.getHeight() / 2;
		
        final Matrix matrix = t.getMatrix();

        mCamera.save();
        
        mCamera.translate(0.0f, 50.0f, 300.0f);
        mCamera.rotateX(20);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        
		return true;
	}
}
