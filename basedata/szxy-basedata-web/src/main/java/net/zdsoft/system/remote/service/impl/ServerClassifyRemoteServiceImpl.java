package net.zdsoft.system.remote.service.impl;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.system.dao.ServerClassifyDao;
import net.zdsoft.system.dao.ServerClassifyRelationDao;
import net.zdsoft.system.entity.server.ServerClassify;
import net.zdsoft.system.entity.server.ServerClassifyRelation;
import net.zdsoft.system.remote.dto.UnitServerClassify;
import net.zdsoft.system.remote.service.ServerClassifyRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/3/13 上午11:12
 */
@Service
public class ServerClassifyRemoteServiceImpl implements ServerClassifyRemoteService {

    @Resource
    private ServerClassifyDao serverClassifyDao;
    @Resource
    private ServerClassifyRelationDao serverClassifyRelationDao;
    @Autowired
    private UnitService unitService;

    @Override
    public List<UnitServerClassify> getClassifyByUnitId(String unitId) {
        //1、尝试获取当前单位的分类标准
        List<UnitServerClassify> unitServerClassifies = getSpecialClassify(unitId);
        if (!unitServerClassifies.isEmpty()) {
            return unitServerClassifies;
        }
        //2、尝试获取当前单位顶级单位的分类标准
        Unit topUnit = unitService.findTopUnit(unitId);
        if (topUnit != null) {
            unitServerClassifies = getSpecialClassify(topUnit.getId());
            if (!unitServerClassifies.isEmpty()) {
                return unitServerClassifies;
            }
        }
        //3、获取系统默认的分类标准
        return getSpecialClassify(ServerClassify.SYSTEM_DEFAULT_CLASSIFY_UNIT_ID);
    }

    private List<UnitServerClassify> getSpecialClassify(String unitId) {
        List<ServerClassify> classifies = serverClassifyDao.getClassifyByUnitId(unitId);
        if (!classifies.isEmpty()) {
            String[] classifyIds = classifies.stream().map(ServerClassify::getId).toArray(String[]::new);
            List<ServerClassifyRelation> relations =
                    serverClassifyRelationDao.getServerClassifyRelationsByClassifyIdIn(classifyIds);
            Map<String, Set<String>> codeMap = new HashMap<>(classifies.size());
            for (ServerClassifyRelation relation : relations) {
                Set<String> codes = codeMap.computeIfAbsent(relation.getClassifyId(), key -> new HashSet<>());
                codes.add(relation.getServerCode());
            }
            List<UnitServerClassify> unitServerClassifies = new ArrayList<>(classifies.size());
            for (ServerClassify classify : classifies) {
                UnitServerClassify unitServerClassify = new UnitServerClassify();
                unitServerClassify.setName(classify.getName());
                unitServerClassify.setOrderNumber(classify.getOrderNumber());
                unitServerClassify.setServerCodes(codeMap.getOrDefault(classify.getId(), Collections.emptySet()));
                unitServerClassify.setId(classify.getId());
                unitServerClassify.setIconPath(classify.getIconPath());
                unitServerClassifies.add(unitServerClassify);
            }
            return unitServerClassifies;
        }
        return Collections.emptyList();
    }
}
