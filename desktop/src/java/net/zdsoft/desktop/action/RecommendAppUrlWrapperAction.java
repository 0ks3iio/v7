package net.zdsoft.desktop.action;
import static net.zdsoft.desktop.constant.DeskTopConstant.WENCHUAN_YI_XUE_YUN_AP;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import net.zdsoft.careerplan.remote.service.PaymentDetailsRemoteService;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.RhKeyUnit;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.SysUserBind;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.ClassTeachingRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.RhKeyUnitRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.SysUserBindRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.desktop.constant.DeployRegion;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.utils.Protocol;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ShengySign;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.entity.ServerRegion;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRegionRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;

/**
 * @author shenke
 * @since 2017.05.17
 */
@Controller
@RequestMapping("/recommendApp/wrapper")
public class RecommendAppUrlWrapperAction extends DeskTopBaseAction {

    private Logger logger = Logger.getLogger(RecommendAppUrlWrapperAction.class);

    @Autowired private ServerRemoteService serverRemoteService;
    @Autowired private TeacherRemoteService teacherRemoteService;
    @Autowired private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired private SemesterRemoteService semesterRemoteService;
    @Autowired private ClassRemoteService classRemoteService;
    @Autowired private CourseRemoteService courseRemoteService;
    @Resource
    private ServerRegionRemoteService serverRegionRemoteService;
    @Resource
    private RegionRemoteService regionRemoteService;
    @Resource
    private PaymentDetailsRemoteService paymentDetailsRemoteService;
    @Resource
    private SysUserBindRemoteService sysUserBindRemoteService;
    @Resource
    private SystemIniRemoteService systemIniRemoteService;

