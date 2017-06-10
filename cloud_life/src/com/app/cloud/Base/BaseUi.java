package com.app.cloud.Base;

import com.app.cloud.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * 活动基类
 */
public class BaseUi extends AppCompatActivity {

	ProgressDialog load;

	private Toolbar mToolbar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 禁止横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	/**
	 * 初始化toolbar 
	 * 对于有menu，菜品菜单运动详情，登录注册不使用该方法
	 * @param title 标题
	 */
	public void initToolbar(String title) {

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(title);
		mToolbar.setBackgroundColor(getResources().getColor(R.color.top_bar));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// life cycle
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	// util method

	// 消息提示
	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// 在当前界面之上覆盖目标界面
	public void overlay(Class<?> classObj) {
		Intent intent = new Intent(Intent.ACTION_VIEW);

		// 设置启动模式
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		intent.setClass(this, classObj);
		startActivity(intent);
	}

	// 带数据
	public void overlay(Class<?> classObj, Bundle params) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setClass(this, classObj);
		intent.putExtras(params);
		startActivity(intent);
	}

	// 切换当前界面至目标界面
	public void forward(Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		this.startActivity(intent);
		this.finish();
	}

	// 带数据
	public void forward(Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
	}

	// 获取当前界面的上下文对象
	public Context getContext() {
		return this;
	}

	// 根据id获取对应的模板对象
	public LayoutInflater getLayout() {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getLayout(int layoutId) {
		return getLayout().inflate(layoutId, null);
	}

	public View getLayout(int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}

	// 打开加载框
	public void openLoad(String message) {
		load = new ProgressDialog(this.getContext());
		load.setMessage(message);
		load.setCancelable(false);
		load.show();
	}

	// 关闭加载框
	public void closeLoad() {
		load.dismiss();
	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// logic method

	// 结束当前界面
	public void doFinish() {
		this.finish();
	}
}
