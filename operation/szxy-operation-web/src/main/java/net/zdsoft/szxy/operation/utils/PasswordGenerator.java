package net.zdsoft.szxy.operation.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 默认密码发生器
 *
 * @author shenke
 * @since 2019/2/15 下午4:10
 */
public final class PasswordGenerator {

    public static final String generateStudentAndFamilyPassword(PasswordRule rule, KeyInfo keyInfo) {
        switch (rule) {
            case S_RULE_1:
                return "123456";
            case S_RULE_2:
                if (keyInfo == null || StringUtils.isBlank(keyInfo.getKey())) {
                    return "000000";
                }
                if (keyInfo.getKey().length() < 6) {
                    throw new IllegalArgumentException("不合法的身份证号");
                }
                return keyInfo.getKey().substring(keyInfo.getKey().length() - 6);
            case S_RULE_3:
                if (keyInfo == null || StringUtils.isBlank(keyInfo.getKey())) {
                    throw new IllegalArgumentException("不合法的KeyInfo信息");
                }
                if (keyInfo.getKey().length() < 6) {
                    return StringUtils.leftPad(keyInfo.getKey(), 6, "0");
                }
                return keyInfo.getKey().substring(keyInfo.getKey().length() - 6);
            case S_RULE_4:
                return String.valueOf(Math.random()).substring(2, 8);
            default:
                throw new IllegalArgumentException("不合法的规则");
        }
    }

    public static final String generateTeacherPassword(TRule rule, KeyInfo keyInfo) {
        switch (rule) {
            case T_RULE_1:
                return "123456";
            case T_RULE_2:
                if (keyInfo == null || StringUtils.isBlank(keyInfo.getKey())) {
                    throw new IllegalArgumentException("不合法的KeyInfo信息");
                }
                if (keyInfo.getKey().length() < 6) {
                    return StringUtils.leftPad(keyInfo.getKey(), 6, "0");
                }
                return keyInfo.getKey().substring(keyInfo.getKey().length() - 6);
            default:
                throw new IllegalArgumentException("不合法的密码规则");
        }
    }

    public interface KeyInfo {

        static KeyInfo identityCard(String identityCard) {
            return new IdentityCard(identityCard);
        }

        static KeyInfo username(String username) {
            return new Username(username);
        }

        String getKey();

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        class IdentityCard implements KeyInfo {

            private String identityCard;

            @Override
            public String getKey() {
                return identityCard;
            }
        }

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        class Username implements KeyInfo {

            private String username;

            @Override
            public String getKey() {
                return username;
            }
        }
    }


    public enum PasswordRule {
        /**
         * 学生规则一
         * 123456
         */
        S_RULE_1,
        /**
         * 学生规则二
         * 身份证号后六位，为空则 000000
         */
        S_RULE_2,
        /**
         * 学生规则三
         * 用户名后6位，不足前面补0
         */
        S_RULE_3,
        /**
         * 学生规则四
         * 随机6位数字
         */
        S_RULE_4;

        public static PasswordRule from(String str) {
            switch (str) {
                case "1":
                    return S_RULE_1;
                case "2":
                    return S_RULE_2;
                case "3":
                    return S_RULE_3;
                case "4":
                    return S_RULE_4;
                default:
                    throw new IllegalArgumentException("无法解析的密码规则");
            }
        }
    }

    /**
     * 教师规则
     */
    public enum TRule {
        /**
         * 教师规则1
         */
        T_RULE_1,
        T_RULE_2;

        public static TRule from(String str) {
            if ("1".equals(str)) {
                return T_RULE_1;
            }
            if ("2".equals(str)) {
                return T_RULE_2;
            }
            throw new IllegalArgumentException("无法解析的教师密码规则");
        }
    }
}
