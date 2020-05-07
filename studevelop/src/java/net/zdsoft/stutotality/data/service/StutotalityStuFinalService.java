package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityStuFinal;

import java.util.List;


public interface StutotalityStuFinalService extends BaseService<StutotalityStuFinal,String>{

    public List<StutotalityStuFinal> findListByStuIds(String [] studentIds);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitIdAndStudentIds(String year, String semester, String unitId,String[] studentIds);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitIdAndStudentIdsWithMaster(String year, String semester, String unitId,String[] studentIds);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitId(String year, String semester, String unitId);

    public List<StutotalityStuFinal> findByAcadyearAndSemesterAndUnitIdWithMaster(String year, String semester, String unitId);
    // 出勤记录导入
    public String attendRecordImport(String unitId, List<String[]> datas, String acadyear, String semester ,String classId);
    // 期末评价-班主任寄语导入
    public String acadListImport(String unitId, List<String[]> datas, String acadyear, String semester ,String classId);
}
