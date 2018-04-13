package business;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.ConstantField;
import model.SysAuth;
import model.SysUserAuth;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/14.
 */
@Service
public class SysAuthBusiness {

    public List<SysAuth> getSysAuthList(String menuId)  throws UnsupportedEncodingException {
        String sql = "select *,auth_id as authId from sys_auth where menu_id = " + menuId;
        List<SysAuth> list = SysAuth.dao.find(sql);
        return list;
    }

    public List<Record> getUserAuthList(String userName,String menuId) throws Exception{
        String sql = "select sa.id as id,sua.auth_id as authId,sua.user_name as user_name,sa.auth_name as auth_name from SYS_USER_AUTH sua,SYS_AUTH sa,SYS_MENU sm where sua.auth_id = sa.auth_id and sa.menu_id = sm.menu_id and sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sa.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sua.user_name = '"+userName+"' and sa.menu_id = " + menuId;
        List<Record> list = Db.query(sql);
        return list;
    }

    public List<Record> getUserAuths(String user_name,String url) throws Exception{
        String sql = "select * from SYS_USER_AUTH sua,SYS_AUTH sa,SYS_MENU sm where sua.auth_id = sa.auth_id and sa.menu_id = sm.menu_id and sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sa.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sua.user_name = '"+user_name+"' and sm.menu_url = '" + url + "'";
        List<Record> list = Db.query(sql);
        return list;
    }
//    public List<Record> getUserAuths(String user_name) throws Exception{
//        String sql = "select * from SYS_USER_AUTH sua,SYS_AUTH sa,SYS_MENU sm where sua.auth_id = sa.auth_id and sa.menu_id = sm.menu_id and sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sa.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sua.user_name = '"+user_name+"'";
//        List<Record> list = Db.query(sql);
//        return list;
//    }

    public Boolean saveUserAuth(String userName,String authId) throws Exception{
        SysUserAuth sysUserAuth = new SysUserAuth();
        //sysUserAuth.set("id",Db.queryBigDecimal("select S_SYS_USER_AUTH.nextval from dual").intValue());
        sysUserAuth.set("user_name",userName);
        sysUserAuth.set("auth_id",authId);
        return sysUserAuth.save();
    }

    public Boolean deleteUserAuth(Integer id) throws Exception{
        return SysUserAuth.dao.deleteById(id);
    }

    public SysUserAuth findSysUserAuth(String userName,String authId){
        String sql = "select * from sys_user_auth where user_name = '"+userName + "' and auth_id = "+authId;
        List<SysUserAuth> list = SysUserAuth.dao.find(sql);
        if(list.size()==0){
            return null;
        }else{
            return list.get(0);
        }
    }

}
