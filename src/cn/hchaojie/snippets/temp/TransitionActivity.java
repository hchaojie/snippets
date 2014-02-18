package cn.hchaojie.snippets.temp;

import cn.hchaojie.snippets.SnippetsActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class TransitionActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        Intent intent = new Intent(this, SnippetsActivity.class);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        
        Fragment f = getSupportFragmentManager().findFragmentByTag("dialog");
        if (f != null) {
            Log.d(SimpleActivity.TAG, "dialog is found to be running!");
//            DialogFragment df = (DialogFragment) f;
//            df.dismiss();
            trans.remove(f);
        } else {
            Log.d(SimpleActivity.TAG, "no dialog is found!");
        }

//        startActivity(intent);
        finish();
    }
}
