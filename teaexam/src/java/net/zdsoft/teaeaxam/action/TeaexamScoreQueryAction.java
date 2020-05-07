package net.zdsoft.teaeaxam.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamRegisterInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamRegisterInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

@Controller
@RequestMapping("/teaexam/scoreQuery")
public class TeaexamScoreQueryAction extends BaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamRegisterInfoService teaexamRegisterInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	
	@RequestMapping("/index/page")
	public String indexPage(HttpServletRequest req, ModelMap map){
//		int year = NumberUtils.toInt(req.getParameter("year")); 
//		int type = NumberUtils.toInt(req.getParameter("type"));;
//		Calendar now = Calendar.getInstance();
//		int nowy = now.get(Calendar.YEAR); 
//		if(year == 0) {
//			year = nowy;
//		}
//		map.put("maxYear", nowy+1);
//		map.put("minYear", nowy-5);
//		map.put("year", year);
//		map.put("type", type);
		List<TeaexamRegisterInfo> regList = teaexamRegisterInfoService.findByTeacherId(getLoginInfo().getOwnerId());
		Set<String> examIdSet = new HashSet<String>();
		Set<String> subIdSet = new HashSet<String>();
		for(TeaexamRegisterInfo reg : regList){
			examIdSet.add(reg.getExamId());
			subIdSet.add(reg.getSubjectInfoId());
		}
		Map<String, String> subNameMap = new HashMap<String, String>();
		if(CollectionUtils.isNotEmpty(subIdSet)){
			List<TeaexamSubject> subList = teaexamSubjectService.findListByIds(subIdSet.toArray(new String[0]));
			for(TeaexamSubject sub : subList){
				String subName = "";
				if(sub.getSection()==1){
					subName = sub.getSubjectName()+"（小学）";
				} else if (0 == sub.getSection()) {
					subName = sub.getSubjectName()+"（学前）";
				}else if(sub.getSection()==2){
					subName = sub.getSubjectName()+"（初中）";
				}else if(sub.getSection()==3){
					subName = sub.getSubjectName()+"（高中）";
				}				
				subNameMap.put(sub.getId(), subName);
			}
		}
		for(TeaexamRegisterInfo reg : regList){
			reg.setSubName(subNameMap.get(reg.getSubjectInfoId()));
		}
		List<TeaexamInfo> teaexamInfoList = new ArrayList<TeaexamInfo>();
		if(CollectionUtils.isNotEmpty(examIdSet)){
			List<TeaexamInfo> teaexamInfoListTemp = teaexamInfoService.findListByIds(examIdSet.toArray(new String[0]));
			for(TeaexamInfo exam : teaexamInfoListTemp){
				teaexamInfoList.add(exam);
			}
		}
		map.put("regList", regList);
		map.put("teaexamInfoList", teaexamInfoList);
		return "/teaexam/scoreQuery/scoreQuery.ftl";
	}
}
