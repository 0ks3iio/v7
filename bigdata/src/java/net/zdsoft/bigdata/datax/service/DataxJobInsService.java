package net.zdsoft.bigdata.datax.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datax.entity.DataxJobIns;

import java.util.List;

public interface DataxJobInsService extends BaseService<DataxJobIns, String> {

    /**
     * 查询任务实例
     * @param jobId
     * @return
     */
    List<DataxJobIns> findByJobId(String jobId);

    /**
     * 保存
     * @param dataxJobIns
     */
    void saveDataxJobIns(DataxJobIns dataxJobIns);

    /**
     * 删除
     * @param id
     */
    void deleteDataxJobIns(String id);

    /**
     * 查询第一个任务
     * @param jobId
     * @return
     */
    DataxJobIns findTopJobIns(String jobId);

    void deleteByJobId(String jobId);
}
