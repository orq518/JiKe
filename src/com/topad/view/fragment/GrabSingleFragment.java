package com.topad.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.bean.AdServiceBean;
import com.topad.bean.GrabSingleBean;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.activity.ADSDetailsActivity;
import com.topad.view.activity.GrabSingleDetailsActivity;
import com.topad.view.customviews.PTRListView;
import com.topad.view.customviews.PullToRefreshView;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * ${todo}<我要抢单>
 *
 * @author lht
 * @data: on 15/10/28 18:32
 */
public class GrabSingleFragment extends BaseFragment{
	private static final String LTAG = GrabSingleFragment.class.getSimpleName();
	/** 上下文 **/
	private Context mContext;
	/** 根view布局 **/
	private View mRootView;
	/** listView **/
	private MyListView mListView;
	/** 只是用来模拟异步获取数据 **/
	private Handler handler;
	/** 适配器 **/
	private ListAdapter adapter;
	/** 数据源 **/
	private ArrayList<GrabSingleBean> bankList = new ArrayList<GrabSingleBean>();

	@Override
	public String getFragmentName() {
		return LTAG;
	}

	private final int MSG_REFRESH = 1000;
	private final int MSG_LOADMORE = 2000;
	protected android.os.Handler mHandler = new android.os.Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_REFRESH:

					break;

				case MSG_LOADMORE:

					break;
			}
		}
	};

	/**
	 * ***生命周期******
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null == mRootView) {
			mRootView = getLayoutInflater(savedInstanceState).inflate(R.layout.fargment_grab_single, null);
		}
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {

		mListView = (MyListView) mRootView.findViewById(R.id.listview);

		initData();

		// 设置listview可以加载、刷新
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		// 设置适配器
		adapter = new ListAdapter();
		mListView.setAdapter(adapter);

		// listview单击
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(mContext, GrabSingleDetailsActivity.class);
				intent.putExtra("state", "1");
				startActivity(intent);
			}
		});

		// 设置回调函数
		mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

			@Override
			public void onRefresh() {
				// 模拟刷新数据，1s之后停止刷新
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						mListView.stopRefresh();
						Toast.makeText(mContext, "refresh",
								Toast.LENGTH_SHORT).show();
						mHandler.sendEmptyMessage(MSG_REFRESH);
					}
				}, 1000);
			}

			@Override
			public void onLoadMore() {
				mHandler.postDelayed(new Runnable() {
					// 模拟加载数据，1s之后停止加载
					@Override
					public void run() {
						mListView.stopLoadMore();
						Toast.makeText(mContext, "loadMore",
								Toast.LENGTH_SHORT).show();
						mHandler.sendEmptyMessage(MSG_LOADMORE);
					}
				}, 1000);
			}
		});
	}

	@Override
	public void setVisible(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		LogUtil.d("是否显示：" + isVisibleToUser + "    isNeedRefresh:" + isNeedRefresh);
		if (isVisibleToUser && isNeedRefresh) {
			isNeedRefresh = false;
		}
	}

	/**
	 * 请求数据
	 */
	public void initData() {
//        String merchantId = getIntent().getStringExtra("merchantId");
//        StringBuffer sb = new StringBuffer();
//        sb.append(Constants.getCurrUrl()).append(Constants.URL_BANK_LIST)
//                .append("?");
//        sb.append("merchantId=").append(merchantId);
//
//        showProgressDialog();
//        postWithoutLoading(sb.toString(), true, new HttpCallback() {
//            @Override
//            public <T> void onModel(T t) {
//                closeProgressDialog();
//
//                BankListModel blModel = (BankListModel) t;
//                if (blModel != null) {
//                    bankList.clear();
//                    bankList.addAll(blModel.bankList);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(BaseModel base) {
//
//            }
//        }, BankListModel.class);

		setData();
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ListAdapter() {
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bankList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bankList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate((R.layout.fargment_grab_single_item), null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.im_icon);
				holder.name = (TextView) convertView .findViewById(R.id.tv_name);
				holder.price = (TextView) convertView .findViewById(R.id.tv_price);
				holder.state = (TextView) convertView .findViewById(R.id.tv_state);
				holder.content = (TextView) convertView .findViewById(R.id.tv_content);
				holder.time = (Button) convertView .findViewById(R.id.btn_time);
				holder.countdown = (Button) convertView .findViewById(R.id.btn_countdown);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(bankList.get(position).name);
			holder.price.setText(bankList.get(position).price);
			holder.state.setText(bankList.get(position).state);
			holder.content.setText(bankList.get(position).content);
			holder.time.setText(bankList.get(position).time);
			holder.countdown.setText(bankList.get(position).countdown);
			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView name;
			TextView state;
			TextView price;
			TextView content;
			TextView time;
			TextView countdown;
		}
	}

	/**
	 * 设置数据--测试
	 */
	private void setData() {
		GrabSingleBean bModel0 = new GrabSingleBean();
		bModel0.name = "北京市聚宝网深圳分公司";
		bModel0.price = "￥120000";
		bModel0.state = "已托管";
		bModel0.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
		bModel0.time = "2015-10-22";
		bModel0.countdown = "还有3天到期";
		bankList.add(bModel0);

		GrabSingleBean bModel1 = new GrabSingleBean();
		bModel1.name = "北京市聚宝网深圳分公司";
		bModel1.price = "￥120000";
		bModel1.state = "已托管";
		bModel1.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
		bModel1.time = "2015-10-22";
		bModel1.countdown = "还有3天到期";
		bankList.add(bModel1);

		GrabSingleBean bModel2 = new GrabSingleBean();
		bModel2.name = "北京市聚宝网深圳分公司";
		bModel2.price = "￥120000";
		bModel2.state = "已托管";
		bModel2.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
		bModel2.time = "2015-10-22";
		bModel2.countdown = "还有3天到期";
		bankList.add(bModel2);

		GrabSingleBean bModel3 = new GrabSingleBean();
		bModel3.name = "北京市聚宝网深圳分公司";
		bModel3.price = "￥120000";
		bModel3.state = "已托管";
		bModel3.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
		bModel3.time = "2015-10-22";
		bModel3.countdown = "还有3天到期";
		bankList.add(bModel3);

		GrabSingleBean bModel4 = new GrabSingleBean();
		bModel4.name = "北京市聚宝网深圳分公司";
		bModel4.price = "￥120000";
		bModel4.state = "已托管";
		bModel4.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
		bModel4.time = "2015-10-22";
		bModel4.countdown = "还有3天到期";
		bankList.add(bModel4);
	}
}