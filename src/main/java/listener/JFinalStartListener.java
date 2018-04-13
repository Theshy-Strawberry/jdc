package listener;

import business.*;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.filter.cache.CacheFilter;
import model.SysMenu;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/12.
 */
public class JFinalStartListener {
    private static Logger logger = Logger.getLogger(JFinalStartListener.class);

    public void init(){
        try{
            getCurrentYear();
            getUserInfoList();
            getMenu();
            getTreeMenu();
            println();
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    private static void println() {
        logger.info("System initialization is complete.");
    }

    private static void getCurrentYear(){
        String year = new SimpleDateFormat("yyyy").format(new Date());
        CacheKit.put("SystemCache","currentYear",year);
    }

    private static void getUserInfoList(){
        SysUserBusiness business =  new SysUserBusiness();
        try {
            List list = business.getUserList();
            if (list.size()>0){
                CacheKit.put("SystemCache","sysUser",list);
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        }
    }

    public static void getMenu(){
        SysMenuBusiness business = new SysMenuBusiness();
        try {
            List<SysMenu> list = business.getRootList();
            if (list.size()>0){
                CacheKit.put("SystemCache","sysMenu",list);
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
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
