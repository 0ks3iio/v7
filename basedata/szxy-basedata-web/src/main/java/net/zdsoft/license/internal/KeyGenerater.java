/**
 *
 * 现在很多J2EE应用都采用一个license文件来授权系统的使用，
 * 特别是在系统购买的早期，会提供有限制的license文件对系统
 * 进行限制，比如试用版有譬如IP、日期、最大用户数量的限制等。   
 * 
 * 而license控制的方法又有很多，目前比较流行，只要设计的好
 * 就很难破解的方法就是采用一对密匙（私匙加密公匙解密）来生
 * 成License文件中的Sinature签名内容，再通过Base64或Hex来
 * 进行编码。比如原BEA公司现在是Oracle公司的WebLogic就采用
 * 的是这种方法来设置License文件。   
 * 
 * 这里只进行一个比较简单的实现：   
 * 
 * 一共三个类：   
 * A.KeyGenerater类生成公钥私钥对   
 * B.Signaturer类使用私钥进行签名   
 * C.SignProvider类用公钥验证   
 * 
 */
package net.zdsoft.license.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class KeyGenerater {
	private byte[] priKey;
	private byte[] pubKey;

	public void generater() {
		try {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			SecureRandom secrand = new SecureRandom();
			// secrand.setSeed("www.zdsoft.net-www.winupon.com".getBytes()); //
			// ��ʼ����������
			keygen.initialize(1024, secrand);
			KeyPair keys = keygen.genKeyPair();
			PublicKey pubkey = keys.getPublic();
			PrivateKey prikey = keys.getPrivate();
			pubKey = Base64.encode(pubkey.getEncoded());
			priKey = Base64.encode(prikey.getEncoded());
		} catch (Exception e) {
			System.out.println("生成密钥对失败");
			e.printStackTrace();
		}
	}

	public byte[] getPriKey() {
		return priKey;
	}

	public byte[] getPubKey() {
		return pubKey;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		String plainText = "天天向上";

		KeyGenerater kg = new KeyGenerater();
		kg.generater();

		System.out.println("私钥：" + new String(kg.getPriKey()));
		System.out.println("公钥：" + new String(kg.getPubKey()));
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			//Thread.currentThread().getContextClassLoader();
			in = ClassLoader.getSystemResourceAsStream("license.txt");
			
			out = new ByteArrayOutputStream();
			byte[] buffer = new byte[128];
			int iLength = 0;
			while ((iLength = in.read(buffer)) != -1) {
				out.write(buffer, 0, iLength);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)  in.close();
				if (out != null)  out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File f = new File("license.txt");
		System.out.println(f.getAbsolutePath());
		
		System.out.println("读取的文件："+new String(out.toByteArray()));

		byte[] signText = Signaturer.sign(kg.getPriKey(), plainText);

		boolean b = SignProvider.verify(kg.getPubKey(), plainText, signText);
		System.out.println("result:" + b);
		
	}

}
