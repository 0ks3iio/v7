package net.zdsoft.studevelop.mobile.dao;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.mobile.entity.StuOutside;

public interface StuOutsideDao extends BaseJpaRepositoryDao<StuOutside, String>{
	
	@Modifying
	@Query("update StuOutside set title = ?1, content = ?2, modifyTime = ?3 where id = ?4 ")
	void update(String title, String content, Date modifyTime, String id);
	
	@Query("From StuOutside where id = ?1 ")
	StuOutside findObj(String id);
	
	@Query("From StuOutside where acadyear = ?1 and semester = ?2 and studentId = ?3 and type = ?4 order by creationTime desc")
	List<StuOutside> findList(String acadyear, String semester, String studentId, int type);
	@Query("From StuOutside where acadyear = ?1 and semester = ?2 and studentId in ?3  order by creationTime desc")
	List<StuOutside> findStuOutSideList(String acadyear, String semester, String[] studentIds);
}
