package net.zdsoft.szxy.operation.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import net.zdsoft.szxy.base.enu.FamilyRelationCode;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import net.zdsoft.szxy.utils.AssertUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 教师、学生、家长 账号生成器
 * 教师：
 * G+身份证 或 J+临时神分钟
 * 学生、家长：
 * 1.(学生 s, 家长 p)+学校编号+6位随机数；{(s/p)_etohSchoolId_random(6)}
 * 2.(学生 s, 家长 p)+学校编号取前6位+姓名拼音；{(s/p)_etohSchoolId[0,6]_firstSpell}
 * 3.学生：用户名前缀+学号，家长：p+学校编号+6位随机数；{N_userNamePrefix_stuCode},{p_etohSchoolId_random(6)}
 * 4.学生：x+父亲手机号，父亲：f+自己手机号, 母亲 ：m+自己手机号；{x_fMobile},{(f/m)_mobile}
 * 5.学生：用户名前缀+学生所在班级编号+两位流水号，父亲：f+学生用户名，母亲：m+学生用户名; {N_userNamePrefix_clsCode_maxcode},{(f/m)_tempUserName}
 * 6.学生：G+身份证号，父亲：F+学生身份证号，母亲：M+学生身份证号，监护人：H+学生身份证号 ；{(G/F/M/H)_identitycard}
 * 7.学生：s+身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号；{(s/f/m/g)_identitycard}
 * 8.学生：s+学籍号，父亲：f+学生学籍号，母亲：m+学生学籍号，监护人：g+学生学籍号；{(s/f/m/g)_unitiveCode}
 * 9.学生：身份证号，父亲：f+学生身份证号，母亲：m+学生身份证号，监护人：g+学生身份证号 ；{N_identitycard},{(f/m/g)_identitycard}
 * 10-x：x为数字，表示学籍号后几位，学生：s+学籍号后x位，父亲：f+学生学籍号后x位，母亲：m+学生学籍号后x位，监护人：g+学生学籍号后x位；{(s/f/m/g)_unitiveCode[-1,x]}
 *
 * @author shenke
 * @since 2019/2/14 下午2:23
 */
public final class AccountGenerator {

    private static final Pattern RANDOM_NUMBER = Pattern.compile("random\\(\\d\\)");
    private static final Pattern NUMBER = Pattern.compile("\\d");
    private static final Pattern FIELD_CUT = Pattern.compile("[a-zA-Z]+?\\[\\d,\\d{1,2}]|[a-zA-Z]+?\\[-\\d,\\d{1,2}]");
    private static final Pattern CUT = Pattern.compile("\\[\\d,\\d{1,2}]|\\[\\-\\d,\\d{1,2}]");
    private static final String[] FIXED_STUDENT_CHARS = new String[]{"s", "N", "G", "x"};
    private static final String[] FIXED_FAMILY_CHARS = new String[]{"f", "m", "p", "F", "M"};
    /**
     * 固定字符
     */
    private static final Set<String> FIXED_CHARS = new HashSet<String>() {{
        this.addAll(Arrays.asList(FIXED_STUDENT_CHARS));
        this.addAll(Arrays.asList(FIXED_FAMILY_CHARS));
    }};
    /**
     * 固定字段
     */
    private static final Set<String> FIXED_FIELD_NAMES = new HashSet<String>() {{
        for (Field field : AccountInfo.class.getDeclaredFields()) {
            this.add(field.getName());
        }
    }};

    public static String generateAccount(String code, AccountInfo accountInfo) throws IllegalAccountRuleException {
        AssertUtils.notNull(code, "账号规则不能为空");
        String[] rules = code.split("},");
        for (String rule : rules) {
            String rawRule = rule.replaceAll("[{}]", "");
            String head = rawRule.substring(0, rawRule.indexOf("_")).replaceAll("[()]", "");
            String right = rawRule.substring(rawRule.indexOf("_") + 1);
            for (String s : head.split("/")) {
                String finalRule = null;
                if ("N".equals(s)) {
                    finalRule = right;
                } else {
                    finalRule = s + "_" + right;
                }
                //学生
                if (UserOwnerTypeCode.STUDENT.equals(accountInfo.getOwnerType())) {
                    if (StringUtils.startsWithAny(s, FIXED_STUDENT_CHARS)) {
                        return buildByRuleRegex(finalRule, JSONObject.parseObject(JSONObject.toJSONString(accountInfo)));
                    }
                } else {
                    if (StringUtils.startsWithAny(s, FIXED_FAMILY_CHARS)) {
                        if (FamilyRelationCode.FATHER.equals(accountInfo.getFamilyType())) {
                            if (StringUtils.startsWithAny(s, "f", "F")) {
                                return buildByRuleRegex(finalRule, JSONObject.parseObject(JSONObject.toJSONString(accountInfo)));
                            }
                        } else if (FamilyRelationCode.MOTHER.equals(accountInfo.getFamilyType())) {
                            if (StringUtils.startsWithAny(s, "m", "M")) {
                                return buildByRuleRegex(finalRule, JSONObject.parseObject(JSONObject.toJSONString(accountInfo)));
                            }
                        }
                        if (StringUtils.startsWithAny(s, "p")) {
                            return buildByRuleRegex(finalRule, JSONObject.parseObject(JSONObject.toJSONString(accountInfo)));
                        }
                    }
                }
            }
        }
        throw new IllegalAccountRuleException("无法解析的账号规则");
    }

