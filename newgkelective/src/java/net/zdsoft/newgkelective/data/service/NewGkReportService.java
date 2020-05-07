package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkReport;
import net.zdsoft.newgkelective.data.entity.NewGkReportBase;
import net.zdsoft.newgkelective.data.entity.NewGkReportChose;

public interface NewGkReportService extends BaseService<NewGkReport, String>{
	/**
	 * 参数不能为空
	 * @param unitIds
	 * @param openAcadyear
	 * @param gradeId
	 * @return
	 */
	public List<NewGkReport> findListBy(String[] unitIds,String openAcadyear);
	public List<NewGkReport> findListByWithMaster(String[] unitIds,String openAcadyear);

		/**
	 * 缓存一周，因为页面显示当前3个年级都会展现，主要是用于查看历史的，缓存时间长一点也是可以的
	 * @return openAcadyear
	 */
	public List<String> findAllOpenAcadyear();

    void saveAllChoseList(NewGkReport newGkReport, List<NewGkReportChose> newGkReportChoseList);

	/**
	 * 保存 上报资源
	 * @param newGkReport
	 * @param baseList
	 */
	public void saveAllBaseList(NewGkReport newGkReport, List<NewGkReportBase> baseList);

	NewGkReport findOneChoseByGradeId(String unitId, String gradeId);

    NewGkReport findOneByGradeId(String unitId, String gradeId);
    
}
