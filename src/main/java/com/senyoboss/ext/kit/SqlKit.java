package com.senyoboss.ext.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.beetl.core.BeetlKit;

import com.jfinal.log.Logger;

/**
 * 处理Sql Map
 * @author 董华健
 * 说明：加载sql map中的sql到map中，并提供动态长度sql处理
 */
public class SqlKit {

    protected static final Logger log = Logger.getLogger(SqlKit.class);

    /**
     * 过滤掉的sql关键字
     */
    private static final List<String> badKeyWordList = new ArrayList<String>();
    
    /**
     * 加载关键字到List
     */
    static {
    	String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";
    	badKeyWordList.addAll(Arrays.asList(badStr.split("\\|")));
    }
    
    /**
     * sql查询关键字过滤效验
     * @param queryStr
     * @return
     */
    public static boolean keywordVali(String queryStr) {
    	queryStr = queryStr.toLowerCase();//统一转为小写
        for (String badKeyWord : badKeyWordList) {
        	if (queryStr.indexOf(badKeyWord) >= 0) {
                return true;
            }
		}
        return false;
    }

    /**
     * 获取SQL，动态SQL
     * @param sql
     * @param param
     * @return
     */
    public static String getSql(String sql, Map<String, Object> param) {
    	if(null == sql || sql.isEmpty()){
			log.error("sql语句不存在");
    	}
    	String sqlStr = BeetlKit.render(sql, param);
		
		Set<String> keySet = param.keySet();
		for (String key : keySet) {
			if(param.get(key) == null){
				break;
			}
			String value = (String) param.get(key);
			value = value.replace("'", "").replace(";", "").replace("--", "");
			sqlStr = sqlStr.replace("#" + key + "#", value);
		}
		
        return sqlStr.replaceAll("[\\s]{2,}", " ");
    }

    /**
     * 获取SQL，动态SQL
     * @param sql
     * @param param 查询参数
     * @param list 用于接收预处理的值
     * @return
     */
    public static String getSql(String sql, Map<String, String> param, LinkedList<Object> list) {
    	if(null == sql || sql.isEmpty()){
			log.error("sql语句不存在");
    	}
    	
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	Set<String> paramKeySet = param.keySet();
    	for (String paramKey : paramKeySet) {
    		paramMap.put(paramKey, (Object)param.get(paramKey));
		}
    	String sqlStr = BeetlKit.render(sql, paramMap);
		
    	Pattern pattern = Pattern.compile("#[\\w\\d\\$\\'\\%\\_]+#");	//#[\\w\\d]+#    \\$
		Pattern pattern2 = Pattern.compile("\\$[\\w\\d\\_]+\\$");
		
		Matcher matcher = pattern.matcher(sqlStr);
		
		while (matcher.find()) {
			String clounm = matcher.group(0); // 得到的结果形式：#'%$names$%'#
			
			Matcher matcher2 = pattern2.matcher(clounm);
			matcher2.find();
			String clounm2 = matcher2.group(0); // 得到的结果形式：$names$
			
			String clounm3 = clounm2.replace("$", "");
			
			if(clounm.equals("#" + clounm2 + "#")){ // 数值型，可以对应处理int、long、bigdecimal、double等等
				String val = (String) param.get(clounm3);
				try {
					Integer.parseInt(val);
					sqlStr = sqlStr.replace(clounm, val);
				} catch (NumberFormatException e) {
					log.error("查询参数值错误，整型值传入了字符串，非法字符串是：" + val);
					return null;
				}
				
			}else{ // 字符串，主要是字符串模糊查询、日期比较的查询
				String val = (String) param.get(clounm3);
				
				String clounm4 = clounm.replace("#", "").replace("'", "").replace(clounm2, val);
				list.add(clounm4);
				
				sqlStr = sqlStr.replace(clounm, "?");
			}
		}
		
        return sqlStr.replaceAll("[\\s]{2,}", " ");
    }
}
