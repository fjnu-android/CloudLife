package com.app.cloud.Util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import android.util.Log;

/**
 * Volley Post类型
 *  可以接受和发送Cookie
 */
public class JsonObjectPostRequest extends Request<JSONObject> {

	private Map<String, String> mMap;
	private Response.Listener<JSONObject> mListener;

	// 保存cookie
	public String cookieFromResponse;
	private Map<String, String> sendHeader = new HashMap<String, String>();

	public JsonObjectPostRequest(String url, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener, Map<String, String> map) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mMap = map;
	}

	// 当http请求是post时，则需要该使用该函数设置往里面添加的键值对
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	// 重写方法，处理cookie
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {

			// 防止中文乱码
			@SuppressWarnings("deprecation")
			String jsonString = new String(response.data, HTTP.UTF_8);

			// 将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
			JSONObject jsonObject = new JSONObject(jsonString);

			// 获取cookie字段
			sendHeader = response.headers;

			// 如果因为一些原因没有返回cookie的判断
			if (sendHeader.get("Set-Cookie") != null) {
				cookieFromResponse = sendHeader.get("Set-Cookie");
				cookieFromResponse = cookieFromResponse.replace("\n", ";");
				Log.w("cookie", cookieFromResponse);
				jsonObject.put("Cookie", cookieFromResponse);
			}

			Log.w("json", "jsonObject " + jsonObject.toString());

			return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return sendHeader;
	}

	public void setSendCookie(String cookie) {
		sendHeader.put("Cookie", cookie);
	}
}