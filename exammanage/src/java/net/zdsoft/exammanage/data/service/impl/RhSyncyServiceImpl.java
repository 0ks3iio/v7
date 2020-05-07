package net.zdsoft.exammanage.data.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.dto.CdExamDto;
import net.zdsoft.exammanage.data.dto.CdSaveSysncyDto;
import net.zdsoft.exammanage.data.dto.CdStudentDto;
import net.zdsoft.exammanage.data.dto.CdSubjectDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

@Service("rhSyncyService")
public class RhSyncyServiceImpl implements RhSyncyService {

    // 创建预编译语句对象，一般都是用这个而不用Statement
    PreparedStatement ps = null;
    // 创建一个结果集对象
    ResultSet rs = null;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private RhKeyUnitRemoteService rhKeyUnitRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmClassLockService emClassLockService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmTitleInfoService emTitleInfoService;
    @Autowired
    private EmTitleScoreService emTitleScoreService;
    private String dirverName;
    private String url;
    private String username;
    private String password;
    // 创建一个数据库连接
    private Connection connection = null;

    @Override
    public String findExamList(String unitId, String acadyear, String semester) {
        JSONObject json = new JSONObject();
        Semester sem = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(acadyear, Integer.parseInt(semester), unitId), Semester.class);
        if (sem == null) {
            json.put("success", false);
            json.put("msg", "学年学期未维护");
            return json.toString();
        }
        //学期范围 [前一个学期的开始,后一个学期的开始(不包括))
        Date start1 = sem.getWorkBegin();
        Date end1 = sem.getWorkBegin();
        Semester sem2 = null;
        if ("1".equals(semester)) {
            sem2 = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(acadyear, 2, unitId), Semester.class);
        } else {
            //acadyear
            String[] arr = acadyear.split("-");
            String acadyear2 = (Integer.parseInt(arr[0]) + 1) + "-" + (Integer.parseInt(arr[1]) + 1);
            sem2 = SUtils.dc(semesterRemoteService.findByAcadyearAndSemester(acadyear2, 2, unitId), Semester.class);
        }
        if (sem2 != null) {
            //开始前一天
            end1 = DateUtils.addDay(sem2.getWorkBegin(), -1);
        }

        List<RhKeyUnit> keyList = SUtils.dt(rhKeyUnitRemoteService.findByUnitIdAnduKeyId(unitId, null), new TR<List<RhKeyUnit>>() {
        });
        if (CollectionUtils.isEmpty(keyList)) {
            json.put("success", false);
            json.put("msg", "未找到对应ukey数据");
            return json.toString();
        }
        //如果多个默认取一个
        RhKeyUnit key = keyList.get(0);

        init("", key.getDbUrl(), key.getDbUsername(), key.getDbPassword());
        connection = getConnection();
        if (connection == null) {
            json.put("success", false);
            json.put("msg", "连接数据库失败");
            return json.toString();
        }
        String startTime = DateUtils.date2StringByDay(start1);
        String endTime = DateUtils.date2StringByDay(end1);
        String sql = "select * from cd_exam where ownedby = '" + key.getUkey() + "' and sdate between to_date('" + startTime + "','yyyy-mm-dd') and to_date('" + endTime + "','yyyy-mm-dd') order by sdate desc";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        List<CdExamDto> examList = new ArrayList<>();
        CdExamDto dto = null;
        try {
            while (rs.next()) {
                dto = new CdExamDto();
                dto.setExamKey(rs.getString("examId"));
                dto.setExamName(rs.getString("examname"));
                dto.setExamTableName(rs.getString("dbuser"));
                dto.setOwnedby(rs.getString("ownedby"));
                examList.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        json.put("success", true);
        json.put("examList", examList);
        //关闭
        releaseResource();
        return json.toString();
    }

    //科目列表
    public String findSubjectList(String unitId, String acadyear, String semester, String examUid, String uKeyId) {
        JSONObject json = new JSONObject();
        List<RhKeyUnit> keyList = SUtils.dt(rhKeyUnitRemoteService.findByUnitIdAnduKeyId(unitId, uKeyId), new TR<List<RhKeyUnit>>() {
        });
        if (CollectionUtils.isEmpty(keyList)) {
            json.put("success", false);
            json.put("msg", "未找到对应ukey数据");
            return json.toString();
        }
        //如果多个默认取一个
        RhKeyUnit key = keyList.get(0);
        //创建数据库连接
        init("", key.getDbUrl(), key.getDbUsername(), key.getDbPassword());
        connection = getConnection();
        if (connection == null) {
            json.put("success", false);
            json.put("msg", "连接数据库失败");
            return json.toString();
        }
        //组装数据
        String sql = "select * from cd_exam where ownedby = '" + key.getUkey() + "' and examId='" + examUid + "'  order by sdate desc";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("cd_exam Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        //考试不可能有多条
        CdExamDto dto = null;
        try {
            if (rs.next()) {
                dto = new CdExamDto();
                dto.setExamKey(rs.getString("examId"));
                dto.setExamName(rs.getString("examname"));
                dto.setExamTableName(rs.getString("dbuser"));
                dto.setOwnedby(rs.getString("ownedby"));
                dto.setGradeName(rs.getString("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }

        //找到考试的科目
        String sql2 = "Select * from " + dto.getExamTableName() + ".CD_SUBJECT_PREREPORT1 order by subjectid";
        try {
            ps = connection.prepareStatement(sql2);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CD_SUBJECT_PREREPORT1 Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }

        List<CdSubjectDto> subjectDtoList = new ArrayList<>();
        CdSubjectDto subDto = null;
        try {
            while (rs.next()) {
                subDto = new CdSubjectDto();
                subDto.setSubjectName(rs.getString("subjectname"));
                subDto.setSubkeyId(rs.getString("subjectid"));
                subDto.setTablename(rs.getString("tbname"));
                subDto.setZf(rs.getFloat("zf"));
                subjectDtoList.add(subDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        if (CollectionUtils.isEmpty(subjectDtoList)) {
            json.put("success", false);
            json.put("msg", "考试下没有维护科目");
            releaseResource();
            return json.toString();
        }
        Map<String, String> xdMap = findGradeCode();
        if (!xdMap.containsKey(dto.getGradeName())) {
            json.put("success", false);
            json.put("msg", "考试中对象：" + dto.getGradeName() + "未找到对应的代码");
            releaseResource();
            return json.toString();
        }
        json.put("success", true);
        String gradeSection = xdMap.get(dto.getGradeName()).substring(0, 1);
        //对应学段可以维护的班级
        List<Course> courseList = SUtils.dt(courseRemoteService.getListByCondition(unitId, null, null, null, gradeSection, 1, null), new TR<List<Course>>() {
        });
        //重名随机取一个
        Map<String, Course> courseMap = new HashMap<>();
        for (Course c : courseList) {
            courseMap.put(c.getSubjectName(), c);
        }
        json.put("gradeCode", xdMap.get(dto.getGradeName()));
        json.put("courseList", courseList);
        for (CdSubjectDto c : subjectDtoList) {
            if (courseMap.containsKey(c.getSubjectName())) {
                c.setRelaSubjectId(courseMap.get(c.getSubjectName()).getId());
            }
        }
        json.put("subjectDtoList", subjectDtoList);
        releaseResource();
        return json.toString();
    }

    @Override
    public String saveExamResultByUnitId(String ownerId, String unitId, CdSaveSysncyDto saveDto) {
        String acadyear = saveDto.getAcadyear();
        String semester = saveDto.getSemester();
        JSONObject json = new JSONObject();
        List<RhKeyUnit> keyList = SUtils.dt(rhKeyUnitRemoteService.findByUnitIdAnduKeyId(unitId, saveDto.getuKeyId()), new TR<List<RhKeyUnit>>() {
        });
        if (CollectionUtils.isEmpty(keyList)) {
            json.put("success", false);
            json.put("msg", "未找到对应ukey数据");
            return json.toString();
        }
        //需要同步的科目
        List<CdSubjectDto> syssubList = saveDto.getDtoList();
        Map<String, String> subIdByRhName = new HashMap<>();
        for (CdSubjectDto c : syssubList) {
            if (c == null) {
                continue;
            }
            subIdByRhName.put(c.getSubjectName(), c.getRelaSubjectId());
        }

        //如果多个默认取一个
        RhKeyUnit key = keyList.get(0);
        //创建数据库连接
        init("", key.getDbUrl(), key.getDbUsername(), key.getDbPassword());
        connection = getConnection();
        if (connection == null) {
            json.put("success", false);
            json.put("msg", "连接数据库失败");
            return json.toString();
        }
        //组装数据
        String sql = "select * from cd_exam where ownedby = '" + key.getUkey() + "' and examId='" + saveDto.getExamKey() + "'  order by sdate desc";
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("cd_exam Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        //考试不可能有多条
        CdExamDto dto = null;
        try {
            if (rs.next()) {
                dto = new CdExamDto();
                String examTableName = rs.getString("dbuser");
                String examName = rs.getString("examname");
                String examKey = rs.getString("examId");
                String ownedby = rs.getString("ownedby");

                dto.setExamKey(examKey);
                dto.setExamName(examName);
                dto.setExamTableName(examTableName);
                dto.setOwnedby(ownedby);
                dto.setStartTime(rs.getDate("sdate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        if (dto == null) {
            json.put("success", false);
            json.put("msg", "未找到考试数据");
            releaseResource();
            return json.toString();
        }
        //找到考试的科目
        String sql2 = "Select * from " + dto.getExamTableName() + ".CD_SUBJECT_PREREPORT1 order by subjectid";
        try {
            ps = connection.prepareStatement(sql2);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CD_SUBJECT_PREREPORT1 Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        List<CdSubjectDto> subjectDtoList = new ArrayList<>();
        CdSubjectDto subDto = null;
        List<String> subNames = new ArrayList<>();
        try {
            while (rs.next()) {
                subDto = new CdSubjectDto();
                subDto.setSubjectName(rs.getString("subjectname"));
                if (!subIdByRhName.containsKey(subDto.getSubjectName())) {
                    continue;
                }
                subDto.setRelaSubjectId(subIdByRhName.get(subDto.getSubjectName()));
                subDto.setSubkeyId(rs.getString("subjectid"));
                subDto.setTablename(rs.getString("tbname"));
                subDto.setZf(rs.getFloat("zf"));
                subjectDtoList.add(subDto);
                subNames.add(subDto.getSubjectName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        if (CollectionUtils.isEmpty(subjectDtoList)) {
            json.put("success", false);
            json.put("msg", "未找到需要同步的科目");
            releaseResource();
            return json.toString();
        }
        //科目考试题目
        String sql4 = "select * from (select subjectid,to_number(replace(a.fieldname, 'kgf', '')) as xh,a.fieldname,a.stdscore,substr(a.fieldname,0,1)as tmtype "
                + "from " + dto.getExamTableName() + ".cd_subject_prereport2 a where substr(a.fieldname,0,2)='kg' and a.fieldbelong='kg' "
                + "union select subjectid,to_number(replace(replace(b.fieldname, 'zg', ''),'_','')) xh,b.fieldname,b.stdscore,substr(b.fieldname,0,1)as tmtype "
                + "from " + dto.getExamTableName() + ".cd_subject_prereport2 b where substr(b.fieldname,0,2)='zg' and b.fieldbelong='zg' ) aa order by subjectid,tmtype,xh";
        try {
            ps = connection.prepareStatement(sql4);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CD_SUBJECT_PREREPORT2 Failed to make query!");
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }

        List<EmTitleInfo> emTitleInfoList = new ArrayList<>();
        // key: subjectiD + TITLEnAME
        Map<String, Map<String, EmTitleInfo>> titleInfoMap = new HashMap<>();
        EmTitleInfo emTitleInfo = null;
        try {
            while (rs.next()) {
                emTitleInfo = new EmTitleInfo();
                emTitleInfo.setId(UuidUtils.generateUuid());
                // 后面需要更新 subjectId 和 examId为 本系统的Id
                emTitleInfo.setSubjectId(rs.getString("subjectid"));
                emTitleInfo.setTitleCode(rs.getInt("xh"));
                emTitleInfo.setTitleName(StringUtils.upperCase(rs.getString("fieldname")));
                emTitleInfo.setTotalScore(rs.getInt("stdscore"));
                emTitleInfo.setTitleType(rs.getString("tmtype"));
                emTitleInfo.setExamId(dto.getExamKey());//考试信息
                emTitleInfo.setUnitId(unitId);
                emTitleInfo.setCreationTime(new Date());
                emTitleInfoList.add(emTitleInfo);
                Map<String, EmTitleInfo> subTileMap = titleInfoMap.get(emTitleInfo.getSubjectId());
                if (subTileMap == null) {
                    subTileMap = new HashMap<>();
                    titleInfoMap.put(emTitleInfo.getSubjectId(), subTileMap);
                }
                subTileMap.put(emTitleInfo.getTitleName(), emTitleInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("success", false);
            json.put("msg", "查询失败");
            releaseResource();
            return json.toString();
        }
        if (CollectionUtils.isEmpty(subjectDtoList)) {
            json.put("success", false);
            json.put("msg", "未找到需要同步的科目小题");
            releaseResource();
            return json.toString();
        }

        //学生成绩
//		Set<String> stuRhCode=new HashSet<>();
        List<CdStudentDto> cdStuList = null;
        CdStudentDto cdStuDto = null;
        Set<String> stuCodes = new HashSet<>();
        for (CdSubjectDto item : subjectDtoList) {
            cdStuList = new ArrayList<>();
            item.setCdStuList(cdStuList);
            String sql3 = "select * from " + dto.getExamTableName() + "." + item.getTablename();
            // TODO 考虑 挪到for循环外面
            try {
                ps = connection.prepareStatement(sql3);
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(dto.getExamTableName() + "." + item.getTablename() + " Failed to make query!");
                json.put("success", false);
                json.put("msg", "查询失败");
                releaseResource();
                return json.toString();
            }
            try {
                while (rs.next()) {
                    cdStuDto = new CdStudentDto();
                    cdStuDto.setRhcode(rs.getString("code"));
                    cdStuDto.setStudentName(rs.getString("name"));
                    cdStuDto.setZf(rs.getFloat("zf"));
                    cdStuList.add(cdStuDto);
                    //TODO 这俩结果不是一样的吗？
//					stuRhCode.add(cdStuDto.getRhcode());
                    stuCodes.add(rs.getString("code"));

                    //获取小题分数
                    Map<String, EmTitleInfo> subTitleMap = titleInfoMap.get(item.getSubkeyId());
                    if (subTitleMap != null && subTitleMap.size() > 0) {
                        Map<EmTitleInfo, Float> titleScoreMap = new HashMap<>();
                        cdStuDto.setTitleScoreMap(titleScoreMap);
                        for (String titleName : subTitleMap.keySet()) {
                            EmTitleInfo titleInfo = subTitleMap.get(titleName);

                            rs.getFloat(titleName);
                            titleScoreMap.put(titleInfo, rs.getFloat(titleName));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                json.put("success", false);
                json.put("msg", "查询失败");
                releaseResource();
                return json.toString();
            }
        }
        releaseResource();
        List<Student> stuList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, stuCodes.toArray(new String[]{})), new TR<List<Student>>() {
        });
        if (CollectionUtils.isEmpty(stuList)) {
            json.put("success", false);
            json.put("msg", "学生数据可能里没有同步或者已经删除或者离校等,只对在校学生同步操作");
            return json.toString();
        }
        //stucode+stuname
        Map<String, Student> stuMap = new HashMap<>();
        for (Student s : stuList) {
            if (StringUtils.isNotBlank(s.getStudentCode())) {
                stuMap.put(s.getStudentCode() + s.getStudentName(), s);
            }
        }

        Set<String> classIds = EntityUtils.getSet(stuList, e -> e.getClassId());
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        });
        Set<String> gradeIds = EntityUtils.getSet(clazzList, e -> e.getGradeId());
        Map<String, List<String>> clazzIdByGradeIdMap = EntityUtils.getListMap(clazzList, e -> e.getGradeId(), e -> e.getId());
        Map<String, String> gradeIdByClassId = EntityUtils.getMap(clazzList, e -> e.getId(), e -> e.getGradeId());
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findListByIds(gradeIds.toArray(new String[]{})), new TR<List<Grade>>() {
        });
        //
        Map<String, Grade> gradeByCode = makeGradeCode(gradeList, acadyear);
        List<EmExamInfo> oldList = emExamInfoService.findByUnitIdAndAcadyear(unitId, acadyear, semester);
        Map<String, EmExamInfo> examBygroupCode = new HashMap<>();
        Set<String> delIds = new HashSet<>();
        //前缀
        String examCodePre = acadyear.substring(0, 4);
        //考试编号最大值
        int index = 0;
        for (EmExamInfo info : oldList) {
            if (info.getExamCode().startsWith(examCodePre)) {
                int i = Integer.parseInt(info.getExamCode().substring(examCodePre.length(), info.getExamCode().length()));
                if (index > i) {
                    index = i;
                }

            }
            // 一场考试只能有一个年级
            if (saveDto.getExamKey().equals(info.getOriginExamId())) {
                examBygroupCode.put(info.getGradeCodes(), info);
                delIds.add(info.getId());
            }
        }
        //科目--暂不考虑重名以及学科的学段
//		Map<String,Course> courseMap=new HashMap<>();
//		List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectNameIn(unitId, subNames.toArray(new String[] {})),new TR<List<Course>>() {});
//		if(CollectionUtils.isEmpty(courseList)) {
//			json.put("success", false);
//			json.put("msg", "找不到对应的科目");
//			return json.toString();
//		}
//		for(Course c:courseList) {
//			courseMap.put(c.getSubjectName(), c);
//		}
        //考试信息list
        List<EmExamInfo> examInfoList = new ArrayList<>();
        //考试班级
        List<EmClassInfo> classInfolist = new ArrayList<>();
        //考试科目
        List<EmSubjectInfo> subjectInfoList = new ArrayList<>();
        //考试成绩
        List<EmScoreInfo> scoreInfoList = new ArrayList<>();
        //成绩锁定
        List<EmClassLock> lockList = new ArrayList<>();
        //考号
        List<EmExamNum> examNumList = new ArrayList<>();
        //考号
        List<EmTitleScore> titleScores = new ArrayList<>();
        //一般不会出现多个gradecode
        //从科目设置出发
        EmTitleScore titleScore = null;
        EmExamInfo emInfo = null;
        EmClassInfo classInfo = null;
        EmSubjectInfo subjectInfo = null;
        EmExamNum emExamNum = null;
        Set<String> emExamNumStudentIds = new HashSet<>();
        Map<String, EmExamInfo> emExamByGrade = new HashMap<>();
        DecimalFormat countFormat = new DecimalFormat("000000");
        for (Map.Entry<String, Grade> g : gradeByCode.entrySet()) {
            //只含有一个年级
            if (!saveDto.getGradeCode().equals(g.getKey())) {
                continue;
            }
            emInfo = examBygroupCode.get(g.getKey());
            if (emInfo == null) {
                emInfo = new EmExamInfo();
                emInfo.setId(UuidUtils.generateUuid());
            } else {
                //delIds.remove(emInfo.getId());//需要删除的旧数据
            }
            emInfo.setUnitId(unitId);
            emInfo.setIsDeleted(0);
            emInfo.setAcadyear(acadyear);
            emInfo.setSemester(semester);
            emInfo.setExamName(dto.getExamName());
            //考试编号 当前年度+6位流水号---学年（4位）+6位流水号
            String strValue = countFormat.format(index + 1);
            emInfo.setExamCode(examCodePre + strValue);
            index++;
            emInfo.setExamType("1");//正常考试
            emInfo.setExamUeType("0");//校内考试
            emInfo.setGradeCodes(g.getKey());
            emInfo.setOriginExamId(saveDto.getExamKey());
            emInfo.setIsgkExamType("0");
            emInfo.setExamStartDate(dto.getStartTime());
            emInfo.setExamEndDate(dto.getStartTime());//默认=开始时间
            emInfo.setCreationTime(new Date());
            emInfo.setModifyTime(new Date());
            emExamByGrade.put(g.getValue().getId(), emInfo);
            examInfoList.add(emInfo);
            //考试班级
            List<String> chooseClazz = clazzIdByGradeIdMap.get(g.getValue().getId());
            for (String s : chooseClazz) {
                // TODO 应该删除原来的 课时班级数据
                classInfo = new EmClassInfo();
                classInfo.setClassId(s);
                classInfo.setSchoolId(unitId);
                classInfo.setExamId(emInfo.getId());
                classInfo.setClassType("1");//行政班
                classInfo.setId(UuidUtils.generateUuid());
                classInfolist.add(classInfo);
            }
            //科目
            for (CdSubjectDto dd : subjectDtoList) {
//				String subName = dd.getSubjectName();
//				if(!courseMap.containsKey(subName)) {
//					continue;
//				}
//				Course course = courseMap.get(subName);
                subjectInfo = new EmSubjectInfo();
                subjectInfo.setId(UuidUtils.generateUuid());
                subjectInfo.setUnitId(unitId);
                subjectInfo.setExamId(emInfo.getId());
                subjectInfo.setStartDate(dto.getStartTime());
                subjectInfo.setEndDate(dto.getStartTime());
                subjectInfo.setSubjectId(dd.getRelaSubjectId());
                subjectInfo.setInputType("S");
                subjectInfo.setFullScore(dd.getZf());
                //FIXME 结束时间暂时不给 ？
                subjectInfo.setIsLock("0");//默认否 科目锁定是什么意思？
                subjectInfo.setGkSubType("0");//默认

                subjectInfoList.add(subjectInfo);
                for (String s : chooseClazz) {
                    EmClassLock classLock = new EmClassLock();
                    classLock.setId(UuidUtils.generateUuid());
                    classLock.setClassId(s);
                    classLock.setClassType("1");//杭外默认行政班
                    classLock.setCreationTime(new Date());
                    classLock.setExamId(emInfo.getId());
                    // 默认锁定
                    classLock.setLockState("1");
                    classLock.setSchoolId(unitId);
                    classLock.setSubjectId(subjectInfo.getSubjectId());
                    lockList.add(classLock);
                }

            }

        }
        //科目
        for (CdSubjectDto dd : subjectDtoList) {
//			String subName = dd.getSubjectName();
//			if(!courseMap.containsKey(subName)) {
//				continue;
//			}
//			Course course = courseMap.get(subName);
            if (CollectionUtils.isNotEmpty(dd.getCdStuList())) {
                for (CdStudentDto k : dd.getCdStuList()) {
                    //key 学号 + 姓名
                    String stukey = k.getRhcode() + k.getStudentName();
//					if(!stuIdByCode.containsKey(k.getRhcode())) {
//						continue;
//					}
//					if(!stuMap.containsKey(stuIdByCode.get(k.getRhcode()))) {
//						continue;
//					}
//					Student stu = stuMap.get(stuIdByCode.get(k.getRhcode()));
                    if (!stuMap.containsKey(stukey)) {
                        continue;
                    }
                    Student stu = stuMap.get(stukey);
                    EmScoreInfo scoreInfo = new EmScoreInfo();
                    scoreInfo.setId(UuidUtils.generateUuid());
                    scoreInfo.setAcadyear(acadyear);
                    scoreInfo.setSemester(semester);
                    // TODO 考试应该确定只有一场了,不需要Map
                    scoreInfo.setExamId(emExamByGrade.get(gradeIdByClassId.get(stu.getClassId())).getId());
                    scoreInfo.setSubjectId(dd.getRelaSubjectId());
                    scoreInfo.setGkSubType("0");
                    scoreInfo.setCreationTime(new Date());
                    scoreInfo.setModifyTime(new Date());
                    scoreInfo.setOperatorId(ownerId);
                    scoreInfo.setUnitId(unitId);
                    scoreInfo.setInputType("S");
                    scoreInfo.setClassId(stu.getClassId());
                    scoreInfo.setStudentId(stu.getId());
                    scoreInfo.setScoreStatus("0");//正常
                    scoreInfo.setScore(String.valueOf(k.getZf()));
                    scoreInfoList.add(scoreInfo);
                    //考号
                    if (!emExamNumStudentIds.contains(stu.getId())) {
                        emExamNum = new EmExamNum();
                        emExamNum.setExamId(scoreInfo.getExamId());
                        // 学号当考号
                        emExamNum.setExamNumber(k.getRhcode());
                        emExamNum.setId(UuidUtils.generateUuid());
                        emExamNum.setSchoolId(unitId);
                        emExamNum.setStudentId(stu.getId());
                        examNumList.add(emExamNum);
                        emExamNumStudentIds.add(stu.getId());
                    }

                    // 小题成绩
                    Map<EmTitleInfo, Float> titleScoreMap = k.getTitleScoreMap();
                    if (titleScoreMap != null && titleScoreMap.size() > 0) {
                        for (EmTitleInfo titleInfo : titleScoreMap.keySet()) {
                            Float ts = titleScoreMap.get(titleInfo);
                            if (ts == null) ts = 0f;
                            titleInfo.setExamId(scoreInfo.getExamId());
                            titleInfo.setSubjectId(scoreInfo.getSubjectId());

                            titleScore = new EmTitleScore();
                            titleScore.setId(UuidUtils.generateUuid());
                            titleScore.setCreationTime(new Date());
                            titleScore.setExamId(scoreInfo.getExamId());
                            titleScore.setStudentId(scoreInfo.getStudentId());
                            titleScore.setSubjectId(scoreInfo.getSubjectId());
                            titleScore.setTitleId(titleInfo.getId());
                            titleScore.setUnitId(unitId);
                            titleScore.setScore(ts);
                            titleScores.add(titleScore);
                        }
                    }
                }
            }
        }
        //通过来源考试id删除exammanage对应的考试以及科目成绩等--delIds 一般就一个考试
        // TODO  可能有多个考试？
        if (CollectionUtils.isNotEmpty(delIds)) {
            String[] examIds = delIds.toArray(new String[]{});
            //TODO 这一步没有必要吧
            if (CollectionUtils.isNotEmpty(examInfoList)) {
                Set<String> newIds = EntityUtils.getSet(examInfoList, e -> e.getId());
                delIds.removeAll(newIds);
            }
            if (CollectionUtils.isNotEmpty(delIds)) {
                emExamInfoService.deleteAllIsDeleted(delIds.toArray(new String[]{}));
            }

            emClassInfoService.deleteByExamIds(examIds);
            emSubjectInfoService.deleteByExamIdIn(examIds);
            emScoreInfoService.deleteByExamIdIn(examIds);
            emClassLockService.deleteByExamIdIn(examIds);

            for (String s : examIds) {
                emExamNumService.deleteBy(unitId, s);
                //删除统计结果
                EmStatObject obj = emStatObjectService.findByUnitIdExamId(unitId, s);
                if (obj != null) {
                    emStatService.deleteByStatObjectId(obj.getId());
                }
            }
            // 删除小题 和 小题分数
            emTitleInfoService.deleteByExamIds(examIds);
            emTitleScoreService.deleteByExamIds(examIds);
        }
        //删除后 保存新的
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            emExamInfoService.saveAll(examInfoList.toArray(new EmExamInfo[]{}));
        }
        if (CollectionUtils.isNotEmpty(classInfolist)) {
            emClassInfoService.saveAll(classInfolist.toArray(new EmClassInfo[]{}));
        }
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            emSubjectInfoService.saveAll(subjectInfoList.toArray(new EmSubjectInfo[]{}));
        }
        if (CollectionUtils.isNotEmpty(scoreInfoList)) {
            emScoreInfoService.saveAll(scoreInfoList.toArray(new EmScoreInfo[]{}));
        }
        if (CollectionUtils.isNotEmpty(lockList)) {
            emClassLockService.saveAll(lockList.toArray(new EmClassLock[]{}));
        }
        if (CollectionUtils.isNotEmpty(examNumList)) {
            emExamNumService.saveAll(examNumList.toArray(new EmExamNum[]{}));
        }
        // 考试小题目录  和 每个学生的考试分数
        if (CollectionUtils.isNotEmpty(emTitleInfoList)) {
            emTitleInfoService.saveAll(emTitleInfoList.toArray(new EmTitleInfo[]{}));
        }
        if (CollectionUtils.isNotEmpty(titleScores)) {
            emTitleScoreService.saveAll(titleScores.toArray(new EmTitleScore[]{}));
        }


        //根据科目名称获取科目---暂时不考虑可能存在年级问题---目前一个学校的科目名称是唯一的
        json.put("success", true);
        json.put("msg", "成功");

//		int ui = 1/0;
        return json.toString();
    }

    private Map<String, Grade> makeGradeCode(List<Grade> gradeList, String acadyear) {
        Map<String, Grade> returnMap = new HashMap<>();
        for (Grade g : gradeList) {
            int start = Integer.parseInt(g.getOpenAcadyear().split("-")[0]);
            int end = Integer.parseInt(acadyear.split("-")[0]);
            if (end < start) {
                //不课程存在
            }
            //（end-start+1） 查出学生入学几年；算出高几了
            String gradeCode = g.getGradeCode().substring(0, 1) + "" + (end - start + 1);
            returnMap.put(gradeCode, g);
        }


        return returnMap;
    }

    public void init(String dirverName, String url, String username, String password) {
        this.dirverName = dirverName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        if (StringUtils.isBlank(dirverName)) {
            dirverName = "oracle.jdbc.driver.OracleDriver";
        }
        try {
            Class.forName(dirverName);
        } catch (ClassNotFoundException e) {

            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
            return null;

        }
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@" + url, username, password);

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;
        }

        if (connection != null) {

            return connection;
        } else {
            System.out.println("Failed to make connection!");
        }
        return null;
    }


    public void releaseResource() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> findGradeCode() {
        return RedisUtils.getObject("XD_NAME_GRADECODE", RedisUtils.TIME_ONE_DAY, new TypeReference<Map<String, String>>() {
        }, new RedisInterface<Map<String, String>>() {
            @Override
            public Map<String, String> queryData() {
                Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}), new TR<Map<String, Map<String, McodeDetail>>>() {
                });
                Map<String, String> gradeCodeByName = new HashMap<>();
                String[] arr = new String[]{"0", "1", "2", "3", "9"};
                for (String s : arr) {
                    if (findMapMapByMcodeIds.get("DM-RKXD-" + s) != null) {
                        for (Entry<String, McodeDetail> item : findMapMapByMcodeIds.get("DM-RKXD-" + s).entrySet()) {
                            gradeCodeByName.put(item.getValue().getMcodeContent(), s + item.getKey());
                        }
                    }
                }
                return gradeCodeByName;
            }
        });

    }
}
