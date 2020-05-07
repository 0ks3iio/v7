package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityGoodStat;

import java.util.List;

public interface StutotalityGoodStatService extends BaseService<StutotalityGoodStat,String> {
    List<StutotalityGoodStat> findListByStudentIds(String year, String semete, String[] studentIds );
    List<StutotalityGoodStat> findListByStudentIdsWithMaster(String year, String semete, String[] studentIds );
    void delByStudentIds(String year, String semeter,String[] studentIds);

    void saveAll(String year, String semeter, String[] studentIds,List<StutotalityGoodStat>list);
}
