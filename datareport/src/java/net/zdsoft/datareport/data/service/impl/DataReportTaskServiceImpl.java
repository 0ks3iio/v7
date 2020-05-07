package net.zdsoft.datareport.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dao.DataReportTaskDao;
import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.datareport.data.entity.DataReportObj;
import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.datareport.data.service.DataReportInfoService;
import net.zdsoft.datareport.data.service.DataReportObjService;
import net.zdsoft.datareport.data.service.DataReportTaskService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;

@Service("dataReportTaskService")
public class DataReportTaskServiceImpl extends BaseServiceImpl<DataReportTask,String> implements DataReportTaskService{

	@Autowired
	private DataReportTaskDao dataReportTaskDao;
	@Autowired
	private DataReportObjService dataReportObjService;
	@Autowired
	private DataReportInfoService dataReportInfoService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	
	@Override
	public List<DataReportTask> findTaskInfoList(String objectId, Integer state, Pagination page) {
		List<DataReportObj> dataReportObjs = dataReportObjService.findByObjectId(objectId);
		List<DataReportTask> dataReportTasks = Lists.newArrayList();
		if (CollectionUtils.isEmpty(dataReportObjs)) {
			return dataReportTasks;
		}
		Set<String> objIds = EntityUtils.getSet(dataReportObjs, obj->obj.getId());
		Pageable pageable = Pagination.toPageable(page);
		Specification<DataReportTask> specification = new Specification<DataReportTask>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<DataReportTask> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				In<String> in = cb.in(root.get("objId").as(String.class));
				for (String id : objIds) {
					in.value(id);
				}
				ps.add(in);
				if (!Objects.equals(0, state)) {
					ps.add(cb.equal(root.get("state").as(Integer.class), state));
				}
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				List<Order> orderList = new ArrayList<Order>();
				if (Objects.equals(0, state)) {
					orderList.add(cb.asc(root.<Integer>get("state")));
				}
				orderList.add(cb.desc(root.<Date>get("creationTime")));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		Page<DataReportTask> findAll = dataReportTaskDao.findAll(specification, pageable);
		page.setMaxRowCount((int) findAll.getTotalElements());
		dataReportTasks = findAll.getContent();
		Set<String> infoIds = EntityUtils.getSet(dataReportTasks, data -> data.getReportId());
		List<DataReportInfo> dataReportInfos = dataReportInfoService.findListByIds(infoIds.toArray(new String[infoIds.size()]));
		Set<String> unitIds = EntityUtils.getSet(dataReportInfos,info -> info.getUnitId());
		List<Unit> unitList = SUtils.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[unitIds.size()])),new TypeReference<List<Unit>>() {});
		Map<String,String> unitNameMap = EntityUtils.getMap(unitList,Unit::getId,Unit::getUnitName);
		Map<String,DataReportInfo> infoMap = EntityUtils.getMap(dataReportInfos, data -> data.getId());
		DataReportInfo info = null;
		for (DataReportTask task :dataReportTasks) {
			info = infoMap.get(task.getReportId());
			task.setTitle(info.getTitle());
			task.setUnitName(unitNameMap.get(info.getUnitId()));
			task.setStartTime(info.getStartTime());
			task.setEndTime(info.getEndTime());
		}
		return dataReportTasks;
	}
	
	@Override
	public DataReportTask findByTaskInfo(String taskId) {
		DataReportTask dataReportTask = dataReportTaskDao.findOneById(taskId);
		DataReportInfo dataReportInfo = dataReportInfoService.findOne(dataReportTask.getReportId());
		Unit unit = SUtils.dc(unitRemoteService.findOneById(dataReportInfo.getUnitId()),Unit.class);
		dataReportTask.setTitle(dataReportInfo.getTitle());
		dataReportTask.setUnitName(unit.getUnitName());
		return dataReportTask;
	}
	
	@Override
	public void updateState(Integer state,String taskId) {
		dataReportTaskDao.updateState(state,taskId);
	}
	
	@Override
	public List<DataReportTask> findByReportId(String infoId,Pagination page) {
		List<DataReportTask> dataReportTasks = null;
		Specification<DataReportTask> specification = new Specification<DataReportTask>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<DataReportTask> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				ps.add(cb.equal(root.get("reportId").as(String.class), infoId));
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(root.<Date>get("state")));
				orderList.add(cb.desc(root.<Date>get("creationTime")));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		if (page != null) {
			Pageable pageable = Pagination.toPageable(page);
			Page<DataReportTask> findAll = dataReportTaskDao.findAll(specification, pageable);
			page.setMaxRowCount((int) findAll.getTotalElements());
			dataReportTasks = findAll.getContent();
			DataReportInfo dataReportInfo = dataReportInfoService.findOne(infoId);
			List<DataReportObj> dataReportObjs =  dataReportObjService.findByReportIds(new String[]{infoId}, true);
			Map<String,String> objNameMap = EntityUtils.getMap(dataReportObjs, data -> data.getId(), data -> data.getObjectName());
			for (DataReportTask task : dataReportTasks) {
				task.setObjName(objNameMap.get(task.getObjId()));
				task.setStartTime(dataReportInfo.getStartTime());
				task.setEndTime(dataReportInfo.getEndTime());
			}
		} else {
			dataReportTasks = dataReportTaskDao.findAll(specification);
		}
		return dataReportTasks;
	}
	
	@Override
	public void reportTaskRun(String reportId, Boolean isEnd) {
		if (isEnd) {
			dataReportInfoService.updateState(ReportConstants.REPORT_INFO_STATE_4,reportId);
		} else {
			dataReportInfoService.updateState(ReportConstants.REPORT_INFO_STATE_3,reportId);
			List<DataReportObj> dataReportObjs = dataReportObjService.findByReportIds(new String[]{reportId}, false);
			List<DataReportTask> dataReportTasks = Lists.newArrayList();
			DataReportTask dataReportTask = null;
			for (DataReportObj obj : dataReportObjs) {
				dataReportTask = new DataReportTask();
				dataReportTask.setId(UuidUtils.generateUuid());
				dataReportTask.setReportId(reportId);
				dataReportTask.setObjId(obj.getId());
				dataReportTask.setState(ReportConstants.REPORT_TASK_STATE_1);
				dataReportTask.setIsDeleted(0);
				dataReportTask.setCreationTime(new Date());
				dataReportTask.setModifyTime(new Date());
				dataReportTasks.add(dataReportTask);
			}
			dataReportTaskDao.saveAll(dataReportTasks);
		}
	}
	
	@Override
	public void deleteByReportId(String infoId) {
		dataReportTaskDao.deleteByReportId(infoId);
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportTask, String> getJpaDao() {
		return dataReportTaskDao;
	}

	@Override
	protected Class<DataReportTask> getEntityClass() {
		return DataReportTask.class;
	}

}
