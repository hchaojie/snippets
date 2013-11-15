package cn.hchaojie.snippets.apps.itodo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Gallery;

public class MainActivity extends Gallery {
	public MainActivity(Context context) {
		this(context, null);
	}

	public MainActivity(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}