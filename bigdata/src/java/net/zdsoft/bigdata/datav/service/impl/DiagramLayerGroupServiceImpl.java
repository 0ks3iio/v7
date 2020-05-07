//package net.zdsoft.bigdata.datav.service.impl;
//
//import net.zdsoft.bigdata.datav.dao.DiagramLayerGroupRepository;
//import net.zdsoft.bigdata.datav.entity.DiagramLayerGroup;
//import net.zdsoft.bigdata.datav.service.DiagramLayerGroupService;
//import net.zdsoft.framework.utils.UuidUtils;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author shenke
// * @since 2019/5/20 下午6:57
// */
//@Service("diagramLayerGroupService")
//public class DiagramLayerGroupServiceImpl implements DiagramLayerGroupService {
//
//    @Resource
//    private DiagramLayerGroupRepository diagramLayerGroupRepository;
//
//    @Override
//    public void save(DiagramLayerGroup diagramLayerGroup) {
//        diagramLayerGroup.setId(UuidUtils.generateUuid());
//        diagramLayerGroupRepository.save(diagramLayerGroup);
//    }
//
//    @Override
//    public void delete(String id) {
//        diagramLayerGroupRepository.deleteById(id);
//    }
//
//    @Override
//    public List<DiagramLayerGroup> getLayerGroupsByScreenId(String screenId) {
//        return diagramLayerGroupRepository.getAllByScreenId(screenId);
//    }
//}
