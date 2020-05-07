package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.exammanage.data.dto.EmArrangePlaceSettingDto;
import net.zdsoft.exammanage.data.dto.EmOptionSchoolDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/exammanage/edu/examArrange")
public class EmEduOptionStuAction extends BaseAction {
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmOptionSchoolService emOptionSchoolService;
    @Autowired
    private EmArrangeService emArrangeService;
    @Autowired
    private EmPlaceSettingService emPlaceSettingService;
    @Autowired
    private EmOptionService emOptionService;


    @RequestMapping("/optionStuNum/page")
    @ControllerInfo("参考人数设置")
    public String showOpthionStuIndex(String examId, ModelMap map, HttpSession httpSession, HttpServletRequest request) {
        LoginInfo info = getLoginInfo(httpSession);
        String unitId = info.getUnitId();
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
//		Pagination page = createPagination();
        List<EmOptionSchoolDto> dtos = emOptionSchoolService.findByDtos(unitId, examId, null);
//		map.put("Pagination", page);
//        sendPagination(request, map, page);
        map.put("dtos", dtos);
        return "/exammanage/edu/examArrange/optionStuIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/optionStuNum/save")
    @ControllerInfo(value = "保存参考人数分配")
    public String doSaveOptionStuNum(EmOptionSchoolDto dto) {
        try {
            emOptionSchoolService.updateOptionStuNum(dto.getEnlist());
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @RequestMapping("/seatSetting/page")
    @ControllerInfo("座位编排")
    public String showSeatSettingIndex(String examId, ModelMap map, HttpSession httpSession) {
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            return errorFtl(map, "考试不存在");
        }
        map.put("examId", examId);
        EmArrangePlaceSettingDto dto = emPlaceSettingService.findDtoByExamId(examId);
        map.put("dto", dto);
        return "/exammanage/edu/examArrange/seatSettingIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/seatSetting/save")
    @ControllerInfo(value = "保存编排设置")
    public String doSaveSetting(String examId, String type, String[] seatNums) {
        try {
            if (StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(type) && seatNums != null && seatNums.length > 0) {
                emArrangeService.saveArrange(examId, type, seatNums);
            } else {
                return returnError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/arrangeResult")
    @ControllerInfo(value = "自动编排")
    public String arrangeResult(String examId, ModelMap map) {
        if (StringUtils.isBlank(examId)) {
            return error("项目不存在!");
        }
        try {
            ArrangeThread arrangeThread = new ArrangeThread(examId, emArrangeService);
            arrangeThread.start();
        } catch (Exception e) {
            return error("自动编排失败!");
        }
        return success("自动编排中，请稍后进行查看！");
    }

    public class ArrangeThread extends Thread {
        private String examId;
        private EmArrangeService emArrangeService;

        public ArrangeThread(String examId, EmArrangeService emArrangeService) {
            this.examId = examId;
            this.emArrangeService = emArrangeService;
        }

        public void run() {
            RedisUtils.set("arrangeExam_" + examId, "2");
            try {
                emArrangeService.arrangeResult(examId);
                RedisUtils.del("arrangeExam_" + examId);
            } catch (Exception e) {
                RedisUtils.del("arrangeExam_" + examId);
                e.printStackTrace();
            }
        }
    }
}
