package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "studevelop_perform_item")
public class StuDevelopPerformItem extends BaseEntity<String>  implements  Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7918004359097210444L;
	private String unitId;
    /**
     * 学段加年级code
     */
    private String itemGrade;
    /**
     * 评估名称
     */
    private String itemName;
    /**
     * 排列number
     */
    private int displayOrder;
    private Date creationTime;
    private Date modifyTime;
    @Transient
    private List<StuDevelopPerformItemCode> codeList;

    public List<StuDevelopPerformItemCode> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<StuDevelopPerformItemCode> codeList) {
        this.codeList = codeList;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
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
        return "stuDevelopPerformItem";
    }

    public Object clone(){
        Object o = null;

        try {
            o = super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
