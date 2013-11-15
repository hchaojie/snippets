package cn.hchaojie.snippets.view.drawable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.hchaojie.snippets.R;

public class TestBoxDrawable extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout v = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.test_box_drawable, null);
        ImageView image = new ImageView(this);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBoxDrawable.this, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        image.setImageResource(R.drawable.action_reply);
        image.setEnabled(false);

        ImageView image2 = new ImageView(this);
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBoxDrawable.this, "clicked two!", Toast.LENGTH_SHORT).show();
            }
        });
        image2.setImageResource(R.drawable.action_reply);

        View view = new View(this);
        view.setBackgroundResource(R.drawable.action_reply);
        view.setLayoutParams(new LayoutParams(50, 50));
        view.setEnabled(false);

        View view2 = new View(this);
        view2.setBackgroundResource(R.drawable.action_reply);
        view2.setLayoutParams(new LayoutParams(50, 50));
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestBoxDrawable.this, "clicked two!", Toast.LENGTH_SHORT).show();
            }
        });
        view2.setEnabled(true);

        View view3 = new View(this);
        view3.setBackgroundResource(R.drawable.action_reply);
        view3.setLayoutParams(new LayoutParams(50, 50));
        view3.setClickable(true);
        view3.setEnabled(true);

        SecondaryMenuButton b1 = new SecondaryMenuButton(this);
        b1.setIcon(getResources().getDrawable(R.drawable.action_reply));
        b1.setText("hello butty");

        SecondaryMenuButton b2 = new SecondaryMenuButton(this);
        b2.setIcon(getResources().getDrawable(R.drawable.action_reply));
        b2.setEnabled(false);

        v.addView(b1);
        v.addView(b2);

        v.addView(view);
        v.addView(view2);
        v.addView(view3);

        v.addView(image);
        v.addView(image2);

        setContentView(v);

        b2.setIcon(getResources().getDrawable(R.drawable.action_reply));
    }

    public void nextClicked(View v) {
        Intent intent = new Intent(this, TestGradientBox.class);
        startActivity(intent);
    }
}
