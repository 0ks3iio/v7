package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public class GkPlaceDto {
	private String xzbId;
	private String xzbClassName;
	private String placeId;
	private String placeName;
	private String noCanArrangeBath;//不能安排的批次 例如A_1,A_2
	private String remake;//注解：不能安排选考1等
	
	private List<NewGkDivideClass> jxbClass=new ArrayList<NewGkDivideClass>();
	//跟随行政班的教学班
	private List<NewGkDivideClass> jxbClass2=new ArrayList<NewGkDivideClass>();
	
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
	public String getRemake() {
		return remake;
	}
	public void setRemake(String remake) {
		this.remake = remake;
	}
	public String getNoCanArrangeBath() {
		return noCanArrangeBath;
	}
	public void setNoCanArrangeBath(String noCanArrangeBath) {
		this.noCanArrangeBath = noCanArrangeBath;
	}
	public List<NewGkDivideClass> getJxbClass() {
		return jxbClass;
	}
	public void setJxbClass(List<NewGkDivideClass> jxbClass) {
		this.jxbClass = jxbClass;
	}
	public List<NewGkDivideClass> getJxbClass2() {
		return jxbClass2;
	}
	public void setJxbClass2(List<NewGkDivideClass> jxbClass2) {
		this.jxbClass2 = jxbClass2;
	}
	public String getXzbClassName() {
		return xzbClassName;
	}
	public void setXzbClassName(String xzbClassName) {
		this.xzbClassName = xzbClassName;
	}
	public String getXzbId() {
		return xzbId;
	}
	public void setXzbId(String xzbId) {
		this.xzbId = xzbId;
	}
	
}
