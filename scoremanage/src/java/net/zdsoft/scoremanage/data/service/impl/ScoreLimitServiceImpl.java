package net.zdsoft.scoremanage.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.scoremanage.data.dao.ScoreLimitDao;
import net.zdsoft.scoremanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.scoremanage.data.entity.ScoreLimit;
import net.zdsoft.scoremanage.data.service.ClassInfoService;
import net.zdsoft.scoremanage.data.service.ExamInfoService;
import net.zdsoft.scoremanage.data.service.ScoreLimitService;
import net.zdsoft.scoremanage.data.service.SubjectInfoService;

@Service("scoreLimitService")
public class ScoreLimitServiceImpl extends BaseServiceImpl<ScoreLimit, String> implements ScoreLimitService{

	@Autowired
	private ScoreLimitDao scoreLimitDao;
	@Override
	protected BaseJpaRepositoryDao<ScoreLimit, String> getJpaDao() {
		return scoreLimitDao;
	}

	@Override
	protected Class<ScoreLimit> getEntityClass() {
		return ScoreLimit.class;
	}

	@Override
	public List<ScoreLimit> saveAllEntitys(ScoreLimit... scoreLimit) {
		return scoreLimitDao.saveAll(checkSave(scoreLimit));
	}

	@Override
	public List<ScoreLimit> findBySearchDto(ScoreLimitSearchDto searchDto) {
		
		Specification<ScoreLimit> specification = new Specification<ScoreLimit>(){
			@Override
			public Predicate toPredicate(Root<ScoreLimit> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = new ArrayList<Predicate>();
				ps.add(cb.equal(root.get("unitId").as(String.class), searchDto.getUnitId()));
				ps.add(cb.equal(root.get("acadyear").as(String.class), searchDto.getAcadyear()));
				ps.add(cb.equal(root.get("semester").as(String.class), searchDto.getSemester()));
				if(StringUtils.isNotBlank(searchDto.getExamId())){
					ps.add(cb.equal(root.get("examInfoId").as(String.class), searchDto.getExamId()));
				}
				if(StringUtils.isNotBlank(searchDto.getSubjectId())){
					ps.add(cb.equal(root.get("subjectId").as(String.class), searchDto.getSubjectId()));
				}else{
					if(ArrayUtils.isNotEmpty(searchDto.getSubjectIds())){
						ps.add(root.get("subjectId").in(searchDto.getSubjectIds()));
					}
				}
				if(StringUtils.isNotBlank(searchDto.getTeacherId())){
					ps.add(cb.equal(root.get("teacherId").as(String.class), searchDto.getTeacherId()));
				}
				if(ArrayUtils.isNotEmpty(searchDto.getClassIds())){
					ps.add(root.get("classId").in(searchDto.getClassIds()));
				}
				cq.where(ps.toArray(new Predicate[ps.size()]));
				return cq.getRestriction();
			}			
		};
		List<ScoreLimit> scoreLimitList = findAll(specification);
		return scoreLimitList;
	}

	@Override
	public int deleteByClassIdAndTeacherId(ScoreLimitSearchDto dto) {
		return scoreLimitDao.deleteBySearch(dto.getAcadyear(),dto.getSemester(),dto.getExamId(),dto.getUnitId(),dto.getSubjectId(),dto.getClassId(),dto.getTeacherId());
	}

	@Override
	public Map<String, Set<String>> findByTeacherId(
			ScoreLimitSearchDto searchDto) {
		List<ScoreLimit> list = findBySearchDto(searchDto);
		Map<String, Set<String>> returnMap=new HashMap<String, Set<String>>();
		if(CollectionUtils.isNotEmpty(list)){
			for(ScoreLimit limit:list){
				if(!returnMap.containsKey(limit.getSubjectId())){
					returnMap.put(limit.getSubjectId(), new HashSet<String>());
				}
				returnMap.get(limit.getSubjectId()).add(limit.getTeacherId());
			}
		}
		return returnMap;
	}

	@Override
	public List<String> findTeacherIdByUnitId(String unitId,String type) {
		if(BaseConstants.SUBJECT_TYPE_BX.equals(type)){
			return scoreLimitDao.findTeacherIdByUnitIdAndExamInfoIdNot(unitId,Constant.GUID_ZERO);
		}else if(BaseConstants.SUBJECT_TYPE_XX.equals(type)){
			return scoreLimitDao.findTeacherIdByUnitIdAndExamInfoId(unitId,Constant.GUID_ZERO);
		}
		return null;
	}

}
