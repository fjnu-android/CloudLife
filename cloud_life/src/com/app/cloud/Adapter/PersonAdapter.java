package com.app.cloud.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.app.cloud.R;
import com.app.cloud.Base.BaseAuth;
import com.app.cloud.Base.C;
import com.app.cloud.Model.QuanZi;
import com.app.cloud.Model.User;
import com.app.cloud.Util.BitmapCache;
import com.app.cloud.Util.JsonObjectPostRequest;
import com.app.cloud.Util.SingleRequestQueue;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 个人动态列表适配器 有删除
 */
public class PersonAdapter extends BaseAdapter {

	RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

	private LayoutInflater inflater;
	public ArrayList<QuanZi> list = new ArrayList<QuanZi>();
	Context context;
	AlertDialog.Builder dialog;

	protected static User customer = BaseAuth.getCustomer();
	static PersonAdapter adapter = null;

	public PersonAdapter(Context context, ArrayList<QuanZi> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	public PersonAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public static PersonAdapter instance(Context context) {
		if (adapter == null) {
			adapter = new PersonAdapter(context);
		}
		return adapter;
	}

	// listview有多少个item
	@Override
	public int getCount() {
		return list.size();
	}

	//
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		final QuanZi quan = list.get(position);

		// convertView == null 造成图片错乱
		if (true) {
			convertView = inflater.inflate(R.layout.item_person, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.person_name);
			holder.time = (TextView) convertView.findViewById(R.id.person_time);
			holder.delete = (Button) convertView.findViewById(R.id.person_delete);
			holder.content = (TextView) convertView.findViewById(R.id.person_text);
			holder.image = (ImageView) convertView.findViewById(R.id.person_image);

			holder.delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openDialog(position);
				}
			});
			// 将viewholder存储在view中
			convertView.setTag(holder);
		} else {
			// 重新获取viewholder
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(quan.getName());
		holder.time.setText(quan.getTime());
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

		return convertView;
	}

	private void openDialog(final int position) {
		dialog = new AlertDialog.Builder(context);
		dialog.setMessage("你确定删除该条信息吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteData(position);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialog.show();
	}

	private void deleteData(final int position) {

		HashMap<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("sid", list.get(position).getSid());
		urlParams.put("id", customer.getPhone());

		// 删除请求
		JsonObjectPostRequest post = new JsonObjectPostRequest(C.api.deletePersonData,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							if (jsonObject.getString("status").equals("1")) {
								toast("删除成功");
								list.remove(position);
								adapter.notifyDataSetChanged();
							}
							if (jsonObject.getString("status").equals("-1"))
								toast("cookie失效，删除失败");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						toast("网络错误，删除失败！");
					}
				}, urlParams);

		if (!customer.getCookie().equals("")) {
			// 向服务器发起post请求时加上cookie字段
			post.setSendCookie(customer.getCookie());
		}

		mQueue.add(post);
	}

	public void toast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	// 获取控件实例优化
	class ViewHolder {
		TextView name;
		TextView time;
		Button delete;
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
