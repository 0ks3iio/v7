package net.zdsoft.officework.action;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.officework.event.CallbackCls;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import LovicocoAPParser.Interface.IParser;
import LovicocoAPParser.Provider.APProvider;

/**
 * 获取健康热点数据
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sgi_bin/device_entry")
public class TBHealthAPAction extends BaseAction {
	@RequestMapping("/entry.aspx")
	@ResponseBody
	public void doRequest(HttpServletRequest request,
			HttpServletResponse response, String str) {
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			DataInputStream dataInStream = new DataInputStream(inputStream);
			outputStream = response.getOutputStream();		
			byte[] message = IOUtils.toByteArray(dataInStream);
			
			APProvider provider = new APProvider(new CallbackCls());
			IParser parser = provider.CreateGenernalParser();
			byte[] bout = parser.Execute(message);
			response.setContentType("application/xhtml+xml; charset=UTF-8");
			outputStream = response.getOutputStream();
			outputStream.write(bout, 0, bout.length);
			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
				outputStream = null;
			}
		}
	}

}
