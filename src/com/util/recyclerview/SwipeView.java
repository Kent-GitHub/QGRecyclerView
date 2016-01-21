package com.util.recyclerview;

import org.androidannotations.annotations.EViewGroup;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
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
	 * setColorSchemeColors 只有前两种颜色有效，第一个颜色是刷新完成前颜色，第二个是刷新完成后颜色
	 * setProgressBackgroundColorSchemeColor 刷新指示器背景色
	 * @param context
	 */
	private void init(Context context) {
		setColorSchemeColors(Color.parseColor("#0000FF"),Color.parseColor("#FFFF00"));
//		setProgressBackgroundColorSchemeColor(Color.parseColor("#cc00C78C"));
		mRecyclerView = new CustomRecyclerView(context);

		addView(mRecyclerView);
	}

	/**
	 * 获取RecyclerView实例
	 * 
	 * @return
	 */
	public final CustomRecyclerView getRecyclerView() {
		return mRecyclerView;
	}

	// getProgressCircleDiameter 获取刷新圈圈直径
	// public void setColorSchemeColors(int... colors) 设置刷新环颜色
	// public void setDistanceToTriggerSync(int distance)  手指移动多少触发刷新
	// public void setProgressBackgroundColorSchemeColor (int color) 刷新指示器背景色
	// public void setProgressBackgroundColorSchemeResource (int colorRes) 刷新指示器背景色
	// public void setSize (int size) 加载指示器大小   DEFAULT, or LARGE.
	// public void setProgressViewEndTarget (boolean scale, int end) 刷新指示器刷新的位置
	// public void setProgressViewOffset (boolean scale, int start, int end) 刷新指示器初始位置、刷新位置
	
}
