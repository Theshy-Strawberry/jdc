package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.ProjectBaseManagement;
import model.ProjectPersonnel;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by liu on 2016/6/28.
 */
@Service
public class ProjectPersonnelBusiness {
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

    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）gridOptions2
    public Page<ProjectPersonnel> findByPaginate2(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("project_personnel");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<ProjectPersonnel> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = ProjectPersonnel.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }


    //    校验修改后col_name是否一致
    public Boolean checkNameSave(String projectId,String name,String id)throws Exception{
        String sql = "select * from project_personnel where project_base_id = '"+projectId+"' and user_name ='"+ name +"' and del_flg = 1";
        if(id != null){
            sql = " and id <> '"+id+"'";
        }
        List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
        if(list.size()>1){
            return false;
        }else{
            return true;
        }
    }

    //  查询项目人员表下所有人员
    public List<ProjectPersonnel> getPersonnelByProjectId(String id)throws Exception{
        String sql = "select * from project_personnel where project_base_id ='"+ id +"' and del_flg =1";
        List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
        return list;
    }
    /**
     *  查询用户真实名
     */
    public List<ProjectPersonnel> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user where del_flg=1";
        List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
        return list;
    }

    //查询业务主体下的所有信息
    public List<ProjectPersonnel> getBusinessMainPartList()  throws UnsupportedEncodingException {
        String sql = "select * from business_main_part where del_flg = 1";
        List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
        return list;
    }
}
