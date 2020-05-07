package net.zdsoft.framework.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {
	public static final Logger logger = Logger.getLogger(ResponseUtil.class);

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static void doResponse(HttpServletResponse response, Object obj) {

		String jsonStr = null;
		try {
			jsonStr = objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			logger.error(e, e);
		} catch (JsonMappingException e) {
			logger.error(e, e);
		} catch (IOException e) {
			logger.error(e, e);
		}
		try {
			response.getWriter().print(jsonStr);
		} catch (IOException e) {
			logger.error(e, e);
		}

	}
}
