/**
 * @description: 离线地图下载管理界面
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年9月7日 下午2:51:50   
 * @version 1.0   
 */
package com.gps808.app.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.gps808.app.R;
import com.gps808.app.adapter.OfflineExpandableListAdapter;
import com.gps808.app.adapter.OfflineMapAdapter;
import com.gps808.app.adapter.OfflineMapManagerAdapter;
import com.gps808.app.fragment.SearchFragment;
import com.gps808.app.fragment.SearchFragment.OnSearchClickListener;
import com.gps808.app.interfaces.OnOfflineItemStatusChangeListener;
import com.gps808.app.models.OfflineMapItem;
import com.gps808.app.utils.BackgroundTask;
import com.gps808.app.utils.BaseActivity;
import com.gps808.app.utils.Utils;

public class OfflineMapActivity extends BaseActivity implements
		MKOfflineMapListener, OnOfflineItemStatusChangeListener,
		OnClickListener, OnPageChangeListener {

	// ------------------------ Constants ------------------------

	// ------------------------- Fields --------------------------

	private ViewPager viewpager;
	private TextView mDownloadText;
	private TextView mDownloadedText;

	private SearchFragment svDown;
	private ListView lvDown;

	private SearchFragment svAll;
	private ExpandableListView lvWholeCountry;
	private ListView lvSearchResult;

	private List<View> views = new ArrayList<View>(2);
	private List<String> titles = new ArrayList<String>(2);

	private MKOfflineMap mOffline = null;

	private OfflineMapManagerAdapter downAdapter;
	private OfflineMapAdapter allSearchAdapter;
	private OfflineExpandableListAdapter allCountryAdapter;

	private List<OfflineMapItem> itemsDown; // 下载或下载中城市
	private List<OfflineMapItem> itemsAll; // 所有城市，与热门城市及下载管理对象相同

	private List<OfflineMapItem> itemsProvince;
	private List<List<OfflineMapItem>> itemsProvinceCity;

	// ----------------------- Constructors ----------------------

	// -------- Methods for/from SuperClass/Interfaces -----------

	private ImageView backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline_map);
		// 初始化离线地图管理
		mOffline = new MKOfflineMap();
		mOffline.init(this);

		initViews();

		viewpager.setCurrentItem(1);
		backBtn=(ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private boolean isResumed = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isResumed) {
			isResumed = true;
			loadData();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mOffline.destroy();
	}

	/**
	 * 
	 * @author chenshiqiang E-mail:csqwyyx@163.com
	 * @param type
	 *            事件类型: MKOfflineMap.TYPE_NEW_OFFLINE,
	 *            MKOfflineMap.TYPE_DOWNLOAD_UPDATE,
	 *            MKOfflineMap.TYPE_VER_UPDATE.
	 * @param state
	 *            事件状态: 当type为TYPE_NEW_OFFLINE时，表示新安装的离线地图数目.
	 *            当type为TYPE_DOWNLOAD_UPDATE时，表示更新的城市ID.
	 */
	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);

			if (setElement(update, true) != null) {
				if (itemsDown != null && itemsDown.size() > 1) {
					Collections.sort(itemsDown);
				}

				refreshDownList("");

			} else {
				downAdapter.notifyDataSetChanged();
			}

			allSearchAdapter.notifyDataSetChanged();
			allCountryAdapter.notifyDataSetChanged();
			break;

		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
			break;

		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			break;
		}
	}

	/**
	 * 百度下载状态改变（暂停--》恢复）居然不回调，所以改变状态时自己得增加接口监听状态改变刷新界面
	 * 
	 * @author chenshiqiang E-mail:csqwyyx@163.com
	 * @param item
	 *            有状态改变的item
	 * @param removed
	 *            item是否被删除
	 */
	@Override
	public void statusChanged(OfflineMapItem item, boolean removed) {
		// TODO Auto-generated method stub
		if (removed) {
			for (int i = itemsDown.size() - 1; i >= 0; i--) {
				OfflineMapItem temp = itemsDown.get(i);
				if (temp.getCityId() == item.getCityId()) {
					itemsDown.remove(i);
				}
			}
			refreshDownList("");

		} else {
			downAdapter.notifyDataSetChanged();
		}

		allSearchAdapter.notifyDataSetChanged();
		allCountryAdapter.notifyDataSetChanged();
	}

	// --------------------- Methods public ----------------------

	public void toDownloadPage() {
		viewpager.setCurrentItem(0);
	}

	// --------------------- Methods private ---------------------

	private void initViews() {
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		// 顶部
		mDownloadText = (TextView) findViewById(R.id.download_list_text);
		mDownloadedText = (TextView) findViewById(R.id.downloaded_list_text);
		mDownloadText.setOnClickListener(this);
		mDownloadedText.setOnClickListener(this);
		viewpager.setOnPageChangeListener(this);

		LayoutInflater inf = LayoutInflater.from(this);
		View v1 = inf.inflate(R.layout.view_offline_download, null, false);
		svDown = (SearchFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.svDown);
		lvDown = (ListView) v1.findViewById(R.id.lvDown);
		views.add(v1);

		View v2 = inf.inflate(R.layout.view_offline_countrys, null, false);
		svAll = (SearchFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.svAll);
		lvWholeCountry = (ExpandableListView) v2
				.findViewById(R.id.lvWholeCountry);
		lvSearchResult = (ListView) v2.findViewById(R.id.lvSearchResult);
		views.add(v2);

		titles.add("下载管理");
		titles.add("城市列表");

		viewpager.setOffscreenPageLimit(2);
		viewpager.setAdapter(new MyPagerAdapter());
		svDown.setHint("请输入要搜索的地区名称,如:沧州");
		svAll.setHint("请输入要搜索的地区名称,如:沧州");
		svAll.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearch(String k) {
				// TODO Auto-generated method stub
				refreshAllSearchList(k);
			}

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub
				refreshAllSearchList("");
			}
		});
		svDown.setOnSearchClickListener(new OnSearchClickListener() {

			@Override
			public void onSearchClose() {
				// TODO Auto-generated method stub
				refreshDownList("");
			}

			@Override
			public void onSearch(String key) {
				// TODO Auto-generated method stub
				refreshDownList(key);
			}
		});

		downAdapter = new OfflineMapManagerAdapter(this, mOffline, this);
		lvDown.setAdapter(downAdapter);

		allSearchAdapter = new OfflineMapAdapter(this, mOffline, this);
		lvSearchResult.setAdapter(allSearchAdapter);

		allCountryAdapter = new OfflineExpandableListAdapter(this, mOffline,
				this);
		lvWholeCountry.setAdapter(allCountryAdapter);
		lvWholeCountry.setGroupIndicator(null);
	}

	/**
	 * 刷新下载列表, 根据搜索关键字及itemsDown 下载管理数量变动时调用
	 */
	private void refreshDownList(String key) {

		if (key == null || key.length() < 1) {
			downAdapter.setDatas(itemsDown);

		} else {
			List<OfflineMapItem> filterList = new ArrayList<OfflineMapItem>();
			if (itemsDown != null && !itemsDown.isEmpty()) {
				for (OfflineMapItem i : itemsDown) {
					if (i.getCityName().contains(key)) {
						filterList.add(i);
					}
				}
			}

			downAdapter.setDatas(filterList);
		}
	}

	/**
	 * 刷新所有城市搜索结果
	 */
	private void refreshAllSearchList(String key) {

		if (key == null || key.length() < 1) {
			lvSearchResult.setVisibility(View.GONE);
			lvWholeCountry.setVisibility(View.VISIBLE);

			allSearchAdapter.setDatas(null);

		} else {
			lvSearchResult.setVisibility(View.VISIBLE);
			lvWholeCountry.setVisibility(View.GONE);

			List<OfflineMapItem> filterList = new ArrayList<OfflineMapItem>();
			if (itemsAll != null && !itemsAll.isEmpty()) {
				for (OfflineMapItem i : itemsAll) {
					if (i.getCityName().contains(key)) {
						filterList.add(i);
					}
				}
			}

			allSearchAdapter.setDatas(filterList);
		}
	}

	private void loadData() {

		new BackgroundTask<Void>(this) {
			@Override
			protected Void onRun() {
				// TODO Auto-generated method stub
				// 导入离线地图包
				// 将从官网下载的离线包解压，把vmp文件夹拷入SD卡根目录下的BaiduMapSdk文件夹内。
				// 把网站上下载的文件解压，将\BaiduMap\vmp\l里面的.dat_svc文件，拷贝到手机BaiduMapSDK/vmp/h目录下
				int num = mOffline.importOfflineData();
				if (num > 0) {
					Utils.ToastMessage(OfflineMapActivity.this, "成功导入" + num
							+ "个离线包");
				}

				List<MKOLSearchRecord> all = null;
				try {
					all = mOffline.getOfflineCityList();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (all == null || all.isEmpty()) {

					Utils.ToastMessage(OfflineMapActivity.this,
							"未获取到离线地图城市数据，可能有其他应用正在使用百度离线地图功能！");
					return null;
				}

				List<MKOLSearchRecord> hotCity = mOffline.getHotCityList();
				HashSet<Integer> hotCityIds = new HashSet<Integer>();
				if (!hotCity.isEmpty()) {
					for (MKOLSearchRecord r : hotCity) {
						hotCityIds.add(r.cityID);
					}
				}

				itemsAll = new ArrayList<OfflineMapItem>();
				itemsDown = new ArrayList<OfflineMapItem>();
				itemsProvince = new ArrayList<OfflineMapItem>();
				itemsProvinceCity = new ArrayList<List<OfflineMapItem>>();

				// cityType 0:全国；1：省份；2：城市,如果是省份，可以通过childCities得到子城市列表

				// 全国概略图、直辖市、港澳 子城市列表
				ArrayList<MKOLSearchRecord> childMunicipalities = new ArrayList<MKOLSearchRecord>();
				ArrayList<OfflineMapItem> childHotCitys = new ArrayList<OfflineMapItem>();

				for (MKOLSearchRecord province : all) {
					OfflineMapItem item = new OfflineMapItem();
					item.setCityInfo(province);

					List<MKOLSearchRecord> childs = province.childCities;
					if (childs != null && !childs.isEmpty()) {
						// 省

						List<OfflineMapItem> itemList = new ArrayList<OfflineMapItem>();
						for (MKOLSearchRecord itemCity : childs) {
							OfflineMapItem c = new OfflineMapItem();
							c.setCityInfo(itemCity);
							itemList.add(c);

							itemsAll.add(c);
							if (hotCityIds.contains(itemCity.cityID)) {
								// 添加到热门城市，保证与省份下的城市是一个对象
								childHotCitys.add(c);
							}
						}
						itemsProvinceCity.add(itemList);
						itemsProvince.add(item);

					} else {
						// 全国概略图、直辖市、港澳
						childMunicipalities.add(province);
					}
				}

				// 构建一个省份，放全国概略图、直辖市、港澳
				if (!childMunicipalities.isEmpty()) {
					MKOLSearchRecord proMunicipalities = new MKOLSearchRecord();
					proMunicipalities.cityName = "全国概略图、直辖市、港澳";
					proMunicipalities.childCities = childMunicipalities;
					proMunicipalities.cityType = 1;

					List<OfflineMapItem> itemList = new ArrayList<OfflineMapItem>();
					for (MKOLSearchRecord itemCity : childMunicipalities) {
						OfflineMapItem c = new OfflineMapItem();
						c.setCityInfo(itemCity);
						itemList.add(c);

						proMunicipalities.size += itemCity.size;
						itemsAll.add(c);
						if (hotCityIds.contains(itemCity.cityID)) {
							// 添加到热门城市，保证与省份下的城市是一个对象
							childHotCitys.add(c);
						}
					}

					OfflineMapItem item = new OfflineMapItem();
					item.setCityInfo(proMunicipalities);
					itemsProvinceCity.add(0, itemList);
					itemsProvince.add(0, item);
				}

				// 构建一个省份，放热门城市
				if (!childHotCitys.isEmpty()) {
					int size = 0;
					ArrayList<MKOLSearchRecord> cs = new ArrayList<MKOLSearchRecord>();
					for (OfflineMapItem i : childHotCitys) {
						cs.add(i.getCityInfo());
						size += i.getSize();
					}

					MKOLSearchRecord proHot = new MKOLSearchRecord();
					proHot.cityName = "热门城市";
					proHot.childCities = cs;
					proHot.cityType = 1;
					proHot.size = size;

					OfflineMapItem item1 = new OfflineMapItem();
					item1.setCityInfo(proHot);
					itemsProvinceCity.add(0, childHotCitys);
					itemsProvince.add(0, item1);
				}

				// 刷新状态
				List<MKOLUpdateElement> updates = mOffline.getAllUpdateInfo();
				if (updates != null && updates.size() > 0) {
					for (MKOLUpdateElement element : updates) {
						setElement(element, false);
					}

					if (itemsDown != null && itemsDown.size() > 1) {
						Collections.sort(itemsDown);
					}
				}

				return null;
			}

			@Override
			protected void onResult(Void result) {
				// TODO Auto-generated method stub
				refreshDownList("");
				refreshAllSearchList("");

				allCountryAdapter.setDatas(itemsProvince, itemsProvinceCity);
			}
		}.execute();
	}

	private boolean isWake = false;

	/**
	 * 返回itemsDown.add
	 * 
	 * @param element
	 * @param ischeck
	 * @return
	 */
	private OfflineMapItem setElement(MKOLUpdateElement element, boolean ischeck) {
		OfflineMapItem ret = null;

		if (element == null || itemsAll == null) {
			return null;
		}

		if (element.status == MKOLUpdateElement.DOWNLOADING
				&& element.ratio == 100) {
			element.status = MKOLUpdateElement.FINISHED;
		}

		for (OfflineMapItem item : itemsAll) {
			if (element.cityID == item.getCityId()) {
				item.setDownInfo(element);

				// 过滤已下载数据
				if (item.getStatus() != MKOLUpdateElement.UNDEFINED) {
					if (ischeck) {
						if (!itemsDown.contains(item)) {
							if (itemsDown.add(item)) {
								ret = item;
							}
						}

					} else {
						if (itemsDown.add(item)) {
							ret = item;
						}
					}
				}
				if (!isWake && item.getStatus() == MKOLUpdateElement.WAITING) {
					int id = item.getCityId();
					if (id > 0) {
						mOffline.start(id);
					}
					isWake = true;
				}
				break;
			}
		}

		return ret;
	}

	// --------------------- Getter & Setter ---------------------

	// --------------- Inner and Anonymous Classes ---------------

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View v = views.get(position);
			container.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(views.get(position));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles.get(position);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.equals(mDownloadText)) {
			int paddingHorizontal = mDownloadText.getPaddingLeft();
			int paddingVertical = mDownloadText.getPaddingTop();
			viewpager.setCurrentItem(0);

			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_pressed);

			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_normal);

			mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);

			mDownloadText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);

			// mDownloadedAdapter.notifyDataChange();

		} else if (v.equals(mDownloadedText)) {
			int paddingHorizontal = mDownloadedText.getPaddingLeft();
			int paddingVertical = mDownloadedText.getPaddingTop();
			viewpager.setCurrentItem(1);

			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_normal);
			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_pressed);
			mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);
			mDownloadText.setPadding(paddingHorizontal, paddingVertical,
					paddingHorizontal, paddingVertical);

			// mDownloadedAdapter.notifyDataChange();

			// } else if (v.equals(mBackImage)) {
			// // 返回
			// finish();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		int paddingHorizontal = mDownloadedText.getPaddingLeft();
		int paddingVertical = mDownloadedText.getPaddingTop();

		switch (arg0) {
		case 0:
			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_pressed);
			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_normal);
			// mPageAdapter.notifyDataSetChanged();
			break;
		case 1:
			mDownloadText
					.setBackgroundResource(R.drawable.offlinearrow_tab1_normal);

			mDownloadedText
					.setBackgroundResource(R.drawable.offlinearrow_tab2_pressed);
			// mDownloadedAdapter.notifyDataChange();
			break;
		}
		mDownloadedText.setPadding(paddingHorizontal, paddingVertical,
				paddingHorizontal, paddingVertical);
		mDownloadText.setPadding(paddingHorizontal, paddingVertical,
				paddingHorizontal, paddingVertical);

	}

}
