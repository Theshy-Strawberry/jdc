package controller;

import business.LoginBusiness;
import business.TabRecordBusiness;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.MD5;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@ControllerMapping(value = "/login")
public class LoginController extends BaseController {

	@Autowired
	LoginBusiness business;
	TabRecordBusiness tbBusiness = new TabRecordBusiness();

	/**
	 * 登陆
	 */
	public void login() {
		try {
			//MD5加密
			MD5 getMD5 = new MD5();
			String pwd = getMD5.GetMD5Code(this.getPara("user_pwd"));
			//String pwd = this.getPara("user_pwd");
			//DB用户名、密码判断
			SysUser user = business.getUserLogin(this.getPara("user_name"), pwd);
			//判断是否存在用户
			if(null != user){
				this.setSessionAttr("userSession",user);
				HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> tabPermission = tbBusiness.getTabPermission(this.getPara("user_name"));
				this.setSessionAttr("tabPermission", tabPermission);
				this.setAttr("result", "success");
			}else{
				this.setAttr("result", "false");
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
			this.setAttr("result", "error");
		}
	}

	/**
	 * 登出
	 */
	public void logout() {
		try {
			SysUser user = null;
			CacheKit.put("session", "user", user);
			this.setAttr("result", "success");
		} catch (Exception e) {
			logger.info(e.getMessage());
			this.setAttr("result", "error");
		}
	}

}
