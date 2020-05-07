
package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.common.AbstractPersistable;
import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioning.domain.Course;

public class Course extends AbstractPersistable {

    private String code;			//ex: "地理A"， "物理B"。A类是选考课，B类是学考课
    //private String englishCode;		//ex: "DiLiA", "WuLiB".
    
    private List<Student> studentList; 	//all the students who are taking this course
    
	//public static Set<String> allCourseNames;
	//{"物理A", "化学A", "生物A", "地理A", "政治A", "历史A", "技术A", 
    // "物理B", "化学B", "生物B", "地理B", "政治B", "历史B", "技术B"}
	
	//public static Set<String> aCourseNames;
	// {"物理A", "化学A", "生物A", "地理A", "政治A", "历史A", "技术A"};
	
	//public static Set<String> bCourseNames; 
	//{"物理B", "化学B", "生物B", "地理B", "政治B", "历史B", "技术B"};

	//public static Map<String, Course>  courseMap;
 
    public Course(String code) {
    	this.code = code;
    	//this.englishCode = translateChinese2English(code);
    	studentList = new ArrayList<Student> ();
    }
    
//    private String translateChinese2English(String chineseCourseName) {
//    	String englishCourseName = null;
//    	if (chineseCourseName.startsWith("语文")) {
//    		englishCourseName = chineseCourseName.replaceFirst("语文", "YuWen");
//    	}
//    	else if (chineseCourseName.startsWith("数学")) {
//    		englishCourseName = chineseCourseName.replaceFirst("数学", "ShuXue");
//    	}
//    	else if (chineseCourseName.startsWith("英语")) {
//    		englishCourseName = chineseCourseName.replaceFirst("英语", "YinYu");
//    	}
//    	else if (chineseCourseName.startsWith("物理")) {
//    		englishCourseName = chineseCourseName.replaceFirst("物理", "WuLi");
//    	}
//    	else if (chineseCourseName.startsWith("化学")) {
//    		englishCourseName = chineseCourseName.replaceFirst("化学", "HuaXue");
//    	}
//    	else if (chineseCourseName.startsWith("生物")) {
//    		englishCourseName = chineseCourseName.replaceFirst("生物", "ShengWu");
//    	}
//    	else if (chineseCourseName.startsWith("政治")) {
//    		englishCourseName = chineseCourseName.replaceFirst("政治", "ZhengZhi");
//    	}
//    	else if (chineseCourseName.startsWith("地理")) {
//    		englishCourseName = chineseCourseName.replaceFirst("地理", "DiLi");
//    	}
//    	else if (chineseCourseName.startsWith("历史")) {
//    		englishCourseName = chineseCourseName.replaceFirst("历史", "LiShi");
//    	}
//    	else if (chineseCourseName.startsWith("技术")) {
//    		englishCourseName = chineseCourseName.replaceFirst("技术", "JiShu");
//    	}
//    	else
//    		assert false;
//    	
//    	return englishCourseName;
//    	
//    }
    
    /**
     * 
     * @param selected3names, 有A后缀，是3门选课
     * @return 有B后缀的其它几门学课的名字，如果7门课全部开班，就返回B类的4门课的名字
     */
//    public static Set<String> getBNameSet(Set<String> selected3Anames) {
//    	
//    	Set<String> notMyBCourses = new HashSet<String> ();
//    	for (String c : selected3Anames) {
//    		notMyBCourses.add(c.substring(0, c.length() - 1) + "B");
//    	}
//    	
//    	Set<String> myBCourses = Sets.difference(Course.bCourseNames, notMyBCourses);
//    	
//    	return myBCourses;
//    }
     
//    public String getEnglishCode() {
//		return englishCode;
//	}
//
//	public void setEnglishCode(String englishCode) {
//		this.englishCode = englishCode;
//	}
//    
    
    public String getCode() {
    	
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

	public void addStudentList(List<Student> students) {
		studentList.addAll(students);
	}
	
	public int getStudentCount() {
		return studentList.size();
	}
	

}
