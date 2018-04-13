package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.PosInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/14.
 */
@Service
public class PosInfoBusiness {
    public Page<PosInfo> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("pos_info");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<PosInfo> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = PosInfo.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    /**
     * 校验保存后的pos机编号是否唯一
     * @param pos_code 要判断的pos机编号
     * @return Boolean false数据库中已存在相同的pos机编号，true数据库中不存在此pos机编号
     */
    public Boolean checkNameSave (String pos_code) throws Exception{
        String sql = "select id from pos_info where del_flg = 1 and pos_code ='"+ pos_code +"'";
        List<PosInfo> list = PosInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 校验编辑后的pos机编号是否唯一
     * @param pos_code 要判断的pos机编号
     * @return Boolean false数据库中已存在相同的pos机编号，true数据库中不存在此pos机编号
     */
    public Boolean checkNameUpdate (String pos_code,String id) throws Exception{
        String sql = "select * from pos_info where del_flg = 1 and id <> '"+id+"' and pos_code ='"+ pos_code +"'";
        List<PosInfo> list = PosInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     *  查询用户真实名
     */
    public List<PosInfo> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user";
        List<PosInfo> list = PosInfo.dao.find(sql);
        return list;
    }


}
