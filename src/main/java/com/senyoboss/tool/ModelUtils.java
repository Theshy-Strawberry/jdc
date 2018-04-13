package com.senyoboss.tool;

import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Model工具类
 * @title ModelUtils.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class ModelUtils {

	/**
	 * copy 老model的属性到新model
	 * @param src 源model
	 * @param dist 新model
	 */
	public static void copy(Model<?> src, Model<?> dist) {
		dist.setAttrs(CPI.getAttrs(src));
	}

	/**
	 * copy 老model的属性到新Record
	 * @param src 源model
	 * @param dist 新Record
	 */
	public static void copy(Model<?> src, Record dist) {
		dist.setColumns(src);
	}

	/**
	 * copy 老Record的属性到新model
	 * @param src 源Record
	 * @param dist 新model
	 */
	public static void copy(Record src, Model<?> dist) {
		dist.setAttrs(src.getColumns());
	}

}
