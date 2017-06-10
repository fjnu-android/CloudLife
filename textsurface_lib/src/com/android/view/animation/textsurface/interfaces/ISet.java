package com.android.view.animation.textsurface.interfaces;

import java.util.LinkedList;

import com.android.view.animation.textsurface.contants.TYPE;

/**
 * Created by Eugene Levenetc.
 */
public interface ISet extends ISurfaceAnimation {
	TYPE getType();

	LinkedList<ISurfaceAnimation> getAnimations();
}
