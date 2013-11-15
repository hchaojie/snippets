package cn.hchaojie.snippets.view.transform;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import cn.hchaojie.snippets.R;

public class FlipViewActivity extends Activity {
	ViewGroup mContainer;
	View mView1;
	View mView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.flip_views);
        
        mContainer = (ViewGroup) findViewById(R.id.container);
        mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
        mView1 = findViewById(R.id.view1);
        mView2 = findViewById(R.id.view2);
    }
    
    public void doFlip(View v) {
    	flipContentView(mContainer);
    }

	public void flipContentView(View v) {
        // Find the center of the container
        final float centerX = v.getWidth() / 2.0f;
        final float centerY = v.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final FlipAnimation rotation = new FlipAnimation(0, 90, centerX, centerY);
        
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.setAnimationListener(new FlipAnimationListener());

        v.startAnimation(rotation);
	}
    
    private final class FlipAnimationListener implements Animation.AnimationListener {
        public void onAnimationStart(Animation animation) {}

        public void onAnimationEnd(Animation animation) {
        	mContainer.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation) {}
    }
    
    private final class SwapViews implements Runnable {
        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            FlipAnimation rotation;
            
            if (mView1.getVisibility() == View.VISIBLE) {
                mView1.setVisibility(View.GONE);
                mView2.setVisibility(View.VISIBLE);
                mView2.requestFocus();

            } else {
            	mView2.setVisibility(View.GONE);
                mView1.setVisibility(View.VISIBLE);
                mView1.requestFocus();

            }

            rotation = new FlipAnimation(270, 360, centerX, centerY);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());

            mContainer.startAnimation(rotation);
        }
    }
}
