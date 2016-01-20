package com.util.recyclerview;

import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.util.recyclerview.divider.HorizontalDividerItemDecoration;

public class CustomRecyclerView extends RecyclerView implements
		OnItemTouchListener {
	/**
	 * HeaderView类型
	 */
	public final static int VIEW_TYPE_HEADER = 101;
	/**
	 * Footer类型
	 */
	public final static int VIEW_TYPE_FOOTER = 102;
	/**
	 * 加载状态View类型
	 */
	public final static int VIEW_TYPE_LOAD_STATE_VIEW = 103;

	private Context mContext;
	/**
	 * 分隔符实例
	 */
	private ItemDecoration mItemDecoration;

	private String logTag = "CustomRecyclerView";
	/**
	 * Item触摸监听实例
	 */
	private OnItemTouchListener mOnItemTouchListener = this;
	/**
	 * ItemView点击监听事件实例
	 */
	private OnItemClickListener mOnItemClickListener = null;
	/**
	 * ItemView子view点击监听事件实例
	 */
	private OnSubItemCLickListener mOnSubItemCLickListener = null;
	/**
	 * Item选中监听实例
	 */
	private OnItemSelectedListener mOnItemSelectedListener = null;
	/**
	 * Item长按监听实例
	 */
	private OnItemLongClickListener mOnItemLongClickListener = null;
	/**
	 * Item左划监听实例
	 */
	private OnItemLeftScrollListener mOnItemLeftScrollListener;
	/**
	 * Item右划监听实例
	 */
	private OnItemRightScrollListener mOnItemRightScrollListener;
	/**
	 * Item长按监听实例
	 */
	private OnLoadMoreListener mOnLoadMoreListener;
	/**
	 * Item被选中时显示的背景色，为灰色
	 */
	private int selectedColor = Color.parseColor("#20000000");
	/**
	 * Item未被选中时显示的背景色，为透明
	 */
	private int unSelectedColor = Color.parseColor("#00ffffff");
	/**
	 * 触发SubView点击事件的ViewID
	 */
	private int mSubViewId;
	/**
	 * 选中效果
	 */
	private boolean selectedEffectEnable = false;
	/**
	 * 正在加载更多
	 */
	private boolean isLoadingMore = false;
	/**
	 * 自动加载更多
	 */
	private boolean isAutoLoadMore = false;

	private View mHeaderView;

	private View mFooterView;
	/**
	 * RecyclerView的ReViewAdapter
	 */
	private ReViewAdapter mAdapter;
	// 在底部显示加载状态的ItemView
	private LoadStateView mLoadStateView;

	public CustomRecyclerView(Context context) {
		super(context);
		init(context);
	}

	public CustomRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 设置Item点击监听
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.addOnItemTouchListener(mOnItemTouchListener);
		mOnItemClickListener = listener;
	}

	/**
	 * 设置ItemView子view点击监听事件
	 * 
	 * @param id
	 */
	public void setOnSubItemClickListener(int subViewId,
			OnSubItemCLickListener listener) {
		mSubViewId = subViewId;
		mOnSubItemCLickListener = listener;
	}

	/**
	 * 设置Item长按监听
	 * 
	 * @param listener
	 */
	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		this.addOnItemTouchListener(mOnItemTouchListener);
		mOnItemLongClickListener = listener;
	}

	/**
	 * 设置Item选中监听 选中效果关闭
	 * 
	 * @param listener
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		setOnItemSelectedListener(listener, false);
	}

	/**
	 * 设置Item选中监听
	 * 
	 * @param listener
	 * @param setSelectedEffect
	 *            设置选中效果开关
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener,
			boolean setSelectedEffect) {
		setOnItemSelectedListener(listener, setSelectedEffect, selectedColor,
				unSelectedColor);
	}

	/**
	 * 设置Item选中监听 选中效果开启
	 * 
	 * @param listener
	 * @param selectedColor
	 *            ItemView被选中时的背景色
	 * @param unSelectedColor
	 *            ItemView未被选中时的背景色
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener,
			int selectedColor, int unSelectedColor) {
		setOnItemSelectedListener(listener, true, selectedColor,
				unSelectedColor);
	}

	/**
	 * 设置Item选中监听 选中效果开启
	 * 
	 * @param listener
	 * @param setSelectedEffect
	 *            设置选中效果开关
	 * @param selectedColor
	 *            ItemView被选中时的背景色
	 * @param unSelectedColor
	 *            ItemView未被选中时的背景色
	 */
	private void setOnItemSelectedListener(OnItemSelectedListener listener,
			boolean setSelectedEffect, int selectedColor, int unSelectedColor) {
		mOnItemSelectedListener = listener;
		this.selectedEffectEnable = setSelectedEffect;
		this.selectedColor = selectedColor;
		this.unSelectedColor = unSelectedColor;
		Adapter adapter = getAdapter();
		if (adapter != null) {
			ReViewAdapter myAdapter = (ReViewAdapter) adapter;
			myAdapter.setOnItemSelectedListener(mOnItemSelectedListener,
					setSelectedEffect, selectedColor, unSelectedColor);
		}
	}

	/**
	 * 设置ItemView左划监听事件
	 * 
	 * @param listener
	 */
	public void setOnItemLeftScrollListener(OnItemLeftScrollListener listener) {
		mOnItemLeftScrollListener = listener;
	}

	/**
	 * 设置ItemView右划监听监听
	 * 
	 * @param listener
	 */
	public void setOnItemRightScrollListener(OnItemRightScrollListener listener) {
		mOnItemRightScrollListener = listener;
	}

	/**
	 * 设置RecyclerView划到底部加载更多的监听事件
	 * 
	 * @param listener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener listener) {
		mOnLoadMoreListener = listener;
		getDefaultLoadStateView();
		if (listener != null && getAdapter() != null) {
			getMyAdapter().setLoadStateView(mLoadStateView);
		} else if (listener == null && getAdapter() != null) {
			mLoadStateView = null;
			getMyAdapter().setLoadStateView(null);
		}
	}

	/**
	 * 获取点击监听事件实例
	 * 
	 * @return
	 */
	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	/**
	 * 获取选中监听事件实例
	 * 
	 * @return
	 */
	public OnItemSelectedListener getOnItemSelectedLisenter() {
		return mOnItemSelectedListener;
	}

	/**
	 * 获取长按监听事件实例
	 * 
	 * @return
	 */
	public OnItemLongClickListener getOnItemLongClickListener() {
		return mOnItemLongClickListener;
	}

	/**
	 * 获取RecyclerView在底部监听事件实例
	 * 
	 * @return
	 */
	public OnLoadMoreListener getOnLoadMoreListener() {
		return mOnLoadMoreListener;
	}

	/**
	 * 设置Adapter同时传递监听事件
	 */
	@Override
	public void setAdapter(Adapter adapter) {
		super.setAdapter(adapter);
		ReViewAdapter myAdapter = getMyAdapter();
		myAdapter.setRecyclerView(this);
		myAdapter.setOnItemSelectedListener(mOnItemSelectedListener,
				selectedEffectEnable, selectedColor, unSelectedColor);
		myAdapter.setFooterView(mFooterView);
		myAdapter.setHeaderView(mHeaderView);
		myAdapter.setLoadStateView(mLoadStateView);
	}

	/**
	 * 获取HeaderView
	 * 
	 * @return
	 */
	public View getHeaderView() {
		return mHeaderView;
	}

	/**
	 * 获取FooterView
	 * 
	 * @return
	 */
	public View getFooterView() {
		return mFooterView;
	}

	/**
	 * 获取加载状态ItemView
	 * 
	 * @return
	 */
	public LoadStateView getLoadStateView() {
		return mLoadStateView;
	}

	/**
	 * 移除HeaderView
	 * 
	 * @param headerView
	 */
	public void removeHeaderView(View headerView) {
		mHeaderView = null;
		getMyAdapter().setHeaderView(null);
	}

	/**
	 * 移除FooterView
	 * 
	 * @param headerView
	 */
	public void removeFooterView(View footerView) {
		mFooterView = null;
		getMyAdapter().setFooterView(null);
	}

	/**
	 * 设置HeaderView
	 * 
	 * @param headerView
	 */
	public CustomRecyclerView setHeaderView(View headerView) {
		mHeaderView = headerView;
		if (getMyAdapter() != null) {
			getMyAdapter().setHeaderView(mHeaderView);
		}
		return this;
	}

	/**
	 * 设置FooterView
	 * 
	 * @param footerView
	 */
	public CustomRecyclerView setFooterView(View footerView) {
		mFooterView = footerView;
		if (getMyAdapter() != null) {
			getMyAdapter().setFooterView(mFooterView);
		}
		return this;
	}

	/**
	 * 设置加载状态文本
	 * 
	 * @param state
	 */
	public CustomRecyclerView setLoadState(String state) {
		if (mLoadStateView != null) {
			mLoadStateView.setLoadingStateText(state);
		}
		return this;
	}

	/**
	 * 设置分割线
	 * 
	 * @param color
	 *            分割线颜色
	 * @param size
	 *            分割线大小
	 * @param leftMargin
	 *            左边距
	 * @param rightMargin
	 *            右边距
	 */
	public CustomRecyclerView setItemDecoration(int color, int size,
			int leftMargin, int rightMargin) {
		this.removeItemDecoration(mItemDecoration);
		mItemDecoration = new HorizontalDividerItemDecoration.Builder(mContext)
				.color(color).size(size + 1).margin(leftMargin, rightMargin)
				.build();
		this.addItemDecoration(mItemDecoration);
		return this;
	}

	/**
	 * 设置触发OnLoadMore时是否自动刷新
	 */
	public CustomRecyclerView setAutoLoadMoreEnable(boolean autoLoadMore) {
		isAutoLoadMore = autoLoadMore;
		return this;
	}

	/**
	 * 点击事件接口
	 * 
	 * @param <T>
	 */
	public interface OnItemClickListener {
		void onItemClick(RecyclerView parent, View view, int position, int id,
				Object data);
	}

	/**
	 * ItemView子View点击监听事件接口
	 * 
	 */
	public interface OnSubItemCLickListener {
		void onSubItemClick(View view, Object data);
	}

	/**
	 * ItemSelected接口
	 */
	public interface OnItemSelectedListener {

		void onItemSelected(View view, int position, int viewType);

		/**
		 * 唯一一个选中的View变为不选中时调用，并获得这个view
		 */
		void onNothingSelected(View lastView, int lastPosition);

		/**
		 * 一个View由选中状态变成不选中状态时调用
		 */
		void onItemUnselected(View view, int position, int viewType);
	}

	/**
	 * ItemLongClick接口
	 */
	public interface OnItemLongClickListener {
		void onItemLongClick(RecyclerView parent, View view, int position,
				int id, Object data);
	}

	/**
	 * ItemView左划事件接口
	 */
	public interface OnItemLeftScrollListener {
		void onItemLeftScroll(View view, int position, float touchPonitX,
				float distanceX, float totalDistanceX);
	}

	/**
	 * Item右划事件接口
	 * 
	 */
	public interface OnItemRightScrollListener {
		void onItemRightScroll(View view, int position, float touchPonitX,
				float distanceX, float totalDistanceX);
	}

	public interface OnLoadMoreListener {
		/**
		 * RecyclerView在底部时加载更多
		 */
		void loadMore();
	}

	/**
	 * 通知RecyclerView加载数据已开始
	 */
	private void loadStarted() {
		isLoadingMore = true;
		mLoadStateView.setLoadingStateText("正在加载中");
		mLoadStateView.showProgressBar();
	}

	/**
	 * 数据更新完成后通知RecyclerView刷新完成
	 */
	public void succeeded() {
		isLoadingMore = false;
		mLoadStateView.hideProgressBar();
		mLoadStateView.setLoadingStateText("点击加载更多");
	}

	/**
	 * 通知RecyclerView数据加载失败
	 */
	public void failed() {
		isLoadingMore = false;
		mLoadStateView.hideProgressBar();
		mLoadStateView.setLoadingStateText("加载失败，点击重试");
	}

	/**
	 * 通知RecyclerView已无数据可以加载
	 */
	public void noMore() {
		isLoadingMore = false;
		mLoadStateView.hideProgressBar();
		mLoadStateView.setLoadingStateText("没有更多数据了");
	}

	/**
	 * 设置布局管理器--setLayoutManager(manager) 这里设置为竖直方向排布
	 * 添加Item分隔符--addItemDecoration
	 */
	private void init(Context context) {
		mContext = context;
		LinearLayoutManager manager = new LinearLayoutManager(context,
				LinearLayoutManager.VERTICAL, false);
		this.setLayoutManager(manager);
		mItemDecoration = new HorizontalDividerItemDecoration.Builder(context)
				.color(Color.parseColor("#dcdcdc")).size(2).margin(0, 0)
				.build();
		this.addItemDecoration(mItemDecoration);
		this.setOnScrollListener(mScrollListener);
	}

	private ReViewAdapter getMyAdapter() {
		mAdapter = (ReViewAdapter) getAdapter();
		return mAdapter;
	}

	/**
	 * Touch事件获取触摸点控件ID
	 * 
	 * @param view
	 * @param event
	 * @return
	 */
	private int getTouchPointId(View view, MotionEvent event) {
		int x = (int) event.getRawX();
		int y = (int) event.getRawY();
		int id = 0;
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); i++) {
				View childView = vg.getChildAt(i);
				int[] location = new int[2];
				childView.getLocationInWindow(location);
				if (location[0] < x && location[0] + childView.getWidth() > x
						&& location[1] < y
						&& location[1] + childView.getHeight() > y) {
					id = getTouchPointId(childView, event);
				}
			}
		} else {

			return view.getId();
		}
		return id;
	}

	/**
	 * 由ItemView所在position获取其数据(如果不存在返回null)
	 * 
	 * @param position
	 * @return
	 */
	private Object getDataByPosition(int position) {
		Object date = null;
		try {
			date = getMyAdapter().getDate(position);
		} catch (Exception e) {

		}
		return date;
	}

	/**
	 * 获得或者实例化一个底部加载状态的ItemView并返回
	 * 
	 * @return
	 */
	private LoadStateView getDefaultLoadStateView() {
		if (mLoadStateView == null) {
			mLoadStateView = LoadStateView_.build(mContext);
			mLoadStateView.hideProgressBar();
		}
		return mLoadStateView;
	}

	/**
	 * 最后一个Item已经在最底部返回True，反之False
	 */
	private boolean isBottom() {
		LinearLayoutManager manager = (LinearLayoutManager) this
				.getLayoutManager();
		int position = manager.findLastCompletelyVisibleItemPosition();
		if (position == manager.getItemCount() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * 标记判定左划右划事件中方向是否已经确定
	 */
	private boolean isDirectionConfirm = false;
	/**
	 * 是否已确定为左划事件
	 */
	private boolean isHorizontalLeftScroll = false;
	/**
	 * 是否已确定为右划事件
	 */
	private boolean isHorizontalRightScroll = false;
	/**
	 * 标记在某一次滑动中左划、右划是否已经触发
	 */
	private boolean isHorizontalScrollTriggered = false;
	/**
	 * mGestureDetector由OnItemTouchListener的方法调用并触发ItemView的点击事件、长按事件、左划右划事件
	 */
	private GestureDetector mGestureDetector = new GestureDetector(mContext,
			new SimpleOnGestureListener() {
				/**
				 * 触发ItemView点击事件或SubView点击事件或者触发LoadStateView的点击加载更多
				 */
				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					View view = findChildViewUnder(e.getX(), e.getY());
					int position = getChildPosition(view);
					int viewType = getMyAdapter().getItemViewType(position);
					if (view != null && mOnItemClickListener != null
							&& viewType != VIEW_TYPE_LOAD_STATE_VIEW) {
						// 触发ItemView点击事件
						mOnItemClickListener.onItemClick(
								CustomRecyclerView.this, view, position,
								getTouchPointId(view, e),
								getDataByPosition(position));
					} else if (view != null && !isLoadingMore
							&& viewType == VIEW_TYPE_LOAD_STATE_VIEW
							&& mOnLoadMoreListener != null) {
						// 触发LoadStateView加载更多事件
						mOnLoadMoreListener.loadMore();
						loadStarted();
					}
					if (mOnSubItemCLickListener != null
							&& mSubViewId == getTouchPointId(view, e)) {
						// 触发SubView点击事件
						mOnSubItemCLickListener.onSubItemClick(
								view.findViewById(mSubViewId),
								getDataByPosition(position));
					}
					return true;
				}

				/**
				 * 触发长按事件
				 */
				@Override
				public void onLongPress(MotionEvent e) {
					if (mOnItemLongClickListener != null) {
						View view = findChildViewUnder(e.getX(), e.getY());
						int position = getChildPosition(view);
						super.onLongPress(e);
						mOnItemLongClickListener.onItemLongClick(
								CustomRecyclerView.this, view, position,
								getTouchPointId(view, e),
								getDataByPosition(position));
					}
				}

				/**
				 * 触发左划右划事件
				 */
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					if (mOnItemLeftScrollListener != null
							|| mOnItemRightScrollListener != null) {
						int parentWidth = getWidth();
						View view = findChildViewUnder(e1.getX(), e1.getY());
						int position = getChildPosition(view);
						int viewType = getAdapter().getItemViewType(position);
						if (view != null
								&& viewType != VIEW_TYPE_LOAD_STATE_VIEW) {
							if (!isDirectionConfirm) {
								if (Math.abs(distanceX) > Math
										.abs(3 * distanceY)) {
									isHorizontalRightScroll = !(isHorizontalLeftScroll = e2
											.getX() < e1.getX());
								} else {
									isHorizontalLeftScroll = false;
									isHorizontalRightScroll = false;
								}
								isDirectionConfirm = true;
							}
							if (isHorizontalLeftScroll
									&& mOnItemLeftScrollListener != null
									&& !isHorizontalScrollTriggered
									&& (e1.getX() - e2.getX()) > parentWidth / 3) {
								isHorizontalScrollTriggered = true;
								mOnItemLeftScrollListener.onItemLeftScroll(
										view, getChildPosition(view),
										e1.getRawX(), distanceX,
										e1.getX() - e2.getX());
							} else if (isHorizontalRightScroll
									&& mOnItemRightScrollListener != null
									&& !isHorizontalScrollTriggered
									&& (e2.getX() - e1.getX()) > parentWidth / 3) {
								isHorizontalScrollTriggered = true;
								mOnItemRightScrollListener.onItemRightScroll(
										view, getChildPosition(view),
										e1.getRawX(), distanceX,
										e1.getX() - e2.getX());
							}
						}
					}

					return super.onScroll(e1, e2, distanceX, distanceY);
				}

			});
	/**
	 * RecyclerView的OnScrollListener实例
	 */
	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			super.onScrollStateChanged(recyclerView, newState);
			// 松手才刷新
			// if (mOnLoadMoreListener!=null&&isAutoLoadMore&&isBottom()) {
			// mOnLoadMoreListener.loadMore();
			// notifyLoadStarted();
			// }
		}

		/**
		 * RecyclerView在底部时触发加载更多事件
		 */
		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			if (mOnLoadMoreListener != null && isAutoLoadMore && isBottom()) {
				mOnLoadMoreListener.loadMore();
				loadStarted();
			}
		}
	};

	@Override
	public boolean onInterceptTouchEvent(RecyclerView recyclerView,
			MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			isDirectionConfirm = false;
			isHorizontalScrollTriggered = false;
			break;
		}
		if (isDirectionConfirm
				&& (isHorizontalLeftScroll || isHorizontalRightScroll)) {
			return true;
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			isDirectionConfirm = false;
			isHorizontalScrollTriggered = false;
			break;
		}
	}

}
