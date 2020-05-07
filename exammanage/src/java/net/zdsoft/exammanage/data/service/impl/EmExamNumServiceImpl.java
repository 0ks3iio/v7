package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmExamNumDao;
import net.zdsoft.exammanage.data.entity.EmExamNum;
import net.zdsoft.exammanage.data.service.EmExamNumService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("emExamNumService")
public class EmExamNumServiceImpl extends BaseServiceImpl<EmExamNum, String>
        implements EmExamNumService {

    @Autowired
    private EmExamNumDao emExamNumDao;

    @Override
    public Map<String, String> findBySchoolIdAndExamId(String schoolId,
                                                       String examId) {
        List<EmExamNum> list = emExamNumDao.findBySchoolIdAndExamId(schoolId, examId);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<String, String>();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (EmExamNum item : list) {
            if (StringUtils.isNotBlank(item.getExamNumber())) {
                map.put(item.getStudentId(), item.getExamNumber());
            }
        }
        return map;
    }

    @Override
    public Map<String, String> findNumStuIdMap(String schoolId, String examId) {
        List<EmExamNum> list = emExamNumDao.findBySchoolIdAndExamId(schoolId, examId);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<String, String>();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (EmExamNum item : list) {
            if (StringUtils.isNotBlank(item.getExamNumber())) {
                map.put(item.getExamNumber(), item.getStudentId());
            }
        }
        return map;
    }

    @Override
    public List<EmExamNum> findunitIdAndExamId(String unitId, String examId) {
        return emExamNumDao.findBySchoolIdAndExamId(unitId, examId);
    }

    @Override
    protected BaseJpaRepositoryDao<EmExamNum, String> getJpaDao() {
        return emExamNumDao;
    }

    @Override
    protected Class<EmExamNum> getEntityClass() {
        return EmExamNum.class;
    }

    @Override
    public List<EmExamNum> saveAllEntitys(EmExamNum... exammanageExamNum) {
        return emExamNumDao.saveAll(checkSave(exammanageExamNum));
    }

    @Override
    public void addOrDel(List<EmExamNum> insertList, String examId,
                         Set<String> delStudentIds) {
        if (delStudentIds != null && delStudentIds.size() > 0) {
            String[] stuIds = delStudentIds.toArray(new String[]{});
            if (stuIds.length <= 1000) {
                emExamNumDao.deleteByExamIdAndStudentIdIn(examId, delStudentIds.toArray(new String[]{}));
            } else {
                int length = stuIds.length;
                int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
                for (int i = 0; i < cyc; i++) {
                    int max = (i + 1) * 1000;
                    if (max > length)
                        max = length;
                    String[] stuId = ArrayUtils.subarray(stuIds, i * 1000, max);
                    emExamNumDao.deleteByExamIdAndStudentIdIn(examId, stuId);
                }
            }

        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveAllEntitys(insertList.toArray(new EmExamNum[]{}));
        }

    }

    @Override
    public void insertAllAndDeleteAll(List<EmExamNum> insertList, String examId,
                                      String unitId) {
        emExamNumDao.deleteBySchoolIdAndExamId(unitId, examId);
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveAllEntitys(insertList.toArray(new EmExamNum[]{}));
        }
    }

    @Override
    public void deleteBy(String unitId, String examId) {
        emExamNumDao.deleteBySchoolIdAndExamId(unitId, examId);
    }

    @Override
    public Map<String, String> findByExamIdAndStudentIdIn(String examId,
                                                          String[] studentIds) {
        if (studentIds == null || studentIds.length <= 0) {
            return new HashMap<String, String>();
        }
        int length = studentIds.length;
        List<EmExamNum> list = new ArrayList<EmExamNum>();
        if (length <= 1000) {
            list = emExamNumDao.findByExamIdAndStudentIdIn(examId,
                    studentIds);
        } else {
            int cny = length / 1000 + (length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cny; i++) {
                int max = (i + 1) * 1000;
                if (max > length)
                    max = length;
                String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);

                List<EmExamNum> list1 = emExamNumDao.findByExamIdAndStudentIdIn(examId,
                        stuId);
                list.addAll(list1);
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, String> map = new HashMap<String, String>();
            for (EmExamNum examNum : list) {
                map.put(examNum.getStudentId(), examNum.getExamNumber());
            }
            return map;
        } else {
            return new HashMap<String, String>();
        }
    }

}
