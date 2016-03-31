package com.gps808.app.activity;

import com.gps808.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * 欢迎界面
 * 
 * @author JIA
 * 
 */
public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		ImageView imageView = (ImageView) findViewById(R.id.welcome_image);
		imageView.setImageResource(R.drawable.app_start);
		// 设置渐变启动
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {

				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
//				 Intent intent = new Intent(WelcomeActivity.this,
//				 OfflineMapActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}
}
