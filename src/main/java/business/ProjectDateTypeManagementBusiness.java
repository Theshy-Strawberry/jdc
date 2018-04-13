package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 冕寒 on 2016/6/23.
 */
@Service
public class ProjectDateTypeManagementBusiness {
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）
    public Page<ProjectBaseManagement> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("project_base_management");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<ProjectBaseManagement> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = ProjectBaseManagement.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL);
        }
        return pageList;
    }
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）gridOptions2
    public Page<DataTab> findByPaginate2(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("tab_col");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<DataTab> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = DataTab.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL);
        }
        return pageList;
    }

    public Page<UserTab> findByPaginate3(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("user_tab");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<UserTab> pageList = null;
        String sql = "";
        sql = "from (SELECT @rownum:=0) r,(select distinct(tab_code),project_id from user_tab where 0=0 " + searchSQL+" ) ut";
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = UserTab.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum,ut.* ",sql);
        }
        return pageList;
    }

    public Page<UserTabCol> findByPaginate4(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("user_tab_col");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<UserTabCol> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = UserTabCol.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL);
        }
        return pageList;
    }
    /**
     *  查询用户真实名
     */
    public List<ProjectBaseManagement> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user";
        List<ProjectBaseManagement> list = ProjectBaseManagement.dao.find(sql);
        return list;
    }

    //查询业务主体下的所有信息
    public List<SysUser> getBusinessMainPartList()  throws UnsupportedEncodingException {
        String sql = "select * from business_main_part";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<DataTab> getDataType()  throws UnsupportedEncodingException {
        String sql = "select * from data_tab";
        List<DataTab> list = DataTab.dao.find(sql);
        return list;
    }

    public List<DataCol> getDataTypeName()  throws UnsupportedEncodingException {
        String sql = "select * from data_col where del_flg = 1";
        List<DataCol> list = DataCol.dao.find(sql);
        return list;
    }

    public List<ProjectPersonnel> getProjectUser(Integer project_id)  throws UnsupportedEncodingException {
        String sql = "select p.*,s.real_name from project_personnel p left join sys_user s on s.id = p.user_id where p.del_flg = 1 and p.project_base_id = "+project_id;
        List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
        return list;
    }

    public Boolean checkUserTabColSave(String name,Integer project_id)throws Exception{
        String sql = "select * from user_tab_col where user_name ='"+ name +"' and project_id = " + project_id;
        List<UserTabCol> list = UserTabCol.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    public Boolean checkUserTabSave(Integer user_id,Integer project_id,String tab_code)throws Exception{
        String sql = "select * from user_tab where del_flg = 1 and user_id ='"+ user_id +"' and tab_code ='"+ tab_code +"' and project_id = " + project_id;
        List<UserTab> list = UserTab.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    public Boolean checkTabPermissionSave(String name,Integer project_id)throws Exception{
        String sql = "select * from tab_permission where user_name ='"+ name +"' and project_id = " + project_id;
        List<TabPermission> list = TabPermission.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
}
