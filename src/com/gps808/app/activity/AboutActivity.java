package com.gps808.app.activity;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;

import com.gps808.app.R;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.FancyButton;

public class AboutActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private FancyButton about_share;
	private static final String FILE_NAME = "/XingTD/share_pic.jpg";
	public static String TEST_IMAGE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("关于我们");
		about_share = (FancyButton) findViewById(R.id.about_share);
		about_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initImagePath();
				Utils.Share(AboutActivity.this,
						getResources().getString(R.string.app_name),
						"易路通，您身边的行车助手！",
						"http://app.gps808.com/app_download.html", TEST_IMAGE);
			}
		});
	}

	// copy the picture from the drawable to sdcard,把图片从drawable复制到sdcard中
	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
						R.drawable.share_pic);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}
}
