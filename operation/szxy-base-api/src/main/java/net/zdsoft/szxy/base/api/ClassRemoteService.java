package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Clazz;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;

/**
 * 基础数据：班级
 * @author shenke
 * @since 2019/3/21 下午4:30
 */
@Rpc(domain = RecordName.name)
public interface ClassRemoteService {
    /**
     * 根据班级ID查询班级信息
     * @param id 班级ID
     * @return
     */
    Clazz getClazzById(String id);

    List<Clazz> getClazzesByGradeId(String gradeId);
}
