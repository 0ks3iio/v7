package net.zdsoft.diathesis.data.service.impl;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.service.DiathesisRoleService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.tutor.remote.service.TutorRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service("diathesisRoleService")
public class DiathesisRoleServiceImpl implements DiathesisRoleService {
    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    ClassRemoteService classRemoteService;
    @Autowired
    GradeRemoteService gradeRemoteService;
    @Autowired
    CustomRoleRemoteService customRoleRemoteService;
    @Autowired
    private CustomRoleUserRemoteService customRoleUserRemoteService;
    @Autowired
    private TutorRemoteService tutorRemoteService;


    public static final String SUBSYSTEM_78 = "78";

    @Override
    public Map<String, List<String>> findRoleByUserId(String unitId, String userId) {
        Map<String, List<String>> roleTypeMap = RedisUtils.getObject("REDIS.DIA.ROLE." + userId, RedisUtils.TIME_HALF_MINUTE, new TypeReference<Map<String, List<String>>>() {
        }, new RedisInterface<Map<String, List<String>>>() {
            @Override
            public Map<String, List<String>> queryData() {
                Map<String, List<String>> map = new HashMap<>();
                List<String> adminList = isAdmin(unitId, userId);
                if (CollectionUtils.isNotEmpty(adminList)) {
                    map.put(DiathesisConstant.ROLE_1, adminList);
                }
                User user = SUtils.dc(userRemoteService.findOneById(userId), User.class);
                String teacherId = user.getOwnerId();
                if (StringUtils.isEmpty(teacherId)) {
                    return map;
                }
                List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), Grade.class);
                if (CollectionUtils.isEmpty(gradeList)) {
                    return map;
                }
                //年段长管理的年级
                List<String> gradeIds = new ArrayList<>();

                for (Grade g : gradeList) {
                    if (teacherId.equals(g.getTeacherId())) {
                        gradeIds.add(g.getId());
                    }
                }
                if (CollectionUtils.isNotEmpty(gradeIds)) {
                    map.put(DiathesisConstant.ROLE_2, gradeIds);
                }
                //班主任
                List<String> clazzIds = new ArrayList<>();
                List<Clazz> clazzAllList = SUtils.dt(classRemoteService.findBySchoolIdInGradeIds(unitId,EntityUtils.getArray(gradeList,x->x.getId(),String[]::new)), Clazz.class);
                if (CollectionUtils.isNotEmpty(clazzAllList)) {
                    for (Clazz c : clazzAllList) {
                        if (teacherId.equals(c.getTeacherId()) || teacherId.equals(c.getViceTeacherId())) {
                            clazzIds.add(c.getId());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(clazzIds)) {
                    map.put(DiathesisConstant.ROLE_3, clazzIds);
                }
                //todo v1.2.0 新增导师角色
                List<String> stuIds = SUtils.dt(tutorRemoteService.getTutorStuByTeacherId(teacherId), String.class);
                if (CollectionUtils.isNotEmpty(stuIds)) {
                    map.put(DiathesisConstant.ROLE_4, stuIds);
                }
                return map;
            }

            /**
             * 判断用户是不是管理员  过滤了 学生,班主任,年级主任,导师的身份,
             * 留下自定义的 roleCode
             * @param unitId
             * @param userId
             * @return
             */
            private List<String> isAdmin(String unitId, String userId) {
                List<String> roleIds = new ArrayList<String>();
                List<CustomRole> roleList = SUtils.dt(customRoleRemoteService.findListByUnitAndSubsystem(unitId, SUBSYSTEM_78), CustomRole.class);
                if (CollectionUtils.isNotEmpty(roleList)) {
                    Map<String, String> idCodeMap = EntityUtils.getMap(roleList, CustomRole::getId, CustomRole::getRoleCode);
                    List<CustomRoleUser> roleUserList = SUtils.dt(customRoleUserRemoteService.findListByIn("roleId", EntityUtils.getList(roleList, CustomRole::getId).toArray(new String[roleList.size()])), CustomRoleUser.class);
                    if (CollectionUtils.isNotEmpty(roleUserList)) {
                        Map<String, List<String>> listMap = EntityUtils.getListMap(roleUserList, CustomRoleUser::getRoleId, CustomRoleUser::getUserId);
                        for (Entry<String, List<String>> entry : listMap.entrySet()) {
                            if (entry.getValue().contains(userId)) {
                                if (!DiathesisConstant.ROLE_CODE_LIST.contains(idCodeMap.get(entry.getKey()))) {
                                    roleIds.add(idCodeMap.get(entry.getKey()));
                                }
                            }
                        }
                    }
                }
                return roleIds;
            }
        });
        return roleTypeMap;
    }
}
