package cn.hchaojie.snippets.apps.contacts;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class QueryAllPhoneAcitvity extends Activity {

    private static final String TAG = "QueryContacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        queryColumns();

//        queryALlPhone();
    }
    
    private void queryALlPhone() {
        Cursor c = null;
        String[] projection = new String[] { Phone.CONTACT_ID, Phone.NUMBER};
        
        try {
            // long start = System.currentTimeMillis();
            c = getContentResolver().query(Phone.CONTENT_URI, projection, null, null, null);

            Log.v(TAG, "Getting contacts with PTT - found [" + c.getCount() + "]");

            if (c != null && c.getCount() > 0) {
                c.moveToFirst();

                do {
                    long contactID = c.getLong(0);
                    String number = c.getString(1);
                    
                    Log.d(TAG, "Contact id: " + contactID + " with phone number: " + number);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) c.close();
        }

        return;
    }
    
    private void queryColumns() {
        Cursor c = null;
        try {
            // long start = System.currentTimeMillis();
            c = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
            
            if (c != null && c.moveToFirst()) {
                String[] colNames = c.getColumnNames();
                for (String col : colNames) {
                    System.out.println("COL NAME: " + col);
                }
            }

        } finally {
            if (c != null) c.close();
        }
    }
}
