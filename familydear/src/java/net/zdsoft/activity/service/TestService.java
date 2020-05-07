package net.zdsoft.activity.service;

import net.zdsoft.familydear.entity.FamDearAttachment;
import net.zdsoft.basedata.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.activity.service
 * @ClassName: TestService
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/23 14:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/23 14:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface TestService extends BaseService<FamDearAttachment, String> {
    /**
     * 保存附件
     * @param attachment
     * @param files
     */
    public void saveAttachment(FamDearAttachment attachment, MultipartFile file);

    /**
     * 处理ios拍照旋转问题
     * @param attachment
     * @param multfile
     * @param file 修正之后的file
     */
    public void saveAttachment(FamDearAttachment attachment, MultipartFile multfile, File file);

    /**
     * 保存附件，
     * @param attachment
     * @param oriFile 原图
     */
    public void saveAttachment(FamDearAttachment attachment, File oriFile);

    /**
     * 根据ids数组删除studevelop_attachment数据
     * @param ids
     * @return
     */
    public Integer delete(String[] ids);

    public void deleteByObjIds(String[] objIds);

    /**
     * 根据objId数组获取list
     * @param objId
     * @return
     */
    List<FamDearAttachment> findListByObjIds(String... objId);

    /**
     * 根据objId数组获取map  key:objId, value:list
     * @param objId
     * @return
     */
    Map<String, List<FamDearAttachment>> findMapByObjIds(String...objId);

    public List<FamDearAttachment> getAttachmentByObjId(String objId, String objType);

    public int findAttachmentNumByObjId(String objId, String objType);

    public void saveFile(String objId,String objType,String insertId);
}
