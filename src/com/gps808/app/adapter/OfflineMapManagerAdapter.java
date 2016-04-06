/**
 * @description: 下载管理列表适配器
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年9月7日 下午4:53:40   
 * @version 1.0   
 */
package com.gps808.app.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.gps808.app.R;
import com.gps808.app.activity.ShowMapActivity;
import com.gps808.app.interfaces.OnOfflineItemStatusChangeListener;
import com.gps808.app.models.OfflineMapItem;
import com.gps808.app.utils.FileUtils;
import com.gps808.app.view.FancyButton;

public class OfflineMapManagerAdapter extends ArrayListAdapter<OfflineMapItem> {

	// ------------------------ Constants ------------------------

	// ------------------------- Fields --------------------------

	private Context context;
	private MKOfflineMap mOffline;
	private OnOfflineItemStatusChangeListener listener;
	private HashSet<Integer> expandedCityIds = new HashSet<Integer>();

	// ----------------------- Constructors ----------------------

	public OfflineMapManagerAdapter(Context context, MKOfflineMap mOffline,
			OnOfflineItemStatusChangeListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mOffline = mOffline;
		this.listener = listener;
	}

	// -------- Methods for/from SuperClass/Interfaces -----------

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listitem_offline_manager,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		OfflineMapItem data = (OfflineMapItem) getItem(position);
		holder.setData(data);