    private static String buildByRuleRegex(String rule, JSONObject accountInfo) throws IllegalAccountRuleException {
        StringBuilder builder = new StringBuilder();
        for (String s : rule.split("_")) {
            int ol = builder.length();
            builder.append(tryParseRandomNumber(s))
                    .append(tryParseFixedChars(s))
                    .append(tryParseFixedFieldNames(s, accountInfo))
                    .append(tryParseFieldCut(s, accountInfo));
            if (ol == builder.length()) {
                throw new IllegalAccountRuleException(String.format("无法解析的账号号规则:%s", s));
            }
        }
        return builder.toString();
    }


    private static String tryParseRandomNumber(String regex) {
        if (RANDOM_NUMBER.matcher(regex).find()) {
            Matcher matcher = NUMBER.matcher(regex);
            if (matcher.find()) {
                int val = Integer.valueOf(matcher.group());
                return String.valueOf(Math.random()).substring( 2, 2 + val);
            }
        }
        return "";
    }

    private static String tryParseFixedChars(String regex) {
        if (FIXED_CHARS.contains(regex)) {
            return regex;
        }
        return "";
    }

    private static String tryParseFixedFieldNames(String regex, JSONObject accountInfo) {
        if (FIXED_FIELD_NAMES.contains(regex)) {
            return accountInfo.getString(regex);
        }
        return "";
    }

    private static String tryParseFieldCut(String regex, JSONObject accountInfo) {
        if (!FIELD_CUT.matcher(regex).find()) {
            return "";
        }
        Matcher matcher = CUT.matcher(regex);
        if (!matcher.find()) {
            return "";
        }
        String scope = matcher.group();
        String[] scopes = scope.replaceAll("[\\[\\]]", "").split(",");
        int start = Integer.valueOf(scopes[0]);
        int end = Integer.valueOf(scopes[1]);

        String fieldName = regex.replaceAll(CUT.pattern(), "");
        String value = accountInfo.getString(fieldName);
        if (start < 0) {
            return value.substring(value.length() - end);
        } else {
            return value.substring(start, end);
        }
    }

    /**
     * 账号创建需要的相关信息
     */
    @Data
    public static class AccountInfo {

        /**
         * 学校编号
         */
        private String etohSchoolId;
        /**
         * 姓名全拼，名字取首字母
         */
        private String firstSpell;
        /**
         * 用户名前缀
         * 取sys_option 表
         * @see net.zdsoft.szxy.operation.enums.SysOptionCode#KEY_USERNAME_PREFIX
         */
        private String userNamePrefix;
        /**
         * 学号
         */
        private String stuCode;
        /**
         * 自己手机号
         */
        private String mobile;
        /**
         * 父亲手机号
         * 参见规则4，生成学生账号时使用
         */
        private String fMobile;
        /**
         * 班级编号
         */
        private String clsCode;
        /**
         * 两位流水号
         */
        private String maxcode;
        /**
         * 临时用户名
         * 规则5需要的学生用户名
         */
        private String tempUserName;
        /**
         * 学生身份证号
         */
        private String identitycard;
        /**
         * 学籍号
         */
        private String unitiveCode;


        /*************************以下是标记字段*************************/
        /**
         * 标记是家长还是学生
         */
        private Integer ownerType;

        /**
         * 标记是父亲还是母亲
         */
        private String familyType;
    }

    public static class IllegalAccountRuleException extends Exception {
        IllegalAccountRuleException(String message) {
            super(message);
        }
    }
}
