package com.example.recyclerview.test;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qgrecyclerview.R;
import com.util.recyclerview.CustomRecyclerView;
import com.util.recyclerview.CustomRecyclerView.OnLoadMoreListener;
import com.util.recyclerview.CustomRecyclerView.OnSubItemCLickListener;
import com.util.recyclerview.ReViewAdapter;
import com.util.recyclerview.CustomRecyclerView.OnItemClickListener;
import com.util.recyclerview.CustomRecyclerView.OnItemLeftScrollListener;
import com.util.recyclerview.CustomRecyclerView.OnItemLongClickListener;
import com.util.recyclerview.CustomRecyclerView.OnItemRightScrollListener;
import com.util.recyclerview.CustomRecyclerView.OnItemSelectedListener;

@EActivity(R.layout.test_acty_layout)
public class TestActivity extends Activity {

	@ViewById(R.id.test_recyclerview)
	CustomRecyclerView mRecyclerView;

	@ViewById(R.id.test_root_view)
	LinearLayout mLL;
	
	public final int STATE_SUCCEEDED = 1;
	public final int STATE_FAILED = 2;
	public final int STATE_NO_MORE_DATAS = 3;
	
	private String logTag="TestActivity";
	private int loadState;

	@CheckedChange({ R.id.test_state1, R.id.test_state2, R.id.test_state3,
			R.id.test_auto_load })
	void checkChanged(CompoundButton button, boolean isChecked) {
		if (isChecked) {
			switch (button.getId()) {
			case R.id.test_state1:
				loadState = 1;
				break;
			case R.id.test_state2:
				loadState = 2;
				break;
			case R.id.test_state3:
				loadState = 3;
				break;
			case R.id.test_auto_load:
				mRecyclerView.setAutoLoadMoreEnable(isChecked);
			}
		} else {
			if (button.getId() == R.id.test_auto_load) {
				mRecyclerView.setAutoLoadMoreEnable(isChecked);
			}
		}
	}

	public static final int TYPE_WITHOUT_BUTTON = 1;
	public static final int TYPE_WITH_BUTTON = 2;

	private List<MyBean> mDatas;
	private Context mContext = TestActivity.this;
	private ReViewAdapter<MyBean> mAdapter;

	@Click({ R.id.btn_1, R.id.btn_2, R.id.btn_add_header, R.id.btn_add_footer })
	void onClick(View v) {
		switch (v.getId()) {
		// ListView
		case R.id.btn_1:

			mAdapter = new ReViewAdapter<MyBean>(mContext, mDatas) {
				@Override
				public View onCreateItemView(int viewType) {
					if (viewType == TYPE_WITH_BUTTON) {
						return MyViewGroup1_.build(mContext);
					}
					return MyViewGroup2_.build(mContext);
				}

				@Override
				public int setItemViewType(int position) {
					if ((position + 1) % 3 == 0) {
						return TYPE_WITH_BUTTON;
					}
					return TYPE_WITHOUT_BUTTON;
				}
			};
			break;
		case R.id.btn_2:
			mAdapter = new ReViewAdapter<MyBean>(mContext, mDatas,
					MyViewGroup1_.class);
			break;
		case R.id.btn_add_header:

			break;
		case R.id.btn_add_footer:

		}
		mRecyclerView.setAdapter(mAdapter);
	}

