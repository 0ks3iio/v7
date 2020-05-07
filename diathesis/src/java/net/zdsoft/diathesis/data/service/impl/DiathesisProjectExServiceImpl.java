package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisProjectExDao;
import net.zdsoft.diathesis.data.dto.DiathesisChildProjectDto;
import net.zdsoft.diathesis.data.entity.DiathesisProject;
import net.zdsoft.diathesis.data.entity.DiathesisProjectEx;
import net.zdsoft.diathesis.data.entity.DiathesisSet;
import net.zdsoft.diathesis.data.service.DiathesisProjectExService;
import net.zdsoft.diathesis.data.service.DiathesisProjectService;
import net.zdsoft.diathesis.data.service.DiathesisSetService;
import net.zdsoft.diathesis.data.vo.DiathesisChildProjectVo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
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
 * @Date: 2019/6/13 17:53
 */
@Service("diathesisProjectEx")
public class DiathesisProjectExServiceImpl extends BaseServiceImpl<DiathesisProjectEx,String> implements DiathesisProjectExService {
    @Autowired
    private DiathesisProjectExDao diathesisProjectExDao;
    @Autowired
    private DiathesisProjectService diathesisProjectService;
    @Autowired
    private DiathesisSetService diathesisSetService;
    @Override
    protected BaseJpaRepositoryDao<DiathesisProjectEx, String> getJpaDao() {
        return diathesisProjectExDao;
    }

    @Override
    protected Class<DiathesisProjectEx> getEntityClass() {
        return DiathesisProjectEx.class;
    }

    @Override
    public DiathesisProjectEx findByUnitIdAndProjectId(String unitId, String projectId) {
        if(StringUtils.isBlank(projectId))return null;
        DiathesisProjectEx ex=diathesisProjectExDao.findByUnitIdAndProjectId(unitId,projectId);
        DiathesisProject one = diathesisProjectService.findOne(projectId);
        DiathesisSet set = diathesisSetService.findByUnitId(unitId);

        String type = one.getProjectType();
        if(ex==null){
            ex=new DiathesisProjectEx();
            //ex.setId(UuidUtils.generateUuid());
            ex.setUnitId(unitId);
            ex.setProjectId(projectId);
            ex.setModifyTime(new Date());
           // ex.setOperator(realName);
            if(type.equals(DiathesisConstant.PROJECT_TOP)){
                ex.setProportions("20,20,20,20,20");
            }else if(type.equals(DiathesisConstant.PROJECT_RECORD)){
                ex.setAuditorTypes(StringUtils.defaultString(set.getInputTypes(),turnToString(DiathesisConstant.ROLE_CODE_LIST)));
                ex.setInputTypes(StringUtils.defaultString(set.getAuditorTypes(),turnToString(DiathesisConstant.ROLE_CODE_LIST)));
            }else if (DiathesisConstant.PROJECT_CHILD.equals(type)){
                //子项目
                //todo 改成从自己单位的全局设置取值
                ex.setEvaluationTypes(StringUtils.defaultString(set.getEvaluation(),"1,2,3,4,5"));
            }
            //外网数据重复,暂时屏蔽(同时注释id)   2019/10/30
           // diathesisProjectExDao.save(ex);
        }
        return ex;
    }

