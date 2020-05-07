package net.zdsoft.dataimport.socket;

import net.zdsoft.dataimport.cache.ReplyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenke
 * @since 2017.08.04
 */
@RestController
public class ReplyAction  {

    @Autowired private ReplyCache replyCache;

    @MessageMapping(value = "status")
    @SendTo("/import/status")
    public Object execute(String cacheId) {

        return null;
    }
}
