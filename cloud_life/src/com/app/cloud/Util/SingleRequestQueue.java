package com.app.cloud.Util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.cloud.Base.BaseApp;

import android.content.Context;

/**
 * 保持项目中唯一volley的requestQueue
 * 使用全局context
 */
public class SingleRequestQueue {

	private static RequestQueue mQueue;

	private SingleRequestQueue(Context context) {
		mQueue = Volley.newRequestQueue(context);
	}

	public static synchronized RequestQueue getRequestQueue() {
		if (mQueue == null) {
			new SingleRequestQueue(BaseApp.getAppContext());
		}
		return mQueue;
	}

}
