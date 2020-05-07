package net.zdsoft.desktop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import net.zdsoft.framework.utils.RedisUtils;

import org.apache.commons.logging.Log;

import com.iflytek.edu.ew.session.SerialiableSession;
import com.iflytek.edu.ew.session.SessionProvider;

public class RedisSessionProvider implements SessionProvider {
	
	protected final Log log = org.apache.commons.logging.LogFactory.getLog(getClass());
	public Map<String, Object> getSession(String key, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	{
		 try
		 {
			  String serialisedSession = RedisUtils.get("session:" + key);
			  SerialiableSession serialiableSession = (SerialiableSession)fromString(serialisedSession);
			  return serialiableSession.getAttributes();
		 }catch (Exception ex) {}
	   return null;
	 }
	
	   public void saveSession(String key, Map<String, Object> session, int timeout, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	  {
	     try
	     {
	       SerialiableSession serialiableSession = new SerialiableSession();
	       serialiableSession.setAttributes(session);
	       if (timeout > 0) {
	    	   RedisUtils.set("session:" + key, toString(serialiableSession), timeout);
	      }
	       else {
	    	   RedisUtils.set("session:" + key, toString(serialiableSession));
	       }
	     }
	     catch (Exception ex)
	     {
	       log.error("无法将session序列化进redis，异常信息为：" + ex.toString());
	     }
	   }

	   public void removeSession(String key, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
	   {
		   RedisUtils.del(new String[] { "session:" + key });
	   }

	   private Object fromString(String string)
	     throws IOException, ClassNotFoundException
	   {
	    byte[] data = DatatypeConverter.parseBase64Binary(string);
	     ObjectInputStream objectInputStream = new ObjectInputStream(new java.io.ByteArrayInputStream(data));
	     Object object = objectInputStream.readObject();
	    objectInputStream.close();
	     return object;
	   }
	 
	   private String toString(Serializable object)
	     throws IOException
	   {
	     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	     ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
	     objectOutputStream.writeObject(object);
	     objectOutputStream.close();
	     return new String(DatatypeConverter.printBase64Binary(byteArrayOutputStream.toByteArray()));
	   }

}
