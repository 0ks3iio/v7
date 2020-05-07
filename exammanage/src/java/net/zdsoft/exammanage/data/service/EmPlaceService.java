package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmPlaceService extends BaseService<EmPlace, String> {

    /**
     * @param examId
     * @param unitId
     * @param isMake 是否添加拓展字段
     * @return
     */
    public List<EmPlace> findByExamIdAndSchoolIdWithMaster(String examId, String unitId, boolean isMake);

    /**
     * 通过考试id以及考点ids 获取考场信息
     *
     * @param examId
     * @param optionIds
     * @return
     */
    public List<EmPlace> findByExamIdAndOptionIds(String examId, String[] optionIds, Pagination page);

    public List<EmPlace> findByExamIdAndOptionIds(String examId, String[] optionIds);

    public void deleteByExamIdAndSchoolId(String examId, String unitId);

    public List<EmPlace> saveAllEntitys(EmPlace... emPlace);

    /**
     * 批量保存考场信息
     *
     * @param emPlaceList
     * @param emPlaceIds
     */
    public void insertEmPlaceList(List<EmPlace> emPlaceList, String[] emPlaceIds);

    public EmPlace findByEmPlaceId(String id);

    public List<EmPlace> findByExamId(String examId);

    public Set<String> getOptionId(String examId);

    /**
     * 获取考场数量
     *
     * @param examId
     * @param unitId
     * @return
     */
    public Integer getSizeByExamIdAndSchoolId(String examId, String unitId);

    public EmPlace findByExamIdAndSchoolIdAndPlaceId(String unitId, String examId, String placeId);

    public Map<String, EmPlace> findByIdsMap(String[] ids);

    /**
     * 组装场地
     *
     * @param list
     */
    public void makePlaceName(List<EmPlace> list);

    public void deleteByExamId(String examId);

    public void deleteByExamIdAndOptionId(String examId, String optionId);

    public Map<String, EmPlace> findByUnitIdAndExamIdMap(String unitId, String examId);
}
