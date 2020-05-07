package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;

public interface NewGkDivideStusubDao extends BaseJpaRepositoryDao<NewGkDivideStusub, String>{

	void deleteByChoiceIdAndDivideId(String choiceId, String divideId);

	@Query("From NewGkDivideStusub where divideId=?1 and subjectType =?2 and studentId in (?3)  order by className,subjectIds,studentCode,studentSex")
	List<NewGkDivideStusub> findByDivideIdAndStudentIdIn(String divideId, String subjectType,String[] studentIds);

	void deleteByChoiceIdAndDivideIdAndStudentId(String choiceId, String divideId, String studentId);

}
