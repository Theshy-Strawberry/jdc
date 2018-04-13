package controller.projectmanage;

import business.ProjectBaseManagementBusiness;
import business.SysLogBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.ProjectBaseManagement;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.SysUser;
import java.util.Map;
/**
 * Created by 冕寒 on 2016/6/23.
 */
@ControllerMapping(value = "/projectBaseManagement")
public class ProjectBaseManagementController extends BaseController  {
    private static final Logger logger = Logger.getLogger(ProjectBaseManagement.class);
    @Autowired
//      调用Business （ssh-dao）
            ProjectBaseManagementBusiness business;
    @Autowired
    SysLogBusiness logBusiness;
    //      查询方法+分页
    public void findAll()  {
        Page<ProjectBaseManagement> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql = " and del_flg = '1'" + searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    //            保存方法
    public void save() {


        try {
            ProjectBaseManagement projectBaseManagement = null;
            Map<String, String[]> map = this.getParaMap();
//            创建新的对象
            projectBaseManagement = (ProjectBaseManagement) parameter2Model(map, new ProjectBaseManagement(), null);
//            重名方法的验证
            Boolean name= business.checkNameSave(projectBaseManagement.getStr("project_id"));

//            判断 如果重名 把checkNameError 赋值给result
//            如果不重名继续执行

            SysUser user= this.getSessionAttr("userSession");
            String userName=user.getStr("user_name");
            projectBaseManagement.set("del_flg", ConstantField.IS_SFSCH_YES);
            projectBaseManagement.set("create_date", new Date());
            projectBaseManagement.set("create_user", userName);
            String expiryBegin = projectBaseManagement.get("effective_begin_date");
            String expiryEnd = projectBaseManagement.get("effective_end_date");
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin = s.parse(expiryBegin);
            Date dateEnd = s.parse(expiryEnd);
            if (dateBegin.getTime() < dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
            if(name) {
//                从前台获取时间的字符串
//                String createDate=brokerageType.getStr("create_date");
////                设定格式化格式
//                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////                创建格式化时间对象对从前台获取的时间 进行格式化
//                Date date=simpleDateFormat.parse(createDate);
////                把date格式化后的对象 赋值给create_date
//                brokerageType.set("create_date",date);
//                第二种
//                brokerageType.set("create_date",brokerageType.getStr("create_date"));

//                设置删除标识为1（可见）


//              判断 如果保存成功 把success赋值给 result 反之把 false赋值给result
                    if (projectBaseManagement.save()) {
                        String projectId = projectBaseManagement.getStr("project_id");
                        logBusiness.SaveLog("项目基础管理",ConstantField.LOG_ADD,projectId,userName);
                        this.setAttr("result", "success");
                    } else {
                        this.setAttr("result", "false");
                    }
                } else {
                this.setAttr("result", "checkNameError");
                }
            }else{
                this.setAttr("result", "date");
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
//            异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }

    }
    //修改方法
    public void update() {
//        新建一个brokerageType对象并初始化
        ProjectBaseManagement projectBaseManagement = null;
        try {
//            从前台获取map
            Map<String, String[]> map = this.getParaMap();
            projectBaseManagement = (ProjectBaseManagement) parameter2Model(map, new ProjectBaseManagement(), null);
            SysUser user= this.getSessionAttr("userSession");
            String userName=user.getStr("user_name");
//            重名验证方法
            Boolean name= business.checkNameUpdate(projectBaseManagement.getStr("project_id"), projectBaseManagement.getStr("id"));
//            brokerageType.set("modify_date",new Date());
            String expiryBegin=projectBaseManagement.get("effective_begin_date");
            String expiryEnd=projectBaseManagement.get("effective_end_date");
            SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin=s.parse(expiryBegin);
            Date dateEnd=s.parse(expiryEnd);
            if (dateBegin.getTime() <dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
                if (name) {
                    projectBaseManagement.set("modify_date", new Date());
                    projectBaseManagement.set("modify_user", userName);
                    if (projectBaseManagement.update()) {
                        String projectId = projectBaseManagement.getStr("project_id");
                        logBusiness.SaveLog("项目基础管理",ConstantField.LOG_EDIT,projectId,userName);
                        this.setAttr("result", "success");
                    } else {
                        this.setAttr("result", "false");
                    }
                } else {
                    this.setAttr("result", "checkNameError");
                }
            }else {
               this.setAttr("result", "date");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    //删除方法 就是改标记
    public void delete(){
        try {
            //取值
//            删除标记
//            第一种方法
            String id = this.getPara("id");
            ProjectBaseManagement o = new ProjectBaseManagement();
            o.set("id",id);
            o.set("del_flg", ConstantField.IS_SFSCH_NO);
            if(o.update()){
                setAttr("result", "true");
            }else{
                setAttr("result", "false");
            }
//            第二种方法
//            BrokerageType.dao.findById(id).set("del_flg", ConstantField.IS_SFSCH_NO).update();
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    public void getUserAll(){
        List<ProjectBaseManagement> list=new ArrayList<ProjectBaseManagement>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
    //查询所有公司信息【乙方主体】
    public void getBusinessMainPartList(){
        List<SysUser>list=new ArrayList<SysUser>();
        try{
            list=business.getBusinessMainPartList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
}
