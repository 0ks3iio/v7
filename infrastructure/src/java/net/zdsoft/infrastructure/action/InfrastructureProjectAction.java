package net.zdsoft.infrastructure.action;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.infrastructure.entity.InfrastructureProject;
import net.zdsoft.infrastructure.service.InfrastructureProjectService;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;
import net.zdsoft.studevelop.studevelop.remote.service.StudevelopRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Created by luf on 2018/11/15.
 * /infrastructure/project/index/page
 */
@Controller
@RequestMapping("/infrastructure/project")
public class InfrastructureProjectAction extends BaseAction {
    @Autowired
    private InfrastructureProjectService infrastructureProjectService;

    @Autowired
    private StudevelopRemoteService studevelopRemoteService;
    @RequestMapping("/index/page")
    public String  main( ModelMap map){

        boolean isAdmin = isPerssion();
        map.put("isAdmin" ,isAdmin);
        return "/infrastructure/infrastructureHead.ftl";
    }
    private boolean isPerssion(){
        String unitId= getLoginInfo().getUnitId();
        List<StudevelopPermission> permissionList = SUtils.dt(studevelopRemoteService.getPermissiontListByUnitId("3" , unitId) , StudevelopPermission.class) ;
        String userId = getLoginInfo().getUserId();
        boolean isAdmin = false;
        if(CollectionUtils.isNotEmpty(permissionList)){
            StudevelopPermission permission = permissionList.get(0);
            String userIds = permission.getUserIds();
            if(StringUtils.isNotEmpty(userIds)){
                String[] arr = userIds.split(",");
                if(Arrays.asList(arr).contains(userId)){
                    isAdmin = true;
                }
            }
        }
        return isAdmin;

    }
    @RequestMapping("/edit")
    public String edit(String id , boolean isDetail , ModelMap map){
        InfrastructureProject infrastructureProject = new InfrastructureProject();
        if(StringUtils.isNotEmpty(id)){
            infrastructureProject = infrastructureProjectService.findOne(id);
        }
        map.put("infrastructureProject" ,infrastructureProject);
        if(isDetail){
            return "/infrastructure/infrastructureDetail.ftl";
        }else{
            return "/infrastructure/infrastructureEdit.ftl";
        }

    }
    @ResponseBody
    @ControllerInfo("保存")
    @RequestMapping("/save")
    public String save(InfrastructureProject infrastructureProject){
        try{
            String unitId = getLoginInfo().getUnitId();
            if(StringUtils.isEmpty(infrastructureProject.getId())){
                infrastructureProject.setId(UuidUtils.generateUuid());
                infrastructureProject.setCreationTime(new Date());
                infrastructureProject.setUnitId(unitId);
            }else{
                infrastructureProject.setModifyTime(new Date());
            }
            infrastructureProjectService.save(infrastructureProject);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();

    }

    @ResponseBody
    @ControllerInfo("删除")
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids[]") String[] ids){
        try{

            List<InfrastructureProject> list = infrastructureProjectService.findListByIds(ids);

            infrastructureProjectService.deleteAll(list.toArray(new InfrastructureProject[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }
    @RequestMapping("/list")
    public String list(String schoolName , HttpServletRequest request , ModelMap map){
        Pagination page = createPagination(request);
        String unitId = getLoginInfo().getUnitId();
        List<InfrastructureProject> infrastructureProjectList = null;
        if(StringUtils.isEmpty(schoolName)){
            infrastructureProjectList = infrastructureProjectService.getInfrastructureListByUnitId( unitId ,page);
        }else{
            infrastructureProjectList = infrastructureProjectService.getInfrastructureListBySchName(schoolName , unitId ,page);
        }
        boolean isAdmin = isPerssion();
        map.put("isAdmin" ,isAdmin);
        sendPagination(request, map, page);
        map.put("Pagination", page);
        map.put("infrastructureProjectList" ,infrastructureProjectList);

        return "/infrastructure/infrastructureList.ftl";
    }
}
