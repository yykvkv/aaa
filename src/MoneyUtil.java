package com.grgbanking.core.utils;




import java.math.BigDecimal;

/**
 * 金额转换工具
 *
 * @author czbao
 *
 */
public class MoneyUtil {

	/**
	 * 判断是否为带小数的金额字串("123.9":是, "123":否, "123.12.2":否, "122ds.22":否)
	 * 
	 * @param DotStrAmt
	 * @return boolean
	 */
	public static boolean isDotAmountStr(String DotStrAmt) throws Exception{
		int locb, loce;
		if (DotStrAmt == null)
			return false;

		locb = DotStrAmt.indexOf('.');
		if (locb < 0)
			return false;
		loce = DotStrAmt.lastIndexOf('.');
		/* 字符串金额含两个以上小数，非法 */
		if (locb != loce)
			return false;
		String tStr = DotStrAmt.replace('.', '0');
		return isNumberstr(tStr);
	}

	/**
	 * 判断字符串是否全部数字("123.9":否, "123":是, "123.12.2":否, "122ds.22":否)
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumberstr(String str) throws Exception{
		if (str == null)
			return (false);
		int len = str.length();
		if (len == 0)
			return (false);

		if (len != str.getBytes("UTF8").length)
			return (false);

		for (int i = 0; i < len; i++) {
			if ((str.charAt(i) > '9') || (str.charAt(i) < '0'))
				return (false);
		}
		return (true);
	}

	/**
	 * 不带小数的字符串转换为金额("000000012310"->123.1)
	 * 
	 * @param strAmount
	 * @return double
	 */
	public static double strAmount2D(String strAmount) throws Exception{
		double damount = 0.0;
		if (strAmount == null) {
			strAmount = "0";
		}
		if (strAmount != null && strAmount.length() > 0) {
			damount = Double.parseDouble(strAmount.trim());
			damount = damount / 100.0;
		}
		return damount;
	}

	/**
	 * 不带小数的字符串转换为带小数的字符串("000000012310"->"123.10")
	 * 
	 * @param strAmount
	 * @return String
	 */
	public static String strAmount2S(String strAmount) throws Exception{
		if (strAmount == null || strAmount.length() == 0) {
			strAmount = "0000";
		}
		long longAmount = Long.parseLong(strAmount) / 100;
		String strRet = "0000" + strAmount;
		return Long.toString(longAmount) + "."
				+ strRet.substring(strRet.length() - 2);
	}

	/**
	 * 金额转换为带小数的字符串(123.1->"123.10")
	 * 
	 * @param dAmount
	 * @return String
	 */
	public static String DAmount2S(double dAmount) throws Exception{
		return strAmount2S(DAmount2Str(dAmount));
	}

	/**
	 * 金额(带正负号)转换为不带小数的字符串(123.1->"12310", -123.1->"-12310")
	 * 
	 * @param dAmount
	 * @return String
	 */
	public static String DAmount2Str(double dAmount) throws Exception{
		if (round(dAmount, 2) <= -0.005)
			return "-" + DAmount2StrPositive(dAmount);
		return DAmount2StrPositive(dAmount);
	}

	/**
	 * 金额转换为不带小数的字符串(123.1->"12310")
	 * 
	 * @param dAmount
	 * @return String
	 */
	public static String DAmount2StrPositive(double dAmount) throws Exception{
		String strRet;
		dAmount = Math.abs(dAmount);

		BigDecimal b = new BigDecimal(Double.toString(100 * round(dAmount, 2)));
		strRet = b.toBigInteger().toString();
		return strRet;
	}

