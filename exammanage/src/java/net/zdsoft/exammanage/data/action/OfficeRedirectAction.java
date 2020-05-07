package net.zdsoft.exammanage.data.action;

import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/remote/openapi/office"})
public class OfficeRedirectAction {
    @Autowired
    SystemIniRemoteService systemIniRemoteService;

    @RequestMapping(value = "/teacherAttendance")
    public String updateTicket(HttpSession httpSession, RedirectAttributes redAttri, final String syncUserId,
                               String hideRightButton, String customReturn) {
        redAttri.addAttribute("customReturn", customReturn);
        redAttri.addAttribute("hideRightButton", hideRightButton);
        redAttri.addAttribute("syncUserId", syncUserId);
        String url6 = systemIniRemoteService.findValue("wiki_url");
        System.out.println(url6);
        return "redirect:" + url6 + "/common/open/remote/weike/officeTeacherAttendance.action";
    }
}
