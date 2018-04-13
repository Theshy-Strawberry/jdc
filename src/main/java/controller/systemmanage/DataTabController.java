package controller.systemmanage;

import business.DataTabBusiness;
import business.SysLogBusiness;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.*;
import model.ext.JsonTabCol;
import org.apache.commons.lang.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liu on 2016/6/24.
 */
@ControllerMapping(value = "/dataTab")
public class DataTabController extends BaseController {
    private static final Logger logger = Logger.getLogger(DataTab.class);
    /*注入business*/

    @Autowired
    DataTabBusiness business;
    @Autowired
    SysLogBusiness logBusiness;
    //      查询方法+分页
    public void findAll()  {
        Page<DataTab> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql =  searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //      查询方法+分页
    public void findSubAll()  {
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            //Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            //Integer page = this.getParaToInt("page");
            String whereSql = " del_flg = '1' " + searchSQL;
            String sql = "select @rownum:=@rownum+1 AS rownum,data_col.* from (SELECT @rownum:=0) r,data_col where "+whereSql;
            List<DataTab> list = DataTab.dao.find(sql);
            //pageList = business.findByPaginate2(pageSize, page, whereSql);
            this.setAttr("result", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    @Before(Tx.class)
    public void save() throws UnsupportedEncodingException {
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        DataTab dataTab = new DataTab();
        try {
            //保存dataTab名称和代码
            String tab_name = this.getPara("tab_name");
            Boolean name= business.checkNameSave(tab_name);
            if(name){
                dataTab.set("tab_name",tab_name);
                String sql = "select * from data_tab order by id desc";
                DataTab tab = DataTab.dao.findFirst(sql);
                if(tab != null){
                    Integer id = tab.get("id");
                    dataTab.set("tab_code", "tab_" + (id+1));
                }else{
                    dataTab.set("tab_code", "tab_1");
                }
                dataTab.set("del_flg", ConstantField.IS_SFSCH_YES);
                dataTab.set("create_date",new Date());
                dataTab.set("create_user", user.getStr("user_name"));
                if(dataTab.save()){
                    logBusiness.SaveLog("数据类型管理",ConstantField.LOG_ADD,tab_name,userName);
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
                //保存dataTab代码和dataCol中的代码
                String[] arr = this.getParaValues("arr");
                List<JsonTabCol> tabCols = JSON.parseArray(Arrays.toString(arr), JsonTabCol.class);
                if(tabCols != null){
                    for (int i = 0 ; i <tabCols.size() ; i++ ) {
                        TabCol tabCol = new TabCol();
                        tabCol.set("tab_code",dataTab.getStr("tab_code"));
                        tabCol.set("col_code",tabCols.get(i).getCol_code());
                        tabCol.set("del_flg", ConstantField.IS_SFSCH_YES);
                        if(tabCol.save()){
                            this.setAttr("result", "true");
                        }else{
                            this.setAttr("result", "false");
                        }
                    }
                }
            }else{
                this.setAttr("result", "checkNameError");
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
            //异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }
    }

    @Before(Tx.class)
    public void update() throws UnsupportedEncodingException {
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        DataTab dataTab = new DataTab();
        try {
            //保存dataTab名称和代码
            String id = this.getPara("id");
            String tab_name = this.getPara("tab_name");
            String tab_code = this.getPara("tab_code");
            Boolean name = business.checkNameUpdate(tab_name, id);
            if(name){
                dataTab.set("id", id);
                dataTab.set("tab_name", tab_name);
                dataTab.set("create_date",new Date());
                dataTab.set("create_user", user.getStr("user_name"));
                if(dataTab.update()){
                    logBusiness.SaveLog("数据类型管理",ConstantField.LOG_EDIT,tab_name,userName);
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
                //保存dataTab代码和dataCol中的代码
                String[] arr = this.getParaValues("arr");
                String[] delArr = this.getParaValues("delArr");
                List<JsonTabCol> tabCols = JSON.parseArray(Arrays.toString(arr), JsonTabCol.class);
                List<JsonTabCol> delTabCols = JSON.parseArray(Arrays.toString(delArr), JsonTabCol.class);
                if(tabCols != null){
                    for (int i = 0 ; i <tabCols.size() ; i++ ) {
                        TabCol tabCol = new TabCol();
                        tabCol.set("tab_code",tab_code);
                        tabCol.set("col_code",tabCols.get(i).getCol_code());
                        tabCol.set("del_flg", ConstantField.IS_SFSCH_YES);
                        if(tabCol.save()){
                            try{
                                //根据台帐字段查询tab_record_ids的record_id
                                String recordIdSql = "select record_id from tab_record_ids where tab_code='"+tab_code+"'";
                                List<TabRecordIds> list = TabRecordIds.dao.find(recordIdSql);
                                if(list.size()>0){
                                    //循环record_id
                                    for(int j=0;j<list.size();j++){
                                        //根据record_id和台帐code以及拆分后台头code 去查询tab_record数据是否存在 如果不存在则向tab_record表中插入一条数据
                                        String recordSql = "select * from tab_record where tab_code='"+tab_code+"' and record_id='"+list.get(j).get("record_id")+"' and  col_code='"+tabCols.get(i).getCol_code()+"'";
                                        List<TabRecord> tabRecordList = TabRecord.dao.find(recordSql);
                                        if(tabRecordList.size()<=0){
                                            TabRecord record = new TabRecord();
                                            record.set("tab_code",tab_code);
                                            record.set("col_code",tabCols.get(i).getCol_code());
                                            record.set("col_val", "");
                                            record.set("record_id",list.get(j).get("record_id"));
                                            record.set("is_updated",0);
                                            record.set("is_locked",0);
                                            record.set("del_flg",ConstantField.IS_SFSCH_YES);
                                            record.save();
                                        }
                                    }
                                }
                            }catch (Exception e) {
                                logger.info(e.getMessage());
                                break;
                            }
                            this.setAttr("result", "true");
                        }else{
                            this.setAttr("result", "false");
                        }
                    }
                    if(delTabCols != null){
                        for (int i = 0 ; i <delTabCols.size() ; i++ ) {
                            try{
                                //根据台帐字段查询tab_record_ids的record_id
                                String userTabColSql = "select * from user_tab_col where tab_code='"+tab_code+"' and col_code = '"+delTabCols.get(i).getCol_code()+"'";
                                List<UserTabCol> list = UserTabCol.dao.find(userTabColSql);
                                if(list.size()>0){
                                    //循环record_id
                                    for(int j=0;j<list.size();j++){
                                        UserTabCol userTabCol = new UserTabCol();
                                        userTabCol.set("id",list.get(j).getInt("id"));
                                        userTabCol.set("del_flg",ConstantField.IS_SFSCH_NO);
                                        userTabCol.update();
                                    }
                                }
                            }catch (Exception e) {
                                logger.info(e.getMessage());
                                break;
                            }
                        }
                    }
                } else {
                    this.setAttr("result", "checkNameError");
                }
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
            //异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }
    }


    //修改状态
    public void open(){
        try {
            //取值
            String id = this.getPara("id");
            Integer del_flg = this.getParaToInt("del_flg");
            DataTab o = new DataTab();
            if(del_flg == 2) {
                o.set("id",id);
                o.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(o.update()){
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }else if(del_flg == 1){
                o.set("id",id);
                o.set("del_flg", ConstantField.IS_SFSCH_NO);
                if(o.update()){
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //查询所有col_code信息
    public void getTabColByTabCode(){
        String code = this.getPara("code");
        List<TabCol> list=new ArrayList<TabCol>();
        try{
            list=business.getTabColByTabCode(code);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    //查询data_tab下的tab_code信息
    public void getTabCodeById(){
        String id = this.getPara("id");
        List<DataTab> list=new ArrayList<DataTab>();
        try{
            list=business.getTabCodeById(id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    //批量修改状态
    public void deleteByTabCode(){
        try {
            //取值
            String code = this.getPara("code");
            String sql = "select * from tab_col where tab_code='"+code+"'";
            List<TabCol> list = TabCol.dao.find(sql);
            for(int i=0;i<list.size();i++){
                TabCol o = new TabCol();
                o.set("id",list.get(i).get("id"));
                o.set("del_flg", ConstantField.IS_SFSCH_NO);
                o.update();
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
}
