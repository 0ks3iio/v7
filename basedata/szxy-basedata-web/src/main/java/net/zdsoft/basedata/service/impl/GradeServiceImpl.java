package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.dao.GradeDao;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.service.ClassService;
import net.zdsoft.basedata.service.GradeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("gradeService")
public class GradeServiceImpl extends BaseServiceImpl<Grade, String> implements GradeService {

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private ClassService classService;

    @Autowired
    private McodeRemoteService mcodeRemoteService;

    @Override
    public List<Grade> findByUnitId(String unitId) {
        return gradeDao.findByUnitId(unitId);
    }

    @Override
    protected BaseJpaRepositoryDao<Grade, String> getJpaDao() {
        return gradeDao;
    }

    @Override
    public List<Grade> findBySectionAndAcadyear(String unitId, Integer section, String acadyear) {
        if (StringUtils.isBlank(acadyear)) {
            return gradeDao.findBySectionAndAcadyear(unitId, section);
        } else {
            return gradeDao.findBySectionAndAcadyear(unitId, section, acadyear);
        }

    }

    @Override
    public List<Grade> findByUnitId(String unitId, Pagination page) {
        Pageable pageable = page.toPageable();
        List<Grade> grades = gradeDao.findByUnitId(unitId, pageable);
        return grades;
    }

    @Override
    protected Class<Grade> getEntityClass() {
        return Grade.class;
    }

    @Override
    public List<Grade> findByUnitId(String unitId, Integer... section) {
        return gradeDao.findByUnitId(unitId, section);
    }

    @Override
    public List<Grade> findByUnitIdOrderByOpenAcadyear(String unitId) {
        return gradeDao.findByUnitIdOrderByOpenAcadyear(unitId);
    }

    @Override
    public List<Grade> findBySchoolIdAndAcadyear(String unitId, String acadyear) {
        return gradeDao.findBySchoolIdAndAcadyear(unitId, acadyear);
    }

    @Override
    public void deleteAllIsDeleted(String... ids) {
        gradeDao.updateIsDelete(ids);
    }

    @Override
    public void saveGradeOne(Grade grade) {
        this.saveAllEntitys(grade);
        List<Clazz> findByGradeId = classService.findByGradeIdIn(grade.getId());
        if (CollectionUtils.isNotEmpty(findByGradeId)) {
            for (Clazz item : findByGradeId) {
                item.setSchoolingLength(grade.getSchoolingLength());
            }
            classService.saveAllEntitys(findByGradeId.toArray(new Clazz[0]));
        }
    }

