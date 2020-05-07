package net.zdsoft.framework.popup;

import java.util.HashMap;
import java.util.Map;

import net.zdsoft.framework.action.BaseAction;

public abstract class BaseDivAction extends BaseAction {
	
	public static Map<String, String> codeMap = new HashMap<String, String>();
	
	public static final int MAX_COUNT=12;

	public static final String TYPE_TOP = "top";

	public static final String TYPE_SUB = "sub";

	public static final String TYPE_DATA = "data";
	
	public static final String TYPE_DATA_HISTORY = "data-history";

	/**
	 * 获取业务数据
	 * @return
	 */
	public abstract String showPopUpData();
	
	/**
	 * 获取最近操作的数据
	 * @param ids
	 * @return
	 */
	public abstract String putRecentPopUpData(String ids);
}
