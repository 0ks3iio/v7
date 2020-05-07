package net.zdsoft.webservice.service.impl;

import java.util.List;

import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.MobileUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.webservice.dao.SuCdStaffBindDao;
import net.zdsoft.webservice.dto.Body;
import net.zdsoft.webservice.dto.Body.StaffInfo;
import net.zdsoft.webservice.dto.StaffBindReq;
import net.zdsoft.webservice.entity.SuCdStaffBind;
import net.zdsoft.webservice.service.SuCdStaffBindService;
import net.zdsoft.webservice.utils.XmlToBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.huawei.eidc.slee.security.Base64;
import com.huawei.eidc.slee.security.DESTools;
import com.huawei.eidc.slee.security.MD5;

@Service("suCdStaffBindService")
public class SuCdStaffBindServiceImpl extends BaseServiceImpl<SuCdStaffBind, String> implements SuCdStaffBindService {
	private static final Logger log = Logger.getLogger(SuCdStaffBindServiceImpl.class);
	public static final String KEY = "B573C576D870082B";
	public static final String PHONE_NUMBER_REG = "^1+[35678]+\\d{9}";
	
	@Autowired
	private SuCdStaffBindDao suCdStaffBindDao;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<SuCdStaffBind, String> getJpaDao() {
		return suCdStaffBindDao;
	}

	@Override
	protected Class<SuCdStaffBind> getEntityClass() {
		return SuCdStaffBind.class;
	}

	@Override
	public String dealBindMsg(String msg) {
		return decode(msg,KEY);
	}
	
	private void openAccount(Body body,String pushSysTime,String serviceId) {
		List<SuCdStaffBind> staffLists = Lists.newArrayList();
		for(Body.StaffInfo info:body.getStaffInfo()){
			SuCdStaffBind staff = new SuCdStaffBind();
			String mobile = info.getStaffMobile();
			int state = info.getOptType();
			checkAndOpt(info,mobile,state);
			staff.setId(UuidUtils.generateUuid());
			staff.setCorpAccount(body.getCorpAccount());
			staff.setOptNote(info.getOptNote()+","+info.getResultMsg());
			staff.setOptType(state);
			staff.setPushSysTime(pushSysTime);
			staff.setStaffMobile(info.getStaffMobile());
			staff.setStaffName(info.getStaffName());
			staff.setUfId(info.getUfId());
			staff.setUserType(info.getUserType());
			staff.setResultCode(info.getResultCode());
			staff.setServiceId(serviceId);
			staffLists.add(staff);
		}
		if(CollectionUtils.isNotEmpty(staffLists)){
			saveAll(staffLists.toArray(new SuCdStaffBind[staffLists.size()]));
		}
	}
	
	private void checkAndOpt(StaffInfo info,String mobile,int state) {
		if(!MobileUtils.isChinaMobile(mobile, PHONE_NUMBER_REG)|| (state!=1&&state!=5)){
			info.setResultCode(1);
			info.setResultMsg("参数错误");
		}else{
			String result = userRemoteService.openeUserdForMobile(mobile, state);
			if("0".equals(result)){
				info.setResultCode(0);
				info.setResultMsg("成功");
			}else if("2".equals(result)){
				info.setResultCode(1);
				info.setResultMsg("没有手机号为"+mobile+"的家长");
			}else{
				info.setResultCode(1);
				info.setResultMsg("业务操作失败");
			}
		}
	}

	/**
     * 对传入打包好的整个消息进行解码
     * 分别对头部和尾部进行处理
     * @param str String 打包好的消息字符串
     * @param desSecret String 密钥
     * @return boolean 返回解包成功失败
     */
    private String decode(String str, String desSecret)
    {
        if (str == null)
        {
        	log.error("bind数据为空");
            return "";
        }
        try{
            StaffBindReq msg = (StaffBindReq)XmlToBean.xmlToBean(str, StaffBindReq.class);
            String rspBody = decodeBody(msg.getBody(),desSecret,msg.getHead().getTimestamp(),msg.getHead().getServiceid());
            
            return encodeRsp(msg,rspBody);
        }
        catch(Exception ex)
        {
        	log.error("bind数据解析出错"+ex.getMessage());
        	ex.printStackTrace();
            return "";
        }
    }
    
    /**
     * 对消息体进行解包识别输出实例
     * @param str String  消息体字符串
     * @param desSecret String 密钥
     * @return boolean 解包成功失败
     */
    private String decodeBody(String str, String desSecret,String pushSysTime,String serviceid)
    {
        String bodyStr = decrypt(str,desSecret);
        if (bodyStr == null) return "";
        try{
            Body body = (Body)XmlToBean.xmlToBean(bodyStr, Body.class);
            openAccount(body, pushSysTime,serviceid);
            return encodeBody(desSecret,body);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
            return "";
        }
    }
    
