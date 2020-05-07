package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.diathesis.data.dto.DiathesisTreeDto;
import net.zdsoft.diathesis.data.service.DiathesisCustomAuthorService;
import net.zdsoft.diathesis.data.service.DiathesisUnitService;
import net.zdsoft.framework.utils.SUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/7/29 9:23
 */
@Service
public class DiathesisUnitServiceImpl  implements DiathesisUnitService {

    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;

    @Override
    public List<Unit> findAllChildUnit(String unitId) {
        Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
        if(unit==null) return new ArrayList<>();
        return diathesisCustomAuthorService.findAllChildUnit(unitId);
    }

    @Override
    public List<DiathesisTreeDto> turnToTree(List<Unit> unitList, String topUnitId) {
        List<DiathesisTreeDto> list = unitList.stream().map(x -> {
            DiathesisTreeDto dto = new DiathesisTreeDto();
            dto.setId(x.getId());
            dto.setpId(x.getParentId());
            return dto;
        }).collect(Collectors.toList());
        Map<String, List<DiathesisTreeDto>> pidMap = list.stream().collect(Collectors.groupingBy(x -> x.getpId()));
        List<DiathesisTreeDto> root=new ArrayList<>();
        for (DiathesisTreeDto node : list) {
            if(node.getpId().equals(topUnitId)){
                root.add(node);
            }
            node.setChildList(pidMap.get(node.getId()));
        }
        return root;
    }

    @Override
    public void  getIds(List<DiathesisTreeDto> treeList,List<String> unitList) {
        if(treeList==null || treeList.size()==0)return ;
        if(unitList==null)unitList=new ArrayList<>();
        for (DiathesisTreeDto treeDto : treeList) {
            unitList.add(treeDto.getId());
            getIds(treeDto.getChildList(),unitList);
        }
    }
}
