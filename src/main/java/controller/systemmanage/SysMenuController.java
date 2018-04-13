package controller.systemmanage;

import business.SysLogBusiness;
import business.SysMenuBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import event.CacheEvent;
import model.SysMenu;
import model.SysUser;
import model.ext.TreeModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jr.Rex on 2015/9/10.
 */
@ControllerMapping(value = "/sysmenu")
public class SysMenuController extends BaseController {
    private static Logger logger = Logger.getLogger(SysMenuController.class);

    @Autowired
    SysMenuBusiness business;
    @Autowired
    SysLogBusiness logBusiness;

    public void index() {
        System.out.println("===index");
    }

    public void getRoot() {
        List<TreeModel> list = null;
        try {
            list = CacheKit.get("SystemCache", "sysMenu");
            this.setAttr("result", list);
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

    public void getBaseMenu(){
        List<TreeModel> list = null;
        String user_name = this.getPara("user_name");

        try {
            list = business.getUserSysRootList(user_name);
            this.setAttr("result", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUserMenu(){
        String user_name = this.getPara("user_name");
        List<SysMenu> userMenuList = business.getUserMenuList(user_name);
        this.setAttr("result",userMenuList);
    }

}
