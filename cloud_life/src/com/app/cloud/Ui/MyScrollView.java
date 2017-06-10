package com.app.cloud.Ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 
 * ∑¿÷π”Îswipelistview viewpager ≥ÂÕª
 */
public class MyScrollView extends ScrollView {

	private GestureDetector mGestureDetector;
	private static double SCROLL_ANGLE = 90;

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetecotr());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetecotr extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			double angle = Math.atan2(Math.abs(distanceY), Math.abs(distanceX));
			if ((180 * angle) / Math.PI < 180) {
				return false;
			}

			return false;
		}
	}

}
