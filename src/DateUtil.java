package com.grgbanking.core.utils;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理工具类
 * 
 * @date Kevin 04, 2018
 *
 *
 */
public class DateUtil {
	private static String defaultDatePattern = null;
	private static String timePattern = "HH:mm";

	/** 日期格式yyyy-MM字符串常量 */
	private static final String MONTH_FORMAT = "yyyy-MM";
	/** 日期格式yyyy-MM-dd字符串常量 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 日期格式HH:mm:ss字符串常量 */
	private static final String HOUR_FORMAT = "HH:mm:ss";
	/** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 某天开始时分秒字符串常量 00:00:00 */
	private static final String DAY_BEGIN_STRING_HHMMSS = " 00:00:00";
	/** 某天结束时分秒字符串常量 23:59:59 */
	public static final String DAY_END_STRING_HHMMSS = " 23:59:59";

	public static final String DATETIME_FORMAT_WITHOUTSPLITER = "yyyyMMddHHmmss";

	public static final String TIME_MILLISECOND_FORMAT_WITHOUTSPLITER = "HHmmssSSS";

	public DateUtil() {
	}

	/**
	 * 获得服务器当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
	 *
	 * @date  04, 2018
	 * @return
	 */
	public static String getDateTime() throws Exception{
		SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);
		Calendar cale = Calendar.getInstance();
		return sdf_datetime_format.format(cale.getTime());
	}


	/**
	 * 获取当前系统服务器时间，格式为传入参数格式
	 * 
	 * @param datetime_format
	 * @return
	 */
	public static String getDateTime(String datetime_format) throws Exception{
		SimpleDateFormat time_format = new SimpleDateFormat(datetime_format);
		Calendar cale = Calendar.getInstance();
		return time_format.format(cale.getTime());
	}

	/**
	 * 获得服务器当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getDate() throws Exception{
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(
				DATE_FORMAT);
		Calendar cale = Calendar.getInstance();
		return sdf_date_format.format(cale.getTime());
	}

	/**
	 * 获得服务器当前时间，以格式为：HH:mm:ss的日期字符串形式返回
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getTime() throws Exception{
		SimpleDateFormat sdf_hour_format = new SimpleDateFormat(HOUR_FORMAT);
		String temp = " ";
		Calendar cale = Calendar.getInstance();
		temp += sdf_hour_format.format(cale.getTime());
		return temp;
	}

	/**
	 * 统计时开始日期的默认值
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getStartDate() throws Exception{
		return getYear() + "-01-01";
	}

	/**
	 * 统计时结束日期的默认值
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getEndDate() throws Exception{
		return getDate();
	}

	/**
	 * 获得服务器当前日期的年份
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getYear() throws Exception{
		Calendar cale = Calendar.getInstance();
		return String.valueOf(cale.get(Calendar.YEAR));
	}

	/**
	 * 获得服务器当前日期的月份
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getMonth() throws Exception{
		Calendar cale = Calendar.getInstance();
		java.text.DecimalFormat df = new java.text.DecimalFormat();
		df.applyPattern("00;00");
		return df.format((cale.get(Calendar.MONTH) + 1));

	}

	/**
	 * 获得服务器在当前月中天数
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getDay() throws Exception{
		Calendar cale = Calendar.getInstance();
		return String.valueOf(cale.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 比较两个日期相差的天数
	 * 
	 * @date  04, 2018
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMargin(String date1, String date2) throws Exception{
		int margin;
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = sdf_date_format.parse(date1, pos);
		Date dt2 = sdf_date_format.parse(date2, pos1);
		long l = dt1.getTime() - dt2.getTime();
		margin = (int) (l / (24 * 60 * 60 * 1000));
		return margin;
	}

	/**
	 * 比较两个日期相差的天数
	 * 
	 * @date  04, 2018
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static double getDoubleMargin(String date1, String date2) throws Exception{
		double margin;
		SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		ParsePosition pos1 = new ParsePosition(0);
		Date dt1 = sdf_datetime_format.parse(date1, pos);
		Date dt2 = sdf_datetime_format.parse(date2, pos1);
		long l = dt1.getTime() - dt2.getTime();
		margin = (l / (24 * 60 * 60 * 1000.00));
		return margin;

	}

	/**
	 * 比较两个日期相差的月数
	 * 
	 * @date  04, 2018
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getMonthMargin(String date1, String date2) throws Exception{
		int margin;

		margin = (Integer.parseInt(date2.substring(0, 4)) - Integer
				.parseInt(date1.substring(0, 4))) * 12;
		margin += (Integer.parseInt(date2.substring(4, 7).replaceAll(
				"-0",
				"-")) - Integer.parseInt(date1.substring(4, 7).replaceAll(
				"-0", "-")));
		return margin;

	}

	/**
	 * 返回日期加X天后的日期
	 * 
	 * @date  04, 2018
	 * @param date
	 * @param i
	 * @return
	 */
	public static String addDay(String date, int i) throws Exception{
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);
		GregorianCalendar gCal = new GregorianCalendar(Integer
				.parseInt(date.substring(0, 4)), Integer.parseInt(date
				.substring(5, 7)) - 1, Integer.parseInt(date.substring(8,
				10)));
		gCal.add(GregorianCalendar.DATE, i);
		return sdf_date_format.format(gCal.getTime());
	}

	/**
	 * 返回日期加X月后的日期
	 *
	 * @param date
	 * @param i
	 * @return
	 */
	public static String addMonth(String date, int i) throws Exception{
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);
		GregorianCalendar gCal = new GregorianCalendar(Integer
				.parseInt(date.substring(0, 4)), Integer.parseInt(date
				.substring(5, 7)) - 1, Integer.parseInt(date.substring(8,
				10)));
		gCal.add(GregorianCalendar.MONTH, i);
		return sdf_date_format.format(gCal.getTime());
	}

	/**
	 * 返回日期加X年后的日期
	 *
	 * @param date
	 * @param i
	 * @return
	 */
	public static String addYear(String date, int i) throws Exception{
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);
		GregorianCalendar gCal = new GregorianCalendar(Integer
				.parseInt(date.substring(0, 4)), Integer.parseInt(date
				.substring(5, 7)) - 1, Integer.parseInt(date.substring(8,
				10)));
		gCal.add(GregorianCalendar.YEAR, i);
		return sdf_date_format.format(gCal.getTime());
	}

	/**
	 * 返回某年某月中的最大天
	 *
	 * @return
	 */
	public static int getMaxDay(int iyear, int imonth) throws Exception{
		int day = 0;
		if (imonth == 1 || imonth == 3 || imonth == 5 || imonth == 7
				|| imonth == 8 || imonth == 10 || imonth == 12) {
			day = 31;
		} else if (imonth == 4 || imonth == 6 || imonth == 9
				|| imonth == 04) {
			day = 30;
		} else if ((0 == (iyear % 4)) && (0 != (iyear % 100))
				|| (0 == (iyear % 400))) {
			day = 29;
		} else {
			day = 28;
		}
		return day;
	}


	public static String rollDate(Calendar cal, int Type, int Span) throws Exception{
		SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);
		String temp = "";
		Calendar rolcale;
		rolcale = cal;
		rolcale.add(Type, Span);
		temp = sdf_date_format.format(rolcale.getTime());
		return temp;

	}

	/**
	 * 返回默认的日期格式
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static synchronized String getDatePattern() throws Exception{
		defaultDatePattern = "yyyy-MM-dd";
		return defaultDatePattern;
	}

	/**
	 * 将指定日期按默认格式进行格式代化成字符串后输出如：yyyy-MM-dd
	 * 
	 * @date  04, 2018
	 * @param aDate
	 * @return
	 */
	public static final String getDate(Date aDate) throws Exception{
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 取得给定日期的时间字符串，格式为当前默认时间格式
	 * 
	 * @date  04, 2018
	 * @param theTime
	 * @return
	 */
	public static String getTimeNow(Date theTime) throws Exception{
		return getDateTime(timePattern, theTime);
	}

	/**
	 * 取得当前时间的Calendar日历对象
	 * 
	 * @date  04, 2018
	 * @return
	 * @throws ParseException
	 */
	public Calendar getToday() throws Exception {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));
		return cal;
	}

	/**
	 * 将日期类转换成指定格式的字符串形式
	 * 
	 * @date  04, 2018
	 * @param aMask
	 * @param aDate
	 * @return
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {

		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 将指定的日期转换成默认格式的字符串形式
	 * 
	 * @date  04, 2018
	 * @param aDate
	 * @return
	 */
	public static final String convertDateToString(Date aDate)throws Exception {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * 将日期字符串按指定格式转换成日期类型
	 * 
	 * @date  04, 2018
	 * @param aMask
	 *            指定的日期格式，如:yyyy-MM-dd
	 * @param strDate
	 *            待转换的日期字符串
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws Exception{
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		date = df.parse(strDate);
		return (date);
	}

	/**
	 * 将日期字符串按默认格式转换成日期类型
	 * 
	 * @date  04, 2018
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) throws Exception{
		Date aDate = null;
		aDate = convertStringToDate(getDatePattern(), strDate);
		return aDate;
	}

	/**
	 * 返回一个JAVA简单类型的日期字符串
	 * 
	 * @date  04, 2018
	 * @return
	 */
	public static String getSimpleDateFormat() throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat();
		String NDateTime = formatter.format(new Date());
		return NDateTime;
	}

	/**
	 * 将指定字符串格式的日期与当前时间比较
	 * 
	 * @date Feb 17, 2018
	 * @param strDate
	 *            需要比较时间
	 * @return <p>
	 *         int code
	 *         <ul>
	 *         <li>-1 当前时间 < 比较时间</li>
	 *         <li>0 当前时间 = 比较时间</li>
	 *         <li>>=1当前时间 > 比较时间</li>
	 *         </ul>
	 *         </p>
	 */
	public static int compareToCurTime(String strDate) throws Exception{
		SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);
		if (StringUtils.isBlank(strDate)) {
			return -1;
		}
		Calendar cale = Calendar.getInstance();
		Date curTime = cale.getTime();
		String strCurTime = null;
		strCurTime = sdf_datetime_format.format(curTime);
		if (StringUtils.isNotBlank(strCurTime)) {
			return strCurTime.compareTo(strDate);
		}
		return -1;
	}

	/**
	 * 为查询日期添加最小时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date addStartTime(Date param) throws Exception{
		Date date = param;
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}

	/**
	 * 为查询日期添加最大时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date addEndTime(Date param) throws Exception{
		Date date = param;

		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(0);
		return date;

	}

	/**
	 * 返回系统现在年份中指定月份的天数
	 * 
	 * @param month
	 * @return 指定月的总天数
	 */
	@SuppressWarnings("deprecation")
	public static String getMonthLastDay(int month) throws Exception{
		Date date = new Date();
		int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
		int year = date.getYear() + 1900;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return day[1][month] + "";
		}
		else {
			return day[0][month] + "";
		}
	}

	/**
	 * 返回指定年份中指定月份的天数
	 * 
	 * @param year
	 * @param month
	 * @return 指定月的总天数
	 */
	public static String getMonthLastDay(int year, int month) throws Exception{
		int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return day[1][month] + "";
		}
		else {
			return day[0][month] + "";
		}
	}

	/**
	 * 判断是平年还是闰年
	 * 
	 * @author  04, 2018
	 * @date 04, 2018
	 * @param year
	 * @return
	 */
	public static boolean isLeapyear(int year) throws Exception{
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400) == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 取得当前时间的日戳
	 * 
	 * @date  04, 2018
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getTimestamp() throws Exception{
		Calendar cale = Calendar.getInstance();
		Date date = cale.getTime();
		String timestamp = "" + (date.getYear() + 1900) + date.getMonth()
				+ date.getDate() + date.getMinutes() + date.getSeconds()
				+ date.getTime();
		return timestamp;
	}

	/**
	 * 取得指定时间的日戳
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getTimestamp(Date date) throws Exception{
		String timestamp = "" + (date.getYear() + 1900) + date.getMonth()
				+ date.getDate() + date.getMinutes() + date.getSeconds()
				+ date.getTime();
		return timestamp;
	}

	/**
	 * 由时间字符串获取时间戳
	 * 
	 * @param s
	 * @return
	 */
	public static String dateToStamp(String s) throws Exception{
		String res = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmss");
		Date date;
		date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param aMask
	 *            转传承的样式
	 * @param defultMask
	 *            传入的默认格式
	 * @param strDate
	 *            日期格式
	 * @return
	 */
	public static final Date convertStringToDate(String aMask,
			String defultMask, String strDate) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat(aMask);
		SimpleDateFormat df1 = new SimpleDateFormat(defultMask);
		Date date = null;
		date = df.parse(df.format(df1.parse(strDate)));
		return (date);
	}

	/**
	 * 输入日期格式，计算差值后，按照输入格式传出
	 * 
	 * @param aMask
	 *            日期输入、输出格式
	 * @param strDate
	 *            日期信息
	 * @param dif
	 *            日期差
	 * @return
	 * @throws Exception
	 */
	public static final String dateCalculate(String aMask, String strDate,
			String dif) throws Exception {
		Date date = convertStringToDate(aMask, strDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int iDif = Integer.parseInt(dif);
		calendar.add(Calendar.DATE, iDif);
		date = calendar.getTime();
		String dateStr = getDateTime(aMask, date);
		return dateStr;
	}

	/**
	 * 输入日期格式，计算差值后，按照指定格式传出
	 * 
	 * @param inMask
	 *            日期输入格式
	 * @param strDate
	 *            日期信息
	 * @param outMask
	 *            日期输出格式
	 * @param dif
	 *            日期差
	 * @return
	 * @throws Exception
	 */
	public static final String dateCalculate(String inMask, String strDate,
			String outMask, String dif) throws Exception {
		Date date = convertStringToDate(inMask, strDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int iDif = Integer.parseInt(dif);
		calendar.add(Calendar.DATE, iDif);
		date = calendar.getTime();
		String dateStr = getDateTime(outMask, date);
		return dateStr;
	}

	/**
	 * 将日期信息按照指定的格式输出
	 * 
	 * @param inMask
	 *            输入格式
	 * @param strDate
	 *            输入日期
	 * @param outMask
	 *            输出格式
	 * @return
	 * @throws Exception
	 */
	public static final String convertStringToDateStr(String inMask,
			String strDate, String outMask) throws Exception {
		Date date = DateUtil.convertStringToDate(inMask, strDate);
		String dateStr = getDateTime(outMask, date);
		return dateStr;
	}

	/**
	 * 根据输入日期 及 相差秒数 获取计算后的时间。精确到秒数
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差秒数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForS(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差分钟 获取计算后的时间。精确到分钟
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差分钟
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForM(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差小时 获取计算后的时间。精确到小时
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差小时
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForH(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差天数 获取计算后的时间。精确到天
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差天数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForD(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差月 获取计算后的时间。精确到月
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差月数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForMo(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差季数 获取计算后的时间。精确到季数
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差季数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForQ(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, dif * 3);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差半年数 获取计算后的时间。精确到半年数
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差半年数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForHY(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, dif * 6);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 根据输入日期 及 相差年数 获取计算后的时间。精确到年数
	 * 
	 * @param date
	 *            输入日期
	 * @param dif
	 *            相差年数
	 * @return 计算后时间
	 * @throws Exception
	 */
	public static final Date dateCalculateForY(Date date, int dif)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, dif);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 时间戳转换成时间字符串
	 *
	 * @param s
	 * @return
	 */
	public static String stampToDate(long s) throws Exception{
		String res = null;
		String format="HH:mm:ss:SSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		res = simpleDateFormat.format(new Date(s));
		return res;
	}

	public static String dateToString(Date date, String format) {
		String result = "";
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);

		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	public static String getGMTDate(){
		Calendar cal = Calendar.getInstance();
		// Locale.US用于将日期区域格式设为美国（英国也可以）。缺省改参数的话默认为机器设置，如中文系统星期将显示为汉子“星期六”
		SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		// 时区设为格林尼治
		greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));

//        System.out.println("当前时间：" + localDate.format(cal.getTime()));
		String date = greenwichDate.format(cal.getTime());
		return date;
	}

	public static void main(String[] str) {
		try {
			Date date = new Date();
		} catch (Exception e) {

		}

	}


}