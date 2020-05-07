package net.zdsoft.exammanage.xj.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangsj  2017年10月12日下午4:11:56
 */
@Entity
@Lazy
@Table(name = "xjexam_type")
public class XjexamType extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String typeKey;
    private String typeValue;

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public String fetchCacheEntitName() {
        // TODO Auto-generated method stub
        return "examType";
    }

}
