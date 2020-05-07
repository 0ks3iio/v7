package net.zdsoft.syncdata.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class GanSuSecurityUtil {
	    // 加载密钥文件
	    private static Key privateKey = null;

	    static {
	        ObjectInputStream ois = null;
	        try {
//	            InputStream inputStream = new FileInputStream("E:\\IdeaWorkPace\\gansu-simple-test\\src\\main\\resources\\uc.key");
//	            InputStream inputStream = new FileInputStream("classpath:gansu.key"); 
	            ResourceLoader resourceLoader = new DefaultResourceLoader();
	            Resource resource = resourceLoader.getResource("classpath:gansu.key");
	            InputStream inputStream = resource.getInputStream();
	            ois = new ObjectInputStream(inputStream);
	            privateKey = (Key) ois.readObject();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (ois != null) {
	                try {
	                    ois.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    public static String encode(final String content, final Key key) {
	        try {
	            Cipher cipher = Cipher.getInstance("AES");
	            cipher.init(Cipher.ENCRYPT_MODE, key);
	            byte[] b = cipher.doFinal(content.getBytes("utf-8"));
	            return Base64.encodeBase64String(b);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    private static String decode(final String miwen, final Key key) {
	        try {
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            return new String(cipher.doFinal(Base64.decodeBase64(miwen)), "utf-8");
	        } catch (Exception e) {
	            System.out.println("解密失败！密文：" + miwen);
	        }
	        return miwen;
	    }

	    /**
	     * 加密，仅供加密cookie和Saml令牌使用
	     *
	     * @param content
	     * @return
	     */
	    public static String jiami(final String content) {
	        return encode(content, privateKey);
	    }

	    /**
	     * 解密，仅供解密cookie和Saml令牌使用
	     *
	     * @param miwen
	     * @return
	     */
	    public static String jiemi(final String miwen) {
	        return decode(miwen, privateKey);
	    }
}
