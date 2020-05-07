package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisCustomAuthorDao;
import net.zdsoft.diathesis.data.dao.DiathesisRecordInfoDao;
import net.zdsoft.diathesis.data.dto.DiathesisTreeDto;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/6/11 9:52
 */
@Service("diathesisCustomAuthor")
public class DiathesisCustomAuthorServiceImpl extends BaseServiceImpl<DiathesisCustomAuthor, String> implements DiathesisCustomAuthorService {
    @Autowired
    private DiathesisScoreTypeService diathesisScoreTypeService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private DiathesisCustomAuthorDao diathesisCustomAuthorDao;
    @Autowired
    private DiathesisSubjectFieldService diathesisSubjectFieldService;
    @Autowired
    private DiathesisRecordSetService diathesisRecordSetService;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;

    @Autowired
    private DiathesisRecordInfoDao diathesisRecordInfoDao;

    @Autowired
    private DiathesisStructureService diathesisStructureService;

    @Autowired
    private DiathesisOptionService diathesisOptionService;
    @Autowired
    private DiathesisUnitService diathesisUnitService;
    @Autowired
    private DiathesisRecordMessageService diathesisRecordMessageService;

    @Override
    protected BaseJpaRepositoryDao<DiathesisCustomAuthor, String> getJpaDao() {
        return diathesisCustomAuthorDao;
    }

    @Override
    protected Class<DiathesisCustomAuthor> getEntityClass() {
        return DiathesisCustomAuthor.class;
    }

    @Override
    public boolean hasSetAuthor(String unitId) {
        List<Integer> authorList = EntityUtils.getList(diathesisCustomAuthorDao.findByUnitId(unitId), x -> x.getAuthorType());
        return authorList.contains(DiathesisConstant.AUTHOR_ADMIN) || authorList.contains(DiathesisConstant.AUTHOR_GOBAL_SET);
    }

    @Override
    public void saveChildUnitAuthor(String unitId, List<Integer> list, String realName) {
        try {
            RedisUtils.hasLocked("diathesisInitLock_" + unitId + "_78,");
            List<Integer> newList = new ArrayList<>();
            if (list.contains(DiathesisConstant.AUTHOR_ADMIN)) {
                newList.add(DiathesisConstant.AUTHOR_ADMIN);
            } else {
                if (list.contains(DiathesisConstant.AUTHOR_GOBAL_SET)) {
                    newList.add(DiathesisConstant.AUTHOR_GOBAL_SET);
                }
                if (list.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)) {
                    newList.add(DiathesisConstant.AUTHOR_PROJECT_ALL);
                } else {
                    if (list.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD)) {
                        newList.add(DiathesisConstant.AUTHOR_PROJECT_CHILD);
                    }
                    if (list.contains(DiathesisConstant.AUTHOR_PROJECT_RECORD)) {
                        newList.add(DiathesisConstant.AUTHOR_PROJECT_RECORD);
                    }
                }
            }
            DiathesisSet set = diathesisSetService.findByUnitId(unitId);
            List<Integer> oldList = EntityUtils.getList(diathesisCustomAuthorDao.findByUnitId(unitId), x -> x.getAuthorType());
            List<Integer> sameList = oldList.stream().filter(x -> newList.contains(x)).collect(Collectors.toList());

            //需要修改
            List<Integer> newProjectAuthor = changeAuthor(newList);

            List<Integer> oldProjectAuthor = changeAuthor(oldList);

            Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
            //如果原来有所有项目权限,现在没有所有项目权限, 那么初始化的一级项目,应该是当前单位的教育局所使用的一级项目
            String usingTopUnitId = findUsingUnitId(unit.getParentId(), DiathesisConstant.AUTHOR_PROJECT_ALL);
            String usingChildUnitId = findUsingUnitId(unit.getParentId(), DiathesisConstant.AUTHOR_PROJECT_CHILD);
            String usingRecordUnitId = findUsingUnitId(unit.getParentId(), DiathesisConstant.AUTHOR_PROJECT_RECORD);

            List<String> topIds = EntityUtils.getList(diathesisProjectService.findTopProjectByUnitId(usingTopUnitId), x -> x.getId());

            //二级+写实
            //List<DiathesisProject> secList = diathesisProjectService.findByUnitIdAndParentIdIn(unitId, topIds);
            //二级权限   从无-->有

