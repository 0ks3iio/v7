package net.zdsoft.newgkelective.data.dto;

import java.util.Map;

public class SimpleClassroomUseageDto {
	private String placeId;
	private String placeName;
	private Map<String,Integer> useageMap;
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public Map<String, Integer> getUseageMap() {
		return useageMap;
	}
	public void setUseageMap(Map<String, Integer> useageMap) {
		this.useageMap = useageMap;
	}
}
