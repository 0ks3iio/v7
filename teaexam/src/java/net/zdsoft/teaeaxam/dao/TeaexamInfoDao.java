package net.zdsoft.teaeaxam.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;

import org.springframework.data.jpa.repository.Query;

public interface TeaexamInfoDao extends BaseJpaRepositoryDao<TeaexamInfo, String>{
	@Query("FROM TeaexamInfo WHERE unitId=?1 and infoYear = ?2 and infoType=?3 order by registerBegin desc")
	public List<TeaexamInfo> findByInfoYearType(String unitId, int year, int type);
	
	@Query("FROM TeaexamInfo WHERE infoYear = ?1 and schoolIds like ?2 and state=2 order by infoType,registerBegin desc")
	public List<TeaexamInfo> findByInfoYearSchoolId(int year, String schoolId);
	
	@Query("FROM TeaexamInfo WHERE infoYear = ?1 and schoolIds like ?2 and state=2 and infoType=?3 order by registerBegin desc")
	public List<TeaexamInfo> findByInfoYearTypeSchoolId(int year, String schoolId, int type);
	
	@Query("FROM TeaexamInfo WHERE unitId=?1")
	public List<TeaexamInfo> findByUnitId(String unitId);
	@Query("FROM TeaexamInfo WHERE to_date(?1,'yyyy-MM-dd') >=registerBegin and to_date(?1,'yyyy-MM-dd') <=registerEnd and state=2")
	public List<TeaexamInfo> findByRegisterTime(String registerTime);
	@Query("FROM TeaexamInfo WHERE unitId=?1 and to_date(?2,'yyyy-MM-dd') >=registerBegin and to_date(?2,'yyyy-MM-dd')<=registerEnd and infoType=?3 and state=2")
	public List<TeaexamInfo> findByRegisterTimeAndUnitId(String unitId,
			String registerTime, int type);
	@Query("FROM TeaexamInfo WHERE infoYear = ?1 and infoType=?2 and to_date(?3,'yyyy-MM-dd') >registerEnd and state=2")
	public List<TeaexamInfo> findByRegisterEnd(int year, int type, String registerTime);
	@Query("FROM TeaexamInfo WHERE unitId = ?1 and infoYear = ?2 and infoType=?3 and to_date(?4,'yyyy-MM-dd') > examEnd and state=2 order by registerBegin desc")
	public List<TeaexamInfo> findByEndTime(String unitId, int year, int type,
			String examEndTime);
}
