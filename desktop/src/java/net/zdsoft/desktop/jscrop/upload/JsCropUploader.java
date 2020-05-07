package net.zdsoft.desktop.jscrop.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import net.zdsoft.desktop.jscrop.define.JsCropConstant;
import net.zdsoft.desktop.jscrop.define.JsCropFile;
import net.zdsoft.desktop.jscrop.define.JsCropFileType;
import net.zdsoft.desktop.jscrop.define.JsCropState;
import net.zdsoft.desktop.jscrop.utils.JsCropUploaderUtils;
import net.zdsoft.framework.utils.JsCropUtils;
import net.zdsoft.framework.utils.StorageFileUtils;

/**
 * @author shenke
 */
public class JsCropUploader {

    private static final Logger LOG = LoggerFactory.getLogger(JsCropUploader.class);

    private HttpServletRequest request;
    private JsCropConfigure jsCropConfigure;
    private String userId;
    private String unitId;

    public JsCropUploader(String userId, String unitId) {
        this.unitId = unitId;
        this.userId = userId;
    }



    /**
     * 预处理
     * @return
     */
    public JsCropState pretreatment(HttpServletRequest request){
        long startTime = System.currentTimeMillis();
        this.request = request;
        JsCropState jsCropState = new JsCropState();
        this.initEnv();
        File originFile = null;
        try {
            JsCropFile jsCropFile = new JsCropFile(null);
            MultipartFile file = StorageFileUtils.getFile(request);
            Assert.notNull(file);
            //缩放图片
            //ImageIO.setCacheDirectory();
            ImageIO.setUseCache(Boolean.FALSE);
            //JPEGCodec.createJPEGDecoder(file.getInputStream());
            BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            //BufferedImage zoomOriginImage = JsCropUtils.zoom(originImage,jsCropConfigure.getOriginZoomWidth(),jsCropConfigure.getOriginZoomHeight());
            //保存
            String dir =  JsCropUploaderUtils.getFilePath() + File.separator +  getDir();
            originFile = new File(dir + File.separator + "temp" + File.separator +
                    JsCropConstant.TEMP_FILE_PREFIX + JsCropConstant.FILE_SUFFIX);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(originImage,JsCropConstant.FILE_SUFFIX.replace(".",""),out);
            FileUtils.writeByteArrayToFile(originFile,out.toByteArray());

            jsCropFile.setBytes(out.toByteArray());
            jsCropFile.setFile(originFile);

            jsCropState.setSuccess(Boolean.TRUE);
            jsCropState.setJsCropFile(jsCropFile);
            System.out.println("[保存临时图片用时："+(System.currentTimeMillis()-startTime)+"ms]");
        } catch (IOException e) {
            LOG.error(ExceptionUtils.getRootCauseMessage(e));
            deleteFile(originFile);
            jsCropState.setJsCropFile(null);
            jsCropState.setSuccess(Boolean.FALSE);
            jsCropState.setDetailError(e.getMessage());
        }

        return jsCropState;
    }

