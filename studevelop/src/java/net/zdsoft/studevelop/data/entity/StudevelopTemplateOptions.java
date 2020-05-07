package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by luf on 2018/12/14.
 */
@Entity
@Table(name="studevelop_template_options")
public class StudevelopTemplateOptions  extends BaseEntity<String> {

    private String optionName;
    private String  templateItemId;

    private Date creationTime;
    private Date modifyTime;

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getTemplateItemId() {
        return templateItemId;
    }

    public void setTemplateItemId(String templateItemId) {
        this.templateItemId = templateItemId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String fetchCacheEntitName() {
        return "studevelopTemplateOptions";
    }
}
