package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Grade;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;

/**
 * 基础数据：年级接口
 * @author shenke
 * @since 2019/3/21 下午4:52
 */
@Rpc(domain = RecordName.name)
public interface GradeRemoteService {

    /**
     * 不包含软删的数据
     * @param gradeId 不能为空
     * @return
     */
    Grade getGradeById(String gradeId);

    /**
     * 查询指定学校的年级信息 <br>不包含软删的, 未毕业的数据<br/>
     * @param schoolId 学校ID不能为空
     * @return
     */
    List<Grade> getGradesBySchoolId(String schoolId);
}
