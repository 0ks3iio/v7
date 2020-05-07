package net.zdsoft.desktop.msg.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.desktop.msg.dto.MessageDto;
import net.zdsoft.desktop.msg.dto.MessageGroupDto;
import net.zdsoft.desktop.msg.entity.Message;
import net.zdsoft.desktop.msg.entity.MessageGroup;
import net.zdsoft.desktop.msg.enums.MessageType;
import net.zdsoft.framework.utils.EntityUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by shenke on 2017/3/14.
 */
public class MessageUtils {

    private static final Map<String,String[]> GROUP_CACHE = Maps.newConcurrentMap();

    public static List<MessageGroupDto> turn2MessageGroup(String msg,List<MessageGroup> groups){
        List<MessageGroupDto> messageGroupDtos = Lists.newArrayList();

//        Map<Integer,List<MessageDto>> messaMap = Maps.newHashMap();
//        List<Message> messageList = Message.toMessageList(msg);
//        for (Message message : messageList) {
//            List<MessageDto> messageDtos = messaMap.get(message.getMessageType());
//            if(messageDtos == null){
//                messageDtos = Lists.newArrayList();
//            }
//            MessageType messageType
//            MessageDto messageDto = new MessageDto()
//        }

        return null;
    }

    public static List<MessageType> turnType(String types){
        List<MessageType> messageTypes = Lists.newArrayList();
        if(StringUtils.isBlank(types)){
            return messageTypes;
        }
        String[] typeArray = StringUtils.split(types, MessageGroup.TYPE_SEPARATOR);
        for (String s : typeArray) {
            messageTypes.add(MessageType.valueOf(NumberUtils.toInt(s)));
        }
        messageTypes = EntityUtils.removeEmptyElement(messageTypes);
        return messageTypes;
    }

    public static boolean belongTo(Integer type, MessageGroup group){
        String[] groups = GROUP_CACHE.get(group.getMsgType());
        if(ArrayUtils.isEmpty(groups)){
            groups = group.getMsgType().split(MessageGroup.TYPE_SEPARATOR);
            GROUP_CACHE.put(group.getMsgType(),groups);
        }
        return ArrayUtils.contains(groups,type.toString());
    }

    public static MessageGroup searchGroup(Integer type, List<MessageGroup> messageGroups){
        for (MessageGroup messageGroup : messageGroups) {
            if(belongTo(type,messageGroup)){
                return messageGroup;
            }
        }
        return null;
    }

    public static MessageType searchMessageType(Integer type){
        return MessageType.valueOf(type);
    }
}
