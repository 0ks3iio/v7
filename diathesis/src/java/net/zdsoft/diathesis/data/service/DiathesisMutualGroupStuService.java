package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisMutualGroupStu;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:14
 */
public interface DiathesisMutualGroupStuService extends BaseService<DiathesisMutualGroupStu,String> {

    /** 根据学生id, 学年  学期
     * 获得学生所在组的 组id
     * @param studentId
     * @return
     */
    String findMutualGroupIdByStudentId(String studentId,String acady,Integer semester);

    List<DiathesisMutualGroupStu> findListByMutualGroupId(String groupId);

    List<DiathesisMutualGroupStu> findListByMutualGroupIdIn(List<String> groupId);

    void deleteByMutualGroupIds(List<String> groupIds);
}
