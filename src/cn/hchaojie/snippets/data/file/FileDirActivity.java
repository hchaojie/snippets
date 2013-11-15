package cn.hchaojie.snippets.data.file;

import java.io.File;

import cn.hchaojie.snippets.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FileDirActivity extends Activity {

	private Button myButton1; 
	private Button myButton2; 
	private File cacheDir; 
	private File fileDir; 
	
@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_dir);
		myButton1 = (Button) findViewById(R.id.btn1);
		myButton2 = (Button) findViewById(R.id.btn2);
		/* 取得目前Cache目录 */
		cacheDir = this.getCacheDir();
		/* 取得目前File目录 */
		fileDir = this.getFilesDir();
		myButton1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String path = fileDir.getParent() + java.io.File.separator
						+ fileDir.getName();
				showListActivity(path);
			}
		});
		myButton2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String path = cacheDir.getParent() + java.io.File.separator
						+ cacheDir.getName();
				showListActivity(path);
			}
		});

	}

	private void showListActivity(String path) {
		Intent intent = new Intent();
		intent.setClass(this, ListDirActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("path", path);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}