package net.zdsoft.newgkelective.data.optaplanner.util;

import java.io.Serializable;

import org.optaplanner.core.api.domain.lookup.PlanningId;

public abstract class AbstractPersistable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PlanningId
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
