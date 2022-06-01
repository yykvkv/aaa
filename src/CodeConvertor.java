package com.grgbanking.core.utils;


import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Code Format Conversion Class
 * @version 1.0 2018.04
 *
 */
public class CodeConvertor {
	
	private static final char[] EBCDIC_CODE = {
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ' ', 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00,  '.',  '<',  '(',  '+',  '|', '&',  0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, '!',  '$',  '*',  ')',  ';',  0x00, '-',  '/',
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ',',  '%',  '_',  '>',  '?', 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, ':',  '#',  '@',  '\'', 
			'=',  '"',  0x00, 'a',  'b',  'c',  'd',  'e',  'f',  'g',  'h',  'i',  0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 'j',  'k',  'l',  'm',  'n',  'o',  'p',  'q',  'r', 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 's',  't',  'u',  'v',  'w',  'x', 
			'y',  'z',  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 'A',  'B',  'C', 
			'D', 'E', 'F', 'G', 'H', 'I', 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 'J',  'K', 
			'L', 'M', 'N', 'O', 'P', 'Q', 'R',  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			'S', 'T', 'U', 'V', 'W', 'X', 'Y',  'Z',  0x00, 0x00, 0x00, 0x00, 0x00, 0x00, '0', 
			'1', '2', '3', '4', '5', '6', '7',  '8',  '9',  0x00, 0x00, 0x00, 0x00, 0x00, 0x00
		};

	/**
	 * asc byte[] 转 16进制 byte[]
	 * 
	 * @param asc
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] ascToHex(byte[] asc){
		if(asc == null)
			return null;
		
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < asc.length; i++) {
			String hv = Integer.toHexString(asc[i] & 0xFF);
			if (hv.length() < 2) {
				hex.append(0);
			}
			hex.append(hv);
		}
		
		return hex.toString().getBytes();
	}
	
	/**
	 * asc String 转 16进制 String
	 * 
	 * @param asc
	 *            String
	 * @return String
	 */
	public static String ascToHex(String asc) {
		if (asc == null)
			return null;

		StringBuilder hex = new StringBuilder();
		byte[] temp = asc.getBytes();
		for (int i = 0; i < temp.length; i++) {
			String hv = Integer.toHexString(temp[i] & 0xFF);
			if (hv.length() < 2) {
				hex.append(0);
			}
			hex.append(hv);
		}

		return hex.toString();
	}
	public static byte[] asc2Hex(byte[] hex) {
		return hexToAsc(hex);

	}

	/**
	 * @param  byte[] 转 asc byte[]
	 * @return
	 */
	public static byte[] hexToAsc(byte[] hex){
		if(hex == null)
			return null;
		
		byte[] asc = new byte[hex.length/2];
		for (int i = 0,j = 0; i < hex.length - 1; i += 2,j++) {
			asc[j] = (byte)Integer.parseInt(new String(hex).substring(i, (i + 2)), 16);
		}
		
		return asc;
	}


	public static byte[] hex2Asc(byte[] hex) {
		return ascToHex(hex);

	}

	/**
	 * 16进制String转asc String
	 * 
	 * @param hex
	 *            String
	 * @return String
	 */
	public static String hexToAsc(String hex) {
		if (hex == null)
			return null;

		byte[] temp = hex.getBytes();
		byte[] asc = new byte[temp.length / 2];
		for (int i = 0, j = 0; i < temp.length - 1; i += 2, j++) {
			asc[j] = (byte) Integer.parseInt(hex.substring(i, (i + 2)), 16);
		}

		return new String(asc);
	}

	private static byte asc_to_bcd(byte asc) {  
		byte bcd;  
	  
	    if ((asc >= '0') && (asc <= '9')) {
			bcd = (byte) (asc - '0');
		} else if ((asc >= 'A') && (asc <= 'F')) {
			bcd = (byte) (asc - 'A' + 10);
		} else if ((asc >= 'a') && (asc <= 'f')) {
			bcd = (byte) (asc - 'a' + 10);
		} else {
			bcd = (byte) (asc - 48);
		}  
	    return bcd;  
	}  
	
