package cn.hchaojie.snippets.view;

import cn.hchaojie.snippets.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class TestSlidingDrawer extends Activity {
	private GridView gv;
	private SlidingDrawer sd;
	private ImageView im;
	private int[] icons = {
			R.drawable.android_normal,
			R.drawable.android_waving,
			R.drawable.left,
			R.drawable.right
	};
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_drawer);
        gv = (GridView) findViewById(R.id.myContent1);
        sd = (SlidingDrawer) findViewById(R.id.drawer1);
        im = (ImageView) findViewById(R.id.myImage1);
        gv.setAdapter(new GridViewAdapter());
        // 设置SlidingDrawer被打开的事件处理
        sd.setOnDrawerOpenListener(new OnDrawerOpenListener() {			
			@Override
			public void onDrawerOpened() {
				im.setImageResource(R.drawable.right);
				
			}
		});
        // 设置SlidingDrawer被关闭的事件处理
        sd.setOnDrawerCloseListener(new OnDrawerCloseListener() {			
			@Override
			public void onDrawerClosed() {
				im.setImageResource(R.drawable.left);
			}
		});
    }
    // 因为我这边Adapter的数据不仅仅是TextView，所以我自己定义了一个Adapter
    class GridViewAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return icons.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater flater = TestSlidingDrawer.this.getLayoutInflater();
			View view = flater.inflate(R.layout.grid, null);
			ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
			TextView tv = (TextView) view.findViewById(R.id.textView1);
			iv.setImageResource(icons[position]);
			tv.setText(String.valueOf(position));
			return view;
		}
    	
    }
}