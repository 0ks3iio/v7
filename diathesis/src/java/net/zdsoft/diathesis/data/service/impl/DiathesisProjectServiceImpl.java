package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.*;
import net.zdsoft.diathesis.data.dto.DiathesisChildProjectDto;
import net.zdsoft.diathesis.data.dto.DiathesisIdAndNameDto;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.diathesis.data.vo.DiathesisChildProjectVo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:44
 */
@Service("diathesisProjectService")
public class DiathesisProjectServiceImpl extends BaseServiceImpl<DiathesisProject, String> implements DiathesisProjectService {
    @Autowired
    private UnitRemoteService unitRemoteService;


    @Autowired
    private DiathesisRecordService diathesisRecordService;

    @Autowired
    private DiathesisOptionService diathesisOptionService;

    @Autowired
    private DiathesisRecordMessageService diathesisRecordMessageService;

    @Autowired
    private DiathesisRecordSetService diathesisRecordSetService;

    @Autowired
    private CustomRoleRemoteService customRoleRemoteService;

    @Autowired
    private CustomRoleUserRemoteService customRoleUserRemoteService;
    @Autowired
    private DiathesisProjectDao diathesisProjectDao;

    @Autowired
    private DiathesisRecordInfoService diathesisRecordInfoService;
    @Autowired
    private DiathesisSetService diathesisSetService;

    @Autowired
    private DiathesisOptionDao diathesisOptionDao;

    @Autowired
    private DiathesisStructureDao diathesisStructureDao;
    @Autowired
    private DiathesisProjectExService diathesisProjectExService;
    @Autowired
    private DiathesisStructureService diathesisStructureService;
    @Autowired
    private DiathesisCustomAuthorService diathesisCustomAuthorService;




    @Override
    protected BaseJpaRepositoryDao<DiathesisProject, String> getJpaDao() {
        return diathesisProjectDao;
    }

    @Override
    protected Class<DiathesisProject> getEntityClass() {
        return DiathesisProject.class;
    }

