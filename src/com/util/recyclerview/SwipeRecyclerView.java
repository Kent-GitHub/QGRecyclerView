package com.util.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SwipeRecyclerView extends SwipeRefreshLayout{
	private CustomRecyclerView mRecyclerView;
	private LinearLayout mLL;
	public SwipeRecyclerView(Context context) {
		super(context);
		init(context);
	}
	
	public SwipeRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		mLL=new LinearLayout(context);
		mLL.setOrientation(LinearLayout.VERTICAL);
		LayoutParams lp= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLL.setLayoutParams(lp);
		mLL.setBackgroundColor(Color.parseColor("#e1e1e1"));
		mLL.setGravity(Gravity.CENTER);
		mRecyclerView=new CustomRecyclerView(context);
		TextView tv=new TextView(context);
		tv.setText("HEHE");
		tv.setGravity(Gravity.CENTER);
		mLL.addView(mRecyclerView);
		addView(tv);
	}
	
	public final CustomRecyclerView getRecyclerView(){
		return mRecyclerView;
	}
	
}
