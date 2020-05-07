package net.zdsoft.basedata.service;

import java.io.Serializable;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.StringUtils;

public abstract class SyncBasedataService<T extends BaseEntity, K extends Serializable> {

	protected abstract void add(T... t);

	protected abstract void update(T... t);

	protected abstract void delete(K... k);

	/**
	 * 删除前判断是否允许
	 * 
	 * @param k
	 * @return null或者空，表示允许删除，否则返回不允许删除的原因
	 */
	protected abstract String preDelete(K... k);

	/**
	 * 修改前判断是否允许
	 * 
	 * @param k
	 * @return null或者空，表示允许，否则返回不允许的原因
	 */
	protected abstract String preUpdate(T t);

	/**
	 * 新增前判断是否允许
	 * 
	 * @param k
	 * @return null或者空，表示允许，否则返回不允许的原因
	 */
	protected abstract String preAdd(T t);

	public String addEntity(T t) {
		String msg = preAdd(t);
		if (StringUtils.isNotBlank(msg))
			return msg;
		add(t);
		return "";
	}

	public String updateEntity(T t) {
		String msg = preUpdate(t);
		if (StringUtils.isNotBlank(msg))
			return msg;
		update(t);
		return "";
	}

	public String preDel(K... k) {
		String msg = preDelete(k);
		if (StringUtils.isNotBlank(msg))
			return msg;
		return "";
	}
	public void deleteEntity(K... k) {
		delete(k);
	}

}