    @Override
    public List<DiathesisProjectEx> findByUnitIdAndProjectIdIn(String unitId, List<String> projectIdList) {
        List<String> projectIds = new ArrayList<>(projectIdList);
        Assert.notEmpty(projectIds,"该单位还没有开始录入");
        List<DiathesisProject> projectList = diathesisProjectService.findListByIdIn(projectIds.toArray(new String[0]));
        Map<String, String> typeMap = projectList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x.getProjectType()));

        List<DiathesisProjectEx> exList = diathesisProjectExDao.findByUnitIdAndProjectIdIn(unitId, projectIds);
        //现有的
        List<String> hasList = EntityUtils.getList(exList, x -> x.getProjectId());

        //要查询的-现有的 =需要初始化的
        if(CollectionUtils.isNotEmpty(hasList)) projectIds.removeAll(hasList);

        //初始化
        ArrayList<DiathesisProjectEx> saveList = new ArrayList<>();
        DiathesisSet set = diathesisSetService.findByUnitId(unitId);
        for (String p : projectIds) {
            DiathesisProjectEx ex = new DiathesisProjectEx();
            //ex.setId(UuidUtils.generateUuid());
            ex.setModifyTime(new Date());
            ex.setProjectId(p);
            ex.setUnitId(unitId);
            //ex.setOperator(realName);
            String type = typeMap.get(p);
            if(DiathesisConstant.PROJECT_TOP.equals(type)){
                ex.setProportions("20,20,20,20,20");
            }else if(DiathesisConstant.PROJECT_CHILD.equals(type)){
                ex.setEvaluationTypes(StringUtils.defaultString(set.getEvaluation(),"1,2,3,4,5"));
            }else {
                //改成从全局获取
                ex.setAuditorTypes(StringUtils.defaultString(set.getInputTypes(),turnToString(DiathesisConstant.ROLE_CODE_LIST)));
                ex.setInputTypes(StringUtils.defaultString(set.getAuditorTypes(),turnToString(DiathesisConstant.ROLE_CODE_LIST)));
            }
            saveList.add(ex);
        }
        //外网数据重复,暂时屏蔽(同时注释id)  2019/10/30
        //if(saveList.size()>0) diathesisProjectExDao.saveAll(saveList);
        exList.addAll(saveList);
        return CollectionUtils.isEmpty(exList)?new ArrayList<>():exList;
    }

    @Override
    public boolean existInputTypesByUnitIdAndRoleCode(String unitId, String roleCode) {
        Integer count=diathesisProjectExDao.countInputTypesByUnitIdAndRoleCode(unitId,"%"+roleCode+"%");
        return !(count==null || count==0);
    }

    @Override
    public void deleteByProjectIdsAndUnitId(String[] projectIds,String unitId) {
        if(projectIds==null || projectIds.length==0)return;
        diathesisProjectExDao.deleteByProjectIdsAndUnitId(projectIds,unitId);
    }

    @Override
    public void saveChildProjectEx(DiathesisChildProjectVo vo) {
        List<DiathesisChildProjectDto> childProjectList = vo.getChildProjectList();
        if (CollectionUtils.isEmpty(childProjectList))return;
        //key: projectId
        Map<String, List<String>> idEvaluateMap = childProjectList.stream().flatMap(x -> x.getChildProjectList().stream())
                .collect(Collectors.toMap(x -> x.getId(), x -> {
                    if(CollectionUtils.isEmpty(x.getEvaluationTypes())){
                        throw new RuntimeException("评价人至少选一个");
                    }
                    return x.getEvaluationTypes();
                }));
        if(CollectionUtils.isEmpty(idEvaluateMap.keySet()))return;
        List<DiathesisProjectEx> exList = diathesisProjectExDao.findByUnitIdAndProjectIdIn(vo.getUnitId(), new ArrayList<>(idEvaluateMap.keySet()));

        List<DiathesisProjectEx> saveEx = exList.stream().map(x -> {
            if(StringUtils.isBlank(x.getId()))x.setId(UuidUtils.generateUuid());
            x.setModifyTime(new Date());
            x.setOperator(vo.getRealName());
            x.setEvaluationTypes(turnToString(idEvaluateMap.get(x.getProjectId())));
            return x;
        }).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(saveEx))diathesisProjectExDao.saveAll(saveEx);
    }



    @Override
    public void deleteByProjectIds(String[] projectIds) {
        if(projectIds!=null && projectIds.length>0)
             diathesisProjectExDao.deleteByProjectIds(projectIds);
    }

    @Override
    public void deleteByIds(List<String> delExIds) {
        diathesisProjectExDao.deleteByIds(delExIds);
    }

    private String turnToString(List<String> auditorTypes) {
        if (auditorTypes == null || auditorTypes.size() == 0) return null;
        return StringUtils.join(auditorTypes.toArray(new String[0]), ",");
    }
}
