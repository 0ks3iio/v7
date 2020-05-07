package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.exammanage.data.dto.EmAimsStudentDto;
import net.zdsoft.exammanage.data.entity.EmAimsInfo;
import net.zdsoft.exammanage.data.entity.EmAimsStudent;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.exammanage.data.service.EmAimsInfoService;
import net.zdsoft.exammanage.data.service.EmAimsStudentService;
import net.zdsoft.exammanage.data.service.EmJoinexamschInfoService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduAimsAction extends EmExamCommonAction {

    @Autowired
    private EmAimsInfoService emAimsInfoService;
    @Autowired
    private EmAimsStudentService emAimsStudentService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;

    @RequestMapping("/stuAims/page")
    @ControllerInfo(value = "学生报名List")
    public String stuAimsSave(ModelMap map) {
        LoginInfo loginInfo = this.getLoginInfo();
        String stuId = loginInfo.getOwnerId();
        String schoolId = loginInfo.getUnitId();
        List<EmAimsStudentDto> dtolist = emAimsStudentService.findListByStuId(schoolId, stuId);
        map.put("dtolist", dtolist);
        return "/exammanage/edu/aims/stuAimsIndex.ftl";
    }

    @RequestMapping("/stuAims/edit")
    @ControllerInfo(value = "学生报名-编辑")
    public String stuAimsEdit(String aimsId, ModelMap map) {
        LoginInfo loginInfo = this.getLoginInfo();
        String stuId = loginInfo.getOwnerId();
        EmAimsStudentDto dto = emAimsStudentService.findByAimsIdAndStuId(aimsId, stuId);
        map.put("dto", dto);
        EmAimsInfo info = emAimsInfoService.findOne(aimsId);
        String[] schIds = info.getSchoolIds().split(",");
        List<School> schs = SUtils.dt(schoolRemoteService.findListByIds(schIds), new TR<List<School>>() {
        });
        map.put("schools", schs);
        return "/exammanage/edu/aims/stuAimsEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/stuAims/save")
    @ControllerInfo(value = "学生报名-保存")
    public String saveStuAims(EmAimsStudent aimsStu) {
        try {
            if (StringUtils.isBlank(aimsStu.getAimsSchoolId())
                    || StringUtils.isBlank(aimsStu.getAimsId())) {
                return error("无数据");
            }
            LoginInfo loginInfo = this.getLoginInfo();
            String stuId = loginInfo.getOwnerId();
            aimsStu.setSchoolId(loginInfo.getUnitId());
            aimsStu.setStudentId(stuId);
            EmAimsInfo aimsInfo = emAimsInfoService.findOneWithMaster(aimsStu.getAimsId());
            List<EmPlaceStudent> emStuList = emPlaceStudentService.findByExamIdStuIds(aimsInfo.getExamId(), new String[]{stuId});
            Map<String, String> examNumMap = EntityUtils.getMap(emStuList, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);
            if(MapUtils.isNotEmpty(examNumMap)&&examNumMap.containsKey(aimsStu.getStudentId())){
            	aimsStu.setExamCode(examNumMap.get(aimsStu.getStudentId()));
            }
            emAimsStudentService.saveAimsStu(aimsStu);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    @RequestMapping("/aims/result/page")
    @ControllerInfo(value = "报名结果index")
    public String resultIndex(String examId, ModelMap map) {
        List<EmJoinexamschInfo> joinSchs = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schIds = EntityUtils.getSet(joinSchs, EmJoinexamschInfo::getSchoolId);
        List<School> schs = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
        });
        map.put("schools", schs);
        map.put("examId", examId);
        return "/exammanage/edu/aims/resultIndex.ftl";
    }

    @RequestMapping("/aims/result/list")
    @ControllerInfo(value = "报名结果list")
    public String examList(String examId, String schoolId, String field, String keyWord, ModelMap map, HttpServletRequest request) {
        List<EmAimsStudent> results = new ArrayList<>();
        EmAimsInfo aimsInfo = emAimsInfoService.findByExamIds(examId);
        Pagination page = createPagination();
        results = emAimsStudentService.findListBySchoolId(examId,aimsInfo.getId(), schoolId, field, keyWord, page);
        map.put("list", results);
        sendPagination(request, map, page);
        return "/exammanage/edu/aims/resultList.ftl";
    }


    @RequestMapping("/aims/page")
    @ControllerInfo(value = "志愿报名index")
    public String enrollstuIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/edu/aims/aimsIndex.ftl";
    }

    @RequestMapping("/aims/list")
    @ControllerInfo(value = "局端考试列表")
    public String examList(String acadyear, String semester, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<EmAimsInfo> infoList = emAimsInfoService.findByExams(unitId, acadyear, semester);
        map.put("list", infoList);
        return "/exammanage/edu/aims/aimsList.ftl";
    }

    @RequestMapping("/aims/edit/page")
    @ControllerInfo(value = "编辑")
    public String aimsPage(String examId, ModelMap map) {
        LoginInfo info = getLoginInfo();
        String unitId = info.getUnitId();
        EmAimsInfo aims = emAimsInfoService.findByExamIds(examId);
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        List<Unit> findAll = SUtils.dt(unitRemoteService.findByUnionCode(unit.getUnionCode(), Unit.UNIT_MARK_NORAML, Unit.UNIT_CLASS_SCHOOL), new TR<List<Unit>>() {
        });
        map.put("aims", aims);
        Map<String, String> tbMap = new HashMap<>();
        if (StringUtils.isNotBlank(aims.getSchoolIds())) {
            String[] schIds = aims.getSchoolIds().split(",");
            for (String schId : schIds) {
                tbMap.put(schId, schId);
            }
        }
        map.put("unitList", findAll);
        map.put("tbMap", tbMap);
        return "/exammanage/edu/aims/aimsEdit.ftl";
    }

    @ResponseBody
    @RequestMapping("/aims/save")
    @ControllerInfo(value = "保存")
    public String doSaveAims(EmAimsInfo aims) {
        try {
            if (StringUtils.isBlank(aims.getExamId())) {
                return error("无数据");
            }
            emAimsInfoService.saveAims(aims);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }


}
