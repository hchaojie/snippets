package cn.hchaojie.util;

import android.app.Activity;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.hchaojie.snippets.R;
import cn.hchaojie.util.DialogButton.OnButtonClickListener;

public class DialogUtils {
    public static void showBadgeDialog(final Activity context) {
        Dialog d = getDialog(context, R.layout.sab_badge_dialog_content_view, new OnButtonClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        
       d.show();
    }
    
    public static void showLevelDialog(final Activity context) {
        Dialog d = getDialog(context, R.layout.sab_badge_dialog_content_view, null);
       d.show();
    }
    
    private static Dialog getDialog(final Activity context, int contentViewId, OnButtonClickListener okClickListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        LayoutInflater inflater = context.getLayoutInflater();
        LinearLayout baseDailog = (LinearLayout) inflater.inflate(R.layout.sab_dialog_view, null);
        ViewGroup contentContainer = (ViewGroup) baseDailog.findViewById(R.id.content_container);
        
        View btnDivider = baseDailog.findViewById(R.id.btn_divider);
        DialogButton btnRight = (DialogButton) baseDailog.findViewById(R.id.btn_positive);
        if (okClickListener != null) {
            btnRight.setVisibility(View.VISIBLE);
            btnDivider.setVisibility(View.VISIBLE); 
            btnRight.setOnButtonClickListener(okClickListener);
        } else {
            btnRight.setVisibility(View.GONE);
            btnDivider.setVisibility(View.GONE);
        }
        
        DialogButton btnLeft = (DialogButton) baseDailog.findViewById(R.id.btn_nagtive);
        btnLeft.setOnButtonClickListener(new OnButtonClickListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        
        inflater.inflate(contentViewId, contentContainer);
        
        dialog.setContentView(baseDailog);
        
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int h = dm.heightPixels - DensityUtil.dip2px(context, 150);
//        int w = dm.widthPixels - DensityUtil.dip2px(context, 40);
//        dialog.getWindow().setLayout(w, h);
        
        return dialog;
    }
}
