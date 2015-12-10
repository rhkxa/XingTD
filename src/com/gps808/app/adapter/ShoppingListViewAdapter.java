package com.gps808.app.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gps808.app.R;
import com.gps808.app.bean.BnShopping;
import com.gps808.app.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShoppingListViewAdapter extends BaseAdapter {

	private Context mContext;
	private List<BnShopping> selectList;
	private List<BnShopping> dataList;

	public ShoppingListViewAdapter(Context c, List<BnShopping> dataList,
			List<BnShopping> selectList) {

		this.mContext = c;
		this.dataList = dataList;
		this.selectList = selectList;

	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();

	}

	@Override
	public Object getItem(int position) {
		// return dataList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_shopping_list, parent, false);

			vh.item_shopping_checkBox = (CheckBox) convertView
					.findViewById(R.id.item_shopping_checkbox);
			vh.currency_add = (ImageButton) convertView
					.findViewById(R.id.currency_add);
			vh.currency_subtract = (ImageButton) convertView
					.findViewById(R.id.currency_subtract);
			vh.item_shopping_delete = (ImageView) convertView
					.findViewById(R.id.item_shopping_delete);
			vh.item_shopping_image = (ImageView) convertView
					.findViewById(R.id.item_shopping_image);
			vh.item_shopping_introduce = (TextView) convertView
					.findViewById(R.id.item_shopping_introduce);
			vh.item_shopping_new_price = (TextView) convertView
					.findViewById(R.id.item_shopping_new_price);
			vh.item_shopping_old_price = (TextView) convertView
					.findViewById(R.id.item_shopping_old_price);
			vh.show_text = (TextView) convertView.findViewById(R.id.show_text);
			vh.item_shopping_parameters = (TextView) convertView
					.findViewById(R.id.item_shopping_parameters);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(
				dataList.get(position).getImg_url(), vh.item_shopping_image);
		vh.item_shopping_introduce.setText(dataList.get(position).getName());
		vh.item_shopping_new_price.setText("￥"
				+ dataList.get(position).getPrice());
		vh.item_shopping_old_price.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG);
		vh.item_shopping_old_price.setText("￥"
				+ dataList.get(position).getMktprice());
		if (dataList.get(position).getSpecs().size() > 0) {
			vh.item_shopping_parameters.setText(dataList.get(position)
					.getSpecs().get(0).getSpec_name()
					+ dataList.get(position).getSpecs().get(0).getSpec_value());
		}

		vh.show_text.setText("" + dataList.get(position).getQuantity());

		vh.item_shopping_checkBox.setChecked(isInSelectedDataList(dataList.get(
				position).getProduct_id()));
		vh.item_shopping_checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size()) {
					mOnItemClickListener.onItemClick(vh.item_shopping_checkBox,
							dataList.get(position),
							vh.item_shopping_checkBox.isChecked());

				}
			}
		});
		vh.currency_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// dataList.get(position).getQuantity() + 1)) {
				// dataList.get(position).setQuantity(
				// dataList.get(position).getQuantity() + 1);
				mOnItemClickListener.onEdit(position, dataList.get(position)
						.getQuantity() + 1);

			}
		});
		vh.currency_subtract.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (dataList.get(position).getQuantity() > 1) {

					// dataList.get(position).setQuantity(
					// dataList.get(position).getQuantity() - 1);
					mOnItemClickListener.onEdit(position, dataList
							.get(position).getQuantity() - 1);

				}
			}
		});
		vh.item_shopping_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext)
						.setTitle("删除商品")
						.setMessage("确定要删除吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										mOnItemClickListener.onDelete(
												dataList.get(position),
												position);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// TODO Auto-generated method stub
										arg0.dismiss();
									}
								}).show();
			}
		});

		return convertView;
	}

	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < selectList.size(); i++) {
			if (selectList.get(i).getProduct_id().equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(CheckBox view, BnShopping shoppingBean,
				boolean isChecked);

		public void onDelete(BnShopping shoppingBean, int position);

		public void onEdit(int position, int quantity);
	}

	class ViewHolder {
		CheckBox item_shopping_checkBox;
		TextView item_shopping_old_price;
		TextView item_shopping_new_price;
		TextView item_shopping_introduce;
		TextView item_shopping_parameters;
		ImageView item_shopping_image;
		ImageView item_shopping_delete;
		ImageButton currency_subtract;
		ImageButton currency_add;
		TextView show_text;
	}

}
