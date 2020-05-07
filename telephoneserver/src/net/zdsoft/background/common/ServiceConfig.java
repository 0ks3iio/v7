package net.zdsoft.background.common;

import java.util.HashMap;
import java.util.Set;

public class ServiceConfig {
	private String name;
	  private String className;
	  private int serviceInterval;
	  private final HashMap<String, ServiceParam> params;
	  
	  public ServiceConfig()
	  {
	    this.params = new HashMap<String, ServiceParam>();
	  }
	  
	  public String getName()
	  {
	    return this.name;
	  }
	  
	  public void setName(String name)
	  {
	    this.name = name;
	  }
	  
	  public String getClassName()
	  {
	    return this.className;
	  }
	  
	  public void setClassName(String className)
	  {
	    this.className = className;
	  }
	  
	  public int getServiceInterval()
	  {
	    return this.serviceInterval;
	  }
	  
	  public void setServiceInterval(int serviceInterval)
	  {
	    this.serviceInterval = serviceInterval;
	  }
	  
	  public void addParam(ServiceParam param)
	  {
	    this.params.put(param.getName(), param);
	  }
	  
	  public ServiceParam getParam(String paramName)
	  {
	    return (ServiceParam)this.params.get(paramName);
	  }
	  
	  public String getParamValue(String paramName)
	  {
	    ServiceParam param = getParam(paramName);
	    return param == null ? null : param.getValue();
	  }
	  
	  public Set<String> getParamNames()
	  {
	    return this.params.keySet();
	  }
}
