package net.zdsoft.scoremanage.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.Filtration;

public interface FiltrationService extends BaseService<Filtration, String>{

	/**
	 * 
	 * @param examId
	 * @param unitId
	 * @param type 1：不排考:2：不统考
	 * @return <学生id,学生id>
	 */
	public Map<String, String> findByExamIdAndSchoolIdAndType(String examId,
			String unitId, String type);

	public void insertList(List<Filtration> fList);

	public void deleteByExamIdAndStudentIdIn(String examId,String type, String[] studentIds);

	public List<Filtration> findListBySchoolIds(String examId, String... schoolIds);
	
	public List<Filtration> saveAllEntitys(Filtration... filtration);

	public List<Filtration> findBySchoolIdAndStudentIdAndType(String unitId, String studentId, String type);

}