	/**
	 * asc byte[] 转bcd byte[]
	 * 
	 * @param asc
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] ascToBcd(byte[] asc){
		if (asc == null)
			return null;

		byte[] bcd = new byte[asc.length / 2];
		int j = 0;
		for (int i = 0; i < (asc.length + 1) / 2; i++) {
			bcd[i] = asc_to_bcd((byte) asc[j++]);
			bcd[i] = (byte) (((j >= asc.length) ? 0x00 : asc_to_bcd((byte) asc[j++]))
					+ (bcd[i] << 4));
		}
		
		return bcd;
	}
	
	/**
	 * asc String 转 bcd String
	 * 
	 * @param asc
	 *            String
	 * @return String
	 */
	public static String ascToBcd(String asc) {
		if (asc == null)
			return null;

		byte[] temp = asc.getBytes();
		byte[] bcd = new byte[temp.length / 2];
		int j = 0;
		for (int i = 0; i < (temp.length + 1) / 2; i++) {
			bcd[i] = asc_to_bcd((byte) temp[j++]);
			bcd[i] = (byte) (((j >= temp.length) ? 0x00
					: asc_to_bcd((byte) temp[j++])) + (bcd[i] << 4));
		}

		return new String(bcd);
	}

	/**
	 * bcd byte[] 转 asc byte[]
	 * 
	 * @param bcd
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] bcdToAsc(byte[] bcd){
		if (bcd == null)
			return null;
		
		byte[] asc = new byte[bcd.length * 2];
		byte val;  

	    for (int i = 0; i < bcd.length; i++) {  
	        val = (byte) (((bcd[i] & 0xf0) >> 4) & 0x0f);  
	        asc[i * 2] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');  
	  
	        val = (byte) (bcd[i] & 0x0f);
	        asc[i * 2 + 1] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');  
	    }

		return asc;
	}
	

	/**
	 * bcd String 转 asc String
	 * 
	 * @param bcd
	 *            String
	 * @return String
	 */
	public static String bcdToAsc(String bcd) {
		if (bcd == null)
			return null;

		byte[] temp = bcd.getBytes();

		byte[] asc = new byte[temp.length * 2];
		byte val;

		for (int i = 0; i < temp.length; i++) {
			val = (byte) (((temp[i] & 0xf0) >> 4) & 0x0f);
			asc[i * 2] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (byte) (temp[i] & 0x0f);
			asc[i * 2 + 1] = (byte) (val > 9 ? val + 'A' - 10 : val + '0');
		}

		return new String(asc);
	}


	/**
	 * asc byte[] 转 Ebcd char[]
	 * 
	 * @param asc
	 *            byte[]
	 * @return char[]
	 */
	public static char[] ascToEbcd(byte[] asc){
		if(asc == null)
			return null;
		
		char[] ebcd = new char[asc.length];
		
		for(int i = 0;i < asc.length;i++)
		{
			for(int j = 0;j < CodeConvertor.EBCDIC_CODE.length ;j++)
			{
				if(asc[i] == CodeConvertor.EBCDIC_CODE[j])
				{
					ebcd[i] = (char) j;
					break;
				}
			}
		}
		
		return ebcd;
	}
	
