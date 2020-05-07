package net.zdsoft.teaeaxam.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dto.TeaexamSubjectLineDto;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLine;
import net.zdsoft.teaeaxam.service.TeaexamInfoService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLineService;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;
@Controller
@RequestMapping("/teaexam/subjectLine")
public class TeaexamSubjectLineAction extends BaseAction{
	@Autowired
	private TeaexamInfoService teaexamInfoService;
	@Autowired
	private TeaexamSubjectService teaexamSubjectService;
	@Autowired
	private TeaexamSubjectLineService teaexamSubjectLineService;
	@RequestMapping("/index/page")
	public String indexPage(String examId, String subjectId, HttpServletRequest req, ModelMap map){
		LoginInfo info = getLoginInfo();
		String unitId = info.getUnitId();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(new Date());
		int year = NumberUtils.toInt(req.getParameter("year")); 
		int type = TeaexamConstant.EXAM_INFOTYPE_0;
		Calendar now = Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR); 
		if(year == 0) {
			year = nowy;
		}
		map.put("maxYear", nowy+1);
		map.put("minYear", nowy-5);
		map.put("year", year);
		map.put("type", type);
		
		List<TeaexamInfo> examList = teaexamInfoService.findByEndTime(year, type, dateString,unitId);
		map.put("examList", examList);
		if(StringUtils.isBlank(examId) && CollectionUtils.isNotEmpty(examList)){
			examId = examList.get(0).getId();
		}
		if(CollectionUtils.isEmpty(examList)){
			examId = "";
		}
		if(StringUtils.isNotBlank(examId)){
			List<TeaexamSubject> subList = teaexamSubjectService.findByExamIds(new String[]{examId});
			for(TeaexamSubject sub : subList){
				if(sub.getSection()==1){
					sub.setSubjectName(sub.getSubjectName()+"（小学）");
				}else if(0==sub.getSection()){
					sub.setSubjectName(sub.getSubjectName()+"（学前）");
				}else if(sub.getSection()==2){
					sub.setSubjectName(sub.getSubjectName()+"（初中）");
				}else if(sub.getSection()==3){
					sub.setSubjectName(sub.getSubjectName()+"（高中）");
				}
			}
			if(StringUtils.isBlank(subjectId) && CollectionUtils.isNotEmpty(subList)){
				subjectId = subList.get(0).getId();
			}
			map.put("subList", subList);
		}
		if(StringUtils.isNotBlank(subjectId)){
			List<TeaexamSubjectLine> lineList = teaexamSubjectLineService.findBySubjectId(subjectId);
			TeaexamSubject subject = teaexamSubjectService.findOne(subjectId);
			map.put("fullScore", subject.getFullScore());
			for(TeaexamSubjectLine line : lineList){
				if(TeaexamConstant.SCORE_GRADE_YX.equals(line.getGradeCode())){
					map.put("yxMinScore", line.getMinScore());
				}else if(TeaexamConstant.SCORE_GRADE_HG.equals(line.getGradeCode())){
					map.put("hgMinScore", line.getMinScore());
				}
			}
		}
		map.put("subjectId", subjectId);
		map.put("examId", examId);
		return "/teaexam/subjectLine/subjectLine.ftl";
	}
	
	
	@ResponseBody
	@RequestMapping("/saveLine")
    @ControllerInfo(value = "")
	public String saveTeachers(TeaexamSubjectLineDto lineDto, Float fullScore, String subjectId){
		try{
			List<TeaexamSubjectLine> lineList = lineDto.getLineList();
			/*if(CollectionUtils.isNotEmpty(lineList)){
				List<TeaexamSubjectLine> lineList2 = lineList;
				int i=0;
				for(TeaexamSubjectLine line1 : lineList){
					int t=0;
					if(line1.getMaxScore()>fullScore){
						return error(line1.getGradeName()+"分数上限不能大于满分值！");
					}
					if(line1.getMinScore()>line1.getMaxScore()){
						return error(line1.getGradeName()+"分数下限不能大于分数上限！");
					}
					for(TeaexamSubjectLine line2 : lineList2){
						if(line1.getGradeName().equals(line2.getGradeName()) && i!=t){
							return error(line1.getGradeName()+"名称重复！");
						}
						if(((line1.getMinScore()<=line2.getMinScore() && line1.getMaxScore()>=line2.getMinScore()) || (line1.getMinScore()>=line2.getMinScore() &&
								line1.getMinScore()<=line2.getMaxScore()) || (line1.getMinScore()<=line2.getMinScore() && line1.getMaxScore()>=line2.getMaxScore())) && i!=t){
							return error(line1.getGradeName()+"与其他分数线上下限存在交叉！");
						}
						t++;
					}
					i++;
				}
			}*/
			Float yxMinScore = null;
			Float hgMinScore = null;
			if(CollectionUtils.isNotEmpty(lineList)){
				for(TeaexamSubjectLine line : lineList){
					if(TeaexamConstant.SCORE_GRADE_YX.equals(line.getGradeCode())){
						yxMinScore=line.getMinScore();
					}else if(TeaexamConstant.SCORE_GRADE_HG.equals(line.getGradeCode())){
						hgMinScore=line.getMinScore();
					}
				}
				if(yxMinScore>fullScore){
					return error("优秀下限分数不能大于满分值！");
				}
				if(hgMinScore>=yxMinScore){
					return error("合格下限分数不能大于或等于优秀下限分数！");
				}
			}
			teaexamSubjectLineService.saveLine(lineList, fullScore, subjectId);
		}catch(Exception e){
			e.printStackTrace();
			return error("操作失败！"+e.getMessage());
		}
		return returnSuccess();	
	}
}
