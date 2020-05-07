package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessRankDao;
import net.zdsoft.teacherasess.data.dto.RankDto;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.service.TeacherasessRankService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessRankService")
public class TeacherasessRankServiceImpl extends BaseServiceImpl<TeacherAsessRank, String> implements TeacherasessRankService{
	@Autowired
	private TeacherasessRankDao teacherasessRankDao;
	@Autowired
	private CourseRemoteService courseRemoteService;

	@Override
	public List<TeacherAsessRank> findByUnitIdAndAsessIdAndSubjectId(
			String unitId, String asessId, String subjectId) {
		return teacherasessRankDao.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
	}
	@Override
	public List<TeacherAsessRank> findByUnitIdAndAssessId(
			String unitId, String asessId) {
		return teacherasessRankDao.findByUnitIdAndAssessId(unitId, asessId);
	}
	
	@Override
	public List<RankDto> findRankDtoByUnitIdAndAsessIdAndSubjectId(
			String unitId, String asessId, String subjectId) {
		List<RankDto> rankDtos=new ArrayList<>();
		List<TeacherAsessRank> teacherAsessRanks=teacherasessRankDao.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
		if(CollectionUtils.isNotEmpty(teacherAsessRanks)){
			for (TeacherAsessRank teacherAsessRank : teacherAsessRanks) {
				for (int j = 0; j < 2; j++) {
					for (int i = 1; i <(StringUtils.equals(BaseConstants.ZERO_GUID, subjectId)?2:3); i++) {//上线
						String title="";
						if(i==2){
							title="双上线";
						}else{
							title="单上线";
						}
						String cla="";
						if(j==1){
							cla="B班";
						}else{
							cla="A班";
						}		
						RankDto rankDto=new RankDto();
						rankDto.setLineType(i+"");//1单上线，2双上线
						rankDto.setAsessRankId(teacherAsessRank.getId());
						rankDto.setSlice(j+"");//A班
						rankDto.setName((j==0?teacherAsessRank.getAslice():teacherAsessRank.getBslice())+"("+cla+title+")");
						rankDtos.add(rankDto);
						
					}
				}
			}
		}
		return rankDtos;
	}
	
	@Override
	public List<RankDto> findCheckRankDtoByUnitIdAndAsessIdAndSubjectId(
			String unitId, String asessId, String subjectId) {
		List<RankDto> rankDtos=new ArrayList<>();
		List<TeacherAsessRank> teacherAsessRanks=teacherasessRankDao.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
		if(CollectionUtils.isNotEmpty(teacherAsessRanks)){
			for (TeacherAsessRank teacherAsessRank : teacherAsessRanks) {
				for (int i =1; i < 3; i++) {//1得分，2排名
					String title="";
					RankDto rankDto=new RankDto();
					rankDto.setAsessRankId(teacherAsessRank.getId());
					rankDto.setLineType(i+"");
					if(i==1){
						title="线得分";
					}else{
						title="排名";
					}
					rankDto.setName(teacherAsessRank.getName()+title);
					rankDtos.add(rankDto);
				}
			}
			RankDto rankDto=new RankDto();
			rankDto.setAsessRankId(subjectId);
			rankDto.setLineType(1+"");//得分
			String subName="";
			if(StringUtils.equals(subjectId, BaseConstants.ZERO_GUID)){
				subName="总分";
			}else{
				Course course= SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
				subName=course.getSubjectName();
			}
			rankDto.setName(subName+"得分");
			rankDtos.add(rankDto);
			
			RankDto rankDto2=new RankDto();
			rankDto2.setAsessRankId(subjectId);
			rankDto2.setLineType(2+"");//排名
			rankDto2.setName(subName+"得分排名");
			rankDtos.add(rankDto2);
		}
		return rankDtos;
	}
	
	@Override
	protected BaseJpaRepositoryDao<TeacherAsessRank, String> getJpaDao() {
		return teacherasessRankDao;
	}

	@Override
	protected Class<TeacherAsessRank> getEntityClass() {
		return TeacherAsessRank.class;
	}

}