	@AfterViews
	void afterViews() {
		initDatas();

		mRecyclerView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(RecyclerView parent, View view,
					int position, int id, Object data) {
				Log.d(logTag, "Clicked position : " + position+ ", Title"+ ((MyBean)data).newsTitle + ".");
			}
		});
		
		mRecyclerView.setOnSubItemClickListener(R.id.vg1_btn, new OnSubItemCLickListener() {
			
			@Override
			public void onSubItemClick(View view, Object data) {
				if (view instanceof TextView) {
					((Button) view).setText("Clicked");
				}
			}
		});
		
		mRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public void onItemLongClick(RecyclerView parent, View view,
					int position, int id, Object data) {
				Log.d(logTag,"Long clicked position : " + position+ ".\nClicked child view ID:" + id + ".");
			}
		});

		// 没有Item选中效果
		// mRecyclerView.setOnItemSelectedListener(listener)
		// 设置选中效果，使用默认：选中时背景色灰色，不选中背景色为透明
		// mRecyclerView.setOnItemSelectedListener(listener, setSelectedEffect)
		// 设置选中效果，并设置选中或未选中背景色
		// mRecyclerView.setOnItemSelectedListener(listener, selectedColor,
		// unSelectedColor)
		mRecyclerView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onNothingSelected(View lastView, int lastPosition) {
				Log.d(logTag, "nothing selected.  lastPosition : " + lastPosition);
			}

			@Override
			public void onItemUnselected(View view, int position, int type) {
				Log.d(logTag, "item unselected position : " + position);
			}

			@Override
			public void onItemSelected(View view, int position, int type) {
				Log.d(logTag, "item selected position : " + position);
			}
		}, true);

		mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void loadMore() {
				loadMoreDatas();
			}
		});
		
		mRecyclerView.setOnItemLeftScrollListener(new OnItemLeftScrollListener() {
			
			@Override
			public void onItemLeftScroll(View view, int position, float touchPonitX,
					float distanceX, float totalDistanceX) {
				Toast.makeText(mContext, "Item : "+position +" left scrolled", 0).show();
				Log.d(logTag, "onItemLeftScroll : "+position +".");
			}
		});
		
		mRecyclerView.setOnItemRightScrollListener(new OnItemRightScrollListener() {
			
			@Override
			public void onItemRightScroll(View view, int position, float touchPonitX,
					float distanceX, float totalDistanceX) {
				Toast.makeText(mContext, "Item : "+position +"right scrolled", 0).show();
				Log.d(logTag, "onItemRightScroll : "+position +".");
			}
		});
		
		TextView headerView = new TextView(mContext);
		headerView.setText("HeaderView");
		TextView footerView = new TextView(mContext);
		footerView.setText("FooterView");
		
		mRecyclerView.setItemDecoration(Color.parseColor("#dcdcdc"), 1, 0, 0)
				.setHeaderView(headerView).setFooterView(footerView)
				.setAutoLoadMoreEnable(false);
		// 设置加载状态Itemview显示的文字
		 mRecyclerView.setLoadState("要显示的文字");
	}

	private void initDatas() {
		loadState = STATE_SUCCEEDED;
		Log.d("Test", Thread.currentThread().getName() + "");
		mDatas = new ArrayList<MyBean>();
		for (int i = 0; i < 51; i++) {
			MyBean bean = new MyBean(R.drawable.ic_launcher, "新闻" + i,
					"新闻内容~~~~~~~~~~~~~~~~~~~" + i);
			mDatas.add(bean);
		}
	}

	@Background
	void loadMoreDatas() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<MyBean> dataList = new ArrayList<MyBean>();
		int currentDatasSize = mDatas.size();
		for (int i = currentDatasSize; i < currentDatasSize + 5; i++) {
			MyBean bean = new MyBean(R.drawable.ic_launcher, "新闻" + i,
					"新闻内容~~~~~~~~~~~~~~~~~~~" + i);
			dataList.add(bean);
		}
		// addDatas(dataList);
		Message msg = Message.obtain();
		msg.obj = dataList;
		handler.sendMessage(msg);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (loadState) {
			case STATE_SUCCEEDED:
				mAdapter.addDatas((List<MyBean>) msg.obj);
				mRecyclerView.succeeded();
				Toast.makeText(mContext, "LoadFinish", 0).show();
				break;
			case STATE_FAILED:
				mRecyclerView.failed();
				break;
			case STATE_NO_MORE_DATAS:
				mRecyclerView.noMore();
				break;
			}
		};
	};

	@UiThread
	void addDatas(List<MyBean> dataList) {
		mAdapter.addDatas(dataList);
		Log.d("Test", Thread.currentThread().getName() + "");
		mRecyclerView.succeeded();
	}

}
