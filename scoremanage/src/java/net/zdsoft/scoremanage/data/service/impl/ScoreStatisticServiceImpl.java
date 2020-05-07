package net.zdsoft.scoremanage.data.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ScoreStatisticDao;
import net.zdsoft.scoremanage.data.entity.ScoreStatistic;
import net.zdsoft.scoremanage.data.service.ScoreStatisticService;

@Service("scoreStatisticService")
public class ScoreStatisticServiceImpl extends BaseServiceImpl<ScoreStatistic, String> implements ScoreStatisticService{

	@Autowired
	private ScoreStatisticDao scoreStatisticDao;
	@Autowired
	private StudentRemoteService studentService;
	
	@Override
	protected BaseJpaRepositoryDao<ScoreStatistic, String> getJpaDao() {
		return scoreStatisticDao;
	}

	@Override
	protected Class<ScoreStatistic> getEntityClass() {
		return ScoreStatistic.class;
	}

	@Override
	public List<ScoreStatistic> findList(String examId, String classId, String subjectId, String type) {
		List<ScoreStatistic> findList = scoreStatisticDao.findList(examId,classId,subjectId,type);
		Set<String> studentIds = new HashSet<String>();
		for (ScoreStatistic item : findList) {
			studentIds.add(item.getStudentId());
		}
		List<Student> students = SUtils.dt(studentService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>(){});
		Map<String, Student> findByIdInMap = EntityUtils.getMap(students, "id");
		for (ScoreStatistic item : findList) {
			Student student = findByIdInMap.get(item.getStudentId());
			if(student!=null){
				item.setStudentName(student.getStudentName());
			}else{
				item.setStudentName("未找到");
			}
		}
		return findList;
	}

	@Override
	public ScoreStatistic findOne(final String unitId,final String classId, final String examId,final String subjectId,final Integer ranking,final String type,final String rankType) {
		ScoreStatistic sst = null;
		Specification<ScoreStatistic> specification = new Specification<ScoreStatistic>() {
            @Override
            public Predicate toPredicate(final Root<ScoreStatistic> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) {
                final List<Predicate> ps = Lists.newArrayList();
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                ps.add(cb.equal(root.get("subjectId").as(String.class), subjectId));
                ps.add(cb.equal(root.get("type").as(String.class), type));
                if("1".equals(rankType)){
                	ps.add(cb.equal(root.get("courseRanking").as(Integer.class), ranking));
                }else{
                	ps.add(cb.equal(root.get("allRanking").as(Integer.class), ranking));
                }
                if(ScoreDataConstants.STATISTIC_TYPE0.equals(type)){
                	ps.add(cb.equal(root.get("classId").as(String.class), classId));
                }else if(ScoreDataConstants.STATISTIC_TYPE1.equals(type)){
                	ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                }else if(ScoreDataConstants.STATISTIC_TYPE2.equals(type)){
                	
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }

        };
        sst = scoreStatisticDao.findOne(specification).orElse(null);
		return sst;
	}

	@Override
	public void deleteByExamId(String examId) {
		scoreStatisticDao.deleteByExamId(examId);
	}

	@Override
	public List<ScoreStatistic> saveAllEntitys(ScoreStatistic... scoreStatistic) {
		return scoreStatisticDao.saveAll(checkSave(scoreStatistic));
	}

}
