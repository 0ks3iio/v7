package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common;

import java.io.Serializable;

public abstract class AbstractPersistable implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Integer id;

	protected AbstractPersistable() {
	}

	protected AbstractPersistable(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}