    @Override
    public List<DiathesisIdAndNameDto> findTopProjectByUnitId(String unitId) {
        List<DiathesisProject> topList = diathesisProjectDao.findTopProjectByUnitId(unitId);
        boolean isnull = false;
        for (DiathesisProject project : topList) {
            if (project.getTopProp() == null) isnull = true;
        }
        if (isnull) {
            boolean isFirst = true;
            for (DiathesisProject project : topList) {
                if (isFirst) {
                    isFirst = false;
                    project.setTopProp(100 - (topList.size() - 1) * (100 / topList.size()));
                } else {
                    project.setTopProp(100 / topList.size());
                }
            }
            diathesisProjectDao.saveAll(topList);
        }

        return topList.stream().map(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getProjectName());
            dto.setProportion(x.getTopProp());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DiathesisProject> findListByUnitIdAndProjectTypeIn(String unitId, String[] projectTypes) {
        if (projectTypes == null || projectTypes.length == 0) {
            projectTypes = new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD, DiathesisConstant.PROJECT_RECORD};
        }
        List<String> needList = Arrays.asList(projectTypes);
        String topUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        String childUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_CHILD);
        String recordUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);

        List<DiathesisProject> resultList = new ArrayList<>();
        //一级项目
        List<DiathesisProject> topList = copyProjectList(diathesisProjectDao.findTopProjectByUnitId(topUnitId));


        List<String> topIds = EntityUtils.getList(topList, x -> x.getId());
        if (needList.contains(DiathesisConstant.PROJECT_TOP)) {
            //装配 proportions
            Map<String, String> proportionMap = diathesisProjectExService.findByUnitIdAndProjectIdIn(unitId, topIds).stream().collect(Collectors.toMap(x -> x.getProjectId(), x -> x.getProportions()));
            for (DiathesisProject x : topList) {
                x.setProportions(proportionMap.get(x.getId()));
            }
            resultList.addAll(topList);
        }
        if (needList.contains(DiathesisConstant.PROJECT_CHILD)) {
            //二级项目
            // List<String> list = EntityUtils.getList(topList, x -> x.getId());
            List<DiathesisProject> secTempList = copyProjectList(diathesisProjectDao.findByUnitIdAndParentIdIn(childUnitId, EntityUtils.getList(topList, x -> x.getId()).toArray(new String[0])));

            List<DiathesisProject> secList =
                    secTempList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)).collect(Collectors.toList());

            List<String> secIds = EntityUtils.getList(secList, x -> x.getId());
            if (CollectionUtils.isNotEmpty(secIds)) {
                //三级项目
                List<DiathesisProject> tempThreeList = diathesisProjectDao.findByUnitIdAndParentIdIn(childUnitId, secIds.toArray(new String[0]));
                if (CollectionUtils.isNotEmpty(tempThreeList)) {
                    List<DiathesisProject> threeList = copyProjectList(tempThreeList);
                    List<String> thirdIds = EntityUtils.getList(threeList, x -> x.getId());
                    List<DiathesisProjectEx> exList = diathesisProjectExService.findByUnitIdAndProjectIdIn(unitId, thirdIds);
                    Map<String, String> exThreeMap = new HashMap<>();
                    for (DiathesisProjectEx x : exList) {
                        exThreeMap.put(x.getProjectId(), x.getEvaluationTypes());
                    }
                    for (DiathesisProject project : threeList) {
                        project.setEvaluationTypes(exThreeMap.get(project.getId()));
                    }
                    resultList.addAll(threeList);
                }
                resultList.addAll(secList);
            }

        }

        //写实记录
        if (needList.contains(DiathesisConstant.PROJECT_RECORD)) {
            // List<String> ids = EntityUtils.getList(topList, x -> x.getId());
            List<DiathesisProject> tempRecordList = diathesisProjectDao.findByUnitIdAndParentIdIn(recordUnitId, EntityUtils.getList(topList, x -> x.getId()).toArray(new String[0])).stream().filter(x -> DiathesisConstant.PROJECT_RECORD.equals(x.getProjectType())).collect(Collectors.toList());
            List<DiathesisProject> recordList = copyProjectList(tempRecordList);

            List<String> recordIds = EntityUtils.getList(recordList, x -> x.getId());
            Map<String, DiathesisProjectEx> recordMap = diathesisProjectExService.findByUnitIdAndProjectIdIn(recordUnitId, recordIds).stream().collect(Collectors.toMap(x -> x.getProjectId(), Function.identity()));
            for (DiathesisProject x : recordList) {
                DiathesisProjectEx project = recordMap.get(x.getId());
                x.setInputTypes(project.getInputTypes());
                x.setAuditorTypes(project.getAuditorTypes());
            }
            resultList.addAll(recordList);
        }
        return resultList;
    }

    private List<DiathesisProject> copyProjectList(List<DiathesisProject> list) {
        if (CollectionUtils.isEmpty(list))
            return new ArrayList<DiathesisProject>();
        List<DiathesisProject> result = new ArrayList<>();
        for (DiathesisProject project : list) {
            result.add(EntityUtils.copyProperties(project, new DiathesisProject()));
        }
        return result;
    }

    @Override
    public void deleteByProjectId(String projectId) {
        String[] projectIds = diathesisProjectDao.findAllChildProject(projectId).stream().map(x -> x.getId()).toArray(String[]::new);
        /* 删除8张表的信息*/
        if (projectIds.length == 0) {
            return;
        }
        diathesisRecordSetService.deleteByIds(projectIds);
        diathesisProjectDao.deleteProjectById(projectIds);
        diathesisOptionService.deleteByProjectIdIn(Arrays.asList(projectIds));
        diathesisStructureService.deleteByProjectIdIn(Arrays.asList(projectIds));
        diathesisProjectExService.deleteByProjectIds(projectIds);
        List<String> recordIds = diathesisRecordService.findIdsByProjectIdIn(projectIds);
        diathesisRecordService.deleteByProjectIds(projectIds);
        //String[] recordIds = diathesisRecordDao.findByProjectIdIn(projectIds).stream().map(x -> x.getId()).toArray(String[]::new);
        if (CollectionUtils.isEmpty(recordIds)) {
            return;
        }
        diathesisRecordInfoService.deleteAllByRecordIds(recordIds);
        diathesisRecordMessageService.deleteByRecordIds(recordIds);

    }

    @Override
    public void updateTopProjects(List<DiathesisIdAndNameDto> list, String unitId, String realName) {

        //批量删除
        List<String> newIds = list.stream().map(x -> x.getId()).filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toList());
        String[] deleteIds = diathesisProjectDao.findTopProjectByUnitId(unitId).stream().map(x -> x.getId()).filter(x -> !newIds.contains(x)).toArray(String[]::new);
        for (int i = 0; i < deleteIds.length; i++) {
            deleteByProjectId(deleteIds[i]);
        }
        List<DiathesisProject> saveList = new ArrayList<>();
        DiathesisProject tempProject = diathesisProjectDao.findTopProjectByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID).get(0);
        int i = 0;
        for (DiathesisIdAndNameDto dto : list) {
            DiathesisProject project;
            if (StringUtils.isBlank(dto.getId())) {
                //新增,修改模板即可
                project = tempProject.clone();
                project.setUnitId(unitId);
                project.setId(UuidUtils.generateUuid());
            } else {
                //更新
                project = diathesisProjectDao.findById(dto.getId()).get().clone();
            }
            project.setTopProp(dto.getProportion());
            project.setModifyTime(Calendar.getInstance().getTime());
            project.setSortNumber(i++);
            project.setProjectName(dto.getName());
            project.setOperator(realName);
            saveList.add(project);
        }
        if (saveList.size() > 0) {
            diathesisProjectDao.saveAll(saveList);
        }
    }

    @Override
    public List<DiathesisChildProjectDto> findChildProjectByParentIdAndUnitId(String parentId, String unitId, String usingUnitId, String realName) {
        //todo EvaluationTypes 这个从 project表分离了,需要改
        List<DiathesisProject> list = diathesisProjectDao.findChildByParentIdAndUnitId(parentId, usingUnitId).stream().filter(x -> DiathesisConstant.PROJECT_CHILD.equals(x.getProjectType())).collect(Collectors.toList());

        //二级+三级id
        Set<String> ids = EntityUtils.getSet(list, x -> x.getId());
        //二级+一级id
        Set<String> secIds = list.stream().map(x -> x.getParentId()).filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toSet());
        //ids 三级类目id
        ids.removeAll(secIds);
        Map<String, String> evaluationMap = new HashMap<>();
        Map<String, String> evaluationMap1 = new HashMap<>();
        List<DiathesisProjectEx> exList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            exList = diathesisProjectExService.findByUnitIdAndProjectIdIn(unitId, new ArrayList<>(ids));
        }
        //todo 需要优化 线上数据重复暂时这么写
        if (CollectionUtils.isEmpty(exList)) {
            evaluationMap = new HashMap<>();
        } else {
            for (DiathesisProjectEx ex : exList) {
                if (evaluationMap.get(ex.getProjectId()) == null) {
                    evaluationMap.put(ex.getProjectId(), ex.getEvaluationTypes());
                } else {
                    evaluationMap1.put(ex.getProjectId(), ex.getEvaluationTypes());
                }

            }
        }

        List<DiathesisProject> collect = list.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)).collect(Collectors.toList());
        List<DiathesisChildProjectDto> listTemp = new ArrayList<>();
        for (DiathesisProject x : collect) {
            DiathesisChildProjectDto dto = new DiathesisChildProjectDto();
            dto.setId(x.getId());
            dto.setParentId(x.getParentId());
            String s = evaluationMap.get(x.getId());
            if (StringUtils.isBlank(s)) {
                s = evaluationMap1.get(x.getId());
            }

            dto.setEvaluationTypes(toList(s));
            if (StringUtils.isBlank(s)) {
                dto.setEvaluationTypes(Arrays.asList("1", "2", "3", "4", "5"));
            }
            dto.setRemark(x.getRemark());
            dto.setProjectName(x.getProjectName().equals("/") ? "" : x.getProjectName());
            dto.setProjectType(DiathesisConstant.PROJECT_CHILD);
            dto.setSortNumber(x.getSortNumber());
            listTemp.add(dto);
        }
        return listTemp;
    }

    @Override
    public Optional<DiathesisProject> findProjectById(String projectId) {
        return diathesisProjectDao.findById(projectId);
    }

    @Override
    public List<DiathesisIdAndNameDto> findRecordsByParentIdAndUnitId(String parentId, String unitId) {
        List<DiathesisProject> list = diathesisProjectDao.findRecordByParentIdAndUnitId(parentId, unitId);
        if (list == null) return new ArrayList<>();
        return list.stream().map(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getProjectName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void addAutoSetting(String unitId, String realName) {
        //设置初始化
        if (diathesisSetService.findByUnitId(unitId) == null) {
            DiathesisSet setting = diathesisSetService.findByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID).clone();
            setting.setUnitId(unitId);
            setting.setId(UuidUtils.generateUuid());
            setting.setModifyTime(new Date());
            setting.setCreationTime(new Date());
            setting.setOperator(realName);
            diathesisSetService.save(setting);
        }


        //默认角色初始化
        List<CustomRole> list = SUtils.dt(customRoleRemoteService.findListBy(new String[]{"unitId", "subsystems"}, new String[]{unitId, "78,"}), CustomRole.class);
        if (list == null || list.size() == 0) {
            autoCustonRole(unitId);
        }

        //获取地区,判断有没有地区模版
        String regionCode = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class).getRegionCode();
        String tempUnitId = StringUtils.leftPad(regionCode, 32, "0");
        List<DiathesisProject> exit = diathesisProjectDao.findTopProjectByUnitId(tempUnitId);
        List<DiathesisProject> projects;
        if (CollectionUtils.isNotEmpty(exit)) {
            // 二级三级走自己的模版    一级和写实走全国模版
            projects = new ArrayList<>();
            projects.addAll(diathesisProjectDao.findByUnitIdAndProjectTypeIn(DiathesisConstant.TEMPLATE_UNIT_ID, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_RECORD}));
            projects.addAll(diathesisProjectDao.findByUnitIdAndProjectTypeIn(tempUnitId, new String[]{DiathesisConstant.PROJECT_CHILD}));
        } else {
            tempUnitId = DiathesisConstant.TEMPLATE_UNIT_ID;
            projects = diathesisProjectDao.findByUnitIdAndProjectTypeIn(tempUnitId, new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD, DiathesisConstant.PROJECT_RECORD});
        }

        Map<String, String> idMap = projects.stream().collect(Collectors.toMap(x -> x.getId(), (x) -> UuidUtils.generateUuid()));
        List<DiathesisProject> childList = EntityUtils.filter2(projects, x -> DiathesisConstant.PROJECT_CHILD.equals(x.getProjectType()));
        //ids为三级类目
        Set<String> secAndThird = EntityUtils.getSet(childList, x -> x.getId());
        Set<String> secAndFirst = EntityUtils.getSet(childList, x -> x.getParentId());
        //一级,二级,三级,写实 初始化
        ArrayList<DiathesisProject> newProjects = new ArrayList<>();
        for (DiathesisProject x : projects) {
            DiathesisProject clone = x.clone();
            clone.setUnitId(unitId);
            clone.setOperator(realName);
            clone.setModifyTime(Calendar.getInstance().getTime());
            clone.setId(idMap.get(x.getId()));
            if (!StringUtils.isBlank(clone.getParentId())) {
                clone.setParentId(idMap.get(x.getParentId()));
            }
            newProjects.add(clone);
        }

        //写实记录赋分
        // List<DiathesisProject> recordList = childList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_RECORD)).collect(Collectors.toList());
        List<DiathesisRecordSet> recordSet = diathesisRecordSetService.findByUnitId(DiathesisConstant.TEMPLATE_UNIT_ID);

        List<DiathesisRecordSet> recordSetList = recordSet.stream().map(x -> {
            DiathesisRecordSet y = EntityUtils.copyProperties(x, new DiathesisRecordSet());
            y.setId(idMap.get(x.getId()));
            y.setOperator(realName);
            y.setUnitId(unitId);
            y.setModifyTime(new Date());
            return y;
        }).collect(Collectors.toList());
        diathesisRecordSetService.saveAllEntity(recordSetList);


        diathesisProjectDao.saveAll(newProjects);
        //saveAll(newProjects.toArray(DiathesisProject[]::new));
        //v1.2.0 新增  把inputTypes 和 auditorTypes proporation evaluation 从project表拆分出来
        //把二级项目的id 弄出来
        DiathesisProjectEx[] saveProjectExList = newProjects.stream().filter(x -> !(secAndFirst.contains(x.getId()) && secAndThird.contains(x.getId())))
                .map(x -> {
                    DiathesisProjectEx ex = new DiathesisProjectEx();
                    ex.setId(UuidUtils.generateUuid());
                    ex.setModifyTime(new Date());
                    ex.setProjectId(x.getId());
                    ex.setUnitId(x.getUnitId());
                    ex.setOperator(realName);
                    if (x.getProjectType().equals(DiathesisConstant.PROJECT_TOP)) {
                        //默认比例
                        ex.setProportions("20,20,20,20,20");
                    } else if (x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)) {
                        //默认评价人
                        ex.setEvaluationTypes("1,2,3,4,5");
                    } else {
                        ex.setAuditorTypes(turnToString(DiathesisConstant.ROLE_CODE_LIST));
                        ex.setInputTypes(ex.getAuditorTypes());
                    }
                    return ex;
                }).toArray(DiathesisProjectEx[]::new);

        diathesisProjectExService.saveAll(saveProjectExList);


        //structure初始化
        List<DiathesisStructure> structures = diathesisStructureDao.findByUnitId(tempUnitId);
        Map<String, String> structureIdMap = structures.stream().collect(Collectors.toMap(x -> x.getId(), x -> UuidUtils.generateUuid()));
        ArrayList<DiathesisStructure> newStructures = new ArrayList<>();
        for (DiathesisStructure x : structures) {
            DiathesisStructure clone = x.clone();
            clone.setUnitId(unitId);
            clone.setProjectId(idMap.get(x.getProjectId()));
            clone.setId(structureIdMap.get(x.getId()));
            newStructures.add(clone);
        }
        diathesisStructureDao.saveAll(newStructures);

        //option初始化
        List<DiathesisOption> options = diathesisOptionService.findByUnitId(tempUnitId);
        List<DiathesisOption> newOptions = new ArrayList<>();
        for (DiathesisOption x : options) {
            DiathesisOption clone = x.clone();
            clone.setId(UuidUtils.generateUuid());
            clone.setProjectId(idMap.get(x.getProjectId()));
            clone.setUnitId(unitId);
            clone.setStructureId(structureIdMap.get(x.getStructureId()));
            newOptions.add(clone);
        }
        diathesisOptionDao.saveAll(newOptions);

      /*  //todo 课程设置初始化
        List<DiathesisSubjectSet> subSetList = new ArrayList<>();
        List<DiathesisSubjectField> fieldList = new ArrayList<>();
        String section = schoolRemoteService.findSectionsById(unitId);
        if(StringUtils.isBlank(section)){
            //教育局
            section="1,2,3";
        }
        for (String s : section.split(",")) {
            if(!s.equals(BaseConstants.SECTION_HIGH_SCHOOL) && !s.equals(BaseConstants.SECTION_JUNIOR) && !s.equals(BaseConstants.SECTION_PRIMARY))continue;
            List<Course> temp=SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId, BaseConstants.SUBJECT_TYPE_BX, s), Course.class);
            //初始化 subject_set 表
            for (Course course : temp) {
                DiathesisSubjectSet subSet = new DiathesisSubjectSet();
                subSet.setSection(s);
                subSet.setId(UuidUtils.generateUuid());
                subSet.setModifyTime(new Date());
                subSet.setUnitId(unitId);
                subSet.setOperator(realName);
                subSet.setType(course.getType());
                subSet.setSortNum(course.getOrderId());
                subSet.setSubjectId(course.getId());
                subSetList.add(subSet);
                //初始化 field表
                if(BaseConstants.SUBJECT_TYPE_BX.equals(course.getType())){
                    //必修
                    setFieldList(unitId,subSet,DiathesisConstant.COMPULSORY_MAP,fieldList);
                }else {
                    //选修
                    setFieldList(unitId,subSet,DiathesisConstant.ELECTIVE_MAP,fieldList);
                }
            }
        }
        if(CollectionUtils.isNotEmpty(subSetList)) diathesisSubjectSetService.saveAllEntity(subSetList);
        if(CollectionUtils.isNotEmpty(fieldList)) diathesisSubjectFieldService.saveAllEntity(fieldList);
*/
//        //todo 字段初始化
//        //必修 选修 学业初始化
//        List<DiathesisSubjectField> fieldList = new ArrayList<>();
//        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_BX,DiathesisConstant.COMPULSORY_MAP,fieldList);
//        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_XX,DiathesisConstant.COMPULSORY_MAP,fieldList);
//        setFieldList(unitId,DiathesisConstant.SUBJECT_FEILD_XY,DiathesisConstant.COMPULSORY_MAP,fieldList);
//        diathesisSubjectFieldService.saveAllEntity(fieldList);
    }


    public void autoCustonRole(String unitId) {
        int size = DiathesisConstant.ROLE_CODE_LIST.size();
        CustomRole[] roles = new CustomRole[size];
        for (int i = 0; i < size; i++) {
            String code = DiathesisConstant.ROLE_CODE_LIST.get(i);
            roles[i] = createNewCustomRole(unitId, DiathesisConstant.ROLE_CODE_MAP.get(code), code, "" + i);
        }
        customRoleRemoteService.saveAll(roles);
    }

    public CustomRole createNewCustomRole(String unitId, String roleName, String roleCode, String orderId) {

        CustomRole role = new CustomRole();
        role.setId(UuidUtils.generateUuid());
        role.setUnitId(unitId);
        role.setRoleName(roleName);
        role.setRoleCode(roleCode);
        //当前子系统都默认78,
        role.setSubsystems("78,");
        role.setOrderId(orderId);
        role.setType("0");
        return role;
    }

