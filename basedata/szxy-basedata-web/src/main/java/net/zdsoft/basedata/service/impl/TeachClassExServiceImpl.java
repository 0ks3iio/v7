package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.TeachClassExDao;
import net.zdsoft.basedata.entity.TeachClassEx;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.service.TeachClassExService;
import net.zdsoft.basedata.service.TeachPlaceService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teachClassExService")
public class TeachClassExServiceImpl extends BaseServiceImpl<TeachClassEx, String>
	implements TeachClassExService{
	
	@Autowired
	private TeachClassExDao  teachClassExDao;
	@Autowired
	private TeachPlaceService teachPlaceService;
	@Override
	public List<TeachClassEx> findByTeachClassIdIn(String[] teachClassIds,
			boolean isMake) {
		if(ArrayUtils.isEmpty(teachClassIds)){
			return new ArrayList<TeachClassEx>();
		}
		int length = teachClassIds.length;
		List<TeachClassEx> list = new ArrayList<TeachClassEx>();
		if(length<=1000){
			list=teachClassExDao.findByClassId(teachClassIds);
		}else{
			int cyc = length/1000+(length%1000 >0?1:0);;
			for(int i=0;i<cyc;i++){
				int max=1000*(i+1);
				if(max>length){
					max=length;
				}
				String[] tId = ArrayUtils.subarray(teachClassIds, 1000*i, max);
				List<TeachClassEx> list1 = teachClassExDao.findByClassId(tId);
				if(CollectionUtils.isNotEmpty(list1)){
					list.addAll(list1);
				}
			}
		}
		if(CollectionUtils.isEmpty(list)){
			return list;
		}
		if(isMake){
			Map<String, String> periodIntegervalMap = BaseConstants.PERIOD_INTERVAL_Map;
			Map<String, String> dayOfWeekMap = BaseConstants.dayOfWeekMap;
			
			Set<String> placeIds = EntityUtils.getSet(list, "placeId");
			Map<String, TeachPlace> pMap=new HashMap<String, TeachPlace>();
			if(CollectionUtils.isNotEmpty(placeIds)){
				pMap = teachPlaceService.findMapByIdIn(placeIds.toArray(new String[]{}));
			}
			for(TeachClassEx ex:list){
				String timeTr="";
				timeTr=timeTr+dayOfWeekMap.get(String.valueOf(ex.getDayOfWeek()));
				timeTr=timeTr+periodIntegervalMap.get(ex.getPeriodInterval());
				timeTr=timeTr+"第"+ex.getPeriod()+"节";
				ex.setTimeStr(timeTr);
				if(pMap.containsKey(ex.getPlaceId())){
					ex.setPlaceName(pMap.get(ex.getPlaceId()).getPlaceName());
				}
			}
		}
		return list;
	}

	@Override
	protected BaseJpaRepositoryDao<TeachClassEx, String> getJpaDao() {
		return teachClassExDao;
	}

	@Override
	protected Class<TeachClassEx> getEntityClass() {
		return TeachClassEx.class;
	}

	@Override
	public void deleteByTeachClass(String[] teachClassId) {
		if(ArrayUtils.isNotEmpty(teachClassId)){
			if(teachClassId.length<=1000){
				teachClassExDao.deleteByTeachClassId(teachClassId);
			}else{
				int length=teachClassId.length;
				int cyc = length/1000+(length%1000 >0?1:0);;
				for(int i=0;i<cyc;i++){
					int max=1000*(i+1);
					if(max>length){
						max=length;
					}
					String[] tId = ArrayUtils.subarray(teachClassId, 1000*i, max);
					teachClassExDao.deleteByTeachClassId(tId);
				}
			}
			
		}
		
	}

	@Override
	public List<TeachClassEx> findByTeachClassId(String teachClassId) {
		return teachClassExDao.findByClassId(new String[]{teachClassId});
	}

	@Override
	public void deleteByGradeIds(String... gradeIds) {
		teachClassExDao.deleteByGradeIds(gradeIds);
	}

	@Override
	public void deleteBySubjectIds(String... subjectIds) {
		teachClassExDao.deleteBySubjectIds(subjectIds);
	}

}
