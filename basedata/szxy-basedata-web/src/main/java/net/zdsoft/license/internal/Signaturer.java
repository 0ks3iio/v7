package net.zdsoft.license.internal;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
/**
 * 
 * @author zhangza
 *
 */
public class Signaturer {
    /**
     * 私钥签名
     * @param priKeyText
     * @param plainText
     * @return
     */
	public static byte[] sign(byte[] priKeyText, String plainText) {
		try {
			// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(priKeyText));

			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyf = KeyFactory.getInstance("RSA");

			// 取私钥匙对象
			PrivateKey prikey = keyf.generatePrivate(priPKCS8);

			// 用私钥对信息生成数字签名
			Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(prikey);
			signet.update(plainText.getBytes());
			byte[] signed = Base64.encode(signet.sign());
			return signed;
		} catch (java.lang.Exception e) {
			System.out.println("签名失败");
			e.printStackTrace();
		}
		return null;
	}

    /**
     * 私钥签名
     * @param priKeyText
     * @param plainText
     * @return
     */
	public static byte[] sign(byte[] priKeyText, byte[] plainText) {
		try {
			// 构造PKCS8EncodedKeySpec对象
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(priKeyText));

			// KEY_ALGORITHM 指定的加密算法
			KeyFactory keyf = KeyFactory.getInstance("RSA");

			// 取私钥匙对象
			PrivateKey prikey = keyf.generatePrivate(priPKCS8);

			// 用私钥对信息生成数字签名
			Signature signet = java.security.Signature.getInstance("MD5withRSA");
			signet.initSign(prikey);
			signet.update(plainText);
			byte[] signed = Base64.encode(signet.sign());
			return signed;
		} catch (java.lang.Exception e) {
			System.out.println("签名失败");
			e.printStackTrace();
		}
		return null;
	}
}
