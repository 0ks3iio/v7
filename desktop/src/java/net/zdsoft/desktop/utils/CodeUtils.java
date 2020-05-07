package net.zdsoft.desktop.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.framework.utils.RedisUtils;

public class CodeUtils {
	private static Color getRandColor(Random random, int fc, int bc){
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	/**
	 * 获得问答式的验证码
	 * @param redisKey 缓存key
	 * @param redisTime 缓存时间
	 * @return 问答式的验证码问题
	 * 答案放入缓存中
	 */
	public static String getQuestionCode(String redisKey,int redisTime) {
		Random random = new Random();
		int intTemp;
		int intFirst = random.nextInt(100);
		int intSec = random.nextInt(100);
		String checkCode = "";
		int result = 0;
		switch (random.nextInt(6)) {
		case 0:
			if (intFirst < intSec) {
				intTemp = intFirst;
				intFirst = intSec;
				intSec = intTemp;
			}
			checkCode = intFirst + " - " + intSec + " = ?";
			result = intFirst-intSec;
			break;
		case 1:
			if (intFirst < intSec) {
				intTemp = intFirst;
				intFirst = intSec;
				intSec = intTemp;
			}
			checkCode = intFirst + " - ? = "+(intFirst-intSec);
			result = intSec;
			break;
		case 2:
			if (intFirst < intSec) {
				intTemp = intFirst;
				intFirst = intSec;
				intSec = intTemp;
			}
			checkCode = "? - "+intSec+" = "+(intFirst-intSec);
			result = intFirst;
			break;
		case 3:
			checkCode = intFirst + " + " + intSec + " = ?";
			result = intFirst + intSec;
			break;
		case 4:
			checkCode = intFirst + " + ? ="+(intFirst+intSec);
			result = intSec;
			break;
		case 5:
			checkCode = "? + " + intSec + " ="+(intFirst+intSec);
				result = intFirst;
				break;
		}
		if(redisTime==0){
			redisTime = 60;
		}
		
		if(StringUtils.isNotBlank(redisKey)){
//			System.out.println(redisKey);
//			System.out.println(redisTime);
			RedisUtils.set(redisKey,result+"",redisTime);
		}

//		if(request!=null){
//			RedisUtils.set(DeskTopConstant.REGISTER_CODE_CACHE_KEY+"_"+request.getSession().getId(), result+"",60*2);
//		}
		return checkCode;
	}
	
	/**
	 * 随机验证码 (数字+字母) 区分大小写
	 * @param length 验证码长度
	 * @return
	 */
	public static  String getNumberWithaACode(int length){
		if(length <= 0){
			throw new IllegalArgumentException("验证码长度非负");
		}
		
		StringBuilder code = new StringBuilder();
		
		for( int i=0; i<length; i++){
			code.append(DeskTopConstant.VERIFY_NUMBER_aA_CODE.charAt(RandomUtils.nextInt(0,DeskTopConstant.VERIFY_NUMBER_aA_CODE.length()-1)));
		}
		
		return code.toString();
	}
	
	/**
	 * 随机验证码 (数字+字母) 区分大小写
	 * @param length 验证码长度
	 * @return
	 */
	public static  String getNumberWithaCode(int length){
		if(length <= 0){
			throw new IllegalArgumentException("验证码长度非负");
		}
		
		StringBuilder code = new StringBuilder();
		
		for( int i=0; i<length; i++){
			code.append(DeskTopConstant.VERIFY_NUMBER_a_CODE.charAt(RandomUtils.nextInt(0,DeskTopConstant.VERIFY_NUMBER_a_CODE.length()-1)));
		}
		
		return code.toString();
	}
	
	/**
	 * 随机验证码 (数字+字母) 区分大小写
	 * @param length 验证码长度
	 * @return
	 */
	public static  String getNumberCode(int length){
		if(length <= 0){
			throw new IllegalArgumentException("验证码长度非负");
		}
		
		StringBuilder code = new StringBuilder();
		
		for( int i=0; i<length; i++){
			code.append(DeskTopConstant.VERIFY_NUMBER_CODE.charAt(RandomUtils.nextInt(0,DeskTopConstant.VERIFY_NUMBER_CODE.length()-1)));
		}
		
		return code.toString();
	}
	
	private static BufferedImage createImage(String code,int width,int height){
		if(width==0){
			width = 140;
		}
		if(height==0){
			height = 30;
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
	
		Random random = new Random();
	
		g.setColor(getRandColor(random, 200, 250));
		g.fillRect(0, 0, width, height);
		
		String[] fontTypes = { "\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66" };
		int fontTypesLength = fontTypes.length;
		
		g.setColor(getRandColor(random, 160, 200));
		g.setFont(new Font("Times New Roman", Font.PLAIN, 14 + random.nextInt(6)));
		
		for (int i = 0; i < 255; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
	
		String [] baseChar = code.split(" ");
		for (int i = 0; i < baseChar.length; i++) {
			g.setColor(getRandColor(random, 30, 150));
			g.setFont(new Font(fontTypes[random.nextInt(fontTypesLength)], Font.BOLD, 22 + random.nextInt(6)));
			g.drawString(baseChar[i], 24 * i + 10, 24);
		}
		g.dispose();
		return image;
	}
	
	/**
	 * 
	 * @param code 生成的验证码信息
	 * @param out 输出流
	 * @param width 宽
	 * @param height 高
	 */
	public static void export(String code, OutputStream out,int width,int height){
	    Assert.hasLength(code);
	    Assert.notNull(out);
	    try {
	        BufferedImage image = createImage(code,width,height);
	        ImageIO.write(image, "JPEG", out);
	        out.flush();
	        out.close();
	    } catch (Exception e){
	        e.printStackTrace();
	    }
	}
	
	/**
	 * 
	 * @param redisKey 缓存key
	 * @param redisTime 缓存时间
	 * @param out 输出流
	 * @param width 图片宽
	 * @param height 图片高
	 */
	public static void export(String redisKey,int redisTime, OutputStream out,int width,int height){
		RedisUtils.del(redisKey);
//		System.out.println("---"+redisKey);
		String code = getQuestionCode(redisKey, redisTime);
		export(code, out, width, height);
//		export(redisKey, redisTime, out, width, height);
	}
	
	public static void  export(HttpServletRequest request,HttpServletResponse response,String redisKey,int redisTime,int width,int height){
		
		if(StringUtils.isEmpty(redisKey)){
			redisKey = DeskTopConstant.REGISTER_CODE_CACHE_KEY+"_"+request.getSession().getId();
		}
//		System.out.println(redisKey);
		try {
			ServletOutputStream out =response.getOutputStream();
			export(redisKey, redisTime, out, width, height);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		RedisUtils.set("1111", "2222",60);
		System.out.println(RedisUtils.get("desktop_register_code_key_3D933CBAE538E83F3F6C9CD8001B7808.a"));
//		try {
//			for (int i = 0; i < 5; i++) {
//				
//				CodeUtils.export(generateCheckCode(null,0), new FileOutputStream(new File("D:\\a"+i+".jpg")), 0, 0);
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        //verifyImage.export("1sd4", ));
	}
	
	// try {
	// response.setHeader("Pragma", "No-cache");
	// response.setHeader("Cache-Control", "no-cache");
	// response.setDateHeader("Expires", 0);
	//
	//
	//
	// String baseStr = generateCheckCode(request);
	//
	//
	// OutputStream os = response.getOutputStream();
	//
	//
	//
	//
	// response.flushBuffer();
	//
	// } catch (IllegalStateException e) {
	// System.out.println(e.getMessage());
	// e.printStackTrace();
	// }
}
