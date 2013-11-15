package cn.hchaojie.snippets.utils;

import android.content.Context;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

public class InterceptableUrlSpan extends URLSpan {
    private final Context mContext;

    public InterceptableUrlSpan(Context context, String url) {
        super(url);
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(mContext, getURL(), Toast.LENGTH_SHORT).show();
    }
}