//    @Override
//    public void deleteRoleByUnitIdAndRoleCode(String unitId, String roleCode) {
//        diathesisProjectDao.deleteRoleByUnitIdAndRoleCode(unitId,roleCode+",");
//        diathesisProjectDao.deleteRoleByUnitIdAndRoleCode(unitId,roleCode);
//    }

//    @Override
//    public void saveUnAuthorChildProjects(DiathesisChildProjectVo vo, String realName) {
//        Optional<DiathesisProject> project = diathesisProjectDao.findById(vo.getParentId());
//        DiathesisProject topProject = project.orElseThrow(() -> new RuntimeException("不存在这个一级类目")).clone();
//        topProject.setProportions(turnToString(vo.getProportions()));
//        //找到所有三级类目的 project 修改 评价人
//        List<DiathesisChildProjectDto> checkList = vo.getChildProjectList().stream().flatMap(x -> x.getChildProjectList().stream()).collect(Collectors.toList());
//        Map<String, List<String>> evaluateMap = checkList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getEvaluationTypes()));
//
//        String[] ids = EntityUtils.getArray(checkList, x -> x.getId(), String[]::new);
//        if (ids == null || ids.length == 0) return;
//        List<DiathesisProject> check = findListByIdIn(ids);
//        if (check.size() != ids.length) throw new RuntimeException("三级项目的id错误");
//        ArrayList<DiathesisProject> threeProjects = new ArrayList<>(check);
//        for (DiathesisProject t : threeProjects) {
//            t.setEvaluationTypes(turnToString(evaluateMap.get(t.getId())));
//        }
//        threeProjects.add(topProject);
//        diathesisProjectDao.saveAll(threeProjects);
//    }

