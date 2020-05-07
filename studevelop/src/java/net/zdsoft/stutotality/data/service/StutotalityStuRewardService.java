package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityStuReward;

import java.util.List;

public interface StutotalityStuRewardService extends BaseService<StutotalityStuReward,String>{

    List<StutotalityStuReward> getByAcadyearAndSemesterAndUnitIdAndStudentId(String year, String semester, String unitId, String studentId);

    List<StutotalityStuReward> getByAcadyearAndSemesterAndUnitIdAndStudentIdWithMaster(String year, String semester, String unitId, String studentId);
    // 获奖情况导入
    public String acadListImport(String unitId, List<String[]> datas, String acadyear, String semester ,String classId);
}
