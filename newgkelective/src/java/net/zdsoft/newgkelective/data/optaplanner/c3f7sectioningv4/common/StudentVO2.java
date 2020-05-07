package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common;

public class StudentVO2 {
	@ExcelAttribute(name = "学生id", column = "A")  
    private String stuId;  
  
    @ExcelAttribute(name = "姓名", column = "B")  
    private String name;  
  
    @ExcelAttribute(name = "性别", column = "C")  
    private String sex;  

    @ExcelAttribute(name = "行政班", column = "D")  
    private String rawClassName;  
    
	@ExcelAttribute(name = "选课1", column = "E")  
    private String chooseSubject1;
    
    @ExcelAttribute(name = "选课2", column = "F")  
    private String chooseSubject2;
    
    @ExcelAttribute(name = "选课3", column = "G")  
    private String chooseSubject3;
    
//    @ExcelAttribute(name = "组合班ID", column = "H")  
//    private String zhbId;
//    
//    @ExcelAttribute(name = "已分配科目1", column = "I")  
//    private String allocatedSubject1;
//    
//    @ExcelAttribute(name = "已分配科目2", column = "J")  
//    private String allocatedSubject2;

    @ExcelAttribute(name = "组合班ID", column = "H")  
    private String sectionID;
    
    @ExcelAttribute(name = "已分配科目1", column = "I")  
    private String sectionSubject1;

    @ExcelAttribute(name = "已分配科目2", column = "J")  
    private String sectionSubject2;
    
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

	public String getChooseSubject2() {
		return chooseSubject2;
	}

	public void setChooseSubject2(String chooseSubject2) {
		this.chooseSubject2 = chooseSubject2;
	}

	public String getChooseSubject3() {
		return chooseSubject3;
	}

	public void setChooseSubject3(String chooseSubject3) {
		this.chooseSubject3 = chooseSubject3;
	}

	public String getSectionID() {
		return sectionID;
	}

	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}

	public String getSectionSubject1() {
		return sectionSubject1;
	}

	public void setSectionSubject1(String sectionSubject1) {
		this.sectionSubject1 = sectionSubject1;
	}

	public String getSectionSubject2() {
		return sectionSubject2;
	}

	public void setSectionSubject2(String sectionSubject2) {
		this.sectionSubject2 = sectionSubject2;
	}

	@Override
	public String toString() {
		return "StudentVO [stuId=" + stuId + ", name=" + name + ", sex=" + sex + ", rawClassName=" + rawClassName
				+ ", chooseSubject1=" + chooseSubject1 + ", chooseSubject2=" + chooseSubject2 + ", chooseSubject3="
				+ chooseSubject3 + ", sectionID=" + sectionID + ", sectionSubject1=" + sectionSubject1 + ", sectionSubject2=" + sectionSubject2 + "]";
	}

//	public String getZhbId() {
//		return zhbId;
//	}
//
//	public void setZhbId(String zhbId) {
//		this.zhbId = zhbId;
//	}
//
//	public String getAllocatedSubject1() {
//		return allocatedSubject1;
//	}
//
//	public void setAllocatedSubject1(String allocatedSubject1) {
//		this.allocatedSubject1 = allocatedSubject1;
//	}
//
//	public String getAllocatedSubject2() {
//		return allocatedSubject2;
//	}
//
//	public void setAllocatedSubject2(String allocatedSubject2) {
//		this.allocatedSubject2 = allocatedSubject2;
//	}
}
