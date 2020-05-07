package net.zdsoft.gkelective.data.action.optaplanner.domain;

public class ArrangeCapacityRange {

	private int classNum;
    private int minCapacity;
    private int maxCapacity;
    
    public int getClassNum() {
		return classNum;
	}
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	/**
     * 获取minCapacity
     * @return minCapacity
     */
    public int getMinCapacity() {
        return minCapacity;
    }
    /**
     * 设置minCapacity
     * @param minCapacity minCapacity
     */
    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }
    /**
     * 获取maxCapacity
     * @return maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    /**
     * 设置maxCapacity
     * @param maxCapacity maxCapacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    
}
