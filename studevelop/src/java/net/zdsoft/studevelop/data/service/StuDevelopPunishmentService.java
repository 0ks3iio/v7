package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.studevelop.data.entity.StuDevelopPunishment;

public interface StuDevelopPunishmentService extends BaseService<StuDevelopPunishment, String>{
	
	public List<StuDevelopPunishment> findListByAll(String acadyear, String semester, String unitId, String classId, String studentId, String punishtype, Pagination page);

	public void save(StuDevelopPunishment stuDevelopPunishment);
	
	public StuDevelopPunishment findById(String id);
	
	public void deleteById(String id);
	
	public List<StuDevelopPunishment> findByAcaAndSemAndStuId(String acadyear, String semester, String studentId);
	
	public String doImport(String unitId, List<String[]> datas,
			String acadyear, String semester, String ownerId);
}
