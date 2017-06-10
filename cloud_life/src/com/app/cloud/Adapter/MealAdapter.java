package com.app.cloud.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.app.cloud.R;
import com.app.cloud.Base.C;
import com.app.cloud.Model.DietMeal;
import com.app.cloud.Model.FoodMenu;
import com.app.cloud.Util.SingleRequestQueue;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 饮食界面三餐列表适配器
 *
 */
public class MealAdapter extends BaseExpandableListAdapter {

	RequestQueue mQueue = SingleRequestQueue.getRequestQueue();

	public ArrayList<DietMeal> group;
	private LayoutInflater inflater;

	public MealAdapter(Context context, ArrayList<DietMeal> group) {
		inflater = LayoutInflater.from(context);
		this.group = group;
	}

	@Override
	public FoodMenu getChild(int groupPosition, int childPosition) {
		return group.get(groupPosition).getChild(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return group.get(groupPosition).getChildSize();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		DietMeal groupObject = (DietMeal) getGroup(groupPosition);
		String groupname = groupObject.getGroup();
		convertView = inflater.inflate(R.layout.item_meal_tag, null);
		TextView tag = (TextView) convertView.findViewById(R.id.meal_tag);
		tag.setText(groupname);

		LinearLayout bg = (LinearLayout) convertView.findViewById(R.id.meal_bg);
		if (group.get(groupPosition).group == "早餐")
			bg.setBackgroundResource(R.drawable.breakfast);
		if (group.get(groupPosition).group == "午餐")
			bg.setBackgroundResource(R.drawable.lunch);
		if (group.get(groupPosition).group == "晚餐")
			bg.setBackgroundResource(R.drawable.dinner);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		FoodMenu food = (FoodMenu) getChild(groupPosition, childPosition);
		convertView = inflater.inflate(R.layout.item_meal, null);

		final ImageView icon = (ImageView) convertView.findViewById(R.id.meal_icon);
		TextView body = (TextView) convertView.findViewById(R.id.meal_body);
		TextView name = (TextView) convertView.findViewById(R.id.meal_name);
		TextView introduce = (TextView) convertView.findViewById(R.id.meal_introduce);

		introduce.setText(food.getEffect());
		name.setText(food.getName());

		if (!food.getBodyType().equals(""))
			body.setText(food.getBodyType());
		else
			body.setVisibility(View.GONE);

		if (!food.getImage().equals("")) {
			// 判断本地是否存在
			File f = new File(C.dir.foodImg + food.getImage().substring(5));
			if (!f.exists()) {
				// 加载图片
				ImageRequest imageRequest = new ImageRequest(C.api.base + "/img" + food.getImage(),
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								icon.setImageBitmap(response);
							}
						}, 100, 100, Config.RGB_565, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								icon.setImageResource(R.drawable.image_fail);
							}
						});
				mQueue.add(imageRequest);
			} else {
				icon.setImageBitmap(decodeSampledBitmapFromFile(C.dir.foodImg + food.getImage().substring(5), 80, 80));
			}
		}

		return convertView;
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
