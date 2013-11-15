package cn.hchaojie.snippets.view;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import cn.hchaojie.snippets.R;


public class TestTabContentById extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TabHost host = getTabHost();
        
        LayoutInflater.from(this).inflate(R.layout.tab_content_views, host.getTabContentView(), true);
        host.addTab(host.newTabSpec("T1").setIndicator("T1").setContent(R.id.tab_content1));
        host.addTab(host.newTabSpec("T2").setIndicator("T2").setContent(R.id.tab_content2));
        host.addTab(host.newTabSpec("T3").setIndicator("T3").setContent(R.id.tab_content3));
    }
}
