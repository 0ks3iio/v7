package net.zdsoft.newgkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectGroupColor;

public interface NewGkSubjectGroupColorService extends BaseService<NewGkSubjectGroupColor, String> {
	/**
	 * 
	 * @param unitId 必填
	 * @param groupTypes 可为空，这时 查询所有类型的记录
	 * @return
	 */
	List<NewGkSubjectGroupColor> findByUnitIdGroupType(String unitId, String[] groupTypes);
}
