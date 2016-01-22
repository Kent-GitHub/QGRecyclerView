package com.example.recyclerview.test;

import com.util.recyclerview.SwipeView;
import com.util.recyclerview.constant.constant;

import android.app.Application;

public class MyApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		SwipeView.initColorsRes(constant.APP1_ColorRes, constant.APP2_ColorRes);
	}
}
