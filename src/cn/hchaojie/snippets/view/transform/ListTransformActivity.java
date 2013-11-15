package cn.hchaojie.snippets.view.transform;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.hchaojie.snippets.R;

public class ListTransformActivity extends Activity {
    private ListView mPhotosList;

    // Names of the photos we show in the list
    private static final String[] PHOTOS_NAMES = new String[] {
            "Lyon AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Livermore AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Tahoe Pier AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Lake Tahoe AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Grand Canyon AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Bodie AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Last one AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Log.v("TransformLayout", "enter temp activity..");

        setContentView(R.layout.transform_view);

        // Prepare the ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, PHOTOS_NAMES);
        
        mPhotosList = (ListView) findViewById(android.R.id.list);
        mPhotosList.setAdapter(adapter);

        // Since we are caching large views, we want to keep their cache
        // between each animation
    }
}
