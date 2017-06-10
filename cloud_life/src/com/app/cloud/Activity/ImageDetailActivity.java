package com.app.cloud.Activity;

import java.io.File;

import com.app.cloud.R;
import com.app.cloud.Base.C;
import com.app.cloud.Ui.ZoomImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

/**
 * 图片详情
 *
 */
public class ImageDetailActivity extends Activity {

	private ZoomImageView zoomImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_detail);

		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_imageview);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String imgId = bundle.getString("imgId");

		// 从sd卡找原图不压缩
		File f = new File(C.dir.imgCache + imgId.substring(13));
		if (f.exists())
			zoomImageView.setImageBitmap(BitmapFactory.decodeFile(C.dir.imgCache + imgId.substring(13)));
		else
			zoomImageView.setImageBitmap(null);

	}

}
