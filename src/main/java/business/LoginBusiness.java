package business;

import com.senyoboss.ioc.Service;
import com.senyoboss.tool.ConstantField;
import model.SysUser;

import java.util.List;


/**
 * Created by Jr on 15/7/27.
 */
@Service
public class LoginBusiness {

    public SysUser getUserLogin(String userName, String userPassword) {
        String sql = "select * from sys_user where user_name='" + userName + "' and user_pwd='" + userPassword +"' and del_flg= "+ ConstantField.IS_DEL_FLG_ON ;
        List<SysUser> users = SysUser.dao.find(sql);
        if(0 != users.size()){
            return users.get(0);
        }
        return null;
    }
}
