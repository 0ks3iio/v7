package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
/**
 * 组合班参数
 */
public class GroupClass extends AbstractPersistable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classId;//班级id
	private List<String> sameSubjectId=new ArrayList<>();//相同的id--批次点必须一样
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public List<String> getSameSubjectId() {
		return sameSubjectId;
	}
	public void setSameSubjectId(List<String> sameSubjectId) {
		this.sameSubjectId = sameSubjectId;
	}
	//获取同样科目数据
	public String toString() {
		String ss="";
		if(CollectionUtils.isNotEmpty(sameSubjectId)) {
			for(String s:sameSubjectId) {
				ss=ss+","+s;
			}
			return ss.substring(1);
		}
		return ss;
	}
}
