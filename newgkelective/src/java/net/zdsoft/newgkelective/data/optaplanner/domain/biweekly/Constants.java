package net.zdsoft.newgkelective.data.optaplanner.domain.biweekly;

import java.util.Map;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

public class Constants extends AbstractPersistable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer,Integer> coupleWeekIdMap;

	public Map<Integer, Integer> getCoupleWeekIdMap() {
		return coupleWeekIdMap;
	}

	public void setCoupleWeekIdMap(Map<Integer, Integer> coupleWeekIdMap) {
		this.coupleWeekIdMap = coupleWeekIdMap;
	}
}
