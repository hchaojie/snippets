package cn.hchaojie.snippets.temp;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.hchaojie.snippets.AssetHelper;
import cn.hchaojie.snippets.R;

import com.twitter.TwitterLinkify;

public class TempActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View contentView = LayoutInflater.from(this).inflate(R.layout.temp, null);

        // ScrollView scrollView = (ScrollView) contentView.findViewById(R.id.post_scroller);
        // LinearLayout container = (LinearLayout) contentView.findViewById(R.id.post_container);

        // ListView listView = new ListView(this);
        // listView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ListView listView = (ListView) LayoutInflater.from(this).inflate(R.layout.temp2, null);
        listView.addHeaderView(contentView);

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
            listView.setAdapter(new ActivityAdapter(this, activities));
            // listView.requestLayout();
            // container.requestLayout();
            // scrollView.requestLayout();
        }

        setContentView(listView);
    }

    private void clicked() {
        Toast.makeText(this, "click ed", Toast.LENGTH_SHORT).show();
    }

    private class ActivityAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private JSONArray mActivities;

        public ActivityAdapter(Context context, JSONArray activities) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mActivities = activities;
        }

        public int getCount() {
            return mActivities.length();
        }

        public Object getItem(int position) {
            return mActivities.opt(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_list_item, null);

                holder = new ViewHolder();
                holder.contentWrapper = (LinearLayout) convertView.findViewById(R.id.content_wrapper);
                holder.imageContactPic = (ImageView) convertView.findViewById(R.id.image_contact_pic);
                holder.txtContactName = (TextView) convertView.findViewById(R.id.txt_contact_name);
                holder.txtContent = (TextView) convertView.findViewById(R.id.txt_activity_content);

                holder.txtTime = (TextView) convertView.findViewById(R.id.txt_activity_time);
                holder.imageActivityType = (ImageView) convertView.findViewById(R.id.icon_activity_type);

                convertView.setTag(holder);

                holder.txtContent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "text clicked", Toast.LENGTH_SHORT).show();
//                       TextView tv =  (TextView) v;
//                       ClickableSpan[] links = ((Spannable) tv.getText()).getSpans(tv.getSelectionStart(),
//                               tv.getSelectionEnd(), ClickableSpan.class);
//                       
//                       if (links.length > 0) {
//                           // do nothing
//                       } else {
//                           clicked();
//                       }
                    }
                });
                
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "convert view clicked tooo...", Toast.LENGTH_SHORT).show();
                            }
                        }, 1000);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String isEllapsed = null;
            if (position % 2 == 0) {
                holder.txtContent.setMaxLines(3);
                isEllapsed = "Ellaspsed--";
            } else {
                holder.txtContent.setMaxLines(Integer.MAX_VALUE);
                isEllapsed = "NOT Ellaspsed--";
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

                SpannableString autoLink = new TwitterLinkify().autoLink(mContext, isEllapsed + content);

                holder.txtContent.setText(autoLink);
                holder.txtContent.setMovementMethod(LinkMovementMethod.getInstance());
                holder.txtContent.setFocusable(false);

                holder.txtTime.setText(time);
                holder.imageActivityType.setImageResource(getIconDrawableByType(type));
            }

            return convertView;
        }

        class ViewHolder {
            LinearLayout contentWrapper;
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

    public static final String TAG = "Test List Activity";
}
