package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.entity.UserProfileTemplate;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.service.*;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;

/**
 * Created by wangdongdong on 2018/7/9 14:15.
 */
@Controller
@RequestMapping("/bigdata/userTag")
public class UserTagController extends BigdataBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(UserTagController.class);
    @Resource
    private UserTagService userTagService;
    @Resource
    private TagRuleSymbolService tagRuleSymbolService;
    @Resource
    private TagRuleTypeService tagRuleTypeService;
    @Resource
    private TagRuleRelationService tagRuleRelationService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private EsClientService esClientService;
    @Resource
    private UserProfileService userProfileService;
    @Resource
    private UserProfileTemplateService userProfileTemplateService;
    @Resource
    private BigLogService bigLogService;


    /**
     * 保存标签
     * @param userTag
     * @return
     */
    @ResponseBody
    @RequestMapping("/save")
    public Response save(UserTag userTag, int saveType) {
        List<UserTag> list = userTagService.findListBy(new String[]{"tagName", "profileCode", "parentId"}, new String[]{userTag.getTagName(), userTag.getProfileCode(), userTag.getParentId()});
        int idNums = 0;
        if(StringUtils.isNotBlank(userTag.getId())){
        	for(UserTag tag:list){
        		if(userTag.getId().equals(tag.getId())){
        			idNums++;
        		}
        	}
        }
        if (list.size() > idNums ) {
            return Response.error().message("标签名称已存在").build();
        }
        idNums=0;
        if("00000000000000000000000000000000".equals(userTag.getParentId())){
        	List<UserTag> list2 = userTagService.findListBy(new String[]{"targetColumn", "profileCode", "parentId"}, new String[]{userTag.getTargetColumn(), userTag.getProfileCode(), userTag.getParentId()});
        	if(StringUtils.isNotBlank(userTag.getId())){
            	for(UserTag tag:list2){
            		if(userTag.getId().equals(tag.getId())){
            			idNums++;
            		}
            	}
            }
        	if (list2.size() > idNums ) {
        		return Response.error().message("标签列名已存在").build();
        	}
        }
        try {
            userTag.setIsMultipleChoice((short) 1);
            userTagService.saveUserTag(userTag,saveType);
            if("00000000000000000000000000000000".equals(userTag.getParentId())){
            	List<UserProfile> userProfiles = userProfileService.findListBy("code",
            			userTag.getProfileCode());
            	UserProfile userProfile = userProfiles.get(0);
            	if(userProfile!=null){
            		List<UserTag> userTags = userTagService.getfirstUserTagByProfileCode(userProfile.getCode());
            		userTags.add(userTag);
            		Set<String> columns = userTags.stream().map(UserTag::getTargetColumn).filter(Objects::nonNull).collect(Collectors.toSet());
            		if(StringUtils.isNotBlank(userProfile.getBasicColumns())){
            			for(String col:userProfile.getBasicColumns().split(",")){
            				columns.add(col);
            			}
            		}
            		columns.forEach(e -> e.toLowerCase());
            		esClientService.upsertIndex(userProfile.getIndexName(), userProfile.getTypeName(), new ArrayList<String>(columns));
            	}
            }
            return Response.ok().data(userTag.getId()).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("保存失败，" + e.getMessage()).build();
        }
    }

    /**
     * 更新标签
     * @param userTag
     * @return
     */
    @ResponseBody
    @RequestMapping("/update")
    public Response update(UserTag userTag) {
        try {
            userTagService.updateUserTagCondition(userTag);
            return Response.ok().data(userTag.getId()).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("保存失败，" + e.getMessage()).build();
        }
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public Response delete(String id) {
        try {
            UserTag oldUserTag = userTagService.findOne(id);
            String tagName=oldUserTag.getTagName();
            userTagService.deleteUserTag(id);
            //业务日志埋点  删除
            LogDto logDto=new LogDto();
            logDto.setBizCode("delete-userTag");
            logDto.setDescription("标签 "+tagName);
            logDto.setBizName("标签库设置");
            logDto.setOldData(oldUserTag);
            bigLogService.deleteLog(logDto);

            return Response.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.error().message("删除失败，" + e.getMessage()).build();
        }
    }
    
    @RequestMapping("/edit")
    public String edit(String id,ModelMap map) {
    	UserTag tag = null;
    	if(StringUtils.isNotBlank(id)){
    		tag = userTagService.findOne(id);
    	}
    	if(tag==null)tag = new UserTag();
    	map.put("tag", tag);
		return "/bigdata/extend/userTag/editTag.ftl";
    }
    
//    /**
//     * 获取标签规则
//     * @param ruleType
//     * @param profileCode
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping("/getTagRule")
//    public Response getTagRule(Short ruleType, String profileCode) {
//        if (ruleType == null || StringUtils.isBlank(profileCode)) {
//            return Response.error().message("获取规则失败").build();
//        }
//        return Response.ok().data(tagRuleService.getTagRuleByProfileCodeAndRuleType(profileCode, ruleType)).build();
//    }

    /**
     * 获取标签规则符号
     * @param ruleType
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTagRuleSymbol")
    public Response getTagRuleSymbol(Short ruleType) {
        if (ruleType == null) {
            return Response.error().message("获取规则符号失败").build();
        }
        return Response.ok().data(tagRuleSymbolService.getTagRuleSymbolByRuleType(ruleType)).build();
    }

    @ResponseBody
    @RequestMapping("/getAllTagRuleType")
    public Response getAllTagRuleType() {
        return Response.ok().data(tagRuleTypeService.findAll()).build();
    }

    /**
     * 保存标签规则关系
     * @param tagRules
     * @param tagId
     * @param profileCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveTagRule")
    public Response saveTagRule(String tagRules, String tagId, String profileCode, String updateCycle, Integer orderId) {
        List<TagRuleRelation> tagRuleRelations = JSON.parseArray(tagRules, TagRuleRelation.class);
        userTagService.saveUserTagAndTagRuleRelation(updateCycle, tagRuleRelations, tagId, profileCode, orderId);
        return Response.ok().build();
    }

    /**
     * 根据标签获取关系
     * @param tagId
     * @param profileCode
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTagRuleRelationByTagId")
    public Response getTagRuleRelationByTagId(String tagId, String profileCode) {
        List<TagRuleRelation> tagRuleRelations = tagRuleRelationService.getTagRuleRelationByTagId(tagId);
        Map<Short, List<TagRuleRelation>> map =tagRuleRelations.stream().collect(Collectors.groupingBy(TagRuleRelation::getGroupId,
                Collectors.toList()));
        return Response.ok().data(map).build();
    }

    /**
     * 获取标签
     * @param tagId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserTagByTagId")
    public Response getUserTagByTagId(String tagId) {
        return Response.ok().data(userTagService.findOne(tagId)).build();
    }
    
    @RequestMapping("/meta/table/column")
	@ResponseBody
	public Response getMetaTableColumn(String metaId){
    	List<MetadataTableColumn> metaColumns = metadataTableColumnService.findByMetadataId(metaId);
    	for(MetadataTableColumn col:metaColumns){
    		if(StringUtils.isBlank(col.getDataDictionary())){
    			col.setDataDictionary("");
    		}
    	}
		return Response.ok().data(metaColumns).build();
	}
    /**
     * 群体画像设置
     * @param profileCode
     * @param map
     * @param type 0:设置  1.查看
     * @return
     */
    @RequestMapping("/group/portrait")
    public String groupPortrait(String profileCode,int type,String tagArray,ModelMap map) {
    	List<UserProfileTemplate> templates = userProfileTemplateService.findByUserProfileCode(profileCode);
    	List<UserProfile> userProfiles = userProfileService.findListBy("code",
    			profileCode);
    	UserProfile userProfile = userProfiles.get(0);
    	if(userProfile!=null){
    		map.put("userProfileName", userProfile.getName());
    	}
    	if(type==0)tagArray="[]";
    	map.put("templates", templates);
    	map.put("profileCode", profileCode);
    	map.put("type", type);
    	map.put("tagArray", tagArray);
    	return "/bigdata/extend/userTag/groupPortrait.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/group/delete/{id}")
    public Response getGroupDelete(@PathVariable String id) {
    	userProfileTemplateService.delete(id);
        return Response.ok().message("删除成功").build();
    }
    
    @RequestMapping("/group/edit")
    public String getGroupEdit(String profileCode,String id,ModelMap map) {
    	boolean isEdit = false;
    	UserProfileTemplate template = null;
    	if(StringUtils.isNotBlank(id)){
    		template = userProfileTemplateService.findOne(id);
    		if(template!=null){
    			isEdit =true;
    		}
    	}
    	if(template == null)template =new UserProfileTemplate();
    	template.setProfileCode(profileCode);
    	List<UserTag> tags = Lists.newArrayList();
    	if(isEdit){
    		tags.add(userTagService.findOne(template.getTagId()));
    	}else{
    		tags = userTagService.getfirstUserTagByProfileCode(profileCode);
    		List<UserProfileTemplate> templates = userProfileTemplateService.findByUserProfileCode(profileCode);
    		Map<String,UserProfileTemplate> temMap = EntityUtils.getMap(templates, UserProfileTemplate::getTagId);
    		tags = tags.stream().filter(t -> !temMap.containsKey(t.getId())).collect(Collectors.toList());
    	}
    	List<UserProfile> userProfiles = userProfileService.findListBy("code",
    			profileCode);
    	UserProfile userProfile = userProfiles.get(0);
    	if(userProfile!=null){
    		map.put("userProfileName", userProfile.getName());
    	}
    	map.put("tags", tags);
    	map.put("isEdit", isEdit);
    	map.put("template", template);
    	return "/bigdata/extend/userTag/groupPortraitEdit.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/group/save")
    public Response getGroupSave(UserProfileTemplate template) {
    	if(StringUtils.isBlank(template.getId())){
    		template.setId(UuidUtils.generateUuid());
    	}
    	userProfileTemplateService.save(template);
        return Response.ok().message("保存成功").build();
    }
    
    @RequestMapping("/group/detail/{id}")
    public String getGroupDetail(@PathVariable String id,int type,String tagArray,ModelMap map) {
    	UserProfileTemplate template = userProfileTemplateService.findOne(id);
    	UserTag tag = userTagService.findOne(template.getTagId());
    	map.put("tagName", tag.getTagName());
    	map.put("template", template);
    	map.put("type", type);
    	map.put("tagArray", tagArray);
        return "/bigdata/extend/userTag/groupPortraitDetail.ftl";
    }
    
}
