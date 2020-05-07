package net.zdsoft.webservice.action;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.webservice.binding.BindingService;
import net.zdsoft.webservice.binding.BindingServiceImplService;

import org.apache.axis.client.Service;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.huawei.eidc.slee.security.Base64;
import com.huawei.eidc.slee.security.DESTools;
import com.huawei.eidc.slee.security.MD5;

@Controller
@RequestMapping(value = "/mobile/open/webservice")
/**
 * 测试移动订购的业务逻辑
 * @author user
 *
 */
public class TestWebServiceAction {
	public static final String TEST_KEY = "B573C576D870082B";
	@ResponseBody
	@RequestMapping(value="/binding/test")
	public String bindingTest(String[] mobile,String type) {
		JSONObject result = new JSONObject();
		if(mobile==null||mobile.length==0){
			result.put("code", -1);
			result.put("message", "param error");
			return result.toJSONString();
		}
		String staffBindReq = encode(TEST_KEY, mobile, type);
		 try {
	          String address = Evn.getWebUrl()+"/services/IfAPService";
	          Service service = new org.apache.axis.client.Service();
	          org.apache.axis.client.Call call = null;
	          call = (org.apache.axis.client.Call) service.createCall();
	          call.setOperationName(new javax.xml.namespace.QName("http://ap.eidc.huawei.com/", "staffBinding"));
	          call.setTargetEndpointAddress(new java.net.URL(address));
	          String ret = (String) call.invoke(new Object[] {staffBindReq});
	          System.out.println(ret);
	      } catch (Exception ex) {
	          ex.printStackTrace();
	      }
		result.put("code", 0);
		result.put("message", "ok");
		return result.toJSONString();
	}
	
	/**
	 * 消息打包过程 将消息头和加密后的消息体放在一起组成整个消息串
	 * 
	 * @param desSecret
	 *            String 密钥
	 * @return String 加密后的消息串
	 */
	public String encode(String desSecret,String[] mobile,String type) {
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element msg = document.addElement("StaffBindReq");
		Element head = msg.addElement("HEAD");
		head.addElement("CODE").addText("103");
		head.addElement("SID").addText("00001");
		head.addElement("TIMESTAMP").addText(
				String.valueOf(System.currentTimeMillis()));
		head.addElement("SERVICEID").addText("01010101");
		msg.addElement("BODY").setText(encodeBody(desSecret,mobile,type));
		String Message = getXMLStr(document);

		System.out.println();
		System.out.println("打包好的消息：");
		System.out.println(Message);
		return Message;
	}

	/**
	 * 组织消息体实例,以一段简单的XML为例
	 * 
	 * @param desSecret
	 *            String 密钥
	 * @return String 加密后的消息体字符串
	 */
	public String encodeBody(String desSecret,String[] mobile,String type) {
		org.dom4j.Document document = DocumentHelper.createDocument();
		Element body = document.addElement("BODY");

		Element corp = body.addElement("CORPACCOUNT");
		corp.addText("12345678912345678912345678996584");
		Element staffList = body.addElement("STAFFLIST");
		Element staffinfo = staffList.addElement("STAFFINFO");
		int index=0;
		for(String mb:mobile){
			if(index>0){
				staffinfo = staffList.addElement("STAFFINFO");
			}
			staffinfo.addElement("UFID")
			.addText(UuidUtils.generateUuid());
			staffinfo.addElement("STAFFNAME").addText("测试"+index);
			staffinfo.addElement("STAFFMOBILE").addText(mb);
			staffinfo.addElement("USERTYPE").addText("1");
			staffinfo.addElement("OPTYPE").addText(type);
			staffinfo.addElement("OPNOTE").addText("测试数据"+index);
			index++;
		}

		System.out.println();
		System.out.println("组织好的消息体XML文档：");
		System.out.println(getXMLStr(document));
		return encrypt(getXMLStr(document), desSecret);
	}

	/**
	 * 传入XML格式的文档对象，返回转换为UTF8格式的字符串
	 * 
	 * @param document
	 *            Document XML格式的文档对象
	 * @return String 转换为UTF8格式的字符串
	 */
	public static String getXMLStr(org.dom4j.Document document) {
		try {
			java.io.StringWriter sw = new java.io.StringWriter();
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding("UTF8");
			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(sw,
					format);
			xmlWriter.write(document);
			xmlWriter.close();
			return sw.getBuffer().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 对字符串进行加密 1. 数字签名保证信息完整性 2. 3DES加密保证不可阅读性 3. BASE64编码 Base64( 3DES( MD5(
	 * 消息体 ) + 消息体)
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public String encrypt(String str, String desSecret) {
		String body = null;

		try {
			DESTools des = DESTools.getInstance(desSecret);
			System.out.println("加密前的消息体:");
			System.out.println(str);
			String md5str = MD5.md5(str) + str;
			System.out.println("MD5后的字符串:");
			System.out.println(md5str);
			byte[] b = des.encrypt(md5str.getBytes("UTF8"));
			System.out.println("DES后的字节数组:");
			body = Base64.encode(b);
			System.out.println("Base64后的字符串:");
			System.out.println(body);
		} catch (Exception ex) {

		}
		return body;
	}
}
