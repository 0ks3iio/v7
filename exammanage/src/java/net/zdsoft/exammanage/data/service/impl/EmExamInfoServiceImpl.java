package net.zdsoft.exammanage.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmExamInfoDao;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmJoinexamschInfoService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service("emExamInfoService")
public class EmExamInfoServiceImpl extends BaseServiceImpl<EmExamInfo, String>
        implements EmExamInfoService {

    @Autowired
    private EmExamInfoDao emExamInfoDao;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;

    @Override
    protected BaseJpaRepositoryDao<EmExamInfo, String> getJpaDao() {
        return emExamInfoDao;
    }

    @Override
    protected Class<EmExamInfo> getEntityClass() {
        return EmExamInfo.class;
    }

    @Override
    public List<EmExamInfo> findExamList(Integer day, String unitId,
                                         EmExamInfoSearchDto searchDto, boolean flag) {
        List<EmExamInfo> findExamInfoList = new ArrayList<>();
        /**
         * 1:本单位设定的考试
         * 2：直属教育局设定的考试
         * 3:参与的校校联考
         */
        if ("1".equals(searchDto.getSearchType())) {
            Specification<EmExamInfo> specification = findExamListSpecification(
                    searchDto.getSearchType(), unitId, searchDto, day, flag);
            findExamInfoList = emExamInfoDao.findAll(specification);
        } else if ("2".equals(searchDto.getSearchType())) {
            Specification<EmExamInfo> specification = findExamListSpecification(
                    searchDto.getSearchType(), unitId, searchDto, day, flag);
            findExamInfoList = emExamInfoDao.findAll(specification);
        } else if ("3".equals(searchDto.getSearchType())) {
            findExamInfoList = this.findExamInfoJoinList(day, unitId,
                    searchDto, flag);
        } else {
            // 所有
            Specification<EmExamInfo> specification = findExamListSpecification(
                    "1", unitId, searchDto, day, flag);
            List<EmExamInfo> findExamInfoList1 = emExamInfoDao
                    .findAll(specification);
            Set<String> examIds = new HashSet<String>();
            if (CollectionUtils.isNotEmpty(findExamInfoList1)) {
                findExamInfoList.addAll(findExamInfoList1);
                for (EmExamInfo exam : findExamInfoList1) {
                    examIds.add(exam.getId());
                }
            }
            specification = findExamListSpecification("2", unitId, searchDto,
                    day, flag);
            List<EmExamInfo> findExamInfoList2 = emExamInfoDao
                    .findAll(specification);
            if (CollectionUtils.isNotEmpty(findExamInfoList2)) {
                findExamInfoList.addAll(findExamInfoList2);
                for (EmExamInfo exam : findExamInfoList2) {
                    examIds.add(exam.getId());
                }
            }
            List<EmExamInfo> findExamInfoList3 = this.findExamInfoJoinList(day,
                    unitId, searchDto, flag);

            if (CollectionUtils.isNotEmpty(findExamInfoList3)) {
                for (EmExamInfo exam : findExamInfoList3) {
                    if (examIds.contains(exam.getId())) {
                        continue;
                    }
                    findExamInfoList.add(exam);
                }
            }
        }
        // 组装年级名称,统考类型
        makeGradeNames(findExamInfoList);
        return findExamInfoList;
    }

    private List<EmExamInfo> findExamInfoJoinList(Integer day, String unitId,
                                                  EmExamInfoSearchDto searchDto, boolean flag) {
        List<EmExamInfo> list = new ArrayList<EmExamInfo>();
        if (day == null) {
            list = emExamInfoDao.findExamInfoJoinList(unitId,
                    searchDto.getSearchAcadyear(),
                    searchDto.getSearchSemester());
            if (StringUtils.isNotBlank(searchDto.getSearchGradeCode()) && CollectionUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); ) {
                    EmExamInfo e = list.get(i);
                    if (!e.getGradeCodes().equals(searchDto.getSearchGradeCode())) {
                        list.remove(e);
                    } else {
                        i++;
                    }
                }
            }
            return list;
        }
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        theCa.add(theCa.DATE, -day);
        Date date = theCa.getTime();
        if (flag) {
            // 30天以内
            if (searchDto.getSearchAcadyear() == null) {
                //不受学年学期
                list = emExamInfoDao.findExamInfoJoinListByDayNew(unitId, date);
            } else {
                list = emExamInfoDao.findExamInfoJoinListByDayNew(unitId,
                        searchDto.getSearchAcadyear(),
                        searchDto.getSearchSemester(), date);
            }


        } else {
            if (searchDto.getSearchAcadyear() == null) {
                //不受学年学期
                list = emExamInfoDao.findExamInfoJoinListByDayOld(unitId, date);
            } else {
                list = emExamInfoDao.findExamInfoJoinListByDayOld(unitId,
                        searchDto.getSearchAcadyear(),
                        searchDto.getSearchSemester(), date);
            }
        }
        return list;
    }

    private void makeGradeNames(List<EmExamInfo> findExamInfoList) {
        // 年级Code列表
        Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
                mcodeRemoteService.findMapMapByMcodeIds(new String[]{
                        "DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2", "DM-RKXD-3", "DM-RKXD-9"}),
                new TR<Map<String, Map<String, McodeDetail>>>() {
                });
        for (EmExamInfo item : findExamInfoList) {
            String string = ExammanageConstants.allTklx.get(item
                    .getExamUeType());
            if (StringUtils.isBlank(string)) {
                item.setExamUeTypeName("");
            } else {
                item.setExamUeTypeName(string);
            }
            if (StringUtils.isNotBlank(item.getGradeCodes())) {
                String str1 = item.getGradeCodes().substring(0, 1);
                String str2 = item.getGradeCodes().substring(1);
                Map<String, McodeDetail> map = findMapMapByMcodeIds
                        .get("DM-RKXD-" + str1);
                if (map != null && map.containsKey(str2)) {
                    item.setGradeCodeName(map.get(str2).getMcodeContent());
                }
            }
        }
    }

    /**
     * 除校校联考外
     *
     * @param searchType 1本单位设定的考试,2直属教育局设定的考试
     * @param unitId
     * @param searchDto
     * @return
     */
    private Specification<EmExamInfo> findExamListSpecification(
            String searchType, final String unitId,
            final EmExamInfoSearchDto searchDto, final Integer day,
            final boolean flag) {
        Specification<EmExamInfo> specification = null;
        if ("1".equals(searchType)) {
            specification = new Specification<EmExamInfo>() {
                @Override
                public Predicate toPredicate(Root<EmExamInfo> root,
                                             CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = Lists.newArrayList();

                    ps.add(cb
                            .equal(root.get("unitId").as(String.class), unitId));
                    if (StringUtils.isNotBlank(searchDto.getSearchAcadyear())) {
                        ps.add(cb.equal(root.get("acadyear").as(String.class),
                                searchDto.getSearchAcadyear()));
                    }
                    if (StringUtils.isNotBlank(searchDto.getSearchSemester())) {
                        ps.add(cb.equal(root.get("semester").as(String.class),
                                searchDto.getSearchSemester()));
                    }

                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    if (StringUtils.isNotBlank(searchDto.getSearchGradeCode())) {
                        ps.add(cb.equal(
                                root.get("gradeCodes").as(String.class),
                                searchDto.getSearchGradeCode()));
                    }
                    if (StringUtils.isNotBlank(searchDto.getSearchGk())) {
                        ps.add(cb.equal(
                                root.get("isgkExamType").as(String.class),
                                searchDto.getSearchGk()));
                    }
                    if (day != null) {
                        Calendar theCa = Calendar.getInstance();
                        theCa.setTime(new Date());
                        theCa.add(theCa.DATE, -day);
                        Date date = theCa.getTime();

                        if (flag) {
                            ps.add(cb.greaterThanOrEqualTo(
                                    root.get("creationTime").as(Date.class),
                                    date));
                        } else {
                            ps.add(cb.lessThan(
                                    root.get("creationTime").as(Date.class),
                                    date));
                        }
                    }

                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.desc(root.get("creationTime").as(
                            Date.class)));
                    orderList.add(cb.desc(root.get("examCode").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
        } else if ("2".equals(searchType)) {
            Unit findOne = SUtils.dc(unitRemoteService.findOneById(unitId),
                    Unit.class);
            final String parentUnitId = findOne.getParentId();
            specification = new Specification<EmExamInfo>() {
                @Override
                public Predicate toPredicate(Root<EmExamInfo> root,
                                             CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = Lists.newArrayList();
                    ps.add(cb.equal(root.get("unitId").as(String.class),
                            parentUnitId));
                    if (StringUtils.isNotBlank(searchDto.getSearchAcadyear())) {
                        ps.add(cb.equal(root.get("acadyear").as(String.class),
                                searchDto.getSearchAcadyear()));
                    }
                    if (StringUtils.isNotBlank(searchDto.getSearchSemester())) {
                        ps.add(cb.equal(root.get("semester").as(String.class),
                                searchDto.getSearchSemester()));
                    }
                    Predicate or1 = cb.notEqual(root.get("examUeType").as(String.class), ExammanageConstants.TKLX_3);
                    Predicate or2 = cb.notEqual(root.<String>get("examUeType"), "4");
                    ps.add(cb.or(or1, or2));

                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    if (StringUtils.isNotBlank(searchDto.getSearchGradeCode())) {
                        ps.add(cb.equal(
                                root.get("gradeCodes").as(String.class),
                                searchDto.getSearchGradeCode()));
                    }

                    if (day != null) {
                        Calendar theCa = Calendar.getInstance();
                        theCa.setTime(new Date());
                        theCa.add(theCa.DATE, -day);
                        Date date = theCa.getTime();

                        if (flag) {
                            ps.add(cb.greaterThanOrEqualTo(
                                    root.get("creationTime").as(Date.class),
                                    date));
                        } else {
                            ps.add(cb.lessThan(
                                    root.get("creationTime").as(Date.class),
                                    date));
                        }
                    }

                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.desc(root.get("creationTime").as(
                            Date.class)));
                    orderList.add(cb
                            .desc(root.get("examCode").as(String.class)));

                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
        } else if ("3".equals(searchType)) {

        }
        return specification;
    }

    @Override
    public List<String> findExamCodeMax() {
        return emExamInfoDao.findExamCodeMax();
    }

    @Override
    public void saveExamInfoOne(EmExamInfo examInfo,
                                List<EmJoinexamschInfo> joinexamschInfoAddList) {
        emJoinexamschInfoService.deleteByExamId(examInfo.getId());
        emJoinexamschInfoService.saveAllEntitys(joinexamschInfoAddList
                .toArray(new EmJoinexamschInfo[0]));
        if (StringUtils.isBlank(examInfo.getIsgkExamType())) {
            examInfo.setIsgkExamType("0");
        }
        this.saveAllEntitys(examInfo);

    }

    @Override
    public List<EmExamInfo> saveAllEntitys(EmExamInfo... examInfo) {
        return emExamInfoDao.saveAll(checkSave(examInfo));
    }

    @Override
    public EmExamInfo findExamInfoOne(String id) {
        EmExamInfo examInfo = this.findOne(id);
        List<EmJoinexamschInfo> joinexamschInfoList = emJoinexamschInfoService
                .findByExamId(id);
        Map<String, String> map = new HashMap<String, String>();
        Set<String> schoolIds = new HashSet<String>();
        for (EmJoinexamschInfo item : joinexamschInfoList) {
            schoolIds.add(item.getSchoolId());
        }
        List<Unit> units = SUtils.dt(
                unitRemoteService.findListByIds(schoolIds.toArray(new String[0])),
                new TR<List<Unit>>() {
                });
        Map<String, Unit> unitMap = EntityUtils.getMap(units, "id");
        for (EmJoinexamschInfo item : joinexamschInfoList) {
            Unit unit = unitMap.get(item.getSchoolId());
            if (unit != null) {
                map.put(item.getSchoolId(), unit.getUnitName());
            } else {
                map.put(item.getSchoolId(), "未找到");
            }
        }
        examInfo.setLkxzSelectMap(map);
        return examInfo;
    }

    @Override
    public void deleteAllIsDeleted(String... ids) {
        emExamInfoDao.updateIsDelete(ids);
    }

    @Override
    public List<EmExamInfo> findByUnitIdAndAcadyear(String unitId,
                                                    String acadyear, String searchSemester) {
        return emExamInfoDao.findByUnitIdAndAcadyear(unitId, acadyear, searchSemester);
    }

    @Override
    public List<EmExamInfo> findBySemesterAndIdIn(String[] ids, String searchSemester) {
        if (ids == null || ids.length == 0) {
            return new ArrayList<>();
        }
        if (StringUtils.isBlank(searchSemester)) {
            return emExamInfoDao.findByIdIn(ids);
        }
        return emExamInfoDao.findBySemesterAndIdIn(ids, searchSemester);
    }

    @Override
    public List<EmExamInfo> findByUnitIdAndAcadAndGradeId(String unitId, String acadyear,
                                                          String searchSemester, String gradeCode) {
        if (StringUtils.isBlank(gradeCode) && StringUtils.isBlank(unitId)) {
            return emExamInfoDao.findByAcadyearAndSemester(acadyear, searchSemester);
        } else {
            if (StringUtils.isBlank(gradeCode)) {
                return emExamInfoDao.findByUnitIdAndAcadyear(unitId, acadyear, searchSemester);
            }
            if (StringUtils.isBlank(unitId)) {
                return emExamInfoDao.findByAcadyearAndSemesterAndGradeCodes(acadyear, searchSemester, gradeCode);
            }
        }
        return emExamInfoDao.findByUnitIdAndAcadAndGradeId(unitId, acadyear, searchSemester, gradeCode);
    }

    @Override
    public List<EmExamInfo> findByOriginExamId(String originExamId) {
        return emExamInfoDao.findByOriginExamId(originExamId);
    }

    @Override
    public List<EmExamInfo> findListByIdsNoDel(String[] ids) {
        return emExamInfoDao.findListByIdsNoDel(ids);
    }
}
