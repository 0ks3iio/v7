package net.zdsoft.framework.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.constant.SystemIniConstants;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

@Lazy(true)
@Service
public class BIBigData {

	private static BIBigData biBigData = new BIBigData();
	
	public static BIBigData newInstance() {
        if (biBigData == null) {
        	biBigData = new BIBigData();
        }
        return biBigData;
    }
	
	public SystemIniRemoteService getSystemIniRemoteService() {
		return Evn.getBean("systemIniRemoteService");
	}
	
	public String getBiUrlLogin(){
		String biUrlCol = getSystemIniRemoteService().findValue(SystemIniConstants.BI_URL);
		String biUrl = biUrlCol;
//		String biUrl = "";
//		if(StringUtils.isNotBlank(biUrlCol)){
//			String[] split = biUrlCol.split(",");
//			biUrl = split[0];
//		}
		if(StringUtils.isNotBlank(biUrl)){
			return biUrl+"/SpagoBI";
		}else{
			return "";
		}
	}
	
//	public String getBiUrlCharts(){
//		String biUrlCol = getSystemIniRemoteService().findValue(SystemIniConstants.BI_URL);
//		String biUrl = "";
//		if(StringUtils.isNotBlank(biUrlCol)){
//			String[] split = biUrlCol.split(",");
//			biUrl = split[0];
//		}
//		if(StringUtils.isNotBlank(biUrl)){
//			return biUrl+"/biap/document/show";
//		}else{
//			return "";
//		}
//	}
	
}
