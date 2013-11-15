package cn.hchaojie.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class DialogButton extends LinearLayout implements View.OnClickListener {
    public static interface OnButtonClickListener {
        public void onClick();
    }

    private Button mButton;
    private OnButtonClickListener mListener;

    public DialogButton(Context context) {
        this(context, null);
    }

    public DialogButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        mButton = (Button) getChildAt(0);

        mButton.setOnClickListener(this);
        setOnClickListener(this);
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        this.mListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null && mListener != this) {
            mListener.onClick();
        }
    }
}
