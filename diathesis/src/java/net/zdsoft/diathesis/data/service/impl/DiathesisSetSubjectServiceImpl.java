package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.GradeTeaching;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.dao.DiathesisSetSubjectDao;
import net.zdsoft.diathesis.data.dto.DiathesisGradeDto;
import net.zdsoft.diathesis.data.entity.DiathesisSetSubject;
import net.zdsoft.diathesis.data.service.DiathesisSetSubjectService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/8/22 16:23
 */
@Service("diathesisSetSubjectService")
public class DiathesisSetSubjectServiceImpl implements DiathesisSetSubjectService {
    @Autowired
    private DiathesisSetSubjectDao diathesisSetSubjectDao;

    @Autowired
    private UnitRemoteService unitRemoteService;

    @Autowired
    private GradeRemoteService gradeRemoteService;

    @Autowired
    private SchoolRemoteService schoolRemoteService;

    @Autowired
    private CourseRemoteService courseRemoteService;

    @Autowired
    private GradeTeachingRemoteService gradeTeachingRemoteService;
    @Override
    public List<DiathesisSetSubject> findByGradeIdAndGradeCodeAndSemesterAndType(String gradeId, String gradeCode, Integer semester, String type) {
        RedisUtils.hasLocked("diathesisSubSetInitLock_" + gradeId + "_78,");
        List<DiathesisSetSubject> list = diathesisSetSubjectDao.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, semester, type);
        if(CollectionUtils.isEmpty(list)){
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Assert.notNull(grade,"gradeId不存在");
            String schoolId = grade.getSchoolId();
            List<Course> courseList = getTeachingCourses(gradeId, gradeCode, semester, schoolId);
            if(CollectionUtils.isNotEmpty(courseList)){
                courseList = courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).collect(Collectors.toList());
                List<DiathesisSetSubject> saveList = courseList.stream().map(x -> {
                    DiathesisSetSubject set = new DiathesisSetSubject();
                    set.setGradeId(gradeId);
                    set.setGradeCode(gradeCode);
                    set.setSemester(semester);
                    set.setUnitId(schoolId);
                    set.setSubjectType(type);
                    set.setModifyTime(new Date());
                    set.setId(UuidUtils.generateUuid());
                    set.setSubjectId(x.getId());
                    return set;
                }).collect(Collectors.toList());
                diathesisSetSubjectDao.saveAll(saveList);
            }
            list=diathesisSetSubjectDao.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId,gradeCode,semester,type);
        }
        RedisUtils.unLock("diathesisSubSetInitLock_" + gradeId + "_78,");
        return list;
    }

    public List<Course> getTeachingCourses(String gradeId, String gradeCode, Integer semester, String schoolId) {
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String[] acadyearTmp = grade.getOpenAcadyear().split("-");
        String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
        List<GradeTeaching> gradeTeachingList = SUtils.dt(gradeTeachingRemoteService.findBySearchList(schoolId, acadyear, String.valueOf(semester), gradeId, Integer.parseInt(BaseConstants.SUBJECT_TYPE_BX)), GradeTeaching.class);
        return SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getArray(gradeTeachingList, GradeTeaching::getSubjectId, String[]::new)), Course.class);
    }

    @Override
    public void updateSubjectList(DiathesisGradeDto subjectSet) {
        diathesisSetSubjectDao.deleteByGradeIdAndGradeCodeAndSemesterAndType(subjectSet.getGradeId(),subjectSet.getGradeCode(),Integer.parseInt(subjectSet.getSemester()),subjectSet.getType());
        List<String> subjectIs = subjectSet.getSubjectIds();
        List<DiathesisSetSubject> saveList = EntityUtils.getList(subjectIs, x -> {
            DiathesisSetSubject set = new DiathesisSetSubject();
            set.setSubjectId(x);
            set.setId(UuidUtils.generateUuid());
            set.setModifyTime(new Date());
            set.setSubjectType(subjectSet.getType());
            set.setGradeId(subjectSet.getGradeId());
            set.setGradeCode(subjectSet.getGradeCode());
            set.setSemester(Integer.parseInt(subjectSet.getSemester()));
            set.setUnitId(subjectSet.getUnitId());
            return set;
        });
        if(CollectionUtils.isNotEmpty(saveList))diathesisSetSubjectDao.saveAll(saveList);
    }

    @Override
    public List<DiathesisSetSubject> findByGradeIdAndType(String gradeId, String type) {
        return diathesisSetSubjectDao.findByGradeIdAndType(gradeId,type);
    }

}
