package net.zdsoft.newgkelective.data.optaplanner.domain.biweekly;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

public class Teacher extends AbstractPersistable{

	private static final long serialVersionUID = 1L;
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
