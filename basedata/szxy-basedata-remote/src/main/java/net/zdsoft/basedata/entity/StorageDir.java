package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_storage_dir")
public class StorageDir extends BaseEntity<String>{

	private static final long serialVersionUID = -1271239569305809640L;
	
	@ColumnInfo(displayName = "类型")
	private Integer type;
	@ColumnInfo(displayName = "是否为激活")
    private String active;
	@ColumnInfo(displayName = "目录路径")
    private String dir;
    @ColumnInfo(displayName = "是否为预置")
    private String preset;

    public int getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getPreset() {
		return preset;
	}

	public void setPreset(String preset) {
		this.preset = preset;
	}

	public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

	@Override
	public String fetchCacheEntitName() {
		return "storageDir";
	}

}
