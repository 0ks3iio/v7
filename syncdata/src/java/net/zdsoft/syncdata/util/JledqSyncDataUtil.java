package net.zdsoft.syncdata.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.zdsoft.framework.utils.FileUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class JledqSyncDataUtil {
	private static Map<String, String> propertyMap;
	static {
		propertyMap = new HashMap<String, String>();
		try {
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource("classpath:conf/spring/custom/jledq.properties");
			if(resource != null && resource.exists()) {
				Properties prs = FileUtils.readProperties(
						resource.getInputStream());
				Set<String> keys = prs.stringPropertyNames();
				for(String key : keys) {
					propertyMap.put(key, prs.getProperty(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static String encodeBase64(byte[]input) throws Exception{
		Class<?> clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		 Object retObj=mainMethod.invoke(null, new Object[]{input});
		 return (String)retObj;
	}
 
	public static byte[] decodeBase64(String input) throws Exception{
		Class<?> clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod= clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		Object retObj=mainMethod.invoke(null, input);
		return (byte[])retObj;
	}
    
    public static String encryption(byte[] bytes, String keyStr) {
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return encodeBase64(cipher.doFinal(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decryption(String bytes, String keyStr) {
        try {
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "DESede");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(decodeBase64(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 解密
     * @param str
     * @return
     */
    public static String decode(String str){
    	try {
			byte[] resBytes = decryption(str, getKey());
			return new String(resBytes,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    // 获取密钥
    private static String getKey(){
    	return propertyMap.get("jledq.encodekey");
    }
    
    // 默认密码
    public static String getDefaultPwd() {
    	return propertyMap.get("jledq.pwdinit");
    }
    
    /**
     * 是否开启同步
     * @return
     */
    public static boolean isOpenSync() {
    	return BooleanUtils.toBoolean(propertyMap.get("jledq.sync"));
    }
    
    /**
     * 获取二道区教育局单位id
     * @return
     */
    public static String getEdqEduUnitId() {
    	return propertyMap.get("jledq.top.unitId");
    }
    
    public static <O, K, V> Map<String, V> getMap(List<O> os, String keyXpath, String valueXpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<String, V>();
		}
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		List<String> keys = parser
				.parseExpression("#list.![#this" + (StringUtils.isNotBlank(keyXpath) ? ("." + keyXpath) : "") + "]")
				.getValue(context, List.class);
		List<V> values;
		if (StringUtils.isBlank(valueXpath)) {
			values = parser.parseExpression("#list.![#this]").getValue(context, List.class);
		} else {
			values = parser.parseExpression("#list.![#this." + valueXpath + "]").getValue(context, List.class);
		}
		Map<String, V> map = new HashMap<String, V>();
		if (values.size() == keys.size()) {
			for (int i = 0; i < keys.size(); i++) {
				map.put(keys.get(i).toLowerCase(), values.get(i));
			}
		}
		return map;
	}
}
