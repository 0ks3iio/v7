package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.framework.entity.Pagination;

public interface GradeService extends BaseService<Grade, String> {

    /**
     * 通用方法 根据单位找未删除未毕业的年级 排序：order by displayOrder, section, openAcadyear desc, gradeName
     *
     * @param unitId
     * @return
     */
    List<Grade> findByUnitId(String unitId);

    /**
     * 通用方法 根据单位找未删除未毕业的年级 排序：order by displayOrder, section, openAcadyear desc, gradeName
     *
     * @param unitId
     * @return
     */
    List<Grade> findByUnitId(String unitId, Pagination page);

    /**
     * 根据单位找未删除未毕业的年级 排序：order by openAcadyear desc
     *
     * @param unitId
     * @return
     */
    List<Grade> findByUnitIdOrderByOpenAcadyear(String unitId);

    /**
     * 根据单位、学段、开设学年找未删除的年级 排序：无排序
     *
     * @param unitId
     * @param section
     * @param acadyear 可以为空 根据排序openAcadyear
     * @return
     */
    List<Grade> findBySectionAndAcadyear(String unitId, Integer section, String acadyear);

    /**
     * 根据单位、学段找未删除的年级 排序：无排序
     *
     * @param unitId
     * @param section
     * @return
     */
    List<Grade> findByUnitId(String unitId, Integer... section);

    /**
     * 根据id软删
     *
     * @param ids
     */
    void deleteAllIsDeleted(String... ids);

    /**
     * 保存年级同时更新班级的年制字段
     *
     * @param grade
     */
    void saveGradeOne(Grade grade);

    /**
     * 根据单位、年级code找未删除未毕业的年级 排序：order by displayOrder, section, openAcadyear desc, gradeName
     *
     * @param unitId
     * @param gradeCodes
     * @return
     */
    List<Grade> findByUnitIdAndGradeCode(String unitId, String... gradeCodes);

    /**
     * 获取该学年下正常的某学段年纪
     * @param unitId
     * @param sections
     * @param currentAcadyear
     * @return
     */
    public List<Grade> findByUnitIdAndGradeCode(String unitId,Integer[] sections, String currentAcadyear);

    /**
     * 根据单位条件查找相关年级信息
     *
     * @param unitid
     * @param currentAcadyear 当前学年
     * @param isGraduate      是否需要过来毕业条件，可以为空
     * @param ReCalculation   年级名称是否根据当前学年重新推算
     * @return
     */
    List<Grade> findByUnitIdAndCurrentAcadyear(String unitid, String currentAcadyear, Integer isGraduate,
                                               boolean ReCalculation);

    /**
     * 获取单位学年下正常的年级
     *
     * @param unitId
     * @param acadyear
     * @return
     */
    List<Grade> findByUnitIdsAndCurrentAcadyear(String[] unitIds, String acadyear);

    Map<String, List<Grade>> findByUnitIdMap(String[] unitIds);

    /**
     * 根据教师ID得到该教师是年级组长的年级列表
     *
     * @param teacherId
     * @return
     */
    List<Grade> findByTeacherId(String teacherId);

    void updateGraduate(Date date, String schId, String acadYear, Integer section, Integer schoolingLength);

    List<Grade> findBySchidSectionAcadyear(String schoolId, String curAcadyear, Integer[] section);

    List<Grade> findByUnitIdNotGraduate(String schoolId, Integer... section);

    List<Grade> saveAllEntitys(Grade... grade);

    /**
     * 获取年级信息（不包括删除的）
     *
     * @param schoolId
     * @param section
     * @param openAcadyear
     * @param isOnlyNotGraduate 是否只获取未毕业的
     * @return
     */
    List<Grade> findGradeList(String schoolId, Integer[] section, String openAcadyear, boolean isOnlyNotGraduate);

    Grade findByIdAndCurrentAcadyear(String unitId, String gradeId,
                                     String searchAcadyear);

    List<Grade> findByIdsIn(String[] ids);

    Grade findById(String gradeId);

    List<Grade> findBySchoolIdAndIsGraduate(String unitId, Integer graduated);

    List<Grade> findBySchoolIdAndAcadyear(String unitId, String acadyear);

    /**
     * 特殊接口如老师存在跨年级教学情况、课表上下午都是取年级最大值接口调用如下
     * 根据需求传入，如果gradeIds更精确
     *
     * @param schoolId 不是必填
     * @param gradeIds
     * @return Grade
     */
    Grade findTimetableMaxRangeBySchoolId(String schoolId, String[] gradeIds);

    void deleteGradesBySchoolId(String unitId);
    
    public List<Grade> findByUnitIdsIn(String[] unitIds);

    List<Grade> findBySchoolIdsAndOpenAcaday(String[] schoolIds, String openAcadyear);
}
