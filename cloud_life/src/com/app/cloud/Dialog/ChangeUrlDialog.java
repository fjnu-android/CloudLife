package com.app.cloud.Dialog;

import com.app.cloud.R;
import com.app.cloud.Base.C;
import com.app.cloud.Dialog.AddPhotoDialog.OnAddPhotoListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 修改服务器路径
 *
 */
public class ChangeUrlDialog extends Dialog implements android.view.View.OnClickListener {

	private TextView present;
	private EditText after;
	private TextView sure;
	private TextView cancel;
	
	private Context context;
	private String strAdd;
	private OnChangeUrlListener onChangeUrlListener;
	
	public ChangeUrlDialog(Context context) {
		super(context, R.style.ShareDialog);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_change_url);
		
		cancel = (TextView) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(this);
		sure = (TextView) findViewById(R.id.btn_sure);
		sure.setOnClickListener(this);
		
		present = (TextView) findViewById(R.id.changeurl_present);
		present.setText(C.api.base);
		
		after = (EditText) findViewById(R.id.changeurl_after);
		
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_sure:
			if (onChangeUrlListener != null) {
				strAdd = after.getText().toString();
				onChangeUrlListener.onClick(strAdd);
			}
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		}
	}
	
	public void setonChangeUrlListener(OnChangeUrlListener onChangeUrlListener) {
		this.onChangeUrlListener = onChangeUrlListener;
	}

	public interface OnChangeUrlListener {
		public void onClick(String url);
	}
	
}
