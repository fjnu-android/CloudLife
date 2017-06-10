package com.app.cloud.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Dialog.AddPhotoDialog;
import com.app.cloud.Dialog.AddPhotoDialog.OnAddPhotoListener;
import com.app.cloud.Util.FormImage;
import com.app.cloud.Util.PostUploadRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 发表动态
 *
 */
public class PublishActivity extends BaseUiAuth implements View.OnClickListener {

	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	public static final int CHOOSE_PHOTO = 3;

	private Toolbar mToolbar;

	EditText text;
	Button clear;
	ImageView photo;

	AlertDialog.Builder dialog;

	// 照相图片路径
	private Uri imageUri = null;

	// 使用时间戳
	private Long photoName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);

		initToolbar();
		createSDCardDir();
		initView();

	}

	public void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("发表动态");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// menu
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.publish:
					if (text.length() == 0)
						toast("内容不能为空");
					else {
						openLoad("正在发表动态...");
						publish();
					}
					break;
				default:
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_publish, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	private void initView() {
		text = (EditText) findViewById(R.id.publish_text);
		clear = (Button) findViewById(R.id.publish_delete);
		photo = (ImageView) findViewById(R.id.publish_photo);
		clear.setOnClickListener(this);
		photo.setOnClickListener(this);
	}

	// 发表动态
	private void publish() {
		RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

		FormImage image;

		if (imageUri != null) {
			Bitmap bitmap = null;
			try {// 将路径转化成bitmap
				bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			image = new FormImage(bitmap);
		} else
			image = null;

		PostUploadRequest post = new PostUploadRequest(C.api.publish, text.getText().toString(), image,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							closeLoad();
							if (jsonObject.getString("status").equals("1")) {
								toast("发表成功");
								finish();
							}
							if (jsonObject.getString("status").equals("-1")) {
								toast("cookie失效,发表失败");
							}
							if (jsonObject.getString("status").equals("-2")) {
								toast("服务器错误,发表失败");
							}
							if (jsonObject.getString("status").equals("0")) {
								toast("内容为空,发表失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						closeLoad();
						Log.e("VolleyError", error.getMessage(), error);
						toast("网络错误，发表失败！");
					}
				});

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publish_delete:
			if (!(text.length() == 0))
				openDialog();
			break;
		case R.id.publish_photo:
			AddPhotoDialog mAddPhotoDialog = new AddPhotoDialog(this);
			mAddPhotoDialog.show();
			mAddPhotoDialog.setonAddPhotoListener(new OnAddPhotoListener() {
				@Override
				public void onClick(String add) {
					// 启动相机
					if (add.equals("1")) {
						// 保证每张图片名字不一样
						photoName = System.currentTimeMillis();
						File addImage = new File(C.dir.user, photoName.toString() + ".jpg");
						try {
							if (addImage.exists()) {
								addImage.delete();
							}
							addImage.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// 将file转成uri,uri代表图片唯一地址
						imageUri = Uri.fromFile(addImage);
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
						startActivityForResult(intent, TAKE_PHOTO);
					}
					// 打开相册
					if (add.equals("2")) {
						Intent intent = new Intent("android.intent.action.GET_CONTENT");
						intent.setType("image/*");
						startActivityForResult(intent, CHOOSE_PHOTO);
					}
					if (add.equals("3")) {
						photo.setImageBitmap(null);
					}
				}
			});
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				// 设置裁剪
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			// 压缩图片
			if (resultCode == RESULT_OK) {
				try {
					// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, options);
					// 调用上面定义的方法计算inSampleSize值
					options.inSampleSize = calculateInSampleSize(options, 100, 100);
					// 使用获取到的inSampleSize值再次解析图片
					options.inJustDecodeBounds = false;
					photo.setImageBitmap(
							BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri), null, options));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		case CHOOSE_PHOTO:
			// 判断手机系统版本，选择方法
			if (resultCode == RESULT_OK) {
				if (Build.VERSION.SDK_INT >= 19) {
					handleImageOnKitKat(data);
				} else {
					handleImageBeforeKitKat(data);
				}
			}
			break;
		}
	}

	private void handleImageBeforeKitKat(Intent data) {
		Uri uri = data.getData();
		String imagePath = getImagePath(uri, null);
		displayImage(imagePath);
	}

	@TargetApi(19)
	private void handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if (DocumentsContract.isDocumentUri(this, uri)) {
			// 如果是document类型uri，则通过document id 处理
			String docId = DocumentsContract.getDocumentId(uri);
			if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
				String id = docId.split(":")[1];// 解析出数字格式的id
				String selection = MediaStore.Images.Media._ID + "=" + id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
			} else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// 如果不是document类型uri，则通过普通方式处理
			imagePath = getImagePath(uri, null);
		}
		displayImage(imagePath);
	}

	private String getImagePath(Uri uri, String selection) {
		String path = null;
		// 通过uri和selection获取真实的图片路径
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			}
			cursor.close();
		}
		return path;
	}

	// 根据路径显示图片
	private void displayImage(String imagePath) {
		if (imagePath != null) {
			File file = new File(imagePath);
			imageUri = Uri.fromFile(file);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(imageUri, "image/*");
			intent.putExtra("scale", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CROP_PHOTO);
		} else {
			toast("获取图片失败");
		}
	}

	// 在SD卡上创建一个文件夹
	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File path = new File(C.dir.user);
			if (!path.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path.mkdirs();
			}
		}
	}

	private void openDialog() {
		dialog = new AlertDialog.Builder(getContext());
		dialog.setMessage("你确定清楚当前编辑内容吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				text.setText("");
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}