//    @Override
//    public void autoChildProject(String unitId, String parentUnitId, String realName) {
//        //初始化二级类目
//        List<DiathesisProject> projectList = diathesisProjectDao.findListByUnitId(parentUnitId);
//        //二级,三级
//        List<DiathesisProject> childList = projectList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)).collect(Collectors.toList());
//        Map<String, String> idMap = childList.stream().collect(Collectors.toMap(x -> x.getId(), (x) -> UuidUtils.generateUuid()));
//        Map<String, String> parentMap = childList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getParentId()));
//        diathesisProjectDao.saveAll(childList.stream().map(x -> {
//            x.setModifyTime(new Date());
//            x.setOperator(realName);
//            x.setUnitId(unitId);
//            if (StringUtils.isNotBlank(idMap.get(x.getParentId()))) {
//                x.setParentId(idMap.get(x.getParentId()));
//            }else{
//                x.setParentId(parentMap.get(x.getParentId()));
//            }
//            x.setId(idMap.get(x.getId()));
//            return x;
//        }).collect(Collectors.toList()));
//        //初始化 评价占比
//
//       //todo
//        List<String> topIds = projectList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_TOP)).map(x -> x.getId()).collect(Collectors.toList());
//        ArrayList<DiathesisProjectEx> exListSave = new ArrayList<>();
//        //一级和二级id
//        List<String> secIds = EntityUtils.getList(childList, x -> x.getParentId());
//        //二级,三级id
//        List<DiathesisProjectEx> thirdIds = idMap.keySet().stream().filter(x -> !secIds.contains(x)).map(x -> {
//            DiathesisProjectEx e = new DiathesisProjectEx();
//            e.setId(UuidUtils.generateUuid());
//            e.setUnitId(unitId);
//            e.setModifyTime(new Date());
//            e.setOperator(realName);
//            e.setProjectId(x);
//            e.setEvaluationTypes("1,2,3,4,5");
//            return e;
//        }).collect(Collectors.toList());
//        exListSave.addAll(thirdIds);
//
//        List<DiathesisProjectEx> proportions = topIds.stream().map(x -> {
//            DiathesisProjectEx e = new DiathesisProjectEx();
//            e.setId(UuidUtils.generateUuid());
//            e.setUnitId(unitId);
//            e.setModifyTime(new Date());
//            e.setOperator(realName);
//            e.setProjectId(x);
//            e.setProportions("20,20,20,20,20");
//            return e;
//        }).collect(Collectors.toList());
//        exListSave.addAll(proportions);
//
//        diathesisProjectExService.saveAll(exListSave.toArray(new DiathesisProjectEx[0]));
//
//    }
//


