package com.example.recyclerview.test;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qgrecyclerview.R;
import com.util.recyclerview.ItemView;
@EViewGroup(R.layout.bean_layout2) 
public class MyViewGroup2 extends ItemView<MyBean>{
	@ViewById(R.id.vg2_image)
	ImageView beanIv;
	@ViewById
	TextView vg2Title,Vg2Content;
	public MyViewGroup2(Context context) {
		super(context);
	}
	
	@Override
	public void bind(MyBean bean){
		beanIv.setImageResource(bean.iconId);
		vg2Title.setText(bean.newsTitle);
		Vg2Content.setText(bean.newsContent);
	}
}
