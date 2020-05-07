package net.zdsoft.api.base.enums;

public enum CensusTypeEnum {
	
	ALL {
      @Override
      public String getName() {
          return "all";
      }
    },
	APITYPE{
      @Override
      public String getName() {
          return "type";
      }
    },
	DEVELOPER{
      @Override
      public String getName() {
          return "developer";
      }
    };
	public abstract String getName();
}
