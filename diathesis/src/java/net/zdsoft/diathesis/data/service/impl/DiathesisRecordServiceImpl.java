package net.zdsoft.diathesis.data.service.impl;

import net.zdsoft.basedata.remote.service.FilePathRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dao.DiathesisRecordDao;
import net.zdsoft.diathesis.data.dao.DiathesisRecordJdbcDao;
import net.zdsoft.diathesis.data.dto.FileDto;
import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.diathesis.data.entity.DiathesisRecordInfo;
import net.zdsoft.diathesis.data.service.DiathesisRecordInfoService;
import net.zdsoft.diathesis.data.service.DiathesisRecordMessageService;
import net.zdsoft.diathesis.data.service.DiathesisRecordService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: panlf
 * @Date: 2019/3/29 9:44
 */
@Service("diathesisRecordService")
public class DiathesisRecordServiceImpl extends BaseServiceImpl<DiathesisRecord, String>  implements DiathesisRecordService {
	private Logger log = Logger.getLogger(DiathesisRecordServiceImpl.class);

	@Autowired
	private DiathesisRecordDao diathesisRecordDao;
	@Autowired
	private DiathesisRecordJdbcDao diathesisRecordJdbcDao;
	@Autowired
	private DiathesisRecordInfoService diathesisRecordInfoService;
	@Autowired
	private FilePathRemoteService filePathRemoteService;

	@Autowired
	private DiathesisRecordMessageService diathesisRecordMessageService;
	@Override
	protected BaseJpaRepositoryDao<DiathesisRecord, String> getJpaDao() {
		return diathesisRecordDao;
	}

	@Override
	protected Class<DiathesisRecord> getEntityClass() {
		return DiathesisRecord.class;
	}

