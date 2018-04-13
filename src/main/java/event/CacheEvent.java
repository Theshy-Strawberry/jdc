package event;

import business.*;
import com.jfinal.plugin.ehcache.CacheKit;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/18.
 */
public class CacheEvent {

    public static void getSysUserList() throws UnsupportedEncodingException {
        SysUserBusiness business =  new SysUserBusiness();
        List list = business.getUserList();
        if (list.size()>0){
            CacheKit.put("SystemCache","sysUser",list);
        }
    }

}
