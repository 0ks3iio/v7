package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.query.StudentQuery;
import net.zdsoft.szxy.monitor.Rpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 基础数据：学生接口
 * @author shenke
 * @since 2019/3/19 下午6:04
 */
@Rpc(domain = RecordName.name)
public interface StudentRemoteService {

    /**
     * 根据Id查询指定学生信息，不包括软删的数据
     * @param id 学生ID
     * @return
     */
    Student getStudentById(String id);

    /**
     * 根据学生id列表查询学生信息，不包含软删的数据
     * @param ids 学生id数组
     * @return List
     */
    List<Student> getStudentsById(String[] ids);

    /**
     * 查询学生信息
     * @param studentQuery 动态查询条件
     * @param pageable 分页条件 {@link net.zdsoft.szxy.dubbo.jpa.DubboPageable} 原生分页对象在dubbo序列化和反序列化的时候出现问题
     * @return
     */
    Page<Student> queryStudents(StudentQuery studentQuery, Pageable pageable);
}
