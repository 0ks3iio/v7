package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.domain;

import java.util.List;

import net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common.AbstractPersistable;

public class Curriculum extends AbstractPersistable{
	
	List<V4Course>		courseInCurriculumList;
	List<V4Student>		studentInCurriculumList;
	
	List<StudentCurriculum>	curriculumSectionList;

	
	public Curriculum() {
		super();
	}

	public Curriculum(List<V4Course> courseInCurriculumList, List<V4Student> studentInCurriculumList) {
		super();
		this.courseInCurriculumList = courseInCurriculumList;
		this.studentInCurriculumList = studentInCurriculumList;
	}
	
	public void printCurriculum () {
		courseInCurriculumList.stream().forEach(e -> System.out.print(e.getCourseCode()+ "-"));
		System.out.print("" + studentInCurriculumList.size() + ": ");
		curriculumSectionList.stream().forEach(e -> System.out.print(e.getStudentList().size() + ", "));
		System.out.println("");
		
		
	}

	public List<V4Course> getCourseInCurriculumList() {
		return courseInCurriculumList;
	}

	public void setCourseInCurriculumList(List<V4Course> courseInCurriculumList) {
		this.courseInCurriculumList = courseInCurriculumList;
	}

	public List<V4Student> getStudentInCurriculumList() {
		return studentInCurriculumList;
	}

	public void setStudentInCurriculumList(List<V4Student> studentInCurriculumList) {
		this.studentInCurriculumList = studentInCurriculumList;
	}

	public List<StudentCurriculum> getCurriculumSectionList() {
		return curriculumSectionList;
	}

	public void setCurriculumSectionList(List<StudentCurriculum> curriculumSectionList) {
		this.curriculumSectionList = curriculumSectionList;
	}  
	
	public String getCurriculumCode() {
		courseInCurriculumList.sort((e1, e2) -> {
			String o1 = e1.getCourseCode();
			String o2 = e2.getCourseCode();
            if((o1==null && o2==null))
                return 0;
            //假定o1和o2长度相同
            int flag=0;
            for(int i=0;i<o1.length();i++){
                if(o1.charAt(i)>o2.charAt(i)){
                    //第一个字符串大，返回值>0
                    flag=1;
                    break;
                }
                else if(o1.charAt(i)<o2.charAt(i)){
                    //第一个字符串小，返回值<0
                    flag=-1;
                    break;
                }
            }
            return flag;
        });
		
		StringBuilder strBuilder = new StringBuilder();
		for ( V4Course c : courseInCurriculumList) {
			strBuilder.append(c.getCourseCode() + "-");
		}
		
		return strBuilder.toString();
		
	}
}
