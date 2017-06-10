package com.app.cloud.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.app.cloud.R;
import com.app.cloud.Base.C;
import com.app.cloud.Model.QuanZi;
import com.app.cloud.Ui.CircleImageView;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户动态列表适配器
 *
 */
public class QuanAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	public ArrayList<QuanZi> list = new ArrayList<QuanZi>();
	RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

	public QuanAdapter(Context context, ArrayList<QuanZi> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;

	}

	public QuanAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	// listview有多少个item
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final QuanZi quan = list.get(position);

		// convertView == null 造成图片错乱
		if (true) {
			convertView = inflater.inflate(R.layout.item_quan, null);
			holder = new ViewHolder();
			holder.icon = (CircleImageView) convertView.findViewById(R.id.quan_icon);
			holder.name = (TextView) convertView.findViewById(R.id.quan_name);
			holder.time = (TextView) convertView.findViewById(R.id.quan_time);
			holder.city = (TextView) convertView.findViewById(R.id.quan_city);
			holder.content = (TextView) convertView.findViewById(R.id.quan_text);
			holder.image = (ImageView) convertView.findViewById(R.id.quan_image);

			// 将viewholder存储在view中
			convertView.setTag(holder);
		} else {
			// 重新获取viewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(quan.getName());
		holder.time.setText(quan.getTime());
		holder.city.setText(quan.getCity());
		holder.content.setText(quan.getContent());

		if (!quan.getImage().equals("")) {
			// 判断本地是否存在
			File f = new File(C.dir.imgCache + quan.getImage().substring(13));
			if (f.exists()) {
				// sd卡本地图片放入缓存
				// "#W100#H100"为缓存前缀
				BitmapCache.instance().putBitmap("#W100#H100" + C.api.base + "/img" + quan.getImage(),
						decodeSampledBitmapFromFile(C.dir.imgCache + quan.getImage().substring(13), 100, 100));
			}
			// 判断从缓存加载图片
			ImageListener imageListener = ImageLoader.getImageListener(holder.image, R.drawable.image_default,
					R.drawable.image_fail);
			ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.instance());
			imageLoader.get(C.api.base + "/img" + quan.getImage(), imageListener, 100, 100);

		} else
			holder.image.setVisibility(View.GONE);

		if (quan.getIcon() != null) {
			// 判断本地是否存在
			File f = new File(C.dir.icon + quan.getIcon().substring(10));
			if (f.exists()) {
				// sd卡本地头像放入缓存
				// "#W100#H100"为缓存前缀
				BitmapCache.instance().putBitmap("#W100#H100" + C.api.base + "/img" + quan.getIcon(),
						decodeSampledBitmapFromFile(C.dir.icon + quan.getIcon().substring(10), 100, 100));
			}

			// 加载头像
			ImageListener imageListener = ImageLoader.getImageListener(holder.icon, R.drawable.icon_profile,
					R.drawable.icon_profile);
			ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.instance());
			imageLoader.get(C.api.base + "/img" + quan.getIcon(), imageListener, 100, 100);
		}

		return convertView;
	}

	// 获取控件实例优化
	class ViewHolder {
		CircleImageView icon;
		TextView name;
		TextView time;
		TextView city;
		TextView content;
		ImageView image;
	}

	/**
	 * 
	 * @param path
	 *            根据sd卡路径加载图片并压缩
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
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
