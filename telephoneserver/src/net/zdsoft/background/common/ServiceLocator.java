package net.zdsoft.background.common;

import java.util.List;

import net.zdsoft.message.client.MessageClient;

public class ServiceLocator {
	  private String warningSmsSpCommName;
	  private List<String> watchManagerPhones;
	  private MessageClient sysWatchMsgClient;
	  private static ServiceLocator instance = new ServiceLocator();
	  
	  public static ServiceLocator getInstance()
	  {
	    return instance;
	  }
	  
	  public MessageClient getSysWatchMsgClient()
	  {
	    return this.sysWatchMsgClient;
	  }
	  
	  public void setSysWatchMsgClient(MessageClient sysWatchMsgClient)
	  {
	    this.sysWatchMsgClient = sysWatchMsgClient;
	  }
	  
	  public String getWarningSmsSpCommName()
	  {
	    return this.warningSmsSpCommName;
	  }
	  
	  public void setWarningSmsSpCommName(String warningSmsSpCommName)
	  {
	    this.warningSmsSpCommName = warningSmsSpCommName;
	  }
	  
	  public List<String> getWatchManagerPhones()
	  {
	    return this.watchManagerPhones;
	  }
	  
	  public void setWatchManagerPhones(List<String> watchManagerPhones)
	  {
	    this.watchManagerPhones = watchManagerPhones;
	  }
}
