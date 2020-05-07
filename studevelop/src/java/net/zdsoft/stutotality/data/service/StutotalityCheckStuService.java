package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityCheckStu;

import java.util.List;

public interface StutotalityCheckStuService extends BaseService<StutotalityCheckStu,String> {


    List<StutotalityCheckStu> findByUParms(String unitId, String acadyear, String semester);

    List<StutotalityCheckStu> findByGParms(String gradeId, String acadyear, String semester);

    List<StutotalityCheckStu> findBySParms(String[] studentIds, String acadyear, String semester);
}
