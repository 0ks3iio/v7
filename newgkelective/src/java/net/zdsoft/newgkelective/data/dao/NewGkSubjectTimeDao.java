package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;

public interface NewGkSubjectTimeDao extends BaseJpaRepositoryDao<NewGkSubjectTime, String>{

	List<NewGkSubjectTime> findByIdIn(List<String> idList);

	List<NewGkSubjectTime> findByArrayItemId(String arrayItemId);

	List<NewGkSubjectTime> findByArrayItemIdAndSubjectTypeIn(String subjectArrangeId, String[] subjectTypes);

	void deleteByArrayItemId(String itemId);

	@Modifying
	@Query("update NewGkSubjectTime set firstsdWeekSubject='',firstsdWeek=3 where arrayItemId=?1 and firstsdWeekSubject=?2")
	void updateFirstsdWeekSubject(String arrayItemId, String subjejctIdType);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);

	void deleteByIdIn(String[] ids);
}
