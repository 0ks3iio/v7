package net.zdsoft.operation.action;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.operation.entity.TrialUser;
import net.zdsoft.operation.service.TrialUserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author panlf
 */
@Controller
@RequestMapping("/operation/trialUser")
public class TrialUserRegisterAction extends BaseAction {

    private Logger logger = LoggerFactory.getLogger(TrialUserRegisterAction.class);

    @Autowired
    private TrialUserRegisterService trialUserRegisterService;

    @RequestMapping("/trialUserRegister")
    @ResponseBody
    public Object trialUserRegister(@Valid TrialUser trialUser, Errors errors) {
        if (errors.hasErrors()) {
            if (errors.hasFieldErrors()) {
                return errors.getFieldError().getDefaultMessage();
            }
            if (errors.hasGlobalErrors()) {
                return errors.getGlobalError().getDefaultMessage();
            }
        }

        String state = "";
        try {
            trialUserRegisterService.insertTrialUser(trialUser);
            state = "ok";
        } catch (Exception e) {
            logger.error("申请使用信息保存失败", e);
            state = "申请失败";
        }
        return state;
    }
}
