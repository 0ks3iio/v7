package net.zdsoft.szxy.operation.unitmanage.controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.dto.TreeNode;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.unitmanage.service.UnitTreeService;
import net.zdsoft.szxy.plugin.mvc.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/operation/{dataType}/unitTree")
public class UnitTreeController {

    @Resource
    private UnitTreeService unitTreeService;

    @Record(type = RecordType.URL)
    @GetMapping("findUnitByParentId")
    public String findUnitByParentId(String id) {
        List<TreeNode> list = unitTreeService.findUnitByParentId(id);
        return JSON.toJSONString(list);
    }

    @Record(type = RecordType.URL)
    @GetMapping("findUnitByUnitName")
    public Response findUnitByUnitName(String unitName) {
        List<TreeNode> unitList = unitTreeService.findUnitByUnitName(unitName);
        if (StringUtils.isBlank(unitName)) {
            return Response.error().message("搜索名称不能为空!").build();
        } else if (unitList == null || unitList.size() == 0) {
            return Response.error().message("查不到相关单位!").build();
        } else {
            return Response.ok().data("unitList", unitList).build();
        }
    }


}
