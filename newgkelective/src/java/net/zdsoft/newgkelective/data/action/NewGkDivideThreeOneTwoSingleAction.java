package net.zdsoft.newgkelective.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dto.*;
import net.zdsoft.newgkelective.data.entity.*;
import net.zdsoft.newgkelective.data.service.NewGKStudentRangeExService;
import net.zdsoft.newgkelective.data.service.NewGkDivideStusubService;
import net.zdsoft.newgkelective.data.utils.CombineAlgorithmInt;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/newgkelective/{divideId}/divideClass/singleRecomb")
public class NewGkDivideThreeOneTwoSingleAction extends NewGkElectiveDivideCommonAction {

    public static final String THREE_COMBINE = "3";
    public static final String ONE_COMBINE = "1";

    public static final List<String> titleList = new ArrayList<>();

    static {
        titleList.add("学号");
        titleList.add("姓名");
        titleList.add("性别");
        titleList.add("原行政班");
        titleList.add("选课科目");
        titleList.add("总成绩");
    }

    @Autowired
    private NewGKStudentRangeExService newGKStudentRangeExService;
    @Autowired
    private NewGkDivideStusubService newGkDivideStusubService;

    @RequestMapping("/exportTeachClass")
    public void exportTeachClass(@PathVariable String divideId, String subjectType, HttpServletRequest request, HttpServletResponse response) {
        NewGkDivide divide = newGkDivideService.findById(divideId);
        if (divide == null) {
            return;
        }
        List<NewGkDivideClass> allClassList = newGkDivideClassService.
                findByDivideIdAndClassTypeSubjectType(divide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_1, NewGkElectiveConstant.CLASS_TYPE_2, NewGkElectiveConstant.CLASS_TYPE_3, NewGkElectiveConstant.CLASS_TYPE_4},
                        true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, null);

        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(divide.getUnitId()), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Map<String, Course> courseMap = courseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        // 获取选课数据
        Map<String,Set<String>> subjectChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);

        List<NewGkDivideClass> showTeachClass = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType()) && Objects.equals(subjectType, e.getBestType())).collect(Collectors.toList());
        List<String> allStudentIdList = showTeachClass.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toList());
        List<Student> allStudentList = SUtils.dt(studentRemoteService.findListByIds(allStudentIdList.toArray(new String[0])), Student.class);

        List<StudentResultDto> studentResultDtoList = makeStudentDto(divide, courseList, allStudentList, allClassList, courseMap, studentChosenMap);
        Map<String, StudentResultDto> stringStudentResultDtoMap = EntityUtils.getMap(studentResultDtoList, StudentResultDto::getStudentId);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 10);
        headfont.setBold(true);
        style.setFont(headfont);
        HSSFSheet sheet;

        if (CollectionUtils.isNotEmpty(showTeachClass)) {
            for (NewGkDivideClass classTmp : showTeachClass) {
                sheet = workbook.createSheet(classTmp.getClassName());
                HSSFRow titleRow = sheet.createRow(0);
                for (int index = 0; index < titleList.size(); index++) {
                    HSSFCell cellTmp = titleRow.createCell(index);
                    cellTmp.setCellValue(new HSSFRichTextString(titleList.get(index)));
                }
                List<String> studentIdListTmp = classTmp.getStudentList();
                for (int index = 1; index <= studentIdListTmp.size(); index++) {
                    StudentResultDto tmp = stringStudentResultDtoMap.get(studentIdListTmp.get(index - 1));
                    HSSFRow row = sheet.createRow(index);
                    row.createCell(0).setCellValue(tmp.getStudentCode());
                    row.createCell(1).setCellValue(tmp.getStudentName());
                    row.createCell(2).setCellValue(tmp.getSex());
                    row.createCell(3).setCellValue(tmp.getClassName());
                    row.createCell(4).setCellValue(tmp.getChooseSubjects());
                    row.createCell(5).setCellValue(tmp.getSubjectScore().get("TOTAL"));
                }
            }
        }
        String fileName = "教学班详情";
        ExportUtils.outputData(workbook, fileName, response);
    }

    @RequestMapping("/rangeSet")
    public String showDivideRangeIndex(@PathVariable String divideId, String subjectId,  ModelMap map) {
        NewGkDivide newDivide = newGkDivideService.findOne(divideId);
        List<Course> allCourseList = newGkChoRelationService.findChooseSubject(newDivide.getChoiceId(), newDivide.getUnitId());
        List<Course> courseList =new ArrayList<>();
        //开设科目
      	List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(newDivide.getId(), new String[] {NewGkElectiveConstant.SUBJECT_TYPE_A});
      	Set<String> subIds = EntityUtils.getSet(openSubList, e->e.getSubjectId());
        boolean isConWL=true;
      	if(NewGkElectiveConstant.DIVIDE_TYPE_11.equals(newDivide.getOpenType())) {
      		isConWL=false;
        }
        for(Course c:allCourseList) {
    		if(NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(c.getSubjectCode())) {
    			if(isConWL) {
    				courseList.add(c);
    			}
    			continue;
    		}
    		if(NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(c.getSubjectCode())) {
    			if(isConWL) {
    				courseList.add(c);
    			}
    			continue;
    		}
    		if(subIds.contains(c.getId())) {
    			courseList.add(c);
    		}
    	}
        Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getSubjectName);

        map.put("courseMap", courseMap);
        map.put("divideId", newDivide.getId());
        map.put("gradeId", newDivide.getGradeId());
        map.put("subjectId", subjectId);
        map.put("openType", newDivide.getOpenType());
        return "/newgkelective/singleRecomb/rankStuRangeIndex.ftl";
    }

    @RequestMapping("/subjectChoiceInfo")
    public String changeSubjectTab(@PathVariable("divideId") String divideId, String subjectId, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findById(divideId);

        List<String> choiceList = newGkChoResultService.findByChoiceIdAndSubjectIdAndKindType(divide.getUnitId(), NewGkElectiveConstant.KIND_TYPE_01, divide.getChoiceId(), subjectId);
        Set<String> studentIds = new HashSet<>(choiceList);

        // 取出已安排人数
        List<NewGKStudentRange> stuRangeList = newGKStudentRangeService.findByDivideIdSubjectIdAndSubjectType(divideId, subjectId,
                NewGkElectiveConstant.SUBJECT_TYPE_A, null);
        Set<String> rangeStuIds = EntityUtils.getSet(stuRangeList, NewGKStudentRange::getStudentId);

        studentIds.removeAll(rangeStuIds);

        return makeStuResultDto(subjectId, map, divide, studentIds.toArray(new String[0]));
    }

    /**
     * 展示 学生分层信息
     * @return
     */
    @RequestMapping("/showRangeStus")
    public String showRangeInfo(@PathVariable("divideId") String divideId, String subjectId, String range, ModelMap map) {
        // LoginInfo linfo = getLoginInfo();
        NewGkDivide divide = newGkDivideService.findOne(divideId);

        List<NewGKStudentRange> allStuRangeList = newGKStudentRangeService
                .findByDivideIdSubjectIdAndSubjectType(divideId, subjectId, NewGkElectiveConstant.SUBJECT_TYPE_A, range);
        List<String> stuIdList = allStuRangeList.stream()
                .map(e -> e.getStudentId()).collect(Collectors.toList());

        Map<String, Integer> rangeMap = newGKStudentRangeService.findStuRangeCount(divide.getUnitId(), divideId, subjectId, NewGkElectiveConstant.SUBJECT_TYPE_A);

        map.put("rangeMap", rangeMap);
        map.put("right", 1);
        map.put("range", range);

        return makeStuResultDto(subjectId, map, divide, stuIdList.toArray(new String[0]));
    }

    @RequestMapping("/subjectRangeInfo")
    public String showSubjectRangeInfo(@PathVariable String divideId, String subjectType, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        NewGkDivide divide = newGkDivideService.findById(divideId);
        map.put("divide", divide);
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Map<String, String> subjectCodeToIdMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getId);
        map.put("subjectCodeMap", subjectCodeToIdMap);
        Map<String, String> courseNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
        List<NewGKStudentRange> newGKStudentRangeList = newGKStudentRangeService.findByDivideId(unitId, divideId);
        List<NewGKStudentRangeEx> newGKStudentRangeExList = newGKStudentRangeService.findExListByDivideIdAndSubjectType(divideId, subjectType);
        Map<String, NewGKStudentRangeEx> subjectIdAndRangeToStudentRangeExMap = EntityUtils.getMap(newGKStudentRangeExList, e -> e.getSubjectId() + "_" + e.getRange());
        Map<String, Set<String>> subjectRangeMap = new HashMap<>();
        for (Course one : courseList) {
            subjectRangeMap.put(one.getId(), new TreeSet<>());
        }
        Map<String, Integer> subjectIdToStudentCount = new HashMap<>();
        for (NewGKStudentRange one : newGKStudentRangeList) {
            // 科目总人数
            Integer count = subjectIdToStudentCount.get(one.getSubjectId());
            if (count == null) {
                count = new Integer(0);
            }
            subjectIdToStudentCount.put(one.getSubjectId(), count++);
            // 科目各层人数
            count = subjectIdToStudentCount.get(one.getSubjectId() + "_" + one.getRange());
            if (count == null) {
                count = new Integer(0);
            }
            subjectIdToStudentCount.put(one.getSubjectId() + "_" + one.getRange(), count++);

            subjectRangeMap.get(one.getSubjectId()).add(one.getRange());
        }
        Map<String, NewGKStudentRangeDto> subjectIdToStudentRangeDtoMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> one : subjectRangeMap.entrySet()) {
            NewGKStudentRangeDto tmp = new NewGKStudentRangeDto();
            tmp.setSubjectId(one.getKey());
            tmp.setSubjectName(courseNameMap.get(one.getKey()));
            tmp.setSubjectType(subjectType);
            tmp.setStuNum(subjectIdToStudentCount.get(one.getKey()) == null ? Integer.valueOf(0) : subjectIdToStudentCount.get(one.getKey()));
            List<NewGKStudentRangeEx> innerList = new ArrayList<>(4);
            for (String range : one.getValue()) {
                NewGKStudentRangeEx subTmp = subjectIdAndRangeToStudentRangeExMap.get(one.getKey() + "_" + range);
                if (subTmp == null) {
                    subTmp = new NewGKStudentRangeEx();
                    // subTmp.setId(UuidUtils.generateUuid());
                    subTmp.setSubjectType(subjectType);
                    subTmp.setRange(range);
                    subTmp.setSubjectId(one.getKey());
                    subTmp.setDivideId(divideId);
                    subTmp.setStuNum(subjectIdToStudentCount.get(one.getKey() + "_" + range) == null ? Integer.valueOf(0) : subjectIdToStudentCount.get(one.getKey() + "_" + range));
                }
                innerList.add(subTmp);
            }
            tmp.setExList(innerList);
            subjectIdToStudentRangeDtoMap.put(one.getKey(), tmp);
        }
        map.put("resultDtoMap", subjectIdToStudentRangeDtoMap);
        return "/newgkelective/singleRecomb/singleArrangeList.ftl";
    }

    @RequestMapping("/saveStudentRangeEx")
    @ResponseBody
    public String saveStudentRangeExInfo(@PathVariable String divideId, List<NewGKStudentRangeDto> newGKStudentRangeDtoList) {
        List<NewGKStudentRangeEx> saveList = new ArrayList<>(16);
        for (NewGKStudentRangeDto one : newGKStudentRangeDtoList) {
            if (CollectionUtils.isNotEmpty(one.getExList())) {
                for (NewGKStudentRangeEx sub : one.getExList()) {
                    if (StringUtils.isBlank(sub.getId())) {
                        sub.setId(UuidUtils.generateUuid());
                        sub.setModifyTime(new Date());
                    }
                    saveList.add(sub);
                }
            }
        }
        try {
            newGKStudentRangeExService.saveAll(saveList.toArray(new NewGKStudentRangeEx[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return error("保存失败");
        }
        return success("保存成功");
    }

    @RequestMapping("/showSingleDivideClassResult")
    public String showDivideClassResult(@PathVariable String divideId, String subjectType, String isCombine, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(unitId), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Map<String, String> subjectNameMap = EntityUtils.getMap(courseList, Course::getId, Course::getSubjectName);
        map.put("subjectNameMap", subjectNameMap);
        Map<String, String> subjectCodeMap = EntityUtils.getMap(courseList, Course::getSubjectCode, Course::getId);
        map.put("subjectCodeMap", subjectCodeMap);

        List<NewGKStudentRange> newGKStudentRangeList = newGKStudentRangeService.findByDivideId(unitId, divideId);
        List<NewGKStudentRangeEx> newGKStudentRangeExList = newGKStudentRangeService.findExListByDivideIdAndSubjectType(divideId, subjectType);
        Map<String, NewGKStudentRangeEx> subjectIdAndRangeToStudentRangeExMap = EntityUtils.getMap(newGKStudentRangeExList, e -> e.getSubjectId() + "_" + e.getRange());
        Map<String, Set<String>> subjectRangeMap = new HashMap<>();
        for (Course one : courseList) {
            subjectRangeMap.put(one.getId(), new TreeSet<>());
        }
        Map<String, Integer> subjectIdToStudentCount = new HashMap<>();
        for (NewGKStudentRange one : newGKStudentRangeList) {
            // 科目总人数
            Integer count = subjectIdToStudentCount.get(one.getSubjectId());
            if (count == null) {
                count = new Integer(0);
            }
            subjectIdToStudentCount.put(one.getSubjectId(), count++);
            // 科目各层人数
            count = subjectIdToStudentCount.get(one.getSubjectId() + "_" + one.getRange());
            if (count == null) {
                count = new Integer(0);
            }
            subjectIdToStudentCount.put(one.getSubjectId() + "_" + one.getRange(), count++);

            subjectRangeMap.get(one.getSubjectId()).add(one.getRange());
        }
        Map<String, NewGKStudentRangeDto> subjectIdToStudentRangeDtoMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> one : subjectRangeMap.entrySet()) {
            NewGKStudentRangeDto tmp = new NewGKStudentRangeDto();
            tmp.setSubjectId(one.getKey());
            tmp.setSubjectName(subjectNameMap.get(one.getKey()));
            tmp.setSubjectType(subjectType);
            tmp.setStuNum(subjectIdToStudentCount.get(one.getKey()) == null ? Integer.valueOf(0) : subjectIdToStudentCount.get(one.getKey()));
            List<NewGKStudentRangeEx> innerList = new ArrayList<>(4);
            for (String range : one.getValue()) {
                NewGKStudentRangeEx subTmp = subjectIdAndRangeToStudentRangeExMap.get(one.getKey() + "_" + range);
                if (subTmp == null) {
                    subTmp = new NewGKStudentRangeEx();
                    // subTmp.setId(UuidUtils.generateUuid());
                    subTmp.setSubjectType(subjectType);
                    subTmp.setRange(range);
                    subTmp.setSubjectId(one.getKey());
                    subTmp.setDivideId(divideId);
                    subTmp.setStuNum(subjectIdToStudentCount.get(one.getKey() + "_" + range) == null ? Integer.valueOf(0) : subjectIdToStudentCount.get(one.getKey() + "_" + range));
                }
                innerList.add(subTmp);
            }
            tmp.setExList(innerList);
            subjectIdToStudentRangeDtoMap.put(one.getKey(), tmp);
        }
        map.put("resultDtoMap", subjectIdToStudentRangeDtoMap);

        List<NewGkDivideClass> teachClassList = allClassList.stream().filter(e -> NewGkElectiveConstant.CLASS_TYPE_2.equals(e.getClassType())).collect(Collectors.toList());
        Map<String, List<NewGkDivideClass>> rangeAndBatchMap = new HashMap<>();
        for (NewGkDivideClass one : teachClassList) {
            String key = one.getSubjectIds() + "_" + one.getBestType() + "_" + one.getBatch();
            if (rangeAndBatchMap.get(key) == null) {
                rangeAndBatchMap.put(key, new ArrayList<>());
            }
            rangeAndBatchMap.get(key).add(one);
        }
        map.put("rangeAndBatchMap", rangeAndBatchMap);
        return "/newgkelective/singleRecomb/singleTeachClassList.ftl";
    }

    @RequestMapping("/quickStratify")
    public String quickStratify(@PathVariable String divideId, String subjectId, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findById(divideId);
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        map.put("divideId", divideId);
        map.put("course", course);
        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);
        map.put("count", subjectChosenMap.get(subjectId).size());
        return "/newgkelective/singleRecomb/quickStratify.ftl";
    }

    @RequestMapping("/quickStratifyRangeEdge")
    @ResponseBody
    public String quickStratifyRangeEdge(@PathVariable String divideId, String subjectId, String edge) {
        NewGkDivide divide = newGkDivideService.findById(divideId);

        // 返回该科目下所有学生已排序排名，缓存5分钟
        List<StudentResultDto> dtoList = RedisUtils.getObject("DIVIDE_RANGE_EDGE_" + divideId + "_" + subjectId, RedisUtils.TIME_FIVE_MINUTES, new TypeReference<List<StudentResultDto>>() {
        }, new RedisInterface<List<StudentResultDto>>() {
            @Override
            public List<StudentResultDto> queryData() {
                // 获取选课数据
                Map<String, Set<String>> subjectChosenMap = new HashMap<>();
                Map<String, Set<String>> studentChosenMap = new HashMap<>();
                makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);
                Set<String> studentIdSet = subjectChosenMap.get(subjectId);

                Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);
                List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(divide.getUnitId()), Course.class);
                List<StudentResultDto> dtoList = new ArrayList<>();
                StudentResultDto tmp;
                for (String studentId : studentIdSet) {
                    tmp = new StudentResultDto();
                    tmp.setStudentId(studentId);
                    Map<String, Float> scoreMap = stuScoreMap.get(studentId);
                    Float scoreTmp;
                    Float totalScore = Float.valueOf(0.0f);
                    if (scoreMap == null) {
                        scoreMap = new HashMap<>();
                    }
                    for (Course one : ysyCourses) {
                        scoreTmp = scoreMap.get(one.getId());
                        totalScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
                    }
                    totalScore += scoreMap.get(subjectId) == null ? Float.valueOf(0.0f) : scoreMap.get(subjectId);
                    tmp.getSubjectScore().put("TOTAL", totalScore);
                    dtoList.add(tmp);
                }
                dtoList.sort(new Comparator<StudentResultDto>() {
                    @Override
                    public int compare(StudentResultDto o1, StudentResultDto o2) {
                        float tmp = o1.getSubjectScore().get("TOTAL") - o2.getSubjectScore().get("TOTAL");
                        if (tmp == 0f) {
                            return 0;
                        } else if (tmp < 0f) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                return dtoList;
            }
        });

        Integer edgeInt = Integer.valueOf(edge);
        Integer size = dtoList.size();
        if (edgeInt.equals(size)) {
            return successByValue(String.valueOf(edgeInt));
        }

        StudentResultDto edgeDto = dtoList.get(edgeInt - 1);
        while (edgeInt - 1 < size) {
            if (!edgeDto.getSubjectScore().get("TOTAL").equals(dtoList.get(edgeInt).getSubjectScore().get("TOTAL"))) {
                return successByValue(String.valueOf(edgeInt));
            }
            edgeInt += 1;
        }
        return successByValue(String.valueOf(edgeInt - 1));
    }

    @RequestMapping("/saveQuickStratify")
    @ResponseBody
    public String quickStratifySave(@PathVariable String divideId, String subjectId, String range, String sameLevel, NewGKStudentRangeExDto saveDto) {
        NewGkDivide divide = newGkDivideService.findById(divideId);

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);
        Set<String> studentIdSet = subjectChosenMap.get(subjectId);

        Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);
        List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(divide.getUnitId()), Course.class);
        List<StudentResultDto> dtoList = new ArrayList<>();
        StudentResultDto tmp;
        for (String studentId : studentIdSet) {
            tmp = new StudentResultDto();
            tmp.setStudentId(studentId);
            Map<String, Float> scoreMap = stuScoreMap.get(studentId);
            Float scoreTmp;
            Float totalScore = Float.valueOf(0.0f);
            if (scoreMap == null) {
                scoreMap = new HashMap<>();
            }
            for (Course one : ysyCourses) {
                scoreTmp = scoreMap.get(one.getId());
                totalScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
            }
            totalScore += scoreMap.get(subjectId) == null ? Float.valueOf(0.0f) : scoreMap.get(subjectId);
            tmp.getSubjectScore().put("TOTAL", totalScore);
            dtoList.add(tmp);
        }
        dtoList.sort(new Comparator<StudentResultDto>() {
            @Override
            public int compare(StudentResultDto o1, StudentResultDto o2) {
                float tmp = o1.getSubjectScore().get("TOTAL") - o2.getSubjectScore().get("TOTAL");
                if (tmp == 0f) {
                    return 0;
                } else if (tmp < 0f) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        Integer rangeInt = Integer.valueOf(range);
        List<NewGKStudentRangeEx> rangeList = saveDto.getExList();

        List<NewGKStudentRange> stuRangeList = new ArrayList<>();
        NewGKStudentRange stuRange;
        int index = 1;
        Map<Integer, Float> rangeEdgeScore = new HashMap<>();
        for (StudentResultDto one : dtoList) {
            stuRange = new NewGKStudentRange();
            stuRange.setId(UuidUtils.generateUuid());
            stuRange.setDivideId(divideId);
            stuRange.setSubjectId(subjectId);
            stuRange.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
            if (BaseConstants.ONE_STR.equals(sameLevel)) {
                for (int i = 0; i < rangeInt; i++) {
                    // 如果早最后一层则不用考虑该层下限
                    if (i == rangeInt - 1) {
                        stuRange.setRange(rangeList.get(i).getRange());
                    }
                    if (index < rangeList.get(i).getStuNum()) {
                        stuRange.setRange(rangeList.get(i).getRange());
                        break;
                    } else if (index == rangeList.get(i).getStuNum()) {
                        stuRange.setRange(rangeList.get(i).getRange());
                        rangeEdgeScore.put(i, one.getSubjectScore().get("TOTAL"));
                        break;
                    } else {
                        // 若是同分则归为同一层
                        if (rangeEdgeScore.get(i) != null && rangeEdgeScore.get(i).equals(one.getSubjectScore().get("TOTAL"))) {
                            stuRange.setRange(rangeList.get(i).getRange());
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < rangeInt; i++) {
                    if (i == rangeInt - 1) {
                        stuRange.setRange(rangeList.get(i).getRange());
                    } else if (index <= rangeList.get(i).getStuNum()) {
                        stuRange.setRange(rangeList.get(i).getRange());
                        break;
                    }
                }
            }
            stuRange.setStudentId(one.getStudentId());
            stuRange.setModifyTime(new Date());
            stuRange.setUnitId(divide.getUnitId());
            index++;
            stuRangeList.add(stuRange);
        }

        try {
            newGKStudentRangeService.updateStudentRange(divide.getUnitId(), divideId, subjectId, NewGkElectiveConstant.SUBJECT_TYPE_A, null, stuRangeList);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("");
    }

    @RequestMapping("/intelliStratify")
    public String intelliStratify(@PathVariable String divideId, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findById(divideId);
        
        List<Course> courseList = newGkChoRelationService.findChooseSubject(divide.getChoiceId(), divide.getUnitId());
        //开设科目
        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divide.getId(), new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        if (NewGkElectiveConstant.DIVIDE_TYPE_11.equals(divide.getOpenType())) {
            //去除物理历史
            courseList = courseList.stream().filter(e -> !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode()) && openSubjectIds.contains(e.getId())).collect(Collectors.toList());
        } else {
            courseList = courseList.stream().filter(e -> NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) || NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode()) || openSubjectIds.contains(e.getId())).collect(Collectors.toList());
        }

        map.put("divideId", divideId);

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);

        courseList.stream().forEach(e -> {
            // 暂用此属性存总人数
            e.setOrderId(subjectChosenMap.get(e.getId()).size());
        });
        map.put("courseList", courseList);
        return "/newgkelective/singleRecomb/intelliStratify.ftl";
    }

    @RequestMapping("/saveIntelliStratify")
    @ResponseBody
    public String intelliStratifySave(@PathVariable String divideId, NewGKStudentRangeExDto saveDto) {
        NewGkDivide divide = newGkDivideService.findById(divideId);

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);

        Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);
        List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(divide.getUnitId()), Course.class);
        List<Course> otherCourses = SUtils.dt(courseRemoteService.findByCodes73(divide.getUnitId()), Course.class);
        Map<String, StudentResultDto> dtoMap = new HashMap<>();
        StudentResultDto tmp;
        for (String studentId : studentChosenMap.keySet()) {
            tmp = new StudentResultDto();
            tmp.setStudentId(studentId);
            Map<String, Float> scoreMap = stuScoreMap.get(studentId);
            Float scoreTmp;
            Float ysyScore = Float.valueOf(0.0f);
            Float totalScore = Float.valueOf(0.0f);
            if (scoreMap == null) {
                scoreMap = new HashMap<>();
            }
            for (Course course : ysyCourses) {
                scoreTmp = scoreMap.get(course.getId());
                ysyScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
            }
            totalScore += ysyScore;
            tmp.getSubjectScore().put("YSY", ysyScore);
            for (Course course : otherCourses) {
                tmp.getSubjectScore().put(course.getId(), scoreMap.get(course.getId()) == null ? 0.0f : scoreMap.get(course.getId()));
                totalScore += scoreMap.get(course.getId()) == null ? Float.valueOf(0.0f) : scoreMap.get(course.getId());
            }
            tmp.getSubjectScore().put("TOTAL", totalScore);
            dtoMap.put(studentId, tmp);
        }

        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(), divide.getGradeId()), Clazz.class);
        int classNum = clazzList.size();
        int studentNum = studentChosenMap.keySet().size();
        // 每个班人数规模以此为准
        int studentNumPerClass = studentNum / classNum;

        List<NewGKStudentRange> stuRangeList = new ArrayList<>();
        NewGKStudentRange stuRange;
        for (NewGKStudentRangeEx one : saveDto.getExList()) {
            int aLevel = one.getClassNum() * studentNumPerClass;
            List<StudentResultDto> dtoList = new ArrayList<>();
            for (String studentId : subjectChosenMap.get(one.getSubjectId())) {
                dtoList.add(dtoMap.get(studentId));
            }
            dtoList.sort(new Comparator<StudentResultDto>() {
                @Override
                public int compare(StudentResultDto o1, StudentResultDto o2) {
                	float ss = o1.getSubjectScore().get("YSY") + o1.getSubjectScore().get(one.getSubjectId()) - o2.getSubjectScore().get("YSY") - o2.getSubjectScore().get(one.getSubjectId());
                    if(ss==0) {
                    	return  0;
                    }else if(ss>0) {
                    	return -1;
                    }else {
                    	return 1;
                    }
                }
            });
            int index = 1;
            for (StudentResultDto sub : dtoList) {
                stuRange = new NewGKStudentRange();
                stuRange.setId(UuidUtils.generateUuid());
                stuRange.setDivideId(divideId);
                stuRange.setSubjectId(one.getSubjectId());
                stuRange.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
                if (index <= aLevel) {
                    stuRange.setRange("A");
                } else {
                    stuRange.setRange("B");
                }
                stuRange.setStudentId(sub.getStudentId());
                stuRange.setModifyTime(new Date());
                stuRange.setUnitId(divide.getUnitId());
                index++;
                stuRangeList.add(stuRange);
            }
        }

        try {
            newGKStudentRangeService.updateStudentRange(divide.getUnitId(), divideId, null, NewGkElectiveConstant.SUBJECT_TYPE_A, null, stuRangeList);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("");
    }

    private String makeStuResultDto(String subjectId, ModelMap map, NewGkDivide divide, String[] studentIds) {
        List<Student> gradeStuList = SUtils.dt(studentRemoteService.findPartStudByGradeId(divide.getUnitId(), divide.getGradeId(), null, null), Student.class);
        Map<String, Student> stuMap = EntityUtils.getMap(gradeStuList, Student::getId);
        List<Student> studentList = Arrays.stream(studentIds)
                .distinct()
                .filter(e -> stuMap.containsKey(e))
                .map(e -> stuMap.get(e)).collect(Collectors.toList());

        int manCount = (int) studentList.stream().filter(e -> e.getSex().equals(1)).count();
        int woManCount = (int) studentList.stream().filter(e -> e.getSex().equals(2)).count();

        // 成绩
        String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
        Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId),
                new TypeReference<Map<String, McodeDetail>>() {
                });
        Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);


        List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()), Course.class);
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
        List<StudentResultDto> dtoList = new ArrayList<>();
        StudentResultDto dto;
        float courseAvg = (float) 0.0;
        float ysyAvg = (float) 0.0;
        float totalAvg = (float) 0.0;
        for (Student stu : studentList) {
            dto = new StudentResultDto();
            dto.setStudentId(stu.getId());
            dto.setStudentName(stu.getStudentName());
            dto.setSex(codeMap.get(stu.getSex() + "").getMcodeContent());

            Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
            Float scoreTmp;
            float ysyScore = (float) 0.0;
            float allScore = (float) 0.0;
            if (scoreMap == null) {
                scoreTmp = (float) 0.0;
            } else {
                scoreTmp = scoreMap.get(subjectId);
                if (scoreTmp == null) {
                    scoreTmp = (float) 0.0;
                }
                for (Course courseTmp : ysyCourses) {
                    if (scoreMap.containsKey(courseTmp.getId())) {
                        Float tmp = scoreMap.get(courseTmp.getId());
                        if (tmp == null) {
                            tmp = (float) 0.0;
                        }
                        ysyScore += tmp;
                        allScore += tmp;
                    }
                }

                Float subScore = scoreMap.get(subjectId);
                if (subScore == null) {
                    subScore = (float) 0.0;
                }
                allScore += subScore;
            }

            courseAvg += scoreTmp;
            ysyAvg += ysyScore;
            totalAvg += allScore;

            dto.getSubjectScore().put(subjectId, scoreTmp);
            dto.getSubjectScore().put("YSY", ysyScore);
            dto.getSubjectScore().put("TOTAL", allScore);

            dtoList.add(dto);
        }

        int size = dtoList.size();
        if (size == 0) {
            size = 1;
        }

        map.put("courseName", course.getSubjectName());
        map.put("subjectId", subjectId);
        map.put("dtoList", dtoList);
        map.put("manCount", manCount);
        map.put("woManCount", woManCount);
        map.put("courseAvg", courseAvg / size);
        map.put("ysyAvg", ysyAvg / size);
        map.put("totalAvg", totalAvg / size);

        return "/newgkelective/singleRecomb/rangeStuInfoList.ftl";
    }

    // ================================= 组合固定分教学班 =================================
    @RequestMapping("/intelliDivide")
    @ResponseBody
    public String intelliDivideClass(@PathVariable String divideId, String subjectType, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findById(divideId);

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);

        // 学生数据
        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentChosenMap.keySet().toArray(new String[0])), Student.class);
        Map<String, StudentResultDto> studentResultDtoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(studentList)) {
            StudentResultDto dto;
            for (Student student : studentList) {
                dto = new StudentResultDto();
                StringBuilder tmp = new StringBuilder();
                for (String sub : studentChosenMap.get(student.getId())) {
                    tmp.append("," + sub);
                }
                dto.setChooseSubjects(tmp.substring(1));
                dto.setStudentName(student.getStudentName());
                dto.setStudentId(student.getId());
                if (Integer.valueOf(BaseConstants.MALE).equals(student.getSex())) {
                    dto.setSex("1");
                } else {
                    dto.setSex("2");
                }
                studentResultDtoMap.put(student.getId(), dto);
            }
        }
        makeStudentSubjectScore(divide.getReferScoreId(), new ArrayList<>(studentResultDtoMap.values()));

        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(divide.getUnitId()), Course.class);
        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divide.getId(), new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        Map<String, Course> courseMap = EntityUtils.getMap(courseList, e -> e.getId());
        List<Course> sixCourseList = courseList.stream().filter(e -> openSubjectIds.contains(e.getId())).collect(Collectors.toList());
        List<Course> otherCourseList = sixCourseList.stream().filter(e -> !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode())).collect(Collectors.toList());

        List<Clazz> clazzList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(divide.getUnitId(), divide.getGradeId()), Clazz.class);
        int classNum = clazzList.size();
        int studentNum = studentChosenMap.keySet().size();
        // 每个班人数规模以此为准
        int studentNumPerClass = studentNum / classNum;

        List<NewGkDivideClass> insertClassList = new ArrayList<>();
        List<NewGkClassStudent> insertStuList = new ArrayList<>();

        List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);

        if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
            // 三科组合班
            List<NewGkDivideClass> threeClassList = allClassList.stream()
                    .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(THREE_COMBINE, e.getSubjectType()))
                    .collect(Collectors.toList());
            Set<String> threeClassStudentIdSet = threeClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
            // 一科组合班
            List<NewGkDivideClass> oneClassList = allClassList.stream()
                    .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(ONE_COMBINE, e.getSubjectType()))
                    .collect(Collectors.toList());
            Set<String> oneClassStudentIdSet = oneClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());

            String[][] hisAndPhy = new String[2][];
            for (Course cours : courseList) {
                if (NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(cours.getSubjectCode())) {
                    hisAndPhy[0] = new String[]{cours.getSubjectCode(), cours.getId(), cours.getSubjectName()};
                } else if (NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(cours.getSubjectCode())) {
                    hisAndPhy[1] = new String[]{cours.getSubjectCode(), cours.getId(), cours.getSubjectName()};
                }
            }

            // 3门组合
            List<NewGkGroupDto> threeGroupDtoList = new ArrayList<>();
            Map<String, NewGkGroupDto> threeGroupDtoMap = new HashMap<>();
            // 同组合下学生
            Map<String, Set<String>> subjectIdsToStudentIdMap = new HashMap<>();

            // 1门组合
            List<NewGkGroupDto> oneGroupDtoList = new ArrayList<>();
            Map<String, NewGkGroupDto> oneGroupDtoMap = new HashMap<>();
            // 同组合下学生
            Map<String, Set<String>> oneSubjectIdToStudentIdsMap = new HashMap<>();
            NewGkGroupDto groupDto;
            for (Map.Entry<String, Set<String>> stuSubjectIds : studentChosenMap.entrySet()) {
                if (threeClassStudentIdSet.contains(stuSubjectIds.getKey()) || oneClassStudentIdSet.contains(stuSubjectIds.getKey())) {
                    continue;
                }
                if (CollectionUtils.isEmpty(stuSubjectIds.getValue())) {
                    continue;
                }
                // 选择满3门才算组合
                if (stuSubjectIds.getValue().size() != 3) {
                    continue;
                }
                String studentId = stuSubjectIds.getKey();
                Set<String> chosenSubjectId = new HashSet<>();
                chosenSubjectId.addAll(stuSubjectIds.getValue());
                String ids = keySort(chosenSubjectId);
                // 3门组合
                if (!threeGroupDtoMap.containsKey(ids)) {
                    groupDto = makeNewGkGroupDto(ids, NewGkElectiveConstant.SUBJTCT_TYPE_3, nameSet(courseMap, ids));
                    threeGroupDtoList.add(groupDto);
                    threeGroupDtoMap.put(ids, groupDto);
                    subjectIdsToStudentIdMap.put(ids, new HashSet<>());
                    subjectIdsToStudentIdMap.get(ids).add(studentId);
                } else {
                    subjectIdsToStudentIdMap.get(ids).add(studentId);
                    groupDto = threeGroupDtoMap.get(ids);
                    groupDto.setAllNumber(groupDto.getAllNumber() + 1);
                }

                // 1门组合
                for (String subjectIdTmp : chosenSubjectId) {
                    if (hisAndPhy[0][1].equals(subjectIdTmp) || hisAndPhy[1][1].equals(subjectIdTmp)) {
                        if (!oneGroupDtoMap.containsKey(subjectIdTmp)) {
                            groupDto = makeNewGkGroupDto(subjectIdTmp, NewGkElectiveConstant.SUBJTCT_TYPE_1, courseMap.get(subjectIdTmp).getSubjectName());
                            oneGroupDtoList.add(groupDto);
                            oneGroupDtoMap.put(subjectIdTmp, groupDto);
                            oneSubjectIdToStudentIdsMap.put(subjectIdTmp, new HashSet<>());
                            oneSubjectIdToStudentIdsMap.get(subjectIdTmp).add(studentId);
                        } else {
                            oneSubjectIdToStudentIdsMap.get(subjectIdTmp).add(studentId);
                            groupDto = oneGroupDtoMap.get(subjectIdTmp);
                            groupDto.setAllNumber(groupDto.getAllNumber() + 1);
                        }
                    }
                }
            }

            // 固定三科组合班
            for (NewGkGroupDto dto : threeGroupDtoList) {
                if (dto.getAllNumber() >= studentNumPerClass) {
                    Set<String> oneSubjectStudentSet = new HashSet<>();
                    if (dto.getSubjectIds().contains(hisAndPhy[0][1])) {
                        oneSubjectStudentSet = oneSubjectIdToStudentIdsMap.get(hisAndPhy[0][1]);
                    }
                    if (dto.getSubjectIds().contains(hisAndPhy[1][1])) {
                        oneSubjectStudentSet = oneSubjectIdToStudentIdsMap.get(hisAndPhy[1][1]);
                    }
                    int classNumTmp = dto.getAllNumber() / studentNumPerClass;
                    List<StudentResultDto> unStudentResultDtoList = subjectIdsToStudentIdMap.get(dto.getSubjectIds()).stream().map(e -> studentResultDtoMap.get(e)).collect(Collectors.toList());
                    makeScoreByOpenBasic(unStudentResultDtoList, dto.getSubjectIds(), true);

                    List<StudentResultDto> moveList;
                    if (studentNumPerClass * classNumTmp <= unStudentResultDtoList.size()) {
                        moveList = unStudentResultDtoList.subList(0, studentNumPerClass * classNumTmp);
                    } else {
                        moveList = unStudentResultDtoList;
                    }
                    // 学生(每班学生)
                    List<String>[] array = new List[classNumTmp];
                    for (int index = 0; index < classNumTmp; index++) {
                        array[index] = new ArrayList<>();
                    }
                    // 交叉 加上性别
                    openClassBySex(classNumTmp, moveList, array);

                    String groupName = "";
                    for (String subjectId : dto.getSubjectIds().split(",")) {
                        groupName += courseMap.get(subjectId).getShortName();
                    }

                    int index = 1;
                    if (CollectionUtils.isNotEmpty(threeClassList)) {
                        List<String> teachNameList = EntityUtils.getList(threeClassList, NewGkDivideClass::getClassName);
                        while (true) {
                            if (!teachNameList.contains(groupName + index + "班")) {
                                break;
                            }
                            index++;
                        }
                    }

                    for (List<String> one : array) {
                        NewGkDivideClass clazz = initNewGkDivideClass(divideId, dto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_0);
                        clazz.setSubjectType(THREE_COMBINE);
                        clazz.setClassName(groupName + index + "班");
                        clazz.setOrderId(index);
                        insertClassList.add(clazz);
                        oneSubjectStudentSet.removeAll(one);
                        index++;
                        for (String sub : one) {
                            NewGkClassStudent tmp = initClassStudent(divide.getUnitId(), divideId, clazz.getId(), sub);
                            insertStuList.add(tmp);
                        }
                    }
                }
            }

            // 一科组合班
            for (String subjectId : oneSubjectIdToStudentIdsMap.keySet()) {
                Set<String> oneSubjectStudentSet = oneSubjectIdToStudentIdsMap.get(subjectId);
                int classNumTmp = oneSubjectStudentSet.size() / studentNumPerClass;
                if (classNumTmp == 0 || (oneSubjectStudentSet.size() % studentNumPerClass) > (studentNumPerClass / 2)) {
                    classNumTmp += 1;
                }
                List<StudentResultDto> unStudentResultDtoList = oneSubjectStudentSet.stream().map(e -> studentResultDtoMap.get(e)).collect(Collectors.toList());
                makeScoreByOpenBasic(unStudentResultDtoList, subjectId, true);

                List<StudentResultDto> moveList = unStudentResultDtoList;

                // 学生(每班学生)
                List<String>[] array = new List[classNumTmp];
                for (int index = 0; index < classNumTmp; index++) {
                    array[index] = new ArrayList<>();
                }
                // 交叉 加上性别
                openClassBySex(classNumTmp, moveList, array);

                String groupName = courseMap.get(subjectId).getShortName();

                int index = 1;
                if (CollectionUtils.isNotEmpty(oneClassList)) {
                    List<String> teachNameList = EntityUtils.getList(threeClassList, NewGkDivideClass::getClassName);
                    while (true) {
                        if (!teachNameList.contains(groupName + index + "班")) {
                            break;
                        }
                        index++;
                    }
                }

                for (List<String> one : array) {
                    NewGkDivideClass clazz = initNewGkDivideClass(divideId, subjectId, NewGkElectiveConstant.CLASS_TYPE_0);
                    clazz.setSubjectType(ONE_COMBINE);
                    clazz.setClassName(groupName + index + "班");
                    clazz.setOrderId(index);
                    insertClassList.add(clazz);
                    index++;
                    for (String sub : one) {
                        NewGkClassStudent tmp = initClassStudent(divide.getUnitId(), divideId, clazz.getId(), sub);
                        insertStuList.add(tmp);
                    }
                }
            }
        } else if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
            // 三科组合班
            List<NewGkDivideClass> threeClassList = allClassList.stream()
                    .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(THREE_COMBINE, e.getSubjectType()))
                    .collect(Collectors.toList());
            Set<String> threeClassStudentIdSet = threeClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
            // 两科组合班
            List<NewGkDivideClass> teachClassList = allClassList.stream()
                    .filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
                    .collect(Collectors.toList());
            Map<String, List<NewGkDivideClass>> subjectIdsToClassMap = teachClassList.stream().collect(Collectors.groupingBy(NewGkDivideClass::getSubjectIds));

            Integer[] cSize = new Integer[otherCourseList.size()];
            for (int i = 0; i < otherCourseList.size(); i++) {
                cSize[i] = i;
            }
            CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, Integer.valueOf(2));
            Integer[][] result = combineAlgorithm.getResutl();
            List<ChoiceSubjectDto> twoCombineSubjectDtoList = new ArrayList<>();
            for (int i = 0; i < result.length; i++) {
                ChoiceSubjectDto twoCombineSubjectDto = new ChoiceSubjectDto();
                String ids = "";
                for (int j = 0; j <result[i].length; j++) {
                    if (j == 0) {
                        ids += otherCourseList.get(result[i][j]).getId();
                    } else {
                        ids = ids + ',' + otherCourseList.get(result[i][j]).getId();
                    }
                }
                final String subjectIds = ids;
                String subjectIdsB = otherCourseList.stream().filter(e -> !subjectIds.contains(e.getId())).map(e -> e.getId()).collect(Collectors.joining(","));
                Set<String> twoCombineStudentSet = new HashSet<>(CollectionUtils.intersection(subjectChosenMap.get(ids.split(",")[0]), subjectChosenMap.get(ids.split(",")[1])));
                twoCombineSubjectDto.setIds(ids);
                // 若为组合固定重组，无需考虑三科固定的学生
                if (NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
                    twoCombineStudentSet.removeAll(threeClassStudentIdSet);
                }
                if (CollectionUtils.isNotEmpty(subjectIdsToClassMap.get(ids))) {
                    for (NewGkDivideClass one : subjectIdsToClassMap.get(ids)) {
                        twoCombineStudentSet.removeAll(one.getStudentList());
                    }
                }
                if (CollectionUtils.isEmpty(twoCombineStudentSet)) {
                    continue;
                }
                int unSettledNum = twoCombineStudentSet.size();
                if (CollectionUtils.isNotEmpty(subjectIdsToClassMap.get(ids))) {
                    twoCombineSubjectDto.setClassList(subjectIdsToClassMap.get(ids));
                    for (NewGkDivideClass one : subjectIdsToClassMap.get(ids)) {
                        unSettledNum -= one.getStudentCount();
                    }
                }
                twoCombineSubjectDto.setUnSettledNum(unSettledNum);
                twoCombineSubjectDtoList.add(twoCombineSubjectDto);

                // 初始化教学班
                int classNumTmp = twoCombineStudentSet.size() / studentNumPerClass;
                if (classNumTmp == 0 || (twoCombineStudentSet.size() % studentNumPerClass) > (studentNumPerClass / 2)) {
                    classNumTmp += 1;
                }

                List<StudentResultDto> unStudentResultDtoList = twoCombineStudentSet.stream().map(e -> studentResultDtoMap.get(e)).collect(Collectors.toList());
                makeScoreByOpenBasic(unStudentResultDtoList, ids, true);

                List<StudentResultDto> moveList = unStudentResultDtoList;
                // 学生(每班学生)
                List<String>[] array = new List[classNumTmp];
                for (int index = 0; index < classNumTmp; index++) {
                    array[index] = new ArrayList<>();
                }
                // 交叉 加上性别
                openClassBySex(classNumTmp, moveList, array);

                String groupName = "";
                for (String subjectId : ids.split(",")) {
                    groupName += courseMap.get(subjectId).getShortName();
                }

                int index = 1;
                if (CollectionUtils.isNotEmpty(teachClassList)) {
                    List<String> teachNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
                    while (true) {
                        if (!teachNameList.contains(groupName + index + "班")) {
                            break;
                        }
                        index++;
                    }
                }

                for (List<String> one : array) {
                    NewGkDivideClass clazz = initNewGkDivideClass(divideId, ids, NewGkElectiveConstant.CLASS_TYPE_4);
                    clazz.setSubjectIdsB(subjectIdsB);
                    clazz.setClassName(groupName + index + "班");
                    clazz.setOrderId(index);
                    insertClassList.add(clazz);
                    index++;
                    for (String sub : one) {
                        NewGkClassStudent tmp = initClassStudent(divide.getUnitId(), divideId, clazz.getId(), sub);
                        insertStuList.add(tmp);
                    }
                }
            }

        }

        try {
            if (CollectionUtils.isNotEmpty(insertClassList)) {
                newGkDivideClassService.saveAllList(null, insertClassList, insertStuList);
            }
        } catch (Exception e) {
            return error("保存失败！");
        }

        return success("保存成功！");
    }

    @ResponseBody
    @RequestMapping("/moveAllGroup")
    @ControllerInfo(value = "移除所有开设班级")
    public String moveAllGroup(@PathVariable String divideId) {
        try {
            String unitId=this.getLoginInfo().getUnitId();
            //删除所有
            List<NewGkDivideClass> groupClassList = newGkDivideClassService
                    .findClassBySubjectIdsWithMaster(unitId,
                            divideId,
                            NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_4,
                            null, false);
            if (CollectionUtils.isNotEmpty(groupClassList)) {
                newGkDivideClassService.deleteByClassIdIn(unitId,divideId, EntityUtils
                        .getSet(groupClassList, NewGkDivideClass::getId).toArray(
                                new String[] {}));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return error("解散数据失败！");
        }
        return success("解散数据成功！");
    }

    private NewGkGroupDto makeNewGkGroupDto(String subjectIds, String subjectType, String conditionName) {
        NewGkGroupDto groupDto = new NewGkGroupDto();
        groupDto.setSubjectIds(subjectIds);
        groupDto.setSubjectType(subjectType);
        groupDto.setConditionName(conditionName);
        groupDto.setGkGroupClassList(new ArrayList<>());
        groupDto.setAllNumber(1);
        return groupDto;
    }

    @RequestMapping("/editClass/page")
    @ControllerInfo(value = "编辑")
    public String editClass(@PathVariable String divideId, String subjectIds, ModelMap map) {
        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(getLoginInfo().getUnitId()),
                new TR<List<Course>>() {
                });
        Map<String, Course> courseMap = EntityUtils.getMap(courseList, e -> e.getId());
        String groupName = nameSet(courseMap, subjectIds);
        String[] subjectIdArr = subjectIds.split(",");

        map.put("groupName", groupName);
        List<NewGkDivideClass> clazzList = newGkDivideClassService.findClassBySubjectIdsWithMaster(getLoginInfo().getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_4, subjectIds, true);
        // 组装数据
        List<NewGkDivideStusub> chooselist = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, subjectIdArr);
        Map<String, String> stuMap = EntityUtils.getMap(chooselist, NewGkDivideStusub::getStudentId, NewGkDivideStusub::getSubjectIds);
        List<DivideClassEditSaveDto> dtoList = new ArrayList<>();
        DivideClassEditSaveDto dto;
        for (NewGkDivideClass cc : clazzList) {
            dto = new DivideClassEditSaveDto();
            dto.setClassId(cc.getId());
            dto.setClassName(cc.getClassName());
            if (CollectionUtils.isEmpty(cc.getStudentList())) {
                // 学生人数为0
                dto.setStuNum(0);
                dtoList.add(dto);
                continue;
            }
            // 每种组合人数
            Map<String, Integer> numMap = new HashMap<>();
            for (String s : cc.getStudentList()) {
                if (!stuMap.containsKey(s)) {
                    //一般不会进来
                    continue;
                }
                if (!numMap.containsKey(stuMap.get(s))) {
                    numMap.put(stuMap.get(s), 1);
                } else {
                    numMap.put(stuMap.get(s), numMap.get(stuMap.get(s)) + 1);
                }
            }
            if (numMap.size() == 0) {
                // 一般不会进来--如果进来 建议删除
                dto.setStuNum(0);
                dtoList.add(dto);
                continue;
            }
            // 保留的组合数据
            String[] subjectIdsq = new String[numMap.size()];
            String[] subGroupName = new String[numMap.size()];
            Integer[] subStuNum = new Integer[numMap.size()];
            int i = 0;
            int size = 0;
            for (Map.Entry<String, Integer> ii : numMap.entrySet()) {
                subjectIdsq[i] = ii.getKey();
                subStuNum[i] = ii.getValue();
                subGroupName[i] = nameSet(courseMap, ii.getKey());
                i++;
                size = size + ii.getValue();
            }
            dto.setStuNum(size);
            dto.setSubGroupName(subGroupName);
            dto.setSubjectIds(subjectIdsq);
            dto.setSubStuNum(subStuNum);
            dtoList.add(dto);
        }

        map.put("dtoList", dtoList);
        return "/newgkelective/singleRecomb/classEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/saveClassOrDel")
    public String saveClassOrDel(@PathVariable String divideId, String delClassId, NewGkGroupDto dto) {
        //delClassId 防止对其他刚新增的班级进行删除
        List<DivideClassEditSaveDto> dcList = dto.getSaveDto();
        List<NewGkDivideClass> groupClassList = newGkDivideClassService
                .findClassBySubjectIds(getLoginInfo().getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
                        NewGkElectiveConstant.CLASS_TYPE_4, null, false);

        // 需要完全删除的班级id
        Set<String> delSet = new HashSet<>();
        // 学生数据修改
        // 需要删除的学生数据
        Set<String> delCStuIds = new HashSet<>();
        boolean isNoFind = false;
        if (CollectionUtils.isEmpty(groupClassList)) {
            return error("班级数据有调整，请刷新后操作");
        }
        Map<String, NewGkDivideClass> clazzMap = EntityUtils.getMap(groupClassList, e -> e.getId());
        if (net.zdsoft.framework.utils.StringUtils.isNotBlank(delClassId)) {
            String[] delIds = delClassId.split(",");
            for (String one : delIds) {
                if (clazzMap.containsKey(one)) {
                    delSet.add(one);
                } else {
                    if (!isNoFind) {
                        isNoFind = true;
                    }
                }
            }
        }

        // 修改名称
        List<NewGkDivideClass> insertUpdateList = new ArrayList<>();
        List<DivideClassEditSaveDto> updateNameList = new ArrayList<>();
        List<DivideClassEditSaveDto> updateStuList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dcList)) {
            // 修改名称--是否重名
            Set<String> updateIds = new HashSet<>();
            for (DivideClassEditSaveDto one : dcList) {
                if (one == null || net.zdsoft.framework.utils.StringUtils.isBlank(one.getClassId())) {
                    continue;
                }
                if (!clazzMap.containsKey(one.getClassId())) {
                    if (!isNoFind) {
                        isNoFind = true;
                    }
                    continue;
                }
                if (net.zdsoft.framework.utils.StringUtils.isBlank(one.getClassName())) {
                    return error("班级名称不能为空");
                }
                updateStuList.add(one);
                if (clazzMap.get(one.getClassId()).getClassName().equals(one.getClassName())) {
                    // 不需要改变
                    continue;
                }
                // 需要修改名称的班级
                updateNameList.add(one);
                updateIds.add(one.getClassId());
            }
            if (CollectionUtils.isNotEmpty(updateIds)) {
                groupClassList = groupClassList.stream()
                        .filter(x -> !updateIds.contains(x.getId()))
                        .collect(Collectors.toList());
                Set<String> sameList = EntityUtils.getSet(groupClassList, e -> e.getClassName());
                for (DivideClassEditSaveDto v : updateNameList) {
                    if (sameList.contains(v.getClassName().trim())) {
                        return error(v.getClassName() + "名称重复");
                    }
                    NewGkDivideClass item = clazzMap.get(v.getClassId());
                    item.setClassName(v.getClassName().trim());
                    item.setModifyTime(new Date());
                    Integer ii = subNum(item.getClassName());
                    if (ii != null) {
                        item.setOrderId(ii);
                    }
                    insertUpdateList.add(item);
                    sameList.add(item.getClassName());
                }
            }

            if (CollectionUtils.isNotEmpty(updateStuList)) {
                List<NewGkDivideStusub> chooselist = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
                Map<String, String> stuMap = EntityUtils.getMap(chooselist, NewGkDivideStusub::getStudentId, NewGkDivideStusub::getSubjectIds);
                Set<String> classIds = EntityUtils.getSet(updateStuList, e -> e.getClassId());
                List<NewGkClassStudent> slist = newGkClassStudentService.findListByClassIds(getLoginInfo().getUnitId(), divideId, classIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(slist)) {
                    Map<String, List<NewGkClassStudent>> classStuList = EntityUtils.getListMap(slist, NewGkClassStudent::getClassId, e -> e);
                    for (DivideClassEditSaveDto c : updateStuList) {
                        NewGkDivideClass clazz = clazzMap.get(c.getClassId());
                        List<NewGkClassStudent> ll = classStuList.get(clazz.getId());
                        //传递的subjectIds都是同样的排序
                        //sub 包含null 影响不大
                        List<String> sub = Arrays.asList(c.getSubjectIds());
                        if (CollectionUtils.isNotEmpty(ll)) {
                            for (NewGkClassStudent s : ll) {
                                if (!stuMap.containsKey(s.getStudentId())) {
                                    delCStuIds.add(s.getId());
                                }
                                if (!sub.contains(stuMap.get(s.getStudentId()))) {
                                    delCStuIds.add(s.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
        String[] ids = null;
        if (CollectionUtils.isNotEmpty(delSet)) {
            ids = delSet.toArray(new String[0]);
        }
        String[] csids = null;
        if (CollectionUtils.isNotEmpty(delCStuIds)) {
            csids = delCStuIds.toArray(new String[0]);
        }
        try {
            newGkDivideClassService.saveClassOrDel(getLoginInfo().getUnitId(),divideId,insertUpdateList, ids, csids);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    @RequestMapping("/openTeachClass")
    public String showTeachClass(@PathVariable String divideId, String subjectType, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findOne(divideId);
        String unitId = divide.getUnitId();
        map.put("divide", divide);
        if (subjectType == null) {
            subjectType = NewGkElectiveConstant.SUBJECT_TYPE_A;
        }
        map.put("subjectType", subjectType);
        map.put("divideId", divideId);
        if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
            map.put("batchCount", divide.getBatchCountTypea());
        } else {
            map.put("batchCount", divide.getBatchCountTypeb());
        }

        List<NewGkDivideClass> allClassList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        // 三科组合班
        List<NewGkDivideClass> threeClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(THREE_COMBINE, e.getSubjectType()))
                .collect(Collectors.toList());
        Set<String> threeClassStudentIdSet = threeClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
        // 一科组合班
        List<NewGkDivideClass> oneClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(ONE_COMBINE, e.getSubjectType()))
                .collect(Collectors.toList());
        
        List<NewGkDivideClass> teachClassList = allClassList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType()))
                .collect(Collectors.toList());
        Map<String, List<NewGkDivideClass>> subjectIdsToClassMap = teachClassList.stream().collect(Collectors.groupingBy(NewGkDivideClass::getSubjectIds));
        map.put("subjectIdsToClassMap", subjectIdsToClassMap);

        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(divideId), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divide.getId(), new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        // 获取选课数据
        Map<String,Set<String>> subjectChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenBMap = new HashMap<>();
        makeStudentChooseResult(divide, openSubjectIds, subjectChosenMap, studentChosenMap, studentChosenBMap, subjectType);

        // 政治 地理 化学 生物
        List<Course> otherCourseList = courseList.stream().filter(e -> openSubjectIds.contains(e.getId()) && !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode())).collect(Collectors.toList());
        Integer[] cSize = new Integer[otherCourseList.size()];
        for (int i = 0; i < otherCourseList.size(); i++) {
            cSize[i] = i;
        }
        CombineAlgorithmInt combineAlgorithm = new CombineAlgorithmInt(cSize, Integer.valueOf(2));
        Integer[][] result = combineAlgorithm.getResutl();
        List<ChoiceSubjectDto> twoCombineSubjectDtoList = new ArrayList<>();
        int chosenStudentNum = 0;
        int fixStudentNum = 0;
        int noFixStudentNum = 0;
        for (int i = 0; i < result.length; i++) {
            ChoiceSubjectDto twoCombineSubjectDto = new ChoiceSubjectDto();
            String ids = "";
            String shortNames = "";
            for (int j = 0; j <result[i].length; j++) {
                if (j == 0) {
                    ids += otherCourseList.get(result[i][j]).getId();
                } else {
                    ids = ids + ',' + otherCourseList.get(result[i][j]).getId();
                }
                shortNames += otherCourseList.get(result[i][j]).getShortName();
            }
            Set<String> twoCombineStudentSet = new HashSet<>(CollectionUtils.intersection(subjectChosenMap.get(ids.split(",")[0]), subjectChosenMap.get(ids.split(",")[1])));
            twoCombineSubjectDto.setIds(ids);
            twoCombineSubjectDto.setShortNames(shortNames);
            twoCombineStudentSet.removeAll(threeClassStudentIdSet);
            twoCombineSubjectDto.setTotalNum(twoCombineStudentSet.size());
            int unSettledNum = twoCombineStudentSet.size();
            chosenStudentNum += unSettledNum;
            if (CollectionUtils.isNotEmpty(subjectIdsToClassMap.get(ids))) {
                twoCombineSubjectDto.setClassList(subjectIdsToClassMap.get(ids));
                for (NewGkDivideClass one : subjectIdsToClassMap.get(ids)) {
                    unSettledNum -= one.getStudentCount();
                    fixStudentNum += one.getStudentCount();
                }
            }
            noFixStudentNum += unSettledNum;
            twoCombineSubjectDto.setUnSettledNum(unSettledNum);
            twoCombineSubjectDtoList.add(twoCombineSubjectDto);
        }
        if (noFixStudentNum == 0) {
            map.put("allSolved", true);
        }
        map.put("chosenStudentNum", chosenStudentNum);
        map.put("fixStudentNum", fixStudentNum);
        map.put("noFixStudentNum", noFixStudentNum);
        map.put("twoCombineSubjectDtoList", twoCombineSubjectDtoList);
        return "/newgkelective/singleRecomb/noSingleTeachClassArrangeList.ftl";
    }

    @RequestMapping("/quickOpenClass/page")
    @ControllerInfo(value = "进入快捷设置")
    public String quickOpenClass(@PathVariable String divideId, String subjectIds, String type, ModelMap map) {
        map.put("divideId", divideId);
        map.put("subjectIds", subjectIds);
        NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
        List<Course> allCourseList = SUtils.dt(courseRemoteService.findByCodes73(divide.getUnitId()), Course.class);
        allCourseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divide.getId(), new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        String subjectIdsB = allCourseList.stream().filter(e -> openSubjectIds.contains(e.getId()) && !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode()) && !subjectIds.contains(e.getId())).map(e -> e.getId()).collect(Collectors.joining(","));
        map.put("subjectIdsB", subjectIdsB);

        String[] subjectIdArr = subjectIds.split(",");
        List<NewGkDivideStusub> chosenList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, subjectIdArr);
        // 已经安排的学生
        // List<String> arrangeStudentList = newGkClassStudentService.findArrangeStudentIdWithMaster(divideId, NewGkElectiveConstant.CLASS_TYPE_4);
        List<NewGkDivideClass> teachClassList = newGkDivideClassService.findByDivideIdAndClassTypeAndSubjectIds(divide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_TYPE_4, subjectIds, true);
        List<String> arrangeStudentList = teachClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toList());
        if (NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
            List<NewGkDivideClass> threeClassList = newGkDivideClassService.findByDivideIdAndClassType(divide.getUnitId(), divideId, new String[] {NewGkElectiveConstant.CLASS_TYPE_0}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
            int tmp = chosenList.size();
            if (CollectionUtils.isNotEmpty(threeClassList)) {
                for (NewGkDivideClass one : threeClassList) {
                    boolean flag = true;
                    for (String subjectId : subjectIdArr) {
                        if (!one.getSubjectIds().contains(subjectId)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        arrangeStudentList.addAll(one.getStudentList());
                        tmp -= one.getStudentList().size();
                    }
                }
            }
            map.put("allSize", tmp);
        } else {
            map.put("allSize", chosenList.size());
        }
        // 总体年级平均人数
        int avg = findAvgByGradeId(divide.getGradeId());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdArr), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        StringBuilder groupName = new StringBuilder();
        courseList.stream().forEach(e -> groupName.append(e.getShortName()));
        map.put("groupName", groupName.toString());
        int arrangeSize;
        if (CollectionUtils.isNotEmpty(arrangeStudentList)) {
            Set<String> ids = EntityUtils.getSet(chosenList, e -> e.getStudentId());
            arrangeSize = chosenList.size() - CollectionUtils.intersection(ids, arrangeStudentList).size();
        } else {
            arrangeSize = chosenList.size();
        }
        int defaultClassNum = (arrangeSize - 1) / avg + 1;
        map.put("defaultClassNum", defaultClassNum);
        map.put("arrangeSize", arrangeSize);
        map.put("avg", Math.round(arrangeSize * 1.0f / defaultClassNum));
        return "/newgkelective/singleRecomb/quickOpenClass.ftl";
    }

    @ResponseBody
    @RequestMapping("/saveQuickOpen")
    @ControllerInfo(value = "快捷分班保存")
    public String saveQuickOpen(@PathVariable String divideId, NewGkQuickSaveDto saveDto) {
        NewGkDivide divide = newGkDivideService.findOneWithMaster(divideId);
        List<NewGkDivideClass> insertClassList = new ArrayList<>();
        List<NewGkClassStudent> insertStuList = new ArrayList<>();
        String unitId = divide.getUnitId();
        String subjectIds = saveDto.getSubjectIds();
        String[] subjectIdArr = subjectIds.split(",");
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIdArr), new TR<List<Course>>() {});
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        StringBuilder groupNameBuilder = new StringBuilder();
        courseList.stream().forEach(e -> groupNameBuilder.append(e.getShortName()));
        String groupName = groupNameBuilder.toString();

        // 获取选课数据
        Map<String, Set<String>> subjectChosenMap = new HashMap<>();
        Map<String, Set<String>> studentChosenMap = new HashMap<>();
        makeStudentChooseResult(divide, null, subjectChosenMap, studentChosenMap, null, NewGkElectiveConstant.SUBJECT_TYPE_A);

        List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        // 三科组合班
        List<NewGkDivideClass> threeClassList = classList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(THREE_COMBINE, e.getSubjectType()))
                .collect(Collectors.toList());
        Set<String> threeClassStudentIdSet = threeClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
        List<NewGkDivideClass> teachClassList = classList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType())
                        && Objects.equals(e.getSubjectIds(), subjectIds))
                .collect(Collectors.toList());
        Set<String> teachClassStudentIdSet = teachClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());

        int index = 1;
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            List<String> teachNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
            while (true) {
                if (!teachNameList.contains(groupName + index + "班")) {
                    break;
                }
                index++;
            }
        }

        // 剩余未安排的学生
        List<String> unSettledStudentList = new ArrayList<>(CollectionUtils.intersection(subjectChosenMap.get(subjectIdArr[0]), subjectChosenMap.get(subjectIdArr[1])));
        // 去除三科固定学生
        unSettledStudentList.removeAll(threeClassStudentIdSet);
        // 去除两科已安排学生
        unSettledStudentList.removeAll(teachClassStudentIdSet);

        List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(unSettledStudentList.toArray(new String[0])), Student.class);
        Map<String, Student> studentMap = EntityUtils.getMap(studentList, Student::getId);

        List<StudentResultDto> unStudentResultDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(unSettledStudentList)) {
            StudentResultDto dto;
            for (String studentId : unSettledStudentList) {
                dto = new StudentResultDto();
                StringBuilder tmp = new StringBuilder();
                for (String sub : studentChosenMap.get(studentId)) {
                    tmp.append("," + sub);
                }
                dto.setChooseSubjects(tmp.substring(1));
                dto.setStudentName(studentMap.get(studentId).getStudentName());
                dto.setStudentId(studentId);
                if (studentMap.get(studentId).getSex().equals(BaseConstants.MALE)) {
                    dto.setSex("1");
                } else {
                    dto.setSex("2");
                }
                unStudentResultDtoList.add(dto);
            }
        }
        if (CollectionUtils.isNotEmpty(unStudentResultDtoList)) {
            // 0:无依据
            // 1:按该选科成绩排名  ---3+0 1+x----2+x
            // 2:按该选科与语数英成绩之和排名---3+0 1+x----2+x
            if ("1".equals(saveDto.getOpenBasis()) || "2".equals(saveDto.getOpenBasis())) {
                makeStudentSubjectScore(divide.getReferScoreId(), unStudentResultDtoList);
                if ("1".equals(saveDto.getOpenBasis())) {
                    makeScoreByOpenBasic(unStudentResultDtoList, subjectIds, false);
                } else {
                    makeScoreByOpenBasic(unStudentResultDtoList, subjectIds, true);
                }
                onlysortStuScore(unStudentResultDtoList);
                // 根据moveList 排序
                if ("1".equals(saveDto.getBasisType())) {
                    // 按顺序
                    // 根据成绩排序
                    makeClassesBySort(unStudentResultDtoList, divideId, unitId, index, groupName, saveDto, insertClassList, insertStuList);
                } else {
                    int classNum = saveDto.getClassNum();
                    int stuNum = saveDto.getArrangeStudentNum();
                    List<StudentResultDto> moveList;
                    if (stuNum <= unStudentResultDtoList.size()) {
                        moveList = unStudentResultDtoList.subList(0, stuNum);
                    } else {
                        moveList = unStudentResultDtoList;
                    }
                    // 学生(每班学生)
                    List<String>[] array = new List[classNum];
                    for (int i = 0; i < classNum; i++) {
                        array[i] = new ArrayList<>();
                    }
                    // 交叉 加上性别
                    openClassBySex(classNum, moveList, array);

                    for (List<String> one : array) {
                        NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_4);
                        clazz.setSubjectIdsB(saveDto.getSubjectIdsB());
                        clazz.setClassName(groupName + index + "班");
                        clazz.setOrderId(index);
                        insertClassList.add(clazz);
                        index++;
                        for (String sub : one) {
                            NewGkClassStudent tmp = initClassStudent(unitId, divideId, clazz.getId(), sub);
                            insertStuList.add(tmp);
                        }
                    }
                }
            } else {
                makeClassesBySort(unStudentResultDtoList, divideId, unitId, index, groupName, saveDto, insertClassList, insertStuList);
            }
        } else {
            return error("没有未安排的学生，请刷新后操作");
        }

        try {
            if (CollectionUtils.isNotEmpty(insertClassList)) {
                newGkDivideClassService.saveAllList(null, insertClassList, insertStuList);
            }
        } catch (Exception e) {
            return error("保存失败！");
        }

        return success("保存成功！");
    }

    @ResponseBody
    @RequestMapping("/autoOpenClass")
    public String autoOpenClassByClassNum(@PathVariable String divideId, String subjectType, String subjectIds, int openNum, String stuIds) {

        if (StringUtils.isBlank(subjectIds)) {
            return error("没有选中组合");
        }
        if (StringUtils.isBlank(stuIds)) {
            return error("未选择学生");
        }
        if (openNum <= 0) {
            return error("开设班级数应为正整数");
        }

        NewGkDivide newDivide = newGkDivideService.findById(divideId);
        if (newDivide == null) {
            return error("分班方案不存在");
        }

        List<Course> allCourseList = SUtils.dt(courseRemoteService.findByCodes73(newDivide.getUnitId()), Course.class);
        allCourseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divideId, new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        String subjectIdsB = allCourseList.stream()
                .filter(e -> openSubjectIds.contains(e.getId())
                        && !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode())
                        && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode())
                        && !subjectIds.contains(e.getId())
                        && !subjectIds.contains(e.getId()))
                .map(e -> e.getId())
                .collect(Collectors.joining(","));

        String unitId = getLoginInfo().getUnitId();

        List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.split(",")), Course.class);
        
        Map<String, Course> courseMap=EntityUtils.getMap(courseList, e->e.getId());
		String teachClassName =  nameSet(courseMap, subjectIds);
        List<NewGkDivideClass> teachClassList = newGkDivideClassService.findClassBySubjectIds(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_4, subjectIds, false);
        
        List<String> teachClassNameList =new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            teachClassNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
        }

        // 新增班级
        List<NewGkClassStudent> insertStudentList = new ArrayList<>();
        List<NewGkDivideClass> insertClassList = new ArrayList<>();
        NewGkDivideClass newGkDivideClass;
        NewGkClassStudent newGkClassStudent;
        int k = 1;
        for (int i = 0; i < openNum; i++) {
        	while(true) {
        		if(teachClassNameList.contains(teachClassName + k + "班")) {
        			k++;
        		}else {
        			break;
        		}
        	}
            newGkDivideClass = initNewGkDivideClass(divideId, subjectIds, NewGkElectiveConstant.CLASS_TYPE_4);
            newGkDivideClass.setSubjectIdsB(subjectIdsB);
            newGkDivideClass.setClassName(teachClassName + k + "班");
            newGkDivideClass.setOrderId(k);
            insertClassList.add(newGkDivideClass);
            k++;
        }
        int index = 0;
        for (String studentId : stuIds.split(",")) {
            newGkClassStudent = initClassStudent(unitId, divideId, insertClassList.get(index++).getId(), studentId);
            insertStudentList.add(newGkClassStudent);
            if (index == openNum) {
                index = 0;
            }
        }

        try {
            newGkDivideClassService.saveAllList(null, null,
                    null, insertClassList, insertStudentList, false);
        } catch (Exception e) {
            e.printStackTrace();
            return error("分班失败！");
        }
        return success("分班成功！");
    }

    @RequestMapping("/teachClassStudentSet")
    public String teachClassStudentSet(@PathVariable String divideId, String subjectIds, ModelMap map) {
        NewGkDivide divide = newGkDivideService.findOne(divideId);
        String unitId = divide.getUnitId();
        map.put("subjectIds", subjectIds);

        List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.split(",")), Course.class);
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        List<Course> chooseSubject = newGkChoRelationService.findChooseSubject(divide.getChoiceId(), unitId);
        Set<String> choiceSubjectIds = EntityUtils.getSet(chooseSubject, Course::getId);
        Map<String, Course> courseMap = EntityUtils.getMap(chooseSubject, Course::getId);

        StringBuilder shortNames = new StringBuilder();
        courseList.stream().forEach(e -> shortNames.append(e.getShortName()));
        map.put("shortNames", shortNames);

        List<NewGkDivideClass> classList = newGkDivideClassService.findByDivideIdAndClassType(unitId, divideId, null, true,
                NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        // classType = 3
        List<NewGkDivideClass> threeClassList = classList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_3.equals(e.getClassType()))
                .collect(Collectors.toList());
        Set<String> threeClassStudentIdSet = threeClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
        // 一科组合班
        List<NewGkDivideClass> oneClassList = classList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_0.equals(e.getClassType()) && Objects.equals(ONE_COMBINE, e.getSubjectType()))
                .collect(Collectors.toList());
        List<NewGkDivideClass> teachClassList = classList.stream()
                .filter(e -> NewGkElectiveConstant.CLASS_TYPE_4.equals(e.getClassType())
                        && Objects.equals(e.getSubjectIds(), subjectIds))
                .collect(Collectors.toList());
        teachClassList.forEach(e -> {
            if (CollectionUtils.isNotEmpty(e.getStudentList())) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String one : e.getStudentList()) {
                    stringBuilder.append("," + "\"" + one + "\"");
                }
                e.setStuIdStr(stringBuilder.substring(1));
            }
        });
        map.put("independentTeachClassList", teachClassList);

        // 获取选课数据
        Map<String,Set<String>> subjectChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenMap = new HashMap<>();
        Map<String,Set<String>> studentChosenBMap = new HashMap<>();
        makeStudentChooseResult(divide, choiceSubjectIds, subjectChosenMap, studentChosenMap, studentChosenBMap, NewGkElectiveConstant.SUBJECT_TYPE_A);

        Set<String> studentIds;
        if (NewGkElectiveConstant.DIVIDE_TYPE_10.equals(divide.getOpenType())) {
            studentIds = new HashSet<>();
            oneClassList.stream().filter(e -> CollectionUtils.isNotEmpty(e.getStudentList())).forEach(e -> {
                for (String student : e.getStudentList()) {
                    boolean flag = false;
                    for (Course course : courseList) {
                        if (!subjectChosenMap.get(course.getId()).contains(student)) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                    studentIds.add(student);
                }
            });
        } else {
            studentIds = new HashSet<>(CollectionUtils.intersection(subjectChosenMap.get(subjectIds.split(",")[0]), subjectChosenMap.get(subjectIds.split(",")[1])));
        }

        Set<String> teachClassStudentIdSet = teachClassList.stream().flatMap(e -> e.getStudentList().stream()).collect(Collectors.toSet());
        List<Student> students = SUtils.dt(studentRemoteService.findPartStudByGradeId(unitId, null, null, studentIds.toArray(new String[0])), new TR<List<Student>>() {});
        List<StudentResultDto> studentResultDtoList = makeStudentDto(divide, courseList, students, classList, courseMap, studentChosenMap);
        List<StudentResultDto> unSolveStudentList = new ArrayList<>();
        List<StudentResultDto> solveStudentList = new ArrayList<>();
        for (StudentResultDto one : studentResultDtoList) {
            if (teachClassStudentIdSet.contains(one.getStudentId())) {
                solveStudentList.add(one);
            } else {
                unSolveStudentList.add(one);
            }
        }

        map.put("solveStudentList", solveStudentList);
        map.put("unSolveStudentList", unSolveStudentList);
        map.put("courseList", courseList);
        map.put("divide", divide);
        return "/newgkelective/singleRecomb/teachClassStudentSet.ftl";
    }

    @RequestMapping("/schedulingEdit/page")
    public String schedulingEdit(@PathVariable String divideId, String subjectType, String subjectIds, String stuIdStr, ModelMap map) {
        NewGkDivide newDivide = newGkDivideService.findById(divideId);
        if (newDivide == null) {
            return errorFtl(map, "分班方案不存在");
        }
        map.put("divideId", divideId);

        Set<String> subjectIdSet = new HashSet<>();
        String[] subjectIdArr = subjectIds.split(",");
        for (String subject : subjectIdArr) {
            if (StringUtils.isNotBlank(subject)) {
                subjectIdSet.add(subject);
            }
        }

        List<Course> courseList = SUtils.dt(courseRemoteService.findByCodes73(newDivide.getUnitId()), new TR<List<Course>>() {});
        courseList.sort(new Comparator<Course>() {
            @Override
            public int compare(Course o1, Course o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        List<NewGkOpenSubject> openSubList = newGkOpenSubjectService.findByDivideIdAndSubjectTypeInWithMaster(divideId, new String[]{NewGkElectiveConstant.SUBJECT_TYPE_A});
        Set<String> openSubjectIds = EntityUtils.getSet(openSubList, e -> e.getSubjectId());
        Map<String, Course> courseMap = EntityUtils.getMap(courseList, Course::getId);
        String subjectIdsB = courseList.stream().filter(e -> openSubjectIds.contains(e.getId()) && !NewGkElectiveConstant.SUBJRCTCODE_WL_SET.contains(e.getSubjectCode()) && !NewGkElectiveConstant.SUBJRCTCODE_LS_SET.contains(e.getSubjectCode()))
                .filter(e -> !subjectIds.contains(e.getId())).map(e -> e.getId()).collect(Collectors.joining(","));
        map.put("subjectIdsB", subjectIdsB);
        String teachClassName = "";
        for (String subject : subjectIdArr) {
            if (StringUtils.isNotBlank(subject)) {
                teachClassName += courseMap.get(subject).getShortName();
            }
        }

        List<NewGkDivideClass> teachClassList = newGkDivideClassService.findClassBySubjectIdsWithMaster(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, NewGkElectiveConstant.CLASS_TYPE_4, subjectIds, false);
        int k = 1;
        if (CollectionUtils.isNotEmpty(teachClassList)) {
            List<String> teachClassNameList = EntityUtils.getList(teachClassList, NewGkDivideClass::getClassName);
            while (true) {
                if (!teachClassNameList.contains(teachClassName + k + "班")) {
                    break;
                }
                k++;
            }
        }
        map.put("className", teachClassName + k + "班");
        map.put("subjectIds", subjectIds);
        map.put("divideId", divideId);
        map.put("stuIdStr", stuIdStr);
        return "/newgkelective/singleRecomb/schedulingEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/saveClass")
    public String saveGroupClass(@PathVariable String divideId, NewGkDivideClass newGkDivideClass) {
        NewGkDivide newDivide = newGkDivideService.findById(divideId);
        if (newDivide == null) {
            return error("分班方案不存在");
        }
        if (StringUtils.isBlank(newGkDivideClass.getId())) {
            newGkDivideClass.setId(UuidUtils.generateUuid());
        }
        if (StringUtils.isBlank(newGkDivideClass.getSubjectIds())) {
            return error("数据错误！");
        }
        // 验证名字是否重复
        List<NewGkDivideClass> groupClassList = newGkDivideClassService
                .findClassBySubjectIdsWithMaster(newDivide.getUnitId(), divideId, NewGkElectiveConstant.CLASS_SOURCE_TYPE1,
                        newGkDivideClass.getClassType(), newGkDivideClass.getSubjectIds(), false);
        if (CollectionUtils.isNotEmpty(groupClassList)) {
            List<String> groupNameList = EntityUtils.getList(groupClassList, NewGkDivideClass::getClassName);
            if (groupNameList.contains(newGkDivideClass.getClassName())) {
                return error("班级名称重复！");
            }
        }
        List<NewGkClassStudent> insertStudentList = new ArrayList<>();
        NewGkClassStudent teachClassStudent;
        if (StringUtils.isNotBlank(newGkDivideClass.getStuIdStr())) {
            String[] studentIdArr = newGkDivideClass.getStuIdStr().split(",");
            for (String tmp : studentIdArr) {
                teachClassStudent = initClassStudent(newDivide.getUnitId(), divideId, newGkDivideClass.getId(), tmp);
                insertStudentList.add(teachClassStudent);
            }
        }
        Integer orderId = subNum(newGkDivideClass.getClassName());
        if(orderId!=null) {
        	newGkDivideClass.setOrderId(orderId);
        }
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        jsonObject.put("code", "00");
        jsonObject.put("msg", "保存成功");
        jsonObject.put("id", newGkDivideClass.getId());
        jsonObject.put("className", newGkDivideClass.getClassName());
        newGkDivideClass.setDivideId(divideId);
        newGkDivideClass.setCreationTime(new Date());
        newGkDivideClass.setModifyTime(new Date());
        newGkDivideClass.setIsHand(NewGkElectiveConstant.IS_HAND_1);
        newGkDivideClass.setSourceType(NewGkElectiveConstant.CLASS_SOURCE_TYPE1);
        List<NewGkDivideClass> insertClassList = new ArrayList<>();
        insertClassList.add(newGkDivideClass);
        try {
            // 考虑班级学生重复：理论上新增班级 不会出现重复
            newGkDivideClassService.saveAllList(null, null,
                    null, insertClassList, insertStudentList, false);
        } catch (Exception e) {
            e.printStackTrace();
            return error("保存失败");
        }
        return jsonObject.toString();
    }


    private void makeScoreByOpenBasic(List<StudentResultDto> list, String subjectIds, boolean isContatinYsy) {
        for (StudentResultDto result : list) {
            // score切换成按分班要求的参数
            float score = 0.0f;
            Map<String, Float> scoreMap = result.getSubjectScore();
            for (Map.Entry<String, Float> item : scoreMap.entrySet()) {
                if (subjectIds.indexOf(item.getKey()) > -1) {
                    if (item.getValue() != null) {
                        score = score + item.getValue();
                    }
                } else if (isContatinYsy && NewGkElectiveConstant.YSY_SUBID.equals(item.getKey())) {
                    if (item.getValue() != null) {
                        score = score + item.getValue();
                    }
                }
            }
            result.setScore(score);
        }
    }

    private void makeClassesBySort(List<StudentResultDto> leftList,
                                   String divideId, String unitId, int index, String groupName,
                                   NewGkQuickSaveDto saveDto,
                                   List<NewGkDivideClass> insertClassList, List<NewGkClassStudent> insertStuList) {
        int classNum = saveDto.getClassNum();
        int stuNum = saveDto.getArrangeStudentNum();
        List<StudentResultDto> moveList;
        if (stuNum <= leftList.size()) {
            moveList = leftList.subList(0, stuNum);
        } else {
            moveList = leftList;
        }

        int n1 = moveList.size() / classNum;
        int n2 = moveList.size() % classNum;
        if (n1 == 0) {
            //直接安排一个班 moveList
            NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_4);
            clazz.setSubjectIdsB(saveDto.getSubjectIdsB());
            clazz.setClassName(groupName + index + "班");
            clazz.setOrderId(index);
            insertClassList.add(clazz);
            index++;
            for (StudentResultDto dd : moveList) {
                NewGkClassStudent ss = initClassStudent(unitId, divideId, clazz.getId(), dd.getStudentId());
                insertStuList.add(ss);
            }
        } else {
            int start = 0;
            for (int i = 0; i < classNum; i++) {
                List<StudentResultDto> chooseList;
                if (n2 > 0) {
                    chooseList = moveList.subList(start, start + n1 + 1);
                    start = start + n1 + 1;
                    n2--;

                } else {
                    chooseList = moveList.subList(start, start + n1);
                    start = start + n1;
                    n2--;
                }
                //新增一个班
                NewGkDivideClass clazz = initNewGkDivideClass(divideId, saveDto.getSubjectIds(), NewGkElectiveConstant.CLASS_TYPE_4);
                clazz.setSubjectIdsB(saveDto.getSubjectIdsB());
                clazz.setClassName(groupName + index + "班");
                clazz.setOrderId(index);
                insertClassList.add(clazz);
                index++;
                for (StudentResultDto dd : chooseList) {
                    NewGkClassStudent ss = initClassStudent(unitId, divideId, clazz.getId(), dd.getStudentId());
                    insertStuList.add(ss);
                }
            }
        }
    }

    /**
     * 使用分班时创建的 选课备份数据 而不是 实时的 选课数据 , 只有 studentId 和 subjectId
     * @param divide
     * @param studentIds
     * @return
     */
    private List<NewGkChoResult> makeChosenByBackUp(NewGkDivide divide, List<String> studentIds) {
        List<NewGkChoResult> choseResults = new ArrayList<>();
        List<NewGkDivideStusub> stuSubList;
        if (studentIds == null) {
            stuSubList = newGkDivideStusubService.findByDivideIdWithMaster(divide.getId(),
                    NewGkElectiveConstant.SUBJECT_TYPE_A, null);
        } else {
            stuSubList = newGkDivideStusubService.findListByStudentIdsWithMaster(divide.getId(),
                    NewGkElectiveConstant.SUBJECT_TYPE_A, studentIds.toArray(new String[0]));
        }
        if (CollectionUtils.isEmpty(stuSubList)) {
            return choseResults;
        }
        NewGkChoResult cho;
        for (NewGkDivideStusub stuSub : stuSubList) {
            String studentId = stuSub.getStudentId();
            String subjectIds = stuSub.getSubjectIds();
            if (StringUtils.isBlank(subjectIds)) {
                continue;
            }
            String[] subIdArr = subjectIds.split(",");
            if (subIdArr.length == 0) {
                continue;
            }
            for (String subId : subIdArr) {
                cho = new NewGkChoResult();
                cho.setStudentId(studentId);
                cho.setSubjectId(subId);
                choseResults.add(cho);
            }
        }
        return choseResults;
    }

    /**
     * @param divide
     * @param subjectChosenMap K:科目 V：学生
     * @param studentChosenMap K:学生 V：科目
     */
    private void makeStudentChooseResult(NewGkDivide divide, Set<String> openSubjectIdSet, Map<String,Set<String>> subjectChosenMap, Map<String,Set<String>> studentChosenMap, Map<String,Set<String>> studentChosenBMap, String subjectType) {
        List<NewGkChoResult> chosenList = makeChosenByBackUp(divide, null);

        for (NewGkChoResult result : chosenList) {
            if (NewGkElectiveConstant.SUBJECT_TYPE_A.equals(subjectType)) {
                if(!subjectChosenMap.containsKey(result.getSubjectId())) {
                    subjectChosenMap.put(result.getSubjectId(), new TreeSet<>());
                }
                subjectChosenMap.get(result.getSubjectId()).add(result.getStudentId());
            }
            if(!studentChosenMap.containsKey(result.getStudentId())) {
                studentChosenMap.put(result.getStudentId(), new TreeSet<>());
            }
            studentChosenMap.get(result.getStudentId()).add(result.getSubjectId());
        }
        if (NewGkElectiveConstant.SUBJECT_TYPE_B.equals(subjectType)) {
            // 取补集
            for (String student : studentChosenMap.keySet()) {
                studentChosenBMap.put(student, new TreeSet<>(CollectionUtils.subtract(openSubjectIdSet, studentChosenMap.get(student))));
            }
            for (Map.Entry<String, Set<String>> entry : studentChosenBMap.entrySet()) {
                for (String subject : entry.getValue()) {
                    if (subjectChosenMap.get(subject) == null) {
                        subjectChosenMap.put(subject, new TreeSet<>());
                    }
                    subjectChosenMap.get(subject).add(entry.getKey());
                }
            }
        }
    }

    private List<StudentResultDto> makeStudentDto(NewGkDivide divide, List<Course> courseList, List<Student> studentList,
                                                  List<NewGkDivideClass> classList, Map<String, Course> courseMap, Map<String, Set<String>> studentChosenMap) {

        // 学生 -> 班级
        Map<String, NewGkDivideClass> studentClassMap = new HashMap<>();
        if (classList != null) {
            for (NewGkDivideClass clazz : classList) {
                if (NewGkElectiveConstant.CLASS_TYPE_1.equals(clazz.getClassType())) {
                    clazz.getStudentList().forEach(e -> studentClassMap.put(e, clazz));
                }
            }
        }

        List<Course> ysyCourses = SUtils.dt(courseRemoteService.findByCodesYSY(getLoginInfo().getUnitId()),Course.class);

        // 成绩
        String mcodeId = ColumnInfoUtils.getColumnInfo(Student.class, "sex").getMcodeId();
        Map<String, McodeDetail> codeMap = SUtils.dt(mcodeRemoteService.findMapByMcodeId(mcodeId), new TypeReference<Map<String, McodeDetail>>() {});
        Map<String, Map<String, Float>> stuScoreMap = getScoreMap(divide);
        List<StudentResultDto> dtoList = new ArrayList<>();
        StudentResultDto dto;
        for (Student stu : studentList) {
            dto = new StudentResultDto();
            dto.setStudentId(stu.getId());
            dto.setStudentCode(stu.getStudentCode());
            dto.setStudentName(stu.getStudentName());
            if (stu.getSex() != null) {
                dto.setSex(codeMap.get(stu.getSex() + "").getMcodeContent());
            }else {
                dto.setSex("未知性别");
            }

            Map<String, Float> scoreMap = stuScoreMap.get(stu.getId());
            Float scoreTmp;
            Float ysyScore = Float.valueOf(0.0f);
            Float totalScore = Float.valueOf(0.0f);
            if (scoreMap == null) {
                scoreMap = new HashMap<>();
            }
            for (Course one : ysyCourses) {
                scoreTmp = scoreMap.get(one.getId());
                ysyScore += (scoreTmp == null ? Float.valueOf(0.0f) : scoreTmp);
            }
            totalScore += ysyScore;
            dto.getSubjectScore().put("YSY", ysyScore);
            if (courseList != null) {
                for (Course one : courseList) {
                    dto.getSubjectScore().put(one.getId(), scoreMap.get(one.getId()));
                    totalScore += scoreMap.get(one.getId()) == null ? 0.0f : scoreMap.get(one.getId());
                }
            }

            dto.getSubjectScore().put("TOTAL", totalScore);

            NewGkDivideClass divideClass = studentClassMap.get(stu.getId());
            if (divideClass == null || StringUtils.isBlank(divideClass.getClassName())) {
                dto.setClassName("未知");
            } else {
                dto.setClassName(studentClassMap.get(stu.getId()).getClassName());
            }
            if (studentChosenMap != null && studentChosenMap.size() > 0 && studentChosenMap.get(stu.getId()) != null) {
                String chosenName = studentChosenMap.get(stu.getId()).stream().map(e -> courseMap.get(e).getShortName()).collect(Collectors.joining(""));
                dto.setChooseSubjects(chosenName);
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private Map<String, Map<String, Float>> getScoreMap(NewGkDivide divide) {
        List<NewGkScoreResult> scoreList = newGkScoreResultService.findListByReferScoreId(divide.getUnitId(), divide.getReferScoreId(), true);
        Map<String, Map<String, Float>> stuScoreMap = new HashMap<>();
        for (NewGkScoreResult one : scoreList) {
            String studentId = one.getStudentId();
            if (!stuScoreMap.containsKey(one.getStudentId())) {
                stuScoreMap.put(one.getStudentId(), new HashMap<>());
            }
            stuScoreMap.get(studentId).put(one.getSubjectId(), one.getScore());
        }
        return stuScoreMap;
    }
    
    @ResponseBody
	@RequestMapping("/finshComDivide")
	@ControllerInfo(value = "完成分班")
	public String finshComDivide(@PathVariable String divideId) {
    	NewGkDivide gkDivide = newGkDivideService.findById(divideId);
    	List<NewGkDivideStusub> stuchooseList = newGkDivideStusubService.findByDivideIdWithMaster(divideId, NewGkElectiveConstant.SUBJECT_TYPE_A, null);
		Map<String, NewGkDivideStusub> subsByStuId = EntityUtils.getMap(stuchooseList, e->e.getStudentId());
		List<String> subjectIdList = newGkChoRelationService.findByChoiceIdAndObjectType(gkDivide.getUnitId(),gkDivide.getChoiceId(), NewGkElectiveConstant.CHOICE_TYPE_01);
		if(CollectionUtils.isEmpty(subjectIdList)){
			return error("选课的科目记录不存在或已被删除！");
		}
		List<Course> courselist = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIdList.toArray(new String[] {})),Course.class);
		Map<String, Course> courseMap = EntityUtils.getMap(courselist, e->e.getId());
		
		List<NewGkDivideClass> teachClassList = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_2}, false, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
        String[] oldTeachClassIds=null;
		if (CollectionUtils.isNotEmpty(teachClassList)) {
            oldTeachClassIds = EntityUtils.getArray(teachClassList, NewGkDivideClass::getId, String[]::new);
        }
		if(NewGkElectiveConstant.DIVIDE_TYPE_10.equals(gkDivide.getOpenType())) {
    		List<NewGkDivideClass> oldlist = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), new String[] {NewGkElectiveConstant.CLASS_TYPE_0,NewGkElectiveConstant.CLASS_TYPE_4}, true, NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
    		if(CollectionUtils.isEmpty(oldlist)) {
    			return error("未找到物理历史班级");
    		}
    		List<NewGkDivideClass> list0 = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_0)).collect(Collectors.toList());
    		if(CollectionUtils.isEmpty(list0)) {
    			return error("未找到组合班");
    		}
    		List<NewGkDivideClass> list4 = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
    		if(CollectionUtils.isEmpty(list4)) {
    			return error("未找到二科组合班");
    		}
    		Map<String,NewGkDivideClass> stuByClass=new HashMap<String, NewGkDivideClass>();
    		Set<String> subIds=new HashSet<>();
    		for(NewGkDivideClass n:list4) {
    			if(CollectionUtils.isEmpty(n.getStudentList())) {
    				return error("存在二科组合班人数为0");
    			}
    			for(String s:n.getStudentList()) {
    				if(stuByClass.containsKey(s)) {
    					return error("存在学生位于两个二科组合班");
    				}
    				stuByClass.put(s, n);
    			}
    		}
    		for(NewGkDivideClass s:list0) {
    			if(NewGkElectiveConstant.SUBJECT_TYPE_3.equals(s.getSubjectType())) {
    				continue;
    			}
    			for(String ss:s.getStudentList()) {
    				if(!subsByStuId.containsKey(ss)) {
    					return error("存在学生没有二科组合班");
    				}
    				if(!stuByClass.containsKey(ss)) {
    					return error(s.getClassName()+"班级下存在学生安排二科组合班");
    				}
    				String[] arr = stuByClass.get(ss).getSubjectIds().split(",");
					for(String a:arr) {
						if(StringUtils.isNotBlank(a) && subsByStuId.get(ss).getSubjectIds().indexOf(a)==-1) {
							return error("学生"+subsByStuId.get(ss).getStudentName()+"不能安排在"+stuByClass.get(ss).getClassName()+"班级内");
						}
					}
    			}
    		}
    		List<NewGkDivideClass> insertClassList = new ArrayList<>();
 	        List<NewGkClassStudent> insertStudentList = new ArrayList<>();
    		
 	        String ss=initFourClass(divideId, gkDivide.getUnitId(), courseMap, list4, insertClassList, insertStudentList);
    		if(StringUtils.isNotBlank(ss)) {
    			return error(ss);
    		}
    		
		   try {
	            newGkDivideClassService.saveAllList(gkDivide.getUnitId(), divideId,
	                    oldTeachClassIds, insertClassList, insertStudentList, false);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return error(e.getMessage());
	        }
    		 
    	}else {
    		List<NewGkDivideClass> oldlist = newGkDivideClassService.findByDivideIdAndClassType(gkDivide.getUnitId(), gkDivide.getId(), 
    				new String[] {NewGkElectiveConstant.CLASS_TYPE_1,NewGkElectiveConstant.CLASS_TYPE_3,NewGkElectiveConstant.CLASS_TYPE_4}, true, 
    				NewGkElectiveConstant.CLASS_SOURCE_TYPE1, false);
    		if(CollectionUtils.isEmpty(oldlist)) {
    			return error("未找到开设班级");
    		}
    		Map<String, NewGkDivideClass> xzbMap = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_1)).collect(Collectors.toMap(NewGkDivideClass::getId, e->e));
    		List<NewGkDivideClass> list3 = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_3)).collect(Collectors.toList());
    		
    		if(CollectionUtils.isEmpty(list3)) {
    			return error("未找到物理历史班级");
    		}
    		List<NewGkDivideClass> list4 = oldlist.stream().filter(e->e.getClassType().equals(NewGkElectiveConstant.CLASS_TYPE_4)).collect(Collectors.toList());
    		if(CollectionUtils.isEmpty(list4)) {
    			return error("未找到二科组合班");
    		}
    		Map<String,NewGkDivideClass> stuByClass=new HashMap<String, NewGkDivideClass>();
    		for(NewGkDivideClass n:list4) {
    			if(CollectionUtils.isEmpty(n.getStudentList())) {
    				return error("存在二科组合班人数为0");
    			}
    			for(String s:n.getStudentList()) {
    				if(stuByClass.containsKey(s)) {
    					return error("存在学生位于两个二科组合班");
    				}
    				stuByClass.put(s, n);
    			}
    		}
    		
    		List<NewGkClassStudent> insertStuList=new ArrayList<>();
    		//list3 下学生id
    		Set<String> dels=new HashSet<>();
    		int size=0;
    		for(NewGkDivideClass s:list3) {
    			s.getStudentList().clear();
    			dels.add(s.getId());
    			List<String> stuIds=new ArrayList<>();
    			String[] relaIds = s.getRelateId().split(",");
    			for(String sr:relaIds) {
    				NewGkDivideClass oldXzb = xzbMap.get(sr);
    				if(oldXzb!=null && CollectionUtils.isNotEmpty(oldXzb.getStudentList())) {
    					for(String ss:oldXzb.getStudentList()) {
    						if(!subsByStuId.containsKey(ss)) {
    							//学生未选
    							continue;
    						}
    						if(subsByStuId.get(ss).getSubjectIds().indexOf(s.getSubjectIds())>-1) {
    							stuIds.add(ss);
    							NewGkClassStudent su = initClassStudent(gkDivide.getUnitId(), divideId, s.getId(), ss);
    							insertStuList.add(su);
    							s.getStudentList().add(su.getStudentId());
    							//TODO 
    							if(!stuByClass.containsKey(ss)) {
    								return error("学生"+subsByStuId.get(ss).getStudentName()+"未找到对应二科组合班");
    							}
    							String[] arr = stuByClass.get(ss).getSubjectIds().split(",");
    							for(String a:arr) {
    								if(StringUtils.isNotBlank(a) && subsByStuId.get(ss).getSubjectIds().indexOf(a)==-1) {
    									return error("学生"+subsByStuId.get(ss).getStudentName()+"不能安排在"+stuByClass.get(ss).getClassName()+"班级内");
    								}
    							}
    						}
    					}
    				}
    			}
    			if(CollectionUtils.isEmpty(stuIds)) {
    				return error(s.getClassName()+"下没有学生");
    			}
    			size=size+stuIds.size();
    			
    		}
    		if(stuchooseList.size()!=size) {
    			return error("存在学生选课数据不是3+1+2模式，或者未安排物理或者历史");
    		}
    		List<NewGkDivideClass> insertClassList = new ArrayList<>();
 	        List<NewGkClassStudent> insertStudentList = new ArrayList<>();
    		
 	        String ss=initFourClass(divideId, gkDivide.getUnitId(), courseMap, list4, insertClassList, insertStudentList);
    		if(StringUtils.isNotBlank(ss)) {
    			return error(ss);
    		}
    		ss = initTwoClass(gkDivide, courseMap, oldlist, insertClassList, insertStudentList);
