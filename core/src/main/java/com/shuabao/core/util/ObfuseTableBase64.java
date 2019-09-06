package com.shuabao.core.util;



/**
 * 自定义编码表的Base64,该编码表已被打乱。查看编码表请调用printData64(data64)方法。 此版本编码表：<br/>
 * 0->7[a 0 A n N b 9 B ]<br/>
 * 8->15[o O c _ C p P d ]<br/>
 * 16->23[- D q Q e 1 E r ]<br/>
 * 24->31[R f 8 F s S g 6 ]<br/>
 * 32->39[G t T h 3 H u U ]<br/>
 * 40->47[i 4 I v V j 2 J ]<br/>
 * 48->55[w W k 7 K x X 5 ]<br/>
 * 56->63[L y Y m # M z Z ]<br/>
 * 
 * @author Sodino Email:sodinoopen@hotmail.com <br/>
 *         Time:2012年4月28日11时45分58秒
 */
public class ObfuseTableBase64 {
	private static final int MAX_LENGTH = 5;
	private static final int MAX_CODE_TIME = 3;

	private static final byte placeHolder = 'l';
	private static final byte[] encodingTable;
	private static final byte[] decodingTable;
	static {
		byte[][] data2Arr = new byte[5][13];
		byte[] tmp0 = { '0', '9', '_', '-', '1', '8', '6', '3', '4', '2', '7', '5', '#' };
		byte[] tmp1 = new byte[tmp0.length];
		for (int i = 0; i < tmp1.length; i++) {
			tmp1[i] = (byte) (i + 0x41);
		}
		byte[] tmp2 = new byte[tmp1.length];
		for (int i = 0; i < tmp2.length; i++) {
			tmp2[i] = (byte) (tmp1[tmp1.length - 1] + i + 1);
		}
		byte[] tmp3 = new byte[tmp2.length];
		for (int i = 0; i < tmp3.length; i++) {
			tmp3[i] = (byte) (i + 0x61);
		}
		byte[] tmp4 = new byte[tmp3.length];
		for (int i = 0; i < tmp3.length; i++) {
			tmp4[i] = (byte) (tmp3[tmp3.length - 1] + i + 1);
		}
		data2Arr[0] = tmp3;// 3
		data2Arr[1] = tmp0;// 0
		data2Arr[2] = tmp1;// 1
		data2Arr[3] = tmp4;// 4
		data2Arr[4] = tmp2;// 2
		tmp4 = tmp3 = tmp2 = tmp1 = tmp0 = null;
		// ///////////////////////////////////////////////////////////////////
		// for (int i = 0; i < 5; i++) {
		// System.out.print(i + \\"[\");
		// for (int j = 0; j < 13; j++) {
		// System.out.print((char) data2Arr[i][j] + " ");
		// }
		// System.out.println("]");
		// }
		// ///////////////////////////////////////////////////////////////////
		byte[] data65 = new byte[data2Arr[0].length * data2Arr.length];
		int quotient = 0;
		int remainder = 0;
		int idx = 0;
		for (int i = 0; i < data65.length; i++) {
			quotient = i / 5;
			remainder = i % 5;
			data65[i] = data2Arr[remainder][quotient];
			if (data65[i] == placeHolder) {
				idx = i;
			}
		}
		byte[] data64 = new byte[data65.length - 1];
		System.arraycopy(data65, 0, data64, 0, idx);
		System.arraycopy(data65, idx + 1, data64, idx, 64 - idx);
		// printData64(data64);
		encodingTable = data64;
		decodingTable = new byte[(data65.length - 1) << 1];
		data65 = null;
		for (int i = 0; i < encodingTable.length; i++) {
			decodingTable[encodingTable[i]] = (byte) i;
		}
	}


	public static String encode(String data){
		return new String(ObfuseTableBase64.encode(data.getBytes()));
	}

