package cn.hchaojie.snippets.view.pageindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class TabContainer extends LinearLayout {
    public interface OnTabSelectedListener {
        void onTabSelected(int position);
    }
    
	private ViewPager mViewPager;
	private OnTabSelectedListener mTabSelectedListener;

	public TabContainer(Context context) {
		this(context, null);
	}

    public TabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onFinishInflate() {
    	super.onFinishInflate();
    	
        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = getChildAt(i);
            final int index = i;
            child.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setCurrentItem(index);
				}
			});
        }
    }
    
    public void setOnTabSelectedListener(OnTabSelectedListener l) {
    	mTabSelectedListener = l;
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        
        PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter set.");
        }
        
        mViewPager = view;
        
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int idx) {
				setCurrentTab(idx);
				
				if (mTabSelectedListener != null) {
					mTabSelectedListener.onTabSelected(idx); 
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
        
        setCurrentItem(0);
    }

    public void setCurrentItem(int index) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        
        mViewPager.setCurrentItem(index);

        setCurrentTab(index);
    }
    
    private void setCurrentTab(int index) {
        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = getChildAt(i);
            boolean isSelected = (i == index);
            child.setSelected(isSelected);
        }
    }
}