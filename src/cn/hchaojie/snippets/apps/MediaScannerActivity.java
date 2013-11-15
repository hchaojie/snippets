package cn.hchaojie.snippets.apps;

import cn.hchaojie.snippets.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class MediaScannerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.normal_text);

        // Broadcast the Media Scanner Intent to trigger it
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

        // Just a message
        Toast toast = Toast.makeText(this, "Media Scanner Triggered...", Toast.LENGTH_SHORT);
        toast.show();
    }
}