    /**
     * 对字符串进行解密
     * Base64( 3DES( MD5( 消息体 ) + 消息体) 逆操作
     * @param str String
     * @return String
     * 解密时判断数据签名，如果不匹配则返回null
     */
    private String decrypt(String str,String desSecret)
    {
        String body = null;
        try {
            DESTools des = DESTools.getInstance(desSecret);
            log.info("对方传来的消息源串body："+str);
            byte[] b = Base64.decode(str);
            String md5body = new String(des.decrypt(b),"UTF8");
            log.info("3DES解密后的结果："+md5body);
            if(md5body.length()<32)
            {
            	log.error("错误！消息体长度小于数字签名长度!");
                return null;
            }
            String md5Client = md5body.substring(0,32);
            log.info("对方传来的数字签名："+md5Client);
            body = md5body.substring(32);
            log.info("对方传来的消息体："+body);
            String md5Server = MD5.md5(body);
            log.info("我方对传来消息的数字签名："+md5Server);
            if(!md5Client.equals(md5Server))
            {
                log.error("错误！数字签名不匹配:");
                return null;
            }
        } catch (Exception ex) {
        	log.error("body解析异常："+ex.getMessage());
        }
        return body;
    }
    
    public String encodeRsp(StaffBindReq req,String body)
    {
        org.dom4j.Document document = DocumentHelper.createDocument();
        Element msg = document.addElement("StaffBindRsp");
        Element head = msg.addElement("HEAD");
        head.addElement("CODE").addText(req.getHead().getCode());
        head.addElement("SID").addText(req.getHead().getSid());
        head.addElement("TIMESTAMP").addText(String.valueOf(System.currentTimeMillis()));
        head.addElement("SERVICEID").addText(req.getHead().getServiceid());
        msg.addElement("BODY").setText(body);
        String rmsg = getXMLStr(document);
        log.info("返回的消息："+rmsg);
        return rmsg;
    }
    
    /**
     * 组织消息体实例,以一段简单的XML为例
     * @param desSecret String 密钥
     * @return String 加密后的消息体字符串
     */
    public String encodeBody(String desSecret,Body reqbody)
    {
        org.dom4j.Document document = DocumentHelper.createDocument();
        Element body = document.addElement("BODY");

        Element corp = body.addElement("CORPACCOUNT");
        corp.addText(reqbody.getCorpAccount());
        Element staffList = body.addElement("STAFFLIST");
        Element staffinfo = staffList.addElement("STAFFINFO");
        int index = 0;
        for(Body.StaffInfo info:reqbody.getStaffInfo()){
        	if(index>0){
        		staffinfo = staffList.addElement("STAFFINFO");
        	}
        	staffinfo.addElement("UFID").addText(info.getUfId());
        	staffinfo.addElement("STAFFNAME").addText(info.getStaffName());
        	staffinfo.addElement("STAFFMOBILE").addText(info.getStaffMobile());
        	staffinfo.addElement("RESULTCODE").addText(info.getResultCode()+"");
        	staffinfo.addElement("RESULTMSG").addText(info.getResultMsg()==null?"":info.getResultMsg());
        	index++;
        }

        String bodyStr = getXMLStr(document);
        log.info("组织好的消息体XML文档："+bodyStr);
        return encrypt(bodyStr, desSecret);
    }
    
    /**
     * 对字符串进行加密
     * 1. 数字签名保证信息完整性
     * 2. 3DES加密保证不可阅读性
     * 3. BASE64编码
     * Base64( 3DES( MD5( 消息体 ) + 消息体)
     * @param str String
     * @return String
     */
    public String encrypt(String str,String desSecret)
    {
        String body = null;

        try {
            DESTools des = DESTools.getInstance(desSecret);
            String md5str = MD5.md5(str)+str;
            byte[] b = des.encrypt(md5str.getBytes("UTF8"));
            body = Base64.encode(b);
            log.info("Base64后的返回字符串:"+body);
        } catch (Exception ex) {
        	log.error("加密字符串失败!");
        }
        return body;
   }

    /**
     * 传入XML格式的文档对象，返回转换为UTF8格式的字符串
     * @param document Document XML格式的文档对象
     * @return String 转换为UTF8格式的字符串
     */
    public static String getXMLStr(org.dom4j.Document document)
    {
        try
        {
            java.io.StringWriter sw = new java.io.StringWriter();
            OutputFormat format = OutputFormat.createCompactFormat();
            format.setEncoding("UTF8");
            org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(sw,format);
            xmlWriter.write(document);
            xmlWriter.close();
            return sw.getBuffer().toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

}
