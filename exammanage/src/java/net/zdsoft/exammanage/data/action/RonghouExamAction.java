package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.exammanage.data.dto.CdSaveSysncyDto;
import net.zdsoft.exammanage.data.service.RhSyncyService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/exammanage/sysncy")
public class RonghouExamAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private RhSyncyService rhSyncyService;

    @RequestMapping("/index/page")
    public String sysncyIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);

        return "/exammanage/ronghou/sysncyIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/findExamList")
    public String findExamList(String acadyear, String semester) {
        String unitId = getLoginInfo().getUnitId();
        return rhSyncyService.findExamList(unitId, acadyear, semester);
    }

    @ResponseBody
    @RequestMapping("/findExamSubjectList")
    public String findExamSubjectList(String acadyear, String semester, String examKey, String uKeyId) {
        String unitId = getLoginInfo().getUnitId();
        return rhSyncyService.findSubjectList(unitId, acadyear, semester, examKey, uKeyId);
    }

    /**
     * 同步考试内容
     *
     * @param saveDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveSysncy")
    public String saveSysncy(CdSaveSysncyDto saveDto) {
        LoginInfo lon = getLoginInfo();
        try {
            return rhSyncyService.saveExamResultByUnitId(lon.getOwnerId(), lon.getUnitId(), saveDto);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("success", false);
            json.put("msg", "操作失败");
            return json.toString();
        }
    }

    public boolean isShowRonghou() {
        return true;
    }

}
