package net.zdsoft.studevelop.data.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.studevelop.data.dao.StudevelopActivityDao;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;
import net.zdsoft.studevelop.data.service.StudevelopActivityService;
import net.zdsoft.studevelop.data.service.StudevelopAttachmentService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * studevelop_activity
 * @author 
 * 
 */
@Service("studevelopActivityService")
public class StudevelopActivityServiceImpl extends BaseServiceImpl<StudevelopActivity, String>implements StudevelopActivityService{
	@Autowired
	private StudevelopActivityDao studevelopActivityDao;
	@Autowired
	private StudevelopAttachmentService studevelopAttachmentService;

	public List<StudevelopActivity> findActBySemeRangeId(String acadyear, int semester, String actType, 
			String rangeId, String rangeType) {
		List<StudevelopActivity> acts = studevelopActivityDao.findActBySemeRangeId(acadyear, semester, actType, rangeId, rangeType);
		if(CollectionUtils.isNotEmpty(acts)){
			List<String> ids = EntityUtils.getList(acts, StudevelopActivity::getId);
			List<StudevelopAttachment> atts = studevelopAttachmentService.findListByObjIds(ids.toArray(new String[0]));
			Map<String, String> actAttIds = EntityUtils.getMap(atts, "objId", "id");
			for(StudevelopActivity act : acts){
				act.setCoverAttId(actAttIds.get(act.getId()));
			}
		}
		return acts;
	}

	@Override
	public List<StudevelopActivity> findActBySemeRangeType(String acadyear, int semester, String actType, String rangeType) {
		List<StudevelopActivity> acts = studevelopActivityDao.findActBySemeRangeType(acadyear, semester, actType, rangeType);
		if(CollectionUtils.isNotEmpty(acts)){
			List<String> ids = EntityUtils.getList(acts, StudevelopActivity::getId);
			List<StudevelopAttachment> atts = studevelopAttachmentService.findListByObjIds(ids.toArray(new String[0]));
			Map<String, String> actAttIds = EntityUtils.getMap(atts, "objId", "id");

			Map<String , List<StudevelopAttachment>  > attsMap = studevelopAttachmentService.findMapByObjIds(ids.toArray(new String[0]));
			if(attsMap == null){
				attsMap = new HashMap<>();
			}
			for(StudevelopActivity act : acts){
				act.setCoverAttId(actAttIds.get(act.getId()));
				act.setAtts(attsMap.get(act.getId()));
			}
		}
		return acts;
	}

	@Override
	protected BaseJpaRepositoryDao<StudevelopActivity, String> getJpaDao() {
		return studevelopActivityDao;
	}

	@Override
	protected Class<StudevelopActivity> getEntityClass() {
		return StudevelopActivity.class;
	}
	
	public void saveInfo(StudevelopActivity info){
		if(StringUtils.isNotEmpty(info.getDelAttIds())){
			studevelopAttachmentService.delete(StringUtils.split(info.getDelAttIds(), ","));
		}
		this.save(info);
		
		String fileSystemPath = Evn.<SysOptionRemoteService> getBean(
				"sysOptionRemoteService").findValue(Constant.FILE_PATH);// 文件系统地址
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		String filePath = "upload" + File.separator + ymd + File.separator
				+ info.getId();
		String tempDirPath = fileSystemPath + File.separator + filePath;
		System.out.println(info.getActType()+"保存附件，提取文件的临时目录："+tempDirPath);
		File dir = new File(tempDirPath);
		if(dir == null || !dir.exists()){
			return ;
		}
		if(info.isHasUpload()){
			File[] files = dir.listFiles();
			if(ArrayUtils.isEmpty(files)){
				return ;
			}
			for (int i=0;i<files.length;i++) {
				File file = files[i];
				StudevelopAttachment att = new StudevelopAttachment();
				att.setObjecttype(info.getActType());
				att.setObjId(info.getId());
				att.setUnitId(info.getUnitId());
				try {
					studevelopAttachmentService.saveAttachment(att, file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {// 此次保存若没有上传新图片，则将临时目录下的图片清掉
			FileUtils.deleteQuietly(dir);
		}
	}

	@Override
	public Integer delete(String[] ids) {
		if(ArrayUtils.isEmpty(ids)){
			return 0;
		}
		studevelopAttachmentService.deleteByObjIds(ids);
		studevelopActivityDao.deleteByIds(ids);
		return null;
	}

}
