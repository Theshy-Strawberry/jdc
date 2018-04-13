package com.senyoboss.tool;

import java.util.Arrays;

/**
 * Sql相关工具类
 * @title SqlUtils.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class SqlUtils {

	/**
	 * 生成sql占位符 ?,?,?
	 * @param size
	 * @return
	 */
	public static String sqlHolder(int size) {
		String[] paras = new String[size];
		Arrays.fill(paras, "?");
		return StringUtils.join(paras, ',');
	}

}
