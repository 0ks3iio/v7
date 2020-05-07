package net.zdsoft.exammanage.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.ExammanageSportsScoreDao;
import net.zdsoft.exammanage.data.dto.EmSportsScoreDto;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.exammanage.data.entity.ExammanageSportsScore;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.exammanage.data.service.ExammanageSportsScoreService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("exammanageSportsScoreService")
public class ExammanageSportsScoreServiceImpl extends BaseServiceImpl<ExammanageSportsScore, String> implements ExammanageSportsScoreService {

    @Autowired
    private ExammanageSportsScoreDao exammanageSportsScoreDao;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;

    @Override
    public List<ExammanageSportsScore> findByList(String examId,
                                                  String schoolId, String field, String keyWord, Pagination page) {
        List<EmEnrollStudent> enrollStudentList = Lists.newArrayList();
        List<ExammanageSportsScore> exammanageSportsScores = Lists.newArrayList();
        enrollStudentList = emEnrollStudentService.findByIdsAndstate(examId, schoolId,"", "1", page);
        if (CollectionUtils.isNotEmpty(enrollStudentList)) {
            Set<String> schoolIdSet = EntityUtils.getSet(enrollStudentList, "schoolId");
            Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
            Set<String> studentIds = EntityUtils.getSet(enrollStudentList, "studentId");
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentNameMap = EntityUtils.getMap(students, "id");
            Set<String> classIds = EntityUtils.getSet(students, "classId");
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzesNameMap = EntityUtils.getMap(clazzes, "id");

            List<ExammanageSportsScore> scoreList = exammanageSportsScoreDao.findListByExamIdAndStudentIdsIn(examId, studentIds.toArray(new String[0]));
            Map<String, ExammanageSportsScore> map = EntityUtils.getMap(scoreList, ExammanageSportsScore::getStudentId);

            List<EmPlaceStudent> emStuList = emPlaceStudentService.findByExamIdStuIds(examId, studentIds.toArray(new String[0]));
            Map<String, String> examNumMap = EntityUtils.getMap(emStuList, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);

            Iterator<EmEnrollStudent> it = enrollStudentList.iterator();
            while (it.hasNext()) {
                EmEnrollStudent emEnrollStudent = it.next();
                ExammanageSportsScore exammanageSportsScore = new ExammanageSportsScore();
                exammanageSportsScore.setSchoolName(schoolNameMap.get(emEnrollStudent.getSchoolId()));
                Student student = studentNameMap.get(emEnrollStudent.getStudentId());
                if (student != null) {
                    if (clazzesNameMap.get(student.getClassId()) == null) {
                        it.remove();
                        continue;
                    }
                    exammanageSportsScore.setCardNum(student.getIdentityCard());
                    if (MapUtils.isNotEmpty(map) && map.containsKey(student.getId())) {
                        ExammanageSportsScore old = map.get(student.getId());
                        exammanageSportsScore.setCourseScore(old.getCourseScore());//
                        exammanageSportsScore.setExamNum(old.getExamNum());
                        exammanageSportsScore.setExamScore(old.getExamScore());
                        exammanageSportsScore.setState(old.getState());
                    }
                    if(MapUtils.isNotEmpty(examNumMap)&&examNumMap.containsKey(student.getId())){
                    	exammanageSportsScore.setExamNum(examNumMap.get(student.getId()));
                    }
                    exammanageSportsScore.setExamId(examId);
                    exammanageSportsScore.setSchoolId(emEnrollStudent.getSchoolId());
                    exammanageSportsScore.setSex(student.getSex() + "");
                    exammanageSportsScore.setStudentId(student.getId());
                    exammanageSportsScore.setStuName(student.getStudentName());
                    exammanageSportsScore.setXueji(student.getUnitiveCode());

                    if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(keyWord.trim())) {
                        String str = "";
                        if (StringUtils.equals("1", field)) {
                            str = exammanageSportsScore.getExamNum();
                        } else if (StringUtils.equals("2", field)) {
                            str = exammanageSportsScore.getXueji();
                        }else if(StringUtils.equals("4", field)){
                            str = exammanageSportsScore.getStuName();
                        } else {
                            str = exammanageSportsScore.getCardNum();
                        }
                        if (StringUtils.equals(keyWord.trim(), str)) {
                            exammanageSportsScores.add(exammanageSportsScore);
                        }
                    } else {
                        exammanageSportsScores.add(exammanageSportsScore);
                    }
                }
            }

        }
        return exammanageSportsScores;
    }

    @Override
    public void batchSave(String acadyear, String semester, String examId,
                          EmSportsScoreDto dto) {
        List<ExammanageSportsScore> list = dto.getDtos();
        Set<String> stuIds = EntityUtils.getSet(list, ExammanageSportsScore::getStudentId);
        exammanageSportsScoreDao.deleteByStuIds(examId, stuIds.toArray(new String[0]));
        for (ExammanageSportsScore e : list) {
            e.setAcadyear(acadyear);
            e.setSemester(semester);
            e.setExamId(examId);
        }
        checkSave(list.toArray(new ExammanageSportsScore[0]));
        saveAll(list.toArray(new ExammanageSportsScore[0]));

    }

    private Map<String, String> getSchoolNameMap(Set<String> schoolIds) {
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, School::getId, School::getSchoolName);
        return schoolNameMap;
    }

    @Override
    protected BaseJpaRepositoryDao<ExammanageSportsScore, String> getJpaDao() {
        return exammanageSportsScoreDao;
    }

    @Override
    protected Class<ExammanageSportsScore> getEntityClass() {
        return ExammanageSportsScore.class;
    }


}
