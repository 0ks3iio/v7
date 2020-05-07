package net.zdsoft.basedata.job;


public interface TaskJobService {
	
	public void jobDatas(TaskJobDataParam param,TaskJobReply reply) throws TaskErrorException;
	
}
