package com.truthso.ip360.utils;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007-5-1
 * </p>
 * <p>
 * Company: www.mapabc.com
 * </p>
 * 
 * @author luoyj
 * @version 1.0
 */
public class StringUtil {
	/*
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0
				|| "null".equalsIgnoreCase(str.trim());
	}

	/*
	 * 将字符串null转换为空
	 */
	public static String trimNull(String str) {
		if (str == null || "null".equalsIgnoreCase(str.trim()))
			return "";
		return str.trim();
	}

	/*
	 * 将字符串null转换为空
	 */
	public static String trimNull(Object obj) {
		String str = String.valueOf(obj);
		if (str == null || "null".equalsIgnoreCase(str.trim()))
			return "";
		return str.trim();
	}

	/*
	 * 将字符串null转换为任意字符
	 */
	public static String nullToValue(String str, String value) {
		if (str == null || "null".equalsIgnoreCase(str.trim()))
			return value;
		return str.trim();
	}

	/*
	 * 将单引号 ' 转换为 双引号 " 主要为了方便构造xml内容
	 */
	public static String transfDblQuot(String str) {
		return str.replaceAll("'", "\"");
	}

	/*
	 * 将yyyy-MM-dd hh:mm:ss.SSS日期格式的字符串转换为yyyy-MM-dd字符串格式
	 */
	public static String conv2YyyyMmDdWithSep(String str) {
		if (isEmpty(str)) {
			return "";
		} else {
			return str.split(" ")[0];
		}
	}

	/*
	 * 将yyyy-MM-dd hh:mm:ss.SSS日期格式的字符串转换为hh:mm:ss.SSS字符串格式
	 */

	public static String conv2HhMmSsWithSep(String str) {
		if (isEmpty(str)) {
			return "";
		} else {
			return str.split(" ")[1].split("\\.")[0];
		}
	}

	/*
	 * 将yyyy-MM-dd hh:mm:ss.SSS日期格式的字符串转换为hh:mm:ss.SSS字符串格式
	 */

	public static String conv2YyyyMmWithSep(String str) {
		if (isEmpty(str)) {
			return "";
		} else {
			return str.split(" ")[0].substring(0, 7);
		}
	}

	/*
	 * 将yyyy-MM-dd hh:mm:ss.SSS日期格式的字符串转换为yyyy-MM-dd hh:mm:ss字符串格式
	 */
	public static String conv2DateTime(String str) {
		if (isEmpty(str)) {
			return "";
		} else {
			return str.split("\\.")[0];
		}
	}

	/**
	 * 判断字符串不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 将 " ' 字符转换为全角的字符
	 * 
	 * @param str
	 * @return
	 */
	public static String castQujiao(String str) {
		if (isEmpty(str)) {
			return "";
		}
		String tmp = str.replaceAll("'", "‘");
		return trimNull(tmp.replaceAll("\"", "“"));
	}

