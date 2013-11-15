package cn.hchaojie.snippets.view;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.html.HTML;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.hchaojie.snippets.AssetHelper;
import cn.hchaojie.snippets.R;

public class TestList extends ListActivity {
    public static final String TAG = "Test List Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONArray activities = null;
        try {
            JSONObject root = AssetHelper.readJson(this, "data/all_activities.json");

            activities = root.getJSONArray("activities");
        } catch (IOException e) {
            Log.e(TAG, "Get asset data/all_activities.json error. " + e.getMessage());
            // to pop up a dialog
        } catch (JSONException e) {
            Log.e(TAG, "Parse json data error: " + e.getMessage());
            // to pop up a dialog
        }

        if (activities != null) {
            setListAdapter(new ActivityAdapter(this, activities));
            
            scroolToCenter();
        }
    }

    private void scroolToCenter() {
        final ListView list = getListView();
        
        list.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListAdapter adapter = getListAdapter();
                int mid = adapter.getCount() / 2;
                
                View child = list.getChildAt(mid);
//                int top = child.getTop();
//                
                int pH = list.getHeight();
                Log.d("TEST LIST", list.getHeight() + "");
                int cH = child.getHeight();
                
                int backOffset = (pH / 2 / cH);
//                
//                int scroll = top - (pH - cH) / 2;
//                
//                list.scrollTo(0, scroll);
//                
//                list.smoothScrollBy(distance, duration)
                
                if (mid - backOffset > 0) {
                    list.setSelection(mid - backOffset);
                }
            }
        }, 1000);
    }
    
    //
    // @Override
    // protected void onListItemClick(ListView l, View v, int position, long id) {
    // clicked();
    // }

    private void clicked(int p) {
        Toast.makeText(this, "clicked at position: " + p, Toast.LENGTH_SHORT).show();
    }

    private class ActivityAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final Context mContext;
        private final JSONArray mActivities;

        private final View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer p = (Integer) v.getTag();
                clicked(p);
            }
        };

        public ActivityAdapter(Context context, JSONArray activities) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mActivities = activities;
        }

        @Override
        public int getCount() {
            return mActivities.length();
        }

        @Override
        public Object getItem(int position) {
            return mActivities.opt(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_list_item, null);

                holder = new ViewHolder();
                holder.imageContactPic = (ImageView) convertView.findViewById(R.id.image_contact_pic);
                holder.txtContactName = (TextView) convertView.findViewById(R.id.txt_contact_name);
                holder.txtContent = (TextView) convertView.findViewById(R.id.txt_activity_content);
                holder.txtTime = (TextView) convertView.findViewById(R.id.txt_activity_time);
                holder.imageActivityType = (ImageView) convertView.findViewById(R.id.icon_activity_type);

                convertView.setTag(holder);

                // convertView.setFocusable(false);
                holder.imageActivityType.setTag(position);
                holder.imageActivityType.setOnClickListener(mListener);

                // holder.txtContent.setOnClickListener(new OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // clicked(position);
                // }
                // });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            JSONObject activity = mActivities.optJSONObject(position);
            if (activity != null) {
                String type = activity.optString("type");
                String name = activity.optString("who");
                String content = activity.optString("content");
                String time = activity.optString("time");
                String pic = activity.optString("pic_url");

                InputStream is = null;
                try {
                    is = mContext.getAssets().open(pic);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    holder.imageContactPic.setImageBitmap(bm);
                } catch (IOException e) {
                    Log.e(TAG, "Error reading user picture from url: " + pic + " message is " + e.getMessage());
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e1) {
                            Log.e(TAG, "Error close input stream. " + e1.getMessage());
                        }
                    }
                }

                holder.txtContactName.setText(name);
                
                holder.txtContent.setText(Html.fromHtml(content));

                holder.txtContent.setFocusable(false);

                holder.txtTime.setText(time);
                holder.imageActivityType.setImageResource(getIconDrawableByType(type));
            }

            return convertView;
        }

        class ViewHolder {
            ImageView imageContactPic;
            TextView txtContactName;
            TextView txtContent;
            TextView txtTime;
            ImageView imageActivityType;
        }

        private int getIconDrawableByType(String type) {
            if ("call".equalsIgnoreCase(type)) {
                return R.drawable.icon_call;
            } else if ("message".equalsIgnoreCase(type)) {
                return R.drawable.icon_chat;
            } else if ("facebook".equalsIgnoreCase(type)) {
                return R.drawable.icon_facebook;
            } else if ("twitter".equalsIgnoreCase(type)) {
                return R.drawable.icon_twitter;
            } else {
                return R.drawable.icon_twitter;
            }
        }
    }
}
