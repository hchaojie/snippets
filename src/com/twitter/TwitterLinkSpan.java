package com.twitter;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

import com.twitter.Extractor.Entity;

public class TwitterLinkSpan extends URLSpan {
    private static final String DEFAULT_USERNAME_URL_BASE = "https://twitter.com/";
    private static final String DEFAULT_HASHTAG_URL_BASE = "https://twitter.com/#!/search?q=%23";

    private final Context mContext;
    private final Entity mEntity;

    public TwitterLinkSpan(Context context, Entity text) {
        super(getURL(text));
        mContext = context;
        mEntity = text;
    }
    
    @Override
    public void onClick(View widget) {
        String url = getURL();

        if (TextUtils.isEmpty(url)) return;

        Toast.makeText(mContext, "clicked link:" + url, Toast.LENGTH_SHORT).show();
    }

    private static String getURL(Entity text) {
        switch (text.type) {
        case URL:
            return text.value;

        case HASHTAG:
            return DEFAULT_HASHTAG_URL_BASE + text.value;

        case MENTION:
            return DEFAULT_USERNAME_URL_BASE + text.value;

        default:
            break;
        }

        return null;
    }
}