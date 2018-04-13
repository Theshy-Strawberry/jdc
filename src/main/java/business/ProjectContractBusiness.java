package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.ProjectBaseManagement;
import model.ProjectContract;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 冕寒 on 2016/6/25.
 */
@Service
public class ProjectContractBusiness {
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）
    public Page<ProjectContract> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("project_contract");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<ProjectContract> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = ProjectContract.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }
    //    校验保存后project_id是否一致
//     还有别的字段需要添加在此处 project_name
    public Boolean checkNameSave(String project_code)throws Exception{
        String sql = "select id from project_contract where del_flg = 1 and project_code ='"+ project_code +"'";
        List<ProjectContract> list = ProjectContract.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    //    校验修改后tproject_id是否一致
    public Boolean checkNameUpdate(String project_code,String id)throws Exception{
        String sql = "select * from project_contract where del_flg = 1 and id <> '"+id+"' and project_code ='"+ project_code +"'";
        List<ProjectContract> list = ProjectContract.dao.find(sql);
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
    public List<ProjectContract> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user";
        List<ProjectContract> list = ProjectContract.dao.find(sql);
        return list;
    }

    //查询项目基础管理下的所有信息 getBusinessMainPartList
    public List<SysUser> getProjectBaseManagementList()  throws UnsupportedEncodingException {
        String sql = "select * from project_base_management where del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }
    //查询渠道框架合同下的所有信息 getBusinessMainPartList
    public List<SysUser> getProjectBaseManagementList1()  throws UnsupportedEncodingException {
        String sql = "select * from frame_contract where del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }
}
