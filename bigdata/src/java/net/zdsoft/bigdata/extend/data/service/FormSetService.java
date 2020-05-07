package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.framework.entity.Pagination;

public interface FormSetService extends BaseService<FormSet, String> {

	public List<FormSet> findListByOrder(String formName,Pagination page);

	/**
	 * 保存表单
	 * @param form
	 * @param content  表单html页面
	 */
	public void saveOrUpdate(FormSet form,String content);

	public String findHtmlByMdId(String mdId);
	public FormSet findByMdId(String mdId);


}
