package net.zdsoft.framework.entity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletRequest;

import org.apache.commons.io.IOUtils;

public class HttpHelper {    
    /**  
     * 获取请求Body  
     *  
     * @param request  
     * @return  
     */    
    public static byte[] getBodyString(ServletRequest request) {    
        byte[] buffer = null;
        InputStream inputStream = null;    
        try {   
            inputStream = request.getInputStream();
            DataInputStream dataInStream = new DataInputStream(inputStream);
            buffer = IOUtils.toByteArray(dataInStream);
        } catch (IOException e) {    
            e.printStackTrace();    
        } finally {    
            if (inputStream != null) {    
                try {    
                    inputStream.close();    
                } catch (IOException e) {    
                    e.printStackTrace();    
                }    
            }    
        }    
        return buffer;  
    }    
}