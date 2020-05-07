package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.entity.Pagination;

public interface TeacherService extends BaseService<Teacher, String> {

    List<Teacher> findByUnitId(String unitId);

    void saveWithUser(Teacher teacher, User user);

    List<Teacher> findByUnitId(String unitId, Pagination page);

    List<Teacher> findByDeptId(String deptId);

    List<Teacher> findByDeptId(String deptId, Pagination page);

    Teacher findByUsername(String username);

    Teacher findByUserId(String userId);

    Map<String, List<Teacher>> findMapByDeptIdIn(String[] deptIds);

    List<Teacher> saveAllEntitys(Teacher... teacher);

    /**
     * 硬删
     *
     * @param id
     */
    void deleteAllByIds(String... id);

    List<Teacher> findByTeacherNameAndIdentityCard(String teacherName, String identityCard);

    List<Teacher> findByTeacherNameAndIdentityCardWithNoUser(String realName,
                                                             String identityCard);

    List<Teacher> findByTeacherNameAndMobilePhone(String realName, String mobilePhone);

    Teacher findByCardNumber(String unitId, String cardNumber);

    List<Teacher> findListByTeacherName(String unitId, String teacherName);

    //id,teacherName
    Map<String, String> findPartByTeacher(String[] ids);

    void deleteTeachersByUnitId(String unitId);
}
