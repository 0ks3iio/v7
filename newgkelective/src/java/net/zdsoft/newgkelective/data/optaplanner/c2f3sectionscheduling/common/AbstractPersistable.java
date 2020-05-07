package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common;

import java.io.Serializable;

public abstract class AbstractPersistable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2973831019135502507L;
	protected Integer id;//用于唯一标示

	protected AbstractPersistable() {
	}

	protected AbstractPersistable(int id) {
		this.id = id;
	}

	public int getId() {
		return id.intValue();
	}

	public void setId(int id) {
		this.id = id;
	}

}
