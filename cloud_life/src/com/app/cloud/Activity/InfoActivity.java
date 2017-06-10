package com.app.cloud.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Dialog.ChangeAddressDialog;
import com.app.cloud.Dialog.ChangeAddressDialog.OnAddressCListener;
import com.app.cloud.Dialog.ChangeBirthDialog;
import com.app.cloud.Dialog.ChangeBirthDialog.OnBirthListener;
import com.app.cloud.Dialog.ChangeHeightDialog;
import com.app.cloud.Dialog.ChangeHeightDialog.OnHeightCListener;
import com.app.cloud.Dialog.ChangeIconDialog;
import com.app.cloud.Dialog.ChangeIconDialog.OnChangeIconListener;
import com.app.cloud.Dialog.ChangeSexDialog;
import com.app.cloud.Dialog.ChangeSexDialog.OnSexCListener;
import com.app.cloud.Dialog.ChangeWeightDialog;
import com.app.cloud.Dialog.ChangeWeightDialog.OnWeightCListener;
import com.app.cloud.Dialog.ChangeWorkDialog;
import com.app.cloud.Dialog.ChangeWorkDialog.OnWorkCListener;
import com.app.cloud.Ui.CircleImageView;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.FormImage;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.PostUploadRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 个人信息
 */
public class InfoActivity extends BaseUiAuth implements View.OnClickListener {

	private Toolbar mToolbar;

	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	public static final int CHOOSE_PHOTO = 3;

	// 照相图片路径
	private Uri imageUri = null;

	// 使用时间戳
	private Long photoName;

	TextView phone;
	EditText name;
	EditText sign;
	TextView body;
	TextView sex;
	TextView height;
	TextView weight;
	TextView birth;
	TextView city;
	TextView work; 

	RelativeLayout bg;
	CircleImageView icon;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		mQueue = SingleRequestQueue.getRequestQueue();

