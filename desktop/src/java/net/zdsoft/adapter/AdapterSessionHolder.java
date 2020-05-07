package net.zdsoft.adapter;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.entity.LoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenke
 * @date 2019/10/23 下午2:35
 */
@Component
public class AdapterSessionHolder implements InitializingBean {

    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;

    private static Map<String, HttpSession> sessions = new ConcurrentHashMap<>(64);

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().invalidate();
            }
        }
        sessions.clear();
    }

    static void clearHttpSession(HttpSession session) {
        String deleteAuthorization = null;
        for (Map.Entry<String, HttpSession> entry : sessions.entrySet()) {
            if (entry.getValue() != null && entry.getValue().getId().equals(session.getId())) {
                deleteAuthorization = entry.getKey();
            }
        }
        if (deleteAuthorization != null) {
            synchronized (AdapterSessionHolder.class) {
                sessions.remove(deleteAuthorization);
            }
        }
    }

    /**
     * 主动注销
     * @param authorization
     */
    static void invalidAndClearHttpSession(String authorization) {
        synchronized (AdapterSessionHolder.class) {
            HttpSession httpSession = sessions.get(authorization);
            if (httpSession != null) {
                httpSession.invalidate();
            }
            sessions.remove(authorization);
        }
    }


    public void initLoginInfo(HttpSession httpSession, User user, String authorization) {

        LoginInfo loginInfo = new LoginInfo();
        if (user.getOwnerType() == User.OWNER_TYPE_TEACHER) {
            Teacher teacher = teacherRemoteService.findOneObjectById(
                    user.getOwnerId(), new String[] { "id", "deptId" });
            if (teacher != null) {
                loginInfo.setOwnerId(teacher.getId());
                loginInfo.setDeptId(teacher.getDeptId());
            }
        }
        if (user.getOwnerType() == User.OWNER_TYPE_STUDENT) {
            Student stu = studentRemoteService.findOneObjectById(
                    user.getOwnerId(), new String[] { "id", "classId","sex","birthday" });
            if (stu != null) {
                loginInfo.setOwnerId(stu.getId());
                loginInfo.setClassId(stu.getClassId());
                loginInfo.setSex(stu.getSex());
                loginInfo.setBirthday(stu.getBirthday());
            }
        }
        loginInfo.setOwnerType(user.getOwnerType());
        loginInfo.setUnitId(user.getUnitId());
        loginInfo.setUserId(user.getId());
        loginInfo.setOwnerId(user.getOwnerId());
        loginInfo.setUserName(user.getUsername());
        loginInfo.setRealName(user.getRealName());
        loginInfo.setUserType(user.getUserType());

        if (Integer.valueOf(User.OWNER_TYPE_SUPER).equals(user.getOwnerType())) {
            httpSession.setAttribute(
                    net.zdsoft.system.constant.Constant.KEY_OPS_USER, user);
        }
        if (StringUtils.isNotBlank(user.getUnitId())) {
            Unit unit = unitRemoteService.findOneObjectById(user.getUnitId());
            if (unit != null) {
                loginInfo.setUnitClass(unit.getUnitClass());
                loginInfo.setRegion(unit.getRegionCode());
                loginInfo.setUnitName(unit.getUnitName());
            }
        }
        httpSession.setAttribute(MicroAdapterAuthenticationInterceptor.ATTRIBUTE_LOGIN_KEY, loginInfo);
        synchronized (AdapterSessionHolder.class) {
            //authorization和httpSession建立绑定关系
            sessions.put(authorization, httpSession);
        }
    }
}