	/**
	 * 转换为html的编码
	 * 
	 * @param str
	 * @return
	 */
	public static String toHtmlEncode(String str) {
		if (isEmpty(str)) {
			return "";
		}
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("\r\n", "<br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
		// str=str.replaceAll("\n","<br>");
		// str=str.replaceAll("/g","&nbsp;");
		// str=str.replaceAll("/\t/g","&nbsp;&nbsp;&nbsp;&nbsp;");
		return str;
	}

	/**
	 * 转换为xml的编码
	 * 
	 * @param str
	 * @return
	 */
	public static String toXmlEncode(String str) {
		if (isEmpty(str)) {
			return "";
		}
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

	/**
	 * 为长度不够指定值得整数在左边补0
	 * 
	 * @param value
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public static String addLeftZero(int value, int length) throws Exception {
		String tmp = String.valueOf(value);
		int diff = length - tmp.length();
		for (int i = 0; i < diff; i++) {
			tmp = "0" + tmp;
		}
		return tmp;
	}

	public static String trimRightZero(String str) throws Exception {
		if (isEmpty(str)) {
			return "";
		}
		return str.replaceAll("\\.{0,1}[0]+$", "");
	}

	public final static String toUpperForFirstChar(String str) {
		if (isEmpty(str))
			return "";
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * ISO-8859-1,UTF-8
	 * 
	 * @param str
	 * @return
	 */
	public final static String cast2Gbk(String str, String srcCode) {
		if (isEmpty(str)) {
			return "";
		}
		String ret = "";
		try {
			ret = new String(str.getBytes(srcCode), "GBK");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * ISO-8859-1,UTF-8
	 * 
	 * @param str
	 * @return
	 */
	public final static String cast2UTF8(String str, String srcCode) {
		if (isEmpty(str)) {
			return "";
		}
		String ret = "";
		try {
			ret = new String(str.getBytes(srcCode), "UTF-8");
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public final static boolean isCid(String cId) {
		return Pattern.matches("^([0-9]{14}[X]{0,1})|([0-9]{18})$", cId);
	}

	public final static boolean isVehicleNo(String str) {
		return Pattern
				.matches(
						"^(([\u0391-\uFFE5]{1})|([Ww]{1}[Jj]{1}))[0-9A-Za-z\u0391-\uFFE5]{5,30}$",
						str);
	}

	public final static boolean isMobileNo(String str) {
		return Pattern.matches("^1[3|5][\\d]{9}$", str);
	}

	public final static boolean isTelNo(String str) {
		return Pattern.matches("(^[0-9]{3,4}[0-9]{7,8}$)|(^[0-9]{7,8}$)", str);
	}

	public static boolean isEmail(String email) {
		String regex = "^\\w+@\\w+\\.(com\\.cn)|\\w+@\\w+\\.(com|cn)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}

	public static String getVehicleStringNull(String str) {

		String returnStr = "";
		if (!isEmpty(str)) {
			returnStr = str.replaceAll(" ", "");
		}
		return returnStr;
	}

	public static String[] splitBySeparator(String source, String separator) {

		return splitBySeparator(source, separator, true);

	}

	public static String[] splitBySeparator(String source, String separator,
			boolean blankIncluded) {

		if (source == null) {
			return new String[0];
		}

		if (separator == null || separator.length() == 0) {
			return (new String[] { source });
		}

		int startIndex = 0;

		int endIndex = source.indexOf(separator);

		List<String> items = new ArrayList<String>();

		for (; endIndex >= startIndex; endIndex = source.indexOf(separator,
				startIndex)) {

			String value = source.substring(startIndex, endIndex);

			if (!isEmpty(value) || blankIncluded) {
				items.add(trimNull(value));
			}

			startIndex = endIndex + separator.length();
		}

		String value = source.substring(startIndex);

		if (!isEmpty(value) || blankIncluded) {
			items.add(trimNull(value));
		}

		return items.toArray(new String[items.size()]);
	}

	/**
	 */
	public static String getSubStr(String s) {

		if (CheckUtil.isEmpty(s)) {
			return "";
		}

		String str = s;
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < s.length(); i++) {
			// 获取一个字符
			String temp = s.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}

			if (Math.ceil(valueLength) > 10) {
				str = s.substring(0, i) + "...";
				return str;
			}

		}

		// 进位取整
		return str;
	}

	/**
	 * 
	 * @param strUrl
	 *            uri
	 * @param targetStr
	 *            需要查找的 字符串
	 * @param replaceStr
	 *            替换新的 字符串
	 * @return
	 */
	public static String getUrlReplaceToTarget(String strUrl, String targetStr,
			String replaceStr) {

		String url = strUrl;

		if (strUrl.indexOf(targetStr) != -1) {
			url = strUrl.replace(targetStr, replaceStr);
		}

		return url;
	}

	public static String replaceAll(String source,
			Map<String, String> replacedItems, String startSymbol,
			String endSymbol) {

		if (CheckUtil.isEmpty(source) || CheckUtil.isEmpty(replacedItems)) {
			return source;
		}

		startSymbol = StringUtil.trimNull(startSymbol);
		endSymbol = StringUtil.trimNull(endSymbol);

		for (Iterator<String> it = replacedItems.keySet().iterator(); it
				.hasNext();) {

			String key = it.next();

			Object value = replacedItems.get(key);

			if (value instanceof String) {

				source = replaceAll(source,startSymbol+key+endSymbol,(String) value);

			}

		}

		return source;
	}
	

	

	public static String replaceAll(String source,
			Map<String, String> replacedItems) {
		return replaceAll(source, replacedItems, "", "");
	}
	
	public static void main(String[] args) {
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("sn", "test");
		parameters.put("key","test");
		
		System.out.println(StringUtil.replaceAll("sn={sn}&key={key}", parameters, "{", "}"));
	}
	/**
     * Replaces each substring of this string that matches the
     * given regular expression with the given replacement.
     * @param src -- string to be replaced
         * @param tarStr -- the regular expression to which this string is to be matched
     * @param repStr -- the string given replacement
     * @return The resulting String
     */
    public static String replaceAll(String src, String tarStr, String repStr) {
        if (CheckUtil.isEmpty(src)) {
            return src;
        }
        if (CheckUtil.isEmpty(tarStr) || CheckUtil.isEmpty(repStr)) {
            return src;
        }
        int srcPos = 0;
        int tarPos = 0;
        StringBuffer result = new StringBuffer(25);
        while ((tarPos = src.indexOf(tarStr, srcPos)) >= 0) {
            result.append(src.substring(srcPos, tarPos));
            result.append(repStr);
            srcPos = tarPos + tarStr.length();
        }
        result.append(src.substring(srcPos));
        String finalString = result.toString();
        return finalString;
    }

    public static String replaceIngoreCaseAll(
        String src,
        String tarStr,
        String repStr) {
        if (CheckUtil.isEmpty(src)) {
            return src;
        }
        if (CheckUtil.isEmpty(tarStr) || CheckUtil.isEmpty(repStr)) {
            return src;
        }
        int srcPos = 0;
        int tarPos = 0;
        StringBuffer result = new StringBuffer(25);
        while ((tarPos =
            src.toLowerCase().indexOf(tarStr.toLowerCase(), srcPos))
            >= 0) {
            result.append(src.substring(srcPos, tarPos));
            result.append(repStr);
            srcPos = tarPos + tarStr.length();
        }
        result.append(src.substring(srcPos));
        String finalString = result.toString();
        return finalString;
    }
}
