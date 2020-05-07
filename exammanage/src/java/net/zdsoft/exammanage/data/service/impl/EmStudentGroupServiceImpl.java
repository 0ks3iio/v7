package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmStudentGroupDao;
import net.zdsoft.exammanage.data.entity.EmStudentGroup;
import net.zdsoft.exammanage.data.service.EmStudentGroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emStudentGroupService")
public class EmStudentGroupServiceImpl extends BaseServiceImpl<EmStudentGroup, String> implements EmStudentGroupService {
    @Autowired
    private EmStudentGroupDao emStudentGroupDao;

    @Override
    protected BaseJpaRepositoryDao<EmStudentGroup, String> getJpaDao() {
        return emStudentGroupDao;
    }

    @Override
    public List<EmStudentGroup> findByGroupIdAndSchoolId(String groupId, String schoolId) {
        return emStudentGroupDao.findByGroupIdAndSchoolId(groupId, schoolId);
    }

    @Override
    public List<EmStudentGroup> findByGroupIdAndExamIdAndSchoolId(String groupId, String examId, String schoolId) {
        return emStudentGroupDao.findByGroupIdAndExamIdAndSchoolId(groupId, examId, schoolId);
    }

    @Override
    public Map<String, Set<String>> findGroupMap(String unitId, String examId, String[] groupIds) {
        List<EmStudentGroup> stus = emStudentGroupDao.findListBySchoolIdAndExamIdAndGroupIdIn(unitId, examId, groupIds);
        Map<String, Set<String>> groupStuMap = new HashMap<>();
        for (EmStudentGroup e : stus) {
            if (!groupStuMap.containsKey(e.getGroupId())) {
                groupStuMap.put(e.getGroupId(), new HashSet<String>());
            }
            groupStuMap.get(e.getGroupId()).add(e.getStudentId());
        }
        return groupStuMap;
    }

    @Override
    protected Class<EmStudentGroup> getEntityClass() {
        return EmStudentGroup.class;
    }

    @Override
    public void deleteByExamIdAndSchoolId(String examId, String schoolId) {
        emStudentGroupDao.deleteByExamIdAndSchoolId(examId, schoolId);
    }
}
