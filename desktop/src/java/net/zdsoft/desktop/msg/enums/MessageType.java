package net.zdsoft.desktop.msg.enums;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 消息类型，这里主要根据6.0划分的</br>
 * 2 公文类型
 * gt 2 办公类型
 * 1 普通消息 （根据receiverType区分个人还是单位发的）
 *
 * TODO 不包括 系统消息
 * Created by shenke on 2017/3/14.
 */
public enum MessageType {
	
	
    MSG_TYPE_NOTE {
        @Override
        public String getName() {
            return "消息";
        }

        @Override
        public Integer getType() {
            return 1;
        }
    },
    MSG_TYPE_ARCHIVE {
        @Override
        public String getName() {
            return "公文";
        }

        @Override
        public Integer getType() {
            return 2;
        }
    },
    MSG_TYPE_CAR {
        @Override
        public String getName() {
            return "车辆";
        }

        @Override
        public Integer getType() {
            return 3;
        }
    },
    MSG_TYPE_REPAIRE {
        @Override
        public String getName() {
            return "报修";
        }

        @Override
        public Integer getType() {
            return 4;
        }
    },
    MSG_TYPE_ATTENDANCE {
        @Override
        public String getName() {
            return "考勤";
        }

        @Override
        public Integer getType() {
            return 5;
        }
    },
    MSG_TYPE_MEETING {
        @Override
        public String getName() {
            return "会议";
        }

        @Override
        public Integer getType() {
            return 6;
        }
    },
    MSG_TYPE_EDUADM {
        @Override
        public String getName() {
            return "教务";
        }

        @Override
        public Integer getType() {
            return 11;
        }
    },
    MSG_TYPE_BULLETIN {
        @Override
        public String getName() {
            return "通知公告";
        }

        @Override
        public Integer getType() {
            return 13;
        }
    },
    MSG_TYPE_LEAVE {
        @Override
        public String getName() {
            return "请假";
        }

        @Override
        public Integer getType() {
            return 21;
        }
    },
    MSG_TYPE_GO_OUT {
        @Override
        public String getName() {
            return "外出";
        }

        @Override
        public Integer getType() {
            return 22;
        }
    },
    MSG_TYPE_EVECTION {
        @Override
        public String getName() {
            return "出差";
        }

        @Override
        public Integer getType() {
            return 23;
        }
    },
    MSG_TYPE_EXPENSE {
        @Override
        public String getName() {
            return "报销";
        }

        @Override
        public Integer getType() {
            return 24;
        }
    },
    MSG_TYPE_GOODS {
        @Override
        public String getName() {
            return "物品管理";
        }

        @Override
        public Integer getType() {
            return 25;
        }
    },
    MSG_TYPE_WORK_REPORT {
        @Override
        public String getName() {
            return "工作汇报";
        }

        @Override
        public Integer getType() {
            return 26;
        }
    },
    MSG_TYPE_ATTENDLECTURE {
        @Override
        public String getName() {
            return "听课";
        }

        @Override
        public Integer getType() {
            return 27;
        }
    },
    MSG_TYPE_SEAL {
        @Override
        public String getName() {
            return "用印";
        }

        @Override
        public Integer getType() {
            return 28;
        }
    },
    MSG_TYPE_JTGOOUT {
        @Override
        public String getName() {
            return "集体外出管理";
        }

        @Override
        public Integer getType() {
            return 29;
        }
    },
    MSG_TYPE_OFFICE {
        @Override
        public String getName() {
            return "办公";
        }

        @Override
        public Integer getType() {
            return 99;
        }
    },
    MSG_TYPE_UNKNOW {
        @Override
        public String getName() {
            return "其他";
        }

        @Override
        public Integer getType() {
            return Integer.MAX_VALUE;
        }
    };


    public abstract String getName();

    public abstract Integer getType();

    public static MessageType valueOf(Integer type){
        switch (type) {

            case 1:
                return MessageType.MSG_TYPE_NOTE;
            case 2:
                return MessageType.MSG_TYPE_ARCHIVE;
            case 3:
                return MessageType.MSG_TYPE_CAR;
            case 4:
                return MessageType.MSG_TYPE_REPAIRE;
            case 5:
                return MessageType.MSG_TYPE_ATTENDANCE;
            case 6:
                return MessageType.MSG_TYPE_MEETING;
            case 11:
                return MessageType.MSG_TYPE_EDUADM;
            case 13:
            	return MessageType.MSG_TYPE_BULLETIN;
            case 21:
                return MessageType.MSG_TYPE_LEAVE;
            case 22:
                return MessageType.MSG_TYPE_GO_OUT;
            case 23:
                return MessageType.MSG_TYPE_EVECTION;
            case 24:
                return MessageType.MSG_TYPE_EXPENSE;
            case 25:
                return MessageType.MSG_TYPE_GOODS;
            case 26:
                return MessageType.MSG_TYPE_WORK_REPORT;
            case 27:
                return MessageType.MSG_TYPE_ATTENDLECTURE;
            case 28:
                return MessageType.MSG_TYPE_SEAL;
            case 29:
                return MessageType.MSG_TYPE_JTGOOUT;
            case 99:
                return MessageType.MSG_TYPE_OFFICE;
            default:
                return MessageType.MSG_TYPE_UNKNOW;
        }
    }

    public static List<MessageType> valueOf(List<Integer> types){
        if(CollectionUtils.isEmpty(types)){
            return Collections.EMPTY_LIST;
        }
        List<MessageType> messageTypes = Lists.newArrayList();
        for (Integer type : types) {
            messageTypes.add(valueOf(type));
        }
        return messageTypes;
    }
}
