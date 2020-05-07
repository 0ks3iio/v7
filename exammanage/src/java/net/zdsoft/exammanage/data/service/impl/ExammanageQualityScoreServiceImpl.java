package net.zdsoft.exammanage.data.service.impl;


import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.ExammanageQualityScoreDao;
import net.zdsoft.exammanage.data.dto.EmQualityScoreDto;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.exammanage.data.entity.ExammanageQualityScore;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.exammanage.data.service.ExammanageQualityScoreService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("exammanageQualityScoreService")
public class ExammanageQualityScoreServiceImpl extends BaseServiceImpl<ExammanageQualityScore, String> implements ExammanageQualityScoreService {

    @Autowired
    private ExammanageQualityScoreDao exammanageQualityScoreDao;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;

    @Override
    public void batchSave(String acadyear, String semester, String examId, EmQualityScoreDto dto) {
        List<ExammanageQualityScore> list = dto.getDtos();
        Set<String> stuIds = EntityUtils.getSet(list, ExammanageQualityScore::getStudentId);
        exammanageQualityScoreDao.deleteByStuIds(examId, stuIds.toArray(new String[0]));
        for (ExammanageQualityScore e : list) {
            e.setAcadyear(acadyear);
            e.setSemester(semester);
            e.setExamId(examId);
        }
        checkSave(list.toArray(new ExammanageQualityScore[0]));
        saveAll(list.toArray(new ExammanageQualityScore[0]));
    }

    @Override
    public List<ExammanageQualityScore> findByList(String examId, String schoolId, String field, String keyWord, Pagination page) {
        List<EmEnrollStudent> enrollStudentList = emEnrollStudentService.findByIdsAndstate(examId, schoolId, null, "1", page);
        List<ExammanageQualityScore> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(enrollStudentList)) {
            Set<String> schoolIdSet = EntityUtils.getSet(enrollStudentList, EmEnrollStudent::getSchoolId);
            Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
            Set<String> studentIds = EntityUtils.getSet(enrollStudentList, EmEnrollStudent::getStudentId);
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentNameMap = EntityUtils.getMap(students, Student::getId);
            List<ExammanageQualityScore> scoreList = exammanageQualityScoreDao.findListByExamIdAndStudentIdsIn(examId, studentIds.toArray(new String[0]));
            Map<String, ExammanageQualityScore> map = EntityUtils.getMap(scoreList, ExammanageQualityScore::getStudentId);

            List<EmPlaceStudent> emStuList = emPlaceStudentService.findByExamIdStuIds(examId, studentIds.toArray(new String[0]));
            Map<String, String> examNumMap = EntityUtils.getMap(emStuList, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);

            ExammanageQualityScore score = null;
            for (EmEnrollStudent e : enrollStudentList) {
                score = new ExammanageQualityScore();
                score.setExamId(examId);
                score.setSchoolId(e.getSchoolId());
                score.setStudentId(e.getStudentId());
                score.setStudent(studentNameMap.get(e.getStudentId()));
                score.setSchoolName(schoolNameMap.get(e.getSchoolId()));
                if (map.containsKey(e.getStudentId())) {
                    score.setEvaluation(map.get(e.getStudentId()).getEvaluation());
                }
                if (examNumMap.containsKey(e.getStudentId())) {
                    score.setExamCode(examNumMap.get(e.getStudentId()));
                }
                if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(keyWord.trim())) {
                    String str = "";
                    if (StringUtils.equals("1", field)) {
                        str = score.getExamCode();
                    } else if (StringUtils.equals("2", field)) {
                        str = score.getStudent().getUnitiveCode();
                    } else if (StringUtils.equals("4", field)) {
                        str = score.getStudent().getStudentName();
                    } else {
                        str = score.getStudent().getIdentityCard();
                    }
                    if (StringUtils.equals(keyWord.trim(), str)) {
                        list.add(score);
                    }
                } else {
                    list.add(score);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            Collections.sort(list, new Comparator<ExammanageQualityScore>() {
                @Override
                public int compare(ExammanageQualityScore arg0, ExammanageQualityScore arg1) {
                    if (StringUtils.isBlank(arg0.getExamCode())) {
                        return -1;
                    } else if (StringUtils.isBlank(arg1.getExamCode())) {
                        return 1;
                    } else {
                        return arg0.getExamCode().compareTo(arg1.getExamCode());
                    }
                }
            });
        }


        return list;
    }

    private Map<String, String> getSchoolNameMap(Set<String> schoolIds) {
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, School::getId, School::getSchoolName);
        return schoolNameMap;
    }

    @Override
    protected BaseJpaRepositoryDao<ExammanageQualityScore, String> getJpaDao() {
        return exammanageQualityScoreDao;
    }

    @Override
    protected Class<ExammanageQualityScore> getEntityClass() {
        return ExammanageQualityScore.class;
    }

}
