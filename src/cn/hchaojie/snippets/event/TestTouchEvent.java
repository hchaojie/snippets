package cn.hchaojie.snippets.event;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestTouchEvent extends Activity {
	public static final String TAG = "TestTouchEventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLinearLayout layout = new MyLinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        
        TextView tv = new TextView(this);
        tv.setText("hello touch me.");
        
        //layout.setOnClickListener(l);
        
        // click
        tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v(TAG, "My Text on click.");
			}
		});
        
        tv.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.v(TAG, "My Text on long click.");
				return false;
			}
		});
        
        // touch
        tv.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v(TAG, "My Text on touch. MotionEvent.getAction " + getAction(event.getAction()));
				return false;
			}
		});
        
        layout.addView(tv, new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setContentView(layout);
    }
    
    class MyLinearLayout extends LinearLayout {
		public MyLinearLayout(Context context) {
			super(context);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {			
			Log.v(TAG, "MyLinearLayout on InterceptTouch event.  MotionEvent.getAction " + getAction(ev.getAction()));
			return false;
		}
    }
    
    private static String getAction(int id) {
    	switch (id) {
    	case MotionEvent.ACTION_UP:
    		return "ACTION_UP";
    	case MotionEvent.ACTION_DOWN:
    		return "ACTION_DOWN";
    	case MotionEvent.ACTION_CANCEL:
    		return "ACTIOIN_CANCEL";
    	default:
    		return "UNKNOWN";
    	}
    }
}