package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;

public interface NewGkRelateSubtimeService extends BaseService<NewGkRelateSubtime, String>{

	public void saveAllNewGkRelateSubtime(String itemId,List<NewGkRelateSubtime> subtimeList);
	
	public List<NewGkRelateSubtime> findListByItemId(String itemId);
	
	public void deletedByItemId(String itemId);

	//删除某个科目关联的不连排科目
	public void deleteLikeSubjectIdType(String gradeId, String subjectIdType);
}
