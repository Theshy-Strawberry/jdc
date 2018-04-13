package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.SysRegionalManagement;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/17.
 */
@Service
public class SysRegionalManagementBusiness {
    public Page<SysRegionalManagement> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("sys_regional_management");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<SysRegionalManagement> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = SysRegionalManagement.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum,"  + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    public SysRegionalManagement findMaxCode(String whereSql) {
        SearchCondition sc = new SearchCondition("sys_regional_management");


        String sql = "select * from "+ sc.getTableName() + whereSql;
        List<SysRegionalManagement> sysRegionalManagementList = SysRegionalManagement.dao.find(sql);

        if(sysRegionalManagementList!=null){
            return sysRegionalManagementList.size()!=0?sysRegionalManagementList.get(0):null;
        }else {
            return null;
        }


    }
    public List<SysRegionalManagement> findLikeCode(String code){
        SearchCondition sc = new SearchCondition("sys_regional_management");
        String sql = "select * from "+ sc.getTableName() + " where code like '" + code + "%'";
        List<SysRegionalManagement> sysRegionalManagementList =  SysRegionalManagement.dao.find(sql);
        return sysRegionalManagementList;
    }

    public SysRegionalManagement findByRegLevelAndOrderNumber(Integer regLevel, Integer orderNumber) {
        SearchCondition sc = new SearchCondition("sys_regional_management");
        String sql = "select * from " + sc.getTableName() + " where reg_level = " + regLevel + " and order_number = " + orderNumber;
        List<SysRegionalManagement> sysRegionalManagementList = SysRegionalManagement.dao.find(sql);
        if(sysRegionalManagementList!=null){
            return sysRegionalManagementList.size()==0?null:sysRegionalManagementList.get(0);
        }else {
            return null;
        }

    }


    /**
     * 校验项目名称是否唯一
     * @param orderNumber 要判断的用户名
     * @return Boolean false数据库中已存在相同的用户名，true数据库中不存在此用户名
     */
    public boolean checkOrderNumber(Integer id ,Integer regLevel, Integer orderNumber) throws Exception{
        SearchCondition sc = new SearchCondition("sys_regional_management");
        String sql = "select * from " + sc.getTableName() + " where id <> "+ id +" and reg_level = '" + regLevel + "' and order_number = '" + orderNumber+"'";
        List<SysRegionalManagement> list = SysRegionalManagement.dao.find(sql);
        if(list.size()>0){

            return false;
        }else{
            return true;
        }
    }

    /**
     * 校验项目名称是否唯一
     * @param name 要判断的用户名
     * @return Boolean false数据库中已存在相同的用户名，true数据库中不存在此用户名
     */
    public boolean checkName(Integer id ,String name, Integer regLevel) throws Exception{
        SearchCondition sc = new SearchCondition("sys_regional_management");
        String sql = "select * from " + sc.getTableName() + " where id <> " + id + " and reg_level = '" + regLevel + "' and name = '" + name+"'";
        List<SysRegionalManagement> list = SysRegionalManagement.dao.find(sql);
        if(list.size()>0){

            return false;
        }else{
            return true;
        }
    }

    /**
     * 校验区位号是否唯一
     * @param locationNumber 要判断的区位号
     * @return Boolean false数据库中已存在相同的区位号，true数据库中不存在此区位号
     */
    public boolean checkNameLocationNumber(Integer id ,String locationNumber, Integer regLevel) throws Exception{
        SearchCondition sc = new SearchCondition("sys_regional_management");
        String sql = "select * from " + sc.getTableName() + " where id <> " + id + " and reg_level = '" + regLevel + "' and location_number = '" + locationNumber+"'";
        List<SysRegionalManagement> list = SysRegionalManagement.dao.find(sql);
        if(list.size()>0){

            return false;
        }else{
            return true;
        }
    }








}
