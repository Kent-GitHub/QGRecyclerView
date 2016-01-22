package com.util.recyclerview;

import org.androidannotations.annotations.EViewGroup;

import com.util.recyclerview.constant.constant;

import android.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

@EViewGroup
public class SwipeView extends SwipeRefreshLayout {
	private CustomRecyclerView mRecyclerView;

	public SwipeView(Context context) {
		super(context);
		init(context);
	}

	public SwipeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * 指示器颜色
	 */
	public static int[] SchemeColorsRes = null;
	/**
	 * 指示器背景色
	 */
	public static int INDICATOR_BG_ColorRes = -1;
	/**
	 * 初始化指示器颜色
	 * @param color
	 */
	public static void initColorsRes(@ColorRes int... color) {
		constant.APP_SchemeColorsRes = color;
	}
	/**
	 * 初始化指示器背景色
	 * @param color
	 */
	public static void initIndicatorBGColorRes(@ColorRes int  color){
		INDICATOR_BG_ColorRes=color;
	}
	/**
	 * 获取RecyclerView实例
	 * 
	 * @return
	 */
	public final CustomRecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	/**
	 * 初始化入口
	 * @param context
	 */
	private void init(Context context) {
		init(context, INDICATOR_BG_ColorRes, SchemeColorsRes);
	}
	/**
	 * 设置指示器颜色，背景色，初始化RecyclerView
	 * @param context
	 * @param bgColor
	 * @param colors
	 */
	private void init(Context context, int bgColor, int... colors) {
		
		if (colors != null && colors.length > 0) {
			setColorSchemeResources(colors);
		} else if (constant.APP_SchemeColorsRes != null && constant.APP_SchemeColorsRes.length > 0) {
			setColorSchemeResources(constant.APP_SchemeColorsRes);
		}

		if (bgColor != -1) {
			setProgressBackgroundColorSchemeResource(bgColor);
		} else if (constant.APP_BG_ColorRes != -1) {
			setProgressBackgroundColorSchemeResource(constant.APP_BG_ColorRes);
			
		}

		if (context != null) {
			mRecyclerView = new CustomRecyclerView(context);
			addView(mRecyclerView);
		}
	}

	// getProgressCircleDiameter 获取刷新圈圈直径
	// public void setColorSchemeColors(int... colors) 设置刷新环颜色
	// public void setDistanceToTriggerSync(int distance) 手指移动多少触发刷新
	// public void setProgressBackgroundColorSchemeColor (int color) 刷新指示器背景色
	// public void setProgressBackgroundColorSchemeResource (int colorRes) 刷新指示器背景色
	// public void setSize (int size) 加载指示器大小 DEFAULT, or LARGE.
	// public void setProgressViewEndTarget (boolean scale, int end) 刷新指示器刷新的位置
	// public void setProgressViewOffset (boolean scale, int start, int end)
	// 刷新指示器初始位置、刷新位置
	
//	public static int[] SchemeColors = null;
//
//	public static int INDICATOR_BG_Color = -1;
//	
//	public static void initColors( int... color) {
//		constant.APP_SchemeColorsRes = color;
//	}
//	
//	public static void initIndicatorBGColor( int  color){
//		INDICATOR_BG_ColorRes=color;
//	}

}
