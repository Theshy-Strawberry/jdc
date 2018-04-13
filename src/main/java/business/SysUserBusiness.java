package business;

import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by Jr on 15/7/27.
 */
@Service
public class SysUserBusiness {
    private static Logger logger = Logger.getLogger(SysUserBusiness.class);

    public List<SysUser> findAll(String sql) {
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public Page<SysUser> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {

        SearchCondition sc = new SearchCondition("sys_user");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<SysUser> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = SysUser.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by del_flg,id desc");
        }
        return pageList;
    }

    public SysUser getSysUserByUserName(String user_name){
        String sql = "select * from sys_user where user_name = '" + user_name +"' and del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        if(list.size()<=0){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<SysUser> getOfficeSysUserList(String engNo)  throws UnsupportedEncodingException {
//        String sql = "select " +
//                        "s.* " +
//                     "from " +
//                        "SYS_USER s " +
//                     "where s.DEL_FLG=1 and s.OFFICE_ID in " +
//                        "(select " +
//                            "s.OFFICE_ID " +
//                          "from " +
//                            "ENGINEERING_AUTH_MASTER e," +
//                            "SYS_USER s " +
//                          "where " +
//                            "e.ENGINEERING_NO = '" + engNo + "' " +
//                            "and e.AUDITOR_MASTER=s.USER_NAME) " +
//                     "order by s.JOB_TITLE ASC";
        String sql = "select * from SYS_USER where JOB_TITLE = 4 and DEL_FLG = 1 order by JOB_TITLE ASC";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getOfficeSysBackUserList(String engNo)  throws UnsupportedEncodingException {
        String officeSql = "select * from engineering_auth_master m, sys_user u where m.engineering_no = '" + engNo + "' and m.auditor_master = u.user_name)";
        SysUser u = SysUser.dao.findFirst(officeSql);

        String sql = "SELECT * FROM sys_user u WHERE u.user_name IN ( SELECT auditor_junior FROM engineering_auth_junior WHERE engineering_no = '" + engNo + "' UNION SELECT m.auditor_master FROM engineering_auth_master m WHERE m.engineering_no = " + engNo + ") OR  ( u.job_title = 2 AND u.office_id ="+u.getInt("office_id")+")";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getSysUserList(Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or job_title = 4 or (office_id = "+send_office+" and job_title = 2)) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getSysUserLeaderList(Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or job_title = 4) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getSysUserJuniorList(String engineering_no)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where user_name in (select auditor_junior from engineering_auth_junior where engineering_no = '"+engineering_no+"') and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getLeaderList(Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where job_title = 4 and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getReportSysUserList(Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or (office_id = "+send_office+" and job_title = 2)) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getReportToSysUserList(Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 4 or (office_id = (select id from office_info where office_name like '%综合%') and job_title = '2')) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

//    public List<SysUser> getToSysUserList(Integer send_office,Integer job_title)  throws UnsupportedEncodingException {
//        String sql = "";
//        if(job_title < 3){
//            sql = "select id,real_user_name,job_title from sys_user where job_title = 3 or (office_id = "+send_office+" and job_title = 2) ORDER BY job_title asc";
//            List<SysUser> list = SysUser.dao.find(sql);
//            return list;
//        }else if(job_title == 3){
//            sql = "select id,real_user_name,job_title from sys_user where office_id = (select id from office_info where office_name like '%综合%') and job_title = '2' ORDER BY job_title asc";
//            List<SysUser> list = SysUser.dao.find(sql);
//            return list;
//        }else{
//            return null;
//        }
//    }

    public List<SysUser> getManuscriptSysUserList(String project_id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or job_title = 4 or (user_name in (select auditor_master from project_auth_master where project_id = '"+project_id+"'))) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptEngineeringSysUserList(String engineeringNo)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or job_title = 4 or (user_name in (select auditor_master from engineering_auth_master where engineering_no = '"+engineeringNo+"'))) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptContractSysUserList(String contractId)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (user_name in (select audit_master from contract_info where id = '"+contractId+"')) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptArrivedSysUserList(String contractId)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (user_name in (select audit_master from arrived_info where id = '"+contractId+"')) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptBackSysUserList(String project_id,Integer manuscript_id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where user_name = (select create_user from audit_manuscript where id = "+manuscript_id+") and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptEngineeringBackSysUserList(String engineeringNo,Integer manuscript_id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where user_name = (select create_user from engineering_manuscript where id = "+manuscript_id+") and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptContractBackSysUserList(String contractId,Integer manuscript_id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where user_name = (select create_user from contract_manuscript where id = "+manuscript_id+") and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getManuscriptArrivedBackSysUserList(String contractId,Integer manuscript_id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where user_name = (select create_user from contract_manuscript where id = "+manuscript_id+") and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    /**测试审计报告副主任审核后无法报送综合科科员 Start**/
    public List<SysUser> getToSysUserList(Integer send_office,Integer job_title)  throws UnsupportedEncodingException {
        String sql = "";
        if(job_title < 3){
            sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or (office_id = "+send_office+" and job_title = 2)) and del_flg = 1 ORDER BY job_title asc";
            List<SysUser> list = SysUser.dao.find(sql);
            return list;
        }else if(job_title == 3){
            sql = "select id,real_user_name,job_title from sys_user where office_id = (select id from office_info where office_name like '%综合%') and del_flg = 1  ORDER BY job_title asc";
            List<SysUser> list = SysUser.dao.find(sql);
            return list;
        }else{
            return null;
        }
    }
    /**测试审计报告副主任审核后无法报送综合科科员 End**/

    public List<SysUser> getSysUserBackList(String user_name,Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where (job_title = 3 or (office_id = "+send_office+" and job_title = 2) or (office_id = "+send_office+" and user_name = '"+user_name+"')) and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getSysUserLeaderBackList(String user_name,Integer send_office)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,job_title from sys_user where office_id = "+send_office+" and user_name = '"+user_name+"' and del_flg = 1 ORDER BY job_title asc";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }


    public List<SysUser> getUserList()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user where del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getUserListByOffice(SysUser user)  throws UnsupportedEncodingException {

//        SysUser user = CacheKit.get("UserCachingFilter", "user");
        String sql = "select * from sys_user where del_flg = 1 and office_id = " + user.getInt("office_id");
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    /**
     * 校验项目名称是否唯一
     * @param user_name 要判断的用户名
     * @return Boolean false数据库中已存在相同的用户名，true数据库中不存在此用户名
     */
    public Boolean checkUserName (String user_name) throws Exception{
        String sql = "select id from sys_user where user_name ='"+ user_name +"'";
        List<SysUser> list = SysUser.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 校验项目名称是否唯一
     * @param inner_no 要判断的用户名
     * @return Boolean false数据库中已存在相同的用户名，true数据库中不存在此用户名
     */
    public Boolean checkInnerNo (String inner_no) throws Exception{
        String sql = "select id from sys_user where inner_no ='"+inner_no+"' and del_flg=1";
        List<SysUser> list = SysUser.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 校验项目名称是否唯一
     * @param user_name 要判断的科室名称
     * @return 已存在的科室名称集合数
     */
    public Integer checkUserNameUnique (String user_name,String id) throws Exception{
        String sql = "select id from sys_user where del_flg=1 and user_name ='"+ user_name +"' and id != "+ id;
        List<SysUser> list = SysUser.dao.find(sql);
        return list.size();
    }

    /**
     * 校验项目名称是否唯一
     * @param inner_no 要判断的内部编号
     * @return 已存在的内部编号集合数
     */
    public Integer checkInnerNoUnique (String inner_no,String id) throws Exception{
        String sql = "select id from sys_user where del_flg=1 and inner_no ='"+ inner_no +"' and id != "+ id;
        List<SysUser> list = SysUser.dao.find(sql);
        return list.size();
    }

    public List<SysUser> getAuditPersonsList(Integer id)  throws UnsupportedEncodingException {
        String sql = "select id,real_user_name,user_name from sys_user where office_id ="+id+" and job_title = '1' AND del_flg = 1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public List<SysUser> getUserNameList()  throws UnsupportedEncodingException {
        String sql = "select real_user_name,user_name from sys_user where del_flg=1";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }

    public SysUser findRealUserNameByUserName(String userName) {
        String sql = "select real_user_name from sys_user where user_name = '" + userName + "'";
        List<SysUser> list = SysUser.dao.find(sql);
       if(list.size()!=0){
           return list.get(0);
       }else {
           return null;
       }

    }
    //查询省下所有信息
    public List<SysUser> getJobTitleList()  throws UnsupportedEncodingException {
        String sql = "select * from job_title";
        List<SysUser> list = SysUser.dao.find(sql);
        return list;
    }
}
