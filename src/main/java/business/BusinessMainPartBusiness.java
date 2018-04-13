package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.BusinessMainPart;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@Service
public class BusinessMainPartBusiness {
    /**
     * 查找全部的方法进行分页
     * @param pageSize
     * @param page
     * @param searchSQL
     * @return
     * @throws UnsupportedEncodingException
     */
    public Page<BusinessMainPart> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        //对应数据库的表名business_main_part
        SearchCondition sc = new SearchCondition("business_main_part");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<BusinessMainPart> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = BusinessMainPart.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    /**
     * 校验保存后的公司名称是否唯一
     * @param company_name 要判断的公司名称 查询表中所有数据且del_flg状态位为1为可见状态的数据
     * @return Boolean false数据库中已存在相同的公司名称，true数据库中不存在此公司名称
     */
    public Boolean checkCompanySave (String company_name) throws Exception{
        String sql = "select id from business_main_part where del_flg = 1 and company_name ='"+ company_name +"'";
        List<BusinessMainPart> list = BusinessMainPart.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 校验编辑后的公司名称是否唯一 查询表中所有数据且del_flg状态位为1为可见状态的数据
     * @param company_name 要判断的公司名称
     * @return Boolean false数据库中已存在相同的公司名称，true数据库中不存在此公司名称
     */
    public Boolean checkCompanyUpdate (String company_name,String id) throws Exception{
        String sql = "select * from business_main_part where del_flg = 1 and id <> '"+id+"' and company_name ='"+ company_name +"'";
        List<BusinessMainPart> list = BusinessMainPart.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }


    /**
     *  查询用户真实名且del_flg状态位为1为可见状态的数据
     */
    public List<BusinessMainPart> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user where del_flg = 1";
        List<BusinessMainPart> list = BusinessMainPart.dao.find(sql);
        return list;
    }



}
