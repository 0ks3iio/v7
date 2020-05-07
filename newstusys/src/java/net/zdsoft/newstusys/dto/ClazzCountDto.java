package net.zdsoft.newstusys.dto;

public class ClazzCountDto {
	
	private String className;
	private String schoolName;
	private String gradeName;
	private String address;
	private String clsId;
	private int clsStudentCount;//班级学生数
	private int inCityCount;//市内学生总数
	private int notCityCount;//市外学生数
	private int notHkStuCount;//无户口学生数
	private int migrationStuCount;//随迁子女数总
	private int syMigrationStuCount;//随迁子女数省外
	private int stayinStuCount;//留守儿童
	private int regularClassStuCount;//随班就读
	private int normalStuCount;//外籍学生数
	private int compatriotsCount;//港澳台生数
	private int nowStateStuCount;//待转入学生数
	private int boardingStuCount;//住宿学生数
	private String classCode;
	private int section;
	private String openAcadyear;
	
	
	/////异动报表字段
	private int fx;//复学
	private int rj;//入境
	private int xqnzr;//县区内转入
	private int snzr;//省内转入
	private int swzr;//省外转入
	private int hkxb;//户口新报
	private int ynjbb;//一年级补报
	private int qtzj;//其他增加	
	private int xx;//休学
	private int cj;//出境
	private int zwxqn;//转往县区内
	private int zwsn;//转往省内
	private int zwsw;//转往省外
	private int sw;//死亡
	private int qtjs;//其他减少
	
	public String getOpenAcadyear() {
		return openAcadyear;
	}

	public void setOpenAcadyear(String openAcadyear) {
		this.openAcadyear = openAcadyear;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getClsStudentCount() {
		return clsStudentCount;
	}

	public void setClsStudentCount(int clsStudentCount) {
		this.clsStudentCount = clsStudentCount;
	}

	public int getInCityCount() {
		return inCityCount;
	}

	public void setInCityCount(int inCityCount) {
		this.inCityCount = inCityCount;
	}

	public int getNotCityCount() {
		return notCityCount;
	}

	public void setNotCityCount(int notCityCount) {
		this.notCityCount = notCityCount;
	}

	public int getNotHkStuCount() {
		return notHkStuCount;
	}

	public void setNotHkStuCount(int notHkStuCount) {
		this.notHkStuCount = notHkStuCount;
	}

	public int getMigrationStuCount() {
		return migrationStuCount;
	}

	public void setMigrationStuCount(int migrationStuCount) {
		this.migrationStuCount = migrationStuCount;
	}

	public int getSyMigrationStuCount() {
		return syMigrationStuCount;
	}

	public void setSyMigrationStuCount(int syMigrationStuCount) {
		this.syMigrationStuCount = syMigrationStuCount;
	}

	public int getStayinStuCount() {
		return stayinStuCount;
	}

	public void setStayinStuCount(int stayinStuCount) {
		this.stayinStuCount = stayinStuCount;
	}

	public int getRegularClassStuCount() {
		return regularClassStuCount;
	}

	public void setRegularClassStuCount(int regularClassStuCount) {
		this.regularClassStuCount = regularClassStuCount;
	}

	public int getNormalStuCount() {
		return normalStuCount;
	}

	public void setNormalStuCount(int normalStuCount) {
		this.normalStuCount = normalStuCount;
	}

	public int getCompatriotsCount() {
		return compatriotsCount;
	}

	public void setCompatriotsCount(int compatriotsCount) {
		this.compatriotsCount = compatriotsCount;
	}

	public int getNowStateStuCount() {
		return nowStateStuCount;
	}

	public void setNowStateStuCount(int nowStateStuCount) {
		this.nowStateStuCount = nowStateStuCount;
	}

	public int getBoardingStuCount() {
		return boardingStuCount;
	}

	public void setBoardingStuCount(int boardingStuCount) {
		this.boardingStuCount = boardingStuCount;
	}

	public int getFx() {
		return fx;
	}

	public void setFx(int fx) {
		this.fx = fx;
	}

	public int getRj() {
		return rj;
	}

	public void setRj(int rj) {
		this.rj = rj;
	}

	public int getXqnzr() {
		return xqnzr;
	}

	public void setXqnzr(int xqnzr) {
		this.xqnzr = xqnzr;
	}

	public int getSnzr() {
		return snzr;
	}

	public void setSnzr(int snzr) {
		this.snzr = snzr;
	}

	public int getSwzr() {
		return swzr;
	}

	public void setSwzr(int swzr) {
		this.swzr = swzr;
	}

	public int getHkxb() {
		return hkxb;
	}

	public void setHkxb(int hkxb) {
		this.hkxb = hkxb;
	}

	public int getYnjbb() {
		return ynjbb;
	}

	public void setYnjbb(int ynjbb) {
		this.ynjbb = ynjbb;
	}

	public int getQtzj() {
		return qtzj;
	}

	public void setQtzj(int qtzj) {
		this.qtzj = qtzj;
	}

	public int getXx() {
		return xx;
	}

	public void setXx(int xx) {
		this.xx = xx;
	}

	public int getCj() {
		return cj;
	}

	public void setCj(int cj) {
		this.cj = cj;
	}

	public int getZwxqn() {
		return zwxqn;
	}

	public void setZwxqn(int zwxqn) {
		this.zwxqn = zwxqn;
	}

	public int getZwsn() {
		return zwsn;
	}

	public void setZwsn(int zwsn) {
		this.zwsn = zwsn;
	}

	public int getZwsw() {
		return zwsw;
	}

	public void setZwsw(int zwsw) {
		this.zwsw = zwsw;
	}

	public int getSw() {
		return sw;
	}

	public void setSw(int sw) {
		this.sw = sw;
	}

	public int getQtjs() {
		return qtjs;
	}

	public void setQtjs(int qtjs) {
		this.qtjs = qtjs;
	}

	public String getClsId() {
		return clsId;
	}

	public void setClsId(String clsId) {
		this.clsId = clsId;
	}

}