	@Override
	public Map<String, Integer> findMapByProjectIdIn(String unitId, String[] projectIds) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Object[]> list = diathesisRecordDao.findStatusMapByProjectIdIn(unitId, projectIds);
		if(CollectionUtils.isNotEmpty(list)){
			for (Object[] objects : list) {
				map.put((String)objects[0], (Integer)objects[1]);
			}
		}
		return map;
	}

	@Override
	public List<DiathesisRecord> findListByProjectId(String unitId, String projectId, String acadyear, String semester, String classId, String[] classIds, String studentId, String[] status, Pagination page) {
		return diathesisRecordJdbcDao.findListByCondition(unitId, projectId, acadyear, semester,classId, classIds, studentId, status, page);
	}

	@Override
	public void deleteAndSave(String unitId, String recordId, List<DiathesisRecord> saveRecordList, List<DiathesisRecordInfo> saveInfoList) {
		if(StringUtils.isNotBlank(recordId)){
			diathesisRecordInfoService.deleteByRecordIds(unitId, new String[]{recordId});
		}
		if(CollectionUtils.isNotEmpty(saveRecordList)){
			saveAll(saveRecordList.toArray(new DiathesisRecord[saveRecordList.size()]));
		}
		if(CollectionUtils.isNotEmpty(saveInfoList)){
			for (DiathesisRecordInfo recordInfo : saveInfoList) {
				if(DiathesisConstant.DATA_TYPE_4.equals(recordInfo.getDataType())){
					try {

						//UuidUtils.generateUuid()
						String fileSystemPath = filePathRemoteService.getFilePath();
						String filePath = DiathesisConstant.FILEPATH + File.separator  + unitId
								+ File.separator + recordInfo.getStuCode();
						String dirPath = fileSystemPath + File.separator + filePath;
						File dirFile = new File(dirPath);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}
						// diaPath=前缀+/diathesis/unitId/stuCode/
						//之前的文件名是    前缀+/diathesis/unitId/stuCode/structureId.xxx
						//如果改成多附件上传 ,需要修改成for循环
						List<FileDto> fileList = recordInfo.getFileList();
						List<String> contextTxtList=new ArrayList<>();
						if(fileList!=null && fileList.size()>0){
							if(fileList.size()>10){
								throw new RuntimeException("一次性上传附件最多10个");
							}
							for (FileDto fileDto : fileList) {
								String uuid = UuidUtils.generateUuid();
								String suffix = StringUtils.substringAfterLast(fileDto.getFilePath() ,".");

								if (StringUtils.isBlank(fileDto.getFilePath())){
									throw new RuntimeException("附件上传的filePaht不能为空!");
								}
								if(StringUtils.startsWith(fileDto.getFilePath(),DiathesisConstant.SYSTEM_NAME)){
									contextTxtList.add(fileDto.getFileName()+","+fileDto.getFilePath());
								}else{
									File webFile = new File(fileDto.getFilePath());
									File newFile = new File(dirPath + File.separator + recordInfo.getId() + "_" + uuid + "." + suffix);
									if(webFile !=null && webFile.exists()){
										FileUtils.copyFile(webFile,newFile);
										if(StringUtils.isNotBlank(fileDto.getFileName())){
											contextTxtList.add(fileDto.getFileName()+","+filePath+File.separator+recordInfo.getId()+"_"+uuid+"."+suffix);
										}else{
											contextTxtList.add(webFile.getName()+","+filePath+File.separator+recordInfo.getId()+"_"+uuid+"."+suffix);
										}
									}
								}
							}
							//多文件间隔符号
							recordInfo.setContentTxt(StringUtils.join(contextTxtList,DiathesisConstant.FILE_SPLIT));
						}else {
							recordInfo.setContentTxt("");
						}

						//不知道 recordInfo.getResultTxt(); 有什么用?

//						String suffix = StringUtils.substringAfterLast(recordInfo.getContentTxt() ,".");
//						File orFile = new File(recordInfo.getContentTxt());
//						File desFile = new File( dirPath +File.separator + recordInfo.getId() +"."+ suffix);
//						if(orFile != null && orFile.exists()){
//							FileUtils.copyFile(orFile, desFile);
//							//FileUtils.deleteQuietly(orFile);
//						}
//						recordInfo.setContentTxt(orFile.getName()+","+filePath +File.separator + recordInfo.getId() +"."+ suffix);
//						if(StringUtils.isBlank(recordInfo.getResultTxt())){
//							recordInfo.setContentTxt(orFile.getName()+","+filePath +File.separator + recordInfo.getId() +"."+ suffix);
//						}else{
//							recordInfo.setContentTxt(recordInfo.getResultTxt()+","+filePath +File.separator + recordInfo.getId() +"."+ suffix);
//						}
					} catch (IOException e) {
						log.error("附件保存失败："+e.getMessage(), e);
					}
				}
			}
			diathesisRecordInfoService.saveAll(saveInfoList.toArray(new DiathesisRecordInfo[saveInfoList.size()]));
		}

	}

	@Override
	public List<DiathesisRecord> findListByStuId(String unitId, String stuId, String status) {
		return diathesisRecordDao.findListByStuId(unitId,stuId,status);
	}

	@Override
	public List<DiathesisRecord> findListByGradeCodeAndStuId(String unitId, String stuId, String gradeCode,
															 String status) {
		return diathesisRecordDao.findListByGradeCodeAndStuId(unitId,stuId,gradeCode,status);

	}

	@Override
	public List<String> findStuIdByProjectIdInAndUnitId(List<String> projectIds, String unitId) {
		if(CollectionUtils.isEmpty(projectIds))return new ArrayList<>();
		return diathesisRecordDao.findStuIdByProjectIdInAndUnitId(unitId,projectIds);
	}

	@Override
	public List<DiathesisRecord> findListByUnitIdAndStuIdInAndProjectIdIn(String unitId, String acadyear, Integer semester, List<String> stuIds, List<String> proIds) {
		List<DiathesisRecord> records=new ArrayList<>();
		if(CollectionUtils.isEmpty(stuIds))return records;
		for (int i = 0; i < stuIds.size(); i+=900) {
			List<DiathesisRecord> temp = diathesisRecordDao.findListByUnitIdAndStuIdInAndProjectIdIn(unitId,acadyear,semester, stuIds.stream().skip(i).limit(900).collect(Collectors.toList()),proIds);
			if(CollectionUtils.isNotEmpty(temp))records.addAll(temp);
		}
		return records;
	}

	@Override
	public List<DiathesisRecord> findListByUnitIdAndStuId(String unitId, String studentId) {
		return diathesisRecordDao.findListByUnitIdAndStuId(unitId,studentId);
	}

	@Override
	public List<DiathesisRecord> findListByUnitIdAndProjectIdInAndStuId(String unitId, String acadyear, Integer semester, List<String> projectIds, String studentId) {
		return diathesisRecordDao.findListByUnitIdAndProjectIdInAndStuId(unitId,acadyear,semester,projectIds,studentId);
	}

	@Override
	public List<DiathesisRecord> findListByUnitIdAndProjectIdIn(String unitId, String acadyear, Integer semester, String[] projectIds) {
		return diathesisRecordDao.findListByUnitIdAndProjectIdIn(unitId,acadyear,semester,projectIds);
	}

	@Override
	public void deleteRecord(String unitId, String[] ids) {
		if(ids==null || ids.length==0)return;
		diathesisRecordDao.deleteByIds(ids);
		diathesisRecordInfoService.deleteByRecordIds(unitId,ids);
		diathesisRecordMessageService.deleteByRecordIds(Arrays.asList(ids));
	}

	@Override
	public void deleteByProjectIds(String[] projectIds) {
		diathesisRecordDao.deleteByProjectId(projectIds);
	}

	@Override
	public List<String> findIdsByProjectIdIn(String[] projectIds) {
		return diathesisRecordDao.findByProjectIdIn(projectIds);
	}

	@Override
	public List<DiathesisRecord> findListByProjectIdAndStuIdIn(String unitId, String projectId, String acadyear, String semester, String[] statu,String[] stuIds) {
		return diathesisRecordDao.findListByProjectIdAndStuIdIn(unitId,projectId,acadyear,Integer.parseInt(semester),statu,stuIds);
	}

	@Override
	public List<DiathesisRecord> findListByAcadyearAndSemesterAndStuId(String unitId, String acadyear, Integer semester, String studentId) {
		return diathesisRecordDao.findListByAcadyearAndSemesterAndStuId(unitId,acadyear,semester,studentId);
	}
}
