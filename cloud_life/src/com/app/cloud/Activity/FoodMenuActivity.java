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
import com.app.cloud.Model.FoodMenu;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
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
 * ��������
 *
 */
public class FoodMenuActivity extends BaseUiAuth {

	private Toolbar mToolbar;

	FoodMenu menu;

	ImageView image;

	TextView body;
	TextView effect;
	TextView m_main;
	TextView m_assist;
	TextView flavor;
	TextView make;

	RequestQueue mQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foodmenu);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		menu = (FoodMenu) bundle.getSerializable("menu");

		inToolbar(menu.getName());
		initChange();

		final CoordinatorLayout image = (CoordinatorLayout) findViewById(R.id.foodmenu_bg);

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

		// ʹ��CollapsingToolbarLayout�����title���õ�CollapsingToolbarLayout�ϣ����õ�Toolbar���򲻻���ʾ
		CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
		mCollapsingToolbarLayout.setTitle(title);
		// ͨ��CollapsingToolbarLayout�޸�������ɫ
		// ���û�û����ʱ״̬��������ɫ
		mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#1E90FF"));
		// ����������Toolbar���������ɫ
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
		image = (ImageView) findViewById(R.id.foodmenu_image);

		if (!menu.getImage().equals("")) {
			// �жϱ���ͼƬ���������
			File f = new File(C.dir.foodImg + menu.getImage().substring(5));
			if (!f.exists()) {
				// ����ͼƬ
				ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + menu.getImage(),
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								image.setImageBitmap(response);
								// ���ظ��屣�浽����
								try {
									saveBitmap(menu.getImage().substring(5), response);
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
				image.setImageBitmap(BitmapFactory.decodeFile(C.dir.foodImg + menu.getImage().substring(5)));
			}
		}
		flavor = (TextView) findViewById(R.id.foodmenu_flavor);
		effect = (TextView) findViewById(R.id.foodmenu_effect);
		m_main = (TextView) findViewById(R.id.foodmenu_metial_main);
		m_assist = (TextView) findViewById(R.id.foodmenu_metial_assist);
		make = (TextView) findViewById(R.id.foodmenu_make);
		body = (TextView) findViewById(R.id.foodmenu_body);

		flavor.setText(menu.getFlavor());
		effect.setText(menu.getEffect());
		m_main.setText(menu.getMaterial_main());
		m_assist.setText(menu.getMaterial_assist());
		make.setText(menu.getHow_make());
		body.setText(menu.getBodyType());
	}

	// ����ͼƬjpg��ʽ ��ѹ�� ��sd��
	public void saveBitmap(String resourceId, Bitmap bitmap) throws IOException {

		// ����ļ��д��ڷ�
		File imageDir = new File(C.dir.foodImg);
		if (!imageDir.exists())
			imageDir.mkdirs();

		// ·��������
		File f = new File(C.dir.foodImg + resourceId);
		if (f.exists())
			return;

		// ��ʼ����
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
