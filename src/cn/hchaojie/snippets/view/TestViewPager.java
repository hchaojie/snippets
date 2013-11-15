package cn.hchaojie.snippets.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.hchaojie.snippets.R;


public class TestViewPager extends Activity {
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_pager);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(new MyPageAdapter(this));
	}

    public static class MyPageAdapter extends PagerAdapter {
    	Context mContext;
    	
    	public MyPageAdapter(Context context) {
    		mContext = context;
    	}
    	
		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public void startUpdate(View container) {
		}

		@Override
		public Object instantiateItem(View container, int position) {
			int[] background = {R.drawable.blue, R.drawable.red, R.drawable.green};
			
			TextView textView = new TextView(mContext);
			textView.setText("111 http://aa.com bbb ccc" + position);
			Linkify.addLinks(textView, Linkify.WEB_URLS);
			textView.setBackgroundColor(background[position]);
			textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mContext, "clicked!", Toast.LENGTH_LONG).show();
                            }
                        });
			
			((ViewGroup) container).addView(textView);
			return textView;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return object == view;
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}
    }
}
