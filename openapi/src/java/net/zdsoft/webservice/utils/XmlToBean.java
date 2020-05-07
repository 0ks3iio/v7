package net.zdsoft.webservice.utils;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlToBean {
    
    /**
     * xml文件配置转换为对象
     * @param xmlPath  xml文件路径
     * @param load    java对象.Class
     * @return    java对象
     * @throws JAXBException    
     * @throws IOException
     */
    public static Object xmlToBean(String xml,Class<?> load) throws JAXBException, IOException{
        JAXBContext context = JAXBContext.newInstance(load);  
        Unmarshaller unmarshaller = context.createUnmarshaller(); 
        Object object = unmarshaller.unmarshal(new StringReader(xml));
        return object;
    }
    
    public static void main(String[] args) throws IOException, JAXBException {
    	
    }
}