package net.zdsoft.studevelop.data.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.entity.StudevelopPermission;
import net.zdsoft.studevelop.data.service.StudevelopPermissionService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by luf on 2018/10/16.
 */
public class CommonAuthAction extends BaseAction {
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudevelopPermissionService studevelopPermissionService;
    
    public boolean isAdmin(String permissionType){
            String userId = getLoginInfo().getUserId();
        String unitId = getLoginInfo().getUnitId();
        List<StudevelopPermission>  permissionList =  studevelopPermissionService.getStuDevelopPermissionList(unitId,permissionType);
        if(CollectionUtils.isNotEmpty(permissionList)){
            StudevelopPermission permission = permissionList.get(0);
            String userIds = permission.getUserIds();
            if(StringUtils.isNotEmpty(userIds)){
                String[] arr = userIds.split(",");
                List<String> list = Arrays.asList(arr);
                if(list.contains(userId)){
                    return true;
                }
            }
        }

        return false;
    }

    public List<Clazz> headTeacherClass(String acadyear){
        String ownerId = getLoginInfo().getOwnerId();
        String unitId = getLoginInfo().getUnitId();
        List<Clazz> classList = SUtils.dt(classRemoteService.findByIdCurAcadyear(unitId, acadyear), new TR<List<Clazz>>(){});
        Set<String> teacherIdSet  = new HashSet<>();
        Map<String,List<Clazz>> teaIdMap = new HashMap<>();
        for (Clazz cla : classList) {
            List<Clazz> list = teaIdMap.get(cla.getTeacherId());
            if(list == null){
                list = new ArrayList<>();
                teaIdMap.put(cla.getTeacherId(),list);
            }
            list.add(cla);
            List<Clazz> list2 = teaIdMap.get(cla.getViceTeacherId());
            if(list2 == null){
                list2 = new ArrayList<>();
                teaIdMap.put(cla.getViceTeacherId(),list2);
            }
            list2.add(cla);
        }

       return teaIdMap.get(ownerId);
    }
}
