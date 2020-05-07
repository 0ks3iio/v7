package net.zdsoft.exammanage.xj.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yangsj  2017年10月12日下午4:13:51
 */
@Entity
@Lazy
@Table(name = "xjexam_contrast")
public class XjexamContrast extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String columnKey;
    private String columnValue;

    @Override
    public String fetchCacheEntitName() {
        // TODO Auto-generated method stub
        return "examContrast";
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
}
