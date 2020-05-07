package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.studevelop.data.entity.StuDevelopRewards;

public interface StuDevelopRewardsService extends BaseService<StuDevelopRewards, String>{

	public List<StuDevelopRewards> findListByAcaAndSemAndUnitId(String acadyear, String semester, String unitId, String classId, String studentId, String rewardslevel, Pagination page);
	
	public void save(StuDevelopRewards stuDevelopRewards);
	
	public StuDevelopRewards findById(String id);
	
	public void deleteById(String id);
	
	public List<StuDevelopRewards> findByAcaAndSemAndStuId(String acadyear,String semester, String studentId);
	
	public String doImport(String unitId, List<String[]> datas, String acadyear, String semester);
}