    @RequestMapping("/{serverId}")
    @ControllerInfo("第三方AP{serverId}")
    public String exucute(@PathVariable("serverId") Integer serverId, ModelMap map) {
        Server server = SUtils.dc(serverRemoteService.findOneById(serverId), Server.class);
        if (server != null) {
        	//海口党政oa 
        	String	deployRegion = systemIniRemoteService.findValue(DeployRegion.SYS_OPTION_REGION);
        	if(DeployRegion.DEPLOY__HAIKOU.equals(deployRegion) && StringUtils.isNotBlank(server.getCode()) && server.getCode().equals("dangZhengoa")){
				SysUserBind userBind = SUtils.dc(sysUserBindRemoteService.findByUserId(getLoginInfo().getUserId()), SysUserBind.class);
				String  xurl = server.getUrlIndex(false);
				if(userBind != null){
					xurl = xurl + "?username=" + userBind.getRemoteUsername()
							+ "&password=" + userBind.getRemotePassword();
				}
				return "redirect:" + xurl;
			}
            //四川汶川定制
            Map<String,String> parameters = UrlUtils.getParameters(server.getIndexUrl());
            if ( DeskTopConstant.WENCHUAN_YI_XUE_YUN_AP.containsKey(parameters.get("appId")) ) {
                return "redirect:" + getWenChuanRedirectUrl(server, parameters.get("appId"));
            }
            else if(StringUtils.equalsIgnoreCase(parameters.get("rhapp"), "ronghou")) {
            	//容厚定制 首页地址包括rhapp=ronghou
            	LoginInfo loginInfo = getLoginInfo();
            	String unitId=loginInfo.getUnitId();
            	String url = null;  
            	//org.codehaus.xfire.client.Client client = null;  
            	
                String method=null;
                Object[] obj=null;
                String rhip=null;
                RhKeyUnitRemoteService rhKeyUnitRemoteService = Evn.getBean("rhKeyUnitRemoteService");
                if(rhKeyUnitRemoteService!=null){
                	List<RhKeyUnit> keyList = SUtils.dt(rhKeyUnitRemoteService.findByUnitIdAnduKeyId(unitId, null), new TR<List<RhKeyUnit>>() {});
                    if(CollectionUtils.isNotEmpty(keyList)) {
                    	rhip=keyList.get(0).getServerIp();
                    }
                }
                
                if(StringUtils.isBlank(rhip)) {
                	map.put("errorMess", "服务器参数未配置，请联系管理员");
                	return "/desktop/error.ftl";
                }
                String port=null;
                String[] parm=null;
            	try {
	            	if(Objects.equals(loginInfo.getUserType(), 1)) {
	            		//学生
	            		url="http://"+rhip+":8013/newsvc/WebSvc.asmx?WSDL";
	            		method="SSO_Student";
	            		port="8013";
	            		parm=new String[] {"username","pswd"};
	            		obj=new Object[] {loginInfo.getUserName(),"$##OOJJkkfiur586ER" };
//	            		obj=new Object[] {"156010583","$##OOJJkkfiur586ER" };
	            		
	            	}else if(Objects.equals(loginInfo.getUserType(), 2)){
	            		//教师
	            		url="http://"+rhip+":8011/services/MainService.asmx?WSDL";
	            		method="SSO_Teacher";
	            		port="8011";
	            		parm=new String[] {"userid","pswd"};
//	            		obj=new Object[] { "lishi5","58jgguiJUfghr#$kkf" };
	            		obj=new Object[] {loginInfo.getUserName(),"58jgguiJUfghr#$kkf" };
	            	}else {
	            		 return "/desktop/error.ftl";
	            	}
	            	
	            	String result = ronghouWebService(url, method, parm, obj);
//                    client = new org.codehaus.xfire.client.Client(url);
//                    String result = (String) client.invoke(method,obj)[0];  //new object[]{"100","200"} 
	            	// 学生身份不存在/unlicense.aspx
                    if(StringUtils.isNotBlank(result)) {
                    	if(result.contains("unlicense.aspx")) {
                    		map.put("errorMess", "身份不存在");
                        	return "/desktop/error.ftl";
                    	}
                    	String resultUrl="http://"+rhip+":"+port+result;
                    	//System.out.println(resultUrl);
                    	return "redirect:" + resultUrl;
                    } else {
                    	map.put("errorMess", "用户数据没有同步，请联系管理员");
                    	return "/desktop/error.ftl";
                    }
                    
                 }  catch (Exception e) {  
                     e.printStackTrace();  
                 }
            }
            else if(StringUtils.equalsIgnoreCase(parameters.get("yapp"), "yzyapp")) {
            	//优志愿
            	//secretKey 对应的B端秘钥(该项由我们独立提供)
            	//sign 签名（时间加密）详见加密规则
            	LoginInfo loginInfo = getLoginInfo();
            	//根据user获取regin
            	String regionCode=loginInfo.getRegion();
            	Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
            	if(region==null) {
            		//行政区域码不对
            		return "/desktop/error.ftl";
            	}
            	String reginName=region.getFullName();
            	Map<String, String> provinceMap = ShengySign.provinceMap;
            	String provinceNum="";
            	for(Entry<String, String> kk:provinceMap.entrySet()) {
            		if(reginName.contains(kk.getKey())) {
            			provinceNum=kk.getValue();
            			break;
            		}
            	}
            	if(StringUtils.isBlank(provinceNum)) {
            		//省匹配不上
            		return "/desktop/error.ftl";
            	}
            
            	
            	//是不是已经购买vip 
            	String operTypeValue="&operType=6&operValue=0";
            	boolean isVip=paymentDetailsRemoteService.checkByOrderTypeAndUserIdWithMaster("01", loginInfo.getUserId());;
            	if(isVip) {
            		//VIP用户
            		operTypeValue="&operType="+ Evn.getCreeplanDjStatic()+"&operValue=1";
            	}
            	String relaname = UrlUtils.encode(loginInfo.getRealName(),"utf-8");
            	//openNickName 真实姓名
            	String parmUrl="&openUsername="+loginInfo.getUserName()+"&openUserProvince="+provinceNum+
            			"&openUserCity=&openUserArea=&openUserSchool=&openNickName="+relaname+operTypeValue+"&operData=1&isH5=0&remark=";
            	String url=server.getUrl()+"openAuth/login?secretKey=2a532915f2d60fc9b875ea7e335f2fea" + 
            			"&sign="+ShengySign.getShengySign()+"&openUserId=" + loginInfo.getUserId()+parmUrl;
            	return "redirect:" + url;
            	
            }
            String region = serverRegionRemoteService.findRegionByDomain(getRequest().getServerName());
            if (StringUtils.isNotBlank(region)) {
                ServerRegion serverRegion = SUtils.dc(serverRegionRemoteService.findByRegionAndServerId(region, serverId), ServerRegion.class);
                if (serverRegion == null) {
                    return "redirect:" + server.getUrlIndex(false);
                }
                String index = serverRegion.getIndexUrl();
                if(StringUtils.startsWithIgnoreCase(index, Protocol.HTTP.getValue())) {
                    return "redirect:" + index;
                } else {
                    return "redirect:" + serverRegion.getProtocol() + "://"
                            + serverRegion.getDomain() + ":" +serverRegion.getPort()
                            + serverRegion.getContextPath() + "/" + index;
                }
            }
            return "redirect:"+server.getUrlIndex(false);
        }
       
        return "/desktop/error.ftl";
    }

