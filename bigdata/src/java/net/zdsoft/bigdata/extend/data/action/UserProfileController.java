package net.zdsoft.bigdata.extend.data.action;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.bigdata.extend.data.entity.TagRuleType;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.service.TagRuleSymbolService;
import net.zdsoft.bigdata.extend.data.service.TagRuleTypeService;
import net.zdsoft.bigdata.extend.data.service.UserProfileService;
import net.zdsoft.bigdata.extend.data.service.UserTagService;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by wangdongdong on 2018/7/9 11:24.
 */
@RequestMapping("/bigdata/userProfile")
@Controller
public class UserProfileController extends BigdataBaseAction{

    @Resource
    private UserProfileService userProfileService;
    @Resource
    private UserTagService userTagService;
    @Resource
    private TagRuleTypeService tagRuleTypeService;
    @Resource
    private TagRuleSymbolService tagRuleSymbolService;
    @Resource
    private MetadataService metadataService;
    @Resource
    private EsClientService esClientService;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("userProfileList", userProfileService.findAll());
        return "/bigdata/extend/userTag/userProfileList.ftl";
    }

    @RequestMapping("/userTagManage")
    public String userTagManage(Model model, String profileId) {
        UserProfile userProfile = userProfileService.findOne(profileId);
        model.addAttribute("userProfileList", userProfileService.findAll());
        model.addAttribute("userProfile", userProfile);
        List<UserTag> userTags = userTagService.getUserTagByProfileCode(userProfile.getCode());
        userTags.sort(new Comparator<UserTag>() {
            @Override
            public int compare(UserTag o1, UserTag o2) {
                if (o1.getOrderId() == null)
                    return 1;
                if (o2.getOrderId() == null) {
                    return -1;
                }
                return o1.getOrderId().compareTo(o2.getOrderId());
            }
        });
        model.addAttribute("tagList", userTags);
        List<TagRuleType> ruleTypes = tagRuleTypeService.findAll();

        Map<String, List<TagRuleSymbol>> tagRuleSymbolMap = Maps.newHashMap();
        ruleTypes.forEach(e -> tagRuleSymbolMap.put(e.getRuleType().toString(), tagRuleSymbolService.getTagRuleSymbolByRuleType(e.getRuleType())));
        List<Metadata> metas = metadataService.findTableNameOrColName(userProfile.getMainTableName(), userProfile.getForeignKey());
        String metadataId = "";
        String metaName = "未配置关联元数据";
        List<MetadataTableColumn> cols = Lists.newArrayList();
        List<MetadataTableColumn> exitCols = Lists.newArrayList();
        List<MetadataTableColumn> otherCols = Lists.newArrayList();
        if(userProfile.getMainTableName()!=null){
        	for(Metadata m:metas){
        		if(userProfile.getMainTableName().endsWith(m.getTableName())){
        			metadataId = m.getId();
        			cols = metadataTableColumnService.findByMetadataId(metadataId);
        			metaName = m.getName();
        			break;
        		}
        	}
        }
        String bcol = userProfile.getBasicColumns()==null?"":userProfile.getBasicColumns();
        String[] cls = bcol.split(",");
		Set<String> bcols = new HashSet<>(Arrays.asList(cls));
		for(MetadataTableColumn c:cols){
			if(bcols.contains(c.getColumnName())){
				exitCols.add(c);
			}else{
				otherCols.add(c);
			}
		}
        model.addAttribute("metas", metas);
        model.addAttribute("metaName", metaName);
        model.addAttribute("exitCols", exitCols);
        model.addAttribute("otherCols", otherCols);
        model.addAttribute("tagRuleSymbolMap", tagRuleSymbolMap);
        model.addAttribute("ruleTypes", ruleTypes);
        return "/bigdata/extend/userTag/userTagManage.ftl";
    }
    
    @ResponseBody
    @RequestMapping("/update")
    public Response updateProfile(UserProfile userProfile) {
    	try{
    		if(StringUtils.isBlank(userProfile.getId())){
    			return Response.error().message("保存失败，未选择对象").build();
    		}
    		UserProfile profile = userProfileService.findOne(userProfile.getId());
    		if(profile==null){
    			return Response.error().message("保存失败，对象不存在").build();
    		}
    		profile.setBasicColumns(userProfile.getBasicColumns());
    		userProfileService.save(profile);
    		return Response.ok().data(userProfile.getId()).build();
	    } catch (Exception e) {
	        return Response.error().message("保存失败，" + e.getMessage()).build();
	    }
    }
    
}


