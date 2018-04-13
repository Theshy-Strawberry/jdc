package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.FrameContract;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/25.
 */
@Service
public class FrameContractBusiness {
    /**
     * 查找全部的方法进行分页
     * @param pageSize
     * @param page
     * @param searchSQL
     * @return
     * @throws UnsupportedEncodingException
     */
    public Page<FrameContract> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        //对应数据库的表名frame_contract
        SearchCondition sc = new SearchCondition("frame_contract");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<FrameContract> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = FrameContract.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    /**
     * 查询所有甲方主体信息且del_flg状态位为1为可见状态的数据
     */
    public List<FrameContract> getMainPartAll()  throws UnsupportedEncodingException {
        String sql = "select * from business_main_part where del_flg = 1";
        List<FrameContract> list = FrameContract.dao.find(sql);
        return list;
    }

    /**
     * 查询所有渠道信息且del_flg状态位为1为可见状态的数据
     */
    public List<FrameContract> getChannelAll()  throws UnsupportedEncodingException {
        String sql = "select * from channel_info where del_flg = 1";
        List<FrameContract> list = FrameContract.dao.find(sql);
        return list;
    }

    /**
     * 查询所有门店信息且del_flg状态位为1为可见状态的数据
     */
    public List<FrameContract> getStoreAll()  throws UnsupportedEncodingException {
        String sql = "select * from store_info where del_flg = 1";
        List<FrameContract> list = FrameContract.dao.find(sql);
        return list;
    }

    /**
     * 查询渠道下所有门店信息且del_flg状态位为1为可见状态的数据，把渠道id传到后台对应相应的多个门店
     */
    public List<FrameContract> getAllList(int id)  throws UnsupportedEncodingException {
        String sql = "select * from store_info where del_flg = 1 and channel_id = "+id;
        List<FrameContract> list = FrameContract.dao.find(sql);
        return list;
    }

    /**
     * 校验保存后的合同编号是否唯一
     * @param contract_number 要判断的合同编号是否唯一
     * @return Boolean false数据库中已存在相同的合同编号，true数据库中不存在此合同编号
     */
    public Boolean checkNameSave (String contract_number) throws Exception{
        String sql = "select id from frame_contract where del_flg = 1 and contract_number ='"+ contract_number +"'";
        List<FrameContract> list = FrameContract.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 校验编辑后的合同编号是否唯一
     * @param contract_number 要判断的合同编号是否唯一
     * @return Boolean false数据库中已存在相同的合同编号，true数据库中不存在此合同编号是否唯一
     */
    public Boolean checkNameUpdate (String contract_number,String id) throws Exception{
        String sql = "select * from frame_contract where del_flg = 1 and id <> '"+id+"' and contract_number ='"+ contract_number +"'";
        List<FrameContract> list = FrameContract.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }


}