//    		int ui = 1/0;
    		if(StringUtils.isNotBlank(ss)) {
    			return error(ss);
    		}
    		
    		
    		//分两步保存问题不大
    		try{
    			newGkClassStudentService.saveOrSaveList(gkDivide.getUnitId(), divideId, dels.toArray(new String[0]), insertStuList);
    		}catch(Exception e){
    			return error("数据验证成功，保存学生数据失败");
    		}
    		
    		try {
	            newGkDivideClassService.saveAllList(gkDivide.getUnitId(), divideId,
	                    oldTeachClassIds, insertClassList, insertStudentList, false);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return error(e.getMessage());
	        }
    	}
    	try{
    		newGkDivideService.updateStat(divideId, NewGkElectiveConstant.IF_1);
    	}catch(Exception e){
			return error("修改分班状态失败");
		}
		return success("");
	}
    
    
    public String initFourClass(String divideId,String unitId,Map<String,Course> courseMap, List<NewGkDivideClass> list4,List<NewGkDivideClass> insertClassList,List<NewGkClassStudent> insertStudentList) {
        NewGkDivideClass divideClassTmp;
        for (NewGkDivideClass one : list4) {
            if (one.getSubjectIds() != null && one.getSubjectIds().split(",").length == 2) {
                for (String subjectId : one.getSubjectIds().split(",")) {
                    divideClassTmp = initNewGkDivideClass(divideId, subjectId, NewGkElectiveConstant.CLASS_TYPE_2);
                    divideClassTmp.setRelateId(one.getId());
                    divideClassTmp.setClassName(one.getClassName() + "-" + courseMap.get(subjectId).getSubjectName());
                    divideClassTmp.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_A);
                    insertClassList.add(divideClassTmp);
                    if (CollectionUtils.isNotEmpty(one.getStudentList())) {
                        for (String student : one.getStudentList()) {
                            insertStudentList.add(initClassStudent(unitId, divideId, divideClassTmp.getId(), student));
                        }
                    }
                }
            } else {
                return error("两科组合班数据错误");
            }
            if (one.getSubjectIdsB() != null && one.getSubjectIdsB().split(",").length == 2) {
                for (String subjectId : one.getSubjectIdsB().split(",")) {
                    divideClassTmp = initNewGkDivideClass(divideId, subjectId, NewGkElectiveConstant.CLASS_TYPE_2);
                    divideClassTmp.setRelateId(one.getId());
                    divideClassTmp.setClassName(one.getClassName() + "-" + courseMap.get(subjectId).getSubjectName());
                    divideClassTmp.setSubjectType(NewGkElectiveConstant.SUBJECT_TYPE_B);
                    insertClassList.add(divideClassTmp);
                    if (CollectionUtils.isNotEmpty(one.getStudentList())) {
                        for (String student : one.getStudentList()) {
                            insertStudentList.add(initClassStudent(unitId, divideId, divideClassTmp.getId(), student));
                        }
                    }
                }
            } else {
                return "两科组合班数据错误";
            }
        }
        return null;
    }
	
    
}
