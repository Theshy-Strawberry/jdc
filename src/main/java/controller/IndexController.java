package controller;

import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import model.SysUser;

@ControllerMapping(value = "/index")
public class IndexController extends BaseController {

    /**
     * 从Session中获取用户信息
     * EX:
     *    {"result":{
     *              "is_online":null,"id":10,"user_pwd":"e10adc3949ba59abbe56e057f20f883e","user_name":"zhuren",
     *              "inner_no":null,"del_flg":"1","email":null,"job_title":"4","real_user_name":"孙主任",
     *              "telephone":null,"office_id":null
     *              }
     *    }
     */
    public void index(){
        this.setAttr("result", this.getSessionAttr("userSession"));

    }

}
