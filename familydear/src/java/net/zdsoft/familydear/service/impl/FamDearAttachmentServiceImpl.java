package net.zdsoft.familydear.service.impl;

import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.basedata.entity.StorageDir;
import net.zdsoft.basedata.remote.service.StorageDirRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.familydear.dao.FamDearAttachmentDao;
import net.zdsoft.familydear.service.FamDearAttachmentService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.*;
import net.zdsoft.studevelop.data.constant.StuDevelopConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.activity.service.impl
 * @ClassName: testServiceImpl
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/23 14:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/23 14:41
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Service("famDearAttachmentService")
public class FamDearAttachmentServiceImpl extends BaseServiceImpl<FamDearAttachment,String> implements FamDearAttachmentService {
    @Autowired
    private StorageDirRemoteService storageDirRemoteService;
    @Autowired
    private FamDearAttachmentDao famDearAttachmentDao;

    public void saveAttachment(FamDearAttachment attachment, MultipartFile file,boolean isPicture){
        if(file == null){
            throw new IllegalArgumentException("上传文件为空!");
        }
        if(file.getSize() == 0){
            throw new IllegalArgumentException("上传文件大小为空!");
        }
        attachment.setContenttype(file.getContentType());
        attachment.setFilename(file.getOriginalFilename());
        attachment.setExtName(StorageFileUtils.getFileExtension(file.getOriginalFilename()));
        attachment.setFilesize(file.getSize());
        try {
            saveAttachmentWithBytes(attachment, file.getBytes(),isPicture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAttachment(FamDearAttachment attachment, MultipartFile multfile, File file) {

    }

    @Override
    public void saveAttachment(FamDearAttachment attachment, File oriFile) {

    }

    private void saveAttachmentWithBytes(FamDearAttachment att, byte[] bts,boolean isPicture){
        if(ArrayUtils.isEmpty(bts)){
            throw new IllegalArgumentException("上传文件为空或者文件大小为空!");
        }
        // 更新附件表，先删除原附件文件（一个附件一个目录文件夹，删除整个文件夹）
        deleteFile(att);

        List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findByTypeAndActiove(0, "1"), new TR<List<StorageDir>>(){});
        if(CollectionUtils.isEmpty(dirs)){
            return;
        }
        StorageDir dir = dirs.get(0);
        att.setDirId(dir.getId());
        Date now = new Date();
        String year = DateUtils.date2String(now, "yyyy");
        if(StringUtils.isBlank(att.getId())){
            att.setId(UuidUtils.generateUuid());
        }
        att.setCreationTime(now);
        att.setModifyTime(now);
        String filePath = StuDevelopConstant.ATTACHMENT_FILEPATH + File.separator + att.getObjecttype()
                + File.separator + att.getUnitId() + File.separator + year + File.separator + att.getId();
//		dir.setDir("D://store");
        String dirPath = dir.getDir() + File.separator + filePath;
        File dirFile = new File(dirPath);
        dirFile.mkdirs();
        try {
	        if(isPicture){
	        	File orFile = new File(dirPath + File.separator+ StuDevelopConstant.PIC_ORIGIN_NAME+"."+att.getExtName());
	        	FileUtils.writeByteArrayToFile(orFile, bts);
	        	BufferedImage zoomImage = JsCropUtils.zoom(orFile, 100, 100);
	        	File desFile = new File(dirPath + File.separator+ att.getId()+"."+att.getExtName());
	        	FileUtils.writeByteArrayToFile(desFile, JsCropUtils.toBytes(zoomImage));
	        }else{
	        	File orFile = new File(dirPath + File.separator+ att.getId()+"."+att.getExtName());
	        	FileUtils.writeByteArrayToFile(orFile, bts);
	        }
        

//            FamDearAttachment.FamDearAttachmentObjType objType = FamDearAttachment.FamDearAttachmentObjType.getType(att.getObjecttype());
//            if(objType != null){
//
//
//				/*BufferedImage zoomImageMobile =JsCropUtils.zoom(zoomImage, 0.5);
//				File mobileFile = new File(dirPath + File.separator+ StuDevelopConstant.PIC_MOBILE_NAME+"."+att.getExtName());
//				FileUtils.writeByteArrayToFile(mobileFile, JsCropUtils.toBytes(zoomImageMobile));*/
//            }
           
            att.setFilePath(filePath + File.separator+ att.getId()+"."+att.getExtName());
            famDearAttachmentDao.save(att);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    private void deleteFile(FamDearAttachment attachment){
        if(StringUtils.isNotEmpty(attachment.getDirId())
                && StringUtils.isNotEmpty(attachment.getFilePath())){
            try {
                StorageDir dir = SUtils.dc(storageDirRemoteService.findOneById(attachment.getDirId()), StorageDir.class);
                if(dir != null){
                    File img = new File(dir.getDir() + File.separator + attachment.getFilePath());
                    if (img != null && img.exists()) {
                        String dirPath = img.getParent();
                        File dirFile = new File(dirPath);
                        FileUtils.deleteDirectory(dirFile);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public Integer delete(String[] ids) {
        deleteWithEntity(this.findListByIds(ids));
        return null;
    }

    @Override
    public void deleteByObjIds(String[] objIds) {

    }

    @Override
    public List<FamDearAttachment> findListByObjIds(String... objId) {
        return famDearAttachmentDao.findListByObjIds(objId);
    }

    @Override
    public Map<String, List<FamDearAttachment>> findMapByObjIds(String... objId) {
        return null;
    }

    @Override
    public List<FamDearAttachment> getAttachmentByObjId(String objId, String objType) {
        return famDearAttachmentDao.getAttachmentByObjId(objId, objType);
    }

    @Override
    public int findAttachmentNumByObjId(String objId, String objType) {
        return 0;
    }

    @Override
    public void saveFile(String objId, String objType, String insertId) {

    }

    /**
     * 删除附件及文件
     * @param atts
     */
    private void deleteWithEntity(List<FamDearAttachment> atts){
        if(CollectionUtils.isEmpty(atts)){
            return ;
        }
        Set<String> dirIds = EntityUtils.getSet(atts, FamDearAttachment::getDirId);
        List<StorageDir> dirs = SUtils.dt(storageDirRemoteService.findListByIds(dirIds.toArray(new String[0])), new TR<List<StorageDir>>(){});
        if(CollectionUtils.isEmpty(dirs)){
            Map<String, String> dirPathMap = EntityUtils.getMap(dirs, StorageDir::getId, StorageDir::getDir);
            for(FamDearAttachment att : atts){
                String dir = dirPathMap.get(att.getDirId());
                if(StringUtils.isEmpty(dir)){
                    continue;
                }
                File img = new File(dir + File.separator + att.getFilePath());
                String dirPath = img.getParent();
                File dirFile = new File(dirPath);
                try {
                    FileUtils.deleteDirectory(dirFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.deleteAll(atts.toArray(new FamDearAttachment[0]));
    }

    @Override
    protected BaseJpaRepositoryDao<FamDearAttachment, String> getJpaDao() {
        return famDearAttachmentDao;
    }

    @Override
    protected Class<FamDearAttachment> getEntityClass() {
        return FamDearAttachment.class;
    }
}
