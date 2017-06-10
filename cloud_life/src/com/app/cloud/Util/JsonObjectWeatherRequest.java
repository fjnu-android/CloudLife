package com.app.cloud.Util;

import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import android.util.Log;

/**
 * Volley get请求 
 * 无Cookie 天气的json请求
 */
public class JsonObjectWeatherRequest extends Request<JSONObject> {

	private Response.Listener<JSONObject> mListener;

	public JsonObjectWeatherRequest(String url, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Request.Method.GET, url, errorListener);
		mListener = listener;
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		mListener.onResponse(response);

	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {

			// 防止中文乱码
			@SuppressWarnings("deprecation")
			String jsonString = new String(response.data, HTTP.UTF_8);

			JSONObject jsonObject = new JSONObject(jsonString);

			Log.w("weather", "jsonObject " + jsonObject.toString());

			return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

}
