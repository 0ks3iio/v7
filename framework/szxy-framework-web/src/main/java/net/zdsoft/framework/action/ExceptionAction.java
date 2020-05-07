package net.zdsoft.framework.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ex")
public class ExceptionAction extends BaseAction{

	@RequestMapping("/404")
	public String error404(){
		return "/WEB-INF/404.ftl";
	}
	
	@RequestMapping("/exception")
	public String errorException(){
		return "";
	}
}
