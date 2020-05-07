package net.zdsoft.desktop;

import net.zdsoft.framework.action.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 桌面action基类,提供参数校验基本方法以及桌面通用方法
 *
 * @author ke_shen@126.com
 * @since 2018/1/24 下午2:43
 */
public abstract class AbstractController extends BaseAction {

    protected static final String FORWARD_ATTRIBUTES_NAME = "forward_attributes_name";

    /**
     * 优先显示fieldError
     * 获取单个错误信息
     */
    protected String getSingleErrorMsg(BindingResult errors) {
        if (errors.hasFieldErrors()) {
            return errors.getFieldError().getDefaultMessage();
        }
        if (errors.hasGlobalErrors()) {
            return errors.getGlobalError().getDefaultMessage();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 重定向
     */
    protected String redirect(String pathOrUrl) {
        return "redirect:" + pathOrUrl;
    }

    protected String forward(String path, String attributeName, Object attribute) {
        if (StringUtils.isNotBlank(attributeName)) {
            getRequest().setAttribute(attributeName, attribute);
        }
        return "forward:" + path;
    }

    protected String forward(String path) {
        return forward(path, null, null);
    }

    protected HttpSession getHttpSession() {
        return getHttpSession(true);
    }

    protected HttpSession getHttpSession(boolean create) {
        return getRequest().getSession(create);
    }

    protected ModelAndView createMv(String viewName) {
        return new ModelAndView(viewName);
    }

    protected ModelAndView createMv() {
        return new ModelAndView();
    }
}
