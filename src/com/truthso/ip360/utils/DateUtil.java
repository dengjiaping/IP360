package com.truthso.ip360.utils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	// 缺省的日期显示格式： yyyyMMdd
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

	// 缺省的日期时间显示格式：yyyyMMdd HH:mm:ss
	public static final String DEFAULT_DATETIME_FORMAT = "yyyyMMdd HH:mm:ss";

	public static final String YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
	public static final String yyyy年MM月dd日 = "yyyy年MM月dd日";
	public static final String MM月dd日 = "MM月dd日";

	/**
	 * 私有化构造函数
	 */
	private DateUtil() {

	}

	/**
	 * 格式化日期
	 * 
	 * @param source
	 *            被格式化的日期
	 * @param format
	 *            日期格式,默认：yyyyMMddhhmmss
	 * @return
	 */
	public static String formatDate(Date source, String format) {

		String result = null;
		if (CheckUtil.isBlankOrNull(format)) {
			format = YYYYMMDDHHMMSS;
		}
		if (source != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
				result = sdf.format(source);
			} catch (Exception e) {
				Log.e("DateUtil.formatDate", "格式化日期错误" + format);
			}
		}
		return result;
	}

	/**
	 * 获取当前系统日期
	 * 
	 * @param format
	 *            默认 "yyyy-MM-dd HH:mm";
	 * @return
	 */
	public static String getCurrentDate(String format) {
		Calendar calendar = Calendar.getInstance();
		String result = null;
		if (CheckUtil.isBlankOrNull(format)) {
			format = "yyyy-MM-dd HH:mm";
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(calendar.getTime());
		} catch (Exception e) {
			Log.e("DateUtil.formatDate", "格式化日期错误" + format);
		}
		return result;
	}

	/**
	 * 获取source所属月的第一天
	 * 
	 * @param source
	 *            日期
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String getMonthBegin(Date source, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return formatDate(calendar.getTime(), format);
	}

	/**
	 * 获取source所属月的最后一天
	 * 
	 * @param source
	 *            日期
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String getMonthEnd(Date source, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return formatDate(calendar.getTime(), format);
	}

	/**
	 * 获取source所在周的最后一天
	 * 
	 * @param source
	 *            日期
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String getWeekBegin(Date source, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return formatDate(calendar.getTime(), format);
	}

	/**
	 * 获取source所在周的最后一天
	 * 
	 * @param source
	 *            日期
	 * @param format
	 *            日期格式
	 * @return
	 */
	public static String getWeekEnd(Date source, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return formatDate(calendar.getTime(), format);
	}

	/**
	 * 根据数据库服务器时间得到当前日历对象
	 * 
	 * @return 当前日期对应的日历对象
	 */
	public static Date getNow() {

		return new Date();
	}

	/**
	 * 得到系统当前日期时间
	 * 
	 * @return 当前日期时间
	 */
	public static String getNowStr() {

		return DateUtil.getDateTimeStr(1);
	}

	/**
	 * 得到用指定方式格式化的系统日期
	 * 
	 * @param iType
	 *            返回日期格式 ====================================================
	 *            Type 格式 0 2011-04-11(yyyy-MM-dd) 1 2011-04-11
	 *            22:52:08(yyyyMMdd hh:mm:ss)
	 *            ====================================================
	 * @return 解析后的日期
	 */
	public static Date getDateTimeDte(int iType) {
		Date date = null;

		// 格式化标准
		String strPattern = DEFAULT_DATE_FORMAT;
		if (iType == 0)
			strPattern = DEFAULT_DATE_FORMAT;
		if (iType == 1)
			strPattern = DEFAULT_DATETIME_FORMAT;

		// 格式化
		try {
			SimpleDateFormat datetmp = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
			Object[] obj = new Object[] { datetmp.format(new Date()), time.format(new Date()) };// atabasetool.getDBNowTime();
			String strDate = obj[0].toString();
			if (iType == 0)
				strDate = obj[0].toString().trim();
			if (iType == 1)
				strDate = obj[0].toString().trim() + " " + obj[1].toString();

			SimpleDateFormat dateFormat = new SimpleDateFormat(strPattern);

			// strDate 数据库时间
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 得到用指定方式格式化的系统日期
	 * 
	 * @param iType
	 *            返回日期格式 ====================================================
	 *            Type 格式 0 20110411(yyyyMMdd) 1 20110411 22:52:08(yyyyMMdd
	 *            hh:mm:ss) ====================================================
	 * @return 日期时间字符串
	 */
	public static String getDateTimeStr(int iType) {
		String strResult = "";

		// 实例化数据库交互接口
		/*
		 * IDatabaseToolBS atabasetool = (IDatabaseToolBS)new
		 * DateTools().getBean("IDatabaseToolBS");
		 */

		// 取得数据库时间
		try {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
			Object[] obj = new Object[] { date.format(new Date()), time.format(new Date()) };// atabasetool.getDBNowTime();
			if (iType == 0)
				strResult = obj[0].toString().trim();
			if (iType == 1)
				strResult = obj[0].toString().trim() + " " + obj[1].toString();
			if (iType == 2)
				strResult = obj[0].toString().trim() + obj[1].toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return strResult;
	}

	/**
	 * 获取当前或以前的日期字符串 yyyy-mm-dd
	 * 
	 * @param beforeNum
	 *            0:今天的日期 -1:昨天的日期 -2：前天的日期 负数表示获取前几天的日期
	 * @return 指定日期的字符串 yyyy-mm-dd
	 */
	public static String getDate(int beforeNum) {
		Calendar ca = Calendar.getInstance(Locale.CHINA);
		ca.add(Calendar.DATE, beforeNum);
		return new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime());
	}

	/**
	 * 得到当前年份
	 * 
	 * @return 当前年份
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static String getStrNowYear() {
		return String.valueOf(getCurrentYear());
	}

	/**
	 * 得到当前月份
	 * 
	 * @return 当前月份
	 */
	public static int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前日
	 * 
	 * @return 当前日
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	// add by duanzengfu 2012/08/17 start
	/**
	 * 当前系统日期加天数
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期字符串
	 */
	public static String addDaysToStr(int days, String pattern) {

		// 缺省为“yyyyMMdd”
		if (null == pattern || "".equals(pattern)) {
			pattern = "yyyyMMdd";
		}
		Date date = null;
		String strDate = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = add(getNow(), days, Calendar.DATE);
			strDate = dateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 当前系统日期加天数
	 * 
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期字符串
	 */
	public static String addDaysToStr(int days) {
		return addDaysToStr(days, "yyyyMMdd");
	}

	// add by duanzengfu 2012/08/17 end

	/**
	 * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param days
	 *            增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * 取得指定日期以后若干分钟的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param minutes
	 *            增加的分钟数
	 * @return 增加以后的日期
	 */
	public static Date addMinutes(Date date, int minutes) {
		return add(date, minutes, Calendar.MINUTE);
	}

	/**
	 * 取得指定日期以后若干月的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param months
	 *            增加的月数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * 取得指定日期以后若干年的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date
	 *            基准日期
	 * @param years
	 *            增加的年数
	 * @return 增加以后的日期
	 */
	public static Date addYears(Date date, int years) {
		return add(date, years, Calendar.YEAR);
	}

	/**
	 * 内部方法。为指定日期增加相应的天数或月数
	 * 
	 * @param date
	 *            基准日期
	 * @param amount
	 *            增加的数量
	 * @param field
	 *            增加的单位，年，月或者日
	 * @return 增加以后的日期
	 */
	private static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(field, amount);

		return calendar.getTime();
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差月份数
	 */
	public static int diffMonths(Date one, Date two) {

		Calendar calendar = Calendar.getInstance();

		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);

		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);

		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差年数
	 */
	private static int diffYear(Date one, Date two) {

		Calendar calendar = Calendar.getInstance();

		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);

		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);

		return (yearOne - yearTwo);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数<br>
	 * 前后日期格式必须为yyyyMMdd
	 * 
	 * @param one
	 *            第一个日期数，作为基准
	 * @param two
	 *            第二个日期数，作为比较
	 * @return 两个日期相差年数
	 */
	public static int diffYear(String one, String two) {
		return diffYear(parse(one, ""), parse(two, ""));
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。 <br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr
	 *            需要解析的日期字符串
	 * @param pattern
	 *            日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 */
	public static Date parse(String datestr, String pattern) {
		Date date = null;

		if (!CheckUtil.isBlankOrNull(datestr)) {
			if (null == pattern || "".equals(pattern)) {
				pattern = DEFAULT_DATE_FORMAT;
			}

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				date = dateFormat.parse(datestr);
			} catch (ParseException e) {
				//
				e.printStackTrace();
			}
		}

		return date;
	}

	/**
	 * yyyy-MM-dd HH:mm字符转日期
	 * 
	 * @param datestr
	 *            源字符
	 * @return 目标日期
	 */
	public static Date parse(String datestr) {
		return parse(datestr, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 返回本月的最后一天
	 * 
	 * @return 本月最后一天的日期
	 */
	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * 返回给定日期中的月份中的最后一天
	 * 
	 * @param date
	 *            基准日期
	 * @return 该月最后一天的日期
	 */
	public static Date getMonthLastDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);

		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);

		return calendar.getTime();
	}

	/**
	 * 判断是否在有效期
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return 当在有效期时返回True,否则False
	 */
	public static boolean isValidDate(String strStartDate, String strEndDate) {
		boolean bResult = false;
		String strSysDate = getDateTimeStr(0);

		// 开始或结束日期为空表示无此限制
		if (null == strStartDate || "".equals(strStartDate))
			strStartDate = "19700101";
		if (null == strEndDate || "".equals(strEndDate))
			strEndDate = "99999999";

		// 判断日期是否在有效期
		bResult = (strStartDate.compareTo(strSysDate) <= 0 && strEndDate.compareTo(strSysDate) >= 0);

		// 返回值
		return bResult;
	}

	public static boolean isValidDate(String sDate) {
		if (CheckUtil.isEmpty(sDate)) {
			return false;
		}
		try {
			Date nowdate = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.CHINA);
			Date d = sdf.parse(sDate + " 24:00:00");

			boolean flag = d.after(nowdate);
			if (flag) {
				return ValidDate(sDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断日期格式:yyyymmdd
	 * 
	 * @param sDate
	 * @return
	 */
	public static boolean ValidDate(String sDate) {
		String datePattern1 = "\\d{4}\\d{2}\\d{2}";
		String datePattern2 = "^((\\d{2}(([02468][048])|([13579][26]))" + "[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
				+ "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?("
				+ "(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?" + "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(sDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2);
				match = pattern.matcher(sDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 取得系统时间前后(+ -)月数
	 * 
	 * @param strMonthConunt
	 *            月数
	 * @return 计算后年月
	 */
	public String getAroundDate(String strMonthConunt) {
		String strResult = "";
		if (!CheckUtil.isNumeric(strMonthConunt))
			return strResult;
		// 取得相应年月
		strResult = this.aroundDate(getDateTimeDte(1), strMonthConunt);
		return strResult;
	}

	/**
	 * 加/减X个月
	 * 
	 * @param date
	 *            日期基数
	 * @param strMonthConunt
	 *            月数 正数表示加,负数表示减
	 * @return 计算后日期
	 */
	private String aroundDate(Date date, String strMonthConunt) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();

		// 设置传入时间
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + Integer.valueOf(strMonthConunt));
		return df.format(calendar.getTime());
	}

	/**
	 * 加/减X个月
	 * 
	 * @param strDate
	 *            日期基数
	 * @param strMonthCount
	 *            月数 正数表示加,负数表示减
	 * @return 计算后日期
	 */
	public String getAroundDate(String strDate, String strMonthCount) {

		// 定义日期
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		try {

			// 字符类型转换为日期类型
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 设置传入时间
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + Integer.valueOf(strMonthCount));
		return sdf.format(calendar.getTime());

	}

	// add by daiguoming, 2011/7/18, end

	/**
	 * 返回要求格式日期
	 * 
	 * @param iType
	 *            类型 ==================================================== Type
	 *            格式 0 yyyyMMddHHmmss TO yyyy-MM-dd HH:mm 1 yyyyMMdd TO
	 *            yyyy-MM-dd 2 yyyyMMdd HH:mm:ss TO yyyy-MM-dd HH 3 yyyyMMdd TO
	 *            yyyy-MM 4 yyyyMMdd HH:mm TO yyyy-MM-dd HH:mm
	 *            ====================================================
	 * @param strDate
	 *            原值
	 * @return 目的值
	 */
	public static String formatDate(int iType, String strDate) {
		String strResult = "";
		if (strDate == null || "".equals(strDate.trim()))
			return strResult;

		// 源格式
		SimpleDateFormat oriDateFormat = new SimpleDateFormat();

		// 目的格式
		SimpleDateFormat tarDateFormat = new SimpleDateFormat();

		switch (iType) {
		case 0:
			oriDateFormat.applyPattern("yyyyMMddHHmmss");
			tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
			break;
		case 1:
			oriDateFormat.applyPattern("yyyyMMdd");
			tarDateFormat.applyPattern("yyyy-MM-dd");
			break;
		case 2:
			oriDateFormat.applyPattern("yyyyMMdd HH:mm:ss");
			tarDateFormat.applyPattern("yyyy-MM-dd HH");
			break;
		case 3:
			oriDateFormat.applyPattern("yyyyMMdd");
			tarDateFormat.applyPattern("yyyy-MM");
			break;
		case 4:
			oriDateFormat.applyPattern("yyyyMMdd HH:mm");
			tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
			break;
		default:
			oriDateFormat.applyPattern("yyyyMMdd");
			tarDateFormat.applyPattern("yyyy-MM-dd");
		}

		try {

			// 格式转换
			strResult = tarDateFormat.format(oriDateFormat.parse(strDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return strResult;
	}

	/**
	 * 返回要求格式日期
	 * 
	 * @param iType
	 *            类型 ==================================================== Type
	 *            格式 0 yyyyMMddHHmmss TO yyyy-MM-dd HH:mm 1 yyyyMMdd TO
	 *            yyyy-MM-dd 2 yyyyMMdd HH:mm:ss TO yyyy-MM-dd HH 3 yyyyMMdd TO
	 *            yyyy-MM 4 yyyyMMdd HH:mm TO yyyy-MM-dd HH:mm
	 *            ====================================================
	 * @param strDate
	 *            原值
	 * @return 目的值
	 */
	// public static String formatDate(int iType, String strDate) {
	// String strResult = "";
	// if (strDate == null || "".equals(strDate.trim())) return strResult;
	//
	// // 源格式
	// SimpleDateFormat oriDateFormat = new SimpleDateFormat();
	//
	// // 目的格式
	// SimpleDateFormat tarDateFormat = new SimpleDateFormat();
	//
	//
	// switch (iType) {
	// case 0:
	// oriDateFormat.applyPattern("yyyyMMddHHmmss");
	// tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
	// break;
	// case 1:
	// oriDateFormat.applyPattern("yyyyMMdd");
	// tarDateFormat.applyPattern("yyyy-MM-dd");
	// break;
	// case 2:
	// oriDateFormat.applyPattern("yyyyMMdd HH:mm:ss");
	// tarDateFormat.applyPattern("yyyy-MM-dd HH");
	// break;
	// case 3:
	// oriDateFormat.applyPattern("yyyyMMdd");
	// tarDateFormat.applyPattern("yyyy-MM");
	// break;
	// case 4:
	// oriDateFormat.applyPattern("yyyyMMdd HH:mm");
	// tarDateFormat.applyPattern("yyyy-MM-dd HH:mm");
	// break;
	// default:
	// oriDateFormat.applyPattern("yyyyMMdd");
	// tarDateFormat.applyPattern("yyyy-MM-dd");
	// }
	//
	// try {
	//
	// // 格式转换
	// strResult = tarDateFormat.format(oriDateFormat.parse(strDate));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return strResult;
	// }
	//
	// // add by duanzengfu 2012/09/04 start
	// /**
	// * 将yyyymmdd转换为yyyy-mm-dd
	// *
	// * @param strDate 日期原值
	// * @return 转换后的日期字符串
	// */
	// public static String parseDateFmt(String strDate) {
	// return formatDate(1, strDate);
	// }

	/**
	 * 将yyyyMMddHHmmss转换为yyyy-MM-dd HH:mm
	 * 
	 * @param strDate
	 *            源值
	 * @return 格式化后值
	 */
	public static String paresDateLongFmt(String strDate) {
		return formatDate(0, strDate);
	}

	/**
	 * 将yyyyMMdd HH:mm转换为yyyy-MM-dd HH:mm
	 * 
	 * @param strDate
	 *            源值
	 * @return 格式化后值
	 */
	public static String paresDateLongToLong(String strDate) {
		return formatDate(4, strDate);
	}

	/**
	 * 判断输入值是否为指定格式的合法日期
	 * 
	 * @param strDate
	 *            8位日期字符串
	 * @return 合法则返回true,否则返回false
	 */
	public Boolean checkDate(String strDate) {

		try {
			// 如果输入日期不是8位的,判定为false.
			if (null == strDate || "".equals(strDate) || !strDate.matches("[0-9]{8}")) {
				return false;
			}
			int year = Integer.parseInt(strDate.substring(0, 4));
			int month = Integer.parseInt(strDate.substring(4, 6)) - 1;
			int day = Integer.parseInt(strDate.substring(6));
			Calendar calendar = Calendar.getInstance();

			// 当 Calendar 处于 non-lenient 模式时，
			// 如果其日历字段中存在任何不一致性，它都会抛出一个异常。
			calendar.setLenient(false);
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DATE, day);

			// 如果日期错误,执行该语句,必定抛出异常.
			calendar.get(Calendar.YEAR);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据format格式要求，把date转换为相应的字符串
	 * 
	 * @param format
	 *            要求格式
	 * @param date
	 *            待转换对象
	 * @return 按照要求转换后的字符串
	 */
	public static String getDateToStr(String format, Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 将日期转换为长字符串（包含：年-月-日 时:分）
	 * 
	 * @param date
	 *            日期
	 * @return 返回型如：yyyy-MM-dd HH:mm 的字符串
	 */
	public static String getDateToLStr(Date date) {
		if (null == date)
			return "";
		return getDateToStr("yyyy-MM-dd HH:mm", date);
	}

	/**
	 * 将日期转换为字符串（包含：年-月-日 ）
	 * 
	 * @param date
	 *            日期
	 * @return 返回型如：yyyy-MM-dd的字符串
	 */
	public static String getDateToTagStr(Date date) {
		if (null == date)
			return "";
		return getDateToStr("yyyy-MM-dd", date);
	}

	/**
	 * 生成yyyy_MM_dd_HH_mm_ss格式的时间字符串
	 * 
	 * @param date
	 *            需要格式化的日期对象
	 * @return 格式化后的字符串
	 */
	public static String getString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 生成指定格式的时间字符串
	 * 
	 * @param date
	 *            需要格式化的日期对象
	 * @param format
	 *            格式化字符串
	 * @return 格式化后的字符串
	 */
	public synchronized static String getString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 生成指定格式的时间字符串
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param format
	 *            格式化字符串
	 * @return 解析后的日期对象
	 */
	public static Date getDate(String dateStr, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// ----------------------------------------------------------------------------------
	// 计算保险的结束日期（加一年减一天）
	public static String getDateTime(int year, int month, int day) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year + 1); // year 为 int
		time.set(Calendar.MONTH, month - 1);// 注意,Calendar对象默认一月为0
		time.set(Calendar.DAY_OF_MONTH, day - 1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(time.getTime());
	}

	/**
	 * 获得年份列表
	 * 
	 * @return 年份字符串数组
	 */
	public static String[] getYear() {
		String[] year = new String[7];
		year[0] = "请选择年份";
		Date date = new Date(System.currentTimeMillis());
		int y = date.getYear() + 1900;
		int index = 0;
		for (int i = 1; i < 7; i++) {
			year[i] = "" + (y - index);
			index++;
		}
		return year;
	}

	/**
	 * 将毫秒转换为时分秒
	 */
	public static String shiftMsToTime(Object milliscond) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String time = formatter.format(milliscond);
		return time;
	}

	/**
	 * // long类型转换为String类型 // currentTime要转换的long类型的时间 //
	 * formatType要转换的string类型的时间格式
	 * 
	 * @param currentTime
	 * @param formatType
	 * @return format time
	 */
	public static String longToString(long currentTime, String formatType) {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	/**
	 * // string类型转换为date类型 // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd
	 * HH:mm:ss//yyyy年MM月dd日 // HH时mm分ss秒， // strTime的时间格式必须要与formatType的时间格式相同
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 */
	public static Date stringToDate(String strTime, String formatType) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(formatType);
			date = formatter.parse(strTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * // long转换为Date类型 // currentTime要转换的long类型的时间 //
	 * formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * @param currentTime
	 * @param formatType
	 * @return
	 */
	public static Date longToDate(long currentTime, String formatType) {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	/**
	 * string类型转换为long类型 strTime要转换的String类型的时间 formatType时间格式'
	 * strTime的时间格式和formatType的时间格式必须相同
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 */
	public static long stringToLong(String strTime, String formatType) {
		try {
			Date date;
			date = stringToDate(strTime, formatType);// String类型转成date类型
			if (date == null) {
				return 0;
			} else {
				long currentTime = dateToLong(date); // date类型转成long类型
				return currentTime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * date类型转换为long类型 date要转换的date类型的时间
	 * 
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}
	
	
	/**
	 * 判断是否是当天
	 * @param time
	 * @return
	 */
	public static Boolean isCurrentDate(String time) {

		SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		if (time == null || "".equals(time)) {
			return false;
		}
		Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // 今天

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // 昨天

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if (current.after(today)) {
			return true;
		}

		return false;
	}

	
}