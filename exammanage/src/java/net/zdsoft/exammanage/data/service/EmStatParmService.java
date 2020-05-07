package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmStatParm;

import java.util.List;
import java.util.Map;

public interface EmStatParmService extends BaseService<EmStatParm, String> {

    /**
     * 查询某个单位对某次考试某一科目统计参数设置
     *
     * @param unitId
     * @param examId
     * @param subjectId
     * @return
     */
    public EmStatParm findBySubjectId(String unitId, String examId, String subjectId);

    public void deleteBySubjectId(String unitId, String examId, String subjectId);

    public void saveParm(String unitId, EmStatParm emStatParm, boolean isSingle);

    /**
     * 查询某次考试统计参数设置
     *
     * @param unitId
     * @param examId
     * @return key:subjectId
     */
    public Map<String, EmStatParm> findMapByUnitId(String unitId, String examId);

    public void updateStat(String[] ids, String isStat);

    public List<EmStatParm> findByStatObjectIdAndExamId(String statObjectId, String examId);

}
