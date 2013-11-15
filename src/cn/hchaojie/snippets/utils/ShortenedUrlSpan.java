package cn.hchaojie.snippets.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class ShortenedUrlSpan extends InterceptableUrlSpan {
    private final Context mContext;

    public ShortenedUrlSpan(Context context, String url) {
        super(context, url);
        mContext = context;
    }

    @Override
    public void onClick(View widget) {
        String url = getURL();
        String originalUrl = UrlUtils.sShortUrlCache.get(url);

        Toast.makeText(mContext, url + "\n" + originalUrl, Toast.LENGTH_SHORT).show();
    }
}