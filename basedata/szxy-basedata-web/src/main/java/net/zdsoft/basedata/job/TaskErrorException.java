package net.zdsoft.basedata.job;


public class TaskErrorException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public TaskErrorException(){
		super();
	}
	
	public TaskErrorException(String message){
		super(message);
	}

}