	public static byte[] encode(byte[] data) {
		byte[] bytes;
		int modulus = data.length % 3;
		if (modulus == 0) {
			bytes = new byte[(4 * data.length) / 3];
		} else {
			bytes = new byte[4 * ((data.length / 3) + 1)];
		}
		int dataLength = (data.length - modulus);
		int a1;
		int a2;
		int a3;
		for (int i = 0, j = 0; i < dataLength; i += 3, j += 4) {
			a1 = data[i] & 0xff;
			a2 = data[i + 1] & 0xff;
			a3 = data[i + 2] & 0xff;
			bytes[j] = encodingTable[(a1 >>> 2) & 0x3f];
			bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];
			bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];
			bytes[j + 3] = encodingTable[a3 & 0x3f];
		}
		int b1;
		int b2;
		int b3;
		int d1;
		int d2;
		switch (modulus) {
		case 0: /* nothing left to do */
			break;
		case 1:
			d1 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = (d1 << 4) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = placeHolder;
			bytes[bytes.length - 1] = placeHolder;
			break;
		case 2:
			d1 = data[data.length - 2] & 0xff;
			d2 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
			b3 = (d2 << 2) & 0x3f;
			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = encodingTable[b3];
			bytes[bytes.length - 1] = (byte) placeHolder;
			break;
		}
		return bytes;
	}
	
	public static String doDecode(String data) {
		return new String(decode(data.getBytes()));
	}

	public static byte[] decode(byte[] data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Bytes(data);
		if (data[data.length - 2] == placeHolder) {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
		} else if (data[data.length - 1] == placeHolder) {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {
			b1 = decodingTable[data[i]];
			b2 = decodingTable[data[i + 1]];
			b3 = decodingTable[data[i + 2]];
			b4 = decodingTable[data[i + 3]];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data[data.length - 2] == placeHolder) {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data[data.length - 1] == placeHolder) {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			b3 = decodingTable[data[data.length - 2]];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data[data.length - 4]];
			b2 = decodingTable[data[data.length - 3]];
			b3 = decodingTable[data[data.length - 2]];
			b4 = decodingTable[data[data.length - 1]];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	public static byte[] decode(String data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Chars(data);
		if (data.charAt(data.length() - 2) == placeHolder) {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 1];
		} else if (data.charAt(data.length() - 1) == placeHolder) {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length() / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length() - 4); i += 4, j += 3) {
			b1 = decodingTable[data.charAt(i)];
			b2 = decodingTable[data.charAt(i + 1)];
			b3 = decodingTable[data.charAt(i + 2)];
			b4 = decodingTable[data.charAt(i + 3)];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data.charAt(data.length() - 2) == placeHolder) {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data.charAt(data.length() - 1) == placeHolder) {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			b3 = decodingTable[data.charAt(data.length() - 2)];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data.charAt(data.length() - 4)];
			b2 = decodingTable[data.charAt(data.length() - 3)];
			b3 = decodingTable[data.charAt(data.length() - 2)];
			b4 = decodingTable[data.charAt(data.length() - 1)];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	private static byte[] discardNonBase64Bytes(byte[] data) {
		byte[] temp = new byte[data.length];
		int bytesCopied = 0;
		for (int i = 0; i < data.length; i++) {
			if (isValidBase64Byte(data[i])) {
				temp[bytesCopied++] = data[i];
			}
		}
		byte[] newData = new byte[bytesCopied];
		System.arraycopy(temp, 0, newData, 0, bytesCopied);
		return newData;
	}

	private static String discardNonBase64Chars(String data) {
		StringBuffer sb = new StringBuffer();
		int length = data.length();
		for (int i = 0; i < length; i++) {
			if (isValidBase64Byte((byte) (data.charAt(i)))) {
				sb.append(data.charAt(i));
			}
		}
		return sb.toString();
	}

	private static boolean isValidBase64Byte(byte b) {
		if (b == placeHolder) {
			return true;
		} else if ((b < 0) || (b >= 128)) {
			return false;
		} else if (decodingTable[b] == -1) {
			return false;
		}
		return true;
	}

	public static String doDecode(byte[] data, int decodeTime) {
		if (decodeTime > MAX_LENGTH) {
			decodeTime = MAX_CODE_TIME;
		}
		byte[] decode = data;
		for (int i = 0; i < decodeTime; i++) {
			decode = ObfuseTableBase64.decode(decode);
		}
		String name = new String(decode);
		return name;
	}
}