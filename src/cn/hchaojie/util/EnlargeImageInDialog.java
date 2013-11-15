package cn.hchaojie.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import cn.hchaojie.snippets.R;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

/**
 * Typical case is when you click a small size image, you then need to show a large size of image in a dialog
 */
public class EnlargeImageInDialog {
    public static void showImageInDialog(final Context context, String imageUrl) {
        if (imageUrl == null || imageUrl.trim().length() == 0) { return; }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);

        FrameLayout view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.enlarge_image_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

        AQuery query = new AQuery(view);

        query.id(imageView).progress(R.id.progress).image(imageUrl, true, false, 0, 0, new BitmapAjaxCallback() {
            @Override
            public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                if (bm == null) return;         // in case the bitmap is not retrieved

                iv.setImageBitmap(bm);

                Window window = dialog.getWindow();
                View decorView = window.getDecorView();
                int pL = decorView.getPaddingLeft();
                int pR = decorView.getPaddingRight();
                int pT = decorView.getPaddingTop();
                int pB = decorView.getPaddingBottom();

                int frameH = bm.getHeight() + pT + pB;
                int frameW = bm.getWidth() + pL + pR;

                Rect r = new Rect();
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                int w = r.right - r.left;
                int h = r.bottom - r.top;

                // extended screen
                if (frameW > w || frameH > h) {
                    float wScale = (float) frameW / (float) w;
                    float hScale = (float) frameH / (float) h;
                    float scale = Math.max(wScale, hScale);

                    frameW = (int) (frameW / scale);
                    frameH = (int) (frameH / scale);
                }

                window.setLayout(frameW, frameH);
            }
        });
    }
}
