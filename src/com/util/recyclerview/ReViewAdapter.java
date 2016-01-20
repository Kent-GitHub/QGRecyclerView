package com.util.recyclerview;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.util.recyclerview.CustomRecyclerView.OnItemClickListener;
import com.util.recyclerview.CustomRecyclerView.OnItemLongClickListener;
import com.util.recyclerview.CustomRecyclerView.OnItemSelectedListener;

public class ReViewAdapter<T> extends RecyclerView.Adapter<ReViewHolder> {
	/**
	 * ItemView数据源
	 */
	private List<T> mDatas;

	private Context mContext;

	private View mItemView = null;

	private View mHeaderView;

	private View mFooterView;
	/**
	 * 底部显示加载状态的ItemView
	 */
	private View mLoadStateView;
	/**
	 * RecyclerView实例
	 */
	private CustomRecyclerView mRecyclerView;

	private Class mViewClass;
	/**
	 * ItemView选中监听事件实例
	 */
	private OnItemSelectedListener mOnItemSelectedListener;

	/**
	 * 记录选中的ItemView
	 */
	private SparseBooleanArray selectedItem;
	/**
	 * ItemView被选中显示的背景色
	 */
	private int selectedBackgroundColor;
	/**
	 * ItemView未被选中显示的背景色
	 */
	private int unSelectedBackgroundColor;
	/**
	 * ItemView是否有选中效果
	 */
	private boolean selectedEffectEnable;

	private String logTag = "ReViewAdapter";

	/**
	 * Adapter构造函数，需要复写onCreateItemView(int viewType)方法
	 * 
	 * @param context
	 *            上下文
	 */
	public ReViewAdapter(Context context) {
		mDatas = new ArrayList<T>();
		mContext = context;
		selectedItem = new SparseBooleanArray();
	}

	/**
	 * Adapter构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param datas
	 *            ItemView数据源
	 */
	public ReViewAdapter(Context context, List<T> datas) {
		mDatas = datas;
		mContext = context;
		selectedItem = new SparseBooleanArray();
	}

	/**
	 * Adapter构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param datas
	 *            ItemView数据源
	 * @param viewClass
	 *            -->带@EViewGroup注解的类的衍生类的类类型
	 *            例如带@EViewGroup注解的类MItemView，应传入MItemView_.class
	 */
	public ReViewAdapter(Context context, List<T> datas, Class viewClass) {
		this(context, datas);
		mViewClass = viewClass;
	}

	/**
	 * 设置ItemView选中监听事件
	 * 
	 * @param listener
	 * @param setEffect
	 * @param selectedColor
	 * @param unSelectedColor
	 */
	public final void setOnItemSelectedListener(OnItemSelectedListener listener,
			boolean setEffect, int selectedColor, int unSelectedColor) {
		selectedEffectEnable = setEffect;
		this.selectedBackgroundColor = selectedColor;
		this.unSelectedBackgroundColor = unSelectedColor;
		mOnItemSelectedListener = listener;
	}

