package com.shuabao.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class MD5Encode {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resSb.append(byteToHexString(b[i]));
		}
		return resSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String getMD5Str(String str) {
		String resStr = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			resStr = byteArrayToHexString(md.digest(str.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resStr;
	}

	public static byte getMD5FirstByte(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(str.getBytes());
			if (b != null && b.length > 0) {
				return b[0];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return (Byte) null;
	}

	public static String encodePWD(String salt, String pwd) {
		return getMD5Str(salt.toUpperCase() + ":" + pwd);
	}

	public static String getFileMD5(String fn) {
		return getFileMD5(new File(fn));
	}

	public static String getFileMD5(File file) {
		if(file==null) return null;
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			fis.close();
			return byteArrayToHexString(md.digest());
		} catch (Exception ex) {
			return null;
		}
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			MessageDigest md = MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		//System.out.println(MD5Encode.getMD5("9game_pc_lvdousyncEntity=syncField=syncType=1dateFrom=20000101000000dateTo=20140312161518pageSize=100pageNum=1d635fdd3e4f949b92fe9bf53b4a69008".getBytes()));
		//System.out.println(MD5Encode.getMD5Str("9game_pc_lvdousyncEntity=syncField=syncType=1dateFrom=20000101000000dateTo=20140312161518pageSize=100pageNum=1d635fdd3e4f949b92fe9bf53b4a69008"));
		String path="http://sdfwef/wef/wdc/sdf/sdf/7872378.jpg";
		System.out.println(path.substring(path.lastIndexOf("/"),path.length()));
		System.out.println(getMD5Str("100#6e3c6634abac6006787972a1155b4405#20995#1224608786#success#1408286381068181711#1#9671788af4cf296faef58f70d5887004#md5#1224608786#09e32aca8b59c918f3e153419f8bcb20"));
	}
}
