package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkReportChose;

public interface NewGkReportChoseService extends BaseService<NewGkReportChose, String>{
	
	List<NewGkReportChose> findByReportIdAndType(String[] reportIds,String dataType, String dataKeys);

    List<NewGkReportChose> findByReportIdAndTypeWithMaster(String[] reportIds,String dataType, String dataKeys);

    void deleteByReportId(String reportId);
}