    /**
     * 保存头像
     * @return
     */
    public JsCropState save(HttpServletRequest request){
         long startTime = System.currentTimeMillis();
        this.request = request;
        JsCropState jsCropState = new JsCropState();
        this.initEnv();
        File bigFile = null;
        File smallFile = null;
        File originFile = null;
        try{
            String dir =  JsCropUploaderUtils.getFilePath() + File.separator +  getDir();

            MultipartFile file = StorageFileUtils.getFile(request);
            Assert.notNull(file,"请选择照片");

            //保存源图片（未缩放）
            String suffix = JsCropConstant.FILE_SUFFIX;
            originFile = new File(dir + File.separator + JsCropConstant.ORIGIN_FILE_PREFIX + suffix);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(new ByteArrayInputStream(file.getBytes())),JsCropConstant.FILE_SUFFIX.replace(".",""),out);
            byte[] originBytes = out.toByteArray();//FileUtils.readFileToByteArray(originTempFile);
            FileUtils.writeByteArrayToFile(originFile,originBytes);
            //裁剪大图和小图
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(originBytes));
            double scale = 1/JsCropUtils.getScale(src.getWidth(),src.getHeight(),jsCropConfigure.getOriginZoomWidth(),jsCropConfigure.getOriginZoomHeight());
            BufferedImage zoomImage = JsCropUtils.zoom(src,scale);
            BufferedImage cutImage = JsCropUtils.cut(zoomImage,jsCropConfigure.getX(),jsCropConfigure.getY(),jsCropConfigure.getX2(),jsCropConfigure.getY2());
            BufferedImage bigImage = JsCropUtils.zoom(cutImage,jsCropConfigure.getBigDescWidth(),jsCropConfigure.getBigDescHeight());
            BufferedImage smallImage = JsCropUtils.zoom(cutImage,jsCropConfigure.getSmallerDescWidth(),jsCropConfigure.getSmallerDescHeight());
            //保存裁剪的图片
            //String extName = StorageFileUtils.getFileExtension(file.getOriginalFilename());
            bigFile = new File(dir + JsCropConstant.BIG_PORTRAIT_PREFIX + suffix);
            FileUtils.writeByteArrayToFile(bigFile,JsCropUtils.toBytes(bigImage));
            smallFile = new File(dir + JsCropConstant.SMALLER_PORTRAIT_PREFIX + suffix);
            FileUtils.writeByteArrayToFile(smallFile,JsCropUtils.toBytes(smallImage));

            JsCropFile jsCropFile = new JsCropFile(JsCropFileType.BIG);
            jsCropFile.setBytes(JsCropUtils.toBytes(bigImage));
            jsCropFile.setFile(bigFile);

            jsCropState.setOriginFilePath(getDir() + JsCropConstant.ORIGIN_FILE_PREFIX + suffix);
            jsCropState.setBigFilePath(getDir() + JsCropConstant.BIG_PORTRAIT_PREFIX + suffix);
            jsCropState.setSmallerFilePath(getDir() + JsCropConstant.SMALLER_PORTRAIT_PREFIX + suffix);

            jsCropState.setSuccess(Boolean.TRUE);
            jsCropState.setJsCropFile(jsCropFile);
            jsCropState.setMsg("头像保存成功");
            System.out.println("[保存头像用时："+(System.currentTimeMillis()-startTime)+"ms]");
        }catch(Exception e){
        	LOG.error(ExceptionUtils.getRootCauseMessage(e));
            deleteFile(originFile);
            deleteFile(bigFile);
            deleteFile(smallFile);
            jsCropState.setJsCropFile(null);
            jsCropState.setSuccess(Boolean.FALSE);
            jsCropState.setMsg("头像保存失败,"+e.getMessage());
        } finally {
            String dir = getDir();
            try {
                Runtime.getRuntime().exec("chmod -R 755 " + dir );
            } catch (IOException e) {
                //dot care
                LOG.error("上传的头像chmod 授权失败！");
            }
        }
        return jsCropState;
    }

    private void deleteFile(File file){
        if(file != null && file.exists()){
            file.delete();
        }
    }

    /**
     * 获取基本保存路径
     * @return
     */
    private String getDir(){
        return JsCropUploaderUtils.getDir(userId,unitId);
    }


    private void initEnv(){
        if(request == null){
            throw new IllegalArgumentException("request not null");
        }
        this.jsCropConfigure = new JsCropConfigure();
        jsCropConfigure.setOriginZoomHeight(getIntValue("originZoomHeight"));
        jsCropConfigure.setOriginZoomWidth(getIntValue("originZoomWidth"));
        jsCropConfigure.setBigDescHeight(getIntValue("bigDescHeight"));
        jsCropConfigure.setBigDescWidth(getIntValue("bigDescWidth"));

        jsCropConfigure.setSmallerDescHeight(getIntValue("smallerDescHeight"));
        jsCropConfigure.setSmallerDescWidth(getIntValue("smallerDescWidth"));
        jsCropConfigure.setX(getIntValue("x"));
        jsCropConfigure.setX2(getIntValue("x2"));
        jsCropConfigure.setY(getIntValue("y"));
        jsCropConfigure.setY2(getIntValue("y2"));

    }

    private int getIntValue(String attr){
        Object val = request.getParameter(attr);
        val = val==null?"":val;
        return NumberUtils.toInt(val.toString(),0);
    }
}