//    @Override
//    public List<DiathesisProject> findMyProjectByUnitAndTypeIn(String unitId, String[] types) {
//        if (types == null || types.length == 0) {
//            return diathesisProjectDao.findListByUnitId(unitId);
//        }
//        return diathesisProjectDao.findByUnitIdAndProjectTypeIn(unitId, types);
//    }

    @Override
    public void updateRoleUser(String roleId, List<String> userIds) {
        customRoleUserRemoteService.deleteByRoleId(roleId);
        CustomRoleUser[] customRoleUsers = userIds.stream().map(x -> {
            CustomRoleUser roleUser = new CustomRoleUser();
            roleUser.setId(UuidUtils.generateUuid());
            roleUser.setRoleId(roleId);
            roleUser.setUserId(x);
            return roleUser;
        }).toArray(CustomRoleUser[]::new);

        customRoleUserRemoteService.saveAll(customRoleUsers);
    }

    @Override
    public Integer countTopProjectByUnitId(String unitId) {
        return diathesisProjectDao.countTopProjectByUnitId(unitId);
    }

//    @Override
//    public void deleteByUnitIdAndParentIdIn(String unitId, List<String> topIds) {
//        diathesisProjectDao.deleteByUnitIdAndParentIdIn(unitId,topIds);
//    }

    @Override
    public List<DiathesisProject> findByUnitIdAndParentIdIn(String unitId, List<String> topIds) {
        if (CollectionUtils.isEmpty(topIds))
            return new ArrayList<>();
        return diathesisProjectDao.findByUnitIdAndParentIdIn(unitId, topIds.toArray(new String[0]));
    }

    @Override
    public void deleteByIdIn(List<String> ids) {
        if (CollectionUtils.isEmpty(ids))
            return;
        diathesisProjectDao.deleteByIdIn(ids);
    }

    @Override
    public Map<String, Integer> countTopProjectMap(List<String> topProjectIds, String secUnitId, String projectType) {
        if (CollectionUtils.isEmpty(topProjectIds)) return new HashMap<>();
        List<Object[]> counts = diathesisProjectDao.countTopProjectMap(topProjectIds, secUnitId, projectType);
        HashMap<String, Integer> map = new HashMap<>();
        for (Object[] x : counts) {
            map.put((String) x[0], Integer.parseInt(x[1].toString()));
        }
        return map;
    }


    /**
     * 初始化写实记录
     * --只有 一级类目是上级的,写实记录是自己的时候才会调用这个初始化
     *
     * @param unitId
     * @param usingUnitId
     * @param realName
     */
