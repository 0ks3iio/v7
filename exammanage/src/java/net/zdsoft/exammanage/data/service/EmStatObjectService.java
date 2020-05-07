package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmStatObject;

import java.util.List;

public interface EmStatObjectService extends BaseService<EmStatObject, String> {

    /**
     * 查询统计对象信息
     *
     * @param unitId
     * @param examId
     * @return
     */
    public EmStatObject findByUnitIdExamId(String unitId, String examId);

    public void updateIsStat(String isStat, String... id);

    public List<EmStatObject> findByUnitId(String unitId);

    public List<EmStatObject> findByUnitIdAndExamIdIn(String unitId,
                                                      String[] examIds);
}
