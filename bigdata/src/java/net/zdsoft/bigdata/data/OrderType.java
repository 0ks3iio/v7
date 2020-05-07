/**
 * FileName: OrderType.java
 * Author:   shenke
 * Date:     2018/5/25 上午9:35
 * Descriptor:
 */
package net.zdsoft.bigdata.data;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * 图表 大屏等的订阅类型
 * @author shenke
 * @since 2018/5/25 上午9:35
 */
public enum OrderType {

    /** 私有 */
    SELF ("私有") {
        @Override
        public int getOrderType() {
            return 1;
        }
    },
    /** 公开 只可以系统管理员操作*/
    OPEN ("公开") {
        @Override
        public int getOrderType() {
            return 2;
        }
    },
    /** 单位级别公开 */
    UNIT_OPEN ("本单位公开") {
        @Override
        public int getOrderType() {
            return 3;
        }
    },
    /** 单位授权 则该单位下的所有人都可用  该级别的图表才可以在订阅列表中选择*/
    UNIT_ORDER ("单位授权") {
        @Override
        public int getOrderType() {
            return 4;
        }
    },
    /** 单位授权之后 单位下的人仍需要再次授权 该级别的图表才可以在订阅列表中选择*/
    UNIT_ORDER_USER_AUTHORIZATION ("单位授权个人需授权") {
        @Override
        public int getOrderType() {
            return 5;
        }
    },
    /** 个人授权 该级别的图表才可以在订阅列表中选择*/
    USER_AUTHORIZATION ("个人授权") {
        @Override
        public int getOrderType() {
            return 6;
        }
    };

    private int orderType;
    private String orderName;

    public abstract int getOrderType();

    OrderType(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    public static Optional<OrderType> from(int orderType) {
        switch (orderType) {
            case 1 : return Optional.of(SELF);
            case 2 : return Optional.of(OPEN);
            case 3 : return Optional.of(UNIT_OPEN);
            case 4 : return Optional.of(UNIT_ORDER);
            case 5 : return Optional.of(UNIT_ORDER_USER_AUTHORIZATION);
            case 6 : return Optional.of(USER_AUTHORIZATION);
            default:
                return Optional.empty();
        }
    }

    public static List<OrderType> useValues() {
        List<OrderType> orderTypes = Lists.newArrayList();
        for (OrderType e : OrderType.values()) {
            if (e == UNIT_ORDER_USER_AUTHORIZATION) {
                continue;
            }
            orderTypes.add(e);
        }
        return orderTypes;
    }
    //public abstract String getOrderName();
}
