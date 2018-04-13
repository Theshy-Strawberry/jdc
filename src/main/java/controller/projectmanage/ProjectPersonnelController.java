package controller.projectmanage;

import business.ProjectPersonnelBusiness;
import business.SysLogBusiness;
import com.alibaba.fastjson.JSON;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.ProjectBaseManagement;
import model.ProjectPersonnel;
import model.SysUser;
import model.TabCol;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by liu on 2016/6/28.
 */
@ControllerMapping(value = "/projectPersonnel")
public class ProjectPersonnelController extends BaseController {
    private static final Logger logger = Logger.getLogger(ProjectPersonnel.class);
    @Autowired
    ProjectPersonnelBusiness business;
    //      查询方法+分页
    @Autowired
    //调用日志的business
    SysLogBusiness logBusiness;

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

    //      查询方法+分页
    public void findAll2()  {
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            //Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            //Integer page = this.getParaToInt("page");
            Integer projectBaseId = this.getParaToInt("id");
            String sql = "select @rownum:=@rownum+1 AS rownum,project_personnel.* from (SELECT @rownum:=0) r,project_personnel  where del_flg = '1' and project_base_id='"+projectBaseId+"' ";
            List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
            //pageList = business.findByPaginate2(pageSize, page, whereSql);
            this.setAttr("result", list);
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }


    public void save() throws UnsupportedEncodingException {
        SysUser user= this.getSessionAttr("userSession");
        try {
            //保存dataTab名称和代码
            String name = this.getPara("project_base_name");
            String project_id = this.getPara("project_base_id");
            String[] arr = this.getParaValues("arr");
            String str = Arrays.toString(arr);
            String str1 = str.replaceAll("\\[", "");
            String str2 = str1.replaceAll("\\]", "");
            String[] str3 = str2.split(",");
            for (int i = 0 ; i <str3.length;i++ ) {
                ProjectPersonnel o = new ProjectPersonnel();
                o.set("project_base_id", project_id);
                o.set("user_id", Integer.parseInt(str3[i].replaceAll(" ", "")));
                String sql = "select * from sys_user where id = '"+str3[i].replaceAll(" ", "")+"'";
                SysUser sysUser = SysUser.dao.findFirst(sql);
                o.set("user_name", sysUser.get("user_name"));
                o.set("del_flg", ConstantField.IS_SFSCH_YES);
                o.set("create_date",new Date());
                o.set("create_user", user.getStr("user_name"));
                if(o.save()){
                    logBusiness.SaveLog("项目人员管理",ConstantField.LOG_ADD,name,user.getStr("user_name"));
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
            //异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }
    }

    //删除方法 就是改标记
    public void delete() {
        try {
            //取值
            String id = this.getPara("id");
            ProjectPersonnel o = new ProjectPersonnel();
            o.set("id", id);
            o.set("del_flg", ConstantField.IS_SFSCH_NO);
            if (o.update()) {
                setAttr("result", "true");
            } else {
                setAttr("result", "false");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //查询所有col_code信息
    public void getPersonnelByProjectId(){
        String id = this.getPara("id");
        List<ProjectPersonnel> list=new ArrayList<ProjectPersonnel>();
        try{
            list=business.getPersonnelByProjectId(id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    //批量修改状态
    public void deleteByProjectId(){
        try {
            //取值
            String projectId = this.getPara("project_id");
            String sql = "select * from project_personnel where project_base_id='"+projectId+"' and del_flg =1";
            List<ProjectPersonnel> list = ProjectPersonnel.dao.find(sql);
            for(int i=0;i<list.size();i++){
                ProjectPersonnel o = new ProjectPersonnel();
                o.set("id",list.get(i).get("id"));
                o.set("del_flg", ConstantField.IS_SFSCH_NO);
                if (o.update()) {
                    setAttr("result", "true");
                } else {
                    setAttr("result", "false");
                }
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void getUserAll(){
        List<ProjectPersonnel> list=new ArrayList<ProjectPersonnel>();
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
        List<ProjectPersonnel>list=new ArrayList<ProjectPersonnel>();
        try{
            list=business.getBusinessMainPartList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

}
