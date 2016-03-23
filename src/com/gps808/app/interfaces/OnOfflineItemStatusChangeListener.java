/**
 * @description: 
 * @author chenshiqiang E-mail:csqwyyx@163.com
 * @date 2014年9月8日 上午1:25:10   
 * @version 1.0   
 */
package com.gps808.app.interfaces;

import com.gps808.app.models.OfflineMapItem;

public interface OnOfflineItemStatusChangeListener {
	public void statusChanged(OfflineMapItem item, boolean removed);
}
