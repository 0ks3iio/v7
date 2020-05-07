package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmExamRegionDao;
import net.zdsoft.exammanage.data.entity.EmRegion;
import net.zdsoft.exammanage.data.service.EmExamRegionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service("emExamRegionService")
public class EmExamRegionServiceImpl extends BaseServiceImpl<EmRegion, String> implements EmExamRegionService {

    @Autowired
    private EmExamRegionDao emExamRegionDao;

    @Override
    public List<EmRegion> findByExamIdAndUnitId(String examId, String unitId) {
        List<EmRegion> emRegions = emExamRegionDao.findByExamIdAndUnitId(examId, unitId);
        if (CollectionUtils.isNotEmpty(emRegions)) {
            Collections.sort(emRegions, new Comparator<EmRegion>() {
                @Override
                public int compare(EmRegion o1, EmRegion o2) {
                    return Integer.valueOf(o1.getExamRegionCode()) - (Integer.valueOf(o2.getExamRegionCode()));
                }
            });
        }
        return emRegions;
    }

    @Override
    public List<EmRegion> findByExamIdAndLikeCode(String examId, String regionCode, String unitId) {
        List<EmRegion> emRegions = null;
        if (StringUtils.isNotBlank(regionCode)) {
            emRegions = emExamRegionDao.findByExamIdAndLikeCode(examId, regionCode, unitId);
        } else {
            emRegions = emExamRegionDao.findByExamIdAndUnitId(examId, unitId);
        }
        List<EmRegion> lastRegions = new ArrayList<EmRegion>();
        if (CollectionUtils.isNotEmpty(emRegions)) {
            for (EmRegion emRegion : emRegions) {
                if (emRegion.getExamOptionNum() != null && emRegion.getExamOptionNum() > 0) {
                    lastRegions.add(emRegion);
                }
            }
        }
        return lastRegions;
    }

    @Override
    public List<EmRegion> saveAllEntitys(EmRegion... emRegions) {
        return emExamRegionDao.saveAll(checkSave(emRegions));
    }

    @Override
    public List<EmRegion> findByExamIdAndRegionCode(
            String regionCode, String examId) {
        return emExamRegionDao.findByRegionCodeAndExamId(regionCode, examId);
    }

    @Override
    protected BaseJpaRepositoryDao<EmRegion, String> getJpaDao() {
        return emExamRegionDao;
    }

    @Override
    protected Class<EmRegion> getEntityClass() {
        return EmRegion.class;
    }

    @Override
    public List<EmRegion> findByExamId(String examId) {
        return emExamRegionDao.findByExamId(examId);
    }
}
