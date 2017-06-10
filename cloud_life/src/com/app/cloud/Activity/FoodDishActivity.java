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
import com.app.cloud.Model.FoodDish;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 菜单详情
 *
 */
public class FoodDishActivity extends BaseUiAuth {

	private Toolbar mToolbar;

	FoodDish dish;

	ImageView image;
	TextView body;
	TextView introduction;
	TextView effect;
	TextView select;
	TextView save;
	TextView eat;
	TextView suit;
	TextView unsuit;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fooddish);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		dish = (FoodDish) bundle.getSerializable("dish");

		inToolbar(dish.getName());
		initChange();

		final CoordinatorLayout image = (CoordinatorLayout) findViewById(R.id.fooddish_bg);

		image.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				image.getViewTreeObserver().removeOnPreDrawListener(this);
				image.buildDrawingCache();
				image.setBackground(new BitmapDrawable(getResources(), customer.wallBlurs));
				return true;
			}
		});
	}

	public void inToolbar(String title) {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
		mCollapsingToolbarLayout.setTitle(title);

		// 前
		mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#1E90FF"));
		// 标题颜色后
		mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initChange() {
		mQueue = SingleRequestQueue.getRequestQueue();
		image = (ImageView) findViewById(R.id.fooddish_image);

		// 判断本地图片如果不存在
		File f = new File(C.dir.foodImg + dish.getImage().substring(5));
		if (!f.exists()) {
			// 加载图片
			ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + dish.getImage(),
					new Response.Listener<Bitmap>() {
						@Override
						public void onResponse(Bitmap response) {
							image.setImageBitmap(response);
							// 下载高清保存到本地
							try {
								saveBitmap(dish.getImage().substring(5), response);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}, 0, 0, Config.ARGB_8888, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							image.setImageResource(R.drawable.image_fail);
						}
					});

			mQueue.add(imageRequest);
		} else {
			image.setImageBitmap(BitmapFactory.decodeFile(C.dir.foodImg + dish.getImage().substring(5)));
		}

		body = (TextView) findViewById(R.id.fooddish_body);
		select = (TextView) findViewById(R.id.fooddish_select);
		save = (TextView) findViewById(R.id.fooddish_save);
		introduction = (TextView) findViewById(R.id.fooddish_introduction);
		eat = (TextView) findViewById(R.id.fooddish_how_eat);
		suit = (TextView) findViewById(R.id.fooddish_man_suit);
		unsuit = (TextView) findViewById(R.id.fooddish_man_unsuit);
		effect = (TextView) findViewById(R.id.fooddish_effect);

		body.setText(dish.getBodyType());
		select.setText(dish.getHow_select());
		save.setText(dish.getSave());
		introduction.setText(dish.getIntroduction());
		eat.setText(dish.getHow_eat());
		suit.setText(dish.getMan_suit());
		unsuit.setText(dish.getMan_unsuit());
		effect.setText(dish.getEffect());
	}

	// 保存图片jpg格式 不压缩 到sd卡
	public void saveBitmap(String resourceId, Bitmap bitmap) throws IOException {

		// 检查文件夹存在否
		File imageDir = new File(C.dir.foodImg);
		if (!imageDir.exists())
			imageDir.mkdirs();

		// 路径加名字
		File f = new File(C.dir.foodImg + resourceId);
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
