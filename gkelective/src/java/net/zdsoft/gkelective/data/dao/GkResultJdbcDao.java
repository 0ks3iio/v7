package net.zdsoft.gkelective.data.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.entity.GkResult;

public interface GkResultJdbcDao {

    /**
     * 查询所有单科选课人数统计
     * 
     * @param arrangeId
     * @param courseIds
     * @return
     */
    public Map<String, Integer> findResultCountByArrangeIdAndCourseIds(String arrangeId, String... courseIds);

    public Map<String, Integer> findResultCountBySubIdsStuIds(String arrangeId, String[] courseIds,String[] stuids);
    /**
     * 特定优化查下不走in方法
     * @param arrangeId
     * @param gradeId
     * @param dto TODO
     * @return
     */
    public List<GkResult> findResultByChosenSubjectSearchDto(String arrangeId,String gradeId, ChosenSubjectSearchDto dto);
    /**
     * 
     * @param arrangeId
     * @param subjectIds String[]leng =3 学生学的三个科目
     */
    public void deleteBySubjectArrangeIdAndSubjectId(String arrangeId,
			String[] subjectIds);
}
