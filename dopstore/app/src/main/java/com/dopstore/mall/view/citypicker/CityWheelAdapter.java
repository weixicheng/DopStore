/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.dopstore.mall.view.citypicker;

import com.dopstore.mall.activity.bean.CityBean;

import java.util.List;

/**
 * Numeric Wheel adapter.
 */
public class CityWheelAdapter implements WheelAdapter {

	// week list
	private List<CityBean> listItem;
	// format
	private String format;
	private int max;

	/**
	 * Constructor
	 * 
	 * @param listItem
	 *            the wheel region value
	 * @param format
	 *            the format string
	 */
	public CityWheelAdapter(List<CityBean> listItem, String format, int max) {
		this.listItem = listItem;
		this.format = format;
		this.max = max;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < listItem.size()) {
			String regionName = (String) listItem.get(index).getName();
			return format != null ? String.format(format, regionName)
					: regionName;
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return listItem.size();
	}

	@Override
	public int getMaximumLength() {

		return max;
	}
}