    @Override
    public List<Grade> findByUnitIdAndGradeCode(String unitId, String... gradeCodes) {
        return gradeDao.findByUnitIdAndGradeCode(unitId, gradeCodes);
    }
    @Override
    public List<Grade> findByUnitIdAndGradeCode(String unitId,Integer[] sections, String currentAcadyear) {
        List<Grade> ltg=gradeDao.findByUnitIdAndCurrentAcadyearAndSection(unitId,sections, currentAcadyear);
        if (CollectionUtils.isNotEmpty(ltg)) {
            int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentAcadyear, "-"));
            Integer section;
            Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
                    mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2",
                            "DM-RKXD-3", "DM-RKXD-9"}), new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                    });
            Map<String, McodeDetail> mcodeMap = null;
            for (Grade item : ltg) {
                mcodeMap = null;
                section = item.getSection();

                switch (section) {
                    case 0:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-0");
                        break;
                    case 1:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-1");
                        break;
                    case 2:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-2");
                        break;
                    case 3:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-3");
                        break;
                    case 9:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-9");
                        break;
                    default:
                        break;
                }
                if (mcodeMap == null) {
                    mcodeMap = new HashMap<String, McodeDetail>();
                }
                int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(item.getOpenAcadyear(), "-"));
                String gradeName = mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)) == null ? "未设置"
                        : mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)).getMcodeContent();
                item.setGradeName(gradeName);
                String gradeCode = section + String.valueOf(curAcadyear - openAcadyear + 1);
                item.setGradeCode(gradeCode);
            }
            return ltg;
        }
        return new ArrayList<Grade>();
    }
    @Override
    public List<Grade> findByUnitIdAndCurrentAcadyear(String unitid, String currentAcadyear, Integer isGraduate,
                                                      boolean ReCalculation) {
        List<Grade> ltg = null;
        if (isGraduate != null) {
            ltg = gradeDao.findByUnitIdAndCurrentAcadyear(unitid, currentAcadyear, isGraduate);
        } else {
            ltg = gradeDao.findByUnitIdAndCurrentAcadyear(unitid, currentAcadyear);
        }
        if (CollectionUtils.isNotEmpty(ltg)) {
            if (ReCalculation) {
                int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentAcadyear, "-"));
                Integer section;
                Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
                        mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2",
                                "DM-RKXD-3", "DM-RKXD-9"}), new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                        });
                Map<String, McodeDetail> mcodeMap = null;
                for (Grade item : ltg) {
                    mcodeMap = null;
                    section = item.getSection();

                    switch (section) {
                        case 0:
                            mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-0");
                            break;
                        case 1:
                            mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-1");
                            break;
                        case 2:
                            mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-2");
                            break;
                        case 3:
                            mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-3");
                            break;
                        case 9:
                            mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-9");
                            break;
                        default:
                            break;
                    }
                    if (mcodeMap == null) {
                        mcodeMap = new HashMap<String, McodeDetail>();
                    }
                    int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(item.getOpenAcadyear(), "-"));
                    String gradeName = mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)) == null ? "未设置"
                            : mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)).getMcodeContent();
                    item.setGradeName(gradeName);
                    String gradeCode = section + String.valueOf(curAcadyear - openAcadyear + 1);
                    item.setGradeCode(gradeCode);
                }
                return ltg;
            } else {
                return ltg;
            }
        }
        return new ArrayList<Grade>();
    }

    public List<Grade> findByUnitIdsAndCurrentAcadyear(String[] unitIds, String currentAcadyear) {
        List<Grade> ltg = gradeDao.findByUnitIdsAndCurrentAcadyear(unitIds, currentAcadyear);
        if (CollectionUtils.isNotEmpty(ltg)) {
            int curAcadyear = NumberUtils.toInt(StringUtils.substringBefore(currentAcadyear, "-"));
            Integer section;
            Map<String, Map<String, McodeDetail>> findMapMapByMcodeIds = SUtils.dt(
                    mcodeRemoteService.findMapMapByMcodeIds(new String[]{"DM-RKXD-0", "DM-RKXD-1", "DM-RKXD-2",
                            "DM-RKXD-3", "DM-RKXD-9"}), new TypeReference<Map<String, Map<String, McodeDetail>>>() {
                    });
            Map<String, McodeDetail> mcodeMap = null;
            for (Grade item : ltg) {
                mcodeMap = null;
                section = item.getSection();

                switch (section) {
                    case 0:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-0");
                        break;
                    case 1:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-1");
                        break;
                    case 2:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-2");
                        break;
                    case 3:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-3");
                        break;
                    case 9:
                        mcodeMap = findMapMapByMcodeIds.get("DM-RKXD-9");
                        break;
                    default:
                        break;
                }
                if (mcodeMap == null) {
                    mcodeMap = new HashMap<String, McodeDetail>();
                }
                int openAcadyear = NumberUtils.toInt(StringUtils.substringBefore(item.getOpenAcadyear(), "-"));
                String gradeName = mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)) == null ? "未设置"
                        : mcodeMap.get(String.valueOf(curAcadyear - openAcadyear + 1)).getMcodeContent();
                item.setGradeName(gradeName);
                String gradeCode = section + String.valueOf(curAcadyear - openAcadyear + 1);
                item.setGradeCode(gradeCode);
            }
        }
        return ltg;
    }

    @Override
    public Map<String, List<Grade>> findByUnitIdMap(final String[] unitIds) {
        List<Grade> gradeList = findByUnitIdsIn(unitIds);
        Map<String, List<Grade>> map = new HashMap<String, List<Grade>>();
        for (Grade item : gradeList) {
            List<Grade> list = map.get(item.getSchoolId());
            if (list == null) {
                list = new ArrayList<Grade>();
                map.put(item.getSchoolId(), list);
            }
            list.add(item);
        }
        return map;
    }
    @Override
    public List<Grade> findByUnitIdsIn(final String[] unitIds) {
        List<Grade> gradeList = new ArrayList<Grade>();
        if (unitIds != null && unitIds.length > 0) {
            Specification<Grade> s = new Specification<Grade>() {
                @Override
                public Predicate toPredicate(Root<Grade> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                    List<Predicate> ps = new ArrayList<Predicate>();
                    queryIn("schoolId", unitIds, root, ps, null);
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                    ps.add(cb.equal(root.get("isGraduate").as(Integer.class), 0));
                    List<Order> orderList = new ArrayList<Order>();
                    orderList.add(cb.asc(root.get("gradeCode").as(Integer.class)));
//                    orderList.add(cb.asc(root.get("displayOrder").as(Integer.class)));
//                    orderList.add(cb.asc(root.get("section").as(Integer.class)));
//                    orderList.add(cb.desc(root.get("openAcadyear").as(String.class)));
                    cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                    return cq.getRestriction();
                }
            };
            gradeList = gradeDao.findAll(s);
        }
        return gradeList;
    }

    @Override
    public List<Grade> findBySchoolIdsAndOpenAcaday(String[] schoolIds, String openAcadyear) {
        return gradeDao.findBySchoolIdsAndOpenAcaday(schoolIds,openAcadyear);
    }

    @Override
    public List<Grade> findByTeacherId(String teacherId) {
        return gradeDao.findByTeacherId(teacherId);
    }

    @Override
    public void updateGraduate(Date date, String schId, String acadYear, Integer section, Integer schoolingLength) {
        gradeDao.updateGraduate(date, schId, acadYear, section, schoolingLength);
    }

    @Override
    public List<Grade> findBySchidSectionAcadyear(String schoolId, String curAcadyear, Integer[] section) {
        return gradeDao.findBySchidSectionAcadyear(schoolId, curAcadyear, section);
    }

    @Override
    public List<Grade> findByUnitIdNotGraduate(String schoolId, Integer... section) {
        return gradeDao.findByUnitIdNotGraduate(schoolId, section);
    }

    @Override
    public List<Grade> saveAllEntitys(Grade... grade) {
        return gradeDao.saveAll(checkSave(grade));
    }

    @Override
    public List<Grade> findGradeList(final String schoolId, final Integer[] section, final String openAcadyear,
                                     final boolean isOnlyNotGraduate) {
        List<Grade> gradeList = new ArrayList<Grade>();
        Specification<Grade> s = new Specification<Grade>() {
            @Override
            public Predicate toPredicate(Root<Grade> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                if (isOnlyNotGraduate) {
                    ps.add(cb.equal(root.get("isGraduate").as(Integer.class), 0));
                }
                ps.add(cb.equal(root.get("openAcadyear").as(String.class), openAcadyear));
                ps.add(root.<Integer>get("section").in((Object[]) section));
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("displayOrder").as(Integer.class)));
                orderList.add(cb.asc(root.get("section").as(Integer.class)));
                orderList.add(cb.desc(root.get("openAcadyear").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        gradeList = gradeDao.findAll(s);
        return gradeList;
    }

    @Override
    public Grade findByIdAndCurrentAcadyear(String unitId, String gradeId,
                                            String searchAcadyear) {
        return gradeDao.findByIdAndCurrentAcadyear(unitId, gradeId, searchAcadyear);
    }

    @Override
    public Grade findById(String gradeId) {
        return gradeDao.findById(gradeId).orElse(null);
    }

    @Override
    public List<Grade> findByIdsIn(String[] ids) {
        return gradeDao.findByIdsIn(ids);
    }

    @Override
    public List<Grade> findBySchoolIdAndIsGraduate(String unitId,
                                                   Integer graduated) {
        return gradeDao.findBySchoolIdAndIsGraduate(unitId, graduated);
    }

    @Override
    public Grade findTimetableMaxRangeBySchoolId(String schoolId, String[] gradeIds) {
        Grade gd = new Grade();
        List<Grade> grades = null;
        if (gradeIds == null || gradeIds.length == 0) {
            grades = gradeDao.findByUnitId(schoolId);
        } else {
            grades = gradeDao.findByIdsIn(gradeIds);
        }

        Integer ms = 0;
        Integer am = 0;
        Integer pm = 0;
        Integer night = 0;
        Integer weekDays = 5;
        for (Grade g : grades) {
            g.setMornPeriods(g.getMornPeriods() == null ? 0 : g.getMornPeriods());
            if (g.getMornPeriods() >ms ) {
                ms = g.getMornPeriods();
            }
            g.setAmLessonCount(g.getAmLessonCount() == null ? 0 : g.getAmLessonCount());
            if (g.getAmLessonCount() >am) {
                am = g.getAmLessonCount();
            }
            g.setPmLessonCount(g.getPmLessonCount() == null ? 0 : g.getPmLessonCount());
            if (g.getPmLessonCount() >pm) {
                pm = g.getPmLessonCount();
            }
            g.setNightLessonCount(g.getNightLessonCount() == null ? 0 : g.getNightLessonCount());
            if (g.getNightLessonCount() >night) {
                night = g.getNightLessonCount();
            }
            g.setWeekDays(g.getWeekDays() == null ? 0 : g.getWeekDays());
            if (g.getWeekDays() >weekDays) {
                weekDays = g.getWeekDays();
            }
        }
        if(am==0 && pm==0) {
        	am=4;
        	pm=4;
        }
        
        gd.setMornPeriods(ms);
        gd.setAmLessonCount(am);
        gd.setPmLessonCount(pm);
        gd.setNightLessonCount(night);
        gd.setWeekDays(weekDays);
        return gd;
    }

    @Override
    public void deleteGradesBySchoolId(String unitId) {
        gradeDao.deleteGradesBySchoolId(unitId);
    }
}
