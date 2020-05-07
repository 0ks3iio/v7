package net.zdsoft.qulity.data.action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.qulity.data.constant.QualityConstants;
import net.zdsoft.qulity.data.dto.StudentScoreDto;
import net.zdsoft.qulity.data.entity.QualityParam;
import net.zdsoft.qulity.data.entity.QualityScore;
import net.zdsoft.qulity.data.service.QualityScoreService;
import net.zdsoft.qulity.data.utils.QualityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author niuchao
 * @date 2019/10/14 10:20
 */

@Controller
@RequestMapping("/quality/ranking")
public class QualityRankingAction extends QualityCommonAction {

    @Autowired
    private QualityScoreService qualityScoreService;

    @RequestMapping("/index/page")
    @ControllerInfo("总排名首页")
    public String rankingIndex(ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId= loginInfo.getUnitId();
        String userId= loginInfo.getUserId();
        List<Grade> gradeList =getGradeList(userId);
        map.put("gradeList", gradeList);
        map.put("unitId", unitId);
        map.put("userId", userId);
        QualityScore qScore = qualityScoreService.findByUnitIdOne(unitId, QualityConstants.SCORE_TYPE_2);
        map.put("qualityScore", qScore);
        return "/quality/ranking/qualityRankingIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/statistics")
    public String scoreStatistics() {
        try{
            String unitId = getLoginInfo().getUnitId();
            QualityScore qualityScore = qualityScoreService.findByUnitIdOne(unitId, QualityConstants.SCORE_TYPE_1);
            if(qualityScore==null){
                return error("请先在综合素质汇总中进行统计");
            }
            List<QualityScore> qualityScoreList = qualityScoreService.findByUnitIdAndType(unitId, QualityConstants.SCORE_TYPE_1);
            Map<String, List<String>> gradeStudentMap = EntityUtils.getListMap(qualityScoreList, QualityScore::getGradeId, QualityScore::getStudentId);
            List<QualityParam> param = qualityParamService.findByUnitIdAndParamType(unitId, QualityConstants.QULITY_YYBS_MAX_NUMBER);
            int maxValue = 0;
            if(CollectionUtils.isNotEmpty(param)){
                maxValue = param.get(0).getParam();
            }
            Map<String, String> englishMap = comStatisticsRemoteService.findYybsByGradeMap(gradeStudentMap, maxValue);
            QualityScore score;
            List<QualityScore> scoreList = new ArrayList<>();
            for (QualityScore s : qualityScoreList) {
                score = new QualityScore();
                score.setId(UuidUtils.generateUuid());
                score.setUnitId(s.getUnitId());
                score.setGradeId(s.getGradeId());
                score.setClassId(s.getClassId());
                score.setStudentId(s.getStudentId());
                score.setCompreScore(s.getTotalScore());
                score.setCompreGradeRank(s.getGradeRank());
                if(englishMap.containsKey(s.getStudentId())){
                    score.setEnglishScore(QualityUtils.roundFloat(Float.parseFloat(englishMap.get(s.getStudentId())),2));
                }else{
                    score.setEnglishScore(0f);
                }
                score.setType(QualityConstants.SCORE_TYPE_2);
                score.setStatisticalTime(new Date());
                scoreList.add(score);
            }

            Map<String, List<QualityScore>> gradeScoreMap = scoreList.stream().collect(Collectors.groupingBy(QualityScore::getGradeId));
            scoreList.clear();
            for (List<QualityScore> item : gradeScoreMap.values()) {
                //英语成绩排名
                List<Map.Entry<Float, List<QualityScore>>> tempList = item.stream()
                        .collect(Collectors.groupingBy(QualityScore::getEnglishScore)).entrySet()
                        .stream().sorted((s1, s2) -> -Float.compare(s1.getKey(), s2.getKey())).collect(Collectors.toList());
                List<QualityScore> sortList = new ArrayList<>();
                int index = 1;
                for (Map.Entry<Float, List<QualityScore>> entry : tempList) {
                    final int i = index;
                    entry.getValue().forEach(e->{
                        e.setEnglishGradeRank(i);
                        e.setTotalScore(QualityUtils.roundFloat((float)((e.getCompreGradeRank()+i)*0.5),2));
                    });
                    sortList.addAll(entry.getValue());
                    index = index+entry.getValue().size();
                }
                //总分年级排名
                tempList = sortList.stream()
                        .collect(Collectors.groupingBy(QualityScore::getTotalScore)).entrySet()
                        .stream().sorted((s1, s2) -> -Float.compare(s2.getKey(), s1.getKey())).collect(Collectors.toList());
                sortList.clear();
                index = 1;
                for (Map.Entry<Float, List<QualityScore>> entry : tempList) {
                    final int i = index;
                    entry.getValue().forEach(e->e.setGradeRank(i));
                    sortList.addAll(entry.getValue());
                    index = index+entry.getValue().size();
                }
                //总分班级排名
                Map<String, List<QualityScore>> tempMap = sortList.stream().collect(Collectors.groupingBy(QualityScore::getClassId));
                for (List<QualityScore> item2 : tempMap.values()) {
                    tempList = item2.stream()
                            .collect(Collectors.groupingBy(QualityScore::getTotalScore)).entrySet()
                            .stream().sorted((s1, s2) -> -Float.compare(s2.getKey(), s1.getKey())).collect(Collectors.toList());
                    sortList.clear();
                    index = 1;
                    for (Map.Entry<Float, List<QualityScore>> entry : tempList) {
                        final int i = index;
                        entry.getValue().forEach(e->e.setClassRank(i));
                        scoreList.addAll(entry.getValue());
                        index = index+entry.getValue().size();
                    }
                }
            }
            qualityScoreService.saveAndDelete(unitId, QualityConstants.SCORE_TYPE_2, scoreList.toArray(new QualityScore[scoreList.size()]));
        } catch (Exception e) {
            e.printStackTrace();
            return error("统计失败");
        }
            return success("统计完成");
        }

