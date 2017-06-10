package com.app.cloud.Util;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 图片缓存技术的核心类，在程序内存达到设定值时会将最少最近使用的图片移除掉。
 * 主要缓存圈子图片
 *
 */
public class BitmapCache implements ImageCache {

	private static BitmapCache bitmapCache;

	public LruCache<String, Bitmap> mCache;

	public BitmapCache() {
		// LruCache通过构造函数传入缓存值，以KB为单位。
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		//  使用最大可用内存值的1/8作为缓存的大小。
		int cacheSize = maxMemory / 8;
		mCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	// 唯一实例
	public static BitmapCache instance() {
		if (bitmapCache == null) {
			bitmapCache = new BitmapCache();
		}
		return bitmapCache;
	}

	@Override
	public Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if (getBitmap(url) == null)
			mCache.put(url, bitmap);
	}

	public void changeBitmap(String url, Bitmap bitmap) {
		if (getBitmap(url) == null)
			mCache.put(url, bitmap);
		else {
			mCache.remove(url);
			mCache.put(url, bitmap);
		}
	}

}
