package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 学生综合素质查询
 */
@RestController
@RequestMapping("/diathesis/student")
public class DiathesisQueryStudentAction extends BaseAction {

    @Autowired
    private DiathesisSetSubjectService diathesisSetSubjectService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private DiathesisScoreTypeService diathesisScoreTypeService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private DiathesisRecordService diathesisRecordService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisRecordInfoService diathesisRecordInfoService;
    @Autowired
    private DiathesisStructureService diathesisStructureService;
    @Autowired
    private FamilyRemoteService familyRemoteService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private DiathesisOptionService diathesisOptionService;
    @Autowired
    private DiathesisEvaluateService diathesisEvaluateService;
    @Autowired
    private DiathesisRecordSetService diathesisRecordSetService;
    @Autowired
    private DiathesisSubjectFieldService diathesisSubjectFieldService;
    @Autowired
    private DiathesisScoreInfoExService diathesisScoreInfoExService;

    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private FilePathRemoteService filePathRemoteService;
    @Autowired
    private CourseTypeRemoteService courseTypeRemoteService;

    /**
     * 查询学生
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/studentList")
    public String findStudentList(String type, String typeValue) {
        String unitId = getLoginInfo().getUnitId();
        //查询类型 1：姓名 2：学号
        List<Student> studentList = new ArrayList<>();
        if ("1".equals(type)) {
            studentList = SUtils.dt(studentRemoteService.findBySchoolIdStudentNameStudentCode(unitId, typeValue, null), new TR<List<Student>>() {
            });
        } else if ("2".equals(type)) {
            studentList = SUtils.dt(studentRemoteService.findBySchoolIdStudentNameStudentCode(unitId, null, typeValue), new TR<List<Student>>() {
            });
            ;
        }
        JSONArray data = new JSONArray();
        JSONObject json1 = null;
        if (CollectionUtils.isNotEmpty(studentList)) {
            for (Student s : studentList) {
                json1 = new JSONObject();
                json1.put("id", s.getId());
                json1.put("studentName", s.getStudentName());
                data.add(json1);
            }
        }
        return data.toString();
    }

    /**
     * 获取学生个人报表数据
     *
     * @param student
     * @return
     */
    public JSONObject findStuReportItem(Student student) {
        //根据学生所在的学段---目前只显示学生所在的学段所有学期数据
        //微代码
        JSONObject obj = new JSONObject();
        Unit unit = SUtils.dc(unitRemoteService.findOneById(student.getSchoolId()), Unit.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        String sectionMcodeKey = "DM-RKXD-" + grade.getSection();
        String[] mcodeIds = new String[]{"DM-XB", "DM-MZ", sectionMcodeKey};
        Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(mcodeIds), new TR<Map<String, Map<String, McodeDetail>>>() {
        });


        DiatheisStudentDto stuDto = new DiatheisStudentDto();
        String bd = DateUtils.date2StringByDay(student.getBirthday());
        stuDto.setBirthDate(bd == null ? "" : bd);
        stuDto.setClassName(grade.getGradeName() + clazz.getClassName());
        stuDto.setIdCard(student.getIdentityCard());
        stuDto.setNation(mcodeMap.get("DM-MZ") == null ? "" : (mcodeMap.get("DM-MZ").get(student.getNation()) == null ? "" : mcodeMap.get("DM-MZ").get(student.getNation()).getMcodeContent()));
        stuDto.setSchoolName(unit.getUnitName());
        if (student.getSex() != null) {
            stuDto.setSex(mcodeMap.get("DM-XB") == null ? "" : (mcodeMap.get("DM-XB").get(student.getSex().toString()) == null ? "" : mcodeMap.get("DM-XB").get(student.getSex().toString()).getMcodeContent()));
        } else {
            stuDto.setSex("");
        }
        stuDto.setStuCode(student.getStudentCode());
        stuDto.setStudentName(student.getStudentName());
        obj.put("student", stuDto);

        String unitId = unit.getId();

        List<DiathesisScoreInfo> scoreInfoList = diathesisScoreInfoService.findByUnitIdAndStudentId(unitId, student.getId());
        //List<DiathesisScoreInfoEx> scoreInfoExList = diathesisScoreInfoExService.findListByInfoIdIn(EntityUtils.getArray(scoreInfoList, x -> x.getId(), String[]::new));
        Set<String> subjectIds = new HashSet<>();

        //统一所有年级
        JSONArray timedata = new JSONArray();
        List<Object> timedataResult = new JSONArray();

        JSONObject json1 = null;
        //时间
        //学段map
        Map<String, McodeDetail> xdMap = new HashMap<>();
        if (mcodeMap.containsKey(sectionMcodeKey)) {
            for (Entry<String, McodeDetail> item : mcodeMap.get(sectionMcodeKey).entrySet()) {
                xdMap.put(grade.getSection() + item.getValue().getThisId(), item.getValue());
            }
        }
        List<String> timeStringList = new ArrayList<>();//所有时间
        for (int i = 1; i <= grade.getSchoolingLength(); i++) {
            String mm = grade.getSection() + "" + i;
            if (!xdMap.containsKey(mm)) {
                continue;
            }
            json1 = new JSONObject();
            String s = mm + "1";
            json1.put("timeId", s);

            String ss1 = xdMap.get(s.substring(0, 2)).getMcodeContent();
            json1.put("timeText", ss1 + "上");
            timeStringList.add(s);
            timedata.add(json1);
            s = grade.getSection() + "" + i + "2";
            if (!xdMap.containsKey(s.substring(0, 2))) {
                continue;
            }
            json1 = new JSONObject();
            json1.put("timeId", s);
            ss1 = xdMap.get(s.substring(0, 2)).getMcodeContent();
            json1.put("timeText", ss1 + "下");
            timeStringList.add(s);
            timedata.add(json1);
        }
        if (grade.getSection().equals(1)) {
            timeStringList = timeStringList.subList(0, 12);
            timedataResult = timedata.subList(0, 12);
        } else {
            timeStringList = timeStringList.subList(0, 6);
            timedataResult = timedata.subList(0, 6);
        }
        obj.put("timeList", JSONArray.parseArray(JSON.toJSONString(timedataResult)));

        //1、学业水平 key:subjectId,value:score-----取最大值--只能输入等第ABCD
        Map<String, String> score_xy = new HashMap<>();
        //Map<String,Float> score_xy=new HashMap<>();
        Set<String> scoreTypeId_xy = new HashSet<>();
        List<Json> data2 = new ArrayList<>();

        //2、学科成绩
        //data3必修 data4 选修
        List<List<String>> data3 = new ArrayList<>();
        List<List<String>> data4 = new ArrayList<>();
        //每个学期成绩
        //必修  key:scoreTypeId value：学年学期
        Map<String, String> scoreTypeId_xk_1 = new HashMap<>();

        String key_xk = null;
        //选修 key:scoreTypeId value：学年学期
        Map<String, String> scoreTypeId_xk_2 = new HashMap<>();

        //3:综合素质统计后数据
        //key:scoreTypeId value：学年学期
        Map<String, String> scoreTypeId_xk_4 = new HashMap<>();
        //key:331-scoreTypeId value:成绩
        Map<String, String> scoreStatMap = new HashMap<>();


        //时间
//		List<String> timeString=new ArrayList<>();
        DiatheisSubjectScoreDto subjectScoreDto = null;
        if (CollectionUtils.isNotEmpty(scoreInfoList)) {
            Set<String> ids = EntityUtils.getSet(scoreInfoList, e -> e.getScoreTypeId());

            List<DiathesisScoreType> scoreTypeList = diathesisScoreTypeService.findListByIdIn(ids.toArray(new String[]{}));
            if (CollectionUtils.isNotEmpty(scoreTypeList)) {
                for (DiathesisScoreType item : scoreTypeList) {
                    if (DiathesisConstant.INPUT_TYPE_1.equals(item.getType())) {
                        //1:学科成绩录入
                        key_xk = item.getGradeCode() + item.getSemester();
                        if (!timeStringList.contains(key_xk)) {
                            //不是该学段数据
                            continue;
                        }
                        if (DiathesisConstant.SCORE_TYPE_1.equals(item.getScoreType())) {
                            //必修
                            scoreTypeId_xk_1.put(item.getId(), key_xk);
                        } else if (DiathesisConstant.SCORE_TYPE_2.equals(item.getScoreType())) {
                            scoreTypeId_xk_2.put(item.getId(), key_xk);
                        }

                    } else if (DiathesisConstant.INPUT_TYPE_2.equals(item.getType())) {
                        //2:学业水平录入 ---默认取最大值
                        scoreTypeId_xy.add(item.getId());
                    } else if (DiathesisConstant.INPUT_TYPE_4.equals(item.getType())) {
                        //3:综合素质总分
                        key_xk = item.getGradeCode() + item.getSemester();
                        if (!timeStringList.contains(key_xk)) {
                            //不是该学段数据
                            continue;
                        }
                        scoreTypeId_xk_4.put(item.getId(), key_xk);
                    }
                }
            }

            for (DiathesisScoreInfo info : scoreInfoList) {
                if (scoreTypeId_xy.contains(info.getScoreTypeId())) {
                    subjectIds.add(info.getObjId());
                    //学业水平---ABCD 取小的值
                    String oldScore = score_xy.get(info.getObjId());
                    if (StringUtils.isNotBlank(info.getScore())) {
                        if (oldScore == null) {
                            score_xy.put(info.getObjId(), info.getScore());
                        } else {
                            if (info.getScore().compareToIgnoreCase(oldScore) < 0) {
                                score_xy.put(info.getObjId(), info.getScore());
                            }
                        }
                    }


//					Float oldScore = score_xy.get(info.getObjId());
//
//					if(oldScore==null) {
//						score_xy.put(info.getObjId(), Float.parseFloat(info.getScore()));
//					}else {
//						if(Float.parseFloat(info.getScore())>oldScore) {
//							score_xy.put(info.getObjId(), Float.parseFloat(info.getScore()));
//						}
//					}

                } else if (scoreTypeId_xk_1.containsKey(info.getScoreTypeId())) {
                    //必修
					/*subjectIds.add(info.getObjId());
					key_xk=scoreTypeId_xk_1.get(info.getScoreTypeId());
					subjectScoreDto=new DiatheisSubjectScoreDto();
					subjectScoreDto.setSubjectId(info.getObjId());
					subjectScoreDto.setScore(info.getScore());
					subjectScoreDto.setTimeId(key_xk);
					data3.add(subjectScoreDto);*/
                } else if (scoreTypeId_xk_2.containsKey(info.getScoreTypeId())) {
                    //选修
					/*subjectIds.add(info.getObjId());
					key_xk=scoreTypeId_xk_2.get(info.getScoreTypeId());
					subjectScoreDto=new DiatheisSubjectScoreDto();
					subjectScoreDto.setSubjectId(info.getObjId());
					subjectScoreDto.setScore(info.getScore());
					subjectScoreDto.setTimeId(key_xk);
					data4.add(subjectScoreDto);*/
                } else if (scoreTypeId_xk_4.containsKey(info.getScoreTypeId())) {
                    //综合素质部分
                    String mm = scoreTypeId_xk_4.get(info.getScoreTypeId()) + "-" + info.getObjId();
                    scoreStatMap.put(mm, info.getScore());
                }
            }

        }


        //写实记录
        List<DiathesisRecord> recordList = diathesisRecordService.findListByStuId(unitId, student.getId(), DiathesisConstant.AUDIT_STATUS_PASS);

        //拿到该学校所有项目
        //String usingUnitId1 = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        List<DiathesisProject> list = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, null);

        //v1.2.0 如果学校以前是自主设置一级类目, 之后又被教育局收回权限的, 要把历史项目也展现出来

		/*List<String> projectIdscheck = EntityUtils.getList(list, x -> x.getId());
		List<DiathesisProject> mytopList = diathesisProjectService.findMyProjectByUnitAndTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP});
		for (DiathesisProject top : mytopList) {
			if(projectIdscheck.contains(top.getId()));
			list.addAll(mytopList);
			break;
		}*/

        Map<String, DiathesisProject> projectMap = list.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));

        DiathesisStuRecordDto recodeDto = null;
        List<DiathesisStuRecordDto> data5 = new ArrayList<>();
        String timeId = null;
        //Map<String,Integer> data5scoreMap=new HashMap<>();
        if (CollectionUtils.isNotEmpty(recordList)) {
            //写实记录下的细化
            Set<String> recordProjectIds = EntityUtils.getSet(recordList, e -> e.getProjectId());
            List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectIdIn(recordProjectIds.toArray(new String[]{}));
            //key:projectId
            Map<String, List<DiathesisStructure>> structureMap = EntityUtils.getListMap(structureList, DiathesisStructure::getProjectId, e -> e);
            //选项Map
            Map<String, String> optionMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(structureList)) {
                Set<String> ids = EntityUtils.getSet(structureList, e -> e.getId());
                String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
                optionMap = diathesisOptionService.findMapByUnitIdAndStructureIdIn(usingUnitId, ids.toArray(new String[]{}));
            }


            Set<String> recordIds = EntityUtils.getSet(recordList, e -> e.getId());
            List<DiathesisRecordInfo> recodeInfoList = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, recordIds.toArray(new String[]{}));

            //key:recordId
            Map<String, List<DiathesisRecordInfo>> recodeInfoMap = EntityUtils.getListMap(recodeInfoList, DiathesisRecordInfo::getRecordId, e -> e);
            List<DiathesisStructure> strucList = null;
            List<DiathesisRecordInfo> infoList = null;
            Map<String, DiathesisRecordInfo> infoMap = null;
            DiathesisRecordInfo con = null;
            //获得分数

            List<DiathesisRecordSet> recordSet = diathesisRecordSetService.findListByIdIn(recordProjectIds.toArray(new String[0]));

            //projectId  type    0:按次数加分    1: 按单选加
            Map<String, DiathesisRecordSet> scoreTypeMap = EntityUtils.getMap(recordSet, x -> x.getId(), x -> x);
            List<DiathesisOption> optionList = diathesisOptionService.findListByStructureIdIn(EntityUtils.getArray(structureList, e -> e.getId(), String[]::new));
            Map<String, String> optionScoreMap = EntityUtils.getMap(optionList, x -> x.getId(), x -> StringUtils.defaultString(x.getScore(), ""));
            for (DiathesisRecord record : recordList) {
                timeId = record.getGradeCode() + record.getSemester();
                if (!timeStringList.contains(timeId)) {
                    continue;
                }
                strucList = structureMap.get(record.getProjectId());
                if (CollectionUtils.isEmpty(strucList)) {
                    //自定义参数不存在
                    continue;
                }
                infoList = recodeInfoMap.get(record.getId());
                if (CollectionUtils.isEmpty(infoList)) {
                    //自定义参数没有维护值
                    continue;
                }
                recodeDto = new DiathesisStuRecordDto();
                DiathesisProject project = projectMap.get(record.getProjectId());
                if (project == null) {
                    //项目不存在
                    continue;
                }
                recodeDto.setProjectSort(project.getSortNumber() == null ? 0 : project.getSortNumber());
                if (DiathesisConstant.PROJECT_TOP.equals(project.getProjectType())) {
                    //直接挂在顶级
                    recodeDto.setTypeName(project.getProjectName());
                    recodeDto.setProjectName("未分类");
                    recodeDto.setTypeSort(project.getSortNumber() == null ? 0 : project.getSortNumber());
                } else {
                    recodeDto.setProjectName(project.getProjectName());
                    DiathesisProject topproject = projectMap.get(project.getParentId());
                    if (topproject == null) {
                        continue;
                    }
                    recodeDto.setTypeName(topproject.getProjectName());
                    recodeDto.setTypeSort(topproject.getSortNumber() == null ? 0 : topproject.getSortNumber());
                }


                DiathesisRecordSet setType = scoreTypeMap.get(record.getProjectId());// 0:按次数加分    1: 按单选加

                // 0:按次数加分    1: 按单选加
                int score = 0;
                if ("0".equals(setType.getScoreType())) {
                    score = Integer.parseInt(setType.getScore());
                } else {
                    String scoreStructureId = setType.getScoreStructureId();
                    if (StringUtils.isNotBlank(scoreStructureId)) {
                        //List<DiathesisOption> optionList1 = optionSetMap.get(scoreStructureId);
                        List<DiathesisRecordInfo> recordInfoList = recodeInfoMap.get(record.getId());
                        for (DiathesisRecordInfo x : recordInfoList) {
                            //找到单选的那个选项
                            if (scoreStructureId.equals(x.getStructureId())) {
                                score = Integer.parseInt(optionScoreMap.get(x.getContentTxt()));
                            }
                        }
                    }

                }
                recodeDto.setScore("" + score);
//				if(!timeString.contains(timeId)) {
//					timeString.add(timeId);
//				}
                recodeDto.setTimeId(timeId);//---时间

                infoMap = EntityUtils.getMap(infoList, e -> e.getStructureId());
                StringBuffer str = null;
                List<Object[]> fileList = new ArrayList<>();


                for (DiathesisStructure s : strucList) {
                    con = infoMap.get(s.getId());
                    if (con == null) {
                        //数据问题 结果的维护的数据  参数理应属于项目一下的 但是保存参数取的是项目一 实际结果确保存到项目二 错误数据 或者保存进去的参数在后来删除啦
                        continue;
                    }
                    if (StringUtils.isNotBlank(con.getContentTxt())) {
                        if (DiathesisConstant.DATA_TYPE_4.equals(s.getDataType())) {
                            //panlf report
                            String contxt = con.getContentTxt();
                            ArrayList<FileDto> fileDtos = new ArrayList<>();

                            String[] allFile = contxt.split(DiathesisConstant.FILE_SPLIT);
                            for (String one : allFile) {
                                FileDto dto = new FileDto();
                                dto.setFileName(StringUtils.substringBefore(one, ","));
                                dto.setFilePath(StringUtils.substringAfter(one, ","));
                                fileDtos.add(dto);
                            }
                            fileList.add(fileDtos.toArray(new Object[0]));
                        } else {
                            String conTxt = "";
                            if (DiathesisConstant.DATA_TYPE_2.equals(s.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(s.getDataType())) {
                                //optionMap
                                String[] split = con.getContentTxt().split(",");
                                String contentTxt = "";
                                for (String sp : split) {
                                    if (optionMap.containsKey(sp)) {
                                        contentTxt += "," + optionMap.get(sp);
                                    }
                                }
                                if (StringUtils.isNotBlank(contentTxt)) {
                                    conTxt = contentTxt.substring(1);
                                } else {
                                    conTxt = "";
                                }

                            } else {
                                conTxt = con.getContentTxt();
                            }
                            if (str == null) {
                                str = new StringBuffer(s.getTitle() + ":" + conTxt);
                            } else {
                                str.append("," + s.getTitle() + ":" + conTxt);
                            }
                        }

                    }
                }

                recodeDto.setFileList(fileList);
                if (str != null) {
                    recodeDto.setTextContent(str.toString());
                } else {
                    recodeDto.setTextContent("");
                }
                data5.add(recodeDto);

            }
            if (CollectionUtils.isNotEmpty(data5)) {
                //排序
                Collections.sort(data5, new Comparator<DiathesisStuRecordDto>() {

                    @Override
                    public int compare(DiathesisStuRecordDto o1, DiathesisStuRecordDto o2) {
                        if (o1.getTypeSort() != o2.getTypeSort()) {
                            return o1.getTypeSort() - o2.getTypeSort();
                        }
                        if (o1.getProjectSort() != o2.getProjectSort()) {
                            return o1.getProjectSort() - o2.getProjectSort();
                        }
                        return o1.getTimeId().compareTo(o2.getTimeId());
                    }

                });
            }

        }
        //写实记录
        //obj.put("data5scoreMap",data5scoreMap);
        obj.put("data5", data5);


        Map<String, Course> courseMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(subjectIds)) {
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            courseMap = EntityUtils.getMap(courseList, e -> e.getId());
        }

		/*//学业水平
		for(Entry<String, String> item:score_xy.entrySet()) {
			subjectScoreDto=new DiatheisSubjectScoreDto();
			subjectScoreDto.setSubjectName(courseMap.get(item.getKey())==null?"":courseMap.get(item.getKey()).getSubjectName());
			subjectScoreDto.setScore(item.getValue());
			data2.add(subjectScoreDto);
		}
		obj.put("data2", data2);*/


        //学业水平
        String gradeId = clazz.getGradeId();
        String studentId = student.getId();
        List<DiathesisScoreType> scoreTypeList = diathesisScoreTypeService.findListByUnitIdAndGradeIdAndType(unitId, gradeId, DiathesisConstant.INPUT_TYPE_2);
        List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findByStudentIdAndScoreTypeIdIn(studentId, EntityUtils.getArray(scoreTypeList, x -> x.getId(), String[]::new));

        Map<String, String> infoMap = EntityUtils.getMap(infoList, x -> x.getId(), x -> x.getObjId());
        Map<String, DiathesisScoreType> infoTypeMap = EntityUtils.getMap(scoreTypeList, x -> x.getId(), x -> x);

        //List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findByScoreTypeIdIn(scoreTypeIds);

        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
        //List<DiathesisScoreType> scoreTypeList=diathesisScoreTypeService.findListByUnitIdAndGradeIdAndType(unitId,gradeId,DiathesisConstant.INPUT_TYPE_2);
        List<DiathesisSetSubject> setSubjectList = diathesisSetSubjectService.findByGradeIdAndType(gradeId, DiathesisConstant.SUBJECT_FEILD_XY);
        List<String> subIds = setSubjectList.stream().map(x -> x.getSubjectId()).distinct().collect(Collectors.toList());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);

        String[] scoreTypeIds = EntityUtils.getArray(scoreTypeList, x -> x.getId(), String[]::new);
        List<DiathesisScoreInfoEx> infoExList = diathesisScoreInfoExService.findListByInfoTypeIdIn(EntityUtils.getList(scoreTypeList, x -> x.getId()));
        HashMap<String, String> scoreMaxMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(infoExList) && CollectionUtils.isNotEmpty(infoList)) {
            Map<String, List<DiathesisScoreInfo>> info1Map = infoList.stream().collect(Collectors.groupingBy(x -> x.getScoreTypeId()));
            Map<String, List<DiathesisScoreInfoEx>> infoExMap = infoExList.stream().collect(Collectors.groupingBy(x -> x.getScoreInfoId()));

            for (DiathesisScoreType scoreType : scoreTypeList) {
                List<DiathesisScoreInfo> infos = info1Map.get(scoreType.getId());
                if (CollectionUtils.isEmpty(infos)) continue;
                for (DiathesisScoreInfo info : infos) {
                    List<DiathesisScoreInfoEx> infoEx = infoExMap.get(info.getId());
                    if (CollectionUtils.isEmpty(infoEx)) continue;
                    for (DiathesisScoreInfoEx ex : infoEx) {
                        String score = scoreMaxMap.get(info.getObjId() + "_" + ex.getFieldCode());
                        if ((StringUtils.isNotBlank(ex.getFieldValue()) && StringUtils.isBlank(score)) || score.compareTo(ex.getFieldValue()) > 0) {
                            scoreMaxMap.put(info.getObjId() + "_" + ex.getFieldCode(), ex.getFieldValue());
                        }
                    }
                }
            }
        }
        //List<String> fieldNameList = EntityUtils.getList(fieldList, x -> x.getFieldName());
        List<String> data2title = courseList.stream().map(x -> x.getSubjectName()).collect(Collectors.toList());


		/*for (Course course : courseList) {
			for (DiathesisSubjectField field : fieldList) {
				String maxValue = scoreMaxMap.get(course.getId() + "_" + field.getFieldCode());
				data2.add(StringUtils.defaultString(maxValue,""));
			}
		}*/
        for (DiathesisSubjectField field : fieldList) {
            Json json = new Json();
            json.put("fieldName", field.getFieldName());
            JsonArray scoreList = new JsonArray();
            for (Course course : courseList) {
                String maxValue = scoreMaxMap.get(course.getId() + "_" + field.getFieldCode());
                scoreList.add(StringUtils.defaultString(maxValue, ""));
            }
            json.put("scoreList", scoreList);
            data2.add(json);
        }

        obj.put("data2", data2);
        obj.put("data2title", data2title);


        //data3 必修     data4 选修
        List<DiathesisScoreType> scoreTypeList_xk = diathesisScoreTypeService.findListByUnitIdAndGradeIdAndType(unitId, gradeId, DiathesisConstant.INPUT_TYPE_1);
        Map<String, Integer> semesterMap = EntityUtils.getMap(scoreTypeList_xk, x -> x.getId(), x -> x.getSemester());
        List<DiathesisScoreType> bxTypeList = EntityUtils.filter2(scoreTypeList_xk, x -> DiathesisConstant.SCORE_TYPE_1.equals(x.getScoreType()));
        List<DiathesisScoreType> xxTypeList = EntityUtils.filter2(scoreTypeList_xk, x -> DiathesisConstant.SCORE_TYPE_2.equals(x.getScoreType()));
        List<DiathesisScoreInfo> bxInfo = diathesisScoreInfoService.findByScoreTypeIdIn(EntityUtils.getArray(bxTypeList, x -> x.getId(), String[]::new));
        List<DiathesisScoreInfo> xxInfo = diathesisScoreInfoService.findByScoreTypeIdIn(EntityUtils.getArray(xxTypeList, x -> x.getId(), String[]::new));
        Map<String, Integer> infoSemesterMap = Stream.concat(bxInfo.stream(), xxInfo.stream()).collect(Collectors.toMap(x -> x.getId(), x -> semesterMap.get(x.getScoreTypeId())));
        List<DiathesisScoreInfoEx> bxInfoEx = diathesisScoreInfoExService.findListByInfoTypeIdIn(EntityUtils.getList(bxTypeList, x -> x.getId()));
        Map<String, List<DiathesisScoreInfoEx>> bxInfoExMap = bxInfoEx.stream().collect(Collectors.groupingBy(x -> x.getScoreInfoId()));
        List<DiathesisScoreInfoEx> xxInfoEx = diathesisScoreInfoExService.findListByInfoTypeIdIn(EntityUtils.getList(xxTypeList, x -> x.getId()));
        Map<String, List<DiathesisScoreInfoEx>> xxInfoExMap = xxInfoEx.stream().collect(Collectors.groupingBy(x -> x.getScoreInfoId()));


        List<DiathesisSubjectField> bxFieldTemp = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX);
        List<DiathesisSubjectField> xxFieldTemp = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX);

        List<DiathesisSubjectField> bxFieldList = EntityUtils.filter2(bxFieldTemp, x -> DiathesisConstant.COMPULSORY_SCORE.equals(x.getFieldCode()) || DiathesisConstant.COMPULSORY_GREDIT.equals(x.getFieldCode()) || DiathesisConstant.COMPULSORY_PRINCIPAL.equals(x.getFieldCode()));
        //List<DiathesisSubjectField> xxFieldList = EntityUtils.filter2(xxFieldTemp, x -> DiathesisConstant.ELECTIVE_SCORE.equals(x.getFieldCode()) || DiathesisConstant.ELECTIVE_GREDIT.equals(x.getFieldCode()) || DiathesisConstant.ELECTIVE_PRINCIPAL.equals(x.getFieldCode()));

        //diathesisSubjectFieldService
        HashMap<String, String> bxScoreMap = new HashMap<>();

        //Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        String gradeCode = grade.getGradeCode();

        List<Json> data3Title = getData3Title(gradeCode, bxFieldList);
        obj.put("data3Titles", data3Title);
        int section = Integer.parseInt(gradeCode.substring(0, 1));
        for (DiathesisScoreInfo info : bxInfo) {
            bxScoreMap.put(info.getObjId() + "_" + gradeCode + "_" + infoSemesterMap.get(info.getId()) + "_" + DiathesisConstant.COMPULSORY_SCORE, info.getScore());
            List<DiathesisScoreInfoEx> bxlist = bxInfoExMap.get(info.getId());
            if (CollectionUtils.isEmpty(bxlist)) continue;
            for (DiathesisScoreInfoEx ex : bxlist) {
                if (ex.getFieldCode().equals(DiathesisConstant.COMPULSORY_GREDIT) || ex.getFieldCode().equals(DiathesisConstant.COMPULSORY_PRINCIPAL)) {
                    bxScoreMap.put(info.getObjId() + "_" + gradeCode + "_" + infoSemesterMap.get(info.getId()) + "_" + ex.getFieldCode(), ex.getFieldValue());
                }
            }
        }
        HashMap<String, String> xxScoreMap = new HashMap<>();
        Set<String> xxSubIds = new TreeSet<>();
        for (DiathesisScoreInfo info : xxInfo) {
            xxSubIds.add(info.getObjId());
            xxScoreMap.put(info.getObjId() + "_" + gradeCode + "_" + infoSemesterMap.get(info.getId()) + "_" + DiathesisConstant.ELECTIVE_SCORE, info.getScore());
            List<DiathesisScoreInfoEx> xxlist = xxInfoExMap.get(info.getId());
            if (CollectionUtils.isEmpty(xxlist)) continue;
            for (DiathesisScoreInfoEx ex : xxlist) {
                if (ex.getFieldCode().equals(DiathesisConstant.ELECTIVE_GREDIT) || ex.getFieldCode().equals(DiathesisConstant.ELECTIVE_PRINCIPAL)) {
                    xxScoreMap.put(info.getObjId() + "_" + gradeCode + "_" + infoSemesterMap.get(info.getId()) + "_" + ex.getFieldCode(), ex.getFieldValue());
                }
            }
        }

        List<DiathesisSetSubject> bxSubjectList = diathesisSetSubjectService.findByGradeIdAndType(gradeId, DiathesisConstant.SUBJECT_FEILD_BX);
        Set<String> bxSubIds = EntityUtils.getSet(bxSubjectList, x -> x.getSubjectId());
        String[] courseIds = Stream.concat(xxSubIds.stream(), bxSubjectList.stream().map(x -> x.getSubjectId())).distinct().toArray(String[]::new);
        List<Course> courseList1 = SUtils.dt(courseRemoteService.findListByIds(courseIds), Course.class);
        Map<String, String> course1Map = EntityUtils.getMap(courseList1, x -> x.getId(), x -> x.getSubjectName());

        for (String subjectId : bxSubIds) {
            List<String> sub = new ArrayList<>();
            sub.add(course1Map.get(subjectId));
            for (int i = 0; i < (section == 1 ? 6 : 3); i++) {
                for (int j = 0; j < 2; j++) {
                    String[] field = {DiathesisConstant.COMPULSORY_SCORE, DiathesisConstant.COMPULSORY_GREDIT, DiathesisConstant.COMPULSORY_PRINCIPAL};
                    for (String s : field) {
                        String key = subjectId + "_" + section + "" + (i + 1) + "_" + (j + 1) + "_" + s;
                        String value = bxScoreMap.get(key);
                        sub.add(StringUtils.defaultString(value, ""));
                    }

                }
            }
            data3.add(sub);
        }
        obj.put("data3", data3);
        for (String subjectId : xxSubIds) {
            List<String> sub = new ArrayList<>();
            sub.add(course1Map.get(subjectId));
            for (int i = 0; i < (section == 1 ? 6 : 3); i++) {
                for (int j = 0; j < 2; j++) {
                    String[] field = {DiathesisConstant.ELECTIVE_SCORE, DiathesisConstant.ELECTIVE_GREDIT, DiathesisConstant.ELECTIVE_PRINCIPAL};
                    for (String s : field) {
                        String key = subjectId + "_" + section + "" + (i + 1) + "_" + (j + 1) + "_" + s;
                        String value = xxScoreMap.get(key);
                        sub.add(StringUtils.defaultString(value, ""));
                    }

                }
            }
            data4.add(sub);
        }
        obj.put("data4", data4);
		/*//必修
		if(CollectionUtils.isNotEmpty(data3)) {
			for(DiatheisSubjectScoreDto dto:data3) {
				if(courseMap.containsKey(dto.getSubjectId())) {
					dto.setSubjectName(courseMap.get(dto.getSubjectId()).getSubjectName());
				}else {
					//正常不会出现科目找不到
					dto.setSubjectName("");
				}

			}
			obj.put("data3", data3);
		}else {
			obj.put("data3", data3);
		}
		//选修
		if(CollectionUtils.isNotEmpty(data4)) {
			for(DiatheisSubjectScoreDto dto:data4) {
				if(courseMap.containsKey(dto.getSubjectId())) {
					dto.setSubjectName(courseMap.get(dto.getSubjectId()).getSubjectName());
				}else {
					//正常不会出现科目找不到
					dto.setSubjectName("");
				}
			}
			obj.put("data4", data4);
		}else {
			obj.put("data4", data4);
		}
*/

        String usingSetUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET);
        DiathesisSet set = diathesisSetService.findByUnitId(usingSetUnitId);
        DiathesisGlobalSettingDto settingDto = diathesisSetService.findGlobalByUnitId(usingSetUnitId);

        String semesterScoreSet = set.getSemesterScoreProp();
        Integer[] perSemester = new Integer[6];
        if (StringUtils.isBlank(semesterScoreSet)) {
            perSemester = new Integer[]{16, 16, 17, 17, 17, 17};
        } else {
            String[] split = semesterScoreSet.split(",");
            for (int i = 0; i < perSemester.length; i++) {
                perSemester[i] = Integer.parseInt(split[i]);
            }
        }
        //加入分数比例 如果没有设置 按平均处理
        List<String[]> data1 = new ArrayList<>();
        //综合素质
        //所有顶级项目
        List<DiathesisProject> topList = list.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_TOP)).collect(Collectors.toList());

        Float allScore = 0f;
        int semesterCount0 = 0;

        if (CollectionUtils.isNotEmpty(topList) && CollectionUtils.isNotEmpty(timeStringList)) {
            //String usingSetUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET);
            List<String> rankItems = settingDto.getRankItems();
            List<String> rankValues = settingDto.getRankValues();

            Map<String, Integer> scoreSemesterMap = new HashMap<>();
            int j = 0;
            for (String rankItem : rankItems) {
                scoreSemesterMap.put(rankItem, Integer.parseInt(rankValues.get(j++)));
            }
            //学期汇总成绩
            String[] semesterScore = new String[timeStringList.size() + 1];
            semesterScore[0] = "学期汇总";
            Float[] tempSemester = new Float[12];
            for (int i = 0; i < tempSemester.length; i++) {
                tempSemester[i] = 0f;
            }

            boolean flag = false;
            for (DiathesisProject d : topList) {
                //一级科目分数比例
                Float percent;
                if (!flag && d.getTopProp() != null) {
                    percent = d.getTopProp() * 1.0f / 100f;
                } else {
                    flag = true;
                    percent = 100.0f / topList.size();
                }
                String[] arr = new String[timeStringList.size() + 1];
                arr[0] = d.getProjectName();
                for (int i = 0; i < timeStringList.size(); i++) {
                    if (scoreStatMap.containsKey(timeStringList.get(i) + "-" + d.getId())) {
                        arr[i + 1] = scoreStatMap.get(timeStringList.get(i) + "-" + d.getId());
                        tempSemester[i + 1] += scoreSemesterMap.get(arr[i + 1]) * percent;
                    } else {
                        arr[i + 1] = "";
                    }
                }
                data1.add(arr);
            }
            for (int i = 1; i < tempSemester.length; i++) {
                if (gradeCode.substring(0, 1).equals(BaseConstants.SECTION_PRIMARY)) {
                    perSemester = new Integer[]{8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9};
                } else {
                    if (i > 6) continue;
                }
                if (tempSemester[i] == 0) semesterCount0++;
                allScore += perSemester[i - 1] * tempSemester[i] / 100f;

                semesterScore[i] = changeToRank(tempSemester[i], settingDto);
            }
            data1.add(semesterScore);
        }
        obj.put("data1AllScore", changeToRank(allScore / semesterCount0 * ("1".equals(gradeCode.substring(0, 1)) ? 12 : 6), settingDto));
        obj.put("data1", data1);
        return obj;
    }

    private List<Json> getData3Title(String gradeCode, List<DiathesisSubjectField> bxFieldList) {
        List<Json> result = new ArrayList<>();
        List<String> fieldNames = EntityUtils.getList(bxFieldList, x -> x.getFieldName());
        String[] sections = new String[]{"小", "初", "高"};
        String[] gradeList = new String[]{"一", "二", "三", "四", "五", "六"};
        String[] semesterList = new String[]{"上", "下"};
        int section = Integer.parseInt(gradeCode.substring(0, 1));
        String prefix = sections[section - 1];
        for (int i = 0; i < (section == 1 ? 6 : 3); i++) {
            for (String s : semesterList) {
                Json grade = new Json();
                grade.put("gradeName", prefix + gradeList[i] + s);
                grade.put("fieldList", fieldNames);
                result.add(grade);
            }
        }
        return result;
    }

    private String changeToRank(Float value, DiathesisGlobalSettingDto settingDto) {
        if (value == 0f) return "";
        List<String> rankItems = settingDto.getRankItems();
        List<String> rankValues = settingDto.getRankValues();
        if (value <= Float.parseFloat(rankValues.get(rankValues.size() - 1)))
            return rankItems.get(rankItems.size() - 1);
        if (value >= Float.parseFloat(rankValues.get(0))) return rankItems.get(0);
        for (int i = 0; i < rankValues.size(); i++) {
            if (value >= Float.parseFloat(rankValues.get(i))) {
                return rankItems.get(i);
            }
        }
        return rankItems.get(rankItems.size() - 1);
    }

    /**
     * 综合素质查询---由于福建特殊加入
     *
     * @param studentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStuReportHead")
    public String getStuReportHead(@RequestParam(required = false) String studentId) {
        if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT) {
            //学生端查询
            studentId = getLoginInfo().getOwnerId();
        } else if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
            //todo 家长端  暂时只能对应一个学生
            studentId = SUtils.dc(familyRemoteService.findOneById(getLoginInfo().getOwnerId()), Family.class).getStudentId();
        }
        if (StringUtils.isBlank(studentId)) {
            return error("studentId不能为空");
        }
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        if (student == null) {
            return error("学生数据丢失");
        }
        JSONObject obj = new JSONObject();
        obj.put("studentId", studentId);

        Unit unit = SUtils.dc(unitRemoteService.findOneById(student.getSchoolId()), Unit.class);
        if (unit != null && isFuJianReginCode(unit.getRegionCode())) {
            obj.put("ifFuJian", "1");
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, student.getSchoolId()), Semester.class);
            if (semester == null) {
                return error("学年学期未维护，请联系管理员");
            }
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
            List<String[]> timeList = makeSemesterList(semester, grade);
            obj.put("timeList", timeList);
        } else {
            obj.put("ifFuJian", "0");
        }
        return obj.toString();
    }

    public List<String[]> makeSemesterList(Semester semester, Grade grade) {
        //根据当前学年学期
        int openYear = Integer.parseInt(grade.getOpenAcadyear().substring(0, 4));
        int currentYear = Integer.parseInt(semester.getAcadyear().substring(0, 4));
        List<String[]> timeList = new ArrayList<String[]>();
        Integer section = grade.getSection();
        List<String> timeIdList = new ArrayList<String>();
        if (currentYear < openYear) {
            //不存在
            timeList.add(new String[]{"", "汇总表"});
        } else {
            for (int i = 0; i <= currentYear - openYear; i++) {
                if (i < currentYear - openYear) {
                    //31_1;31_2
                    timeIdList.add(section + "" + (i + 1) + "_1");
                    timeIdList.add(section + "" + (i + 1) + "_2");
                } else {
                    timeIdList.add(section + "" + (i + 1) + "_1");
                    if (semester.getSemester() > 1) {
                        timeIdList.add(section + "" + (i + 1) + "_2");
                    }
                }
            }
        }
        //取名
        //学段map
        String sectionMcodeKey = "DM-RKXD-" + grade.getSection();
        String[] mcodeIds = new String[]{sectionMcodeKey};
        Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(mcodeIds), new TR<Map<String, Map<String, McodeDetail>>>() {
        });
        Map<String, McodeDetail> xdMap = new HashMap<>();
        if (mcodeMap.containsKey(sectionMcodeKey)) {
            for (Entry<String, McodeDetail> item : mcodeMap.get(sectionMcodeKey).entrySet()) {
                xdMap.put(grade.getSection() + item.getValue().getThisId(), item.getValue());
            }
        }
        for (String s : timeIdList) {
            String[] arr = s.split("_");
            if (!xdMap.containsKey(arr[0])) {
                continue;
            }
            if ("1".equals(arr[1])) {
                timeList.add(new String[]{s, xdMap.get(arr[0]).getMcodeContent() + "上学期"});
            } else {
                timeList.add(new String[]{s, xdMap.get(arr[0]).getMcodeContent() + "下学期"});
            }
        }
        timeList.add(new String[]{"", "汇总表"});
        return timeList;
    }

    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        obj.put("studentId", "12345678945622455");
        obj.put("ifFuJian", "1");
        List<String[]> timeList = new ArrayList<String[]>();
        timeList.add(new String[]{"31_1", "高一上学期"});
        timeList.add(new String[]{"31_2", "高一下学期"});
        timeList.add(new String[]{"32_1", "高二上学期"});
        timeList.add(new String[]{"", "汇总表"});
        obj.put("timeList", timeList);
        System.out.println(obj.toString());
    }


    /**
     * 综合素质查询
     *
     * @param studentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStuReport")
    public String findStuReport(@RequestParam(required = false) String studentId) {

        if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT) {
            //学生端查询
            studentId = getLoginInfo().getOwnerId();
        } else if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
            //todo 家长端  暂时只能对应一个学生
            studentId = SUtils.dc(familyRemoteService.findOneById(getLoginInfo().getOwnerId()), Family.class).getStudentId();
        }
        if (StringUtils.isBlank(studentId)) {
            return error("studentId不能为空");
        }

        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        if (student == null) {
            return "学生数据丢失";
        }
        JSONObject obj = findStuReportItem(student);

        return obj.toString();
    }


    @ResponseBody
    @RequestMapping("/checkStatStudent")
    public String checkStat(String gradeId, String gradeCode, String semester) {


        //判断是不是已经统计过
        String unitId = getLoginInfo().getUnitId();
        List<DiathesisScoreType> oldlist = diathesisScoreTypeService.findListByGradeId(unitId, gradeId, gradeCode, semester, DiathesisConstant.INPUT_TYPE_4);

        List<DiathesisScoreType> typeList = diathesisScoreTypeService.findListByGradeId(unitId, gradeId, gradeCode, semester, DiathesisConstant.INPUT_TYPE_3);
        for (DiathesisScoreType type : typeList) {
            if (type.getLimitedTime() == null) return error("时间没有维护");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date time = sdf.parse(type.getLimitedTime().split("~")[1]);
                if (time.after(new Date())) return error("评价时间没截止,不能进行统计");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        JSONObject json = new JSONObject();
        json.put("success", true);
        if (CollectionUtils.isNotEmpty(oldlist)) {
            json.put("msg", "old");
            return json.toString();
        } else {
            return statStudent(gradeId, gradeCode, semester);
        }
    }

    /**
     * 分数统计 总评统计
     *
     * @param gradeId
     * @param gradeCode
     * @param semester
     * @return
     */
    @ResponseBody
    @RequestMapping("/statStudent")
    public String statStudent(String gradeId, String gradeCode, String semester) {
        String unitId = getLoginInfo().getUnitId();
        JSONObject json = new JSONObject();
        json.put("success", true);
        //1、全局设置取值 先获取真正有权限的单位id
        String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_GOBAL_SET);
        DiathesisGlobalSettingDto setDto = diathesisSetService.findGlobalByUnitId(usingUnitId);
        DiathesisGlobalSettingDto mySet = diathesisSetService.findGlobalByUnitId(unitId);
        if (setDto == null) {
            json.put("success", false);
            json.put("msg", "全局设置未维护，请先前去查看");
            return json.toString();
        }
        //key:A/B/C/D value:5/4/3/2/1
        Map<String, Integer> rangeValueMap = new HashMap<>();
        List<String> rangItems = setDto.getRankItems();
        List<String> rangValues = setDto.getRankValues();
        List<String> scoreScopes = setDto.getScoreScopes();
        List<BetweenValue> scoreRangeList = new ArrayList<>();
        if (DiathesisConstant.SCORE_RANGE_REGULAR.equals(setDto.getScoreType())) {
            //如果是分数范围制
            for (int i = 0; i < rangItems.size(); i++) {
                rangeValueMap.put(rangItems.get(i), Integer.parseInt(rangValues.get(i)));
                String scoreRange = scoreScopes.get(i);
                String[] arr = scoreRange.split("-");
                scoreRangeList.add(new BetweenValue(Float.parseFloat(arr[1]), Float.parseFloat(arr[0]), rangItems.get(i)));
            }
        } else if (DiathesisConstant.SCORE_PROPORTION_REGULAR.equals(setDto.getScoreType())) {
            //如果是分数比例制  比例要从自己单位设置获取

            scoreScopes = mySet.getScoreScopes();
            for (int i = 0; i < rangItems.size(); i++) {
                rangeValueMap.put(rangItems.get(i), Integer.parseInt(rangValues.get(i)));
                String scoreRange = scoreScopes.get(scoreScopes.size() - i - 1);
                String[] arr = scoreRange.split("-");
                scoreRangeList.add(new BetweenValue(Float.parseFloat(arr[1]) / 100, Float.parseFloat(arr[0]) / 100, rangItems.get(i)));
            }
        } else {
            json.put("success", false);
            json.put("msg", "分数制没有设置");
            return json.toString();
        }


        //评价类型 统计比例
        //所有综合素质相关的项目
        List<DiathesisProject> projectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD});
        if (CollectionUtils.isEmpty(projectList)) {
            json.put("success", false);
            json.put("msg", "综合素质评价类目没有设置");
            return json.toString();
        }
        Map<String, DiathesisProject> projectMap = EntityUtils.getMap(projectList, e -> e.getId());
        //类项目
        List<DiathesisProject> topList = projectList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_TOP)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(topList)) {
            json.put("success", false);
            json.put("msg", "综合素质评价类目没有设置");
            return json.toString();
        }


        //key:projectId,key:评价类型  value:比例
        Map<String, Map<String, Float>> stateParmMap = new HashMap<>();
        //todo 评价人比例要从 projectEx 表中取值

        List<String> topIds = EntityUtils.getList(topList, x -> x.getId());
        List<DiathesisProjectEx> ex = diathesisProjectExService.findByUnitIdAndProjectIdIn(usingUnitId, topIds);
        for (DiathesisProjectEx e : ex) {
            stateParmMap.put(e.getProjectId(), new HashMap<>());
            if (StringUtils.isBlank(e.getProportions())) {
                return error("综合素质评价统计各评价设置比例未维护");
            }
            String[] pros = e.getProportions().split(",");
            for (int i = 0; i < pros.length; i++) {
                if (StringUtils.isNotBlank(pros[i])) {
                    stateParmMap.get(e.getProjectId()).put("" + (i + 1), Float.parseFloat(pros[i]));
                }
            }
        }


        List<DiathesisScoreType> list = diathesisScoreTypeService.findListByGradeId(unitId, gradeId, gradeCode, semester, DiathesisConstant.INPUT_TYPE_3);
        Set<String> scoreTypeIdSet = EntityUtils.getSet(list, e -> e.getId());
        //获取所有综合素质成绩

        List<DiathesisScoreInfo> scoreListTemp = diathesisScoreInfoService.findByScoreTypeIdIn(scoreTypeIdSet.toArray(new String[]{}));
        if (CollectionUtils.isEmpty(scoreListTemp)) {
            json.put("success", false);
            json.put("msg", "学生综合素质评价还未维护");
            return json.toString();
        }

        //List<DiathesisScoreInfo> mutualList = scoreListTemp.stream().filter(x -> DiathesisConstant.SCORE_TYPE_3.equals(x.getType())).collect(Collectors.toList());
        List<DiathesisScoreInfo> scoreList = scoreListTemp.stream().filter(x -> !DiathesisConstant.SCORE_TYPE_7.equals(x.getType())).collect(Collectors.toList());

        //等第  需要转化成分值
	/*	Function<DiathesisScoreInfo,DiathesisScoreInfo> function=DiathesisConstant.INPUT_SCORE.equals(setDto.getInputValueType())?x->x:x->{x.setScore(""+rangeValueMap.get(x.getScore()));return x;};
		Map<String, List<DiathesisScoreInfo>> mutualMap = mutualList.stream().map(function).collect(Collectors.groupingBy(x -> x.getStuId() + "_" + x.getObjId()));;
		HashMap<Integer,String> rankMap=new HashMap<>();
		for (int i = 0; i < rangItems.size(); i++) {
			rankMap.put(Integer.parseInt(rangValues.get(i)),rangItems.get(i));
		}

		for (String key : mutualMap.keySet()){
			List<DiathesisScoreInfo> scorelist = mutualMap.get(key);

			DiathesisScoreInfo d = scorelist.get(0);
			int sum=0;
			for (DiathesisScoreInfo info : scorelist) {
				sum+=Integer.parseInt(info.getScore());
			}
			String score;
			try {
				score=(sum*1.0f/scorelist.size())+"";
			}catch (Exception e){
				score=rangItems.get(rangItems.size()-1);
			}
			d.setScore(score);
			d.setModifyTime(new Date());
			//scoreList.add(d);
		}*/

        Set<String> projectIds = EntityUtils.getSet(scoreList, e -> e.getObjId());
        //key：成绩中关联的projectId,value:顶级projectId
        Map<String, String> topIdByProjectId = makeTopIdMap(projectIds, projectMap);

        //key topProjectId  key studentId key:type value List<Float> 成绩
        Map<String, Map<String, Map<String, List<Float>>>> studentScoreMap = new HashMap<>();
        for (DiathesisScoreInfo info : scoreList) {
            String topPId = topIdByProjectId.get(info.getObjId());
            if (StringUtils.isBlank(topPId)) {
                //关联顶级的类项目找不到
                continue;
            }
            if (StringUtils.isBlank(info.getScore())) {
                //成绩为空
                continue;
            }

            Map<String, Map<String, List<Float>>> map1 = studentScoreMap.get(topPId);
            if (map1 == null) {
                map1 = new HashMap<>();
                studentScoreMap.put(topPId, map1);
            }
            Map<String, List<Float>> map2 = map1.get(info.getStuId());
            if (map2 == null) {
                map2 = new HashMap<>();
                map1.put(info.getStuId(), map2);
            }
            List<Float> sList = map2.get(info.getType());
            if (sList == null) {
                sList = new ArrayList<>();
                map2.put(info.getType(), sList);
            }
            //判断时分数还是等第
            Float ss = null;
            try {
                ss = Float.parseFloat(info.getScore());
            } catch (Exception e) {
                //判断不是数值
                if (rangeValueMap.containsKey(info.getScore())) {
                    ss = rangeValueMap.get(info.getScore()) + 0.0f;
                }

            }
            if (ss == null) {
                continue;
            }
            sList.add(ss);
        }

        List<DiathesisScoreInfo> insertList = new ArrayList<>();
        DiathesisScoreInfo scoreInfo = null;
        List<DiathesisScoreType> oldlist = diathesisScoreTypeService.findListByGradeId(unitId, gradeId, gradeCode, semester, DiathesisConstant.INPUT_TYPE_4);
        //todo 下个版本
        //List<DiathesisScoreType> oldSemesterlist = diathesisScoreTypeService.findListByGradeId(unitId, gradeId, gradeCode, semester, DiathesisConstant.INPUT_TYPE_5);
        DiathesisScoreType scoreType = null;
        DiathesisScoreType scoreSemesterType = null;
        Set<String> delIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(oldlist)) {
            scoreType = oldlist.get(0);
            delIds = EntityUtils.getSet(oldlist, e -> e.getId());
        }
        //todo 下个版本
		/*if(CollectionUtils.isNotEmpty(oldSemesterlist)) {
			scoreSemesterType=oldSemesterlist.get(0);
			delIds.addAll(EntityUtils.getSet(oldlist, e->e.getId()));
		}*/
        if (scoreType == null) {
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            scoreType = new DiathesisScoreType();
            scoreType.setId(UuidUtils.generateUuid());
            scoreType.setGradeId(gradeId);
            scoreType.setUnitId(unitId);
            scoreType.setGradeCode(gradeCode);
            scoreType.setYear(grade.getOpenAcadyear().split("-")[0]);
            scoreType.setOperator(getLoginInfo().getRealName());
            scoreType.setScoreType("0");//没什么用处
            scoreType.setSemester(Integer.parseInt(semester));
            scoreType.setExamName("综合素质");
            scoreType.setType(DiathesisConstant.INPUT_TYPE_4);
        }

        //学期总评价
        //todo 下个版本
        //key topProjectId  key studentId  value 成绩
        Map<String, List<SwitchScore>> scoreMap = new HashMap<String, List<SwitchScore>>();
		/*if(oldSemesterlist==null) {
			Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
			scoreSemesterType=new DiathesisScoreType();
			scoreSemesterType.setId(UuidUtils.generateUuid());
			scoreSemesterType.setGradeId(gradeId);
			scoreSemesterType.setUnitId(unitId);
			scoreSemesterType.setGradeCode(gradeCode);
			scoreSemesterType.setYear(grade.getOpenAcadyear().split("-")[0]);
			scoreSemesterType.setOperator(getLoginInfo().getRealName());
			scoreSemesterType.setScoreType("0");//没什么用处
			scoreSemesterType.setSemester(Integer.parseInt(semester));
			scoreSemesterType.setExamName("综合素质学期总评");
			scoreSemesterType.setType(DiathesisConstant.INPUT_TYPE_5);
		}
		scoreSemesterType.setModifyTime(new Date());*/
        scoreType.setModifyTime(new Date());
        //统计中个评价百分比
        for (Entry<String, Map<String, Map<String, List<Float>>>> item : studentScoreMap.entrySet()) {
            String key1 = item.getKey();
            Map<String, Float> parm = stateParmMap.get(key1);
            if (parm == null || parm.size() == 0) {
                continue;
            }
            List<SwitchScore> inScoreList = scoreMap.get(key1);
            if (inScoreList == null) {
                inScoreList = new ArrayList<SwitchScore>();
                scoreMap.put(key1, inScoreList);
            }
            Map<String, Map<String, List<Float>>> value1 = item.getValue();
            for (Entry<String, Map<String, List<Float>>> stuItem : value1.entrySet()) {
                String stuIdkey2 = stuItem.getKey();

                //统计每种评价类型的平均值 保留四舍五入两位小数
                float avg = 0.0f;
                //int size=0;
                Map<String, List<Float>> value2 = stuItem.getValue();
                if (value2 == null || value2.size() == 0) {
                    continue;
                }
                for (Entry<String, List<Float>> scoreItem : value2.entrySet()) {
                    if (!parm.containsKey(scoreItem.getKey())) {
                        continue;
                    }
                    if (CollectionUtils.isNotEmpty(scoreItem.getValue())) {
                        avg = avg + (countAvg(scoreItem.getValue())) * parm.get(scoreItem.getKey()) / 100;
                        //size++;
                    }
                }
//				if(size!=0) {
//					avg=avg/size;
//				}
                inScoreList.add(new SwitchScore(stuIdkey2, avg));
            }
        }

        if (DiathesisConstant.SCORE_RANGE_REGULAR.equals(setDto.getScoreType())) {
            //如果是分数范围制
            //根据结果换算avg所在的等第---暂时判断如果超过最高分 给最高等第 最低分给最低等第
            for (Entry<String, List<SwitchScore>> item : scoreMap.entrySet()) {
                makeAbcdScore(item.getValue(), scoreRangeList);
            }
        } else if (DiathesisConstant.SCORE_PROPORTION_REGULAR.equals(setDto.getScoreType())) {
            //如果是分数比例制
            //根据结果排名后取比例
            for (Entry<String, List<SwitchScore>> item : scoreMap.entrySet()) {
                List<SwitchScore> switchList = makeAbcdScore2(item.getValue(), scoreRangeList);
                scoreMap.put(item.getKey(), switchList);
            }
        }

        for (Entry<String, List<SwitchScore>> item : scoreMap.entrySet()) {
            String key1 = item.getKey();
            for (SwitchScore switchScore : item.getValue()) {
                scoreInfo = new DiathesisScoreInfo();
                scoreInfo.setId(UuidUtils.generateUuid());
                scoreInfo.setModifyTime(new Date());
                scoreInfo.setObjId(key1);
                scoreInfo.setScore(switchScore.getToScore());
                scoreInfo.setScoreTypeId(scoreType.getId());
                scoreInfo.setStuId(switchScore.getStuId());
                scoreInfo.setType(scoreType.getType());
                scoreInfo.setUnitId(unitId);
                insertList.add(scoreInfo);
            }
        }
        // todo 没时间搞 下个版本 key stuId  value 学期分数
		/*HashMap<String, Double> stuSemeterMap = new HashMap<>();
		if(CollectionUtils.isNotEmpty(insertList)){
			HashMap<String, Integer> perMap = new HashMap<>();
			for (DiathesisProject project : topList) {
				if(project.getTopProp()==null){
					return error("请先设置一类项目占比");
				}
				perMap.put(project.getId(),project.getTopProp());
			}
			Map<String, List<DiathesisScoreInfo>> stuMap = insertList.stream().collect(Collectors.groupingBy(x -> x.getStuId()));

			for (String stuId : stuMap.keySet()) {
				List<DiathesisScoreInfo> infoList = stuMap.get(stuId);
				if(CollectionUtils.isNotEmpty(infoList)){
					for (DiathesisScoreInfo info : infoList) {
						Integer per = perMap.get(info.getId());
						Integer scoreInt = rangeValueMap.get(info.getScore());
						if(scoreInt==null)scoreInt=0;
						stuSemeterMap.merge(info.getStuId(),scoreInt*per/100d,(o,n)->o+n);
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(stuSemeterMap.keySet())){
			Function<Double,String> b=null;
			HashMap<String, Integer> indexMap = new HashMap<>();

			List<Student> students = SUtils.dt(studentRemoteService.findByGradeId(gradeId), Student.class);
			int allNums = students.size();
			if(DiathesisConstant.SCORE_RANGE_REGULAR.equals(setDto.getScoreType())){
				//比例制度
				List<String> blScore = mySet.getScoreScopes();

				List<ScoreSemester> semesterList=new ArrayList<>();
				for (String info : stuSemeterMap.keySet()) {
					ScoreSemester x = new ScoreSemester();
					x.setScore(stuSemeterMap.get(info));
					x.setStuId(info);
				}
				if(CollectionUtils.isNotEmpty(semesterList)){
					semesterList.sort(Comparator.comparingDouble((ScoreSemester x)->x.getScore()).reversed());
					for (int i = 0; i < semesterList.size(); i++) {
						indexMap.put(semesterList.get(i).getStuId(),i);
					}
				}
			}
			for (String info : stuSemeterMap.keySet()) {
				if(DiathesisConstant.SCORE_PROPORTION_REGULAR.equals(setDto.getScoreType())){
					//比例制度
				}
				String score=getScore(rangItems,scoreScopes,"1");
			}
		}

*/

        String[] del = null;
        if (CollectionUtils.isNotEmpty(delIds)) {
            del = delIds.toArray(new String[]{});
        }
        try {
            diathesisScoreTypeService.saveSate(insertList, scoreType, del);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "统计失败");
            return json.toString();
        }
        json.put("msg", "统计成功");
        return json.toString();
    }

    private String getScore(List<String> rangItems, List<String> scoreScopes, String s) {
        if (StringUtils.isBlank(s)) return rangItems.get(rangItems.size() - 1);
        for (int i = 0; i < scoreScopes.size(); i++) {
            String[] split = scoreScopes.get(i).split("-");
            double v0 = Double.parseDouble(split[0]);
            double v1 = Double.parseDouble(split[1]);
            if (v0 <= Double.parseDouble(s) && v1 > Double.parseDouble(s)) return rangItems.get(i);
        }
        if (Double.parseDouble(scoreScopes.get(0)) <= Double.parseDouble(s)) return rangItems.get(0);
        if (Double.parseDouble(scoreScopes.get(scoreScopes.size() - 1)) >= Double.parseDouble(s))
            return rangItems.get(rangItems.size() - 1);
        return rangItems.get(rangItems.size() - 1);
    }

    class ScoreSemester {
        public Double score;
        public String stuId;

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public String getStuId() {
            return stuId;
        }

        public void setStuId(String stuId) {
            this.stuId = stuId;
        }
    }

    private void makeAbcdScore(List<SwitchScore> scoreList, List<BetweenValue> scoreRangeList) {
        for (SwitchScore switchScore : scoreList) {

            float value = switchScore.getScore();
            String toScore = null;
            //scoreRangeList升序--暂不考虑中间有断层的情况
            for (BetweenValue v : scoreRangeList) {
                if (value >= v.getMinvalue() & value < v.getMaxValue()) {
                    toScore = v.getgValue();
                    break;
                }
            }
            if (value >= scoreRangeList.get(0).getMaxValue()) {
                toScore = scoreRangeList.get(0).getgValue();
            }
            if (value < scoreRangeList.get(scoreRangeList.size() - 1).getMinvalue()) {
                toScore = scoreRangeList.get(scoreRangeList.size() - 1).getgValue();
            }
            switchScore.setToScore(toScore);
        }
    }

    private List<SwitchScore> makeAbcdScore2(List<SwitchScore> scoreList, List<BetweenValue> scoreRangeList) {
        scoreList = scoreList.stream()
                .sorted((s1, s2) -> -Float.compare(s1.getScore(), s2.getScore())).collect(Collectors.toList());//从高到低
        float index = 0;
        float rank = 1f;
        float score = 0;
        for (SwitchScore switchScore : scoreList) {
            if (index++ != 0 && score != switchScore.getScore()) {
                rank = index;
            }
            score = switchScore.getScore();
            float value = rank / scoreList.size();
            String toScore = null;
            for (BetweenValue v : scoreRangeList) {
                if (value > v.getMinvalue() & value <= v.getMaxValue()) {
                    toScore = v.getgValue();
                    break;
                }
            }
            switchScore.setToScore(toScore);
        }
        return scoreList;
    }


    private float countAvg(List<Float> value) {
        float count = 0.0f;
        for (Float f : value) {
            count = count + f;
        }
        float avg = count / value.size();
        BigDecimal b = new BigDecimal(avg);
        avg = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return avg;
    }

    private Map<String, String> makeTopIdMap(Set<String> projectIds, Map<String, DiathesisProject> projectMap) {
        Map<String, String> returnMap = new HashMap<>();
        for (String s : projectIds) {
            if (!projectMap.containsKey(s)) {
                //数据不正确
                continue;
            }
            DiathesisProject p1 = projectMap.get(s);
            if (p1.getProjectType().equals("1")) {
                //不可能出现
                returnMap.put(s, p1.getId());
                continue;
            }
            if (StringUtils.isBlank(p1.getParentId())) {
                //数据不正确
                continue;
            }
            DiathesisProject p2 = projectMap.get(p1.getParentId());
            if (p2.getProjectType().equals("1")) {
                //一般不可能出现
                returnMap.put(s, p2.getId());
                continue;
            }
            if (StringUtils.isBlank(p2.getParentId())) {
                //数据不正确
                continue;
            }
            DiathesisProject p3 = projectMap.get(p2.getParentId());
            if (p3.getProjectType().equals("1")) {
                returnMap.put(s, p3.getId());
                continue;
            }
            //剩余的为数据不正确
        }
        return returnMap;
    }

    class BetweenValue {
        float maxValue;//不包括
        float minvalue;//包括
        String gValue;//等第

        public BetweenValue(float maxValue, float minvalue, String gValue) {
            this.maxValue = maxValue;
            this.minvalue = minvalue;
            this.gValue = gValue;

        }

        public float getMaxValue() {
            return maxValue;
        }

        public float getMinvalue() {
            return minvalue;
        }

        public String getgValue() {
            return gValue;
        }
    }

    class SwitchScore {
        String stuId;//学生id
        float score;//原始分
        String toScore;//转换后的分数

        public SwitchScore(String stuId, float score) {
            super();
            this.stuId = stuId;
            this.score = score;
        }

        public String getStuId() {
            return stuId;
        }

        public float getScore() {
            return score;
        }

        public String getToScore() {
            return toScore;
        }

        public void setToScore(String toScore) {
            this.toScore = toScore;
        }
    }


    @RequestMapping("/downExcel")
    public void downExcel(String studentId, HttpServletRequest request, HttpServletResponse response) {
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        if (student == null) {
            ExportUtils.outputData(workbook, "综合素质", response);
            return;
        }
        JSONObject obj = findStuReportItem(student);
        DiatheisStudentDto studentDto = (DiatheisStudentDto) obj.get("student");
        String fileName = studentDto.getStudentName() + "综合素质";

        workbook.setSheetName(0, fileName);
        sheet.setDisplayGridlines(false);
        workbook.setSheetName(0, fileName);
        sheet.setDisplayGridlines(false);
        int rowIndex = 0;
        //单元格样式
        HSSFCellStyle style = workbook.createCellStyle();
        //边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        //背景淡灰色 字体加粗
        HSSFCellStyle style2 = workbook.createCellStyle();
        HSSFFont headfont2 = workbook.createFont();
        headfont2.setBold(true);
        style2.setFont(headfont2);
        style2.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style2.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
//        style2.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
//        style2.setFillPattern(FillPatternType.NO_FILL);
        style2.setBorderTop(BorderStyle.THIN);//上边框
        style2.setBorderBottom(BorderStyle.THIN);//下边框
        style2.setBorderLeft(BorderStyle.THIN);//左边框
        style2.setBorderRight(BorderStyle.THIN);//右边框
        HSSFCellStyle style3 = workbook.createCellStyle();
        style3.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style3.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style3.setBorderTop(BorderStyle.THIN);//上边框
        style3.setBorderBottom(BorderStyle.THIN);//下边框
        style3.setBorderLeft(BorderStyle.THIN);//左边框
        style3.setBorderRight(BorderStyle.THIN);//右边框
        HSSFCellStyle style4 = workbook.createCellStyle();
        style4.setAlignment(HorizontalAlignment.LEFT);//水平居左
        style4.setWrapText(true);//自动换行
        style4.setBorderTop(BorderStyle.THIN);//上边框
        style4.setBorderBottom(BorderStyle.THIN);//下边框
        style4.setBorderLeft(BorderStyle.THIN);//左边框
        style4.setBorderRight(BorderStyle.THIN);//右边框
        style4.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //学生综合信息
        HSSFRow row = sheet.createRow(rowIndex);
        CellRangeAddress range = new CellRangeAddress(rowIndex, rowIndex, 0, 7);
        sheet.addMergedRegion(range);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(new HSSFRichTextString("学生综合信息"));
        cell.setCellStyle(style2);
        for (int y = 1; y <= 7; y++) {
            cell = row.createCell(y);
            cell.setCellStyle(style);
        }
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("姓名"));
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getStudentName()));
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("性别"));
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getSex()));
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("民族"));
        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getNation()));
        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("出生年月"));
        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getBirthDate()));
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellValue(new HSSFRichTextString("身份证号"));
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getIdCard()));
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("学校名称"));
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getSchoolName()));
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("学籍号"));
        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getStuCode()));
        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("班级"));
        cell = row.createCell(7);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString(studentDto.getClassName()));
        rowIndex++;


        JSONArray timeList1 = (JSONArray) obj.get("timeList");
        if (timeList1.size() <= 0) {
            ExportUtils.outputData(workbook, fileName, response);
            return;
        }

        List<String> timeIdList = new ArrayList<>();
        Map<String, String> timeMap = new HashMap<>();
        for (Object obj2 : timeList1) {
            JSONObject obj3 = (JSONObject) obj2;
            timeIdList.add(obj3.getString("timeId"));
            timeMap.put(obj3.getString("timeId"), obj3.getString("timeText"));
        }
        List<String[]> data1 = (List<String[]>) obj.get("data1");
        List<DiatheisSubjectScoreDto> data2 = (List<DiatheisSubjectScoreDto>) obj.get("data2");
        List<DiatheisSubjectScoreDto> data3 = (List<DiatheisSubjectScoreDto>) obj.get("data3");
        List<DiatheisSubjectScoreDto> data4 = (List<DiatheisSubjectScoreDto>) obj.get("data4");
        List<DiathesisStuRecordDto> data5 = (List<DiathesisStuRecordDto>) obj.get("data5");


        if (CollectionUtils.isNotEmpty(data1)) {
            int length = timeIdList.size() + 1;
            row = sheet.createRow(rowIndex);
            range = new CellRangeAddress(rowIndex, rowIndex, 0, length - 1);
            sheet.addMergedRegion(range);
            cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString("综合素质考评"));
            cell.setCellStyle(style2);
            for (int y = 1; y <= length - 1; y++) {
                cell = row.createCell(y);
                cell.setCellStyle(style);
            }
            rowIndex++;
            row = sheet.createRow(rowIndex);
            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString(""));
            for (int i = 0; i < length - 1; i++) {
                cell = row.createCell(i + 1);
                cell.setCellStyle(style);
                cell.setCellValue(new HSSFRichTextString(timeMap.get(timeIdList.get(i))));
            }
            rowIndex++;
            for (String[] aa : data1) {
                row = sheet.createRow(rowIndex);
                for (int i = 0; i < aa.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(style);
                    cell.setCellValue(aa[i]);
                }
                rowIndex++;
            }
        }
        if (CollectionUtils.isNotEmpty(data2)) {
            int length = data2.size() + 1;
            row = sheet.createRow(rowIndex);
            if (length - 1 > 1) {
                range = new CellRangeAddress(rowIndex, rowIndex, 0, length - 1);
                sheet.addMergedRegion(range);
            }

            cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString("学业水平"));
            cell.setCellStyle(style2);
            for (int y = 1; y <= length - 1; y++) {
                cell = row.createCell(y);
                cell.setCellStyle(style);
            }
            rowIndex++;
            row = sheet.createRow(rowIndex);
            cell = row.createCell(0);
            cell.setCellValue("科目");
            cell.setCellStyle(style);
            for (int i = 0; i < length - 1; i++) {
                cell = row.createCell(i + 1);
                cell.setCellStyle(style);
                cell.setCellValue(data2.get(i).getSubjectName());
            }
            rowIndex++;
            row = sheet.createRow(rowIndex);
            cell = row.createCell(0);
            cell.setCellValue("成绩");
            cell.setCellStyle(style);
            for (int i = 0; i < length - 1; i++) {
                cell = row.createCell(i + 1);
                cell.setCellStyle(style);
                cell.setCellValue(data2.get(i).getScore());
            }
            rowIndex++;
        }
        //必修
        if (CollectionUtils.isNotEmpty(data3) || CollectionUtils.isNotEmpty(data4)) {
            //学科成绩
//        	 int length = (timeIdList.size())*3+2;//总共列数
            int length = timeIdList.size() + 2;
            row = sheet.createRow(rowIndex);
            range = new CellRangeAddress(rowIndex, rowIndex, 0, length - 1);
            sheet.addMergedRegion(range);
            cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString("学科成绩"));
            cell.setCellStyle(style2);
            for (int y = 1; y <= length - 1; y++) {
                cell = row.createCell(y);
                cell.setCellStyle(style);
            }
            rowIndex++;
            //学科成绩头部
            row = sheet.createRow(rowIndex);
            range = new CellRangeAddress(rowIndex, rowIndex + 1, 0, 0);
            sheet.addMergedRegion(range);
            cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString("类型"));
            cell.setCellStyle(style3);
            range = new CellRangeAddress(rowIndex, rowIndex + 1, 1, 1);
            sheet.addMergedRegion(range);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(new HSSFRichTextString("科目"));

            for (int j = 0; j < timeIdList.size(); j++) {
                cell = row.createCell(j + 2);
//             	cell = row.createCell(j*3+2);

//             	range = new CellRangeAddress(rowIndex,rowIndex,j*3+2,j*3+4);
//                sheet.addMergedRegion(range);
                cell.setCellStyle(style);
                cell.setCellValue(timeMap.get(timeIdList.get(j)));
//                for(int y=j*3+3;y<=j*3+4;y++) {
//                 	cell = row.createCell(y);
//                    cell.setCellStyle(style);
//                }
            }
            rowIndex++;
            row = sheet.createRow(rowIndex);
            for (int y = 0; y <= 1; y++) {
                cell = row.createCell(y);
                cell.setCellStyle(style);
            }
            int in = 2;
            for (int j = 0; j < timeIdList.size(); j++) {
                cell = row.createCell(in);
                cell.setCellStyle(style);
                in++;
//                cell.setCellValue(new HSSFRichTextString("成绩"));
//                cell = row.createCell(in);
//                cell.setCellStyle(style);
//             	in++;
//                cell.setCellValue(new HSSFRichTextString("学分"));
//                cell = row.createCell(in);
//                cell.setCellStyle(style);
//             	in++;
//                cell.setCellValue(new HSSFRichTextString("负责人"));
            }
            rowIndex++;

            if (CollectionUtils.isNotEmpty(data3)) {
                Map<String, DiatheisSubjectScoreDto> mmpa = new HashMap<>();
                List<String> subjectIds = new ArrayList<>();
                List<String> subjectNames = new ArrayList<>();
                for (DiatheisSubjectScoreDto item : data3) {
                    mmpa.put(item.getSubjectId() + "_" + item.getTimeId(), item);
                    if (!subjectIds.contains(item.getSubjectId())) {
                        subjectIds.add(item.getSubjectId());
                        subjectNames.add(item.getSubjectName());
                    }
                }

                row = sheet.createRow(rowIndex);
                //第一列合并
                range = new CellRangeAddress(rowIndex, rowIndex + subjectIds.size() - 1, 0, 0);
                sheet.addMergedRegion(range);
                cell = row.createCell(0);
                cell.setCellValue(new HSSFRichTextString("必修课"));
                cell.setCellStyle(style3);
                rowIndex++;
                for (int j = 0; j < subjectIds.size(); j++) {
                    if (j != 0) {
                        row = sheet.createRow(rowIndex);
                        rowIndex++;
                    }
                    int k = 1;
                    cell = row.createCell(k);
                    cell.setCellValue(new HSSFRichTextString(subjectNames.get(j)));
                    cell.setCellStyle(style);
                    k++;
                    for (int jk = 0; jk < timeIdList.size(); jk++) {
                        String timeId = timeIdList.get(jk);
                        if (mmpa.containsKey(subjectIds.get(j) + "_" + timeId)) {
                            DiatheisSubjectScoreDto m = mmpa.get(subjectIds.get(j) + "_" + timeId);
                            cell = row.createCell(k);
                            cell.setCellValue(new HSSFRichTextString(m.getScore()));
                            cell.setCellStyle(style);
                            k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(m.getCredit()));
//            				 cell.setCellStyle(style);
//            				 k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(m.getChargePerson()));
//            				 cell.setCellStyle(style);
//            				 k++;
                        } else {
                            cell = row.createCell(k);
                            cell.setCellValue(new HSSFRichTextString(""));
                            cell.setCellStyle(style);
                            k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(""));
//            				 cell.setCellStyle(style);
//            				 k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(""));
//            				 cell.setCellStyle(style);
//            				 k++;
                        }
                    }
                }

            }

            if (CollectionUtils.isNotEmpty(data4)) {
                Map<String, DiatheisSubjectScoreDto> mmpa = new HashMap<>();
                List<String> subjectIds = new ArrayList<>();
                List<String> subjectNames = new ArrayList<>();
                for (DiatheisSubjectScoreDto item : data4) {
                    mmpa.put(item.getSubjectId() + "_" + item.getTimeId(), item);
                    if (!subjectIds.contains(item.getSubjectId())) {
                        subjectIds.add(item.getSubjectId());
                        subjectNames.add(item.getSubjectName());
                    }
                }

                row = sheet.createRow(rowIndex);
                //第一列合并
                range = new CellRangeAddress(rowIndex, rowIndex + subjectIds.size() - 1, 0, 0);
                sheet.addMergedRegion(range);
                cell = row.createCell(0);
                cell.setCellValue(new HSSFRichTextString("选修课"));
                cell.setCellStyle(style3);
                rowIndex++;
                for (int j = 0; j < subjectIds.size(); j++) {
                    if (j != 0) {
                        row = sheet.createRow(rowIndex);
                        rowIndex++;
                    }
                    int k = 1;
                    cell = row.createCell(k);
                    cell.setCellValue(new HSSFRichTextString(subjectNames.get(j)));
                    cell.setCellStyle(style);
                    k++;
                    for (int jk = 0; jk < timeIdList.size(); jk++) {
                        String timeId = timeIdList.get(jk);
                        if (mmpa.containsKey(subjectIds.get(j) + "_" + timeId)) {
                            DiatheisSubjectScoreDto m = mmpa.get(subjectIds.get(j) + "_" + timeId);
                            cell = row.createCell(k);
                            cell.setCellValue(new HSSFRichTextString(m.getScore()));
                            cell.setCellStyle(style);
                            k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(m.getCredit()));
//            				 cell.setCellStyle(style);
//            				 k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(m.getChargePerson()));
//            				 cell.setCellStyle(style);
//            				 k++;
                        } else {
                            cell = row.createCell(k);
                            cell.setCellValue(new HSSFRichTextString(""));
                            cell.setCellStyle(style);
                            k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(""));
//            				 cell.setCellStyle(style);
//            				 k++;
//            				 cell = row.createCell(k);
//            				 cell.setCellValue(new HSSFRichTextString(""));
//            				 cell.setCellStyle(style);
//            				 k++;
                        }
                    }
                }

            }
        }

        row = sheet.createRow(rowIndex);
        range = new CellRangeAddress(rowIndex, rowIndex, 0, 4);
        sheet.addMergedRegion(range);
        cell = row.createCell(0);
        cell.setCellValue(new HSSFRichTextString("写实记录"));
        cell.setCellStyle(style2);
        for (int y = 1; y <= 4; y++) {
            cell = row.createCell(y);
            cell.setCellStyle(style);
        }


        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("类型"));
        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("项目"));
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("时间"));
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("内容"));
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue(new HSSFRichTextString("附件"));
        rowIndex++;

        if (CollectionUtils.isNotEmpty(data5)) {
            //key:类型 key:项目 内容
            Map<String, Map<String, List<DiathesisStuRecordDto>>> itemMap = new LinkedHashMap<String, Map<String, List<DiathesisStuRecordDto>>>();

            Map<String, Integer> numMap = new HashMap<>();
            for (DiathesisStuRecordDto item : data5) {
                Map<String, List<DiathesisStuRecordDto>> map1 = itemMap.get(item.getTypeName());
                if (map1 == null) {
                    map1 = new LinkedHashMap<>();
                    itemMap.put(item.getTypeName(), map1);
                }
                List<DiathesisStuRecordDto> l1 = map1.get(item.getProjectName());
                if (l1 == null) {
                    l1 = new ArrayList<>();
                    map1.put(item.getProjectName(), l1);
                }
                l1.add(item);
                if (!numMap.containsKey(item.getTypeName())) {
                    numMap.put(item.getTypeName(), 1);
                } else {
                    numMap.put(item.getTypeName(), numMap.get(item.getTypeName()) + 1);
                }
            }

            for (Entry<String, Map<String, List<DiathesisStuRecordDto>>> kMap : itemMap.entrySet()) {
                String pn = kMap.getKey();
                Map<String, List<DiathesisStuRecordDto>> valueMap = kMap.getValue();
                row = sheet.createRow(rowIndex);
                if (numMap.get(pn) > 1) {
                    range = new CellRangeAddress(rowIndex, rowIndex + numMap.get(pn) - 1, 0, 0);
                    sheet.addMergedRegion(range);
                }

                cell = row.createCell(0);
                cell.setCellValue(new HSSFRichTextString(pn));
                cell.setCellStyle(style3);
                int jRowIndex = 0;//项目是否在合并
                for (Entry<String, List<DiathesisStuRecordDto>> vMap : valueMap.entrySet()) {
                    String tn = vMap.getKey();
                    List<DiathesisStuRecordDto> vList = vMap.getValue();
                    int jRowIndex2 = 0;//内容是否在合并

                    if (jRowIndex == 0) {
                        //不用创建行
                        cell = row.createCell(1);
                        //同一行中
                        cell.setCellValue(new HSSFRichTextString(tn));
                        cell.setCellStyle(style3);
                        //合并
                        if (vList.size() > 1) {
                            range = new CellRangeAddress(rowIndex, rowIndex + vList.size() - 1, 1, 1);
                            sheet.addMergedRegion(range);
                        }

                        rowIndex++;
                    } else {
                        //需要创建行
                        //合并后第二行 需要加一行
                        row = sheet.createRow(rowIndex);
                        cell = row.createCell(0);
                        cell.setCellStyle(style);
                        cell = row.createCell(1);
                        cell.setCellStyle(style3);
                        cell.setCellValue(new HSSFRichTextString(tn));
                        if (vList.size() > 1) {
                            range = new CellRangeAddress(rowIndex, rowIndex + vList.size() - 1, 1, 1);
                            sheet.addMergedRegion(range);
                        }
                        rowIndex++;
                    }
                    jRowIndex++;

                    for (DiathesisStuRecordDto d : vList) {
                        if (jRowIndex2 != 0) {
                            row = sheet.createRow(rowIndex);
                            cell = row.createCell(0);
                            cell.setCellStyle(style);
                            cell = row.createCell(1);
                            cell.setCellStyle(style);
                            rowIndex++;
                        }
                        jRowIndex2++;
                        cell = row.createCell(2);
                        cell.setCellValue(new HSSFRichTextString(timeMap.get(d.getTimeId())));
                        cell.setCellStyle(style3);
                        cell = row.createCell(3);
                        cell.setCellValue(new HSSFRichTextString(d.getTextContent()));
                        cell.setCellStyle(style4);
                        cell = row.createCell(4);
                        if (CollectionUtils.isNotEmpty(d.getFileList())) {
                            String mm = "";
                            for (Object[] s : d.getFileList()) {
                                mm = mm + "；" + s[0].toString();
                            }
                            mm = mm.substring(1);
                            cell.setCellValue(new HSSFRichTextString(mm));
                            cell.setCellStyle(style4);
                        } else {
                            cell.setCellValue(new HSSFRichTextString(""));
                            cell.setCellStyle(style);
                        }
                    }
                }

            }
        }

        ExportUtils.outputData(workbook, fileName, response);

    }

    @RequestMapping("/getStudentInfo")
    public String getStudentInfo(String studentId) {
        if (StringUtils.isNotBlank(studentId)) {
            return getStudentInfoById(studentId).toJSONString();
        }
        JSONArray jsonArr = new JSONArray();
        if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT || getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
            if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
                studentId = SUtils.dc(familyRemoteService.findOneById(getLoginInfo().getOwnerId()), Family.class).getStudentId();
                JSONObject stu = getStudentInfoById(studentId);
                jsonArr.add(stu);
                return jsonArr.toJSONString();
            } else {
                studentId = getLoginInfo().getOwnerId();
                return getStudentInfoById(studentId).toJSONString();
            }
        }
        return error("参数错误");

    }

    /**
     * 获得学生信息
     *
     * @return
     */
    private JSONObject getStudentInfoById(String studentId) {
        JSONObject json = new JSONObject();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);

        json.put("studentId", student.getId());
        json.put("studentName", student.getStudentName());
        json.put("gradeId", grade.getId());
        json.put("gradeCode", grade.getGradeCode());
        Unit unit = SUtils.dc(unitRemoteService.findOneById(student.getSchoolId()), Unit.class);
        if (unit != null && isFuJianReginCode(unit.getRegionCode())) {
            json.put("ifFuJian", "1");
            Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, student.getSchoolId()), Semester.class);
            List<String[]> timeList = new ArrayList<String[]>();
            if (semester == null) {
                timeList.add(new String[]{"", "汇总表"});
            } else {
                timeList = makeSemesterList(semester, grade);
            }
            json.put("timeList", timeList);
        } else {
            json.put("ifFuJian", "0");
        }
        return json;
    }


    /**
     * 福建综合素质查询
     *
     * @param studentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getStuReport/fj")
    public String findStuReportFJ(@RequestParam(required = false) String studentId, String gradeCodeTime) {

        if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_STUDENT) {
            //学生端查询
            studentId = getLoginInfo().getOwnerId();
        } else if (getLoginInfo().getOwnerType() == User.OWNER_TYPE_FAMILY) {
            //todo 家长端  暂时只能对应一个学生
            studentId = SUtils.dc(familyRemoteService.findOneById(getLoginInfo().getOwnerId()), Family.class).getStudentId();
        }
        if (StringUtils.isBlank(studentId)) {
            return error("studentId不能为空");
        }

        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        if (student == null) {
            return "学生数据丢失";
        }
        //gradeCode为空代表汇总表
        JSONObject obj = null;
        if (StringUtils.isNotBlank(gradeCodeTime)) {
            String[] arr = gradeCodeTime.split("_");
            obj = findStuReportItemFJ(student, arr[0], arr[1]);
        } else {
            obj = findStuReportItemFJ(student, null, null);
        }
        System.out.println(obj.toString());
        return obj.toString();
    }

    /**
     * @param dto
     * @param type 1:思想品德 2:学业水平 3:身心健康 4:艺术修养 5:社会实践 6:典型案例实践
     */
    public void makeLsOrZT(DiatheisStudentItemDto dto, String type,
                           Map<String, List<DiathesisProject>> recodeByTopMap,
                           Map<String, DiathesisRecordSet> projectRecodeMap,
                           Map<String, List<DiathesisRecord>> projectRecordMap,
                           Map<String, List<DiathesisStructure>> structureProMap,
                           Map<String, List<DiathesisRecordInfo>> recodeInfoMap, Map<String, String> optionMap) {
        //下属项目
        List<DiathesisProject> childProList = recodeByTopMap.get(dto.getProjectId());
        if (CollectionUtils.isNotEmpty(childProList)) {
            //区分累计与逐条展示
            List<DiathesisProject> ljList = new ArrayList<DiathesisProject>();
            List<DiathesisProject> ztList = new ArrayList<DiathesisProject>();
            //默认犯罪记录只有一个项目
            String fanzuiProjectId = null;
            for (DiathesisProject r : childProList) {
                if ("1".equals(type)) {
                    //犯罪记录---特殊类型 具体内容不展示
                    if (r.getProjectName().contains("犯罪记录")) {
                        fanzuiProjectId = r.getId();
                        continue;
                    }
                }
                DiathesisRecordSet pprr = projectRecodeMap.get(r.getId());
                if (pprr != null && DiathesisConstant.COUNT_TYPE_1.equals(pprr.getCountType())) {
                    ljList.add(r);
                } else {
                    ztList.add(r);
                }
            }
            //累计
            if (CollectionUtils.isNotEmpty(ljList)) {
                List<String> countTitleList = new ArrayList<String>();
                countTitleList.add("活动项目");
                countTitleList.add("累计次数（次）");
                if ("1".equals(type)) {
                    countTitleList.add("累计时间（小时）");
                } else if ("5".equals(type)) {
                    countTitleList.add("累计时间（小时）");
                    countTitleList.add("组织单位");
                }
                countTitleList.add("审核人");
                dto.setCountTitleList(countTitleList);
                List<List<String>> countList = new ArrayList<List<String>>();
                for (DiathesisProject t : ljList) {
                    List<String> countOneList = new ArrayList<String>();
                    countOneList.add(t.getProjectName());
                    List<DiathesisRecord> pr = projectRecordMap.get(t.getId());
                    int countTimes = 0;
                    double countHours = 0;
                    String makeUnitName = "";
                    List<String> tList = new ArrayList<String>();
                    List<DiathesisStructure> colsList = structureProMap.get(t.getId());
                    //找到累计的字段
                    String countKeyId = "";
                    String countKeyName = "";
                    //找到组织单位字段
                    String makeUniyKeyId = "";
                    for (DiathesisStructure d : colsList) {
                        if (StringUtils.isNotBlank(countKeyId) && countKeyName.contains("累计时间")) {

                        } else {
                            if (Objects.equals(d.getIsCount(), 1) && DiathesisConstant.DATA_TYPE_5.equals(d.getDataType())) {
                                //取得累计 数字类型字段  如果由有多项 尽量取含有累计时间
                                if (StringUtils.isBlank(countKeyId)) {
                                    countKeyId = d.getId();
                                    countKeyName = d.getTitle();
                                } else {
                                    if (countKeyName.contains("累计时间")) {
                                        countKeyId = d.getId();
                                        countKeyName = d.getTitle();
                                    }
                                }
                            }
                        }
                        if (d.getTitle().equals("组织单位")) {
                            makeUniyKeyId = d.getId();
                        }
                    }
                    if (CollectionUtils.isNotEmpty(pr)) {
                        //找到统计的字段
                        for (DiathesisRecord r : pr) {
                            countTimes++;
                            if (!tList.contains(r.getAuditor())) {
                                tList.add(r.getAuditor());
                            }
                            if ("1".equals(type) || "5".equals(type)) {
                                //统计时间
                                List<DiathesisRecordInfo> infoList = recodeInfoMap.get(r.getId());
                                Map<String, DiathesisRecordInfo> infoMap = EntityUtils.getMap(infoList, e -> e.getStructureId());
                                if (StringUtils.isNotBlank(countKeyId)) {
                                    if (infoMap.containsKey(countKeyId)) {
                                        String hourStr = infoMap.get(countKeyId).getContentTxt();
                                        if (StringUtils.isNotBlank(hourStr)) {
                                            try {
                                                countHours = countHours + Double.parseDouble(hourStr);
                                            } catch (Exception e) {
                                                log.info(hourStr + "不是数字");
                                            }
                                        }
                                    }
                                }
                                if ("5".equals(type)) {
                                    //根据字段--组织单位
                                    if (StringUtils.isNotBlank(makeUniyKeyId)) {
                                        if (infoMap.containsKey(makeUniyKeyId)) {
                                            makeUnitName = infoMap.get(makeUniyKeyId).getContentTxt();
                                        }
                                    }
                                }
                            }


                        }
                    }
                    String tNames = "";
                    if (CollectionUtils.isNotEmpty(tList)) {
                        tNames = tList.stream().collect(Collectors.joining(","));
                    }
                    countOneList.add(countTimes + "");
                    if ("1".equals(type) || "5".equals(type)) {
                        countOneList.add(countHours + "");
                        if ("5".equals(type)) {
                            countOneList.add(makeUnitName == null ? "" : makeUnitName);
                        }
                    }
                    countOneList.add(tNames);
                    countList.add(countOneList);
                }
                dto.setCountList(countList);
            }
            if (CollectionUtils.isNotEmpty(ztList)) {
                //逐条显示内容
                List<DiatheisStudentItemDto> itemDtoList = new ArrayList<DiatheisStudentItemDto>();
                DiatheisStudentItemDto dt;
                for (DiathesisProject t : ztList) {
                    dt = new DiatheisStudentItemDto();
                    dt.setProjectId(t.getId());
                    dt.setProjectName(t.getProjectName());
                    List<String> titleIdsList = new ArrayList<String>();
                    List<String> countTitleList = new ArrayList<String>();
                    List<List<String>> countList = new ArrayList<List<String>>();
                    List<DiathesisStructure> colsList = structureProMap.get(t.getId());
                    Map<String, DiathesisStructure> titleMap = new HashMap<String, DiathesisStructure>();
                    if (CollectionUtils.isEmpty(colsList)) {
                        continue;
                    }
                    for (DiathesisStructure d : colsList) {
                        if (d.getIsShow() == 1) {
                            //显示
                            countTitleList.add(d.getTitle());
                            titleIdsList.add(d.getId());
                            titleMap.put(d.getId(), d);
                        }
                    }
                    List<DiathesisRecord> pr = projectRecordMap.get(t.getId());
                    if (CollectionUtils.isNotEmpty(pr)) {
                        for (DiathesisRecord r : pr) {
                            List<DiathesisRecordInfo> infoList = recodeInfoMap.get(r.getId());
                            Map<String, DiathesisRecordInfo> map = EntityUtils.getMap(infoList, e -> e.getStructureId());
                            List<String> contentList = new ArrayList<String>();
                            for (String s : titleIdsList) {
                                DiathesisRecordInfo info = map.get(s);
                                if (info == null) {
                                    contentList.add("");
                                } else {
                                    if (StringUtils.isNotBlank(info.getContentTxt())) {
                                        DiathesisStructure titleDto = titleMap.get(s);
                                        if (DiathesisConstant.DATA_TYPE_4.equals(titleDto.getDataType())) {
                                            //附件
                                            contentList.add(StringUtils.substringBefore(info.getContentTxt(), ","));
                                        } else {
                                            String conTxt = "";
                                            if (DiathesisConstant.DATA_TYPE_2.equals(titleDto.getDataType()) || DiathesisConstant.DATA_TYPE_3.equals(titleDto.getDataType())) {
                                                //optionMap
                                                String[] split = info.getContentTxt().split(",");
                                                String contentTxt = "";
                                                for (String sp : split) {
                                                    if (optionMap.containsKey(sp)) {
                                                        contentTxt += "," + optionMap.get(sp);
                                                    }
                                                }
                                                if (StringUtils.isNotBlank(contentTxt)) {
                                                    conTxt = contentTxt.substring(1);
                                                } else {
                                                    conTxt = "";
                                                }

                                            } else {
                                                conTxt = info.getContentTxt();
                                            }

                                            contentList.add(conTxt == null ? "" : conTxt);
                                        }
                                    } else {
                                        contentList.add("");
                                    }

                                }
                            }
                            countList.add(contentList);
                        }
                    }
                    dt.setCountTitleList(countTitleList);
                    dt.setCountList(countList);
                    itemDtoList.add(dt);
                }
                dto.setItemDtoList(itemDtoList);
            }
            if ("1".equals(type)) {
                if (StringUtils.isNotBlank(fanzuiProjectId)) {
                    List<DiathesisStructure> colsList = structureProMap.get(fanzuiProjectId);
                    if (CollectionUtils.isNotEmpty(colsList)) {
                        dto.setCountCrime(1);
                    } else {
                        dto.setCountCrime(0);
                    }
                } else {
                    dto.setCountCrime(0);
                }
            }
        }

    }


    public JSONObject findStuReportItemFJ(Student student, String gradeCode, String semester) {
        JSONObject obj = new JSONObject();

        //根据学生所在的学段---目前只显示学生所在的学段所有学期数据
        Unit unit = SUtils.dc(unitRemoteService.findOneById(student.getSchoolId()), Unit.class);
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
        String sectionMcodeKey = "DM-RKXD-" + grade.getSection();
        String[] mcodeIds = new String[]{"DM-XB", "DM-MZ", "DM-ZZMM", sectionMcodeKey};
        //微代码
        Map<String, Map<String, McodeDetail>> mcodeMap = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(mcodeIds), new TR<Map<String, Map<String, McodeDetail>>>() {
        });

        /***************************1、学生基本信息***********************************/
        DiatheisStudentDto stuDto = new DiatheisStudentDto();
        String bd = DateUtils.date2StringByDay(student.getBirthday());
        stuDto.setBirthDate(bd == null ? "" : bd);
        stuDto.setClassName(grade.getGradeName() + clazz.getClassName());
        stuDto.setIdCard(student.getIdentityCard());
        stuDto.setNation(mcodeMap.get("DM-MZ") == null ? "" : (mcodeMap.get("DM-MZ").get(student.getNation()) == null ? "" : mcodeMap.get("DM-MZ").get(student.getNation()).getMcodeContent()));
        stuDto.setSchoolName(unit.getUnitName());
        if (student.getSex() != null) {
            stuDto.setSex(mcodeMap.get("DM-XB") == null ? "" : (mcodeMap.get("DM-XB").get(student.getSex().toString()) == null ? "" : mcodeMap.get("DM-XB").get(student.getSex().toString()).getMcodeContent()));
        } else {
            stuDto.setSex("");
        }
        stuDto.setStuCode(student.getStudentCode());
        stuDto.setStudentName(student.getStudentName());

        stuDto.setStrong(student.getStrong() == null ? "" : student.getStrong());
        stuDto.setUnitiveCode(student.getUnitiveCode());
        stuDto.setBackground(mcodeMap.get("DM-ZZMM") == null ? "" : (mcodeMap.get("DM-ZZMM").get(student.getBackground()) == null ? "" : mcodeMap.get("DM-ZZMM").get(student.getBackground()).getMcodeContent()));
        stuDto.setLinkAddress(student.getLinkAddress() == null ? "" : student.getLinkAddress());
        stuDto.setLinkPhone(student.getLinkPhone() == null ? "" : student.getLinkPhone());
        stuDto.setToSchoolDate(student.getToSchoolDate() == null ? "" : DateUtils.date2String(student.getToSchoolDate(), "yyyy"));
        //图片--- String base64
        String fileUrl = makePicUrl(student);
        stuDto.setStuPicUrl(fileUrl);

        List<DiathesisEvaluate> evaluateList = diathesisEvaluateService.findByUnitIdAndStudentId(student.getSchoolId(), student.getId());
        String selfContent = "";
        String teacherContent = "";
        if (CollectionUtils.isNotEmpty(evaluateList)) {
            //取最近的
            for (DiathesisEvaluate n : evaluateList) {
                if (StringUtils.isNotBlank(teacherContent) && StringUtils.isNotBlank(selfContent)) {
                    break;
                }
                //暂时不是学生自己 就是老师评价
                if (StringUtils.isNotBlank(n.getTeacherId())) {
                    if (StringUtils.isBlank(teacherContent)) {
                        teacherContent = n.getContentTxt();
                    }
                } else {
                    if (StringUtils.isBlank(selfContent)) {
                        selfContent = n.getContentTxt();
                    }
                }
            }
        }
        //自我陈述报告
        stuDto.setSelfContent(selfContent);
        //教师评语
        stuDto.setTeacherContent(teacherContent);

        obj.put("student", stuDto);
        /********************************************************************************/
        //限定内容
        String[] headTitle = new String[]{"思想品德", "学业水平", "身心健康", "艺术素养", "社会实践", "典型案例实践"};
        List<String> headTitleList = Arrays.asList(headTitle);
        String unitId = unit.getId();
        List<DiathesisRecord> recodeList = new ArrayList<DiathesisRecord>();
        boolean isAll = false;
        //成绩主表
        List<DiathesisScoreType> scoreTypeList = new ArrayList<DiathesisScoreType>();
        String[] types = new String[]{DiathesisConstant.INPUT_TYPE_1, DiathesisConstant.INPUT_TYPE_2};
        //所有
        scoreTypeList = diathesisScoreTypeService.findByUnitIdAndGradeIdAndTypeIn(unitId, grade.getId(), types);
        if (StringUtils.isNotBlank(gradeCode)) {
            //学期
            recodeList = diathesisRecordService.findListByGradeCodeAndStuId(unitId, student.getId(), gradeCode, DiathesisConstant.AUDIT_STATUS_PASS);
            scoreTypeList = scoreTypeList.stream().filter(e -> {
                return (e.getType().equals(DiathesisConstant.INPUT_TYPE_2) || e.getType().equals(DiathesisConstant.INPUT_TYPE_1)) && (e.getGradeCode().equals(gradeCode)
                        && e.getSemester() == Integer.parseInt(semester));
            }).collect(Collectors.toList());
        } else {
            //汇总
            isAll = true;
            recodeList = diathesisRecordService.findListByStuId(unitId, student.getId(), DiathesisConstant.AUDIT_STATUS_PASS);
        }
        List<DiathesisScoreInfo> scoreInfoList = new ArrayList<DiathesisScoreInfo>();
        if (CollectionUtils.isNotEmpty(scoreTypeList)) {
            Set<String> scoreTypeIds = EntityUtils.getSet(scoreTypeList, e -> e.getId());
            scoreInfoList = diathesisScoreInfoService.findByStudentIdAndScoreTypeIdIn(student.getId(), scoreTypeIds.toArray(new String[0]));
        }


        //学校需要展示的项目 //1、获取根项目与写实项目（写实项目直接挂在根项目中）
        String[] projectTypes = new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_RECORD};
        //这个根据类型获取值
        List<DiathesisProject> projectList = diathesisProjectService.findByUnitIdAndProjectTypeIn(unitId, projectTypes);

        /*****
         * doto
         */
        //防止历史数据 这个地方+写实记录中已经存在得项目
