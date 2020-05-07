package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.io.Serializable;

public abstract class AbstractPersistable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer id;

	protected AbstractPersistable() {
	}

	protected AbstractPersistable(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
