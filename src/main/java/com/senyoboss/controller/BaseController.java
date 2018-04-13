package com.senyoboss.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import com.senyoboss.ext.kit.ReturnKit;
import com.senyoboss.model.BaseModel;

/**
 * 项目Controller的基类，待扩展
 *
 * @company Senyoboss
 * @author Jr.REX
 * @version 1.0
 */
public class BaseController extends Controller {

    protected final Logger logger = Logger.getLogger(getClass());

    protected static RenderFactory renderFactory = RenderFactory.me();

    public Render getRender() {
        if (ReturnKit.isJson(getRequest()) && !(ReturnKit.isJson(super.getRender()))) {
            return renderFactory.getJsonRender();
        }
        return super.getRender();
    }

    public <T> List<T> getModels(Class<T> modelClass) {
        return getModels(modelClass, StrKit.firstCharToLowerCase(modelClass.getSimpleName()));
    }

    /**
     * 跳转错误页
     */
    protected void renderError(String url, String msg, Integer... time) {
        this.setAttr("url", url);
        this.setAttr("error", msg);
        this.setAttr("the_time", time.length == 0 ? 6 : time[0].intValue()); //默认6秒
        render("/commons/error.html");
    }

    @Override
    public Date getParaToDate(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if(this.getPara(name) == null){
            return null;
        }
        try {
            date = sdf.parse(this.getPara(name));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取前端传来的数组对象并响应成Model列表
     */
    public <T> List<T> getModels(Class<T> modelClass, String modelName) {
        List<String> indexes = getIndexes(modelName);
        List<T> list = new ArrayList<T>();
        for (String index : indexes) {
            T m = getModel(modelClass, modelName + "[" + index + "]");
            if (m != null) {
                list.add(m);
            }
        }
        return list;
    }

    /**
     * 提取model对象数组的标号
     */
    private List<String> getIndexes(String modelName) {
        // 提取标号
        List<String> list = new ArrayList<String>();
        String modelNameAndLeft = modelName + "[";
        Map<String, String[]> parasMap = getRequest().getParameterMap();
        for (Map.Entry<String, String[]> e : parasMap.entrySet()) {
            String paraKey = e.getKey();
            if (paraKey.startsWith(modelNameAndLeft)) {
                String no = paraKey.substring(paraKey.indexOf("[") + 1,
                        paraKey.indexOf("]"));
                if (!list.contains(no)) {
                    list.add(no);
                }
            }
        }
        return list;
    }
    /**
     * fastjson转Map
     *
     * @param json
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> fastjson2Map(String json)
            throws UnsupportedEncodingException {
        json = URLDecoder.decode(json, "utf-8");
        return (Map<String, Object>) JSON.parse(json.substring(
                json.lastIndexOf("{"), json.indexOf("}") + 1));
    }

    /**
     * 根据JFinalModel的源码进行修改的setAttrs方法，
     * 根据fastjson2Map转换后的Map对象和Model进行比较并赋值，会根据数据库列名进行比较，
     * 当数据库对象包含此列时进行赋值操作，不存在时则跳过。
     * @param attrs fastjson2Map方法的返回值
     * @param obj 需要进行赋值的对象
     * @return JFinal Model对象
     */
    public static BaseModel setAttrs(Map<String,Object> attrs,BaseModel obj){
        Iterator i$ = attrs.entrySet().iterator();
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(obj.getTable().hasColumnLabel((String) e.getKey())) {
                obj.set((String) e.getKey(), e.getValue());
            }
        }
        return obj;
    }

    /**
     *
     * @param str 前台传入后台的参数对象
     * @param key 包含Array对象的Json key
     * @param model 需要进行赋值的对象
     * @return 根据前台对象赋值后的对象集合
     * @throws UnsupportedEncodingException
     */
    public static List jsonArr2List(String str,String key,BaseModel model) throws UnsupportedEncodingException {
        JSONObject o = (JSONObject)JSON.parse(str);
        JSONArray s= (JSONArray)o.get(key);
        List<BaseModel> list = new ArrayList<BaseModel>();
        for (int i = 0; i < s.size(); i++) {
            String objStr = s.get(i).toString();
            list.add((BaseModel)setAttrs(fastjson2Map(objStr),model));
        }
        return list;
    }

    public static String createSearchSQL(Map<String, String[]> map){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        String key = null;
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            key =(String) e.getKey();
            String splitStr=null;
            String tableColumn = null;
            int j=key.indexOf("$");//找分隔符的位置
            if(j>0){ //没有分隔符存在
                splitStr=key.substring(0,j);//找到分隔符，截取子字符串
                tableColumn=key.substring(j+1); //剩下需要处理的字符串
                if(splitStr != null || !splitStr.equals("")){
                    if(!"".equals(e.getValue())) {
                        String val;
                        String[] split = tableColumn.split("_");
                        if (split.length>0){
                            if("date".equals(split[split.length - 1])){
                                val = " STR_TO_DATE('"+e.getValue()+"','%Y-%m-%d') ";
                                tableColumn = "STR_TO_DATE(" +tableColumn +",'%Y-%m-%d')";
                            }else{
                                val = "'" + e.getValue() + "' ";
                            }
                        }else{
                            val = "'" + e.getValue() + "' ";
                        }

                        if ("EQ".equals(splitStr)) {
                            String str = e.getValue().toString();
                            if(isNum(str)){
                                sb.append("and (" + tableColumn + " = '" + e.getValue() +"') " );
                            }else{
                                sb.append("and "  + tableColumn + " = '" + e.getValue() +"' " );
                            }
                        } else if ("LIKE".equals(splitStr)) {
                            sb.append("and " + tableColumn + " like '%" + e.getValue() + "%' ");
                        } else if ("START".equals(splitStr)) {
                            sb.append("and " + tableColumn + " >= " + val);
                        } else if ("END".equals(splitStr)) {
                            sb.append("and " + tableColumn + " <= " + val);
                        } else if ("IN".equals(splitStr)) {
                            sb.append("and " + tableColumn + " in (" + e.getValue() + ") ");
                        }
//                        else if ("REG".equals(splitStr)) {
//                            sb.append("and regexp_like (" + tableColumn + ",'^"+e.getValue()+",|(,"+e.getValue()+",)+$|*(,"+e.getValue()+")|^"+e.getValue()+"')");
//                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String createExpSearchSQL(Map<String, String[]> map){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        String key = null;
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            key =(String) e.getKey();
            String splitStr=null;
            String tableColumn = null;
            int j=key.indexOf("$");//找分隔符的位置
            if(j>0){ //没有分隔符存在
                splitStr=key.substring(0,j);//找到分隔符，截取子字符串
                tableColumn=key.substring(j+1); //剩下需要处理的字符串
                if(splitStr != null || !splitStr.equals("")){
                    if(!"".equals(e.getValue())) {
                        String val;
                        String[] split = tableColumn.split("_");
                        if (split.length>0){
                            if("date".equals(split[split.length-1])){
                                val = " STR_TO_DATE('"+e.getValue()+"','%Y-%m-%d') ";
                                tableColumn = "STR_TO_DATE(ei." +tableColumn +",'%Y-%m-%d')";
                            }else{
                                val = "'" + e.getValue() + "' ";
                            }
                        }else{
                            val = "'" + e.getValue() + "' ";
                        }

                        if ("EQ".equals(splitStr)) {
                            String str = e.getValue().toString();
                            if(isNum(str)){
                                sb.append("and (ei." + tableColumn + " = " + e.getValue() +" or ei." + tableColumn + " = '" + e.getValue() +"') " );
                            }else{
                                sb.append("and ei."  + tableColumn + " = '" + e.getValue() +"' " );
                            }
                        } else if ("LIKE".equals(splitStr)) {
                            sb.append("and ei." + tableColumn + " like '%" + e.getValue() + "%' ");
                        } else if ("START".equals(splitStr)) {
                            sb.append("and ei." + tableColumn + " >= " + val);
                        } else if ("END".equals(splitStr)) {
                            sb.append("and ei." + tableColumn + " <= " + val);
                        } else if ("IN".equals(splitStr)) {
                            sb.append("and ei." + tableColumn + " in (" + e.getValue() + ") ");
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     *
     * @param map
     * @param model
     * @param username
     * @return
     */
    public static BaseModel parameter2Model(Map<String, String[]> map,BaseModel model,String username){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                model.set((String) e.getKey(), e.getValue());
            }
        }
        return model;
    }
    /**
     *
     * @param map
     * @param model
     * @param username
     * @return
     */
    public static BaseModel parameter2SaveModel(Map<String, String[]> map,BaseModel model,String username) throws ParseException {
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                if (model.getTable().getColumnType((String) e.getKey()).equals(new Timestamp(System.currentTimeMillis()).getClass())) {
                    Date date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = sdf.parse(e.getValue().toString());
                    } catch (ParseException e1) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            date = sdf.parse(e.getValue().toString());
                        } catch (ParseException es) {
                        }
                    }
                    model.set((String) e.getKey(), new Timestamp(date.getTime()));
                }else{
                    model.set((String) e.getKey(), e.getValue());
                }
            }
        }
        return model;
    }
    /**
     *
     * @param map
     * @param model
     * @param username
     * @return
     */
    public static BaseModel parameter2UpdateModel(Map<String, String[]> map,BaseModel model,String username){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                if (model.getTable().getColumnType((String) e.getKey()).equals(new Timestamp(System.currentTimeMillis()).getClass())) {
                    Date date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = sdf.parse(e.getValue().toString());
                    } catch (ParseException e1) {
                        try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf.parse(e.getValue().toString());
                        } catch (ParseException es) {
                        }
                    }
                    model.set((String) e.getKey(), new Timestamp(date.getTime()));
                }else{
                    model.set((String) e.getKey(), e.getValue());
                }
            }
        }
        return model;
    }

    /**
     *
     * @param map
     * @param model
     * @param username
     * @return
     */
    public static BaseModel parameter2SaveModelByOracle(Map<String, String[]> map,BaseModel model,String SeqName,String username) throws ParseException {
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();
        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                if (model.getTable().getColumnType((String) e.getKey()).equals(new Timestamp(System.currentTimeMillis()).getClass())) {
                    Date date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = sdf.parse(e.getValue().toString());
                    } catch (ParseException e1) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            date = sdf.parse(e.getValue().toString());
                        } catch (ParseException es) {
                        }
                    }
                    model.set((String) e.getKey(), new Timestamp(date.getTime()));
                }else{
                    model.set((String) e.getKey(), e.getValue());
                }
            }
        }
        //model.set("id", Db.queryBigDecimal("select "+SeqName+".nextval from dual").intValue());
        return model;
    }

    /**
     *
     * @param map
     * @param model
     * @param username
     * @return
     */
    public static BaseModel parameter2OracleModel(Map<String, String[]> map,BaseModel model,String username){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                if (model.getTable().getColumnType((String) e.getKey()).equals(new Timestamp(System.currentTimeMillis()).getClass())) {
                    Date date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = sdf.parse(e.getValue().toString());
                    } catch (ParseException e1) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            date = sdf.parse(e.getValue().toString());
                        } catch (ParseException es) {
                        }
                    }
                    model.set((String) e.getKey(), new Timestamp(date.getTime()));
                }else{
                    model.set((String) e.getKey(), e.getValue());
                }
            }
        }
        if(model.getTable().hasColumnLabel("create_user")) {
            model.set("create_user",username);
        }
        return model;
    }

    /**
     *
     * @param map
     * @param model
     * @return
     */
    public static BaseModel parameter2OracleModel(Map<String, String[]> map,BaseModel model){
        Map<String,Object> attrs = new HashMap<String,Object>();
        for (Map.Entry<String, String[]> e: map.entrySet()) {
            String[] values = e.getValue();
            if (values.length == 1)
                attrs.put(e.getKey(), values[0]);
            else
                attrs.put(e.getKey(), values);
        }
        Iterator i$ = attrs.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry e = (Map.Entry)i$.next();
            if(model.getTable().hasColumnLabel((String) e.getKey())) {
                if (model.getTable().getColumnType((String) e.getKey()).equals(new Timestamp(System.currentTimeMillis()).getClass())) {
                    Date date = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date = sdf.parse(e.getValue().toString());
                    } catch (ParseException e1) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            date = sdf.parse(e.getValue().toString());
                        } catch (ParseException es) {
                        }
                    }
                    model.set((String) e.getKey(), new Timestamp(date.getTime()));
                }else{
                    model.set((String) e.getKey(), e.getValue());
                }
            }
        }
        return model;
    }
}