		return convertView;
	}

	@Override
	public void setDatas(List<OfflineMapItem> ds) {
		// TODO Auto-generated method stub
		super.setDatas(ds);

		expandedCityIds.clear();
	}

	@Override
	public void setArrayDatas(OfflineMapItem[] array) {
		// TODO Auto-generated method stub
		super.setArrayDatas(array);

		expandedCityIds.clear();
	}

	// --------------------- Methods public ----------------------

	public List<OfflineMapItem> search(String key) {
		List<OfflineMapItem> list = new ArrayList<OfflineMapItem>();
		if (mDatas != null) {
			for (OfflineMapItem item : mDatas) {
				if (item.getCityName().indexOf(key) >= 0) {
					list.add(item);
				}
			}
		}
		return list;
	}

	// --------------------- Methods private ---------------------

	// --------------------- Getter & Setter ---------------------

	// --------------- Inner and Anonymous Classes ---------------

	class ViewHolder implements OnClickListener {
		View lyCityInfo;
		TextView tvCityname;
		TextView tvSize;
		ImageView ivExpande;

		View lyEditPanel;
		ProgressBar pbDownload;
		TextView tvStatus;
		FancyButton btnDown;
		FancyButton btnRemove;
		FancyButton btnDisplay;

		private OfflineMapItem data;

		public ViewHolder(View convertView) {
			lyCityInfo = convertView.findViewById(R.id.lyCityInfo);
			tvCityname = (TextView) convertView.findViewById(R.id.tvCityname);
			tvSize = (TextView) convertView.findViewById(R.id.tvSize);
			ivExpande = (ImageView) convertView.findViewById(R.id.ivExpande);
			lyEditPanel = convertView.findViewById(R.id.lyEditPanel);
			pbDownload = (ProgressBar) convertView
					.findViewById(R.id.pbDownload);
			tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			btnDown = (FancyButton) convertView.findViewById(R.id.btnDown);
			btnRemove = (FancyButton) convertView.findViewById(R.id.btnRemove);
			btnDisplay = (FancyButton) convertView
					.findViewById(R.id.btnDisplay);

			lyCityInfo.setOnClickListener(this);
			btnDown.setOnClickListener(this);
			btnRemove.setOnClickListener(this);
			btnDisplay.setOnClickListener(this);
		}

		public void setData(OfflineMapItem data) {
			this.data = data;

			tvCityname.setText(data.getCityName());
			tvCityname.setTextColor(mContext.getResources().getColor(
					R.color.black));

			tvSize.setText("(" + FileUtils.formatFileSize(data.getSize()) + ")");
			btnDown.setVisibility(View.VISIBLE);
			btnRemove.setVisibility(View.VISIBLE);

			if (expandedCityIds.contains(data.getCityId())) {
				lyEditPanel.setVisibility(View.VISIBLE);
				ivExpande.setImageResource(R.drawable.ssdk_recomm_plats_less);
			} else {
				lyEditPanel.setVisibility(View.GONE);
				ivExpande.setImageResource(R.drawable.ssdk_recomm_plats_more);
			}

			if (data.getStatus() == MKOLUpdateElement.DOWNLOADING) {
				tvCityname.setTextColor(Color.BLUE);

				tvStatus.setText("正在下载" + data.getProgress() + "%");
				btnDown.setText("暂停");
				pbDownload.setProgress(data.getProgress());

				btnDown.setVisibility(View.VISIBLE);
				pbDownload.setVisibility(View.VISIBLE);
				tvStatus.setVisibility(View.VISIBLE);

			} else if (data.getStatus() == MKOLUpdateElement.FINISHED) {
				if (data.isHavaUpdate()) {
					tvCityname.setTextColor(Color.BLUE);

					tvStatus.setText("有更新");
					btnDown.setText("更新");
					pbDownload.setProgress(data.getProgress());

					btnDown.setVisibility(View.VISIBLE);
					pbDownload.setVisibility(View.VISIBLE);
					tvStatus.setVisibility(View.VISIBLE);

				} else {
					btnDown.setVisibility(View.GONE);
					pbDownload.setVisibility(View.GONE);
					tvStatus.setVisibility(View.GONE);
				}

			} else if (data.getStatus() == MKOLUpdateElement.SUSPENDED
					|| data.getStatus() == MKOLUpdateElement.UNDEFINED
					|| data.getStatus() >= MKOLUpdateElement.eOLDSMd5Error) {
				tvCityname.setTextColor(mContext.getResources().getColor(
						R.color.app_blue));

				// 暂停、未知、错误，都是继续下载
				tvStatus.setText("暂停");
				btnDown.setText("继续");
				pbDownload.setProgress(data.getProgress());

				btnDown.setVisibility(View.VISIBLE);
				pbDownload.setVisibility(View.VISIBLE);
				tvStatus.setVisibility(View.VISIBLE);

			} else if (data.getStatus() == MKOLUpdateElement.WAITING) {
				tvCityname.setTextColor(mContext.getResources().getColor(
						R.color.app_blue));
				tvStatus.setText("等待");
				btnDown.setText("暂停");
				pbDownload.setProgress(data.getProgress());

				btnDown.setVisibility(View.VISIBLE);
				pbDownload.setVisibility(View.VISIBLE);
				tvStatus.setVisibility(View.VISIBLE);

			} else {
				btnDown.setVisibility(View.GONE);
				pbDownload.setVisibility(View.GONE);
				tvStatus.setVisibility(View.GONE);
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.lyCityInfo:
				if (lyEditPanel.getVisibility() == View.VISIBLE) {
					lyEditPanel.setVisibility(View.GONE);
					ivExpande
							.setImageResource(R.drawable.ssdk_recomm_plats_more);
					expandedCityIds.remove(data.getCityId());

				} else {
					lyEditPanel.setVisibility(View.VISIBLE);
					ivExpande
							.setImageResource(R.drawable.ssdk_recomm_plats_less);
					expandedCityIds.add(data.getCityId());
				}
				break;

			case R.id.btnDown:
				if (data.getStatus() == MKOLUpdateElement.DOWNLOADING
						|| data.getStatus() == MKOLUpdateElement.WAITING) {
					// 暂停
					int id = data.getCityId();
					if (id > 0) {
						mOffline.pause(id);
						data.setStatus(MKOLUpdateElement.SUSPENDED);
						if (listener != null) {
							listener.statusChanged(data, false);
						}
					}

				} else {
					// 继续or更新
					if (data.isHavaUpdate()) {
						// 先删除,直接start貌似不行
						mOffline.remove(data.getCityId());
						data.setStatus(MKOLUpdateElement.UNDEFINED);
						if (listener != null) {
							listener.statusChanged(data, true);
						}

						// 再下载
						mOffline.start(data.getCityId());

					} else {
						int id = data.getCityId();
						if (id > 0) {
							mOffline.start(id);
							data.setStatus(MKOLUpdateElement.WAITING);
						}
					}

				}
				break;

			case R.id.btnRemove:
				AlertDialog dialog = new AlertDialog.Builder(context)
						.setTitle("提示")
						.setMessage("离线地图为您节省流量，删除后无法恢复，确定要删除？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mOffline.remove(data.getCityId());
										data.setStatus(MKOLUpdateElement.UNDEFINED);
										if (listener != null) {
											listener.statusChanged(data, true);
										}

										dialog.dismiss();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).create();
				dialog.show();
				break;
			default:
				break;
			case R.id.btnDisplay:
				Intent intent = new Intent();
				intent.putExtra("x", data.getDownInfo().geoPt.longitude);
				intent.putExtra("y", data.getDownInfo().geoPt.latitude);
				intent.setClass(context, ShowMapActivity.class);
				context.startActivity(intent);
				break;
			}
		}
	}
}
