package cn.hchaojie.snippets.view;

import cn.hchaojie.snippets.R;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;


public class TestTabFactory extends TabActivity implements TabContentFactory {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost host = getTabHost();
        host.addTab(host.newTabSpec("tab1").setIndicator("T1").setContent(this));
        host.addTab(host.newTabSpec("tab2")
        		.setIndicator("T2", getResources().getDrawable(R.drawable.ic_launcher))
        		.setContent(this));
        host.addTab(host.newTabSpec("tab3").setIndicator("T3").setContent(this));
    }

    
	public View createTabContent(String tag) {
		final TextView tv = new TextView(this);
		tv.setText("this is tab " + tag);
		return tv;
	}
	
}
