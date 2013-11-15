package cn.hchaojie.snippets.view.drawable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.hchaojie.snippets.R;

public class TestColorMatrix extends Activity {

    private Button change;
    private EditText[] et = new EditText[20];
    private float[] carray = new float[20];
    private MyImage sv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.test_color_matrix, null);
        
        sv = new MyImage(this, null);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(sv);
        
        setContentView(layout);

        change = (Button) findViewById(R.id.set_me);
        
        
        ViewGroup rValues = (ViewGroup) findViewById(R.id.r_values);
        ViewGroup gValues = (ViewGroup) findViewById(R.id.g_values);
        ViewGroup bValues = (ViewGroup) findViewById(R.id.b_values);
        ViewGroup aValues = (ViewGroup) findViewById(R.id.a_values);
        
        ViewGroup[] groups = {rValues, gValues, bValues, aValues};

        for (int i = 0; i < 20; i++) {
            int row = i / 5;
            int col = i % 5;
            
            Log.d("COLOR_MATRIX", "row: " + row + " col:" + col);
            et[i] = (EditText) groups[row].getChildAt(col + 1);
            et[i].setText("0");
//            et[i] = (EditText) findViewById(R.id.indexa + i);
//            carray[i] = Float.valueOf(et[i].getText().toString());
        }

        change.setOnClickListener(l);
    }

    private Button.OnClickListener l = new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            try {
                getValues();
            } catch (NumberFormatException e) {
                return;
            }
            
            sv.setValues(carray);
            sv.invalidate();
        }
    };

    public void getValues() {
        for (int i = 0; i < 20; i++) {
            carray[i] = Float.valueOf(et[i].getText().toString());
        }
    }
}

class MyImage extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mBitmap;
    private float[] array = new float[20];

    private float mAngle;

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        invalidate();
    }

    public void setValues(float[] a) {
        for (int i = 0; i < 20; i++) {
            array[i] = a[i];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = mPaint;
        paint.setColorFilter(null);
        
        ColorMatrix cm = new ColorMatrix();
        cm.set(array);
        
        Bitmap bitmap = changeBitmapColor(mBitmap);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        
//        canvas.drawBitmap(mBitmap, 0, 0, paint);
//
//        ColorMatrix cm = new ColorMatrix();
//        // 设置颜色矩阵
//        cm.set(array);
//        // 颜色滤镜，将颜色矩阵应用于图片
//        paint.setColorFilter(new ColorMatrixColorFilter(cm));
//        // 绘图
//        canvas.drawBitmap(mBitmap, 0, 0, paint);
//        Log.i("CMatrix", "--------->onDraw");
    }
    
    
    public Bitmap changeBitmapColor(Bitmap sourceBitmap) {   
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.set(array);
//        cm.set(new float[] {
//                0, 0, 0, 0, 100,
//                0, 1, 0, 0, 100,
//                0, 0, 1, 0, 100,
//                0, 0, 0, 1, 0,
//        }); 


        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);

        c.drawBitmap(sourceBitmap, 0, 0, paint);
        c.drawText("2", width / 2,  height / 2, paint);

        return bmpGrayscale;
    }
}