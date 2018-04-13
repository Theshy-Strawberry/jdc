package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.StoreInfo;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@Service
public class StoreInfoBusiness {
    public Page<StoreInfo> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("store_info");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<StoreInfo> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from " + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = StoreInfo.dao.paginate(sc.getPage(), sc.getPageSize(), "select *", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }


    /**
     * 校验保存后的门店名称是否唯一
     * @param channel_name 要判断的门店名称
     * @return Boolean false数据库中已存在相同的门店名称，true数据库中不存在此门店名称
     */
    public Boolean checkNameSave (String channel_name,String channel_id) throws Exception{
        String sql = "select id from store_info where del_flg = 1 and channel_id='"+channel_id+"' and channel_name ='"+ channel_name +"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }



    /**
     * 校验编辑后的门店名称是否唯一
     * @param channel_name 要判断的门店名称是否唯一
     * @return Boolean false数据库中已存在相同的门店名称，true数据库中不存在门店名称是否唯一
     */
    public Boolean checkNameUpdate (String channel_name,String channel_id,String id) throws Exception{
        String sql = "select * from store_info where del_flg = 1 and id <> '"+id+"' and channel_id='"+channel_id+"' and channel_name ='"+ channel_name +"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }


    public List<StoreInfo> getChannelNameList() throws UnsupportedEncodingException{
        String sql = "select * from channel_info where del_flg =1";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }

}
