package cn.hchaojie.snippets.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import cn.hchaojie.snippets.R;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.loopj.android.image.util.ImageSize;

public class TestGrid extends Activity {
    public static final String TAG = "Test List Activity";
    
    MyGridAdapter mAdapter;
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGridView = (GridView) LayoutInflater.from(this).inflate(R.layout.grid_view, null);
        mAdapter = new MyGridAdapter(this);
        mAdapter.add("Badge1");
        mAdapter.add("Badge1");
        mAdapter.add("Badge1");
        mAdapter.add("Badge1");
        mGridView.setAdapter(mAdapter);

        setContentView(mGridView);
    }
    
    private static class MyGridAdapter extends ArrayAdapter<String> {
        Context mContext;

        public MyGridAdapter(Context context) {
            super(context, R.id.badge_name);

            mContext = context;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
            
            ViewHolder holder = new ViewHolder();
            holder.badgeIcon = (SmartImageView) view.findViewById(R.id.badge_icon);
            holder.badgeText = (TextView) view.findViewById(R.id.badge_name);
                
            holder.badgeText.setText(getItem(position));
            if (position == 0) {
                Log.d(TAG, "About to set image at position 0");
                holder.badgeIcon.setImage(new SmartImage() {
                    public String getKey() {
                        return "badge";
                    }
                    
                    @Override
                    public Bitmap getBitmap(Context arg0, ImageSize arg1) {
                        Log.d(TAG, Thread.currentThread() + "about to get icon, THREAD NAME:" + Thread.currentThread().getName());
                        
                        return BadgeProvider.getBitmap();
                        //return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.action_reply);
                    }
                });
            } else {
                holder.badgeIcon.setImageResource(R.drawable.action_cancel_off);
            }

            return view;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = convertView;
//            ViewHolder holder;
//            if (view == null) {
//                view = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
//                
//                holder = new ViewHolder();
//                holder.badgeIcon = (SmartImageView) view.findViewById(R.id.badge_icon);
//                holder.badgeText = (TextView) view.findViewById(R.id.badge_name);
//                
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//            
//            holder.badgeText.setText(getItem(position));
//            if (position == 0) {
//                Log.d(TAG, "About to set image;");
//                holder.badgeIcon.setImage(new SmartImage() {
//                    public String getKey() {
//                        return "badge";
//                    }
//                    
//                    @Override
//                    public Bitmap getBitmap(Context arg0, ImageSize arg1) {
//                        Log.d(TAG, Thread.currentThread() + "about to get icon, THREAD NAME:" + Thread.currentThread().getName());
//                        
//                        
//                        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.action_reply);
//                    }
//                });
//            } else {
//                holder.badgeIcon.setImageResource(R.drawable.action_cancel_off);
//            }
//
//            return view;
//        }

        @Override
        public final long getItemId(int position) {
            return position;
        }
        
        public static class ViewHolder {
            SmartImageView badgeIcon;
            TextView badgeText;
        }
    }
    
    static class BadgeProvider {
        public static synchronized Bitmap getBitmap() {
            Log.d(TAG, "Start to run!");
            try {
                Thread.currentThread().sleep(10000);
                Log.d(TAG, "COMPLETE!");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }
}
