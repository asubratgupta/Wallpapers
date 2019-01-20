package com.googgler.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.example.util.Constant;
import com.example.util.JsonUtils;

public class SplashActivity extends AppCompatActivity
{

	JsonUtils util;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(Constant.theme);
		super.onCreate(savedInstanceState);

		sharedPreferences = getSharedPreferences("setting",MODE_PRIVATE);
		Constant.isToggle = sharedPreferences.getBoolean("noti",true);
		Constant.color = sharedPreferences.getInt("color",0xff37A5AF);
		Constant.drawable = sharedPreferences.getInt("draw",R.drawable.bg_nav_theme1);
		Constant.theme = sharedPreferences.getInt("theme",R.style.AppTheme);
		setTheme(Constant.theme);

		setContentView(R.layout.splash);
 		
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 2000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {

					util=new JsonUtils(SplashActivity.this);

					Resources r = getResources();
					float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,Constant.GRID_PADDING, r.getDisplayMetrics());
					Constant.columnWidth = (int) ((util.getScreenWidth() - ((Constant.NUM_OF_COLUMNS + 1) * padding)) / Constant.NUM_OF_COLUMNS);
			 
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		};
		splashThread.start();
	}
	
}