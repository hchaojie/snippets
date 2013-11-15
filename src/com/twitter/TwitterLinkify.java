package com.twitter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.twitter.Extractor.Entity;

public class TwitterLinkify {
    private final Extractor mExtractor = new Extractor();

    public SpannableString autoLink(Context mContext, String text) {
        if (TextUtils.isEmpty(text)) return SpannableString.valueOf("");

        SpannableString string = SpannableString.valueOf(text);
        List<Entity> entities = mExtractor.extractEntitiesWithIndices(text);

        for (Entity entity : entities) {
            string.setSpan(new TwitterLinkSpan(mContext, entity), entity.start, entity.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return string;
    }
}
