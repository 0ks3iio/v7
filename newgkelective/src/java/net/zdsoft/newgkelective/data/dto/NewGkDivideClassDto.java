package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public class NewGkDivideClassDto {
	private String key;//行政班，理科，文科，语数英
	private String tabName;
	private List<Course> showList=new ArrayList<Course>();
	//班级 key:subjectId 
	private Map<String,List<NewGkDivideClass>> classList=new HashMap<String,List<NewGkDivideClass>>();
	
	private NewGkDivideClass evn;
	private String[] classNames;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public List<Course> getShowList() {
		return showList;
	}
	public void setShowList(List<Course> showList) {
		this.showList = showList;
	}
	public Map<String, List<NewGkDivideClass>> getClassList() {
		return classList;
	}
	public void setClassList(Map<String, List<NewGkDivideClass>> classList) {
		this.classList = classList;
	}
	public NewGkDivideClass getEvn() {
		return evn;
	}
	public void setEvn(NewGkDivideClass evn) {
		this.evn = evn;
	}
	public String[] getClassNames() {
		return classNames;
	}
	public void setClassNames(String[] classNames) {
		this.classNames = classNames;
	}
}
