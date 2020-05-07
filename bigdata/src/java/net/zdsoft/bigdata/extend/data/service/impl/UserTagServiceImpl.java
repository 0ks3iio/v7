package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.extend.data.dao.UserTagDao;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.bigdata.extend.data.service.UserProfileTemplateService;
import net.zdsoft.bigdata.extend.data.service.UserTagService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class UserTagServiceImpl extends BaseServiceImpl<UserTag, String> implements UserTagService {

    @Resource
    private UserTagDao userTagDao;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private BigLogService bigLogService;
    @Resource
    private UserProfileTemplateService userProfileTemplateService;

    @Override
    protected BaseJpaRepositoryDao<UserTag, String> getJpaDao() {
        return userTagDao;
    }

    @Override
    protected Class<UserTag> getEntityClass() {
        return UserTag.class;
    }

    @Override
    public List<UserTag> getUserTagByProfileCode(String profileCode) {
        return userTagDao.getUserTagByProfileCodeOrderByCreationTimeAsc(profileCode);
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveUserTag(UserTag userTag,int saveType) {
        if (StringUtils.isBlank(userTag.getUpdateCycle())) {
            userTag.setUpdateCycle("daily");
        }
        userTag.setModifyTime(new Date());
        if (StringUtils.isBlank(userTag.getId())) {
            userTag.setId(UuidUtils.generateUuid());
            userTag.setCreationTime(new Date());
            if (StringUtils.isBlank(userTag.getParentId())) {
                userTag.setParentId("00000000000000000000000000000000");
            }
            userTagDao.save(userTag);
            //业务日志埋点  新增
            LogDto logDto=new LogDto();
            logDto.setBizCode("insert-userTag");
            logDto.setDescription("标签 "+userTag.getTagName());
            logDto.setNewData(userTag);
            logDto.setBizName("标签库设置");
            bigLogService.insertLog(logDto);
            return;
        }
        List<TagRuleRelation> ruleRelations = tagRuleRelationService.getTagRuleRelationByTagId(userTag.getId());
        if(saveType ==1){
        	ruleRelations.forEach(e->{
        		e.setTagName(userTag.getTagName());
        		tagRuleRelationService.update(e, e.getId(), new String[]{"tagName"});
        	});
            UserTag oldUserTag = userTagDao.findById(userTag.getId()).get();
            userTagDao.update(userTag, new String[]{"tagName","targetColumn","modifyTime"});
            //业务日志埋点  修改
            LogDto logDto=new LogDto();
            logDto.setBizCode("update-userTag");
            logDto.setDescription("标签 "+oldUserTag.getTagName());
            logDto.setOldData(oldUserTag);
            logDto.setNewData(userTag);
            logDto.setBizName("标签库设置");
            bigLogService.updateLog(logDto);
        }else{
            userTagDao.update(userTag, new String[]{"tagName", "modifyTime", "updateCycle", "orderId","targetColumn"});
        }
    }

    @Override
    public void deleteUserTag(String id) {
        UserTag userTag = this.findOne(id);
        delete(id);
        if (!"00000000000000000000000000000000".equals(userTag.getParentId())) {
            tagRuleRelationService.deleteByTagId(id);
        } else {
            // 所有二级标签
            List<UserTag> child = userTagDao.getUserTagByParentId(id);
            // 删除所有标签规则
            child.forEach(e-> tagRuleRelationService.deleteByTagId(e.getId()));
            // 删除所有二级标签
            userTagDao.deleteUserTagByParentId(id);
            userProfileTemplateService.deleteBytagId(id);
        }
    }

    @Override
    public void saveUserTagAndTagRuleRelation(String updateCycle, List<TagRuleRelation> tagRuleRelations, String tagId, String profileCode, Integer orderId) {
        UserTag userTag = this.findOne(tagId);
        UserTag oldUserTag = new UserTag();
        EntityUtils.copyProperties(userTag,oldUserTag);

        userTag.setUpdateCycle(updateCycle);
        userTag.setOrderId(orderId);
//        if (tagRuleRelations.size() > 0) {
//            String ruleId = tagRuleRelations.get(0).getRuleId();
//            TagRule rule = tagRuleService.findOne(ruleId);
//            if (rule != null) {
//                userTag.setTargetColumn(rule.getTargetColumn());
//            }
//        }
        this.saveUserTag(userTag,2);
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-userTag");
        logDto.setDescription("子标签 "+oldUserTag.getTagName()+" 规则");
        logDto.setOldData(oldUserTag);
        logDto.setNewData(userTag);
        logDto.setBizName("标签库设置");
        bigLogService.updateLog(logDto);

        tagRuleRelationService.saveAllTagRuleRelation(tagRuleRelations, tagId, profileCode);
    }

    @Override
    public void updateUserTagCondition(UserTag userTag) {
        UserTag oldUserTag = userTagDao.findById(userTag.getId()).get();
        userTag.setModifyTime(new Date());
        userTagDao.update(userTag, new String[]{"modifyTime", "isMainQc", "isSecondaryQc", "isMultipleChoice", "updateCycle", "orderId"});
        //业务日志埋点  修改
        LogDto logDto=new LogDto();
        logDto.setBizCode("update-userTag");
        logDto.setDescription("父级标签 "+oldUserTag.getTagName()+" 的规则");
        logDto.setOldData(oldUserTag);
        logDto.setNewData(userTag);
        logDto.setBizName("标签库设置");
        bigLogService.updateLog(logDto);

    }

    @Override
    public List<UserTag> getSecondaryUserTagByProfileCode(String profileCode) {
        return userTagDao.getSecondaryUserTagByProfileCode(profileCode);
    }

	@Override
	public List<UserTag> getfirstUserTagByProfileCode(String profileCode) {
		return userTagDao.getfirstUserTagByProfileCode(profileCode);
	}

	@Override
	public List<UserTag> getSecondaryUserTagByParenrId(String parenrId) {
		return userTagDao.getSecondaryUserTagByParenrId(parenrId);
	}

}
