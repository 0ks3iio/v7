package net.zdsoft.studevelop.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "studevelop_perform_itemcode")
public class StuDevelopPerformItemCode  extends BaseEntity<String>  implements Comparable<StuDevelopPerformItemCode> ,Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5036595594880688091L;

	//
    private String  itemId;

    private String codeContent;
    private int displayOrder;
    private Date  creationTime;
    private Date modifyTime;
    @Override
    public String fetchCacheEntitName() {
        return "stuDevelopPerformItemCode";
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public int compareTo(StuDevelopPerformItemCode code) {
        if(this.displayOrder > code.getDisplayOrder()){
            return 1;
        }
        if(this.displayOrder < code.getDisplayOrder()){
            return -1;
        }
        return 0;
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
