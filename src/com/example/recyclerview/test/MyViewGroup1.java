package com.example.recyclerview.test;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qgrecyclerview.R;
import com.util.recyclerview.ItemView;
@EViewGroup(R.layout.bean_layout1)
public class MyViewGroup1 extends ItemView<MyBean>{
	private Context mContext; 
	public MyViewGroup1(Context context) {
		super(context);
		mContext=context;
	}
	
	@ViewById(R.id.vg1_image)
	ImageView beanImage;
	
	@ViewById(R.id.vg1_title)
	TextView beanTitle;
	@ViewById(R.id.vg1_content)
	TextView beanContent;
	
	@Click(R.id.vg1_btn)
	void onClick(){
		Toast.makeText(mContext, "Button CLicked", 0).show();
	}
	
	@Override
	public void bind(MyBean bean){
		beanImage.setImageResource(bean.iconId);
		beanTitle.setText(bean.newsTitle);
		beanContent.setText(bean.newsContent);
	}

}
