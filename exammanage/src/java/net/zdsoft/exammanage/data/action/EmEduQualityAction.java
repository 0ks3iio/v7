package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.exammanage.data.dto.EmQualityScoreDto;
import net.zdsoft.exammanage.data.entity.ExammanageQualityScore;
import net.zdsoft.exammanage.data.service.ExammanageQualityScoreService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/exammanage/edu/quality")
public class EmEduQualityAction extends EmExamCommonAction {

    @Autowired
    private ExammanageQualityScoreService exammanageQualityScoreService;

    @RequestMapping("/page")
    @ControllerInfo(value = "综合素质录入页")
    public String enrollstuIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/edu/quality/qualityStudentIndex.ftl";
    }

    @RequestMapping("/list")
    @ControllerInfo(value = "报名学生列表")
    public String enrollstuList(String examId, String schoolId, ModelMap map, HttpServletRequest request) {
        String url = "/exammanage/edu/quality/qualityStudentList.ftl";
        if (StringUtils.isEmpty(examId) || StringUtils.isEmpty(schoolId)) {
            return url;
        }
        String field = request.getParameter("field");
        String keyWord = request.getParameter("keyWord");
//        Pagination page = createPagination();
        List<ExammanageQualityScore> qualityList = exammanageQualityScoreService.findByList(examId, schoolId, field, keyWord, null);
        map.put("qualityList", qualityList);
//        sendPagination(request, map, page);
        return url;
    }

    @ResponseBody
    @RequestMapping("/save")
    @ControllerInfo(value = "保存")
    public String qualityScore(EmQualityScoreDto dto, String acadyear, String semester, String examId) {
        try {
            if (CollectionUtils.isEmpty(dto.getDtos())) {
                return error("无数据");
            }
            exammanageQualityScoreService.batchSave(acadyear, semester, examId, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }
}
