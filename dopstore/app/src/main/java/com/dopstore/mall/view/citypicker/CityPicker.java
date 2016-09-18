package com.dopstore.mall.view.citypicker;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dopstore.mall.R;
import com.dopstore.mall.activity.bean.CityBean;



/**
 * RegionPicker
 **/
public class CityPicker extends LinearLayout {

	private Context mContext; // 会话上下文
	private WheelView district; // Wheel picker
	private OnChangeListener onChangeListener; // onChangeListener

	private int districtPos = 0; // 省当前位置

	// Constructors
	public CityPicker(Context context) {
		super(context);
		mContext = context;
	}

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}


	public void init(List<CityBean> districtListItem) {
		district = new WheelView(mContext);
		LayoutParams lparams_weeks = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lparams_weeks.setMargins(0, 0, 0, 0);
		district.setBackgroundColor(getResources().getColor(R.color.white_color));
		district.setLayoutParams(lparams_weeks);
		district.setAdapter(new CityWheelAdapter(districtListItem,null, 24));
		if (districtListItem.size() >= 3) {
			district.setVisibleItems(3);
		} else {
			district.setVisibleItems(districtListItem.size());
		}
		district.setCyclic(false);
		district.setTextSize(24);
		district.addChangingListener(onProvinceChangedListener);
		addView(district);
	}

	// listeners
	private OnWheelChangedListener onProvinceChangedListener = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView province, int oldValue, int newValue) {
			setDistrictPos(newValue);
			onChangeListener.onChange(getDistrictPos());
		}
	};

	/**
	 * 定义了监听时间改变的监听器借口
	 * 
	 * @author Shangxiaoxue
	 * 
	 */
	public interface OnChangeListener {
		void onChange(int position);
	}

	/**
	 * 设置监听器的方法
	 * 
	 * @param onChangeListener
	 */
	public void setOnChangeListener(OnChangeListener onChangeListener) {
		this.onChangeListener = onChangeListener;
	}


	public void setCurrentWeek(int position) {
		district.setCurrentItem(position);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 默认设置为0
		setCurrentWeek(getDistrictPos());
	}

	public int getDistrictPos() {
		return districtPos;
	}

	public void setDistrictPos(int position) {
		this.districtPos = position;
	}
}