            if ((oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL) && (newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD) && !newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)))
                    || ((!oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD) && !oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL) && (newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD) && !newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL))))) {


                List<DiathesisProject> secChildList = diathesisProjectService.findByUnitIdAndProjectTypeIn(usingChildUnitId, new String[]{DiathesisConstant.PROJECT_CHILD});
                // List<DiathesisProject> secChildList =secList.stream().filter(x->DiathesisConstant.PROJECT_CHILD.equals(x.getProjectType())).collect(Collectors.toList());
                //二级项目和三级项目id
                List<String> secIds = EntityUtils.getList(secChildList, x -> x.getId());

                //以前可能挂在这个一级类目的这个单位的二级类目
                List<DiathesisProject> delSecProject = diathesisProjectService.findByUnitIdAndParentIdIn(unitId, topIds).stream().filter(x -> DiathesisConstant.PROJECT_CHILD.equals(x.getProjectType())).collect(Collectors.toList());
                List<String> delIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(delSecProject)) {
                    List<DiathesisProject> delThirdProject = diathesisProjectService.findByUnitIdAndParentIdIn(unitId, EntityUtils.getList(delSecProject, x -> x.getId()));
                    if (CollectionUtils.isNotEmpty(delThirdProject)) {
                        delIds.addAll(EntityUtils.getList(delThirdProject, x -> x.getId()));
                    }
                    delIds.addAll(EntityUtils.getList(delSecProject, x -> x.getId()));
                }
                if (CollectionUtils.isNotEmpty(delIds)) {
                    diathesisProjectService.deleteByIdIn(delIds);
                    diathesisProjectExService.deleteByProjectIdsAndUnitId(delIds.toArray(new String[0]), unitId);
                }

                List<DiathesisProject> projectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(usingChildUnitId, new String[]{DiathesisConstant.PROJECT_CHILD});
                Map<String, String> idMap = EntityUtils.getMap(projectList, x -> x.getId(), x -> UuidUtils.generateUuid());
                List<DiathesisProject> projectSaveList = projectList.stream().map(x -> {
                    DiathesisProject project = EntityUtils.copyProperties(x, new DiathesisProject());
                    project.setId(idMap.get(x.getId()));
                    project.setUnitId(unitId);
                    project.setModifyTime(new Date());
                    project.setOperator(realName);
                    String parentId = idMap.get(x.getParentId());
                    if (StringUtils.isNotBlank(parentId)) project.setParentId(parentId);
                    return project;
                }).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(projectSaveList)) {
                    //二级,一级的id
                    Set<String> twoAndOndIds = EntityUtils.getSet(projectList, x -> x.getParentId());
                    //List<String> threeIds = idMap.keySet().stream().filter(x -> !twoAndOndIds.contains(x)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(projectSaveList))
                        diathesisProjectService.saveAll(projectSaveList.toArray(new DiathesisProject[0]));
                    // List<DiathesisProjectEx> exList = diathesisProjectExService.findByIdIn(unitId, threeIds, realName);
                    DiathesisProjectEx[] saveExList = projectList.stream().filter(x -> !twoAndOndIds.contains(x.getId())).map(x -> {
                        DiathesisProjectEx ex = new DiathesisProjectEx();
                        ex.setId(UuidUtils.generateUuid());
                        ex.setUnitId(unitId);
                        ex.setOperator(realName);
                        ex.setModifyTime(new Date());
                        ex.setProjectId(x.getId());
                        ex.setEvaluationTypes(StringUtils.defaultString(set.getEvaluation(), "1,2,3,4,5"));
                        return ex;
                    }).toArray(DiathesisProjectEx[]::new);
                    if (saveExList != null && saveExList.length > 0) diathesisProjectExService.saveAll(saveExList);
                }
                //todo? 成绩怎么改?
               /* List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findByUnitIdAndProjectIdIn(unitId, new ArrayList<String>(idMap.keySet()));
                List<DiathesisScoreInfo> infoSaveList = infoList.stream().map(x -> {
                    x.setObjId(idMap.get(x.getObjId()));
                    return x;
                }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(infoList)) {
                    diathesisScoreInfoService.saveAll(infoSaveList.toArray(new DiathesisScoreInfo[0]));
                }*/
            }

            if ((oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL) && (newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_RECORD) && !newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)))
                    || ((!oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_RECORD) && !oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL) && (newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_RECORD) && !newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_ALL))))) {

                List<DiathesisProject> secRecordList = diathesisProjectService.findByUnitIdAndParentIdIn(unitId, topIds).stream().filter(x -> DiathesisConstant.PROJECT_RECORD.equals(x.getProjectType())).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(secRecordList)) {
                    List<String> recordDeleteIds = EntityUtils.getList(secRecordList, x -> x.getId());
                    diathesisProjectService.deleteByIdIn(recordDeleteIds);
                    diathesisStructureService.deleteByProjectIdIn(recordDeleteIds);
                    diathesisOptionService.deleteByProjectIdIn(recordDeleteIds);
                    diathesisRecordSetService.deleteByIds(recordDeleteIds.toArray(new String[0]));
                    diathesisProjectExService.deleteByProjectIdsAndUnitId(recordDeleteIds.toArray(new String[0]), unitId);
                    //删除留言
                    diathesisRecordMessageService.deleteByRecordIds(recordDeleteIds);
                }

                //初始化 一级类目 所在的 写实记录
                List<DiathesisProject> recordProjectList = diathesisProjectService.findListByUnitIdAndProjectTypeIn(usingRecordUnitId, new String[]{DiathesisConstant.PROJECT_RECORD});
                Map<String, String> recordIdMap = EntityUtils.getMap(recordProjectList, x -> x.getId(), x -> UuidUtils.generateUuid());

                List<DiathesisProject> saveRecordList = recordProjectList.stream().map(x -> {
                    DiathesisProject project = new DiathesisProject();
                    project.setId(recordIdMap.get(x.getId()));
                    project.setUnitId(unitId);
                    project.setProjectName(x.getProjectName());
                    project.setProjectType(DiathesisConstant.PROJECT_RECORD);
                    project.setSortNumber(x.getSortNumber());
                    project.setRemark(x.getRemark());
                    project.setParentId(x.getParentId());
                    project.setModifyTime(new Date());
                    project.setOperator(realName);
                    return project;
                }).collect(Collectors.toList());

                //todo recordSet
                List<DiathesisRecordSet> saveRecordSetList = recordProjectList.stream().map(x -> {
                    DiathesisRecordSet recordSet = new DiathesisRecordSet();
                    recordSet.setId(recordIdMap.get(x.getId()));
                    recordSet.setModifyTime(new Date());
                    recordSet.setUnitId(unitId);
                    recordSet.setScore("1");
                    recordSet.setScoreType(DiathesisConstant.SCORE_ADD_TYPE_0);
                    recordSet.setCountType(DiathesisConstant.COUNT_TYPE_0);
                    recordSet.setOperator(realName);
                    return recordSet;
                }).collect(Collectors.toList());

                List<DiathesisStructure> structureListTemp = diathesisStructureService.findListByProjectIdIn(recordIdMap.keySet().toArray(new String[0]));
                List<DiathesisStructure> structureList = copyProjectList(structureListTemp);
                Map<String, String> structureIdMap = EntityUtils.getMap(structureList, x -> x.getId(), x -> UuidUtils.generateUuid());
                List<DiathesisStructure> saveStructureList = structureList.stream().map(x -> {
                    x.setUnitId(unitId);
                    x.setId(structureIdMap.get(x.getId()));
                    x.setProjectId(recordIdMap.get(x.getProjectId()));
                    return x;
                }).collect(Collectors.toList());

                List<DiathesisOption> optionListTemp = diathesisOptionService.findListByStructureIdIn(structureIdMap.keySet().toArray(new String[0]));
                List<DiathesisOption> optionList = copyOptionList(optionListTemp);
                List<DiathesisOption> saveOptionList = optionList.stream().map(x -> {
                    x.setUnitId(unitId);
                    x.setId(UuidUtils.generateUuid());
                    x.setProjectId(recordIdMap.get(x.getProjectId()));
                    x.setStructureId(structureIdMap.get(x.getStructureId()));
                    return x;
                }).collect(Collectors.toList());

                DiathesisProjectEx[] saveExList = recordProjectList.stream().map(x -> {
                    DiathesisProjectEx ex = new DiathesisProjectEx();
                    ex.setUnitId(unitId);
                    ex.setId(UuidUtils.generateUuid());
                    ex.setModifyTime(new Date());
                    ex.setProjectId(recordIdMap.get(x.getId()));
                    ex.setInputTypes(StringUtils.defaultString(set.getInputTypes(), turnToString(DiathesisConstant.ROLE_CODE_LIST)));
                    ex.setAuditorTypes(StringUtils.defaultString(set.getInputTypes(), turnToString(DiathesisConstant.ROLE_CODE_LIST)));
                    ex.setOperator(realName);
                    return ex;
                }).toArray(DiathesisProjectEx[]::new);


                if (!CollectionUtils.isEmpty(saveRecordSetList))
                    diathesisRecordSetService.saveAllEntity(saveRecordSetList);
                if (saveExList != null && saveExList.length > 0)
                    diathesisProjectExService.saveAll(saveExList);
                if (!CollectionUtils.isEmpty(saveOptionList))
                    diathesisOptionService.saveAll(saveOptionList.toArray(new DiathesisOption[0]));
                if (!CollectionUtils.isEmpty(saveStructureList))
                    diathesisStructureService.saveAll(saveStructureList.toArray(new DiathesisStructure[0]));
                if (!CollectionUtils.isEmpty(saveRecordList))
                    diathesisProjectService.saveAll(saveRecordList.toArray(new DiathesisProject[0]));
            }


            //二级项目权限更替的时候 需要删除现有的成绩(当前学期)
            //教育局需要删除 挂钩的数据

            if (newProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD) ^ oldProjectAuthor.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD)) {
                List<String> unitIds = new ArrayList<>();
                if (Unit.UNIT_CLASS_EDU == unit.getUnitClass()) {
                    //教育局
                    List<Unit> unitList = diathesisUnitService.findAllChildUnit(unitId); //SUtils.dt(unitRemoteService.findByRegionCode(regionCode), Unit.class);
                    List<DiathesisTreeDto> unitTree = diathesisUnitService.turnToTree(unitList, unitId);

                    List<String> unitIdList = EntityUtils.getList(unitList, x -> x.getId());
                    List<DiathesisCustomAuthor> authorList = findAuthorListByUnitIdInAndTypeIn(unitIdList, new Integer[]{DiathesisConstant.AUTHOR_ADMIN, DiathesisConstant.AUTHOR_PROJECT_CHILD});
                    Set<String> authoredUnitSet = EntityUtils.getSet(authorList, x -> x.getUnitId());
                    removeAuthorenUnit(unitTree, authoredUnitSet);
                    diathesisUnitService.getIds(unitTree, unitIds);
                    unitIds.add(unitId);
                } else {
                    unitIds.add(unitId);
                }
                //当前学年学期  上线改为0
                Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
                String year = semester.getAcadyear().substring(0, 4);
                List<DiathesisScoreType> typeList = diathesisScoreTypeService.findListByUnitIdsAndSemesterAndYear(unitIds, semester.getSemester(), year);
                if (!CollectionUtils.isEmpty(typeList)) {
                    String[] typeIds = EntityUtils.getArray(typeList, x -> x.getId(), String[]::new);
                    diathesisScoreInfoService.deleteByScoreTypeIdIn(typeIds);
                    diathesisScoreTypeService.deleteByIds(typeIds);
                }
            }

            newList.removeAll(sameList);
            oldList.removeAll(sameList);
            if (!CollectionUtils.isEmpty(oldList))
                diathesisCustomAuthorDao.deleteByUnitIdAndAuthorTypeIn(unitId, oldList);
            if (unit == null) {
                throw new RuntimeException("不存在这个单位");
            }
            List<DiathesisCustomAuthor> saveList = newList.stream().map(x -> {
                DiathesisCustomAuthor y = new DiathesisCustomAuthor();
                y.setId(UuidUtils.generateUuid());
                y.setAuthorType(x);
                y.setCreationTime(Calendar.getInstance().getTime());
                y.setIsDeleted(0);
                y.setModifyTime(y.getCreationTime());
                y.setOperator(realName);
                y.setRegionCode(unit.getRegionCode());
                y.setUnitClass(unit.getUnitClass());
                y.setUnitId(unitId);
                return y;
            }).collect(Collectors.toList());
            if (!saveList.isEmpty()) {
                diathesisCustomAuthorDao.saveAll(saveList);
            }

        } finally {
            RedisUtils.unLock("diathesisInitLock_" + unitId + "_78,");
        }

    }

    private String turnToString(List<String> auditorTypes) {
        if (auditorTypes == null || auditorTypes.size() == 0) return null;
        return StringUtils.join(auditorTypes.toArray(new String[0]), ",");
    }

    private void removeAuthorenUnit(List<DiathesisTreeDto> unitTree, Set<String> authoredUnitSet) {
        if (unitTree == null || unitTree.size() == 0) return;
        if (authoredUnitSet == null || unitTree.size() == 0) return;
        for (DiathesisTreeDto dto : unitTree) {
            if (authoredUnitSet.contains(dto.getId())) {
                dto.setChildList(null);
            } else {
                removeAuthorenUnit(dto.getChildList(), authoredUnitSet);
            }
        }
    }

    private List<DiathesisStructure> copyProjectList(List<DiathesisStructure> list) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) return new ArrayList<>();
        List<DiathesisStructure> result = new ArrayList<>();
        for (DiathesisStructure project : list) {
            result.add(EntityUtils.copyProperties(project, new DiathesisStructure()));
        }
        return result;
    }

    private List<DiathesisOption> copyOptionList(List<DiathesisOption> list) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(list)) return new ArrayList<>();
        List<DiathesisOption> result = new ArrayList<>();
        for (DiathesisOption project : list) {
            result.add(EntityUtils.copyProperties(project, new DiathesisOption()));
        }
        return result;
    }


    private List<Integer> changeAuthor(List<Integer> authorList) {
        List<Integer> list = new ArrayList<>();
        if (authorList.contains(DiathesisConstant.AUTHOR_ADMIN)) {
            list.addAll(DiathesisConstant.AUTHOR_TREE_LIST);
        }
        if (authorList.contains(DiathesisConstant.AUTHOR_GOBAL_SET)) {
            list.add(DiathesisConstant.AUTHOR_GOBAL_SET);
        }
        if (authorList.contains(DiathesisConstant.AUTHOR_PROJECT_ALL)) {
            list.add(DiathesisConstant.AUTHOR_PROJECT_ALL);
            list.add(DiathesisConstant.AUTHOR_PROJECT_CHILD);
            list.add(DiathesisConstant.AUTHOR_PROJECT_RECORD);
        }
        if (authorList.contains(DiathesisConstant.AUTHOR_PROJECT_CHILD)) {
            list.add(DiathesisConstant.AUTHOR_PROJECT_CHILD);
        }
        if (authorList.contains(DiathesisConstant.AUTHOR_PROJECT_RECORD)) {
            list.add(DiathesisConstant.AUTHOR_PROJECT_RECORD);
        }
        return list;
    }

    @Override
    public List<DiathesisCustomAuthor> findAuthorListByUnitId(String unitId) {
        Assert.notNull(unitId, "查询权限时,单位id不能为空!");
        //顶级教育局默认都是所有权限
        Unit unit = SUtils.dc(unitRemoteService.findTopUnit(unitId), Unit.class);
        Assert.notNull(unit, "查不到这个单位");
        List<DiathesisCustomAuthor> authorList = diathesisCustomAuthorDao.findByUnitId(unitId);
        if (unit.getId().equals(unitId) && CollectionUtils.isEmpty(authorList)) {
            DiathesisCustomAuthor author = new DiathesisCustomAuthor();
            author.setId(UuidUtils.generateUuid());
            author.setUnitId(unitId);
            author.setRegionCode(unit.getRegionCode());
            author.setOperator("admin");
            author.setModifyTime(new Date());
            author.setIsDeleted(0);
            author.setCreationTime(new Date());
            author.setAuthorType(1);
            author.setUnitClass(1);
            diathesisCustomAuthorDao.save(author);
            authorList.add(author);
        }
        return authorList;
    }

    @Override
    public String findUsingUnitId(String unitId, Integer type) {
        while (!hasAuthorByType(unitId, type)) {
            Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
            if (unit.getParentId().equals(Unit.TOP_UNIT_GUID) || unit.getId().equals(unit.getRootUnitId())) {
                unitId = DiathesisConstant.TEMPLATE_UNIT_ID;
                break;
            }
            unitId = unit.getParentId();
        }
        return unitId;
    }

    @Override
    public boolean hasAuthorByType(String unitId, Integer type) {
        Set<Integer> list = EntityUtils.getSet(diathesisCustomAuthorDao.findByUnitId(unitId), x -> x.getAuthorType());
        list.retainAll(DiathesisConstant.AUTHOR_TREE_MAP.get(type));
        return !list.isEmpty();
    }

    @Override
    public List<DiathesisCustomAuthor> findByRegionCodeAndAuthorTypeIn(String region, Integer[] authorTypes) {
        return diathesisCustomAuthorDao.findByRegionCodeAndAuthorTypeIn(region, authorTypes);
    }

    @Override
    public List<DiathesisCustomAuthor> findAuthorListByUnitIdInAndTypeIn(List<String> unitIds, Integer[] types) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(unitIds)) return new ArrayList<>();
        return diathesisCustomAuthorDao.findByUnitIdInAndAuthorTypeIn(unitIds, types);
    }

    @Override
    public List<Unit> findAllChildUnit(String unitId) {
        return diathesisCustomAuthorDao.findAllChildUnit(unitId).stream().map(x -> {
            Unit unit = new Unit();
            unit.setId((String) x[0]);
            unit.setParentId((String) x[1]);
            unit.setUnitName((String) x[2]);
            unit.setUnitClass(Integer.parseInt(x[3].toString()));
            return unit;
        }).filter(x -> !unitId.equals(x.getId())).collect(Collectors.toList());
    }


}
