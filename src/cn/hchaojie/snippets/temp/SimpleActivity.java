package cn.hchaojie.snippets.temp;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.hchaojie.snippets.R;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SimpleActivity extends SherlockFragmentActivity {
    private static final String EXTRA = "cn.hchaojie.snippet.extra.foo";
    public static final String TAG = "cn.hchaojie.snippet.SimpleActivity";
    
    @Override
    protected void onResume() {
        super.onResume();
        
        System.out.println("Resume!!!");
        
        showDialog2(333333333);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.simple_spinner);

        final Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotification();
                    }
                }, 1000);
            }
        });

        Button bt2 = (Button) findViewById(R.id.btn2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2(111111111);
            }
        });
        
        final Button bt3 = (Button) findViewById(R.id.btn3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDialog2(222222);
                    }
                }, 1000);
            }
        });

    }

    private void showNotification() {
        Intent intent = new Intent(this, TransitionActivity.class);

        createNotification(intent, this, "Hello", "Buddy!", 99);
    }

    private static void createNotification(Intent intent, Context context, String title, String text, int notifId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.left, title, System.currentTimeMillis());

        // Hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        notifId = notifId == -1 ? new Random().nextInt(1000000) : notifId;
        notification.setLatestEventInfo(context, title, text, pendingIntent);
        notificationManager.notify(notifId, notification);
    }

    void showDialog2(int i) {
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            Log.d(TAG, "dialog is already showing!!!");
//            DialogFragment df = (DialogFragment) prev;
//            df.dismiss();
            ft.remove(prev);
        }

        // Create and show the dialog.
        DialogFragment newFragment = MyDialogFragment.newInstance(i);
        newFragment.show(ft, "dialog");
    }

    public static class MyDialogFragment extends DialogFragment {
        int i;

        static MyDialogFragment newInstance(int i) {
            MyDialogFragment f = new MyDialogFragment();
            f.i = i;
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Button v = new Button(getActivity());
            v.setText("" + i);
            return v;
        }
    }
}
