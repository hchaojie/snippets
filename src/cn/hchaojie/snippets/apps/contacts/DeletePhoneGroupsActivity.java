package cn.hchaojie.snippets.apps.contacts;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Groups;
import android.util.Log;

public class DeletePhoneGroupsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                List<Long> ids = readGroupIds();

                deleteGroups(ids);

                return null;
            }
        }.execute();
    }

    private void deleteGroups(List<Long> ids) {
        if (null != ids) {
            ArrayList<ContentProviderOperation> ops = createGroupDeleteOps(ids);
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                Log.e("TEST", e.getMessage());
            }
        }
    }

    private ArrayList<ContentProviderOperation> createGroupDeleteOps(List<Long> ids) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        for (Long id : ids) {
            ops.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(Groups.CONTENT_URI, id)).build());
        }

        return ops;
    }

    private List<Long> readGroupIds() {
        List<Long> result = new ArrayList<Long>();

        Uri uri = Groups.CONTENT_SUMMARY_URI;
        String[] projection = new String[] { BaseColumns._ID };
        String selection = Groups.DELETED + " =?";
        String[] selectionArgs = new String[] { "0" };

        Cursor c = null;

        try {
            c = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (c != null && c.moveToFirst()) {
                do {
                    result.add(c.getLong(0));
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) c.close();
        }

        return result;
    }
}
