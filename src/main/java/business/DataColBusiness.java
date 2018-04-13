package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.DataCol;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by liu on 2016/6/24.
 */
@Service
public class DataColBusiness {
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）
    public Page<DataCol> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("data_col");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<DataCol> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = DataCol.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }

    //    校验保存后col_name是否一致
    public Boolean checkNameSave(String name)throws Exception{
        String sql = "select * from data_col where col_name ='"+ name +"'";
        List<DataCol> list = DataCol.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    //    校验修改后col_name是否一致
    public Boolean checkNameUpdate(String name,String id)throws Exception{
        String sql = "select * from data_col where id <> '"+id+"' and col_name ='"+ name +"'";
        List<DataCol> list = DataCol.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
}
