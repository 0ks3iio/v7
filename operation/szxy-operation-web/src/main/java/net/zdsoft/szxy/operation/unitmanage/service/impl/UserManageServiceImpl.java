package net.zdsoft.szxy.operation.unitmanage.service.impl;

import net.zdsoft.szxy.base.api.FamilyRemoteService;
import net.zdsoft.szxy.base.api.StudentRemoteService;
import net.zdsoft.szxy.base.api.TeacherRemoteService;
import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.entity.Family;
import net.zdsoft.szxy.base.entity.Student;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.query.StudentQuery;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccount;
import net.zdsoft.szxy.operation.unitmanage.dto.StudentAndFamilyAccountQuery;
import net.zdsoft.szxy.operation.unitmanage.service.UserManageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("userManageService")
public class UserManageServiceImpl implements UserManageService {

    @Resource
    private UserRemoteService userRemoteService;
    @Resource
    private StudentRemoteService studentRemoteService;
    @Resource
    private FamilyRemoteService familyRemoteService;

    @Record(type = RecordType.Service)
    @Override
    public Page<StudentAndFamilyAccount> getStudentAndFamilyAccountsByUnitId(
            String unitId, StudentAndFamilyAccountQuery dynamic, Pageable page) {
        //查询学生信息
        StudentQuery studentQuery = new StudentQuery();
        studentQuery.setClassId(dynamic.getClazzId());
        Page<Student> studentPage = studentRemoteService.queryStudents(studentQuery, page);
        List<Student> students = studentPage.getContent();
        if (students.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), page, 0);
        }
        //查询家长信息
        String[] studentIds = students.stream().map(Student::getId).toArray(String[]::new);
        List<Family> families = familyRemoteService.getFamiliesByStudentId(studentIds);
        Map<String, Family> familyMap = families.stream().collect(Collectors.toMap(Family::getStudentId, Function.identity(), (k, v)->v));

        //账号信息
        Set<String> userIds = students.stream().map(Student::getId).collect(Collectors.toSet());
        userIds.addAll(families.stream().map(Family::getId).collect(Collectors.toSet()));
        List<User> accounts = userRemoteService.getUsersByOwnerIds(userIds.toArray(new String[0]));
        Map<String, User> accountsMap = accounts.stream().collect(Collectors.toMap(User::getOwnerId, Function.identity()));
        final User empty = new User();
        //重新封装
        List<StudentAndFamilyAccount> studentAndFamilyAccounts = students.stream().map(student -> {
            StudentAndFamilyAccount studentAndFamilyAccount = new StudentAndFamilyAccount();
            studentAndFamilyAccount.setStudentName(student.getStudentName());
            User studentUser = accountsMap.getOrDefault(student.getId(), empty);
            studentAndFamilyAccount.setStudentUsername(studentUser.getUsername());
            studentAndFamilyAccount.setStudentId(student.getId());
            studentAndFamilyAccount.setStudentUserId(studentUser.getId());

            Family family = familyMap.get(student.getId());
            if (family != null) {
                studentAndFamilyAccount.setFamilyName(family.getRealName());
                User familyUser = accountsMap.getOrDefault(family.getId(), empty);
                studentAndFamilyAccount.setFamilyUsername(familyUser.getUsername());
                studentAndFamilyAccount.setFamilyPhone(family.getMobilePhone());
                studentAndFamilyAccount.setFamilyId(family.getId());
                studentAndFamilyAccount.setFamilyUserId(familyUser.getId());
            }
            return studentAndFamilyAccount;
        }).collect(Collectors.toList());
        return new PageImpl<>(studentAndFamilyAccounts, page, studentPage.getTotalElements());
    }
}
