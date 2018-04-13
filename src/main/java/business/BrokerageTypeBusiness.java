package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.BrokerageType;
import model.BusinessMainPart;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 冕寒 on 2016/6/8.
 * dao
 */
@Service
public class BrokerageTypeBusiness  {
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）
    public Page<BrokerageType> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("brokerage_type");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<BrokerageType> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = BrokerageType.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }
//    校验保存后type_name是否一致
    public Boolean checkNameSave(String type_name)throws Exception{
        String sql = "select id from brokerage_type where del_flg = 1 and type_name ='"+ type_name +"'";
        List<BrokerageType> list = BrokerageType.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
//    校验修改后type_name是否一致
    public Boolean checkNameUpdate(String type_name,String id)throws Exception{
        String sql = "select * from brokerage_type where del_flg = 1 and id <> '"+id+"' and type_name ='"+ type_name +"'";
        List<BrokerageType> list = BrokerageType.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     *  查询用户真实名
     */
    public List<BrokerageType> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user";
        List<BrokerageType> list = BrokerageType.dao.find(sql);
        return list;
    }
}