	/**
	 * 根据getItemViewType(int position)的返回值viewType返回对应的View
	 */
	@Override
	public final ReViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == CustomRecyclerView.VIEW_TYPE_HEADER) {
			return new ReViewHolder(mHeaderView);
		}
		if (viewType == CustomRecyclerView.VIEW_TYPE_FOOTER) {
			return new ReViewHolder(mFooterView);
		}
		if (viewType == CustomRecyclerView.VIEW_TYPE_LOAD_STATE_VIEW) {
			return new ReViewHolder(mLoadStateView);
		}
		// mViewClass!=null
		// 则除了HeaderView、footerView、LoadStateView外所有的View类型都一样，都是mViewClass的实例
		if (mViewClass != null) {
			try {
				Method build = mViewClass.getMethod("build", Context.class);
				mItemView = (View) build.invoke(mViewClass, mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ReViewHolder(mItemView);
		}

		// mViewClass==null 并且复写了onCreateItemView(int
		// viewType)方法时，由onCreateItemView(int viewType)返回的View创建ViewHolder
		if (onCreateItemView(viewType) != null) {
			mItemView = onCreateItemView(viewType);
			return new ReViewHolder(mItemView);
		}

		return new ReViewHolder(mItemView);
	}

	/**
	 * 由ItemViewType显示不同ItemView时重写此方法
	 * @param viewType
	 * @return
	 */
	public View onCreateItemView(int viewType) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void onBindViewHolder(ReViewHolder holder, int position) {
		View view = holder.getView();
		// 绑定数据
		if (view instanceof ItemView) {
			((ItemView) view).bind(mDatas.get(position
					- ((mHeaderView != null) ? 1 : 0)));
		}
		// 绑定监听事件
		if (mOnItemSelectedListener != null) {
			bindListener(holder, position);
		}
		// 針對ItemSelected事件, 取消ViewHolder複用的影響
		// Item被选时修改背景色
		if (selectedItem.get(position)) {
			int viewType = getItemViewType(position);
			if (viewType != CustomRecyclerView.VIEW_TYPE_LOAD_STATE_VIEW
					&& viewType != CustomRecyclerView.VIEW_TYPE_HEADER
					&& viewType != CustomRecyclerView.VIEW_TYPE_FOOTER) {
				if (selectedEffectEnable) {
					holder.getView()
							.setBackgroundColor(selectedBackgroundColor);
				}
				if (mOnItemSelectedListener != null) {
					mOnItemSelectedListener.onItemSelected(holder.getView(),
							position, getItemViewType(position));
				}
			} else {
				if (selectedEffectEnable) {
					holder.getView().setBackgroundColor(
							unSelectedBackgroundColor);
				}
				if (mOnItemSelectedListener != null) {
					mOnItemSelectedListener.onItemUnselected(holder.getView(),
							position, getItemViewType(position));
				}
			}
		}
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

	@Override
	public final int getItemCount() {
		return mDatas.size() + ((mHeaderView != null) ? 1 : 0)
				+ ((mFooterView != null) ? 1 : 0)
				+ ((mLoadStateView != null) ? 1 : 0);

	}

	/**
	 * 一般请不要复写此方法，设置itemViewType请复写setItemViewType(int position)方法
	 */
	@Override
	public final int getItemViewType(int position) {

		if (position == 0 && mHeaderView != null) {
			return CustomRecyclerView.VIEW_TYPE_HEADER;
		}
		if (position == getItemCount() - 1 && mLoadStateView != null) {
			return CustomRecyclerView.VIEW_TYPE_LOAD_STATE_VIEW;
		}
		if (position == getItemCount() - 1 && mLoadStateView == null
				&& mFooterView != null) {
			return CustomRecyclerView.VIEW_TYPE_FOOTER;
		} else if (position == getItemCount() - 2 && mLoadStateView != null
				&& mFooterView != null) {
			return CustomRecyclerView.VIEW_TYPE_FOOTER;
		}
		return setItemViewType(position - (mHeaderView != null ? 1 : 0));
	}

	/**
	 * 需要显示不同类型ItemView时复写此方法，返回View类型
	 * 
	 * @param position
	 * @return
	 */
	public int setItemViewType(int position) {
		return 0;
	}

	public void setRecyclerView(CustomRecyclerView recyclerView) {
		mRecyclerView = recyclerView;
	}

	public void setHeaderView(View headerView) {
		mHeaderView = headerView;
		notifyDataSetChanged();
	}

	public void setFooterView(View footerView) {
		mFooterView = footerView;
		notifyDataSetChanged();
	}

	public void setLoadStateView(LoadStateView loadStateView) {
		mLoadStateView = loadStateView;
		notifyDataSetChanged();
	}

	/**
	 * 在末尾添加单个数据
	 */
	public void addData(T t) {
		mDatas.add(t);
		notifyDataSetChanged();
	}

	/**
	 * 在末尾添加多个数据
	 */
	public void addDatas(List<T> datas) {
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

	/**
	 * 更换全部数据
	 */
	public void setDatas(List<T> datas) {
		mDatas = datas;
		notifyDataSetChanged();
	}

	public T getDate(int position) {
		return mDatas.get(position);
	}

	private void bindListener(final ReViewHolder holder, final int position) {
		holder.itemView.setOnClickListener(new OnClickListener() {
			int viewType = getItemViewType(position);

			@Override
			public void onClick(View v) {
				if (viewType != CustomRecyclerView.VIEW_TYPE_LOAD_STATE_VIEW
						&& viewType != CustomRecyclerView.VIEW_TYPE_HEADER
						&& viewType != CustomRecyclerView.VIEW_TYPE_FOOTER) {
					if (selectedItem.get(position)) {
						if (selectedEffectEnable) {
							v.setBackgroundColor(unSelectedBackgroundColor);
						}
						mOnItemSelectedListener.onItemUnselected(v, position,
								viewType);
						selectedItem.delete(position);
						if (selectedItem.size() == 0) {
							mOnItemSelectedListener.onNothingSelected(v,
									position);
						}
					} else {
						if (selectedEffectEnable) {
							v.setBackgroundColor(selectedBackgroundColor);
						}
						mOnItemSelectedListener.onItemSelected(v, position,
								viewType);
						selectedItem.append(position, true);
					}
				}
			}
		});
	}

}

class ReViewHolder extends ViewHolder {

	private View view;

	public ReViewHolder(View view) {
		super(view);
		this.view = view;
	}

	public View getView() {
		return view;
	}

}
