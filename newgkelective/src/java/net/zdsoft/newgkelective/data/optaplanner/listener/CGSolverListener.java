package net.zdsoft.newgkelective.data.optaplanner.listener;

import net.zdsoft.newgkelective.data.optaplanner.dto.CGInputData;

public interface CGSolverListener {

	/**
	 * 结束排
	 * 
	 * @param resultClass
	 * @param isHaveConflict
	 *            是否存在冲突
	 */
	public abstract void solveFinished(CGInputData resultClass, boolean isHaveConflict);

	/**
	 * 出现意外错误
	 */
	public abstract void onError(Exception e);
}
