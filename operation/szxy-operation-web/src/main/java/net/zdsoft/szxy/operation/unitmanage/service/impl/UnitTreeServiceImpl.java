package net.zdsoft.szxy.operation.unitmanage.service.impl;

import com.alibaba.fastjson.JSON;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.dto.TreeNode;
import net.zdsoft.szxy.operation.security.UserDataRegionHolder;
import net.zdsoft.szxy.operation.unitmanage.dao.OpUnitDao;
import net.zdsoft.szxy.operation.unitmanage.service.UnitTreeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("unitTreeService")
public class UnitTreeServiceImpl implements UnitTreeService {
    @Resource
    private OpUnitDao opUnitDao;

    @Record(type = RecordType.Service)
    @Override
    public List<TreeNode> findUnitByParentId(String id) {
        Set<String> regionCodes = UserDataRegionHolder.getRegions();

        return StringUtils.isBlank(id) ? opUnitDao.findAllTopUnits(regionCodes) : opUnitDao.findTreeNodeByParentId(id);

    }


    @Override
    public List<TreeNode> findUnitByUnitName(String unitName) {
        Set<String> regionCode = UserDataRegionHolder.getRegions();
        System.out.println(JSON.toJSONString(regionCode));
        List<String> regionCodes = regionCode.stream().map(x ->
            StringUtils.rightPad(x, 6, "0")
        ).collect(Collectors.toList());
        if (regionCodes.isEmpty()){
            return opUnitDao.findTreeNodeByName(unitName);
        }else {
            return opUnitDao.findTreeNodeByNameAndRegionCode(unitName,regionCodes);
        }
    }

}
