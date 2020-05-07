package net.zdsoft.comprehensive.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.comprehensive.constant.CompreStatisticsConstants;
import net.zdsoft.comprehensive.dao.CompreStatisticsDao;
import net.zdsoft.comprehensive.entity.CompreStatistics;
import net.zdsoft.comprehensive.service.CompreStatisticsService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("compreStatisticsService")
public class CompreStatisticsServiceImpl extends BaseServiceImpl<CompreStatistics, String>
	implements CompreStatisticsService{
	
	@Autowired
	private CompreStatisticsDao compreStatisticsDao;

	@Override
	protected BaseJpaRepositoryDao<CompreStatistics, String> getJpaDao() {
		
		return compreStatisticsDao;
	}

	@Override
	protected Class<CompreStatistics> getEntityClass() {
		return CompreStatistics.class;
	}

	@Override
	public void saveAllStatistics(List<CompreStatistics> insertList) {
		if(CollectionUtils.isEmpty(insertList)){
			return;
		}
		compreStatisticsDao.saveAll(this.checkSave(insertList.toArray(new CompreStatistics[]{})));
	}

	@Override
	public List<CompreStatistics> findByTimeStudentId(String unitId,
			String acadyear, String semester, String type, String[] studentId) {
		if(studentId==null || studentId.length<=0){
			return new ArrayList<CompreStatistics>();
		}
		int length = studentId.length;
		if(length<=1000){
			if(StringUtils.isNotBlank(type)){
				return compreStatisticsDao.findByStudentIdType(unitId,acadyear,semester,type,studentId);
			}else{
				return compreStatisticsDao.findByStudentId(unitId,acadyear,semester,studentId);
			}
		}else{
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			List<CompreStatistics> returnList = new ArrayList<CompreStatistics>();
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				List<CompreStatistics> l=new ArrayList<CompreStatistics>();
				String[] stuId = ArrayUtils.subarray(studentId, i * 1000, max);
				if(StringUtils.isNotBlank(type)){
					l= compreStatisticsDao.findByStudentIdType(unitId,acadyear,semester,type,stuId);
				}else{
					l= compreStatisticsDao.findByStudentId(unitId,acadyear,semester,stuId);
				}
				returnList.addAll(l);
			}
			return returnList;
		}
	}

	@Override
	public void saveAll(String unitId, String acadyear, String semester,
			String[] delStuIds,String[] delType, List<CompreStatistics> insertList) {
		deleteByStudentIds(unitId, acadyear, semester, delType,delStuIds);
		if(CollectionUtils.isNotEmpty(insertList)){
			saveAllStatistics(insertList);
		}
		
	}

	@Override
	public void deleteByStudentIds(String unitId, String acadyear,
			String semester,String[] delType, String[] studentId) {
		if(delType==null) {
			delType=new String[] {CompreStatisticsConstants.TYPE_OVERALL,CompreStatisticsConstants.TYPE_ENGLISH,
					CompreStatisticsConstants.TYPE_ENG_SPEAK,CompreStatisticsConstants.TYPE_GYM};
		}
		if(studentId!=null && studentId.length>0){
			int length = studentId.length;
			if(length<=1000){  
				compreStatisticsDao.deleteByStudentId(unitId,acadyear,semester,delType,studentId);
			}else{
				int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
				for (int i = 0; i < cyc; i++) {
					int max = (i + 1) * 1000;
					if (max > length)
						max = length;
					String[] stuId = ArrayUtils.subarray(studentId, i * 1000, max);
					compreStatisticsDao.deleteByStudentId(unitId,acadyear,semester,delType,stuId);
				}
			}
		}
		
	}

	@Override
	public List<CompreStatistics> findByStudentIdAcadyear(String studentId,
			String[] acadyear) {
		return compreStatisticsDao.findByStudentIdAcadyear(studentId, acadyear);
	}

	@Override
	public List<CompreStatistics> findByUnitIdAcadyear(String unitId,
			String[] acadyear) {
		return compreStatisticsDao.findByUnitIdAcadyear(unitId, acadyear);
	}

	@Override
	public List<CompreStatistics> findByStudentIdsAcadyear(String[] studentIds, String[] acadyear) {
		if(ArrayUtils.isEmpty(studentIds) || ArrayUtils.isEmpty(acadyear)){
			return new ArrayList<>();
		}
		return compreStatisticsDao.findByStudentIdsAcadyear(studentIds, acadyear);
	}
	@Override
	public List<CompreStatistics> findByStudentIds(String[] studentIds){
		if(ArrayUtils.isEmpty(studentIds)){
			return new ArrayList<>();
		}
		return compreStatisticsDao.findByStudentIds(studentIds);
	}

	@Override
	public List<CompreStatistics> findByAcaSemStudentIds(String acadyear, String semester, String[] studentIds) {
		if(studentIds==null || studentIds.length<=0){
			return new ArrayList<CompreStatistics>();
		}
		int length = studentIds.length;
		if(length<=1000){
			return compreStatisticsDao.findByAcadyearAndSemesterAndStudentIdIn(acadyear, semester, studentIds);
		}else{
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			List<CompreStatistics> returnList = new ArrayList<CompreStatistics>();
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				List<CompreStatistics> l=new ArrayList<CompreStatistics>();
				String[] stuIds = ArrayUtils.subarray(studentIds, i * 1000, max);
				l= compreStatisticsDao.findByAcadyearAndSemesterAndStudentIdIn(acadyear, semester, stuIds);
				returnList.addAll(l);
			}
			return returnList;
		}
	}

	@Override
	public List<CompreStatistics> findByTypeStudentIdsAcadyear(String type, String[] studentIds, String[] acadyear) {
		if(ArrayUtils.isEmpty(studentIds) || ArrayUtils.isEmpty(acadyear)){
			return new ArrayList<CompreStatistics>();
		}
		int length = studentIds.length;
		if(length<=1000){
			return compreStatisticsDao.findByTypeAndAcadyearInAndStudentIdIn(type, acadyear, studentIds);
		}else{
			int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
			List<CompreStatistics> returnList = new ArrayList<CompreStatistics>();
			for (int i = 0; i < cyc; i++) {
				int max = (i + 1) * 1000;
				if (max > length)
					max = length;
				List<CompreStatistics> l=new ArrayList<CompreStatistics>();
				String[] stuIds = ArrayUtils.subarray(studentIds, i * 1000, max);
				l= compreStatisticsDao.findByTypeAndAcadyearInAndStudentIdIn(type, acadyear, stuIds);
				returnList.addAll(l);
			}
			return returnList;
		}
	}

}
