/**
 * Copyright (C) 2012 Critical Path, Inc. All Rights Reserved.
 */
package cn.hchaojie.snippets.view.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class SecondaryMenuButton extends TextView {
    Context context;

    public SecondaryMenuButton(Context context) {
        this(context, null);
        this.context = context;
        init();

    }

    public SecondaryMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        this.setClickable(true);

        // setTextAppearance(context, R.style.BarText);

    }

    public void setIcon(Drawable d) {
        setCompoundDrawables(d, null, null, null);
    }
}
