package net.zdsoft.basedata.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持ajax方式的消息回显机制的实现
 * 
 */
public class TaskJobReply implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private boolean isHasTask = false;//是否任务模式
	private List<String> actionErrors = new ArrayList<String>();//业务中的错误
    private List<String> actionMessages = new ArrayList<String>();//正常返回的信息
    
    private String value;
    
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isHasTask() {
		return isHasTask;
	}

	public void setHasTask(boolean isHasTask) {
		this.isHasTask = isHasTask;
	}

	public void addActionError(String errorMessage){
		actionErrors.add(errorMessage);
    }

    public void addActionMessage(String message){
    	actionMessages.add(message);
    }
    public boolean hasActionErrors(){
    	return actionErrors.isEmpty();
    }

    public boolean hasActionMessages(){
    	return actionMessages.isEmpty();
    }
    
    public List<String> getActionErrors() {
    	return actionErrors;
    }
    
    public void setActionErrors(List<String> actionErrors) {
    	this.actionErrors = actionErrors;
    }
    
    public List<String> getActionMessages() {
    	return actionMessages;
    }
    
    public void setActionMessages(List<String> actionMessages) {
    	this.actionMessages = actionMessages;
    }
    
}
