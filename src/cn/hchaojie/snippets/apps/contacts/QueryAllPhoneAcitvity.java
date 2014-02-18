package cn.hchaojie.snippets.apps.contacts;

import java.util.HashSet;
import java.util.Set;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class QueryAllPhoneAcitvity extends Activity {

    private static final String TAG = "QueryContacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        readPhoneContactsIds(new HashSet<Long>());
//        queryColumns();

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
    
    private void readPhoneContactsIds(Set<Long> phoneContactsIds) {
//        ITransactionFactory factory = Implementations.getTransactionFactory();
//        String accName = factory.getDefaultPhoneAccountName();
//        String accType = factory.getDefaultPhoneAccountType();
        String accName = "Connections";
        String accType = "com.cognima.android.authenticators.MozuAuthenticator.account";

        Uri uri = Contacts.CONTENT_URI;
        final Uri.Builder builder = uri.buildUpon();
        builder.appendQueryParameter(RawContacts.ACCOUNT_NAME, accName);
        builder.appendQueryParameter(RawContacts.ACCOUNT_TYPE, accType);
        uri = builder.build();

        Cursor cursor = null;
        try {
            String[] projection = new String[] { BaseColumns._ID };
            String sort = BaseColumns._ID + " ASC";

//            cursor = iResolver.query(uri, projection, null, null, sort);
            cursor = getContentResolver().query(uri, projection, null, null, sort);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    phoneContactsIds.add(cursor.getLong(0));
                }
            }
        } finally {
            if (cursor != null) cursor.close();
        }

//        Ln.d("Totally loaded phone contacts: %d", phoneContactsIds.size());
        Log.d("TEST", "Totally loaded phone contacts: " + phoneContactsIds.size());
    }
}
