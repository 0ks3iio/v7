package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Teacher;
import net.zdsoft.szxy.monitor.Rpc;

/**
 * 基础数据接口：教师
 * @author shenke
 * @since 2019/3/19 下午2:51
 */
@Rpc(domain = RecordName.name)
public interface TeacherRemoteService {

    /**
     * 根据ID查询教师信息，不包含软删的数据
     * @param id 教师ID
     * @return Teacher or null
     */
    Teacher getTeacherById(String id);

    /**
     * 根据要更新教师排序号,同时也会级连更新对应用户的排序号
     * @param displayOrder 教师排序号
     * @param id 主键
     */
    void updateDisplayOrder(Integer displayOrder, String id);
}
