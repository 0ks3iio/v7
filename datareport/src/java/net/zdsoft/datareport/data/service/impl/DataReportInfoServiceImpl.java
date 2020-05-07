package net.zdsoft.datareport.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.AttachmentRemoteService;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.datareport.data.constants.ReportConstants;
import net.zdsoft.datareport.data.dao.DataReportInfoDao;
import net.zdsoft.datareport.data.dto.DataReportSaveDto;
import net.zdsoft.datareport.data.entity.*;
import net.zdsoft.datareport.data.service.*;
import net.zdsoft.datareport.data.task.ReportTask;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("dataReportInfoService")
public class DataReportInfoServiceImpl extends BaseServiceImpl<DataReportInfo,String> implements DataReportInfoService{

	@Autowired
	private DataReportInfoDao dataReportInfoDao;
	@Autowired
	private DataReportObjService dataReportObjService;
	@Autowired
	private DataReportColumnService dataReportColumnService;
	@Autowired
	private DataReportTitleService dataReportTitleService;
	@Autowired
	private DataReportTaskService dataReportTaskService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private ReportTaskService reportTaskService;
	@Autowired
	private DataReportResultsService dataReportResultsService;
	@Autowired
	private AttachmentRemoteService attachmentRemoteService;
	@Autowired
	private StorageDirRemoteService storageDirRemoteService;
	
	private Pattern pattern = Pattern.compile("<p.*?>(.*?)</p>");
	
