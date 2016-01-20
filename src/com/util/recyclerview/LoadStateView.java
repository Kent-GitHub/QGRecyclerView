package com.util.recyclerview;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.example.qgrecyclerview.R;


import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
@EViewGroup(R.layout.load_state_layout)
public class LoadStateView extends RelativeLayout{

	public LoadStateView(Context context) {
		super(context);
	}
	
	//加载状态进度条
	@ViewById(R.id.load_progressBar)
	ProgressBar mProgressBar;
	//显示加载状态TextView
	@ViewById(R.id.load_tv)
	TextView mTextView;
	
	/**
	 * 隐藏进度条
	 */
	public void hideProgressBar(){
		mProgressBar.setVisibility(View.GONE);
	}
	/**
	 * 显示进度条
	 */
	public void showProgressBar(){
		mProgressBar.setVisibility(View.VISIBLE);
	}
	/**
	 * 设置显示加载状态
	 * @param state
	 */
	public void setLoadingStateText(String state){
		mTextView.setText(state);
	}
}
