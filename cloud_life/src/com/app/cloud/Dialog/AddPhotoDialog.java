package com.app.cloud.Dialog;

import com.app.cloud.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Ìí¼ÓÍ¼Æ¬¶Ô»°¿ò
 *
 */
public class AddPhotoDialog extends Dialog implements android.view.View.OnClickListener {

	private Button camera;
	private Button picture;
	private Button delete;
	private Button cancel;

	private Context context;

	private OnAddPhotoListener onAddPhotoListener;

	private String strAdd;

	public AddPhotoDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_photo);

		cancel = (Button) findViewById(R.id.btn_cancel);

		camera = (Button) findViewById(R.id.to_camera);
		picture = (Button) findViewById(R.id.to_picture);
		delete = (Button) findViewById(R.id.to_delete);

		cancel.setOnClickListener(this);

		camera.setOnClickListener(this);
		picture.setOnClickListener(this);
		delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.to_camera:
			if (onAddPhotoListener != null) {
				strAdd = "1";
				onAddPhotoListener.onClick(strAdd);
			}
			dismiss();
			break;
		case R.id.to_picture:
			if (onAddPhotoListener != null) {
				strAdd = "2";
				onAddPhotoListener.onClick(strAdd);
			}
			dismiss();
			break;
		case R.id.to_delete:
			if (onAddPhotoListener != null) {
				strAdd = "3";
				onAddPhotoListener.onClick(strAdd);
			}
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		}
	}

	public void setonAddPhotoListener(OnAddPhotoListener onAddPhotoListener) {
		this.onAddPhotoListener = onAddPhotoListener;
	}

	public interface OnAddPhotoListener {
		public void onClick(String add);
	}

}
