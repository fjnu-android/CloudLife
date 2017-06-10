package com.app.cloud.Dialog;

import com.app.cloud.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 选择头像对话框
 *
 */
public class ChangeIconDialog extends Dialog implements android.view.View.OnClickListener {

	private Button camera;
	private Button picture;
	private Button cancel;

	private Context context;

	private OnChangeIconListener onChangeIconListener;

	private String strIcon;

	public ChangeIconDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_icon_change);

		cancel = (Button) findViewById(R.id.btn_cancel);

		camera = (Button) findViewById(R.id.to_camera);
		picture = (Button) findViewById(R.id.to_picture);

		cancel.setOnClickListener(this);

		camera.setOnClickListener(this);
		picture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.to_camera:
			if (onChangeIconListener != null) {
				strIcon = "1";
				onChangeIconListener.onClick(strIcon);
			}
			dismiss();
			break;
		case R.id.to_picture:
			if (onChangeIconListener != null) {
				strIcon = "2";
				onChangeIconListener.onClick(strIcon);
			}
			dismiss();
			break;

		case R.id.btn_cancel:
			dismiss();
			break;
		}
	}

	public void setonChangeIconListener(OnChangeIconListener onChangeIconListener) {
		this.onChangeIconListener = onChangeIconListener;
	}

	public interface OnChangeIconListener {
		public void onClick(String add);
	}

}