	@Override
	public List<DataReportInfo> findInfoList(String title, String unitId, Pagination page) {
		List<DataReportInfo> dataReportInfos = Lists.newArrayList();
		Pageable pageable = Pagination.toPageable(page);
		Specification<DataReportInfo> specification = new Specification<DataReportInfo>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<DataReportInfo> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				List<Predicate> ps = Lists.newArrayList();
				if (StringUtils.isNotEmpty(title)) {
					ps.add(cb.like(root.get("title").as(String.class), "%" + title + "%"));
				}
				ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
				ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(root.<Date>get("state")));
				orderList.add(cb.desc(root.<Date>get("creationTime")));
				cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
				return cq.getRestriction();
			}
		};
		Page<DataReportInfo> findAll = dataReportInfoDao.findAll(specification, pageable);
		page.setMaxRowCount((int) findAll.getTotalElements());
		dataReportInfos = findAll.getContent();
		String[] ownerIds = dataReportInfos.stream().map(data -> data.getOwnerId()).toArray(String[]::new);
    	List<Teacher> teacherList = SUtils.dt(teacherRemoteService.findListByIds(ownerIds), new TR<List<Teacher>>() {});
		Map<String,String> teacherNameMap = EntityUtils.getMap(teacherList, tea -> tea.getId(), tea -> tea.getTeacherName());
    	for (DataReportInfo dataReportInfo : dataReportInfos) {
    		dataReportInfo.setOwnerName(teacherNameMap.get(dataReportInfo.getOwnerId()));
		}
    	return dataReportInfos;
	}
	
	@Override
	public void deleteOrRevokeInfo(String infoId, String unitId, Integer type) throws Exception {
		List<DataReportTask> tasks = dataReportTaskService.findByReportId(infoId, null);
		String[] taskIds = EntityUtils.getArray(tasks, DataReportTask::getId, String[]::new);
		if (Objects.equals(type, 1)) {
			dataReportInfoDao.deleteById(infoId);
			dataReportObjService.deleteByReportId(infoId);
			dataReportTitleService.deleteByReportId(infoId);
			dataReportColumnService.deleteByReportId(infoId);
		} else {
			dataReportInfoDao.updateState(ReportConstants.REPORT_INFO_STATE_1, infoId);
		}
		dataReportTaskService.deleteByReportId(infoId);
		dataReportResultsService.deleteByReportId(infoId);
		reportTaskService.deleteReportTask(infoId + "start");
		reportTaskService.deleteReportTask(infoId + "end");
		List<Attachment> attInfo = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(infoId),new TR<List<Attachment>>(){});
		List<Attachment> attTask = null;
		if (taskIds!=null&&taskIds.length>0) {
			attTask = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjId(taskIds),new TR<List<Attachment>>(){});
			if (CollectionUtils.isNotEmpty(attTask)) {
				attInfo.addAll(attTask);
			}
		}
		String[] attIds = EntityUtils.getArray(attInfo, Attachment::getId, String[]::new);
		if (attIds!=null&&attIds.length>0) {
			attachmentRemoteService.deleteAttachments(attIds, null);
		}
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		File pdfFile = null;
		for (String str : taskIds) {
			pdfFile = new File(dir.getDir()+File.separator+"datareport"+File.separator+unitId+File.separator+str+".pdf");
			if (pdfFile.exists()) {
				pdfFile.delete();
			}
		}
		pdfFile = new File(dir.getDir()+File.separator+"datareport"+File.separator+unitId+File.separator+infoId+".pdf");
		if (pdfFile.exists()) {
			pdfFile.delete();
		}
	}
	
	@Override
	public void updateState(Integer state, String reportId) {
		dataReportInfoDao.updateState(state,reportId);
	}
	
	@Override
	public DataReportInfo saveOneInfo(DataReportSaveDto saveDto,boolean isSub) {
		String reportId = "";
		DataReportInfo dataReportInfo = saveDto.getDataReportInfo();
		if (StringUtils.isNotEmpty(dataReportInfo.getId())) {
			reportId = dataReportInfo.getId();
			dataReportInfoDao.deleteById(reportId);
			dataReportObjService.deleteByReportId(reportId);
			dataReportColumnService.deleteByReportId(reportId);
			dataReportTitleService.deleteByReportId(reportId);
		} else {
			reportId = UuidUtils.generateUuid();
		}
		Date date = new Date();
		//保存填报表数据
		dataReportInfo.setId(reportId);
		if (isSub) {
			dataReportInfo.setState(ReportConstants.REPORT_INFO_STATE_2);
		}
		dataReportInfo.setIsDeleted(0);
		dataReportInfo.setCreationTime(date);
		dataReportInfo.setModifyTime(date);
		dataReportInfoDao.save(dataReportInfo);
		//保存发布对象数据
		List<DataReportObj> dataReportObjs = Lists.newArrayList();
		DataReportObj dataReportObj = null;
		String[] objIds = saveDto.getObjectIds().split(",");
		for (String objStr : objIds) {
			dataReportObj = new DataReportObj();
			dataReportObj.setId(UuidUtils.generateUuid());
			dataReportObj.setReportId(reportId);
			dataReportObj.setObjectId(objStr);
			dataReportObj.setObjectType(saveDto.getObjectType());
			dataReportObj.setCreationTime(date);
			dataReportObj.setModifyTime(date);
			dataReportObjs.add(dataReportObj);
		}
		dataReportObjService.saveAll(dataReportObjs.toArray(new DataReportObj[dataReportObjs.size()]));
		//保存表头数据
        String excalHeader = saveDto.getHeader().replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&quot;","\"");
		Matcher matcher = pattern.matcher(excalHeader);
		int orderIndex = 1;
		while(matcher.find()){
			if (StringUtils.isEmpty(matcher.group().replaceAll("</?[^>]+>", "").replaceAll("&nbsp; ", ""))) {
				continue;
			}
			DataReportTitle title = new DataReportTitle();
			title.setId(UuidUtils.generateUuid());
			title.setReportId(reportId);
			title.setCreationTime(date);
			title.setModifyTime(date);
			title.setContent(matcher.group());
			title.setType(ReportConstants.TITLE_TYPE_1);
			title.setOrderIndex(orderIndex);
			dataReportTitleService.save(title);
			orderIndex++;
		}
		//保存备注
		if (StringUtils.isNotEmpty(saveDto.getRemark())) {
			DataReportTitle remarkTitle = new DataReportTitle();
			remarkTitle.setId(UuidUtils.generateUuid());
			remarkTitle.setReportId(reportId);
			remarkTitle.setCreationTime(date);
			remarkTitle.setModifyTime(date);
			remarkTitle.setContent(saveDto.getRemark());
			remarkTitle.setType(ReportConstants.TITLE_TYPE_3);
			dataReportTitleService.save(remarkTitle);
		}
		//保存行或列数据
		int index = 1;
		List<DataReportColumn> rowColumns = Lists.newArrayList();
		List<DataReportColumn> rankColumns = Lists.newArrayList();
		int tableType = dataReportInfo.getTableType();
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_1, tableType) || Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			rowColumns = saveDto.getRowColumns().stream().filter(data -> StringUtils.isNotEmpty(data.getColumnName())).collect(Collectors.toList());
			for (DataReportColumn column : rowColumns) {
				column.setId(UuidUtils.generateUuid());
				column.setReportId(reportId);
				column.setType(ReportConstants.REPORT_COLUMN_TYPE_1);
				column.setColumnIndex(index);
				column.setCreationTime(date);
				column.setModifyTime(date);
				index++;
			}
			if (CollectionUtils.isNotEmpty(rowColumns)) {
				dataReportColumnService.saveAll(rowColumns.toArray(new DataReportColumn[rowColumns.size()]));
			}
		}
		if (Objects.equals(ReportConstants.REPORT_TABLE_TYPE_2, tableType) || Objects.equals(ReportConstants.REPORT_TABLE_TYPE_3, tableType)) {
			rankColumns = saveDto.getRankColumns().stream().filter(data -> StringUtils.isNotEmpty(data.getColumnName())).collect(Collectors.toList());
			index = 1;
			for (DataReportColumn column : rankColumns) {
				column.setId(UuidUtils.generateUuid());
				column.setReportId(reportId);
				column.setType(ReportConstants.REPORT_COLUMN_TYPE_2);
				column.setColumnIndex(index);
				column.setCreationTime(date);
				column.setModifyTime(date);
				index++;
			}
			if (CollectionUtils.isNotEmpty(rankColumns)) {
				dataReportColumnService.saveAll(rankColumns.toArray(new DataReportColumn[rankColumns.size()]));
			}
		}
		//开启发布定时任务
		if (isSub) {
			String startTime = dataReportInfo.getStartTime() + " 00:00:00";
			String endTime = dataReportInfo.getEndTime() + " 23:59:59";
			if(DateUtils.date2StringByMinute(new Date()).compareTo(endTime) < 0){
				Calendar calendarEnd = Calendar.getInstance();
				if (DateUtils.date2StringByMinute(new Date()).compareTo(startTime)>=0) {
					calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
					startTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
				}
				ReportTask reportTaskStart = new ReportTask(reportId,false);
				ReportTask reportTaskEnd = new ReportTask(reportId,true);
				reportTaskService.addReportTask(reportId,startTime,endTime,reportTaskStart,reportTaskEnd);
			}
		}
		return dataReportInfo;
	}
	
	@Override
	public void saveCopyInfo(String infoId) {
		String reportId = UuidUtils.generateUuid();
		Date date = new Date();
		DataReportInfo dataReportInfo = dataReportInfoDao.findOne(infoId);
		DataReportInfo infoCopy = new DataReportInfo();
		BeanUtils.copyProperties(dataReportInfo,infoCopy);
		infoCopy.setId(reportId);
		infoCopy.setTitle(infoCopy.getTitle() + "(复制)");
		infoCopy.setStartTime("");
		infoCopy.setEndTime("");
		infoCopy.setCreationTime(date);
		infoCopy.setModifyTime(date);
		infoCopy.setState(ReportConstants.REPORT_INFO_STATE_1);
		infoCopy.setIsDeleted(0);
		dataReportInfoDao.save(infoCopy);
		
		List<DataReportObj> dataReportObjs = dataReportObjService.findByReportIds(new String[]{infoId}, false);
		List<DataReportObj> objCopys = Lists.newArrayList();
		DataReportObj objCopy = null;
		for (DataReportObj obj : dataReportObjs) {
			objCopy = new DataReportObj();
			BeanUtils.copyProperties(obj,objCopy);
			objCopy.setId(UuidUtils.generateUuid());
			objCopy.setReportId(reportId);
			objCopy.setCreationTime(date);
			objCopy.setModifyTime(date);
			objCopys.add(objCopy);
		}
		dataReportObjService.saveAll(objCopys.toArray(new DataReportObj[objCopys.size()]));
		
		List<DataReportTitle> dataReportTitle = dataReportTitleService.findByReportId(infoId);
		List<DataReportTitle> titleCopys = Lists.newArrayList();
		DataReportTitle titleCopy = null;
		for (DataReportTitle title : dataReportTitle) {
			titleCopy = new DataReportTitle();
			BeanUtils.copyProperties(title,titleCopy);
			titleCopy.setId(UuidUtils.generateUuid());
			titleCopy.setReportId(reportId);
			titleCopy.setModifyTime(date);
			titleCopy.setCreationTime(date);
			titleCopys.add(titleCopy);
		}
		dataReportTitleService.saveAll(titleCopys.toArray(new DataReportTitle[titleCopys.size()]));
		
		List<DataReportColumn> columns1 = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_1);
		List<DataReportColumn> columns2 = dataReportColumnService.findByIdAndType(infoId, ReportConstants.REPORT_COLUMN_TYPE_2);
		if (CollectionUtils.isNotEmpty(columns2)) {
			columns1.addAll(columns2);
		}
		List<DataReportColumn> columnsCopys = Lists.newArrayList();
		DataReportColumn columnsCopy = null;
		for (DataReportColumn column : columns1) {
			columnsCopy = new DataReportColumn();
			BeanUtils.copyProperties(column,columnsCopy);
			columnsCopy.setId(UuidUtils.generateUuid());
			columnsCopy.setReportId(reportId);
			columnsCopy.setCreationTime(date);
			columnsCopy.setModifyTime(date);
			columnsCopys.add(columnsCopy);
		}
		dataReportColumnService.saveAll(columnsCopys.toArray(new DataReportColumn[columnsCopys.size()]));
	}
	
	@Override
	public void addTaskQueue() {
		String nowDate = DateUtils.date2StringByDay(new Date());
		List<DataReportInfo> showInfos = dataReportInfoDao.findShowInfos();
		List<DataReportInfo> updateInfos = Lists.newArrayList();
		for (DataReportInfo info : showInfos) {
			if (nowDate.compareTo(info.getEndTime())>0) {
				info.setState(ReportConstants.REPORT_INFO_STATE_4);
				updateInfos.add(info);
			}
		}
		if (CollectionUtils.isNotEmpty(updateInfos)) {
			saveAll(updateInfos.toArray(new DataReportInfo[updateInfos.size()]));
		}
		showInfos.removeAll(updateInfos);
		String startTime = "";
		String endTime = "";
		Calendar calendarEnd = null;
		if (CollectionUtils.isNotEmpty(showInfos)) {
			for (DataReportInfo info : showInfos) {
				startTime = info.getStartTime() + " 00:00:00";
				endTime = info.getEndTime() + " 23:59:59";
				calendarEnd = Calendar.getInstance();
				if (DateUtils.date2StringByMinute(new Date()).compareTo(startTime)<0) {
					ReportTask reportTaskStart = new ReportTask(info.getId(),false);
					ReportTask reportTaskEnd = new ReportTask(info.getId(),true);
					reportTaskService.addReportTask(info.getId(),startTime,endTime,reportTaskStart,reportTaskEnd);
				} else {
					if (ReportConstants.REPORT_INFO_STATE_2.equals(info.getState())) {
						calendarEnd.add(Calendar.SECOND, 5);//开始时间在当前时间之前，5秒后显示
						startTime = DateUtils.date2StringBySecond(calendarEnd.getTime());
						ReportTask reportTaskStart = new ReportTask(info.getId(),false);
						ReportTask reportTaskEnd = new ReportTask(info.getId(),true);
						reportTaskService.addReportTask(info.getId(),startTime,endTime,reportTaskStart,reportTaskEnd);
					} else {
						reportTaskService.addReportTask(info.getId(),"",endTime,null,new ReportTask(info.getId(),true));
					}
				}
			}
		}
	}
	
	@Override
	public DataReportInfo findSameTitle(String infoTitle, String unitId) {
		return dataReportInfoDao.findSameTitle(infoTitle,unitId);
	}
	
	
	@Override
	public void updateTime(String infoId, String endTime, Integer state, String unitId) throws Exception {
		if (!ReportConstants.REPORT_INFO_STATE_4.equals(state)) {
			dataReportInfoDao.updateTime(endTime,state,infoId);
			reportTaskService.deleteReportTask(infoId + "end");
		} else {
			dataReportInfoDao.updateTime(endTime,ReportConstants.REPORT_INFO_STATE_3,infoId);
		}
		StorageDir dir=SUtils.dc(storageDirRemoteService.findOneById(BaseConstants.ZERO_GUID), StorageDir.class);
		File pdfFile = new File(dir.getDir()+File.separator+"datareport"+File.separator+unitId+File.separator+infoId+".pdf");
		if (pdfFile.exists()) {
			pdfFile.delete();
		}
		List<Attachment> attInfo = SUtils.dt(attachmentRemoteService.findAttachmentDirPathByObjTypeAndId(ReportConstants.REPORT_INFO_STATS, infoId),new TR<List<Attachment>>(){});
		if (CollectionUtils.isNotEmpty(attInfo)) {
			attachmentRemoteService.deleteAttachments(new String[]{attInfo.get(0).getId()}, null);
		}
		endTime = endTime + " 23:59:59";
		reportTaskService.addReportTask(infoId,"",endTime,null,new ReportTask(infoId,true));
	}
	
	@Override
	protected BaseJpaRepositoryDao<DataReportInfo, String> getJpaDao() {
		return dataReportInfoDao;
	}

	@Override
	protected Class<DataReportInfo> getEntityClass() {
		return DataReportInfo.class;
	}
}
