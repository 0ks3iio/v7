package net.zdsoft.newgkelective.data.optaplanner.c2f3sectionscheduling.common;

public class StudentVO1 {
	@ExcelAttribute(name = "学号", column = "A")  
    private String stuId;  
  
    @ExcelAttribute(name = "姓名", column = "B")  
    private String name;  
  
    @ExcelAttribute(name = "性别", column = "C")  
    private String sex;  

    @ExcelAttribute(name = "行政班", column = "D")  
    private String rawClassName;  
    
	@ExcelAttribute(name = "选课1", column = "E")  
    private String chooseSubject1;
    
//    @ExcelAttribute(name = "选课2", column = "F")  
//    private String chooseSubject2;
//    
//    @ExcelAttribute(name = "选课3", column = "G")  
//    private String chooseSubject3;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

    public String getRawClassName() {
		return rawClassName;
	}

	public void setRawClassName(String rawClassName) {
		this.rawClassName = rawClassName;
	}

	public String getChooseSubject1() {
		return chooseSubject1;
	}

	public void setChooseSubject1(String chooseSubject1) {
		this.chooseSubject1 = chooseSubject1;
	}

//	public String getChooseSubject2() {
//		return chooseSubject2;
//	}
//
//	public void setChooseSubject2(String chooseSubject2) {
//		this.chooseSubject2 = chooseSubject2;
//	}
//
//	public String getChooseSubject3() {
//		return chooseSubject3;
//	}
//
//	public void setChooseSubject3(String chooseSubject3) {
//		this.chooseSubject3 = chooseSubject3;
//	}
//
//	@Override
//	public String toString() {
//		return "StudentVO1 [stuId=" + stuId + ", name=" + name + ", sex=" + sex + ", rawClassName=" + rawClassName
//				+ ", chooseSubject1=" + chooseSubject1 + ", chooseSubject2=" + chooseSubject2 + ", chooseSubject3="
//				+ chooseSubject3 + "]";
//	}
}