    public String ronghouWebService(String url,String method,String[] parm,Object[] obj ) {
    	
    	Service sv = new Service();  
   	 
        //创建一个call对象
        Call call=null;
		try {
			call = (Call) sv.createCall();
			call.setUseSOAPAction(true);
	        call.setSOAPActionURI("http://tempuri.org/"+method);
		} catch (ServiceException e2) {
			e2.printStackTrace();
			return null;
		}  
        
        //设置要调用的接口地址
        try {
			call.setTargetEndpointAddress(new java.net.URL(url));
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		} 
        //设置要调用的接口方法 
        call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/",method));  
        //参数
        for(String s:parm) {
        	 call.addParameter( new  javax.xml.namespace.QName("http://tempuri.org/",s),  
            		    org.apache.axis.encoding.XMLType.XSD_STRING,   
            		    javax.xml.rpc.ParameterMode.IN);   
        }
        //返回参数类型
        call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
       
        
        //开始调用方法并返回相应数据信息，以xml格式的字符串返回，也可以json格式主要看对方用什么方式返回
        String result=null;
		try {
			result = (String)call.invoke(obj);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
        System.out.println(result);//打印字符串
        return result;
    }

    private String getWenChuanRedirectUrl(Server server, String appId) {
        try {
            String redirectURL = RedisUtils.get("WENCHUAN.REDIRECT.URL." + server.getId() + "." + getSession().getId());
            if ( StringUtils.isNotBlank(redirectURL) ) {
                return redirectURL;
            }
            String TOKEN = "WENCHUAN.ACCESS.TOKEN" + appId;
            String token = RedisUtils.get(TOKEN);
            int expireTime = 10000;
            if ( StringUtils.isBlank(token) ) {
                Pair<String, Integer> pair = getWenChuanAccessToken(server, TOKEN, appId);
                token = pair.getKey();
                expireTime = pair.getRight();
            }
            if ( StringUtils.isBlank(token) ) {
                return server.getUrl();
            }
            //正常
            StringBuffer redirectUrl = new StringBuffer();
            //教师
            if ( new Integer(User.OWNER_TYPE_TEACHER).equals(getLoginInfo().getOwnerType()) ) {
                Pair<String, String> pair = getWenChuanSubjectIdAndSection();
                redirectUrl.append(DeskTopConstant.WENCHUAN_PASSPORT_URL_TEACHER)
                        .append("?subjectId=").append(pair.getLeft())
                        .append("&schoolPhaesId=").append(pair.getRight()).append("&");

            }
            if ( new Integer(User.OWNER_TYPE_STUDENT).equals(getLoginInfo().getOwnerType()) ) {
                redirectUrl.append(DeskTopConstant.WENCHUAN_PASSPORT_URL_STUDENT).append("?");
            }
            User user = SUtils.dc(userRemoteService.findOneById(getLoginInfo().getUserId()), User.class);
            redirectUrl.append("loginName=")
                    .append(getLoginInfo().getUserName())
                    .append("&username=").append(URLEncoder.encode(user.getRealName(), "UTF-8"))
                    .append("&accessToken=").append(token)
                    .append("&appId=").append(appId)
                    .append("&Returnurl=").append(server.getUrl());
            RedisUtils.set("WENCHUAN.REDIRECT.URL." + server.getId() + "." + getSession().getId(), redirectUrl.toString(), (int)(expireTime/1000));
            logger.warn("realname:" + user.getRealName());
            return redirectUrl.toString();
        } catch (Exception e){
            logger.error("获取汶川第三方AP登录URL失败ServerId {"+server.getId()+"}", e);
            return server.getUrl();
        }
    }

    private Pair<String, Integer> getWenChuanAccessToken(Server server, String redisKey, String appId) {
        Pair<String, Integer> pair;
        String token = null;
        long expireTime = 10000;
        try {
            JSONObject result = JSON.parseObject(UrlUtils.readContent(DeskTopConstant.WENCHUAN_GET_TOKEN_URL + "?appId=" + appId + "&Appsecret=" + WENCHUAN_YI_XUE_YUN_AP.get(appId), getSession().getId()));
            if ( result.getBoolean("State") ) {
                JSONObject re = result.getJSONObject("Result");
                token = re != null ? re.getString("AccessToken") : null;
                Date expireDate = DateUtils.parseDate(re.getString("ExpireTime"),"yyyy-MM-dd HH:mm");
                expireTime = expireDate.getTime();
                //在对方给定的基础之上提前10分钟
                expireTime = expireTime - System.currentTimeMillis() - 10 * 60 * 1000;
            }
            if ( token == null ) {
                logger.error("汶川AccessToken获取失败");
                //return server.getUrl();
            }
            RedisUtils.set(redisKey, token, (int)(expireTime/1000));
            pair = Pair.of(token, (int)(expireTime/1000));
        } catch (IOException e) {
            pair = Pair.of(null, 0);
        } catch (ParseException e) {
            pair = Pair.of(token, (int)expireTime/1000);
        }
        return pair;
    }

    private Pair<String, String> getWenChuanSubjectIdAndSection() {
        Teacher teacher = SUtils.dc(teacherRemoteService.findOneById(getLoginInfo().getOwnerId()), Teacher.class);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(1, getLoginInfo().getUnitId()), Semester.class);
        List<ClassTeaching> teachings = SUtils.dt(
                classTeachingRemoteService.findClassTeachingList(getLoginInfo().getUnitId(), semester.getAcadyear(), Objects.getVaule(semester.getSemester(),""), teacher.getId())
                , ClassTeaching.class);
        Set<String> subjectIds = EntityUtils.getSet(teachings, ClassTeaching::getSubjectId);
        Set<String> classIds   = EntityUtils.getSet(teachings, ClassTeaching::getClassId);
        if ( subjectIds != null && subjectIds.size() > 0 ) {
            logger.warn("该教师教有多门学科！");
        }
        String subjectId = subjectIds != null || subjectIds.isEmpty() ? "13" : subjectIds.toArray(new String[subjectIds.size()])[0];
        String section = Objects.getVaule(teacher.getSection(), "");
        if ( teacher.getSection() == null ) {

            List<Clazz> clazzes = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[classIds.size()])), Clazz.class);
            Set<Integer> sections = EntityUtils.getSet(clazzes, Clazz::getSection);
            sections.remove(0); //幼儿园
            sections.remove(9); //剑桥高中
            if ( sections.size() > 0 ){
                section = sections.toArray(new Integer[sections.size()])[0].toString();
            }
        }
        Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class );
        if ( course != null ) {
            for (Map.Entry<String, Integer> entry : DeskTopConstant.WENCHUAN_SUBJECT_MAP.entrySet()) {
                if (StringUtils.contains(course.getSubjectName(), entry.getKey())) {
                    subjectId = entry.getValue().toString();
                    break;
                }
            }
        } else {
            subjectId = "10";
        }
        return Pair.of(subjectId, section);
    }


}