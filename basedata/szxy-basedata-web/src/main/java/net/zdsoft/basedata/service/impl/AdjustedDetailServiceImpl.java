package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zdsoft.basedata.dao.AdjustedDetailDao;
import net.zdsoft.basedata.dao.AdjustedDetailJdbcDao;
import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.basedata.service.AdjustedDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("adjustedDetailService")
public class AdjustedDetailServiceImpl extends BaseServiceImpl<AdjustedDetail, String> implements AdjustedDetailService {
	
	@Autowired
	private AdjustedDetailDao adjustedDetailDao;
	@Autowired
	private AdjustedDetailJdbcDao adjustedDetailJdbcDao;

	@Override
	protected BaseJpaRepositoryDao<AdjustedDetail, String> getJpaDao() {
		return adjustedDetailDao;
	}
	
	@Override
	protected Class<AdjustedDetail> getEntityClass() {
		return AdjustedDetail.class;
	}

	@Override
	public List<AdjustedDetail> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String[] teacherIds) {
		List<AdjustedDetail> detailList = adjustedDetailJdbcDao.findByCondition(schoolId, acadyear, semester, dates, teacherIds);
		if(detailList==null){
			return new ArrayList<AdjustedDetail>();
		}
		return detailList;
	}

    @Override
    public List<AdjustedDetail> findListByAdjustedIds(String[] adjustedIds) {
    	if(adjustedIds==null || adjustedIds.length==0) {
    		return new ArrayList<AdjustedDetail>();
    	}
        return adjustedDetailDao.findListByAdjustedIds(adjustedIds);
    }

	@Override
	public Map<String, String> findMapByAdjustedIdIn(String[] adjustedIds) {
		Map<String, String> map = adjustedDetailJdbcDao.findMapByAdjustedIdIn(adjustedIds);
		if(map==null){
			return new HashMap<String, String>();
		}
		return map;
	}

    @Override
    public Map<String, String> findCourseScheduleIdMapByAdjustedIdIn(String[] adjustedIds) {
        Map<String, String> map = adjustedDetailJdbcDao.findCourseScheduleIdMapByAdjustedIdIn(adjustedIds);
        if(map==null){
            return new HashMap<String, String>();
        }
        return map;
    }

    @Override
    public void deleteByAdjustedIds(String[] adjustedIds) {
    	if(adjustedIds == null || adjustedIds.length ==0)
    		return;
        adjustedDetailDao.deleteByAdjustedIds(adjustedIds);
    }
    
	@Override
	public void deleteByClassIds(String... classIds) {
		adjustedDetailDao.deleteByClassIdIn(classIds);
	}

	@Override
	public void deleteByTeacherIds(String... teacherIds) {
		adjustedDetailDao.deleteByTeacherIdIn(teacherIds);
	}
}
