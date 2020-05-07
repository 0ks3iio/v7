package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmLimitDao;
import net.zdsoft.exammanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmLimitDetail;
import net.zdsoft.exammanage.data.service.EmLimitDetailService;
import net.zdsoft.exammanage.data.service.EmLimitService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service("emLimitService")
public class EmLimitServiceImpl extends BaseServiceImpl<EmLimit, String> implements
        EmLimitService {
    @Autowired
    private EmLimitDao emLimitDao;
    @Autowired
    private EmLimitDetailService emLimitDetailService;

    @Override
    public List<EmLimit> findBySearchDto(ScoreLimitSearchDto searchDto) {
        Specification<EmLimit> specification = new Specification<EmLimit>() {
            @Override
            public Predicate toPredicate(Root<EmLimit> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("unitId").as(String.class), searchDto.getUnitId()));
//				ps.add(cb.equal(root.get("acadyear").as(String.class), searchDto.getAcadyear()));
//				ps.add(cb.equal(root.get("semester").as(String.class), searchDto.getSemester()));
                if (StringUtils.isNotBlank(searchDto.getExamId())) {
                    ps.add(cb.equal(root.get("examId").as(String.class), searchDto.getExamId()));
                }
                if (StringUtils.isNotBlank(searchDto.getSubjectId())) {
                    ps.add(cb.equal(root.get("subjectId").as(String.class), searchDto.getSubjectId()));
                } else {
                    if (ArrayUtils.isNotEmpty(searchDto.getSubjectIds())) {
                        ps.add(root.get("subjectId").in(searchDto.getSubjectIds()));
                    }
                }
                if (StringUtils.isNotBlank(searchDto.getTeacherId())) {
                    ps.add(cb.equal(root.get("teacherId").as(String.class), searchDto.getTeacherId()));
                }
//				if(ArrayUtils.isNotEmpty(searchDto.getClassIds())){
//					ps.add(root.get("classId").in(searchDto.getClassIds()));
//				}
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };
        List<EmLimit> scoreLimitList = findAll(specification);
        makeClass(scoreLimitList);
        return scoreLimitList;
    }


    @Override
    protected BaseJpaRepositoryDao<EmLimit, String> getJpaDao() {
        return emLimitDao;
    }

    @Override
    protected Class<EmLimit> getEntityClass() {
        return EmLimit.class;
    }

    @Override
    public List<EmLimit> findLimitList(String examId, String unitId,
                                       String subjectId) {
        List<EmLimit> list = emLimitDao.findList(examId, unitId, subjectId);
        makeClass(list);
        return list;
    }

    private void makeClass(List<EmLimit> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<String, List<String>> classIdsByLimitIdMap = new HashMap<>();
        makeMap(list, classIdsByLimitIdMap);
        for (EmLimit limit : list) {
            if (classIdsByLimitIdMap.containsKey(limit.getId())) {
                //排个序
                limit.setClassIds(classIdsByLimitIdMap.get(limit.getId()).toArray(new String[]{}));
            }
        }
    }

    public void makeMap(List<EmLimit> list, Map<String, List<String>> classIdsByLimitIdMap) {
        Set<String> ids = EntityUtils.getSet(list, EmLimit::getId);
        List<EmLimitDetail> detailList = emLimitDetailService.findByLimitIdIn(ids.toArray(new String[]{}));
        if (classIdsByLimitIdMap == null || CollectionUtils.isEmpty(detailList)) {
            return;
        }
        for (EmLimitDetail item : detailList) {
//			if(ExammanageConstants.CLASS_TYPE1.equals(item.getClassType())){
            //行政班
            if (!classIdsByLimitIdMap.containsKey(item.getLimitId())) {
                classIdsByLimitIdMap.put(item.getLimitId(), new ArrayList<String>());
            }
            //去重
            if (classIdsByLimitIdMap.get(item.getLimitId()).contains(item.getClassId())) {
                continue;
            }
            classIdsByLimitIdMap.get(item.getLimitId()).add(item.getClassId());
//			}else{
//				//教学班 暂时没有
//			}
        }
    }

    @Override
    public void deleteByIds(String[] ids) {
        if (ids != null && ids.length > 0) {
            List<EmLimitDetail> findByLimitIdIn = emLimitDetailService.findByLimitIdIn(ids);
            Set<String> limitDetailIds = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(findByLimitIdIn)) {
                limitDetailIds = EntityUtils.getSet(findByLimitIdIn, "limitId");
            }
            if (ids != null && ids.length > 0)
                emLimitDao.deleteAllByIds(ids);
            if (limitDetailIds.size() > 0)
                emLimitDetailService.deleteAllByIds(limitDetailIds.toArray(new String[0]));
        }
    }

    @Override
    public void saveLimitAll(List<EmLimit> limitList, boolean isDeleteClass) {
        if (CollectionUtils.isEmpty(limitList)) {
            return;
        }
        List<EmLimitDetail> addLimitDetailInfo = new ArrayList<EmLimitDetail>();
        EmLimitDetail scoreLimitDetail = null;
        String id = null;
        Set<String> limitIds = new HashSet<String>();
        for (EmLimit item : limitList) {
            if (StringUtils.isBlank(item.getId())) {
                id = UuidUtils.generateUuid();
                item.setId(id);
            } else {
                id = item.getId();
                limitIds.add(id);
            }
            if (item.getClassIds() != null)
                for (String claId : item.getClassIds()) {
                    scoreLimitDetail = new EmLimitDetail();
                    scoreLimitDetail.setClassType(ExammanageConstants.CLASS_TYPE1);
                    scoreLimitDetail.setClassId(claId);
                    scoreLimitDetail.setLimitId(id);
                    addLimitDetailInfo.add(scoreLimitDetail);
                }
        }
        if (CollectionUtils.isNotEmpty(limitIds) && isDeleteClass) {
            Set<String> sldIds = new HashSet<String>();
            List<EmLimitDetail> sldList = emLimitDetailService.findByLimitIdIn(limitIds.toArray(new String[]{}));
            if (CollectionUtils.isNotEmpty(sldList)) {
                sldIds = EntityUtils.getSet(sldList, "id");
                emLimitDetailService.deleteAllByIds(sldIds.toArray(new String[0]));
            }
        }
        if (CollectionUtils.isNotEmpty(addLimitDetailInfo)) {
            emLimitDetailService.saveAllEntitys(addLimitDetailInfo.toArray(new EmLimitDetail[0]));
        }
        if (CollectionUtils.isNotEmpty(limitList)) {
            this.saveAllEntitys(limitList.toArray(new EmLimit[0]));
        }
    }

    @Override
    public List<EmLimit> saveAllEntitys(EmLimit... emLimitList) {
        if (emLimitList != null && emLimitList.length > 0) {
            return emLimitDao.saveAll(checkSave(emLimitList));
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void deleteByClassIdAndTeacherId(ScoreLimitSearchDto dto) {
        List<EmLimit> list = emLimitDao.findByExamIdST(dto.getExamId(), dto.getSubjectId(), dto.getTeacherId().split(","));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<String> ids = EntityUtils.getSet(list, EmLimit::getId);
        List<EmLimitDetail> detailList = emLimitDetailService.findByLimitIdIn(ids.toArray(new String[]{}));
        if (CollectionUtils.isEmpty(detailList)) {
            return;
        }
        Set<String> detailIds = new HashSet<>();
        for (EmLimitDetail e : detailList) {
            if (StringUtils.equals(e.getClassId(), dto.getClassId())) {
                detailIds.add(e.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(detailIds)) {
            emLimitDetailService.deleteAllByIds(detailIds.toArray(new String[0]));
            if (detailIds.size() == detailList.size()) {
                deleteAll(list.toArray(new EmLimit[]{}));
            }
        }
    }

    @Override
    public void saveAllEntitys(EmLimit[] emLimitList, EmLimitDetail[] detailList) {
        if (emLimitList != null && emLimitList.length > 0) {
            emLimitDao.saveAll(checkSave(emLimitList));
        }
        if (detailList != null && detailList.length > 0) {
            emLimitDetailService.saveAll(detailList);
        }
    }

    @Override
    public void initAllEntitys(Set<String> deleteIds, List<EmLimit> emlist, List<EmLimitDetail> emDetaills) {
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            List<EmLimitDetail> d1 = emLimitDetailService.findByLimitIdIn(deleteIds.toArray(new String[]{}));
            if (CollectionUtils.isNotEmpty(d1)) {
                emLimitDetailService.deleteAll(d1.toArray(new EmLimitDetail[]{}));
            }
            List<EmLimit> d2 = findListByIdIn(deleteIds.toArray(new String[]{}));
            if (CollectionUtils.isNotEmpty(d2)) {
                deleteAll(d2.toArray(new EmLimit[]{}));
            }
        }
        saveAllEntitys(emlist.toArray(new EmLimit[]{}), emDetaills.toArray(new EmLimitDetail[]{}));
    }

    @Override
    public Set<String> findByUnitId(String unitId) {
        return emLimitDao.findByUnitId(unitId).stream().map(EmLimit::getTeacherId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findByUnitIdAndExamId(String unitId, String examId) {
        return emLimitDao.findByUnitIdAndExamId(unitId, examId).stream().map(EmLimit::getTeacherId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findByUnitIdAndExamIdNot(String unitId, String examId) {
        return emLimitDao.findByUnitIdAndExamIdNot(unitId, examId).stream().map(EmLimit::getTeacherId).collect(Collectors.toSet());
    }

    @Override
    public List<EmLimit> findByExamIdTeacher(String examId,
                                             Set<String> teacherIds) {
        if (teacherIds != null && teacherIds.size() > 0) {
            List<EmLimit> list = emLimitDao.findByExamIdTeacher(examId, teacherIds.toArray(new String[]{}));
            makeClass(list);
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public void saveByTeacher(List<EmLimit> limitList, Set<String> teacherIds, String examId) {
        if (teacherIds != null && teacherIds.size() > 0) {
            deleteByTeacher(examId, teacherIds.toArray(new String[]{}));
        }
        if (CollectionUtils.isNotEmpty(limitList)) {
            saveLimitAll(limitList, false);
        }

    }


    public void deleteByTeacher(String examId, String... teacherIds) {
        if (teacherIds == null || teacherIds.length <= 0) {
            return;
        }
        List<EmLimit> list = emLimitDao.findByExamIdTeacher(examId, teacherIds);
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> ids = EntityUtils.getSet(list, "id");
            emLimitDetailService.deleteByLimitId(ids.toArray(new String[]{}));
            emLimitDao.deleteAllByIds(ids.toArray(new String[]{}));
        }

    }

    @Override
    public List<EmLimit> findByExamIdST(String examId, String teacherId,
                                        String subjectId, String classId) {
        List<EmLimit> limitList = null;
        if (StringUtils.isBlank(subjectId)) {
            limitList = emLimitDao.findByExamIdTeacher(examId, new String[]{teacherId});
        } else {
            limitList = emLimitDao.findByExamIdST(examId, subjectId, new String[]{teacherId});
        }
        if (StringUtils.isNotBlank(classId)) {
            Map<String, List<String>> classIdsByLimitIdMap = new HashMap<>();
            makeMap(limitList, classIdsByLimitIdMap);
            List<EmLimit> lastList = new ArrayList<>();
            limitList.forEach(item -> {
                List<String> classIds = classIdsByLimitIdMap.get(item.getId());
                if (CollectionUtils.isNotEmpty(classIds) && classIds.contains(classId)) {
                    lastList.add(item);
                }
            });
            return lastList;
        }
        return limitList;
    }

}
