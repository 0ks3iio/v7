package net.zdsoft.basedata.dao;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dto.TeachClassSearchDto;
import net.zdsoft.basedata.entity.TeachClass;

public interface TeachClassJdbcDao {
	List<TeachClass> findListByDto(TeachClassSearchDto dto);

	Map<String, List<String>> findMapByVirtualIds(String unitId, String acadyear, String semester, String gradeId, String[] virtualIds);

	List<TeachClass> findClassHasEx(String unitId, String acadyear, String semester, String gradeId,
			String[] classTypes);
}
