package cn.hchaojie.snippets.view;

import cn.hchaojie.snippets.R;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;


public class TestTab extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TabHost tabHost = getTabHost();
        tabHost.getTabWidget().setStripEnabled(true);
        tabHost.getTabWidget().setLeftStripDrawable(R.drawable.icon_call);
        
        LayoutInflater.from(this).inflate(R.layout.tabs1, tabHost.getTabContentView(), true);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("tab1").setContent(R.id.view1));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("tab2").setContent(R.id.view2));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("tab3").setContent(R.id.view3));
        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("tab3").setContent(R.id.view4));
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("tab1").setContent(R.id.view1));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("tab2").setContent(R.id.view2));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("tab3").setContent(R.id.view3));
        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("tab3").setContent(R.id.view4));
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("tab1").setContent(R.id.view1));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("tab2").setContent(R.id.view2));
        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("tab3").setContent(R.id.view3));
        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("tab3").setContent(R.id.view4));
	}
}
