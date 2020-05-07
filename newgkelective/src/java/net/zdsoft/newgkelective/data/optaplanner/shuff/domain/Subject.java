package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

public class Subject extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String subjectName;
	private String subjectId;
	private int sectionSizeMean;
	private int sectionSizeMargin;
//	public String getSubjectName() {
//		return subjectName;
//	}
//	public void setSubjectName(String subjectName) {
//		this.subjectName = subjectName;
//	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public int getSectionSizeMean() {
		return sectionSizeMean;
	}
	public void setSectionSizeMean(int sectionSizeMean) {
		this.sectionSizeMean = sectionSizeMean;
	}
	public int getSectionSizeMargin() {
		return sectionSizeMargin;
	}
	public void setSectionSizeMargin(int sectionSizeMargin) {
		this.sectionSizeMargin = sectionSizeMargin;
	}

	
}
