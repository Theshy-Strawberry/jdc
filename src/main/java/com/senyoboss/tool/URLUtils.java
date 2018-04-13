package com.senyoboss.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jfinal.kit.StrKit;

/**
 * URL相关工具类
 * @title URLUtils.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class URLUtils {

	private final static String CHARSET = "UTF-8";

	/**
	 * URL编码
	 * @param url
	 * @return
	 */
	public static String encoder(String url) {
		try {
			return URLEncoder.encode(url, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * URL解码
	 * @param url
	 * @return
	 */
	public static String decoder(String url) {
		try {
			return URLDecoder.decode(url, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 构建post paras
	 */
	public static String buildParas(Map<String, String> paras) {
		if (paras == null || paras.isEmpty())
			return null;

		StringBuilder sb = new StringBuilder("");
		boolean isFirst = true;

		for (Entry<String, String> entry : paras.entrySet()) {
			if (isFirst) isFirst = false;	
			else sb.append("&");

			String key = entry.getKey();
			String value = entry.getValue();
			if (StrKit.notBlank(value))
				try {value = URLEncoder.encode(value, CHARSET);} catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}

	/**
	 * 将queryString 转换成map格式，方便获取参数
	 * @param queryString
	 * @return Map
	 */
	public static Map<String, String> queryStringToMap(String queryString) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isBlank(queryString)) {
			return map;
		}
		// 兼容只有=和&、=的情况
		if (queryString.indexOf('&') != -1) {
			String[] params = queryString.split("&");
			for (int i = 0; i < params.length; i++) {
				String[] data = params[i].split("=");
				String value = data.length > 1 ? data[1] : "";
				map.put(data[0], value);
			}
		} else {
			String[] data = queryString.split("=");
			String value = data.length > 1 ? data[1] : "";
			map.put(data[0], value);
		}
		return map;
	}

}
