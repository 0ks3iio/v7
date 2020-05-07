package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkReportBase;

public interface NewGkReportBaseService extends BaseService<NewGkReportBase, String>{

	List<NewGkReportBase> findByReportIdAndType(String[] reportIds, String[] types);
	List<NewGkReportBase> findByReportIdAndTypeWithMaster(String[] reportIds, String[] types);

	void deleteByReportId(String reportId);

}