	/**
	 * 金额转换为不带小数的字符串(123.1->"000000012310")
	 * 
	 * @param dAmount
	 * @param strlen
	 * @return String
	 */
	public static String DAmount2StrPositive(double dAmount, int strlen) throws Exception{
		String strRet;
		StringBuffer strZero = new StringBuffer("");
		for (int i = 0; i < strlen; i++) {
			strZero.append("0") ;
		}
		dAmount = Math.abs(dAmount);

		BigDecimal b = new BigDecimal(Double.toString(100 * round(dAmount, 2)));
		String strAmount = b.toBigInteger().toString();

		int len = strAmount.length();
		if (len < strlen) {
			strRet = strZero.substring(0, strlen - len) + strAmount;
		} else {
			strRet = strAmount;
		}
		return strRet;
	}

	/**
	 * 金额转换为带千分位分隔符的字符串(1234567890.12->"1,234,567,890.12")
	 * 
	 * @param dAmount
	 * @return String
	 */
	public static String transAmountFormat(double dAmount) throws Exception{
		return String.format("%1$,.2f", dAmount);
	}

	/**
	 * 金额转换为带千分位分隔符的字符串(1234567890.12->"1,234,567,890.12")
	 * 
	 * @param dAmount
	 * @param len
	 * @return String
	 */
	public static String transAmountFormat(double dAmount, int len) throws Exception {
		return String.format("%1$,." + len + "f", dAmount);
	}

	/**
	 * 金额转换为千分位分隔符的字符串("1234567890.12"->"1,234,567,890.12")
	 * 
	 * @param StrAmount
	 * @return String
	 */
	public static String transAmountFormat(String StrAmount) throws Exception {
		return transAmountFormat(Double.valueOf(StrAmount));
	}

	/**
	 * 提供2位小数位四舍五入处理(4.015 -> 4.12)
	 * 
	 * @param v
	 * @return double
	 */
	public static double round(double v) throws Exception {
		return round(v, 2);
	}

	/**
	 * 提供精确的小数位四舍五入处理(4.015,2 -> 4.12)
	 * 
	 * @param v
	 * @param scale
	 * @return double
	 */
	public static double round(double v, int scale) throws Exception{
		if (scale < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");

		return Math.round((v * ((double) (Math.pow(10, scale) + 0.5)) + 0.5)) / ((double) (Math.pow(10, scale) + 0.5));
	}

	public static void main(String[] args) throws Exception {
		/* 是否为带小数的金额字串"123.9":是, "123":否, "123.12.2":否, "122ds.22":否 */
		if (isDotAmountStr("12.39")) {
		} else {
		}

		/* 判断字符串是否全部数字("123.9":否, "123":是, "123.12.2":否, "122ds.22":否) */
		if (isNumberstr("12.39")) {
		} else {
		}

//		/* 不带小数的字符串转换为金额 "000000012310"->123.1 */
//		System.out.println("\'000000012310\' doublevalue="
//				+ strAmount2D("000000012310"));
//
//		/* 不带小数的字符串转换为带小数的字符串"000000012310"->"123.10" */
//		System.out.println("\'000000012310\' stringvalue=\'"
//				+ strAmount2S("000000012310") + "\'");
//
//		/* 金额转换为带小数的字符串 123.1->"123.10" */
//		System.out.println("123.1 stringvalue=\'" + DAmount2S(123.1) + "\'");
//
//		/* 金额转换为不带小数的字符串 123.1->"12310" */
//		System.out.println("123.1 formatstring=\'" + DAmount2StrPositive(123.1)
//				+ "\'");
//
//		/* 金额转换为不带小数的字符串 123.1->"000000012310" */
//		System.out.println("123.1 formatstring=\'"
//				+ DAmount2StrPositive(123.1, 12) + "\'");
//
//		/* 金额转换为带千分位分隔符的字符串 1234567890.12->"1,234,567,890.12" */
//		System.out.println("1234567890.12 formatstring=\'"
//				+ transAmountFormat(1234567890.12) + "\'");
//
//		/* 金额转换为带千分位分隔符的字符串 "1234567890.12"->"1,234,567,890.12" */
//		System.out.println("\'1234567890.12\' formatstring=\'"
//				+ transAmountFormat("1234567890.12") + "\'");
	}
}
