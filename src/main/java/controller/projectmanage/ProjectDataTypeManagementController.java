package controller.projectmanage;

import business.ProjectDateTypeManagementBusiness;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by 冕寒 on 2016/6/23.
 */
@ControllerMapping(value = "/projectDataTypeManagement")
public class ProjectDataTypeManagementController extends BaseController  {
    private static final Logger logger = Logger.getLogger(ProjectBaseManagement.class);
    @Autowired
//      调用Business （ssh-dao）
            ProjectDateTypeManagementBusiness business;
    //      查询方法+分页
    public void findAll()  {
        Page<ProjectBaseManagement> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql = " and del_flg = '1' " + searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void findAll2()  {
        List<UserTab> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
//            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
//            Integer page = this.getParaToInt("page");
            Integer project_id = this.getParaToInt("id");
            String whereSql = " and del_flg = '1' and project_id = "+ project_id +" "+ searchSQL;
            String sql = "select @rownum:=@rownum+1 AS rownum,ut.* from (SELECT @rownum:=0) r,(select distinct(tab_code),project_id from user_tab where 0=0 " + whereSql+" ) ut";
//            pageList = business.findByPaginate3(pageSize, page, whereSql);
            pageList = UserTab.dao.find(sql);
            this.setAttr("result", pageList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void findSubAll()  {
        List<TabCol> pageList = null;
        try {
            String tab_code = this.getPara("tab_code");
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
//            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
//            Integer page = this.getParaToInt("page");
            String whereSql = " and del_flg = '1' and tab_code = '"+tab_code+"'";
            String sql = "select @rownum:=@rownum+1 AS rownum,tab_col.* from (SELECT @rownum:=0) r,tab_col where 0=0 " + whereSql;
            pageList = TabCol.dao.find(sql);
//            pageList = business.findByPaginate2(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void findSubAll2()  {
        List<UserTabCol> pageList = null;
        try {
            String tab_code = this.getPara("tab_code");
            Integer user_id = this.getParaToInt("user_id");
//            String user_name = this.getPara("user_name");
            Integer project_id = this.getParaToInt("project_id");
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql = " and del_flg = '1' and tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id;
            String sql = "select @rownum:=@rownum+1 AS rownum,user_tab_col.* from (SELECT @rownum:=0) r,user_tab_col where 0=0 " + whereSql;
            pageList = UserTabCol.dao.find(sql);
//            pageList = business.findByPaginate4(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void findUserPermission() {
        List<TabPermission> pageList = null;
        try {
            String tab_code = this.getPara("tab_code");
            Integer user_id = this.getParaToInt("user_id");
//            String user_name = this.getPara("user_name");
            Integer project_id = this.getParaToInt("project_id");
            String sql = "select * from tab_permission where tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id;
            pageList = TabPermission.dao.find(sql);
            this.setAttr("result", pageList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void findUserTabCol() {
        List<UserTabCol> pageList = null;
        try {
            String tab_code = this.getPara("tab_code");
            Integer user_id = this.getParaToInt("user_id");
//            String user_name = this.getPara("user_name");
            Integer project_id = this.getParaToInt("project_id");
            String sql = "select * from user_tab_col where tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id+" and del_flg = 1";
            pageList = UserTabCol.dao.find(sql);
            this.setAttr("result", pageList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    @Before(Tx.class)
    public void save() throws UnsupportedEncodingException {
        SysUser user= this.getSessionAttr("userSession");
        UserTabCol userTabCol = null;
        UserTab userTab = null;
        TabPermission tabPermission = null;
        try {
            //保存dataTab名称和代码
            Integer user_id = this.getParaToInt("user_id");
            String user_name = SysUser.dao.findById(user_id).getStr("user_name");
            Integer project_id = this.getParaToInt("project_id");
            String tab_code = this.getPara("tab_code");

//            Integer user_sort_code = this.getParaToInt("user_sort_code");
            Integer user_sort_code = 0;
//            Boolean nameCol= business.checkUserTabColSave(user_name, project_id);
//            if(!nameCol){
//                this.setAttr("result", "checkNameColError");
//                return;
//            }else{
//                Boolean nameTab= business.checkUserTabSave(user_name, project_id);
//                if(!nameTab){
//                    this.setAttr("result", "checkNameTabError");
//                    return;
//                }else{
//                    Boolean namePermission= business.checkTabPermissionSave(user_name, project_id);
//                    if(!namePermission){
//                        this.setAttr("result", "checkNamePermissionError");
//                        return;
//                    }
//                }
//                Boolean namePermission= business.checkTabPermissionSave(user_name, project_id);
//                if(!namePermission){
//                    this.setAttr("result", "checkNamePermissionError");
//                    return;
//                }
//            }
            Boolean nameTab= business.checkUserTabSave(user_id, project_id, tab_code);
            if(!nameTab){
                this.setAttr("result", "checkNameTabError");
                return;
            }




            userTab = new UserTab();
            userTab.set("tab_code",tab_code);
            userTab.set("project_id",project_id);
            userTab.set("user_name",user_name);
            userTab.set("user_id",user_id);
            userTab.set("del_flg", ConstantField.IS_SFSCH_YES);
            if(userTab.save()){
                this.setAttr("result", "true");
            }else{
                this.setAttr("result", "false");
            }
            //保存dataTab代码和dataCol中的代码
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String[] arr = map.get("arr");
            if(!"".equals(arr) && arr != null && !"null".equals(arr)){
                for (int i = 0 ; i <arr.length ; i++ ) {
                    JSONObject j = JSONObject.parseObject(arr[i]);
                    userTabCol = new UserTabCol();
                    userTabCol.set("tab_code",tab_code);
                    userTabCol.set("col_code", j.getString("col_code"));
                    userTabCol.set("project_id",project_id);
                    userTabCol.set("user_name",user_name);
                    userTabCol.set("user_id",user_id);
                    userTabCol.set("user_sort_code",j.getString("user_sort_code"));
                    userTabCol.set("del_flg", ConstantField.IS_SFSCH_YES);
                    if(userTabCol.save()){
                        this.setAttr("result", "true");
                    }else{
                        this.setAttr("result", "false");
                    }
                }
            }
            String[] auth = this.getParaValues("auths");
            String strAuth = Arrays.toString(auth).replaceAll("\"", "");
            String strAuth1 = strAuth.replaceAll("\\[", "");
            String strAuth2 = strAuth1.replaceAll("\\]", "");
            String[] strAuth3 = strAuth2.split(",");
            if(!"".equals(strAuth3) && strAuth3 != null && !"null".equals(strAuth3)){
                for (int i = 0 ; i <strAuth3.length ; i++ ) {
                    tabPermission = new TabPermission();
                    tabPermission.set("tab_code",tab_code);
                    tabPermission.set("permission_code",Integer.valueOf(strAuth3[i].trim()));
                    tabPermission.set("project_id",project_id);
                    tabPermission.set("user_name",user_name);
                    tabPermission.set("user_id",user_id);
                    if(tabPermission.save()){
                        this.setAttr("result", "true");
                    }else{
                        this.setAttr("result", "false");
                    }
                }
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
        UserTabCol userTabCol = null;
        UserTab userTab = null;
        TabPermission tabPermission = null;
        try {
            //保存dataTab名称和代码
            Integer user_id = this.getParaToInt("user_id");
            String user_name = SysUser.dao.findById(user_id).getStr("user_name");
            Integer project_id = this.getParaToInt("project_id");
            String tab_code = this.getPara("tab_code");
            String userTabColSql = "select * from user_tab_col where tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id;
            List<UserTabCol> userTabColList = UserTabCol.dao.find(userTabColSql);
            if(userTabColList.size()>0){
                for(int a = 0;a<userTabColList.size();a++){
                    userTabColList.get(a).set("del_flg",ConstantField.IS_SFSCH_NO);
                    userTabColList.get(a).update();
                }
            }
            String tabPermissionSql = "select * from tab_permission where tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id;
            List<TabPermission> tabPermissionList = TabPermission.dao.find(tabPermissionSql);
            if(tabPermissionList.size()>0){
                for(int b = 0;b<tabPermissionList.size();b++){
                    TabPermission.dao.deleteById(tabPermissionList.get(b).getInt("id"));
                }
            }
            String userTabSql = "select * from user_tab where del_flg = 1 and tab_code = '"+tab_code+"' and user_id = '"+user_id+"' and project_id = "+project_id;
            List<UserTab> userTabList = UserTab.dao.find(userTabSql);
            if(userTabList.size() == 0){
                userTab = new UserTab();
                userTab.set("tab_code",tab_code);
                userTab.set("project_id",project_id);
                userTab.set("user_name", user_name);
                userTab.set("user_id", user_id);
                userTab.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(userTab.save()){
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }
            //保存dataTab代码和dataCol中的代码
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String[] arr = map.get("arr");
            if(!"".equals(arr) && arr != null && !"null".equals(arr)){
                for (int i = 0 ; i <arr.length ; i++ ) {
                    JSONObject j = JSONObject.parseObject(arr[i]);
                    userTabCol = new UserTabCol();
                    userTabCol.set("tab_code",tab_code);
                    userTabCol.set("col_code", j.getString("col_code"));
                    userTabCol.set("project_id",project_id);
                    userTabCol.set("user_name",user_name);
                    userTabCol.set("user_id", user_id);
                    userTabCol.set("user_sort_code",j.getString("user_sort_code"));
                    userTabCol.set("del_flg", ConstantField.IS_SFSCH_YES);
                    if(userTabCol.save()){
                        this.setAttr("result", "true");
                    }else{
                        this.setAttr("result", "false");
                    }
                }
            }
            String[] auth = this.getParaValues("auths");
            String strAuth = Arrays.toString(auth).replaceAll("\"", "");
            String strAuth1 = strAuth.replaceAll("\\[", "");
            String strAuth2 = strAuth1.replaceAll("\\]", "");
            String[] strAuth3 = strAuth2.split(",");
            if(!"".equals(strAuth3) && strAuth3 != null && !"null".equals(strAuth3)){
                for (int i = 0 ; i <strAuth3.length ; i++ ) {
                    tabPermission = new TabPermission();
                    tabPermission.set("tab_code",tab_code);
                    tabPermission.set("permission_code",Integer.valueOf(strAuth3[i].trim()));
                    tabPermission.set("project_id",project_id);
                    tabPermission.set("user_name",user_name);
                    tabPermission.set("user_id", user_id);
                    if(tabPermission.save()){
                        this.setAttr("result", "true");
                    }else{
                        this.setAttr("result", "false");
                    }
                }
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
            //异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }
    }

    public void getUserAll(){
        List<ProjectBaseManagement> list=new ArrayList<ProjectBaseManagement>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    public void getDataType(){
        List<DataTab> list=new ArrayList<DataTab>();
        try{
            list=business.getDataType();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    public void getDataTypeName(){
        List<DataCol> list=new ArrayList<DataCol>();
        try{
            list=business.getDataTypeName();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    public void getProjectName(){
        ProjectBaseManagement list=new ProjectBaseManagement();
        try{
            Integer project_id = this.getParaToInt("project_id");
            list = ProjectBaseManagement.dao.findById(project_id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    public void getProjectUser(){
        List<ProjectPersonnel> list=new ArrayList<ProjectPersonnel>();
        try{
            Integer project_id = this.getParaToInt("project_id");
            list=business.getProjectUser(project_id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    //查询所有公司信息【乙方主体】
    public void getBusinessMainPartList(){
        List<SysUser>list=new ArrayList<SysUser>();
        try{
            list=business.getBusinessMainPartList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
}
