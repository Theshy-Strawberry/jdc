package controller.systemmanage;

import business.SysLogBusiness;
import business.SysUserBusiness;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import model.SysLog;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2015/10/9.
 */
@ControllerMapping(value = "/sysLog")
public class SysLogController extends BaseController{
    @Autowired
    SysLogBusiness business;
    @Autowired
    SysUserBusiness sysUserBusiness;
    /**
     * 查询全部数据
     */
    public void findAll() {
        Page<SysLog> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            Integer pageSize = this.getParaToInt("pageSize");
            Integer page = this.getParaToInt("page");
            String whereSql = searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUserNameList() {
        List<SysUser> list = new ArrayList<SysUser>();
        try {
//            list = sysUserBusiness.getUserNameList();
            list = SysUser.dao.find("SELECT * FROM sys_user WHERE del_flg=1");
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result", list);
    }

    public void getModuleNamesList() {
        List<SysLog> list = new ArrayList<SysLog>();
        try {
            list = business.getModuleNamesList();
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result", list);
    }

    /**
     * 查询用户信息列表
     */
    public void getUserInfo() {
        List<SysUser> list = new ArrayList<SysUser>();
        try {
            list = CacheKit.get("SystemCache", "sysUser");
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result", list);
    }
}
