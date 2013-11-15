package cn.hchaojie.snippets.view;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class TestTabContentByIntent extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost host = getTabHost();
        
        host.addTab(host.newTabSpec("T1").setIndicator("T1")
        		.setContent(new Intent(this, TestTabFactory.class)));
        
        host.addTab(host.newTabSpec("T2").setIndicator("T2")
        		.setContent(new Intent(this, TestViewPager.class)));
    }
}
