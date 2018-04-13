package com.senyoboss.tool;

import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.jfinal.kit.PathKit;

/**
 * 字符串工具类，继承lang3字符串工具类
 *
 * @version 1.0
 * @title StringUtil.java
 * @description
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 默认数位对象
     */
    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    public static Boolean isNullOrEmpty(String str) {

        Boolean r = false;
        if ("".equals(str) || str == null) {
            r = true;
        }
        return r;
    }

    public static String ChangeCharset(String str) throws UnsupportedEncodingException {
        String s = "";
        if (!isNullOrEmpty(str)) {
            s = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        }
        return s;
    }

    /**
     * 获取UUID，去掉`-`的
     *
     * @return uuid
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将字符串中特定模式的字符转换成map中对应的值
     * <p/>
     * use: format("my name is ${name}, and i like ${like}!", {"name":"L.cm", "like": "Java"})
     *
     * @param s   需要转换的字符串
     * @param map 转换所需的键值对集合
     * @return 转换后的字符串
     */
    public static String format(String s, Map<String, String> map) {
        StringBuilder sb = new StringBuilder((int) (s.length() * 1.5));
        int cursor = 0;
        for (int start, end; (start = s.indexOf("${", cursor)) != -1 && (end = s.indexOf('}', start)) != -1; ) {
            sb.append(s.substring(cursor, start));
            String key = s.substring(start + 2, end);
            sb.append(map.get(StringUtils.trim(key)));
            cursor = end + 1;
        }
        sb.append(s.substring(cursor, s.length()));
        return sb.toString();
    }

    /**
     * 实现简易的模板
     *
     * @param view
     * @param map
     * @return
     */
    public static String render(String view, Map<String, String> map) {
        String viewPath = PathKit.getWebRootPath() + view;
        try {
            String html = FileUtils.readFileToString(new File(viewPath), "UTF-8");
            return format(html, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串格式化
     * <p/>
     * use: format("my name is {0}, and i like {1}!", "L.cm", "java")
     * <p/>
     * int long use {0,number,#}
     *
     * @param s
     * @param args
     * @return 转换后的字符串
     */
    public static String format(String s, Object... args) {
        return MessageFormat.format(s, args);
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html
     * @return
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * 清理字符串，清理出某些不可见字符
     *
     * @param txt
     * @return {String}
     */
    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　	`·•�\\f\\t\\v]", "");
    }

    // 随机字符串
    private static final String _INT = "0123456789";
    private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String _ALL = _INT + _STR;

    private static final Random RANDOM = new Random();

    /**
     * 生成的随机数类型
     *
     * @author L.cm
     * @email: 596392912@qq.com
     * @site: http://www.dreamlu.net
     * @date 2015年4月20日下午9:15:23
     */
    public static enum RandomType {
        INT, STRING, ALL;
    }

    /**
     * 随机数生成
     *
     * @param count
     * @return
     */
    public static String random(int count, RandomType randomType) {
        if (count == 0) return "";
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (randomType.equals(RandomType.INT)) {
                buffer[i] = _INT.charAt(RANDOM.nextInt(_INT.length()));
            } else if (randomType.equals(RandomType.STRING)) {
                buffer[i] = _STR.charAt(RANDOM.nextInt(_STR.length()));
            } else {
                buffer[i] = _ALL.charAt(RANDOM.nextInt(_ALL.length()));
            }
        }
        return new String(buffer);
    }

    /**
     * 获取数字的位数
     * @param x 需要判断的数字
     * @return 数字的位数
     */
    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

}
