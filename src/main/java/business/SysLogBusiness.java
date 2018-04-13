package business;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.SysLog;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Jr.Rex on 2015/9/14.
 */
@Service
public class SysLogBusiness {
    /**
     * Log4j 日志对象
     */
    private static Logger logger = Logger.getLogger(SysLogBusiness.class);

    /**
     * 保存系统操作日志
     * @param module_name 模块名称
     * @param handle_type 操作说明
     * @param handle_user 操作用户ID
     * @return 保存成功状态
     * @throws Exception
     */
    public Boolean SaveLog(String module_name,int handle_type,String handle_content,String handle_user) throws Exception{
        SysLog log = new SysLog();
        //log.set("id", Db.queryBigDecimal("select S_SYS_LOG.nextval from dual").intValue());
        log.set("module_name",module_name);
        log.set("handle_type",handle_type);
        log.set("handle_user",handle_user);
        log.set("handle_content",handle_content);
        log.set("handle_date", new Date());
        return log.save();
    }

    /**
     * 保存系统操作日志
     * @param module_name 模块名称
     * @param handle_type 操作说明
     * @return 保存成功状态
     * @throws Exception
     */
    public Boolean SaveLog(String module_name,String handle_type,SysUser user) throws Exception{
        SysLog log = new SysLog();
        log.set("id", Db.queryBigDecimal("select S_SYS_LOG.nextval from dual").intValue());

//        SysUser user = CacheKit.get("UserCachingFilter", "user");
        log.set("module_name",module_name);
        log.set("handle_type",handle_type);
        log.set("handle_user",user.getInt("id"));
        log.set("handle_date", new Timestamp(new Date().getTime()));
        return log.save();
    }

    public Page<SysLog> findByPaginate(Integer pageSize, Integer page, String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("sys_log");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<SysLog> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = SysLog.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    public List<SysLog> getModuleNamesList()  throws UnsupportedEncodingException {
        String sql = "select distinct module_name from sys_log";
        List<SysLog> list = SysLog.dao.find(sql);
        return list;
    }
}
