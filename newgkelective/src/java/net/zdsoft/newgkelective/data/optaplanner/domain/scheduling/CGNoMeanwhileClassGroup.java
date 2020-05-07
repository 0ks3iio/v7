package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;
import java.util.Set;

public class CGNoMeanwhileClassGroup implements Serializable{
	private static final long serialVersionUID = 1L;
	// str 格式 ：classId(oldId)
	private Set<String> oldIdGroup;
	
	
	public Set<String> getOldIdGroup() {
		return oldIdGroup;
	}
	public void setOldIdGroup(Set<String> oldIdGroup) {
		this.oldIdGroup = oldIdGroup;
	}
	public CGNoMeanwhileClassGroup(Set<String> oldIdGroup) {
		super();
		this.oldIdGroup = oldIdGroup;
	}
}