    @RequestMapping("/list/page")
    @ControllerInfo("总排名列表")
    public String rankingList(String gradeId, String classId, ModelMap map, HttpServletRequest request) {
        Set<String> classIdSet=getClassIds(classId, gradeId);
        if(CollectionUtils.isEmpty(classIdSet)){
            return "/quality/ranking/qualityRankingList.ftl";
        }
        Pagination page = createPagination();
        List<StudentScoreDto> dtoList = getStudentScoreDtos(classIdSet, page);
        map.put("dtoList", dtoList);
        sendPagination(request, map, page);
        return "/quality/ranking/qualityRankingList.ftl";
    }

    private List<StudentScoreDto> getStudentScoreDtos(Set<String> classIdSet, Pagination page) {
        String unitId = getLoginInfo().getUnitId();
        String[] classIds = classIdSet.toArray(new String[classIdSet.size()]);
        List<Clazz>	clazzList = SUtils.dt(classRemoteService.findListByIds(classIds),Clazz.class);
        Map<String, String> classNameMap = EntityUtils.getMap(clazzList, Clazz::getId, Clazz::getClassNameDynamic);
        List<Student> studentList = Lists.newArrayList();
        Map<String, QualityScore> qualityScoreMap = Maps.newHashMap();
        QualityScore qScore = qualityScoreService.findByUnitIdOne(unitId, QualityConstants.SCORE_TYPE_2);
        if (qScore == null) {
            if(page==null){
                studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds), Student.class);
            }else{
                studentList = Student.dt(studentRemoteService.findByClassIds(classIds, SUtils.s(page)), page);
            }
        } else {
            List<QualityScore> qualityScoreList = qualityScoreService.findByClassIdsAndType(classIds, QualityConstants.SCORE_TYPE_2, page);
            if (CollectionUtils.isNotEmpty(qualityScoreList)) {
                qualityScoreMap = EntityUtils.getMap(qualityScoreList, QualityScore::getStudentId);
                List<String> stuIds = EntityUtils.getList(qualityScoreList, QualityScore::getStudentId);
                studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])),
                        new TR<List<Student>>() {});
            }
        }

        List<StudentScoreDto> dtoList = Lists.newArrayList();
        StudentScoreDto scoreDto;
        for (Student student : studentList) {
            scoreDto = new StudentScoreDto();
            scoreDto.setStudentId(student.getId());
            scoreDto.setStudentCode(student.getStudentCode());
            scoreDto.setStudentName(student.getStudentName());
            if (classNameMap.containsKey(student.getClassId())) {
                scoreDto.setClassName(classNameMap.get(student.getClassId()));
            }
            if (qualityScoreMap.containsKey(student.getId())) {
                QualityScore qualityScore = qualityScoreMap.get(student.getId());
                scoreDto.setTotalScore(qualityScore.getTotalScore());
                scoreDto.setGradeRank(qualityScore.getGradeRank());
                scoreDto.setClassRank(qualityScore.getClassRank());
                scoreDto.setEnglishScore(qualityScore.getEnglishScore());
                scoreDto.setEnglishGradeRank(qualityScore.getEnglishGradeRank());
                scoreDto.setCompreScore(qualityScore.getCompreScore());
                scoreDto.setCompreGradeRank(qualityScore.getCompreGradeRank());
            }
            dtoList.add(scoreDto);
        }

        if(dtoList.size()>0){
            dtoList =  dtoList.stream().sorted((a,b)->a.getGradeRank()-b.getGradeRank()).collect(Collectors.toList());
        }
        return dtoList;
    }


    @ResponseBody
    @RequestMapping("/list/export")
    @ControllerInfo("总排名表导出")
    public String exportList(ModelMap map, HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, List<String>> fieldTitleMap = new HashMap<>();
            List<String> tis = new ArrayList<>();
            tis.add("序号");
            tis.add("姓名");
            tis.add("学号");
            tis.add("班级");
            tis.add("综合素质总分");
            tis.add("综合素质总分年级排名");
            tis.add("英语笔试总折分");
            tis.add("英语笔试总折分年级排名");
            tis.add("总排名分数");
            tis.add("总排名分年级排名");
            tis.add("总排名分班级排名");
            fieldTitleMap.put("综合素质总排名表", tis);

            String gradeId = request.getParameter("gradeId");
            String classId = request.getParameter("classId");
            Set<String> classIdSet = getClassIds(classId, gradeId);

            Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String,List<Map<String,String>>>();
            List<Map<String, String>> datas = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(classIdSet)) {
                List<StudentScoreDto> dtoList = getStudentScoreDtos(classIdSet, null);
                int i = 1;
                for (StudentScoreDto dto : dtoList) {
                    Map<String, String> conMap = new HashMap<>();
                    conMap.put("序号", "" + i++);
                    conMap.put("姓名", dto.getStudentName());
                    conMap.put("学号", dto.getStudentCode());
                    conMap.put("班级", dto.getClassName());

                    conMap.put("综合素质总分", dto.getCompreScore()+"");
                    conMap.put("综合素质总分年级排名", dto.getCompreGradeRank()+"");
                    conMap.put("英语笔试总折分", dto.getEnglishScore()+"");
                    conMap.put("英语笔试总折分年级排名", dto.getEnglishGradeRank()+"");
                    conMap.put("总排名分数", dto.getTotalScore()+"");
                    conMap.put("总排名分年级排名", dto.getGradeRank()+"");
                    conMap.put("总排名分班级排名", dto.getClassRank()+"");
                    datas.add(conMap);
                }
            }
            sheetName2RecordListMap.put("综合素质总排名表", datas);

            ExportUtils exportUtils = ExportUtils.newInstance();
            exportUtils.exportXLSFile("综合素质总排名表", fieldTitleMap, sheetName2RecordListMap, response);
        } catch (Exception e) {
            e.printStackTrace();
            return error("导出失败");
        }
        return success("导出完成");
    }


}
