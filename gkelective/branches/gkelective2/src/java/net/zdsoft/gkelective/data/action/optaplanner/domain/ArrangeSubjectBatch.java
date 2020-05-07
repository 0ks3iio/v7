package net.zdsoft.gkelective.data.action.optaplanner.domain;

public class ArrangeSubjectBatch extends AbstractPersistable{
	private static final long serialVersionUID = 1L;
	
	private Integer[] subjectIdIndexs;

	/**
	 * 获取subjectIdIndexs
	 * @return subjectIdIndexs
	 */
	public Integer[] getSubjectIdIndexs() {
	    return subjectIdIndexs;
	}

	/**
	 * 设置subjectIdIndexs
	 * @param subjectIdIndexs subjectIdIndexs
	 */
	public void setSubjectIdIndexs(Integer[] subjectIdIndexs) {
	    this.subjectIdIndexs = subjectIdIndexs;
	}

	
}