		initToolbar();
		initView();

	}

	// 加载个人信息
	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		if (customer.getIcon().equals("")) {
			icon.setImageResource(R.drawable.icon_profile);
			;
		} else {
			bg.setBackground(new BitmapDrawable(
					blur(BitmapCache.instance().getBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon()))));
			icon.setImageBitmap(
					BitmapCache.instance().getBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon()));
		}
		phone.setText(customer.getPhone());
		name.setHint(customer.getName());
		sign.setHint(customer.getSign());
		body.setText(customer.getBodytype());
		sex.setText(customer.getsex());
		height.setText(customer.getHeight());
		weight.setText(customer.getWeight());
		birth.setText(customer.getBirth());
		city.setText(customer.getCity());
		work.setText(customer.getWork());
	}

	private void initToolbar() {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("个人信息");
		mToolbar.setBackgroundColor(getResources().getColor(R.color.mid_transparent));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// up Button
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
				case R.id.change_info:
					openLoad("正在提交个人信息...");
					submit();
					break;
				default:
				}
				return true;
			}
		});

	}

	private void initView() {

		phone = (TextView) findViewById(R.id.info_phone);
		name = (EditText) findViewById(R.id.info_name);
		sign = (EditText) findViewById(R.id.info_sign);
		body = (TextView) findViewById(R.id.info_bodytype);

		sex = (TextView) findViewById(R.id.info_sex);
		height = (TextView) findViewById(R.id.info_height);
		weight = (TextView) findViewById(R.id.info_weight);
		birth = (TextView) findViewById(R.id.info_birthday);
		city = (TextView) findViewById(R.id.info_city);
		work = (TextView) findViewById(R.id.info_work);

		icon = (CircleImageView) findViewById(R.id.info_icon);
		bg = (RelativeLayout) findViewById(R.id.info_bg);

		weight.setOnClickListener(this);
		height.setOnClickListener(this);
		sex.setOnClickListener(this);
		birth.setOnClickListener(this);
		city.setOnClickListener(this);
		work.setOnClickListener(this);
		icon.setOnClickListener(this);
	}

	private void submit() {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		if (sex.getText().toString().equals("男"))
			urlParams.put("sex", "m");
		else
			urlParams.put("sex", "f");

		urlParams.put("birthday", birth.getText().toString());
		urlParams.put("city", city.getText().toString());
		urlParams.put("height", height.getText().toString());
		urlParams.put("weight", weight.getText().toString());
		urlParams.put("work", work.getText().toString());

		if (name.getText().toString().equals(""))
			urlParams.put("name", name.getHint().toString());
		else
			urlParams.put("name", name.getText().toString());

		if (sign.getText().toString().equals(""))
			urlParams.put("sign", sign.getHint().toString());
		else
			urlParams.put("sign", sign.getText().toString());

		// 提交体质请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.info, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					closeLoad();
					if (jsonObject.getString("status").equals("1")) {
						toast("提交成功");
						changeInfo();
						onStart();
					}
					if (jsonObject.getString("status").equals("-1"))
						toast("cookie失效，提交失败");

					if (jsonObject.getString("status").equals("-2"))
						toast("信息不完整，提交失败");

					if (jsonObject.getString("status").equals("0"))
						toast("服务器异常，提交失败");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				closeLoad();
				Log.e("TAG", error.getMessage(), error);
				toast("网络错误，提交失败！");
			}
		}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	private void changeInfo() {

		customer.setsex(sex.getText().toString());
		customer.setBirth(birth.getText().toString());
		customer.setCity(city.getText().toString());
		customer.setHeight(height.getText().toString());
		customer.setWeight(weight.getText().toString());
		customer.setWork(work.getText().toString());

		if (!name.getText().toString().equals(""))
			customer.setName(name.getText().toString());

		if (!sign.getText().toString().equals(""))
			customer.setSign(sign.getText().toString());

	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.info_birthday:
				ChangeBirthDialog mChangeBirthDialog = new ChangeBirthDialog(this);
				mChangeBirthDialog.setDate(1995, 01, 01);
				mChangeBirthDialog.show();
				mChangeBirthDialog.setBirthdayListener(new OnBirthListener() {
					@Override
					public void onClick(String year, String month, String day) {
						birth.setText(year + "-" + month + "-" + day);
					}
				});
				break;
			case R.id.info_city:
				ChangeAddressDialog mChangeAddressDialog = new ChangeAddressDialog(this);
				mChangeAddressDialog.setAddress("福建", "福州");
				mChangeAddressDialog.show();
				mChangeAddressDialog.setAddresskListener(new OnAddressCListener() {
					@Override
					public void onClick(String province, String City) {
						city.setText(province + "-" + City);
					}
				});
				break;
			case R.id.info_sex:
				ChangeSexDialog mChangeSexDialog = new ChangeSexDialog(this);
				mChangeSexDialog.setSex("男");
				mChangeSexDialog.show();
				mChangeSexDialog.setSexkListener(new OnSexCListener() {
					@Override
					public void onClick(String Sex) {
						sex.setText(Sex);
					}
				});
				break;
			case R.id.info_height:
				ChangeHeightDialog mChangeHeightDialog = new ChangeHeightDialog(this);
				mChangeHeightDialog.setHeight("170");
				mChangeHeightDialog.show();
				mChangeHeightDialog.setHeightkListener(new OnHeightCListener() {
					@Override
					public void onClick(String Height) {
						height.setText(Height);
					}
				});
				break;
			case R.id.info_weight:
				ChangeWeightDialog mChangeWeightDialog = new ChangeWeightDialog(this);
				mChangeWeightDialog.setWeight("60");
				mChangeWeightDialog.show();
				mChangeWeightDialog.setWeightkListener(new OnWeightCListener() {
					@Override
					public void onClick(String Weight) {
						weight.setText(Weight);
					}
				});
				break;
			case R.id.info_work:
				ChangeWorkDialog mChangeWorkDialog = new ChangeWorkDialog(this);
				mChangeWorkDialog.setWork("学生");
				mChangeWorkDialog.show();
				mChangeWorkDialog.setWorkkListener(new OnWorkCListener() {
					@Override
					public void onClick(String Work) {
						work.setText(Work);
					}
				});
				break;
			case R.id.info_icon:
				ChangeIconDialog mChangeIconDialog = new ChangeIconDialog(this);
				mChangeIconDialog.show();
				mChangeIconDialog.setonChangeIconListener(new OnChangeIconListener() {
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
					}
				});
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 上传头像
	private void changeIcon() {
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

		PostUploadRequest post = new PostUploadRequest(C.api.userIcon, null, image,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								onStart();
							}
							if (jsonObject.getString("status").equals("-1")) {
								toast("cookie失效,修改失败");
							}
							if (jsonObject.getString("status").equals("-2")) {
								toast("服务器错误,修改失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("VolleyError", error.getMessage(), error);
						toast("网络错误，修改失败！");
					}
				});

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
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
				// 设置宽高的比例
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				//  outputX outputY 是裁剪后图片宽高    
				intent.putExtra("outputX", 320);
				intent.putExtra("outputY", 320);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			// 压缩
			if (resultCode == RESULT_OK) {
				// 修改缓存图片，并上传
				try {
					BitmapCache.instance().changeBitmap("#W100#H100" + C.api.base + "/img" + customer.getIcon(),
							BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				changeIcon();
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
			// 设置宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			//  outputX outputY 是裁剪后图片宽高    
			intent.putExtra("outputX", 320);
			intent.putExtra("outputY", 320);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	// 背景虚化
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private Bitmap blur(Bitmap bkg) {
		long startMs = System.currentTimeMillis();
		float radius = 5;

		bkg = small(bkg);
		Bitmap bitmap = bkg.copy(bkg.getConfig(), true);

		final RenderScript rs = RenderScript.create(this.getContext());
		final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);

		rs.destroy();
		Log.d("wallPaper", "blur take away:" + (System.currentTimeMillis() - startMs) + "ms");
		return bitmap;
	}

	private static Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.25f, 0.25f); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
}
