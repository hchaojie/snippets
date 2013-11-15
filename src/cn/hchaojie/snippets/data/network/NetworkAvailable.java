package cn.hchaojie.snippets.data.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class NetworkAvailable extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.);

        isNetworkAvailable();
    }

    public boolean isNetworkAvailable() {
        boolean networkAvailable = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info != null) {
            return info.isAvailable();
        } else { // active network is null, try all connections manually
            networkAvailable = false;
            NetworkInfo[] infoAll = connectivityManager.getAllNetworkInfo();
            if (infoAll != null) {
                for (NetworkInfo element : infoAll) {
                    Log.d("Test", element.toString());

                    if (element.getState() == NetworkInfo.State.CONNECTED) {
                        Log.d("Test", String.format("Network %s read as available - device is connected", element.getTypeName()));
                        networkAvailable = true;
                        break;
                    }
                }
            }
        }

        return networkAvailable;
    }
}
