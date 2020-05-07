package net.zdsoft.studevelop.data.action;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import net.zdsoft.studevelop.data.entity.StudevelopTemplate;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateItem;
import net.zdsoft.studevelop.data.entity.StudevelopTemplateOptions;
import net.zdsoft.studevelop.data.service.StudevelopTemplateItemService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateOptionsService;
import net.zdsoft.studevelop.data.service.StudevelopTemplateService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by luf on 2018/12/17.
 */
@Controller
@RequestMapping("/stuDevelop/commonProject")
public class StudevelopCommonProjectAction  extends BaseAction {

    @Autowired
    private StudevelopTemplateService studevelopTemplateService;
    @Autowired
    private StudevelopTemplateOptionsService studevelopTemplateOptionsService;
    @Autowired
    private StudevelopTemplateItemService studevelopTemplateItemService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @RequestMapping("/head/index/page")
    public String head(String code , ModelMap map){



        map.put("code",code);
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {});
        if(CollectionUtils.isEmpty(acadyearList)){
            return errorFtl(map,"学年学期不存在");
        }
        map.put("acadyearList", acadyearList);
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(1,getLoginInfo().getUnitId()), Semester.class);
        if(semesterObj!=null){
            String acadyear=semesterObj.getAcadyear();
            String semester=semesterObj.getSemester()+"";
            map.put("acadyear", acadyear);
            map.put("semester", semester);
        }else{
            map.put("acadyear", "");
            map.put("semester", "");
        }
        //获取学段
        String unitId = getLoginInfo().getUnitId();
        String sections = schoolRemoteService.findSectionsById(unitId);
        String[] sectionArray = sections.split(",");
        Map<String, String> sectionMap = new HashMap<String, String>();
        if(sectionArray.length==0){
            map.put("section", sections);
        }else{
            for(String str : sectionArray){
                if("1".equals(str)){
                    sectionMap.put("1", "小学");
                }else if("2".equals(str)){
                    sectionMap.put("2", "初中");
                }else if("3".equals(str)){
                    sectionMap.put("3", "高中");
                }
            }
            map.put("sectionMap", sectionMap);
        }
        map.put("schoolId",getLoginInfo().getUnitId());
        List<McodeDetail> projectTyps = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-SXXMLX") ,McodeDetail.class);

        map.put("projectTyps", projectTyps);

        return "/studevelop/commonProjectItem/projectItemSetHead.ftl";
    }
    @RequestMapping("/projectItem/edit")
    @ControllerInfo(value = "项目链接")
    public String projectedit(String id, String acadyear, String semester, String gradeId, String section,String objectType,String code, ModelMap map){
        StudevelopTemplateItem studevelopTemplateItem;
        List<StudevelopTemplateOptions> optionsList = null;
        if(StringUtils.isNotEmpty(id)){
            studevelopTemplateItem = studevelopTemplateItemService.findOne(id);
            optionsList =  studevelopTemplateOptionsService.getOptionsListByTemplateItemId(new String[]{studevelopTemplateItem.getId()});

        }else{
            studevelopTemplateItem =  new StudevelopTemplateItem();
            studevelopTemplateItem.setObjectType(objectType);
        }
        if(StuDevelopConstant.TEMPLATE_CODE_THOUGHT.equals(code) && CollectionUtils.isEmpty(optionsList)){
            String[] str = new String[]{"好", "较好", "需努力"};
            optionsList = new ArrayList<>();
            for (String s : str) {
                StudevelopTemplateOptions op1 = new StudevelopTemplateOptions();
                op1.setOptionName(s);
                optionsList.add(op1);
            }
        }
        map.put("optionsList" ,optionsList);
        map.put("studevelopTemplateItem" ,studevelopTemplateItem);
        map.put("acadyear", acadyear);
        map.put("semester", semester);
        map.put("gradeId", gradeId);
        map.put("section", section);
        map.put("code", code);
        map.put("objectType", objectType);
        map.put("unitId", getLoginInfo().getUnitId());
        return "/studevelop/commonProjectItem/projectItemEdit.ftl";
    }
    @RequestMapping("/projectItem/list")
    public String projectList(String acadyear, String semester, String gradeId, String section,String objectType,String code, ModelMap map){
        String unitId = getLoginInfo().getUnitId();
        List<StudevelopTemplate> templates = studevelopTemplateService.getTemplateByCode(acadyear,semester,gradeId,section,code,unitId);
        StudevelopTemplate tem = null;
        if(CollectionUtils.isNotEmpty(templates)){
            tem = templates.get(0);
        }
        if( tem != null){
            List<StudevelopTemplateItem> templateItems = studevelopTemplateItemService.getTemplateItemListByObjectType(tem.getId() , objectType,null);
            //获取option 单选内容
			
			if(CollectionUtils.isNotEmpty(templateItems)){
				List<StudevelopTemplateOptions> optionList=studevelopTemplateOptionsService.getOptionsListByTemplateItemId
											(EntityUtils.getSet(templateItems, StudevelopTemplateItem::getId).toArray(new String[]{}));
				if(CollectionUtils.isNotEmpty(optionList)){
					//置入值
					Map<String, List<StudevelopTemplateOptions>> optionMap=optionList.stream().collect(Collectors.groupingBy(StudevelopTemplateOptions::getTemplateItemId));
					for (StudevelopTemplateItem item : templateItems) {
						if(optionMap.containsKey(item.getId())){
							List<StudevelopTemplateOptions> inOptionList=optionMap.get(item.getId());
							StringBuilder sb= new StringBuilder();
							int i=0;
							for(StudevelopTemplateOptions option:inOptionList){
								if(i!=0){
									sb.append("、");
								}
								i++;
								sb.append(option.getOptionName()+" ");
							}
							item.setOptionNames(sb.toString());
						}
					}
				}
			}
            map.put("itemList", templateItems);
        }
        return "/studevelop/commonProjectItem/projectItemList.ftl";
    }
    @ResponseBody
    @RequestMapping("/projectItem/save")
    @ControllerInfo(value = "项目保存")
    public String projectItemSave(StudevelopTemplateItem studevelopTemplateItem ){
        String result = "";
        try{
            String unitId = getLoginInfo().getUnitId();
            studevelopTemplateItem.setUnitId(unitId);
            result = studevelopTemplateItemService.saveOrUpdateStudevelopTemplateItem(studevelopTemplateItem);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/projectItem/delete")
    public String projectItemDelete(String id){
        try{
            studevelopTemplateItemService.deleteStudevelopTemplateItem(id);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/projectItem/optionDelete")
    public String projectItemOptionDelete(String id){
        try{
            StudevelopTemplateOptions options =studevelopTemplateOptionsService.findOne(id);
            studevelopTemplateOptionsService.delete(options);
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();
    }
    @ResponseBody
    @RequestMapping("/projectItemDoClosed")
    public String projectItemDoClosed(String id ,String state){
        try{
            StudevelopTemplateItem item = studevelopTemplateItemService.findOne(id);
            item.setIsClosed(state);
            studevelopTemplateItemService.update(item , id , new String[]{"isClosed"});
        }catch(Exception e){
            e.printStackTrace();
            return returnError();
        }

        return returnSuccess();
    }
}
