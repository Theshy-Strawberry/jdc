package controller.systemmanage;

import business.BusinessMainPartBusiness;
import business.SysLogBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.BusinessMainPart;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@ControllerMapping(value = "/businessMainPart")
public class BusinessMainPartController extends BaseController {
    private static final Logger logger = Logger.getLogger(BusinessMainPartController.class);
    /*注入BusinessMainPartBusiness*/

    @Autowired
    //调用Business
    BusinessMainPartBusiness business;
    @Autowired
    //调用日志的business
    SysLogBusiness logBusiness;
    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<BusinessMainPart> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            // 查找删除状态位为1的可见数据
            String whereSql = "and del_flg = '1'" + searchSQL;
            pageList=business.findByPaginate(pageSize,page,whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }


    }

    /**
     * 保存的方法
     */
    public void save(){
        //业务主体类对象为空
        BusinessMainPart businessMainPart=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        businessMainPart=(BusinessMainPart)parameter2Model(map,new BusinessMainPart(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过用户Session值获取用户名
        String userName=user.getStr("user_name");
        try {
            //公司名称的验证方法
            Boolean name=business.checkCompanySave(businessMainPart.getStr("company_name"));
            //如果通过验证
            if(name){
//              String createDate=businessMainPart.getStr("create_date");
//              SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//              Date date = format.parse(createDate);
                //设置创建时间
                businessMainPart.set("create_date", new Date());
                //设置创建人
                businessMainPart.set("create_user", userName);
                //设置删除状态位
                businessMainPart.set("del_flg", ConstantField.IS_SFSCH_YES);
                //如果保存成功
                if(businessMainPart.save()){
                    //返回success
                    String companyName= businessMainPart.getStr("company_name");
                    logBusiness.SaveLog("业务主体管理",ConstantField.LOG_ADD,companyName,userName);
                    this.setAttr("result","success");
                }else {
                    //返回false
                    this.setAttr("result","false");
                }

            }else {
                //否则公司名称没有通过验证
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    /**
     * 更新的方法
     */
    public void update(){
        //业务主体类对象为空
        BusinessMainPart businessMainPart=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        businessMainPart=(BusinessMainPart)parameter2Model(map,new BusinessMainPart(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过Session值获取用户名
        String userName=user.getStr("user_name");
        try{
//          businessMainPart.remove("create_date");
//          businessMainPart.remove("modify_date");
            //公司名称验证
            Boolean name=business.checkCompanyUpdate(businessMainPart.getStr("company_name"), businessMainPart.getStr("id"));
            //如果通过验证
            if(name){
                //设置创建时间
                businessMainPart.set("create_date", new Date());
                //如设置创建人
                businessMainPart.set("create_user", userName);
                //如果更新成功
                if(businessMainPart.update()){
                    String companyName = businessMainPart.getStr("company_name");
                    logBusiness.SaveLog("业务主体管理",ConstantField.LOG_EDIT,companyName,userName);
                    //返回success
                    setAttr("result", "success");
                }else {
                    //返回false
                    setAttr("result", "false");
                }
            }else {
                //否则没有通过公司名称验证
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result", "error");
        }


    }
    /**
     * 删除的方法
     */
    public void delete(){
        try {
            //从前台获取id
            String id = this.getPara("id");
            //创建对象
            BusinessMainPart o = new BusinessMainPart();
            //设置id
            o.set("id", id);
            //设置删除标志位为0，为不可见状态
            o.set("del_flg", ConstantField.IS_SFSCH_NO);
            //如果更新成功
            if(o.update()){
                //返回true
                setAttr("result", "true");
            }else {
                //否则返回false
                setAttr("result", "false");
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    /**
     * 查询用户名的方法
     */
    public void getUserAll(){
        List<BusinessMainPart> list=new ArrayList<BusinessMainPart>();
        try{
            //调用BusinessMainPartBusiness里的getUserAll方法
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }





}