	/**
	 * asc String 转 Ebcd String
	 * 
	 * @param asc
	 *            String
	 * @return String
	 */
	public static String ascToEbcd(String asc) {
		if (asc == null)
			return null;

		byte[] temp = asc.getBytes();

		char[] ebcd = new char[temp.length];

		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < CodeConvertor.EBCDIC_CODE.length; j++) {
				if (temp[i] == CodeConvertor.EBCDIC_CODE[j]) {
					ebcd[i] = (char) j;
					break;
				}
			}
		}

		return new String(ebcd);
	}


	public static byte[] asc2Ebcd(byte[] asc){
		if(asc == null)
			return null;

		byte[] ebcd = new byte[asc.length];

		for(int i = 0;i < asc.length;i++)
		{
			for(int j = 0;j < CodeConvertor.EBCDIC_CODE.length ;j++)
			{
				if((asc[i]&0xff)== CodeConvertor.EBCDIC_CODE[j])
				{
					ebcd[i] = (byte)j;
					break;
				}
			}
		}

		return ebcd;
	}
	/**
	 * ebcd byte[] 转asc byte[]
	 *
	 * @param ebcd
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] ebcd2Asc(byte[] ecbd){
		if(ecbd == null)
			return null;

		byte[] asces = new byte[ecbd.length];
		//StringBuilder theAsc = new StringBuilder();

		for(int i = 0;i < ecbd.length;i++)
		{
			asces[i] = (byte)CodeConvertor.EBCDIC_CODE[ecbd[i]&0xff];
		}
		return asces;
	}

	/**
	 * ebcd byte[] 转asc byte[]
	 * 
	 * @param ebcd
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] ebcdToAsc(char[] ebcd){
		if (ebcd == null)
			return null;
		
		byte[] asc = new byte[ebcd.length];
		
		for (int i = 0; i < ebcd.length; i++)
		{
			asc[i] = (byte) CodeConvertor.EBCDIC_CODE[ebcd[i]];
		}
		
		return asc;
	}


	/**
	 * ebcd String 转 asc String
	 * 
	 * @param ebcd
	 *            String
	 * @return String
	 */
	public static String ebcdToAsc(String ebcd) {
		if (ebcd == null)
			return null;

		char[] temp = ebcd.toCharArray();

		byte[] asc = new byte[temp.length];

		for (int i = 0; i < temp.length; i++) {
			asc[i] = (byte) CodeConvertor.EBCDIC_CODE[temp[i]];
		}

		return new String(asc);
	}


	/**
	 * Base64编码
	 * 
	 * @param binaryData
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] encodeBase64(byte[] binaryData){
		if(binaryData == null)
			return null;
		
		return Base64.encodeBase64(binaryData);
	}

	/**
	 * Base64编码
	 * 
	 * @param binaryData
	 *            String
	 * @return String
	 */
	public static String encodeBase64(String binaryData) {
		if (binaryData == null)
			return null;

		return new String(Base64.encodeBase64(binaryData.getBytes()));
	}
	
	/**
	 * Base64解码
	 * 
	 * @param base64Date
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] decodeBase64(byte[] base64Date){
		if(base64Date == null)
			return null;
		
		return Base64.decodeBase64(base64Date);
	}
	
	/**
	 * Base64解码
	 * 
	 * @param base64Date
	 *            String
	 * @return String
	 */
	public static String decodeBase64(String base64Date) {
		if (base64Date == null)
			return null;

		return new String(Base64.decodeBase64(base64Date.getBytes()));
	}

	/**
	 * gbk String 转utf8 String
	 * 
	 * @param gbkStr
	 *            String
	 * @return String
	 * @throws
	 *             e,不支持的转码类型
	 */
	public static String gbkToUtf8(String gbkStr) throws Exception{
		if(gbkStr == null)
			return null;
		byte[] utfData;

		utfData = gbkStr
				.getBytes("UTF-8");
		return new String(utfData, "UTF-8");

	}
	

	/**
	 * utf8 String转gbk String
	 * 
	 * @param utf8Str
	 *            String
	 * @return String
	 * @throws
	 *             e,不支持的转码类型
	 */
	public static String utf8ToGbk(String utf8Str) throws Exception {
		if(utf8Str == null)
			return null;
		byte[] gbkData;
		gbkData = utf8Str.getBytes("GBK");
		return new String(gbkData, "GBK");
	}
	
	/**
	 * gbk byte[] 转 utf8 byte[]
	 * 
	 * @param gbkData
	 *            byte[]
	 * @return byte[]
	 * @throws
	 *             e,不支持的转码类型
	 */
	public static byte[] gbkToUtf8(byte[] gbkData) throws Exception {
		if(gbkData == null)
			return null;
		return new String(gbkData, "GBK")
					.getBytes("UTF-8");

	}
	
	/**
	 * utf8 byte[]转gbk byte[]
	 * 
	 * @param utf8Data
	 *            byte[]
	 * @return byte[]
	 * @throws
	 *             e,不支持的转码类型
	 */
	public static byte[] utf8ToGbk(byte[] utf8Data) throws Exception {
		if(utf8Data == null)
			return null;
		return new String(utf8Data, "UTF-8")
					.getBytes("GBK");
	}


	public static int bytes2Int(byte[] bytes, boolean bBigEndian) {
		/*int result = 0;
		//将每个byte依次搬运到int相应的位置

		ByteBuffer byteBuff = ByteBuffer.allocate(4);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}
		byteBuff.put(bytes, 0, 4);
		byteBuff.flip();
		result = byteBuff.getInt();*/
		return bytes2Int(bytes, 0, bBigEndian);
	}

	public static int bytes2Int(byte[] bytes, int offset, boolean bBigEndian) {
		int result = 0;
		//将每个byte依次搬运到int相应的位置

		ByteBuffer byteBuff = ByteBuffer.allocate(4);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}

		if (offset >= bytes.length){
			return  result;
		}

		byteBuff.put(bytes, offset, Math.min(4, bytes.length-offset));
		byteBuff.flip();
		result = byteBuff.getInt();
		return result;
	}

	public static byte[] int2Bytes(int num, boolean bBigEndian) {
		ByteBuffer byteBuff = ByteBuffer.allocate(4);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}
		byteBuff.putInt(num);
		byteBuff.flip();

		byte[] result = new byte[4];
		byteBuff.get(result);
		return result;
	}

	public static short bytes2Short(byte[] bytes, boolean bBigEndian) {
		/*short result = 0;
		ByteBuffer byteBuff = ByteBuffer.allocate(2);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}
		byteBuff.put(bytes, 0, 2);
		byteBuff.flip();
		result = byteBuff.getShort();*/
		return bytes2Short(bytes, 0, bBigEndian);
	}

    public static short bytes2Short(byte[] bytes, int offset, boolean bBigEndian) {
        short result = 0;
        //将每个byte依次搬运到int相应的位置

        ByteBuffer byteBuff = ByteBuffer.allocate(2);
        if (!bBigEndian){
            byteBuff.order(ByteOrder.LITTLE_ENDIAN);
        }

        if (offset >= bytes.length){
            return  result;
        }

        byteBuff.put(bytes, offset, Math.min(2, bytes.length-offset));
        byteBuff.flip();
        result = byteBuff.getShort();
        return result;
    }

	public static byte[] short2Bytes(short num, boolean bBigEndian) {
		ByteBuffer byteBuff = ByteBuffer.allocate(2);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}
		byteBuff.putShort(num);
		byteBuff.flip();

		byte[] result = new byte[2];
		byteBuff.get(result);
		return result;
	}

	public static long bytes2Long(byte[] bytes, boolean bBigEndian) {
		long result = 0;
		ByteBuffer byteBuff = ByteBuffer.allocate(8);
		if (!bBigEndian){
			byteBuff.order(ByteOrder.LITTLE_ENDIAN);
		}
		byteBuff.put(bytes, 0, 8);
		byteBuff.flip();
		result = byteBuff.getLong();
		return result;
	}

	public static byte[] shortArray2ByteArray(short[] src, boolean bigEndian) {

		if(src==null){
			return null;
		}
		byte[] bytes = new byte[src.length * 2];

		if (bigEndian){
			ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(src);
		}else {
			ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(src);
		}
		return bytes;
	}

	public static short[] byteArray2ShortArray(byte[] src, boolean bigEndian) {

		if(src==null){
			return null;
		}

		short[] shorts = null;

		try{
			shorts = new short[src.length/2];

			if(!bigEndian){
				ByteBuffer.wrap(src).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);

				if (src.length%2 != 0){
					short[] shorts2 = new short[src.length/2+1];
					System.arraycopy(shorts, 0, shorts2, 0, shorts.length);
					shorts2[shorts2.length-1] = src[src.length-1];

					return shorts2;
				}
			}else{

				ByteBuffer.wrap(src).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(shorts);
				if (src.length%2 != 0){
					short[] shorts2 = new short[src.length/2+1];
					System.arraycopy(shorts, 0, shorts2, 0, shorts.length);
					shorts2[shorts2.length-1] = (short) (src[src.length-1] << 8);

					return shorts2;
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
	return shorts;

	}

    public static String bytes2String(byte[] bytes){
	    if (bytes == null){
	        return "";
        }
        String resString = (new String(bytes)).trim();

        int termPos = resString.indexOf(0);
        if (termPos >= 0){
            resString = resString.substring(0, termPos);
        }
        return resString;
    }
}
