package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.DataTab;
import model.TabCol;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by liu on 2016/6/24.
 */
@Service
public class DataTabBusiness {
    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）gridOptions
    public Page<DataTab> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("data_tab");
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
            pageList = DataTab.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }

    //查找数据+分页（方法封装好，修改SearchCondition即可 ，SearchCondition 对应 数据库库名）gridOptions2
    public Page<DataTab> findByPaginate2(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("data_col");
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
            pageList = DataTab.dao.paginate(sc.getPage(), sc.getPageSize(), "," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }

    //    校验保存后tab_name是否一致
    public Boolean checkNameSave(String name)throws Exception{
        String sql = "select * from data_tab where tab_name ='"+ name +"' and del_flg=1";
        List<DataTab> list = DataTab.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    //    校验修改后col_name是否一致
    public Boolean checkNameUpdate(String name,String id)throws Exception{
        String sql = "select * from data_tab where id <> '"+id+"' and tab_name ='"+ name +"' and del_flg=1";
        List<DataTab> list = DataTab.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    //  查询TabCol下tab_code对应的col_code
    public List<TabCol> getTabColByTabCode(String code)throws Exception{
        String sql = "select col_code from tab_col where tab_code ='"+ code +"' and del_flg =1";
        List<TabCol> list = TabCol.dao.find(sql);
        return list;
    }

    //查询data_tab下的tab_code信息
    public List<DataTab> getTabCodeById(String id)throws Exception{
        String sql = "select tab_code from data_tab where id ='"+ id +"' and del_flg =1";
        List<DataTab> list = DataTab.dao.find(sql);
        return list;
    }

}
