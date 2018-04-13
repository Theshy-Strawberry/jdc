package controller.systemmanage;

import business.*;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import event.CacheEvent;
import model.SysAuth;
import model.SysUser;
import model.SysUserAuth;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jr.Rex on 2015/9/10.
 */
@ControllerMapping(value = "/sysauth")
public class SysAuthController extends BaseController {
    private static Logger logger = Logger.getLogger(SysAuthController.class);

    @Autowired
    SysAuthBusiness business;
    @Autowired
    SysUserBusiness userBusiness;
    @Autowired
    SysLogBusiness logBusiness;

    public void index() {
        System.out.println("===index");
    }

    public void getNodeAuth() {
        String menu_id = this.getPara("menu_id");
        List<SysAuth> list = null;
        try {
            list = business.getSysAuthList(menu_id);
            this.setAttr("result", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUserList(){
        try {
            List<SysUser> userList = userBusiness.getUserList();
            this.setAttr("result", userList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void saveUserAuth(){
        String user_name = this.getPara("user_name");
        String auth_id = this.getPara("auth_id");
        try {
            Boolean b = business.saveUserAuth(user_name, auth_id);
            this.setAttr("result",b);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void deleteUserAuth(){
        String user_name = this.getPara("user_name");
        String auth_id = this.getPara("auth_id");
        SysUserAuth sysUserAuth = business.findSysUserAuth(user_name, auth_id);
        if(null == sysUserAuth){
            this.setAttr("result",null);
        }else{
            try {
                Boolean b = business.deleteUserAuth(sysUserAuth.getInt("id"));
                this.setAttr("result",b);
            } catch (Exception e) {
                logger.info(e.getMessage());
                this.setAttr("result", "error");
            }
        }
    }

    public void getUserAuthList(){
        String user_name = this.getPara("user_name");
        String menu_id = this.getPara("menu_id");
        try {
            List<Record> userAuthList = business.getUserAuthList(user_name, menu_id);
            this.setAttr("result",userAuthList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUserAuths(){
        String user_name = this.getPara("user_name");
        String url = this.getPara("url");
        try {
            List<Record> userAuthList = business.getUserAuths(user_name, url);
            //List<Record> userAuthList = business.getUserAuths(user_name);
            this.setAttr("result",userAuthList);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }


    public void getRootMenu() {
        List<TreeModel> list = null;
        try {
            list = CacheKit.get("SystemCache", "sysTreeMenu");
            this.setAttr("result", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public static void getTreeMenu(){
        SysMenuBusiness business = new SysMenuBusiness();
        try {
            List<TreeModel> list = business.getSysRootList();
            if (list.size()>0){
                CacheKit.put("SystemCache","sysTreeMenu",list);
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        }
    }

}
