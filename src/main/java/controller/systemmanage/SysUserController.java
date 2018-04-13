package controller.systemmanage;

import business.SysLogBusiness;
import business.SysUserBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import com.senyoboss.tool.MD5;
import event.CacheEvent;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liu on 2015/9/19.
 */
@ControllerMapping(value = "/sysUser", path = "/sysUser")
public class SysUserController extends BaseController {
    private static Logger logger = Logger.getLogger(SysUserController.class);
    @Autowired
    SysUserBusiness business;
    @Autowired
    SysLogBusiness logBusiness;

    public void index() {
        System.out.println("===index");
    }

    public void findAll()  throws UnsupportedEncodingException {
        Page<SysUser> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql = searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //重置密码
    public void resetPwdById() throws UnsupportedEncodingException {
        try {
            //取值
            String id = this.getPara("id");
            SysUser o = new SysUser();
            o.set("id",id);
            MD5 getMD5 = new MD5();
            o.set("user_pwd", getMD5.GetMD5Code("123456"));
            if(o.update()){
                setAttr("result", "true");
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void save() throws UnsupportedEncodingException {
        SysUser user = null;
        try {
            Map<String, String[]> map = this.getParaMap();
            user = (SysUser) parameter2Model(map, new SysUser(),null);
            Boolean name = business.checkUserName(user.getStr("user_name"));
            SysUser userSession= this.getSessionAttr("userSession");
            String userName=userSession.getStr("user_name");
            if(name){
                user.set("del_flg", ConstantField.IS_SFSCH_YES);
                user.set("create_date", new Date());
                user.set("create_user", userName);
                MD5 getMD5 = new MD5();
                user.set("user_pwd", getMD5.GetMD5Code("123456"));
                if(user.save()){
                    List<SysUser> cacheList = CacheKit.get("SystemCache", "sysUser");
                    cacheList.add(user);
                    CacheKit.put("SystemCache", "sysUser", cacheList);
                    String user_name = user.getStr("user_name");
                    logBusiness.SaveLog("用户管理", ConstantField.LOG_ADD, user_name, userName);
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }else{
                if(!name){
                    this.setAttr("result","checkNameError");
                }else{
                    this.setAttr("result","checkNoError");
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void update(){
        SysUser sysUser = null;
        try {
            Map<String, String[]> map = this.getParaMap();
            sysUser = (SysUser) parameter2Model(map, new SysUser(), null);
            int name = business.checkUserNameUnique(sysUser.getStr("user_name"), sysUser.getStr("id"));
            SysUser userSession= this.getSessionAttr("userSession");
            String userName=userSession.getStr("user_name");
            if(name != 1){
                sysUser.set("modify_date", new Date());
                sysUser.set("modify_user", userName);
                if(sysUser.update()){
                    CacheEvent.getSysUserList();
                    String user_name = sysUser.getStr("user_name");
                    logBusiness.SaveLog("用户管理", ConstantField.LOG_EDIT, user_name, userName);
                    this.setAttr("result","true");
                }else{
                    this.setAttr("result","false");
                }
            }else{
                if(name == 1){
                    this.setAttr("result","checkNameError");
                }else{
                    this.setAttr("result","checkNoError");
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void delete(){
        try {
            //取值
            String id = this.getPara("id");
            SysUser o = new SysUser();
            o.set("id",id);
            o.set("del_flg",ConstantField.IS_SFSCH_NO);
            if(o.update()){
                CacheEvent.getSysUserList();
            }
            setAttr("result", "true");
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUser() {
        SysUser user= this.getSessionAttr("userSession");
        SysUser userInfo = null;
        try {
            String sql = "select * from sys_user where id = '"+user.getInt("id") + "'";
            userInfo = SysUser.dao.findFirst(sql);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result", userInfo);
    }

    public void getUserList(){
        SysUser user = this.getSessionAttr("userSession");
        try {
            List<SysUser> list = business.getUserListByOffice(user);
            this.setAttr("result", list);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    //查询所有职能信息
    public void getJobTitleList(){
        List<SysUser>list=new ArrayList<SysUser>();
        try{
            list=business.getJobTitleList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    //修改状态
    public void open(){
        try {
            //取值
            String id = this.getPara("id");
            Integer del_flg = this.getParaToInt("del_flg");
            SysUser o = new SysUser();
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

}