//		if(CollectionUtils.isNotEmpty(recodeList)) {
//			Set<String> pIds = EntityUtils.getSet(projectList, e->e.getId());
//			Set<String> oldPIds = EntityUtils.getSet(recodeList, e->e.getProjectId());
//			oldPIds.removeAll(pIds);
//			if(CollectionUtils.isNotEmpty(oldPIds)) {
//				//历史项目
//				List<DiathesisProject> oldProjectList = diathesisProjectService.findListByIdIn(oldPIds.toArray(new String[0]));
//				if(CollectionUtils.isNotEmpty(oldProjectList)) {
//					Set<String> ppIds = EntityUtils.getSet(oldProjectList, e->e.getParentId());
//					if(CollectionUtils.isNotEmpty(ppIds)) {
//						List<DiathesisProject> oldTopProjectList = diathesisProjectService.findListByIdIn(ppIds.toArray(new String[0]));
//					}
//				}
//			}
//		}


        Map<String, DiathesisProject> projectMap = EntityUtils.getMap(projectList, e -> e.getId());
        List<DiathesisProject> topProjectList = projectList.stream().filter(e -> DiathesisConstant.PROJECT_TOP.equals(e.getProjectType())).collect(Collectors.toList());

        List<DiathesisProject> recodeProjectList = projectList.stream().filter(e -> DiathesisConstant.PROJECT_RECORD.equals(e.getProjectType())).collect(Collectors.toList());

        Map<String, List<DiathesisProject>> recodeByTopMap = new HashMap<String, List<DiathesisProject>>();
        //对应是否累计还是逐条显示
        Map<String, DiathesisRecordSet> projectRecodeMap = new HashMap<String, DiathesisRecordSet>();

        Map<String, List<DiathesisStructure>> structureProMap = new HashMap<String, List<DiathesisStructure>>();
        //选项Map
        Map<String, String> optionMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(recodeProjectList)) {
            recodeByTopMap = EntityUtils.getListMap(recodeProjectList, DiathesisProject::getParentId, e -> e);
            Set<String> rpIds = EntityUtils.getSet(recodeProjectList, e -> e.getId());
            List<DiathesisRecordSet> projectRecodeList = diathesisRecordSetService.findListByIdIn(rpIds.toArray(new String[0]));
            projectRecodeMap = EntityUtils.getMap(projectRecodeList, e -> e.getId());

            //写实记录的具体属性
            List<DiathesisStructure> structureProList = diathesisStructureService.findListByProjectIdIn(rpIds.toArray(new String[0]));
            structureProMap = EntityUtils.getListMap(structureProList, DiathesisStructure::getProjectId, e -> e);

            if (CollectionUtils.isNotEmpty(structureProList)) {
                Set<String> ids = EntityUtils.getSet(structureProList, e -> e.getId());
                String usingUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);
                optionMap = diathesisOptionService.findMapByUnitIdAndStructureIdIn(usingUnitId, ids.toArray(new String[]{}));
            }
        }


        List<DiatheisStudentItemDto> dtoList = new ArrayList<DiatheisStudentItemDto>();
        DiatheisStudentItemDto dto = null;
        for (String title : headTitle) {
            dto = new DiatheisStudentItemDto();
            dto.setProjectName(title);
            dtoList.add(dto);
        }
        //不考虑同名情况
        for (DiathesisProject top : topProjectList) {
            if (!headTitleList.contains(top.getProjectName())) {
                continue;
            }
            for (int i = 0; i < headTitle.length; i++) {
                if (top.getProjectName().equals(headTitle[i])) {
                    dtoList.get(i).setProjectId(top.getId());
                    break;
                }
            }
        }
        DiatheisStudentItemDto dto1 = dtoList.get(0);
        DiatheisStudentItemDto dto2 = dtoList.get(1);
        DiatheisStudentItemDto dto3 = dtoList.get(2);
        DiatheisStudentItemDto dto4 = dtoList.get(3);
        DiatheisStudentItemDto dto5 = dtoList.get(4);
        DiatheisStudentItemDto dto6 = dtoList.get(5);
        if (CollectionUtils.isNotEmpty(recodeList)) {
            //写实记录下的细化
            Map<String, List<DiathesisRecord>> projectRecordMap = new HashMap<String, List<DiathesisRecord>>();
            for (DiathesisRecord r : recodeList) {
                if (!projectMap.containsKey(r.getProjectId())) {
                    //找不到挂在的项目下
                    continue;
                }
                if (!projectRecordMap.containsKey(r.getProjectId())) {
                    projectRecordMap.put(r.getProjectId(), new ArrayList<DiathesisRecord>());
                }
                projectRecordMap.get(r.getProjectId()).add(r);
            }


            //学生写实记录具体信息
            Set<String> recordIds = EntityUtils.getSet(recodeList, e -> e.getId());
            List<DiathesisRecordInfo> recodeInfoList = diathesisRecordInfoService.findListByUnitIdAndRecordIds(unitId, recordIds.toArray(new String[]{}));
            //key:recordId
            Map<String, List<DiathesisRecordInfo>> recodeInfoMap = EntityUtils.getListMap(recodeInfoList, DiathesisRecordInfo::getRecordId, e -> e);


            //"思想品德",
            if (StringUtils.isNotBlank(dto1.getProjectId())) {
                makeLsOrZT(dto1, "1", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
            }

            /*********************学业水平************************/
            //"学业水平",
            if (StringUtils.isNotBlank(dto2.getProjectId())) {
                makeLsOrZT(dto2, "2", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
                //各个成绩

            }
            /*********************学业水平end************************/
            //"身心健康",
            if (StringUtils.isNotBlank(dto3.getProjectId())) {
                makeLsOrZT(dto3, "3", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
            }
            //"艺术修养",
            if (StringUtils.isNotBlank(dto4.getProjectId())) {
                makeLsOrZT(dto4, "4", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
            }
            //"社会实践",
            if (StringUtils.isNotBlank(dto5.getProjectId())) {
                makeLsOrZT(dto5, "5", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
            }
            //"典型案例实践"
            if (StringUtils.isNotBlank(dto5.getProjectId())) {
                makeLsOrZT(dto6, "5", recodeByTopMap, projectRecodeMap, projectRecordMap, structureProMap, recodeInfoMap, optionMap);
            }

        }
        //必修和选修I（A）课程成绩---必修课程成绩
        //选修I（B）课程成绩
        //选修II课程成绩
        List<DiathesisSubjectField> subjectFileList = diathesisSubjectFieldService.findByUnitId(unitId);
        Map<String, List<DiathesisSubjectField>> filedMap = EntityUtils.getListMap(subjectFileList, DiathesisSubjectField::getSubjectType, e -> e);
        //组装学业水平显示数据isAll

        /*********必修******************/
        DiatheisStudentItemDto bxScoreDto = new DiatheisStudentItemDto();
        bxScoreDto.setProjectName("必修课程成绩");
        List<String> countTitleList1 = new ArrayList<String>();
        List<String> countTitleIds1 = new ArrayList<String>();
        countTitleList1.add("科目");
        if (isAll) {
            Map<String, McodeDetail> xdMap = new HashMap<>();
            if (mcodeMap.containsKey(sectionMcodeKey)) {
                for (Entry<String, McodeDetail> item : mcodeMap.get(sectionMcodeKey).entrySet()) {
                    //31 32 33 34....
                    xdMap.put(grade.getSection() + item.getValue().getThisId(), item.getValue());
                }
            }
            //显示具体学期
            for (int i = 1; i <= grade.getSchoolingLength(); i++) {
                String mm = grade.getSection() + "" + i;
                if (!xdMap.containsKey(mm)) {
                    continue;
                }
                String ss1 = xdMap.get(mm).getMcodeContent();
                countTitleList1.add(ss1 + "上");
                countTitleIds1.add(mm + "1");
                countTitleList1.add(ss1 + "下");
                countTitleIds1.add(mm + "2");
            }
        } else {
            countTitleList1.add("成绩");
            if (filedMap.containsKey(DiathesisConstant.SUBJECT_FEILD_BX)) {
                for (DiathesisSubjectField tt : filedMap.get(DiathesisConstant.SUBJECT_FEILD_BX)) {
                    if ("成绩".equals(tt.getFieldName())) {
                        continue;
                    }
                    if ("1".equals(tt.getIsUsing())) {
                        countTitleList1.add(tt.getFieldName());
                        countTitleIds1.add(tt.getFieldCode());
                    }
                }
            }
        }

        bxScoreDto.setCountTitleList(countTitleList1);
        bxScoreDto.setCountList(new ArrayList<List<String>>());
        /*********学业水平考试******************/
        DiatheisStudentItemDto xyspScoreDto = new DiatheisStudentItemDto();
        xyspScoreDto.setProjectName("学业水平考试成绩");
        List<String> countTitleList2 = new ArrayList<String>();
        List<String> countTitleIds2 = new ArrayList<String>();
        countTitleList2.add("科目");
        if (filedMap.containsKey(DiathesisConstant.SUBJECT_FEILD_XY)) {
            for (DiathesisSubjectField tt : filedMap.get(DiathesisConstant.SUBJECT_FEILD_XY)) {
                if ("1".equals(tt.getIsUsing())) {
                    countTitleList2.add(tt.getFieldName());
                    countTitleIds2.add(tt.getFieldCode());
                }
            }
        }
        xyspScoreDto.setCountTitleList(countTitleList2);
        xyspScoreDto.setCountList(new ArrayList<List<String>>());
        /*********选修I（B）课程成绩******************/
        DiatheisStudentItemDto xj1ScoreDto = new DiatheisStudentItemDto();
        xj1ScoreDto.setProjectName("选修I（B）课程成绩");
        List<String> countTitleList3 = new ArrayList<String>();
        List<String> countTitleIds3 = new ArrayList<String>();
        countTitleList3.add("科目");
        countTitleList3.add("成绩");
        if (filedMap.containsKey(DiathesisConstant.SUBJECT_FEILD_XX)) {
            for (DiathesisSubjectField tt : filedMap.get(DiathesisConstant.SUBJECT_FEILD_XX)) {
                if ("成绩".equals(tt.getFieldName())) {
                    continue;
                }
                if ("1".equals(tt.getIsUsing())) {
                    countTitleList3.add(tt.getFieldName());
                    countTitleIds3.add(tt.getFieldCode());
                }
            }
        }
        xj1ScoreDto.setCountTitleList(countTitleList3);
        xj1ScoreDto.setCountList(new ArrayList<List<String>>());
        /*********选修II课程成绩******************/
        DiatheisStudentItemDto xj2ScoreDto = new DiatheisStudentItemDto();
        xj2ScoreDto.setProjectName("选修II课程成绩");
        List<String> countTitleList4 = new ArrayList<String>();
        countTitleList4.addAll(countTitleList3);
        List<String> countTitleIds4 = new ArrayList<String>();
        countTitleIds4.addAll(countTitleList4);
        xj2ScoreDto.setCountTitleList(countTitleList4);
        xj2ScoreDto.setCountList(new ArrayList<List<String>>());

        if (CollectionUtils.isNotEmpty(scoreInfoList)) {
            Set<String> scoreInfoIds = EntityUtils.getSet(scoreInfoList, e -> e.getId());
            List<DiathesisScoreInfoEx> infoExList = diathesisScoreInfoExService.findListByInfoIdIn(scoreInfoIds.toArray(new String[0]));
            Map<String, List<DiathesisScoreInfoEx>> infoExMap = EntityUtils.getListMap(infoExList, DiathesisScoreInfoEx::getScoreInfoId, e -> e);
            Set<String> subjectIds = EntityUtils.getSet(scoreInfoList, e -> e.getObjId());
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            Map<String, Course> courseMap = EntityUtils.getMap(courseList, e -> e.getId());

            Set<String> courseTypeIdset = EntityUtils.getSet(courseList, e -> e.getCourseTypeId());
            //课程类型
            Map<String, String> courseTypeMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(courseTypeIdset)) {
                courseTypeMap = EntityUtils.getMap(SUtils.dt(courseTypeRemoteService.findListByIds(courseTypeIdset.toArray(new String[0])), CourseType.class), x -> x.getId(), x -> StringUtils.defaultString(x.getName(), ""));
            }


            Map<String, DiathesisScoreType> scoreTypeMap = EntityUtils.getMap(scoreTypeList, e -> e.getId());


            //选修成绩id
            Set<String> xxScoreTypeIds = scoreTypeList.stream().filter(e -> DiathesisConstant.INPUT_TYPE_1.equals(e.getType()) && DiathesisConstant.SCORE_TYPE_2.equals(e.getScoreType()))
                    .map(e -> e.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
            //选修所有成绩
            List<DiathesisScoreInfo> xxScoreList = scoreInfoList.stream().filter(e -> xxScoreTypeIds.contains(e.getScoreTypeId())).collect(Collectors.toList());


            //学业水平成绩id
            Set<String> xyScoreTypeIds = scoreTypeList.stream().filter(e -> DiathesisConstant.INPUT_TYPE_2.equals(e.getType()))
                    .map(e -> e.getId()).filter(Objects::nonNull).collect(Collectors.toSet());
            //学业水平的成绩
            List<DiathesisScoreInfo> xyScoreList = scoreInfoList.stream().filter(e -> xyScoreTypeIds.contains(e.getScoreTypeId())).collect(Collectors.toList());

            //学业水平下所有科目ids
            Set<String> subjectXyIds = new HashSet<String>();

            //key:subjectId value countTitleIds3下成绩
            Map<String, List<String>> xyScoreMap = new HashMap<String, List<String>>();
            if (CollectionUtils.isNotEmpty(xyScoreList)) {
                for (DiathesisScoreInfo d : xyScoreList) {
                    subjectXyIds.add(d.getObjId());
                    List<DiathesisScoreInfoEx> exList = infoExMap.get(d.getId());
                    if (CollectionUtils.isEmpty(exList)) {
                        continue;
                    }
                    if (CollectionUtils.isEmpty(countTitleIds3)) {
                        //一般不会出现
                        continue;
                    }
                    Map<String, DiathesisScoreInfoEx> valueMap = EntityUtils.getMap(exList, e -> e.getFieldCode());
                    List<String> oldScoreList = xyScoreMap.get(d.getObjId());
                    if (CollectionUtils.isEmpty(oldScoreList)) {
                        oldScoreList = new ArrayList<String>();
                        for (int i = 0; i < countTitleIds2.size(); i++) {
                            DiathesisScoreInfoEx vv = valueMap.get(countTitleIds2.get(i));
                            oldScoreList.add(vv == null ? "" : (vv.getFieldValue() == null ? "" : vv.getFieldValue()));
                        }
                        xyScoreMap.put(d.getObjId(), oldScoreList);
                    } else {
                        for (int i = 0; i < countTitleIds2.size(); i++) {
                            DiathesisScoreInfoEx vv = valueMap.get(countTitleIds2.get(i));
                            String v = vv == null ? "" : (vv.getFieldValue() == null ? "" : vv.getFieldValue());
                            if (StringUtils.isNotBlank(v)) {
                                if (StringUtils.isBlank(oldScoreList.get(i))) {
                                    oldScoreList.set(i, v);
                                } else {
                                    //ABCD
                                    if (v.compareToIgnoreCase(oldScoreList.get(i)) < 0) {
                                        oldScoreList.set(i, v);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //学科成绩
            if (CollectionUtils.isNotEmpty(xxScoreList)) {
                for (DiathesisScoreInfo info : xxScoreList) {
                    if (!courseMap.containsKey(info.getObjId())) {
                        continue;
                    }
                    Course subject = courseMap.get(info.getObjId());
                    if (subject.getType().equals(BaseConstants.SUBJECT_TYPE_XX_5)) {
                        //5 xj1ScoreDto
                        List<String> ll = new ArrayList<>();
                        ll.add(subject.getSubjectName());
                        if (CollectionUtils.isEmpty(countTitleIds3)) {
                            //一般不会出现
                            continue;
                        }
                        List<DiathesisScoreInfoEx> exList = infoExMap.get(info.getId());
                        if (CollectionUtils.isEmpty(exList)) {
                            ll.add("");
                            for (int i = 0; i < countTitleIds3.size(); i++) {
                                if (countTitleList3.get(i).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else if (countTitleList3.get(i).equals("选修类型")) {
                                    ll.add("选修Ⅰ-B");
                                } else {
                                    ll.add("");
                                }
                            }
                        } else {
                            Map<String, DiathesisScoreInfoEx> valueMap = EntityUtils.getMap(exList, e -> e.getFieldCode());
                            ll.add(info.getScore() == null ? "" : info.getScore());
                            for (int i = 0; i < countTitleIds3.size(); i++) {
                                if (countTitleList3.get(i + 2).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else if (countTitleList3.get(i + 2).equals("选修类型")) {
                                    ll.add("选修Ⅰ-B");
                                } else {
                                    DiathesisScoreInfoEx vv = valueMap.get(countTitleIds3.get(i));
                                    String v = vv == null ? "" : (vv.getFieldValue() == null ? "" : vv.getFieldValue());
                                    ll.add(v);
                                }

                            }
                        }

                        xj1ScoreDto.getCountList().add(ll);
                    } else if (subject.getType().equals(BaseConstants.SUBJECT_TYPE_XX_6)) {
                        //6 xj2ScoreDto
                        List<String> ll = new ArrayList<>();
                        ll.add(subject.getSubjectName());
                        if (CollectionUtils.isEmpty(countTitleIds3)) {
                            //一般不会出现
                            continue;
                        }
                        List<DiathesisScoreInfoEx> exList = infoExMap.get(info.getId());
                        if (CollectionUtils.isEmpty(exList)) {
                            ll.add("");//成绩
                            for (int i = 0; i < countTitleIds3.size(); i++) {
                                if (countTitleList3.get(i + 2).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else if (countTitleList3.get(i + 2).equals("选修类型")) {
                                    ll.add("选修Ⅱ");
                                } else {
                                    ll.add("");
                                }
                            }
                        } else {
                            Map<String, DiathesisScoreInfoEx> valueMap = EntityUtils.getMap(exList, e -> e.getFieldCode());
                            ll.add(info.getScore() == null ? "" : info.getScore());
                            for (int i = 0; i < countTitleIds3.size(); i++) {
                                if (countTitleList3.get(i + 2).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else if (countTitleList3.get(i + 2).equals("选修类型")) {
                                    ll.add("选修Ⅱ");
                                } else {
                                    DiathesisScoreInfoEx vv = valueMap.get(countTitleIds3.get(i));
                                    String v = vv == null ? "" : (vv.getFieldValue() == null ? "" : vv.getFieldValue());
                                    ll.add(v);
                                }
                            }
                        }

                        xj2ScoreDto.getCountList().add(ll);
                    } else {
                        //其他不显示
                    }

                }
            }
            //课程类型 暂定取科目对应的课程类型 不根据id获取///DOTO

            //必修成绩
            List<DiathesisScoreInfo> bxScoreList = scoreInfoList.stream().filter(e -> !xyScoreTypeIds.contains(e.getScoreTypeId()) && !xxScoreTypeIds.contains(e.getScoreTypeId())).collect(Collectors.toList());
            if (isAll) {
                //必修成绩 直接根据scoreType类型区分
                Map<String, Map<String, String>> sub_time_score = new HashMap<String, Map<String, String>>();
                for (DiathesisScoreInfo info : bxScoreList) {
                    if (!courseMap.containsKey(info.getObjId())) {
                        continue;
                    }
                    if (!sub_time_score.containsKey(info.getObjId())) {
                        sub_time_score.put(info.getObjId(), new HashMap<String, String>());
                    }
                    sub_time_score.get(info.getObjId()).put(scoreTypeMap.get(info.getScoreTypeId()).getGradeCode() + scoreTypeMap.get(info.getScoreTypeId()).getSemester(), info.getScore());
                }
                subjectXyIds.addAll(sub_time_score.keySet());
                List<String> allSubjectIds = new ArrayList<String>();
                //有序
                allSubjectIds.addAll(subjectXyIds);
                //学业水平
                for (String subId : allSubjectIds) {
                    List<String> ll = new ArrayList<String>();
                    ll.add(courseMap.get(subId).getSubjectName());
                    if (xyScoreMap.containsKey(subId)) {
                        ll.addAll(xyScoreMap.get(subId));
                    } else {
                        for (int i = 0; i < countTitleIds2.size(); i++) {
                            ll.add("");
                        }
                    }
                    xyspScoreDto.getCountList().add(ll);
                }
                //必修成绩
                for (String subId : allSubjectIds) {
                    List<String> ll = new ArrayList<String>();
                    ll.add(courseMap.get(subId).getSubjectName());
                    if (sub_time_score.containsKey(subId)) {
                        Map<String, String> timeScoreMap = sub_time_score.get(subId);
                        for (int i = 0; i < countTitleIds1.size(); i++) {
                            if (timeScoreMap.containsKey(countTitleIds1.get(i))) {
                                ll.add(timeScoreMap.get(countTitleIds1.get(i)));
                            } else {
                                ll.add("");
                            }
                        }
                    } else {
                        for (int i = 0; i < countTitleIds1.size(); i++) {
                            ll.add("");
                        }
                    }
                    bxScoreDto.getCountList().add(ll);
                }


            } else {
                if (CollectionUtils.isNotEmpty(bxScoreList)) {
                    for (DiathesisScoreInfo info : bxScoreList) {
                        if (!courseMap.containsKey(info.getObjId())) {
                            continue;
                        }
                        Course subject = courseMap.get(info.getObjId());
                        List<String> ll = new ArrayList<>();
                        ll.add(subject.getSubjectName());
                        ll.add(info.getScore() == null ? "" : info.getScore());
                        if (CollectionUtils.isEmpty(countTitleIds1)) {
                            //一般不会出现
                            continue;
                        }
                        List<DiathesisScoreInfoEx> exList = infoExMap.get(info.getId());
                        if (CollectionUtils.isEmpty(exList)) {
                            for (int i = 0; i < countTitleIds1.size(); i++) {
                                if (countTitleList1.get(i + 2).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else {
                                    ll.add("");
                                }

                            }
                        } else {
                            Map<String, DiathesisScoreInfoEx> valueMap = EntityUtils.getMap(exList, e -> e.getFieldCode());
                            for (int i = 0; i < countTitleIds1.size(); i++) {
                                if (countTitleList1.get(i + 2).equals("课程类型")) {
                                    //取固定课程等
                                    if (courseTypeMap.containsKey(subject.getCourseTypeId())) {
                                        ll.add(courseTypeMap.get(subject.getCourseTypeId()));
                                    } else {
                                        ll.add("");
                                    }
                                } else {
                                    DiathesisScoreInfoEx vv = valueMap.get(countTitleIds1.get(i));
                                    String v = vv == null ? "" : (vv.getFieldValue() == null ? "" : vv.getFieldValue());
                                    ll.add(v);
                                }

                            }
                        }
                        bxScoreDto.getCountList().add(ll);
                    }
                }

                //学业水平
                for (String subId : subjectXyIds) {
                    List<String> ll = new ArrayList<String>();
                    ll.add(courseMap.get(subId).getSubjectName());
                    if (xyScoreMap.containsKey(subId)) {
                        ll.addAll(xyScoreMap.get(subId));
                    } else {
                        for (int i = 0; i < countTitleIds2.size(); i++) {
                            ll.add("");
                        }
                    }
                    xyspScoreDto.getCountList().add(ll);
                }
            }

        }
        obj.put("data1", dto1);
        dto2.setBjScoreDto(bxScoreDto);
        dto2.setXj1ScoreDto(xj1ScoreDto);
        dto2.setXj2ScoreDto(xj2ScoreDto);
        dto2.setXyspScoreDto(xyspScoreDto);
        obj.put("data2", dto2);
        obj.put("data3", dto3);
        obj.put("data4", dto4);
        obj.put("data5", dto5);
        obj.put("data6", dto6);
        return obj;

    }

    public boolean isFuJianReginCode(String reginCode) {
        if (StringUtils.isNotBlank(reginCode) && reginCode.startsWith(DiathesisConstant.FUJIAN_REGION_CODE)) {
            return true;
        }
        return false;
    }

    public String makePicUrl(Student student) {
        String picUrl = "";
        if (StringUtils.isNotEmpty(student.getFilePath())) {
            String fileSystemPath = filePathRemoteService.getFilePath();// 文件系统地址
            picUrl = fileSystemPath + File.separator + student.getFilePath();
        }
        // picUrl="g:/11/22.jpg";
        String base64 = null;
        InputStream in = null;

        if (StringUtils.isNotBlank(picUrl)) {
            try {
                File file = new File(picUrl);
                in = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                in.read(bytes);
                base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return base64;
        }
        return null;
    }

}

