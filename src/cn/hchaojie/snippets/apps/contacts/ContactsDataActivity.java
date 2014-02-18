package cn.hchaojie.snippets.apps.contacts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hchaojie.snippets.R;

public class ContactsDataActivity extends Activity {
    private LinearLayout mContainer;
    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContainer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.contacts_data, null);
        mInput = (EditText) mContainer.findViewById(R.id.input);
        mInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Toast.makeText(getBaseContext(), mInput.getText().toString(), Toast.LENGTH_SHORT).show();

//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);

                    return false;
                }
                
                return false;
            }
        });
        
        AlignmentSpan alignmentSpan = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
        SpannableString text = new SpannableString("Lorem \nipsum dolor sit amet");
        
        text.setSpan(alignmentSpan, 0, 5, 0);
        text.setSpan(new RelativeSizeSpan(2f), 0, 5, 0);
        mInput.setText(text);

        setContentView(mContainer);

        Button update = (Button) mContainer.findViewById(R.id.update1);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // query("");
                update(mInput.getText().toString(), PttType.PTT1);
            }
        });

        Button update2 = (Button) mContainer.findViewById(R.id.update2);
        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // query("");
                update(mInput.getText().toString(), PttType.PTT2);
            }
        });

        Button delete = (Button) mContainer.findViewById(R.id.delete1);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // query("");
                delete(mInput.getText().toString(), PttType.PTT1);
            }
        });

        Button delete2 = (Button) mContainer.findViewById(R.id.delete2);
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // query("");
                delete(mInput.getText().toString(), PttType.PTT2);
            }
        });
    }

    private static final String[] PROJECTION = new String[] { Data.MIMETYPE, Data.DATA1, Data.DATA2, Data.DATA3, Data.RAW_CONTACT_ID };

    private static final String TAG = "CONTACTS_DC_TEST";

    private void readData() {
        Cursor c = getContentResolver().query(Data.CONTENT_URI, PROJECTION, null, null, null);
        try {
            if (c != null && c.moveToFirst()) {
                do {
                    String mimetype = c.getString(c.getColumnIndexOrThrow(Data.MIMETYPE));
                    Log.d(TAG, mimetype);

                    if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equalsIgnoreCase(mimetype)) {
                        // we find a row in data table with MIME_TYPE =vnd.android.cursor.item/phone_v2
                        TextView tv = new TextView(this);
                        tv.setText(c.getString(c.getColumnIndexOrThrow(Data.DATA1)));
                        mContainer.addView(tv);
                    }
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    enum PttType {
        PTT1, PTT2;
    };

    private void update(String rawId, PttType type) {
        Uri pttUri = Uri.withAppendedPath(Data.CONTENT_URI, "ptt");

        String pttType = null;
        String dataType = null;
        if (type == PttType.PTT1) {
            pttType = "PTT1";
            dataType = "0";
        } else {
            pttType = "PTT2";
            dataType = "1";
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String s1 = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=? AND " + Data.DATA2 + "=?";
        String s2 = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=? AND " + Data.DATA3 + "=?";

        String[] args1 = new String[] { rawId, "vnd.android.cursor.item/ptt", dataType };
        String[] args2 = new String[] { rawId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, pttType };

        ops.add(ContentProviderOperation.newUpdate(pttUri).withSelection(s1, args1).withValue(Data.DATA1, "444555").build());
        ops.add(ContentProviderOperation.newUpdate(pttUri).withSelection(s2, args2).withValue(Data.DATA1, "444555").build());

        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d(TAG, "updated rows:" + results[0].count + " / " + results[1].count);
        } catch (RemoteException e) {
            Log.w(TAG, e);
        } catch (OperationApplicationException e) {
            Log.w(TAG, e);
        }
    }

    private void delete(String rawId, PttType type) {
        Uri pttUri = Uri.withAppendedPath(Data.CONTENT_URI, "ptt");

        String pttType = null;
        String dataType = null;
        if (type == PttType.PTT1) {
            pttType = "PTT1";
            dataType = "0";
        } else {
            pttType = "PTT2";
            dataType = "1";
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        String s1 = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=? AND " + Data.DATA2 + "=?";
        String s2 = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=? AND " + Data.DATA3 + "=?";

        String[] args1 = new String[] { rawId, "vnd.android.cursor.item/ptt", dataType };
        String[] args2 = new String[] { rawId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, pttType };

        ops.add(ContentProviderOperation.newDelete(pttUri).withSelection(s1, args1).build());
        ops.add(ContentProviderOperation.newDelete(pttUri).withSelection(s2, args2).build());

        try {
            ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d(TAG, "updated rows:" + results[0].count + " / " + results[1].count);
        } catch (RemoteException e) {
            Log.w(TAG, e);
        } catch (OperationApplicationException e) {
            Log.w(TAG, e);
        }
    }

    private void query(String value) {
        // http://developers.androidcn.com/resources/articles/contacts.html
        Uri pttUri = Uri.withAppendedPath(Data.CONTENT_URI, "ptt");
        Log.d(TAG, "pttUri:" + pttUri.getPath());
        Log.d(TAG, "pttUri:" + pttUri.toString());
        // Uri lookup = Uri.withAppendedPath(pttUri, "ptt");

        // String selection = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "='vnd.android.cursor.item/ptt'";
        // String selection = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=?";
        // String[] args = new String[] { "57", ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };
        // String[] args = new String[] { "57" };

        String s1 = Data.RAW_CONTACT_ID + "=? AND " + Data.MIMETYPE + "=? AND " + Data.DATA3 + " IN ('PTT1', 'PTT2')";
        String[] args1 = new String[] { value, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };

        Cursor c = getContentResolver().query(pttUri, new String[] { Data.DATA1 }, s1, args1, null);
        Log.d(TAG, "queried rows: " + c.getCount());
        try {
            c.moveToFirst();
            String data1 = c.getString(0);
            Log.d(TAG, data1);
        } finally {
            c.close();
        }
    }
}
