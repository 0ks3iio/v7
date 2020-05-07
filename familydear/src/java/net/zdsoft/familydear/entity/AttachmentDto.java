package net.zdsoft.familydear.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.entity
 * @ClassName: AttachmentDto
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/30 13:45
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/30 13:45
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class AttachmentDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private List<FamDearAttachment> attachmentList;

    public List<FamDearAttachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<FamDearAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
