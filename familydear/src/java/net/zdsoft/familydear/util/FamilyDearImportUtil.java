package net.zdsoft.familydear.util;

import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.Validators;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Designed By luf
 *
 * @author luf
 * @date 2019/5/14 15:40
 */
public class FamilyDearImportUtil {

    /**
     * 数据内容简单格式校验
     * @param fieldName 字段名称
     * @param value 内容
     * @param typeStr
     * @param require 是否必填
     * @param regex 正则
     * @param errorMsg 正则校验不通过时返回的提示信息
     * @return
     * @throws Exception
     */
    public static String verifyType(String fieldName, String value, String typeStr, boolean require, String regex, String errorMsg) throws Exception{
        if(require && StringUtils.isEmpty(value)) {
            throw new RuntimeException(fieldName + "不能为空。");
        }
        if(StringUtils.isEmpty(value)) {
            return value;
        }
        if(StringUtils.isEmpty(typeStr) && StringUtils.isEmpty(regex)) {
            return value;
        }
        String[] ts = typeStr.split("-");
        String type = ts[0];
        if (type.equalsIgnoreCase("String")
                || type.toLowerCase().indexOf("string") == 0) {
            int strLength = NumberUtils.toInt(ts[1]);
            if (strLength == 0)
                return value;
            if (Validators.isString(value, 0, strLength)
                    && net.zdsoft.framework.utils.StringUtils
                    .getRealLength(value) <= strLength)
                return value;
            else
                throw new RuntimeException(fieldName + "内容超出了最大长度("
                        + strLength + ")。");
        } else if (type.equalsIgnoreCase("Integer")
                || type.equalsIgnoreCase("Long")) {
            if (Validators.isNumber(value))
                return value;
            else
                return "";
//		} else if (type.equalsIgnoreCase("Datetime")) {
//			if (isDateTime(value))
//				return value;
//			else
//				throw new RuntimeException(fieldName + "不是有效的日期类型。");
//		}
            // 只有年和月的类型的
        } else if (type.equalsIgnoreCase("YearMonth")) {
            if (value.indexOf("-") > 0) {
                // 如果是2007-1这类的,改成2007-1-1
                if (value.indexOf("-", value.indexOf("-") + 1) < 0) {
                    value = value + "-1";
                }
                String[] s = value.split("-");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "-" + month + "-" + day;
                    } else {
                        value = "19" + year + "-" + month + "-" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "-" + month + "-" + day;
                }
            } else if (value.indexOf("/") > 0) {
                if (value.indexOf("/", value.indexOf("/") + 1) < 0) {
                    value = value + "/1";
                }
                String[] s = value.split("/");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "/" + month + "/" + day;
                    } else {
                        value = "19" + year + "/" + month + "/" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "/" + month + "/" + day;
                }
                value = value.replaceAll("/", "-");
            } else if (value.trim().length() == 6) {
                value = value + "01";
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            } else if (value.trim().length() == 8) {
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            }
            if (DateUtils.isDate(value)) {
                return value;
            } else {
                throw new RuntimeException(fieldName + "不是有效的日期类型。");
            }
        } else if (type.equalsIgnoreCase("Date")) {
            if (value.indexOf("-") > 0) {
                String[] s = value.split("-");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;
                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "-" + month + "-" + day;
                    } else {
                        value = "19" + year + "-" + month + "-" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "-" + month + "-" + day;
                }
            } else if (value.indexOf("/") > 0) {
                String[] s = value.split("/");
                if (s.length != 3) {
                    throw new RuntimeException(fieldName + "不是有效的日期类型。");
                }
                String year = s[0];
                String month = s[1];
                if (month.length() == 1)
                    month = "0" + month;
                String day = s[2];
                if (day.length() == 1)
                    day = "0" + day;

                if (year.length() == 2 && Validators.isNumber(year)) {
                    if (Integer.parseInt(year) < 20) {
                        value = "20" + year + "/" + month + "/" + day;
                    } else {
                        value = "19" + year + "/" + month + "/" + day;
                    }
                } else if (year.length() == 4 && Validators.isNumber(year)) {
                    value = year + "/" + month + "/" + day;
                }
                value = value.replaceAll("/", "-");
            } else if (value.trim().length() == 8) {
                if (Validators.isNumber(value)) {
                    value = value.substring(0, 4) + "-"
                            + value.substring(4, 6) + "-"
                            + value.substring(6);
                }
            }
            if (DateUtils.isDate(value))
                return value;
            else
                throw new RuntimeException(fieldName + "不是有效的日期类型。");
        }
//		else if (type.equalsIgnoreCase("Timestamp")) {
//			if (Validators.isTime(value))
//				return "";
//			else
//				throw new Exception(fieldName + "不是有效的日期类型。");
//		} else if (type.indexOf("Numeric") == 0) {
//			if ("N".equalsIgnoreCase(nonnegative)) {
//				if (!Validators.isNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的数字类型。");
//				}
//			} else {
//				if (!Validators.isNonNegativeNumeric(value, fraction)) {
//					throw new RuntimeException(fieldName + "不是有效的非负数字类型。");
//				}
//			}
//			// 判断长度时过滤+ -
//			int beginIndex = 0;
//			if (value.indexOf("+") >= 0 || value.indexOf("-") >= 0) {
//				beginIndex = 1;
//			}
//			// 如果数值包括小数点：分别判断整数和小数的位数是否超过指定的长度
//			if (value.indexOf(".") >= 0) {
//				if (value.substring(beginIndex, value.indexOf("."))
//						.length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision
//							+ "位，小数位不能大于" + fraction + "位。");
//				}
//				// 如果不包括小数点：只判断整数的位数是否超过了指定的长度
//			} else if (StringUtils
//					.isNotBlank(value)) {
//				if (value.substring(beginIndex, value.length()).length() > precision) {
//					throw new RuntimeException(fieldName
//							+ "数字不符合要求，请控制在整数位不能大于" + precision + "位。");
//				}
//			}
//
//			if (value == null || value.trim().equals("")) {
//				value = "0";
//			}
//			return value;
//		}

        if (StringUtils.isEmpty(regex)) {
            return value;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if(!matcher.matches()) {
            return errorMsg;
        }
        return value;
    }

    /**
     * 结果信息
     * @param totalCount
     * @param successCount
     * @param errorCount
     * @param errorDataList
     * @return
     */
    public static  String  result(int totalCount , int successCount , int errorCount , List<String[]> errorDataList){
        Json importResultJson=new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }
    /**
     * 返回错误消息
     */
    public static  String errorResult(String da1, String da2, String da3, String da4,
                               int sequence, int total, int success, List<String[]> errorDataList) {
        addError(da1, da2, da3, da4, sequence, errorDataList);
        return result(total, success, total-success, errorDataList);
    }
    /**
     * 添加错误
     */
    public static  void addError(String da1, String da2, String da3, String da4,
                          int sequence, List<String[]> errorDataList) {
        String[] errorData=new String[4];
        sequence++;
        errorData[0]= StringUtils.trimToEmpty(da1);
        errorData[1]=StringUtils.trimToEmpty(da2);
        errorData[2]=StringUtils.trimToEmpty(da3);
        errorData[3]=StringUtils.trimToEmpty(da4);
        errorDataList.add(errorData);
    }
}
