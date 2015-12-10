package com.gps808.app.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gps808.app.R;
import com.gps808.app.bean.BnUser;
import com.gps808.app.dialog.AlterPassWordDialog;
import com.gps808.app.fragment.HeaderFragment;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.utils.HttpUtil;
import com.gps808.app.utils.ImageUtils;
import com.gps808.app.utils.StringUtils;
import com.gps808.app.utils.UrlConfig;
import com.gps808.app.utils.Utils;
import com.gps808.app.view.CircleImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private RelativeLayout nickLayout;
	private LinearLayout sexLayout;
	private RelativeLayout headLayout;
	private TextView personal_user_text;
	private TextView personal_nick_text;
	private TextView personal_sex_text;

	private final static int CROP = 200;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/YIZHONG/Image/";
	private Uri origUri;
	private Uri cropUri;
	private File protraitFile;
	private Bitmap protraitBitmap;
	private String protraitPath;
	private CircleImageView circleImageView;
	private BnUser myself;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter_person);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("个人中心");
//		circleImageView = (CircleImageView) findViewById(R.id.personal_headimage);
		nickLayout = (RelativeLayout) findViewById(R.id.personal_nick);
		nickLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sexLayout = (LinearLayout) findViewById(R.id.personal_sex);
		sexLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlterPassWordDialog alterPassWordDialog = new AlterPassWordDialog(
						PersonActivity.this);
				alterPassWordDialog.show();
			}
		});
		headLayout = (RelativeLayout) findViewById(R.id.personal_head);
		headLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CharSequence[] items = {
						PersonActivity.this.getString(R.string.img_from_album),
						PersonActivity.this.getString(R.string.img_from_camera) };
				imageChooseItem(items);
			}
		});
		personal_nick_text = (TextView) findViewById(R.id.personal_nick_text);
		personal_sex_text = (TextView) findViewById(R.id.personal_sex_text);
		personal_user_text = (TextView) findViewById(R.id.personal_user_text);

	}

	private void setValue() {
		personal_nick_text.setText(myself.getNickname());
		personal_user_text.setText(myself.getMobile());
		if (myself.getSex().equals("0")) {
			personal_sex_text.setText("男");
		} else {
			personal_sex_text.setText("女");
		}

		ImageLoader.getInstance().displayImage(myself.getFace(),
				circleImageView);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		setValue();
		super.onResume();
	}

	/**
	 * 操作选择
	 * 
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items) {
		AlertDialog imageDialog = new AlertDialog.Builder(this)
				.setTitle("上传头像")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 相册选图
						if (item == 0) {
							startImagePick();
						}
						// 手机拍照
						else if (item == 1) {
							startActionCamera();
						}
					}
				}).create();

		imageDialog.show();
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	private void startImagePick() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
	}

	/**
	 * 相机拍照
	 * 
	 * @param output
	 */
	private void startActionCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.getCameraTempFile());
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
	}

	/**
	 * 拍照后裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", CROP);// 输出图片大小
		intent.putExtra("outputY", CROP);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent,
				ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	// 裁剪头像的绝对路径
	private Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			Utils.showSuperCardToast(PersonActivity.this,
					"无法保存上传的头像，请检查SD卡是否挂载");
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (StringUtils.isEmpty(thePath)) {
			thePath = ImageUtils.getAbsoluteImagePath(PersonActivity.this, uri);
		}
		String ext = FileUtils.getFileFormat(thePath);
		ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName = "yijia_crop_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	// 拍照保存的绝对路径
	private Uri getCameraTempFile() {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			Utils.showSuperCardToast(PersonActivity.this,
					"无法保存上传的头像，请检查SD卡是否挂载");
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		// 照片命名
		String cropFileName = "yijia_camera_" + timeStamp + ".jpg";
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);
		cropUri = Uri.fromFile(protraitFile);
		this.origUri = this.cropUri;
		return this.cropUri;
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
			startActionCrop(origUri);// 拍照后裁剪
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
			startActionCrop(data.getData());// 选图后裁剪
			break;
		case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
			protraitBitmap = ImageUtils
					.loadImgThumbnail(protraitPath, 200, 200);
			circleImageView.setImageBitmap(protraitBitmap);
			upload();
			break;
		}
	}

	private void upload() {
		// TODO Auto-generated method stub
		if (Utils.isNetWorkConnected(PersonActivity.this)) {
			showProgressDialog(PersonActivity.this, "上传中");
			String url = UrlConfig.updateHeadImg();
			RequestParams params = new RequestParams();
			params.put("uid", myself.getId());
			try {
				params.put("photo", new File(protraitPath));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// HttpUtil.post(url, params, new jsonHttpResponseHandler() {
			// @Override
			// public void onSuccess(JSONObject arg0) {
			// // TODO Auto-generated method stub
			//
			// if (Utils.requestOk(arg0)) {
			// Utils.ToastMessage(PersonActivity.this, "上传成功");
			// // PreferenceUtils.getInstance(PersonActivity.this)
			// // .setUserImg("file:/" + protraitPath);
			// }
			// super.onSuccess(arg0);
			// }
			//
			// });
			// } else {
			// Utils.showSuperCardToast(PersonActivity.this, getResources()
			// .getString(R.string.network_not_connected));
			// }
		}

		class AlterNickDialog extends Dialog {
			private EditText dialog_alter_person_et;
			private Button dialog_alter_person_ok;

			private Context context;

			public AlterNickDialog(Context context) {
				super(context, R.style.Dialog);
				// TODO Auto-generated constructor stub
				this.context = context;

			}

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				super.onCreate(savedInstanceState);
				setContentView(R.layout.dialog_alter_nick);
				init();
			}

			private void init() {
				// TODO Auto-generated method stub
				dialog_alter_person_et = (EditText) findViewById(R.id.dialog_alter_person_et);
				dialog_alter_person_ok = (Button) findViewById(R.id.dialog_alter_person_ok);
				dialog_alter_person_ok
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub

								if (!StringUtils.isEmpty(dialog_alter_person_et
										.getText().toString())) {

								} else {
									Utils.ToastMessage(context, "昵称不能为空");
								}
							}
						});

			}
		}

	}

}
