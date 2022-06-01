package com.grgbanking.core.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 报文长度转换处理
 * 
 * @author czbao
 * 
 */
public class MsgPackageUtil {

	/**
	 * 报文长度转换(byte[] -> Integer)
	 * 
	 * @param byte[]
	 *            lenByte 报文长度内容
	 * @param String
	 *            lenCodeType 报文长度编码方式("ASCII"、"BCD"、"BIN"、"HEX")
	 * @param boolean
	 *            boBigEndian 字节流是否大端编码(报文长度为2或4字节BIN时有效)
	 * @return Integer msgLen
	 */
	public static Integer unpackMsgLen(byte[] lenByte, String lenCodeType, boolean boBigEndian)
			throws Exception {
		Integer msgLenValue = 0;

		if (lenCodeType.equalsIgnoreCase("ASCII")) {
			// "1234" -> 1234
			msgLenValue = Integer.valueOf(new String(lenByte));
		} else if (lenCodeType.equalsIgnoreCase("BCD")) {
			// '0x12','0x34' -> 1234
			byte[] tempLength = CodeConvertor.bcdToAsc(lenByte);
			msgLenValue = Integer.parseInt(new String(tempLength));
		} else if (lenCodeType.equalsIgnoreCase("BIN")) {
			if ( (2 == lenByte.length) || (4 == lenByte.length) ) {
				ByteBuffer byteBuff = ByteBuffer.allocate(lenByte.length);
				if (!boBigEndian) {
					byteBuff.order(ByteOrder.LITTLE_ENDIAN);
				}
				byteBuff.put(lenByte);
				byteBuff.flip();
				if (2 == lenByte.length) {
					msgLenValue = Integer.valueOf(byteBuff.getShort());
				} else {
					msgLenValue = byteBuff.getInt();
				}
			} else {
				byte[] tempLength = CodeConvertor.bcdToAsc(lenByte);
				msgLenValue = Integer.parseInt(new String(tempLength), 16);
			}
		} else if (lenCodeType.equalsIgnoreCase("HEX")) {
			// "00FF" -> 255
			msgLenValue = Integer.parseInt(new String(lenByte), 16);
		} else {
			// 其它都默认成"ASCII"
			msgLenValue = Integer.valueOf(new String(lenByte));
		}

		return msgLenValue;
	}

	/**
	 * 报文长度转换(Integer -> byte[])
	 * 
	 * @param Integer
	 *            msgLenValue 报文长度值
	 * @param Integer
	 *            lenNumber 报文长度字段的长度(多少个字节)
	 * @param String
	 *            lenCodeType 报文长度编码方式("ASCII"、"BCD"、"BIN"、"HEX")
	 * @param boolean
	 *            boBigEndian 字节流是否大端编码(报文长度为2或4字节BIN时有效)
	 * @return Integer msgLen
	 */
	public static byte[] packMsgLen(Integer msgLenValue, Integer lenNumber, String lenCodeType, boolean boBigEndian)
			throws Exception {
		byte[] lenByte = null;

		if (lenCodeType.equalsIgnoreCase("ASCII")) {
			// 1234 -> "1234"
			lenByte = String.format("%0" + lenNumber + "d", msgLenValue).getBytes();
		} else if (lenCodeType.equalsIgnoreCase("BCD")) {
			// 1234 -> '0x12','0x34'
			lenByte = CodeConvertor.ascToBcd(String.format("%0" + lenNumber * 2 + "d", msgLenValue).getBytes());
		} else if (lenCodeType.equalsIgnoreCase("BIN")) {
			if ((2 == lenNumber) || (4 == lenNumber)) {
				ByteBuffer byteBuff = ByteBuffer.allocate(lenNumber);
				if (!boBigEndian) {
					byteBuff.order(ByteOrder.LITTLE_ENDIAN);
				}
				if (2 == lenNumber) {
					byteBuff.putShort(msgLenValue.shortValue());
				} else {
					byteBuff.putInt(msgLenValue);
				}
				lenByte = byteBuff.array();
			} else {
				byte[] tmpByte = CodeConvertor.ascToBcd(Integer.toHexString(msgLenValue).getBytes());
				lenByte = new byte[lenNumber];
				if (tmpByte.length <= lenNumber) {
					int destPos = lenNumber - tmpByte.length;
					System.arraycopy(tmpByte, 0, lenByte, destPos, tmpByte.length);
				}
			}
		} else if (lenCodeType.equalsIgnoreCase("HEX")) {
			// 255 -> "00FF"
			lenByte = String.format("%0" + lenNumber + "x", msgLenValue).getBytes();
		} else {
			// 其它都默认成"ASCII"
			lenByte = String.format("%0" + lenNumber + "d", msgLenValue).getBytes();
		}

		return lenByte;
	}

	public static byte[] packMsgLen(Integer msgLenValue, Integer lenNumber, String lenCodeType, boolean boBigEndian,
									boolean lengthIsFullLength)
			throws Exception {
		int lenValue = msgLenValue;
		// 如果长度是整个报文的长度，需要加上长度字段本身的长度
		if (lengthIsFullLength)
			lenValue += lenNumber;
		return packMsgLen(lenValue, lenNumber, lenCodeType, boBigEndian);
	}

	public static Integer unpackMsgLen(byte[] lenByte, String lenCodeType, boolean boBigEndian,
									   boolean lengthIsFullLength)
			throws Exception {
		int msgLenValue = unpackMsgLen(lenByte, lenCodeType, boBigEndian);
		// 如果长度是整个报文的长度，需要减去长度字段本身的长度
		if (lengthIsFullLength)
			msgLenValue -= lenByte.length;
		return msgLenValue;
	}

}

