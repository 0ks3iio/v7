package net.zdsoft.newgkelective.data.optaplanner.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlUtils {
	
	public static XStream generateXStream(Class<?> clazz){
		XStream xstream = new XStream(new DomDriver("utf-8"));
		xstream.setMode(XStream.ID_REFERENCES);
		// 如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
		xstream.processAnnotations(clazz); // 通过注解方式的，一定要有这句话
		// 忽略未知元素
//		xstream.ignoreUnknownElements();
		return xstream;
	}
	/**
	 * java 转换成xml
	 * 
	 * @Title: toXml
	 * @Description: TODO
	 * @param obj
	 *            对象实例
	 * @return String xml字符串
	 */
	public static String toXml(Object obj) {
		XStream xstream = generateXStream(obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * 将传入xml文本转换成Java对象
	 * 
	 * @Title: toBean
	 * @Description: TODO
	 * @param xmlStr
	 * @param cls
	 *            xml对应的class类
	 * @return T xml对应的class类的实例对象
	 * 
	 *         调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr,
	 *         PersonBean.class);
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String xmlStr, Class<T> cls) {
		XStream xstream = generateXStream(cls);
		T obj = (T) xstream.fromXML(xmlStr);
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toBean(Reader reader, Class<T> cls) {
		XStream xstream = generateXStream(cls);
		T obj = (T) xstream.fromXML(reader);
		return obj;
	}
	
	public static <T> T toBeanFromFile(String filePath, Class<T> cls) {
		Reader reader = null;
		try {
			
			reader = new InputStreamReader(new FileInputStream(filePath),"UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件："+filePath);
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		return toBean(reader, cls);
	}
	
	/**
	 * 写到xml文件中去
	 * 
	 * @Title: writeXMLFile
	 * @Description: TODO
	 * @param obj
	 *            对象
	 * @param absPath
	 *            绝对路径
	 * @param fileName
	 *            文件名
	 * @return boolean
	 */

	public static boolean toXMLFile(Object obj, String absPath, String fileName) {
		File file = new File(absPath, fileName);
		String filePath = file.getAbsolutePath();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("创建{" + filePath + "}文件失败!!!" + e.getMessage());
				e.printStackTrace();
				return false;
			}
		} // end if
		OutputStream ous = null;
		Writer writer = null;
		XStream xstream = generateXStream(obj.getClass());
		try {
			ous = new FileOutputStream(file);
			writer = new OutputStreamWriter(ous, "UTF-8");
			xstream.toXML(obj, writer);
			
//			ous.flush();
		} catch (Exception e) {
			System.out.println("写{" + filePath + "}文件失败!!!" + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					System.out.println("写{" + filePath + "}文件关闭输出流异常!!!" + e.getMessage());
					e.printStackTrace();
				}
		}
		return true;
	}

}
