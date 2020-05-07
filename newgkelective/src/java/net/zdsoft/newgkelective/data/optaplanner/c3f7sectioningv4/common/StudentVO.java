package net.zdsoft.newgkelective.data.optaplanner.c3f7sectioningv4.common;

public class StudentVO {
	
	@ExcelAttribute(name = "学生id", column = "A")  
    private String stuId;  
  
    @ExcelAttribute(name = "姓名", column = "E")  
    private String name;  
  
    @ExcelAttribute(name = "性别", column = "C")  
    private String sex;  
    
    @ExcelAttribute(name = "文理分班", column = "F")  
    private String classNum;

    public String getClassNum() {
		return classNum;
	}

	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	// 各科 成绩
 	@ExcelAttribute(name = "语文", column = "G")  
 	private Double yuwen;
 	@ExcelAttribute(name = "数学", column = "H")  
 	private Double math;
 	@ExcelAttribute(name = "英语", column = "I")  
 	private Double english;
 	
 	@ExcelAttribute(name = "物理", column = "J")  
 	public Double physics;
 	@ExcelAttribute(name = "化学", column = "K")  
 	public Double chemistry;
 	@ExcelAttribute(name = "生物", column = "L")  
 	public Double biology;
 	
 	@ExcelAttribute(name = "历史", column = "M")  
 	private Double politic;
 	@ExcelAttribute(name = "政治", column = "N")  
 	private Double history;
 	@ExcelAttribute(name = "地理", column = "O")  
 	private Double geo;
 	
 	// 层次
 	@ExcelAttribute(name = "语文层次", column = "P")  
 	private String yuwenLevel;
 	@ExcelAttribute(name = "数学层次", column = "Q")  
 	private String mathLevel;
 	@ExcelAttribute(name = "英语层次", column = "R")  
 	private String engLevel;
 	
 	@ExcelAttribute(name = "物理层次", column = "S")  
 	private String physicalLevel;
 	@ExcelAttribute(name = "化学层次", column = "T")  
 	private String chemistryLevel;
 	@ExcelAttribute(name = "生物层次", column = "U")  
 	private String biologyLevel;
 	
 	@ExcelAttribute(name = "历史层次", column = "V")  
 	private String historyLevel;
 	@ExcelAttribute(name = "政治层次", column = "W")  
 	private String politicLevel;
 	@ExcelAttribute(name = "地理层次", column = "X")  
 	private String geoLevel;
 	
	@Override
	public String toString() {
		return "StudentVO [stuId=" + stuId + ", name=" + name + ", sex=" + sex + ", classNum=" + classNum + ", yuwen="
				+ yuwen + ", math=" + math + ", english=" + english + ", physics=" + physics + ", chemistry="
				+ chemistry + ", biology=" + biology + ", politic=" + politic + ", history=" + history + ", geo=" + geo
				+ ", yuwenLevel=" + yuwenLevel + ", mathLevel=" + mathLevel + ", engLevel=" + engLevel
				+ ", physicalLevel=" + physicalLevel + ", chemistryLevel=" + chemistryLevel + ", biologyLevel="
				+ biologyLevel + ", historyLevel=" + historyLevel + ", politicLevel=" + politicLevel + ", geoLevel="
				+ geoLevel + "]";
	}
	
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Double getYuwen() {
		return yuwen;
	}
	public void setYuwen(Double yuwen) {
		this.yuwen = yuwen;
	}
	public Double getMath() {
		return math;
	}
	public void setMath(Double math) {
		this.math = math;
	}
	public Double getEnglish() {
		return english;
	}
	public void setEnglish(Double english) {
		this.english = english;
	}
	public Double getPhysics() {
		return physics;
	}
	public void setPhysics(Double physics) {
		this.physics = physics;
	}
	public Double getChemistry() {
		return chemistry;
	}
	public void setChemistry(Double chemistry) {
		this.chemistry = chemistry;
	}
	public Double getBiology() {
		return biology;
	}
	public void setBiology(Double biology) {
		this.biology = biology;
	}
	public Double getPolitic() {
		return politic;
	}
	public void setPolitic(Double politic) {
		this.politic = politic;
	}
	public Double getHistory() {
		return history;
	}
	public void setHistory(Double history) {
		this.history = history;
	}
	public Double getGeo() {
		return geo;
	}
	public void setGeo(Double geo) {
		this.geo = geo;
	}
	public String getYuwenLevel() {
		return yuwenLevel;
	}
	public void setYuwenLevel(String yuwenLevel) {
		this.yuwenLevel = yuwenLevel;
	}
	public String getMathLevel() {
		return mathLevel;
	}
	public void setMathLevel(String mathLevel) {
		this.mathLevel = mathLevel;
	}
	public String getEngLevel() {
		return engLevel;
	}
	public void setEngLevel(String engLevel) {
		this.engLevel = engLevel;
	}
	public String getPhysicalLevel() {
		return physicalLevel;
	}
	public void setPhysicalLevel(String physicalLevel) {
		this.physicalLevel = physicalLevel;
	}
	public String getChemistryLevel() {
		return chemistryLevel;
	}
	public void setChemistryLevel(String chemistryLevel) {
		this.chemistryLevel = chemistryLevel;
	}
	public String getBiologyLevel() {
		return biologyLevel;
	}
	public void setBiologyLevel(String biologyLevel) {
		this.biologyLevel = biologyLevel;
	}
	public String getHistoryLevel() {
		return historyLevel;
	}
	public void setHistoryLevel(String historyLevel) {
		this.historyLevel = historyLevel;
	}
	public String getPoliticLevel() {
		return politicLevel;
	}
	public void setPoliticLevel(String politicLevel) {
		this.politicLevel = politicLevel;
	}
	public String getGeoLevel() {
		return geoLevel;
	}
	public void setGeoLevel(String geoLevel) {
		this.geoLevel = geoLevel;
	}
 	
}
