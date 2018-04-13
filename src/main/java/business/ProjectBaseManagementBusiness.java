package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.ProjectBaseManagement;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 冕寒 on 2016/6/23.
 */
@Service
public class ProjectBaseManagementBusiness {
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
            pageList = ProjectBaseManagement.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }
    //    校验保存后project_id是否一致
//     还有别的字段需要添加在此处 project_name
    public Boolean checkNameSave(String project_id)throws Exception{
        String sql = "select id from project_base_management where del_flg = 1 and project_id ='"+ project_id +"'";
        List<ProjectBaseManagement> list = ProjectBaseManagement.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    //    校验修改后tproject_id是否一致
    public Boolean checkNameUpdate(String project_id,String id)throws Exception{
        String sql = "select * from project_base_management where del_flg = 1 and id <> '"+id+"' and project_id ='"+ project_id +"'";
        List<ProjectBaseManagement> list = ProjectBaseManagement.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
////    校验保存后project_name是否一致
//    public Boolean checkNameSaveProjectName(String project_name)throws Exception{
//        String sql = "select id from project_base_management where del_flg = 1 and project_name ='"+ project_name +"'";
//        List<ProjectBaseManagement> list = ProjectBaseManagement.dao.find(sql);
//        if(list.size()>0){
//            return false;
//        }else{
//            return true;
//        }
//    }
//    //    校验修改后project_name是否一致
//    public Boolean checkNameUpdateProjectName(String project_name,String id)throws Exception{
//        String sql = "select * from project_base_management where del_flg = 1 and id <> '"+id+"' and project_name ='"+ project_name +"'";
//        List<ProjectBaseManagement> list = ProjectBaseManagement.dao.find(sql);
//        if(list.size()>0){
//            return false;
//        }else{
//            return true;
//        }
//    }
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
        String sql = "select * from business_main_part where del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }
}
