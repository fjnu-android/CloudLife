package com.app.cloud.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.app.cloud.R;
import com.app.cloud.Base.BaseUiAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.QuanZi;
import com.app.cloud.Ui.CircleImageView;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 圈子详情
 *
 */
public class QuanDetailActivity extends BaseUiAuth {

	CircleImageView icon;
	ImageView image;
	TextView name;
	TextView city;
	TextView time;
	TextView content;

	RequestQueue mQueue;
	QuanZi quanzi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_detail);

		initToolbar("");

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		quanzi = (QuanZi) bundle.getSerializable("quanzi");

		initView();
	}

	private void initView() {

		icon = (CircleImageView) findViewById(R.id.detail_icon);
		image = (ImageView) findViewById(R.id.detail_image);
		name = (TextView) findViewById(R.id.detail_name);
		city = (TextView) findViewById(R.id.detail_city);
		time = (TextView) findViewById(R.id.detail_time);
		content = (TextView) findViewById(R.id.detail_text);

		name.setText(quanzi.getName());
		city.setText(quanzi.getCity());
		time.setText(quanzi.getTime().substring(0, 16));
		content.setText(quanzi.getContent());

		mQueue = SingleRequestQueue.getRequestQueue();

		if (quanzi.getIcon() != null) {
			// 判断本地头像如果不存在
			File f = new File(C.dir.icon + quanzi.getIcon().substring(10));
			if (!f.exists()) {
				ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + quanzi.getIcon(),
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								try {
									// 下载高清头像保存到本地
									saveBitmapIcon(quanzi.getIcon().substring(10), response);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}, 0, 0, Config.ARGB_8888, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						});
				mQueue.add(imageRequest);
			}
			// 从缓存中获取头像
			icon.setImageBitmap(
					BitmapCache.instance().getBitmap("#W100#H100" + C.api.base + "/img" + quanzi.getIcon()));
		}

		if (!quanzi.getImage().equals("")) {
			// 判断本地图片如果不存在
			File f = new File(C.dir.imgCache + quanzi.getImage().substring(13));
			if (!f.exists()) {
				ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + quanzi.getImage(),
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								try {
									// 下载高清保存到本地
									saveBitmap(quanzi.getImage().substring(13), response);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}, 0, 0, Config.ARGB_8888, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						});
				mQueue.add(imageRequest);
			}
			// 从缓存中获取图片
			image.setImageBitmap(
					BitmapCache.instance().getBitmap("#W100#H100" + C.api.base + "/img" + quanzi.getImage()));
		} else
			image.setVisibility(View.GONE);

		// 传图片路径
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("imgId", quanzi.getImage());
				overlay(ImageDetailActivity.class, bundle);
			}
		});

	}

	// 保存图片jpg格式 不压缩 到sd卡
	public void saveBitmap(String resourceId, Bitmap bitmap) throws IOException {

		// 检查文件夹存在否
		File imageDir = new File(C.dir.imgCache);
		if (!imageDir.exists())
			imageDir.mkdirs();

		// 路径加名字
		File f = new File(C.dir.imgCache + resourceId);
		if (f.exists())
			return;

		// 开始保存
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 保存头像jpg格式 不压缩 到sd卡
	public void saveBitmapIcon(String resourceId, Bitmap bitmap) throws IOException {

		// 检查文件夹存在否
		File imageDir = new File(C.dir.icon);
		if (!imageDir.exists())
			imageDir.mkdirs();

		// 路径加名字
		File f = new File(C.dir.icon + resourceId);
		if (f.exists())
			return;

		// 开始保存
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
