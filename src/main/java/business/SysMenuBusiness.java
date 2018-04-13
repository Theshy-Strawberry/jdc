package business;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.ConstantField;
import model.SysAuth;
import model.SysMenu;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/14.
 */
@Service
public class SysMenuBusiness {

    public List<SysMenu> getSysMenuList()  throws UnsupportedEncodingException {
        String sql = "select * from sys_menu";
        List<SysMenu> list = SysMenu.dao.find(sql);
        return list;
    }

    public SysMenu findById(String id){
        SysMenu o = SysMenu.dao.findById(id);
        return o;
    }

    public List<SysMenu> getUserMenuList(String user_name){
        String sql = "select * from sys_menu where del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and parent_id = 0 order by sort_code";
        String userSQL = " select sm.menu_id menu_id,sm.parent_id parent_id from SYS_USER_AUTH sua,SYS_AUTH sa,SYS_MENU sm  where  sua.auth_id = sa.auth_id and sa.menu_id = sm.menu_id and  sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and sa.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and  sa.auth_type = 'NODE' and  sua.user_name = '"+user_name+"'";
        List<SysMenu> list = SysMenu.dao.find(sql);
        List<Object[]> userList = Db.query(userSQL);
        List<SysMenu> rootList = new ArrayList<SysMenu>();
        List<SysMenu> treeList = new ArrayList<SysMenu>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < userList.size(); j++) {
                if(list.get(i).getInt("menu_id") == Integer.valueOf(userList.get(j)[0].toString()) && Integer.valueOf(userList.get(j)[1].toString()) == 0){
                    rootList.add(list.get(i));
                }
            }
        }
        for (int s = 0; s < rootList.size(); s++) {
            SysMenu sysMenu = rootList.get(s);
            String nextSQL = "select * from sys_menu where parent_id = " +rootList.get(s).getInt("menu_id") + " and del_flg = "+ ConstantField.IS_DEL_FLG_ON ;
            List<SysMenu> nextList = SysMenu.dao.find(nextSQL);
            List<SysMenu> parentList = new ArrayList<SysMenu>();
            for (int p = 0; p < nextList.size(); p++) {
                for (int j = 0; j < userList.size(); j++) {
                    if(nextList.get(p).getInt("menu_id") == Integer.valueOf(userList.get(j)[0].toString()) && Integer.valueOf(userList.get(j)[1].toString()) != 0){
                        parentList.add(nextList.get(p));
                    }
                }
            }
            sysMenu.set("pages",parentList);
            treeList.add(sysMenu);
        }
        return treeList;
    }

    public List<SysMenu> getRootList()  throws UnsupportedEncodingException {
        String sql = "select * from sys_menu where del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and parent_id = 0";
        List<SysMenu> treeList = new ArrayList<SysMenu>();
        List<SysMenu> list = SysMenu.dao.find(sql);
        for (int i = 0; i < list.size(); i++) {
            SysMenu sysMenu = list.get(i);
            String nextSQL = "select * from sys_menu where parent_id = " +list.get(i).getInt("menu_id") + " and del_flg = "+ ConstantField.IS_DEL_FLG_ON ;
            List<SysMenu> nextList = SysMenu.dao.find(nextSQL);
            sysMenu.set("pages",nextList);
            treeList.add(sysMenu);
        }
        return treeList;
    }

    public List<TreeModel> getSysRootList()  throws UnsupportedEncodingException {
        String sql = "select * from sys_menu where del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and parent_id = 0";
        List<TreeModel> treeList = new ArrayList<TreeModel>();
        List<SysMenu> list = SysMenu.dao.find(sql);
        for (int i = 0; i < list.size(); i++) {
            TreeModel model = new TreeModel();
            model.setId(list.get(i).getInt("menu_id"));
            model.setMenu(list.get(i).getInt("menu_id") + "");
            model.setRoot(list.get(i).getInt("menu_id") + "");
            model.setParent(list.get(i).getInt("menu_id") + "");
            model.setTitle(list.get(i).getStr("menu_name"));
            String nextSQL = "select * from sys_menu where parent_id = " +list.get(i).getInt("menu_id") + " and del_flg = "+ ConstantField.IS_DEL_FLG_ON ;
            List<SysMenu> nextList = SysMenu.dao.find(nextSQL);
            List<TreeModel> treeList2 = new ArrayList<TreeModel>();
            for (int j = 0; j < nextList.size(); j++) {
                TreeModel nextModel = new TreeModel();
                nextModel.setId(nextList.get(j).getInt("menu_id"));
                nextModel.setMenu(list.get(i).getInt("menu_id") + "");
                nextModel.setRoot(list.get(i).getInt("menu_id") + "");
                nextModel.setParent(list.get(i).getInt("menu_id") + "");
                nextModel.setTitle(nextList.get(j).getStr("menu_name"));
                nextModel.setNodes(new ArrayList());
                treeList2.add(nextModel);
            }
            model.setNodes(treeList2);
            treeList.add(model);
        }
        return treeList;
    }

    public List<TreeModel> getUserSysRootList(String userName)  throws Exception {
        String sql = "select sa.auth_id as authId,sm.* from sys_menu sm,sys_auth sa where sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON +" and parent_id = 0 and sm.menu_id=sa.menu_id";
        List<TreeModel> treeList = new ArrayList<TreeModel>();
        List<SysMenu> list = SysMenu.dao.find(sql);
        for (int i = 0; i < list.size(); i++) {
            TreeModel model = new TreeModel();
            model.setId(list.get(i).getInt("menu_id"));
            model.setMenu(list.get(i).getInt("menu_id") + "");
            model.setRoot(list.get(i).getInt("menu_id") + "");
            model.setParent(list.get(i).getInt("menu_id") + "");
            model.setTitle(list.get(i).getStr("menu_name"));
            model.setAuthId(list.get(i).getInt("authId"));
            String nextSQL = "select sa.auth_id as authId,sm.* from sys_menu sm,sys_auth sa where sm.parent_id = " +list.get(i).getInt("menu_id") + " and sm.menu_id=sa.menu_id and sa.auth_name='查看权限' and sm.del_flg = "+ ConstantField.IS_DEL_FLG_ON ;
            List<Record> userAuthRootList = new SysAuthBusiness().getUserAuthList(userName, list.get(i).getInt("menu_id").toString());
            model.setBakNodes(userAuthRootList);
            List<SysMenu> nextList = SysMenu.dao.find(nextSQL);
            List<TreeModel> treeList2 = new ArrayList<TreeModel>();
            for (int j = 0; j < nextList.size(); j++) {
                TreeModel nextModel = new TreeModel();
                nextModel.setId(nextList.get(j).getInt("menu_id"));
                nextModel.setMenu(list.get(i).getInt("menu_id") + "");
                nextModel.setRoot(list.get(i).getInt("menu_id") + "");
                nextModel.setParent(list.get(i).getInt("menu_id") + "");
                nextModel.setTitle(nextList.get(j).getStr("menu_name"));
                nextModel.setAuthId(nextList.get(j).getInt("authId"));
                List<Record> userAuthList = new SysAuthBusiness().getUserAuthList(userName, nextList.get(j).getInt("menu_id").toString());
                nextModel.setBakNodes(userAuthList);
                List<SysAuth> userAuthList2 = new SysAuthBusiness().getSysAuthList(nextList.get(j).getInt("menu_id").toString());
                nextModel.setNodes(userAuthList2);
                treeList2.add(nextModel);
            }
            model.setNodes(treeList2);
            treeList.add(model);
        }
        return treeList;
    }

}
