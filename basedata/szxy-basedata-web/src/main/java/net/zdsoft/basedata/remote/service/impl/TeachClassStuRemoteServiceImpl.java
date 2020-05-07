package net.zdsoft.basedata.remote.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.TeachClassStu;
import net.zdsoft.basedata.remote.service.TeachClassStuRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachClassService;
import net.zdsoft.basedata.service.TeachClassStuService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachClassStuRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachClassStuRemoteServiceImpl extends BaseRemoteServiceImpl<TeachClassStu, String> implements
        TeachClassStuRemoteService {

    @Autowired
    private TeachClassStuService teachClassStuService;

    @Override
    public String findMapWithStuIdByClassIds(String[] classIds) {
        return SUtils.s(teachClassStuService.findMapByClassIds(classIds));
    }

    @Override
    public String findByClassIds(String[] classIds) {
        return SUtils.s(teachClassStuService.findByClassIds(classIds));
    }

    @Override
    public String findByStuIds(String[] stuIds) {
        return SUtils.s(teachClassStuService.findByStuIds(stuIds));
    }

    @Override
    public void deleteByIds(String[] ids) {
        teachClassStuService.deleteByIds(ids);
    }

    @Override
    protected BaseService<TeachClassStu, String> getBaseService() {
        return teachClassStuService;
    }

    @Override
    public void saveAll(String teachClassStues) {
        TeachClassStu[] tss = SUtils.dt(teachClassStues, new TR<TeachClassStu[]>() {
        });
        if (ArrayUtils.isNotEmpty(tss)) {
            teachClassStuService.saveAllEntitys(tss);
        }
    }

    @Override
    public String findAll() {
        // TODO Auto-generated method stub
        return super.findAll();
    }

    @Override
    public String findTeachClassByStudentId(String studentId, String acadyear, String semester) {
        return SUtils.s(teachClassStuService.findByStudentId(studentId, acadyear, semester));
    }

	@Override
	public void delete(String[] teachClassId, String[] studentId) {
		teachClassStuService.delete(teachClassId,studentId);
	}

	@Override
	public String findTeachClassByStudentId2(String studentId, String acadyear, String semester) {
		return SUtils.s(teachClassStuService.findByStudentId2(studentId, acadyear, semester));
	}

	@Override
	public String findStudentByClassIds(String[] classIds) {
		return SUtils.s(teachClassStuService.findStudentByClassIds(classIds));
	}
}
