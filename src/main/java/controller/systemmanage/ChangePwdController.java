package controller.systemmanage;

import business.LoginBusiness;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.MD5;
import model.SysUser;

/**
 * Created by Tinkpad on 2016/6/21.
 */
@ControllerMapping(value = "/changePwd")
public class ChangePwdController extends BaseController {

    @Autowired
    LoginBusiness business;

    public void save() {

        String originalPwd = this.getPara("user_pwd");//原始密码
        String newPwd = this.getPara("new_user_pwd");//新密码
        String confirmPwd = this.getPara("confirm_user_pwd");//确认密码
        //获取session中的用户名
        SysUser sysUser= this.getSessionAttr("userSession");
        String user_name = sysUser.getStr("user_name");
        //加密原始密码
        MD5 getMD5 = new MD5();
        String pwd = getMD5.GetMD5Code(originalPwd);
        //DB用户名、密码判断
        SysUser user = business.getUserLogin(user_name, pwd);

        if(user != null){
            //原密码匹配成功 进行密码修改
            if(newPwd.equals(confirmPwd)){
                String p = getMD5.GetMD5Code(newPwd);
                user.set("user_pwd",p);
                user.update();
                this.setAttr("result", "true");
            }else{
                this.setAttr("result", "false");
            }
        }else{
            this.setAttr("result", "pwdFalse");
        }
    }
}
