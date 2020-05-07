package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;

public interface NewGkDivideStusubJdbcDao {

	List<NewGkDivideStusub> findByDivideIdAndSubject(String divideId, String subjectType,String[] subjectIdArr);

	List<NewGkDivideStusub> findNoArrangeXzbStudent(String divideId, String[] subjectIdArr);

}
