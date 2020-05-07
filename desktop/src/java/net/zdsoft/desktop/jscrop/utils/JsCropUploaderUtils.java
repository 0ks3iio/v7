package net.zdsoft.desktop.jscrop.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.zdsoft.desktop.jscrop.define.JsCropConstant;
import net.zdsoft.desktop.jscrop.define.JsCropFile;
import net.zdsoft.desktop.jscrop.define.JsCropFileType;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * @author shenke
 */
public class JsCropUploaderUtils {
	 private static String FILE_PATH ;
    /**
     * 获取源图片
     */
    public static final JsCropFile getOriginFile(String userId, String unitId, Integer sex){
        String baseDir = getFilePath() + File.separator + getDir(userId,unitId) + JsCropConstant.ORIGIN_FILE_PREFIX  + JsCropConstant.FILE_SUFFIX;
        return getFile(baseDir,JsCropFileType.ORIGIN,sex);
    }

    /**
     * 获取大头像
     */
    public static final JsCropFile getBigFile(String userId, String unitId, Integer sex){
        String baseDir = getFilePath() + File.separator + getDir(userId,unitId) + JsCropConstant.BIG_PORTRAIT_PREFIX  + JsCropConstant.FILE_SUFFIX;
        return getFile(baseDir,JsCropFileType.BIG,sex);
    }

    /**
     * 获取小头像
     */
    public static final JsCropFile getSmallerFile(String userId, String unitId, Integer sex){
        String baseDir = getFilePath() + File.separator + getDir(userId,unitId) + JsCropConstant.SMALLER_PORTRAIT_PREFIX  + JsCropConstant.FILE_SUFFIX;
        return getFile(baseDir,JsCropFileType.SMALLER,sex);
    }

    /**
     * 临时
     * @param userId
     * @param unitId
     * @return
     */
    public static final JsCropFile getPreviewFile(String userId, String unitId, Integer sex){
        String baseDir = getFilePath() + File.separator + getDir(userId,unitId) + "temp"+File.separator + JsCropConstant.TEMP_FILE_PREFIX + JsCropConstant.FILE_SUFFIX;
        return getFile(baseDir,JsCropFileType.TEMP_ORIGIN,sex);
    }

    private static final JsCropFile getFile(String path,JsCropFileType type,Integer sex){
        try {
            File file = new File(path);
            JsCropFile jsCropFile = new JsCropFile(type);
            jsCropFile.setFile(file);
            if (!file.exists()){
                if ( sex == null ) {
                    sex = 0;
                }
                if(JsCropFileType.BIG.equals(type)){
                    String defalutPath = null;
                    if (2 == sex) {
                        defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_big_female.png");
                    }else {
                        defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_big_male.png");
                    }
                    jsCropFile.setFile(new File(defalutPath));
                }else{
                    String defalutPath = null;
                    if (2 == sex) {
                        defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_female.png");
                    }else {
                        defalutPath = Evn.getRequest().getRealPath("/static/jscrop/images/portrait_male.png");
                    }
                    jsCropFile.setFile(new File(defalutPath));
                }
            }
            jsCropFile.setBytes(FileUtils.readFileToByteArray(jsCropFile.getFile()));
            return jsCropFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param userId
     * @param unitId
     * @param type
     * @return
     */
    public static final JsCropFile getJsCropFile(String userId, String unitId,JsCropFileType type, Integer sex){
        if(type.equals(JsCropFileType.BIG)){
            return getBigFile(userId,unitId,sex);
        }
        if(JsCropFileType.ORIGIN.equals(type)){
            return getOriginFile(userId,unitId,sex);
        }
        if(JsCropFileType.SMALLER.equals(type)){
            return getSmallerFile(userId,unitId,sex);
        }
        if(JsCropFileType.TEMP_ORIGIN.equals(type)){
            return getPreviewFile(userId,unitId,sex);
        }
        return null;
    }

    public static final String getDir(String userId, String unitId){
        StringBuilder descDir = new StringBuilder();

        descDir.append("portrait")
                .append(File.separator)
                .append(unitId)
                .append(File.separator)
                .append(userId).append(File.separator);
        File saveFile = new File(descDir.toString());
        if(!saveFile.exists()){
            saveFile.mkdirs();
        }
        return descDir.toString();
    }
    
    public static String getFilePath() {
        if ( FILE_PATH == null ) {
            SysOptionRemoteService sysOptionRemoteService = Evn.getBean("sysOptionRemoteService");
            if ( sysOptionRemoteService == null ) {
                return FILE_PATH = Evn.getString(net.zdsoft.framework.entity.Constant.STORE_PATH) ;
            }
            return FILE_PATH = sysOptionRemoteService.findValue(Constant.FILE_PATH) + File.separator + "store";
        }
        return FILE_PATH;
    }
}
