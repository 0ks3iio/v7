package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmStatRangeDao;
import net.zdsoft.exammanage.data.entity.EmStatObject;
import net.zdsoft.exammanage.data.entity.EmStatRange;
import net.zdsoft.exammanage.data.service.EmStatObjectService;
import net.zdsoft.exammanage.data.service.EmStatRangeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("emStatRangeService")
public class EmStatRangeServiceImpl extends BaseServiceImpl<EmStatRange, String> implements EmStatRangeService {

    @Autowired
    private EmStatRangeDao emStatRangeDao;
    @Autowired
    private EmStatObjectService emStatObjectService;

    @Override
    protected BaseJpaRepositoryDao<EmStatRange, String> getJpaDao() {
        return emStatRangeDao;
    }

    @Override
    protected Class<EmStatRange> getEntityClass() {
        return EmStatRange.class;
    }

    @Override
    public List<EmStatRange> saveAllEntitys(EmStatRange... emStatRange) {
        return emStatRangeDao.saveAll(checkSave(emStatRange));
    }

    @Override
    public List<String> getClassIdsBy(String unitId, String examId) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return new ArrayList<>();
        }//14444154304731318202465169064245,14443378600771231155273665828453
        List<String> statClassIds = emStatRangeDao.findClsIdByObjIdAndExamId(emStatObject.getId(), examId);
        return statClassIds;
    }

    @Override
    public List<String> findRangeIdBySubjectId(String subjectId, String rangeType, String unitId, Set<String> examIds) {
        List<EmStatObject> objects = emStatObjectService.findByUnitIdAndExamIdIn(unitId, examIds.toArray(new String[0]));
        if (CollectionUtils.isEmpty(objects)) {
            return new ArrayList<>();
        }
        Set<String> objectIds = EntityUtils.getSet(objects, EmStatObject::getId);
        List<String> statRangeIds = null;
        statRangeIds = emStatRangeDao.findRangeIdBySubjectId(subjectId, rangeType, objectIds.toArray(new String[0]));
        return statRangeIds;
    }

    @Override
    public List<String> getClassIdsBy(String unitId, String examId, String subjectId, String subType) {
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject == null) {
            return new ArrayList<>();
        }
        List<String> statClassIds = null;
        if (StringUtils.isBlank(subType)) {
            statClassIds = emStatRangeDao.findClsIdByObjIdAndExamId(emStatObject.getId(), examId, subjectId);
        } else {
            statClassIds = emStatRangeDao.findClsIdByObjIdAndExamId(emStatObject.getId(), examId, subjectId, subType);
        }
        return statClassIds;
    }

    @Override
    public void deleteByStatObjectId(String... statObjectId) {
        if (statObjectId != null && statObjectId.length > 0) {
            emStatRangeDao.deleteByStatObjectIdIn(statObjectId);
        }
    }


    @Override
    public List<EmStatRange> findByObjIdAndRangeId(String objectId,
                                                   String examId, String rangeId, String rangeType) {

        return emStatRangeDao.findByObjIdAndRangeId(objectId, examId, rangeId, rangeType);
    }


    @Override
    public EmStatRange findByExamIdAndSubIdAndRangIdAndType(String objectId, String examId,
                                                            String subjectId, String rangeId, String rangeType) {
        return emStatRangeDao.findByExamIdAndSubjectIdAndRangeIdAndRangeType(objectId, examId, subjectId, rangeId, rangeType);
    }

    @Override
    public List<EmStatRange> findByExamIdAndSubIdAndRangIdAndTypeList(String objectId, String examId, String subjectId, String rangeId, String rangeType) {
        List<EmStatRange> emStatRanges = emStatRangeDao.findByExamIdAndSubIdAndRangIdAndTypeList(objectId, examId, subjectId, rangeId, rangeType);
        for (EmStatRange emStatRange : emStatRanges) {
            if (emStatRange.getAvgScoreT() != null) {
                BigDecimal b1 = new BigDecimal(emStatRange.getAvgScoreT());
                emStatRange.setAvgScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        return emStatRanges;
    }

    @Override
    public List<EmStatRange> findByExamIdAndSubIds(String objectId, String examId, String[] subjectIds, String rangeId) {
        List<EmStatRange> emStatRanges = null;
        if (StringUtils.isBlank(rangeId)) {
            emStatRanges = emStatRangeDao.findByExamIdAndSubIds(objectId, examId, subjectIds);
        } else
            emStatRanges = emStatRangeDao.findByExamIdAndSubIds(objectId, examId, subjectIds, rangeId);
        for (EmStatRange emStatRange : emStatRanges) {
            if (emStatRange.getAvgScoreT() != null) {
                BigDecimal b1 = new BigDecimal(emStatRange.getAvgScoreT());
                emStatRange.setAvgScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        return emStatRanges;
    }

    @Override
    public EmStatRange findByExamIdAndSubIdAndRangIdAndType1(String objectId, String examId,
                                                             String subjectId, String rangeId, String rangeType) {
        return emStatRangeDao.findByExamIdAndSubjectIdAndRangeIdAndRangeType1(objectId, examId, subjectId, rangeId, rangeType);
    }

    @Override
    public EmStatRange findByExamIdAndSubIdAndRangIdAndTypeSubType(
            String objectId, String examId, String subjectId, String rangeId,
            String rangeType, String subType) {
        return emStatRangeDao.findByExamIdAndSubIdAndRangIdAndTypeSubType(objectId, examId, subjectId, rangeId, rangeType, subType);
    }

    public List<EmStatRange> findBySubIdAndRangIdAndTypeAndExamIdIn(String[] objectIds, String examId, String subjectId, String rangeId, String rangeType) {
        return null;
    }

    @Override
    public List<EmStatRange> findByRangeIdAndSubjectId(String rangeId, String rangeType,
                                                       String subjectId, String[] examIds) {
        if (examIds == null || examIds.length <= 0) {
            return new ArrayList<EmStatRange>();
        }
        return emStatRangeDao.findByRangeIdAndSubjectId(rangeId, rangeType, subjectId, examIds);
    }

    @Override
    public List<EmStatRange> findByRangeIdAndSubjectIds(String schoolId, String rangeType, String subjectId, String[] examIds) {
        return null;
    }

    @Override
    public List<EmStatRange> findByExamIdAndRangeIdIn(String statObjectId,
                                                      String examId, String subjectId, String rangeType, String[] rangeId) {

        return emStatRangeDao.findByExamIdAndRangeIdIn(statObjectId, examId, subjectId, rangeType, rangeId);
    }

    @Override
    public List<EmStatRange> findByExamIdAndSubIdAndType(String objectId, String examId, String subjectId, String rangeType, String subType) {
        List<EmStatRange> rangeList = emStatRangeDao.findByExamIdAndSubjectIdAndRangeType(objectId, examId, subjectId, rangeType, subType);
        if (CollectionUtils.isNotEmpty(rangeList)) {
            for (EmStatRange emStatRange : rangeList) {
                BigDecimal b1 = new BigDecimal(emStatRange.getAvgScoreT());
                emStatRange.setAvgScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        return rangeList;
    }

    @Override
    public List<EmStatRange> findBySubIdAndTypeAndObjectIds(String subjectId, String rangeId, String rangeType, String subType, String[] objectIds) {
        return emStatRangeDao.findBySubIdAndTypeAndObjectIds(subjectId, rangeId, rangeType, subType, objectIds);
    }

    @Override
    public List<EmStatRange> findByExamId(String statObjectId, String examId, String rangeType, String[] rangeId) {
        return emStatRangeDao.findByExamId(statObjectId, examId, rangeType, rangeId);
    }

}
