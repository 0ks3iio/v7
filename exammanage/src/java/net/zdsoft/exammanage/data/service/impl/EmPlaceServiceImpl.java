package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.remote.service.TeachPlaceRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmPlaceDao;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.exammanage.data.service.EmPlaceService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.exammanage.data.service.EmPlaceTeacherService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("emPlaceService")
public class EmPlaceServiceImpl extends BaseServiceImpl<EmPlace, String> implements EmPlaceService {

    @Autowired
    private EmPlaceDao emPlaceDao;
    @Autowired
    private TeachPlaceRemoteService teachPlaceRemoteService;
    @Autowired
    private TeachBuildingRemoteService teachBuildingRemoteService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;

    @Override
    protected BaseJpaRepositoryDao<EmPlace, String> getJpaDao() {
        return emPlaceDao;
    }

    @Override
    protected Class<EmPlace> getEntityClass() {
        return EmPlace.class;
    }

    @Override
    public List<EmPlace> findByExamIdAndSchoolIdWithMaster(String examId, String unitId, boolean isMake) {
        List<EmPlace> list = emPlaceDao.findByExamIdAndSchoolId(examId, unitId);
        if (isMake) {
            makePlaceName(list);
        }

        return list;
    }

    @Override
    public List<EmPlace> findByExamIdAndOptionIds(String examId, String[] optionIds, Pagination page) {
        return emPlaceDao.getEmPlaceOrder(examId, optionIds, page);
		/*Specification<EmPlace> specification = new Specification<EmPlace>() {
			 @Override
			 public Predicate toPredicate(Root<EmPlace> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("examId").as(String.class), examId));
				 if(null!=optionIds && optionIds.length>0){
                	In<String> in = cb.in(root.get("optionId").as(String.class));
                    for (int i = 0; i < optionIds.length; i++) {
                        in.value(optionIds[i]);
                    }
                    ps.add(in);
                 }
				 List<Order> orderList = new ArrayList<Order>();
				 orderList.add(cb.asc(root.get("examPlaceCode").as(String.class)));
	             cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				 return cq.getRestriction();
	         }
	     };
	     if(page != null) {
		    Pageable pageable = Pagination.toPageable(page);
		    Page<EmPlace> findAll = emPlaceDao.findAll(specification, pageable);
		    page.setMaxRowCount((int) findAll.getTotalElements());
		    List<EmPlace> emPlaceList = findAll.getContent();
			return emPlaceList;
	     }else {
	    	 return emPlaceDao.findByExamIdAndOptionIds(examId, optionIds);
	     }*/
    }

    @Override
    public List<EmPlace> findByExamIdAndOptionIds(String examId,
                                                  String[] optionIds) {
        return emPlaceDao.getEmPlacesByExamIdAndOptionIdInOrderByExamPlaceCode(examId, optionIds);
    }

    @Override
    public Set<String> getOptionId(String examId) {
        return emPlaceDao.getOptionId(examId);
    }

    public Map<String, EmPlace> findByIdsMap(String[] ids) {
        List<EmPlace> list = this.findListByIdIn(ids);
        Map<String, EmPlace> map = new HashMap<String, EmPlace>();
        if (CollectionUtils.isNotEmpty(list)) {
            makePlaceName(list);
            for (EmPlace ent : list) {
                map.put(ent.getId(), ent);
            }
        }
        return map;
    }

    public Map<String, EmPlace> findByUnitIdAndExamIdMap(String unitId, String examId) {
        List<EmPlace> list = emPlaceDao.findByExamIdAndSchoolId(examId, unitId);
        Map<String, EmPlace> map = new HashMap<String, EmPlace>();
        if (CollectionUtils.isNotEmpty(list)) {
            makePlaceName(list);
            for (EmPlace ent : list) {
                map.put(ent.getId(), ent);
            }
        }
        return map;
    }

    @Override
    public void makePlaceName(List<EmPlace> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, String> buildMap = new HashMap<String, String>();
            Map<String, TeachPlace> placeMap = new HashMap<String, TeachPlace>();
            Set<String> placeIds = EntityUtils.getSet(list, EmPlace::getPlaceId);
            if (placeIds.size() > 0) {
                List<TeachPlace> placeList = SUtils.dt(teachPlaceRemoteService.findTeachPlaceList(placeIds.toArray(new String[]{})), new TR<List<TeachPlace>>() {
                });
                if (CollectionUtils.isNotEmpty(placeList)) {
                    placeMap = EntityUtils.getMap(placeList, TeachPlace::getId);
                    Set<String> buildIds = EntityUtils.getSet(placeList, TeachPlace::getTeachBuildingId);
                    if (buildIds.size() > 0) {
                        buildMap = SUtils.dt(teachBuildingRemoteService.findTeachBuildMap(buildIds.toArray(new String[]{})), new TR<Map<String, String>>() {
                        });
                    }
                }
            }
            for (EmPlace em : list) {
                if (placeMap.containsKey(em.getPlaceId())) {
                    TeachPlace place = placeMap.get(em.getPlaceId());
                    em.setPlaceName(place.getPlaceName());
                    if (buildMap.containsKey(place.getTeachBuildingId())) {
                        String build = buildMap.get(place.getTeachBuildingId());
                        em.setBuildName(build);
                    }
                }
            }

        }
    }

    @Override
    public void deleteByExamIdAndSchoolId(String examId, String unitId) {
        emPlaceDao.deleteByExamIdAndSchoolId(examId, unitId);
    }

    @Override
    public void deleteByExamId(String examId) {
        emPlaceDao.deleteByExamId(examId);
    }

    @Override
    public void deleteByExamIdAndOptionId(String examId, String optionId) {
        emPlaceDao.deleteByExamIdAndOptionId(examId, optionId);
    }

    @Override
    public List<EmPlace> saveAllEntitys(EmPlace... emPlace) {
        return emPlaceDao.saveAll(checkSave(emPlace));
    }

    @Override
    public void insertEmPlaceList(List<EmPlace> emPlaceList, String[] emPlaceIds) {
        if (emPlaceIds != null && emPlaceIds.length > 0) {
            emPlaceStudentService.deleteByEmPlaceIds(emPlaceIds);
            emPlaceTeacherService.deleteByEmPlaceIds(emPlaceIds);
            emPlaceDao.deleteByIds(emPlaceIds);
        }
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            saveAllEntitys(emPlaceList.toArray(new EmPlace[]{}));
        }

    }

    @Override
    public EmPlace findByEmPlaceId(String id) {
        EmPlace emPlace = emPlaceDao.findByEmPlaceId(id);
        if (emPlace != null) {
            TeachPlace p = SUtils.dt(teachPlaceRemoteService.findTeachPlaceById(emPlace.getPlaceId()), new TR<TeachPlace>() {
            });
            if (p != null) {
                emPlace.setPlaceName(p.getPlaceName());
            }
        }
        return emPlace;
    }

    @Override
    public EmPlace findByExamIdAndSchoolIdAndPlaceId(String unitId,
                                                     String examId, String placeId) {
        return emPlaceDao.findByExamIdAndSchoolIdAndPlaceId(unitId, examId, placeId);
    }

    @Override
    public List<EmPlace> findByExamId(String examId) {
        return emPlaceDao.findByExamId(examId);
    }

    @Override
    public Integer getSizeByExamIdAndSchoolId(String examId, String unitId) {
        return emPlaceDao.getSizeByExamIdAndSchoolId(examId, unitId);
    }

}
