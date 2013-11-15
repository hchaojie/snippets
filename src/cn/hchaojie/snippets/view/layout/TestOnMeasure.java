package cn.hchaojie.snippets.view.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.hchaojie.snippets.R;

public class TestOnMeasure extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_linear_layout);
    }
}

class MyTextView extends TextView {
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String specWidth = MeasureSpec.toString(widthMeasureSpec);
        String specHeight = MeasureSpec.toString(heightMeasureSpec);
        
        Log.d("TAG", "spec width and height: " + specWidth + "/" + specHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}


class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String specWidth = MeasureSpec.toString(widthMeasureSpec);
        String specHeight = MeasureSpec.toString(heightMeasureSpec);
        
        Log.d("TAG", "spec width and height: " + specWidth + "/" + specHeight);
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}