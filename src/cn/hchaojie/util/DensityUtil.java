package cn.hchaojie.util;

import java.awt.Dimension;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
    public static int dip2px(Context context, float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    
    public static Dimension getWindowDimension(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        
        return new Dimension(dm.widthPixels, dm.heightPixels);
    }
}
