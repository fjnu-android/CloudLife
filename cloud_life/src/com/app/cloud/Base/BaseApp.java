package com.app.cloud.Base;

import android.app.Application;
import android.content.Context;

/**
 * appliaction基类
 *
 */
public class BaseApp extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		context = getApplicationContext();
	}

	public static Context getAppContext() {
		return context;
	}

}
