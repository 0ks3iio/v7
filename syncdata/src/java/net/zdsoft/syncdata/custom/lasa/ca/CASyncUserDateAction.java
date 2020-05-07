package net.zdsoft.syncdata.custom.lasa.ca;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.syncdata.custom.lasa.constant.LaSaCAConstant;
import net.zdsoft.syncdata.custom.lasa.service.CaSyncUserService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

@Controller
@RequestMapping("/syncdata/ca")
@Lazy
public class CASyncUserDateAction extends BaseAction{

	private Logger logger = LoggerFactory.getLogger(CASyncUserDateAction.class);
	private int operateID;
	
	@Autowired
	private CaSyncUserService caSyncUserService;
	/**
     *  operateID    操作码
     *  userType  备用
     *  @return 根据业务实际处理情况返回 true 或 false
	 *  @throws IOException 
     */
	@ResponseBody
	@RequestMapping(value = "userInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	String synType=request.getParameter(LaSaCAConstant.CA_SYNTYPE_NAME);
    	String operateID = request.getParameter(LaSaCAConstant.CA_OPERATEID_NAME);
    	if(StringUtils.isNotBlank(synType) && StringUtils.isNotBlank(operateID)) {
    		int synFlag=Integer.parseInt(synType);
    		this.operateID = Integer.parseInt(operateID);
    		if(LaSaCAConstant.CA_USER_SYN_FLAG == synFlag) {
    			synchronizeUser(request,response);
    		}
    	}else {
    		doReturnMsg(response, "false");
    	}
    }

	private void synchronizeUser(HttpServletRequest request, HttpServletResponse response){
        try {
            String userIdCode = request.getParameter("userIdCode");
            String returnMsg = "false";
            switch (operateID) {
                case LaSaCAConstant.CA_USER_ADD_AND_MODIFY: {  //增加、修改、授权用户
                    try {
						String result = getDate(userIdCode);
						if(StringUtils.isNotBlank(result)){
							caSyncUserService.saveUser(result);
							returnMsg = "true";
						}
					} catch (Exception e) {
						this.logger.error("用户信息保存失败", e);
					}
                    this.logger.info("用户信息是否保存成功:========="+returnMsg + " 保存用户的userIdCode=========：" + userIdCode);
                    doReturnMsg(response, returnMsg);
                    break;
                }
                case LaSaCAConstant.CA_USER_DELETE: {  // 删除用户
                	try {
						caSyncUserService.delUser(userIdCode);
						returnMsg = "true";
					} catch (Exception e) {
						this.logger.error("删除用户信息失败", e);
					}
                    this.logger.info("用户信息是否删除成功:" + returnMsg + " 删除用户的userIdCode=========：" + userIdCode);
                    doReturnMsg(response, returnMsg);
                    break;
                }
                default: {
                	doReturnMsg(response, returnMsg);
                    break;
                }
            }
        }catch (IOException e){
            this.logger.info(e.getMessage());
        }catch (Exception ex){
            this.logger.info(ex.getMessage());
        }
    }

	//---------------------------------------私有方法区 ----------------------------------------
	private String getDate(String userIdCode) throws IOException{
		String dataString = null;
		String url = getUrl(userIdCode);
		String jsonData = UrlUtils.get(url, new String());
		JSONObject jsonObject = Json.parseObject(jsonData);
		Integer status = jsonObject.getInteger(LaSaCAConstant.CA_STATUS_NAME);
		if(LaSaCAConstant.CA_SUCCESS_STATUS == status){
			dataString = jsonObject.getString(LaSaCAConstant.CA_RESOLVE_DATA_NAME);
		}
		return dataString;
	}
	
	private String getUrl(String userIdCode) {
		StringBuilder url = new StringBuilder();
		url.append(LaSaCAConstant.CA_QUERY_USER_URL);
		url.append("?userIdCode=");
		url.append(userIdCode);
		url.append("&authId=");
		url.append(LaSaCAConstant.CA_AUTHID_VALUE);
		return url.toString();
	}
	
	private void doReturnMsg(HttpServletResponse response, String returnMsg)
			throws IOException {
		response.getOutputStream().write(returnMsg.getBytes());
	}
}
