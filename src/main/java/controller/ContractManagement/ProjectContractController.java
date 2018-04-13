package controller.ContractManagement;

import business.ProjectContractBusiness;
import business.SysLogBusiness;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import model.ProjectBaseManagement;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ProjectContract;
import model.SysUser;
import java.util.Map;


/**
 * Created by 冕寒 on 2016/6/25.
 */
@ControllerMapping(value = "/projectContract")
public class ProjectContractController extends BaseController {
    private static final Logger logger = Logger.getLogger(ProjectContract.class);
    @Autowired
//      调用Business （ssh-dao）
            ProjectContractBusiness business;
    //      查询方法+分页
    @Autowired
    //调用日志的business
    SysLogBusiness logBusiness;
    public void findAll()  {
        Page<ProjectContract> pageList = null;
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
            ProjectContract projectContract = null;
            Map<String, String[]> map = this.getParaMap();
//            创建新的对象
           projectContract = (ProjectContract) parameter2Model(map, new ProjectContract(), null);
//            重名方法的验证
            Boolean name= business.checkNameSave(projectContract.getStr("project_code"));

//            判断 如果重名 把checkNameError 赋值给result
//            如果不重名继续执行

            SysUser user= this.getSessionAttr("userSession");
            String userName=user.getStr("user_name");
            String expiryBegin=projectContract.get("expiry_begin");
            String expiryEnd=projectContract.get("expiry_end");
            SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin=s.parse(expiryBegin);
            Date dateEnd=s.parse(expiryEnd);

            if (dateBegin.getTime() <dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
                if(name){
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
                    projectContract.set("del_flg", ConstantField.IS_SFSCH_YES);
                    projectContract.set("create_date",new Date());
                    projectContract.set("create_user", userName);
//              判断 如果保存成功 把success赋值给 result 反之把 false赋值给result
                    if(projectContract.save()){
                        String projectCode=projectContract.getStr("project_code");
                        logBusiness.SaveLog("渠道项目合同管理",ConstantField.LOG_ADD,projectCode,userName);
                        this.setAttr("result", "success");
                    }else{
                        this.setAttr("result", "false");
                    }
                }else {
                    this.setAttr("result", "checkNameError");
                }
            }else {
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
        ProjectContract projectContract = null;
        try {
//            从前台获取map
            Map<String, String[]> map = this.getParaMap();
            projectContract = (ProjectContract) parameter2Model(map, new ProjectContract(), null);
            SysUser user= this.getSessionAttr("userSession");
            String userName=user.getStr("user_name");
//            重名验证方法
            Boolean name= business.checkNameUpdate(projectContract.getStr("project_code"), projectContract.getStr("id"));
//            有效时间验证
            String expiryBegin=projectContract.get("expiry_begin");
            String expiryEnd=projectContract.get("expiry_end");
            SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin=s.parse(expiryBegin);
            Date dateEnd=s.parse(expiryEnd);
//            brokerageType.set("modify_date",new Date());
            if (dateBegin.getTime() <dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
                if(name){
                    projectContract.set("modify_date", new Date());
                    projectContract.set("modify_user", userName);
                    if(projectContract.update()){
                        String projectCode = projectContract.getStr("project_code");
                        logBusiness.SaveLog("渠道项目合同管理",ConstantField.LOG_EDIT,projectCode,userName);
                        this.setAttr("result", "success");
                    }else{
                        this.setAttr("result", "false");
                    }
                }else{
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
            ProjectContract o = new ProjectContract();
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
        List<ProjectContract> list=new ArrayList<ProjectContract>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    //查询所有项目基础信息
    public void getProjectBaseManagementList(){
        List<SysUser>list=new ArrayList<SysUser>();
        try{
            list=business.getProjectBaseManagementList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
//    查询所有渠道框架合同信息
    public void getProjectBaseManagementList1(){
        List<SysUser>list=new ArrayList<SysUser>();
        try{
            list=business.getProjectBaseManagementList1();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }
}
