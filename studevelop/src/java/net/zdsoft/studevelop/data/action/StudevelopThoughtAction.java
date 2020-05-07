package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luf on 2018/12/18.
 */

@Controller
@RequestMapping("/studevelop/thought")
public class StudevelopThoughtAction extends BaseAction {
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @RequestMapping("/index/page")
    public String index(String code ,ModelMap map){
        //stuDevelop/healtyHeart/head/index/page
        map.put("code",code);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }
        //获取学段
        String unitId = getLoginInfo().getUnitId();
        String sections = schoolRemoteService.findSectionsById(unitId);
        String[] sectionArray = sections.split(",");
        Map<String, String> sectionMap = new HashMap<String, String>();
        if(sectionArray.length==0){
            map.put("section", sections);
        }else{
            for(String str : sectionArray){
                if("1".equals(str)){
                    sectionMap.put("1", "小学");
                }else if("2".equals(str)){
                    sectionMap.put("2", "初中");
                }else if("3".equals(str)){
                    sectionMap.put("3", "高中");
                }
            }
            map.put("sectionMap", sectionMap);
        }
        map.put("schoolId",getLoginInfo().getUnitId());
        List<McodeDetail> projectTyps = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("projectTyps", projectTyps);

        return "/studevelop/thought/thoughtSetHead.ftl";
    }


}
