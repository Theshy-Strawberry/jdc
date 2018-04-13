package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.ProductType;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@Service
public class ProductTypeBusiness {
    public Page<ProductType> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("product_type");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<ProductType> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = ProductType.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }
    /**
     * 校验保存后的产品类型名称是否唯一
     * @param type_name 要判断的产品类型名称
     * @return Boolean false数据库中已存在相同的产品类型名称，true数据库中不存在此产品类型名称
     */
    public Boolean checkNameSave (String type_name) throws Exception{
        String sql = "select id from product_type where del_flg = 1 and type_name ='"+ type_name +"'";
        List<ProductType> list = ProductType.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 校验编辑后的产品类型名称是否唯一
     * @param type_name 要判断的产品类型名称
     * @return Boolean false数据库中已存在相同的产品类型名称，true数据库中不存在此产品类型名称
     */
    public Boolean checkNameUpdate (String type_name,String id) throws Exception{
        String sql = "select * from product_type where del_flg = 1 and id <> '"+id+"' and type_name ='"+ type_name +"'";
        List<ProductType> list = ProductType.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     *  查询用户真实名
     */
    public List<ProductType> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user";
        List<ProductType> list = ProductType.dao.find(sql);
        return list;
    }

}