//    @Override
//    public void autoRecordProject(String unitId, String usingUnitId, String realName) {
//        //实际调用的一级类目
//        List<DiathesisProject> projectList = diathesisProjectDao.findListByUnitId(usingUnitId);
//        List<DiathesisProject> recordList = projectList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_RECORD)).collect(Collectors.toList());
//        List<String> ids = EntityUtils.getList(recordList, x -> x.getId());
//        //List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectIdIn(usingUnitId, ids.toArray(new String[0]));
//        List<DiathesisStructure> structureList = diathesisStructureService.findListByProjectIdIn(ids.toArray(new String[0]));
//        List<DiathesisOption> optionList = diathesisOptionService.findByUnitId(usingUnitId);
//
//
//        //初始化recordProject
//        Map<String, String> recordIdMap = recordList.stream().collect(Collectors.toMap(x -> x.getId(), x -> UuidUtils.generateUuid()));
//        diathesisProjectDao.saveAll(recordList.stream().map(x -> {
//            x.setId(recordIdMap.get(x.getId()));
//            x.setModifyTime(new Date());
//            x.setOperator(realName);
//            x.setUnitId(unitId);
//            return x;
//        }).collect(Collectors.toList()));
//
//        //初始化structure
//        Map<String, String> structureIdMap = structureList.stream().collect(Collectors.toMap(x -> x.getId(), x -> UuidUtils.generateUuid()));
//        diathesisStructureService.saveAll(structureList.stream().map(x -> {
//            x.setId(structureIdMap.get(x.getId()));
//            x.setProjectId(recordIdMap.get(x.getProjectId()));
//            x.setUnitId(unitId);
//            return x;
//        }).toArray(DiathesisStructure[]::new));
//
//        //初始化option
//        diathesisOptionService.saveAll(optionList.stream().map(x -> {
//            x.setId(UuidUtils.generateUuid());
//            x.setUnitId(unitId);
//            x.setStructureId(structureIdMap.get(x.getStructureId()));
//            x.setProjectId(recordIdMap.get(x.getProjectId()));
//            return x;
//        }).toArray(DiathesisOption[]::new));
//
//
//        //初始化 project_ex  审核人 录入人
//        diathesisProjectExService.saveAll(ids.stream().map(x -> {
//            DiathesisProjectEx ex = new DiathesisProjectEx();
//            ex.setModifyTime(new Date());
//            ex.setUnitId(unitId);
//            ex.setOperator(realName);
//            ex.setProjectId(x);
//            ex.setId(UuidUtils.generateUuid());
//            ex.setInputTypes(turnToString(DiathesisConstant.ROLE_CODE_LIST));
//            ex.setAuditorTypes(turnToString(DiathesisConstant.ROLE_CODE_LIST));
//            return ex;
//        }).toArray(DiathesisProjectEx[]::new));
//    }

    /**
     * 保存二级,三级项目设置信息
     */
    public void saveChildProjects(DiathesisChildProjectVo diathesisChildProjectVo, String realName) {

        List<DiathesisChildProjectDto> list = diathesisChildProjectVo.getChildProjectList();
        if (list == null) {
            list = new ArrayList<>();
        }
        List<String> proportions = diathesisChildProjectVo.getProportions();
        String parentId = diathesisChildProjectVo.getParentId();

        List<String> projectIds = Stream.concat(list.stream().filter(x -> !StringUtils.isBlank(x.getId())).map(x -> x.getId()),
                list.stream().filter(x -> x.getChildProjectList() != null).flatMap(x -> x.getChildProjectList().stream().filter(y -> !StringUtils.isBlank(y.getId())).map(z -> z.getId())))
                .collect(Collectors.toList());
        List<String> oldProjectIds = diathesisProjectDao.findChildByParentIdAndUnitId(parentId, diathesisChildProjectVo.getUnitId())
                .stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)).map(x -> x.getId())
                .collect(Collectors.toList());
        oldProjectIds.removeAll(projectIds);
        //String[] deleteIds = oldProjectIds.filter(x -> !projectIds.contains(x)).toArray(String[]::new);
        for (String projectId : oldProjectIds) {
            deleteByProjectId(projectId);
        }
        /*一级项目 proportions (评价占比) 设置保存*/

        List<DiathesisProjectEx> exSaveList = new ArrayList<>();

        DiathesisProjectEx oldEx = diathesisProjectExService.findByUnitIdAndProjectId(diathesisChildProjectVo.getUnitId(), parentId);
        DiathesisProjectEx newEx;
        if (oldEx == null) {
            newEx = new DiathesisProjectEx();
        } else {
            newEx = oldEx.clone();
        }
        if (StringUtils.isBlank(newEx.getId())) newEx.setId(UuidUtils.generateUuid());
        newEx.setProportions(turnToString(proportions));
        newEx.setOperator(realName);
        newEx.setModifyTime(new Date());
        newEx.setUnitId(diathesisChildProjectVo.getUnitId());
        exSaveList.add(newEx);

        List<String> threeIds = list.stream().flatMap(x -> x.getChildProjectList().stream()).map(x -> x.getId()).filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toList());

        List<DiathesisProjectEx> exList = CollectionUtils.isEmpty(threeIds) ? new ArrayList<>() :
                diathesisProjectExService.findByUnitIdAndProjectIdIn(diathesisChildProjectVo.getUnitId(), threeIds);


        List<String> delExIds = EntityUtils.getList(exList, x -> x.getId());
        if (CollectionUtils.isNotEmpty(delExIds)) {
            diathesisProjectExService.deleteByIds(delExIds);
        }

        String unitId = diathesisChildProjectVo.getUnitId();
        List<DiathesisProject> saveList = new ArrayList<>();
        int i = 0;
        for (DiathesisChildProjectDto dto : list) {
            /*二级项目设置保存*/
            DiathesisProject project = saveProject(dto, unitId, realName);
            project.setSortNumber(i++);
            project.setParentId(parentId);
            saveList.add(project);

            List<DiathesisChildProjectDto> thirdProjects = dto.getChildProjectList();
            if (thirdProjects == null || thirdProjects.size() == 0) continue;
            int j = 0;
            /*三级项目设置 保存*/
            for (DiathesisChildProjectDto d : thirdProjects) {
                DiathesisProject thirdProject = saveProject(d, unitId, realName);
                thirdProject.setParentId(project.getId());
                thirdProject.setSortNumber(j++);

                DiathesisProjectEx e = new DiathesisProjectEx();
                e.setId(UuidUtils.generateUuid());
                e.setProjectId(thirdProject.getId());
                e.setOperator(realName);
                e.setModifyTime(new Date());
                e.setUnitId(unitId);
                e.setEvaluationTypes(turnToString(d.getEvaluationTypes()));
                exSaveList.add(e);
                saveList.add(thirdProject);
            }
        }
        if (exSaveList.size() > 0) {
            diathesisProjectExService.saveAll(exSaveList.toArray(new DiathesisProjectEx[0]));
        }
        if (saveList.size() > 0) {
            diathesisProjectDao.saveAll(saveList);
        }
    }

    //写实更新
    //需要删除 多余的 project_ex
    @Override
    public void updateRecords(List<DiathesisIdAndNameDto> dto, String unitId, String realName, String parentId) {
        //1.遍历找出 已经 不存在的id ,删除
        //2.判断id是否为空,空的话就 新增,不为空就更新
        List<String> pageIds = dto.stream().map(x -> x.getId()).filter(x -> !StringUtils.isBlank(x)).collect(Collectors.toList());
        DiathesisSet set = diathesisSetService.findByUnitId(unitId);
        List<DiathesisProject> list = diathesisProjectDao.findRecordByParentIdAndUnitId(parentId, unitId);
        for (DiathesisProject d : list) {
            if (!pageIds.contains(d.getId())) {
                deleteByProjectId(d.getId());
            }
        }

        int i = 0;
        ArrayList<DiathesisProject> saveList = new ArrayList<>();
        ArrayList<DiathesisRecordSet> saveRecordSetList = new ArrayList<>();
        for (DiathesisIdAndNameDto x : dto) {
            DiathesisProject project = null;
            if (StringUtils.isBlank(x.getId())) {
                //add
                project = diathesisProjectDao.findByUnitIdAndProjectTypeIn(DiathesisConstant.TEMPLATE_UNIT_ID, new String[]{DiathesisConstant.PROJECT_RECORD}).get(0).clone();
                project.setId(UuidUtils.generateUuid());
                project.setInputTypes(set.getInputTypes());
                project.setAuditorTypes(set.getAuditorTypes());

                DiathesisRecordSet recordSet = new DiathesisRecordSet();
                recordSet.setId(project.getId());
                recordSet.setModifyTime(new Date());
                recordSet.setUnitId(unitId);
                recordSet.setOperator(realName);
                recordSet.setCountType(DiathesisConstant.COUNT_TYPE_0);
                recordSet.setScoreType(DiathesisConstant.SCORE_ADD_TYPE_0);
                recordSet.setScore("1");

                saveRecordSetList.add(recordSet);
            } else {
                //update
                project = diathesisProjectDao.findById(x.getId()).get().clone();
            }
            project.setParentId(parentId);
            project.setOperator(realName);
            project.setModifyTime(Calendar.getInstance().getTime());
            project.setSortNumber(i++);
            project.setProjectName(x.getName());
            project.setUnitId(unitId);
            saveList.add(project);
        }
        if (CollectionUtils.isNotEmpty(saveRecordSetList)) {
            diathesisRecordSetService.saveAllEntity(saveRecordSetList);
        }
        if (saveList.size() > 0) {
            diathesisProjectDao.saveAll(saveList);
        }

    }


    private DiathesisProject saveProject(DiathesisChildProjectDto dto, String unitId, String ownerId) {
        DiathesisProject project;
        if (StringUtils.isBlank(dto.getId())) {
            //新增
            project = new DiathesisProject();
            project.setId(UuidUtils.generateUuid());
            project.setUnitId(unitId);
            project.setParentId(dto.getParentId());
        } else {
            //更新
            project = diathesisProjectDao.findById(dto.getId()).get().clone();
        }
        project.setEvaluationTypes(turnToString(dto.getEvaluationTypes()));
        project.setProportions(turnToString(dto.getProportions()));
        project.setProjectName(StringUtils.isBlank(dto.getProjectName()) ? "/" : dto.getProjectName());
        project.setOperator(ownerId);
        project.setModifyTime(Calendar.getInstance().getTime());
        project.setRemark(dto.getRemark());
        project.setProjectType(DiathesisConstant.PROJECT_CHILD);
        return project;
    }

    private List<String> toList(String str) {
        if (StringUtils.isBlank(str)) return null;
        return Arrays.asList(str.split(","));
    }

    private String turnToString(List<String> auditorTypes) {
        if (auditorTypes == null || auditorTypes.size() == 0) return null;
        return StringUtils.join(auditorTypes.toArray(new String[0]), ",");
    }

    @Override
    public List<DiathesisProject> findByUnitIdAndProjectTypeIn(String unitId, String[] projectTypes) {
        if (projectTypes == null || projectTypes.length == 0) {
            projectTypes = new String[]{DiathesisConstant.PROJECT_TOP, DiathesisConstant.PROJECT_CHILD, DiathesisConstant.PROJECT_RECORD};
        }
        List<String> needList = Arrays.asList(projectTypes);
        String topUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_ALL);
        String childUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_CHILD);
        String recordUnitId = diathesisCustomAuthorService.findUsingUnitId(unitId, DiathesisConstant.AUTHOR_PROJECT_RECORD);

        List<DiathesisProject> resultList = new ArrayList<>();
        //一级项目
        List<DiathesisProject> topList = diathesisProjectDao.findTopProjectByUnitId(topUnitId);
        if (CollectionUtils.isEmpty(topList)) {
            return resultList;
        }
        List<String> topIds = EntityUtils.getList(topList, x -> x.getId());
        if (needList.contains(DiathesisConstant.PROJECT_TOP)) {
            resultList.addAll(topList);
        }
        if (needList.contains(DiathesisConstant.PROJECT_CHILD)) {
            //二级项目
            // List<String> list = EntityUtils.getList(topList, x -> x.getId());
            List<DiathesisProject> secTempList = diathesisProjectDao.findByUnitIdAndParentIdIn(childUnitId, EntityUtils.getList(topList, x -> x.getId()).toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(secTempList)) {
                List<DiathesisProject> secList =
                        secTempList.stream().filter(x -> x.getProjectType().equals(DiathesisConstant.PROJECT_CHILD)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(secList)) {
                    resultList.addAll(secList);

                    List<String> secIds = EntityUtils.getList(secList, x -> x.getId());
                    //三级项目
                    //todo 三级项目 装配 评价人
                    List<DiathesisProject> tempThreeList = diathesisProjectDao.findByUnitIdAndParentIdIn(childUnitId, secIds.toArray(new String[0]));
                    if (CollectionUtils.isNotEmpty(tempThreeList)) {
                        resultList.addAll(tempThreeList);
                    }
                }
            }

        }

        //写实记录
        if (needList.contains(DiathesisConstant.PROJECT_RECORD)) {
            List<DiathesisProject> tempRecordList = diathesisProjectDao.findByUnitIdAndParentIdIn(recordUnitId, EntityUtils.getList(topList, x -> x.getId()).toArray(new String[0]));
            if (CollectionUtils.isNotEmpty(tempRecordList)) {
                tempRecordList = tempRecordList.stream().filter(x -> DiathesisConstant.PROJECT_RECORD.equals(x.getProjectType())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tempRecordList)) {
                    resultList.addAll(tempRecordList);
                }
            }
        }
        return resultList;
    }

//    @Override
//    public Integer[] findProjectEvaluationNumByUnitId(String unitId) {
//        List<DiathesisProject> list = findListByUnitIdAndProjectTypeIn(unitId, new String[]{DiathesisConstant.PROJECT_CHILD}, "admin");
//        Integer[] perNum = new Integer[]{0,0,0,0,0};
//        for (DiathesisProject project : list) {
//            String types = project.getEvaluationTypes();
//            if(StringUtils.isNotBlank(types)){
//                for (String s : types.split(",")) {
//                    perNum[Integer.parseInt(s)-1]++;
//                }
//            }
//        }
//        return perNum;
//    }

}
