package controller.systemmanage;

import business.PaySubjectBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.PaySubject;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/14.
 */
@ControllerMapping(value = "/paySubject")
public class PaySubjectController extends BaseController {
    private static final Logger logger = Logger.getLogger(PaySubjectController.class);
    /*注入business*/

    @Autowired
    PaySubjectBusiness business;

    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<PaySubject> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
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
        PaySubject paySubject=null;
        Map<String,String[]>map=this.getParaMap();
        paySubject=(PaySubject)parameter2Model(map,new PaySubject(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");

        try {
            Boolean name=business.checkNameSave(paySubject.getStr("subject_name"));

            if(name){
                paySubject.set("create_date", new Date());
                paySubject.set("create_user",userName);
                paySubject.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(paySubject.save()){
                    this.setAttr("result","success");
                }else {
                    this.setAttr("result","false");
                }

            }else {
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void update(){
        PaySubject paySubject=null;
        Map<String,String[]>map=this.getParaMap();
        paySubject=(PaySubject)parameter2Model(map,new PaySubject(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        try{
            Boolean name=business.checkNameUpdate(paySubject.getStr("subject_name"), paySubject.getStr("id"));
            if(name){
                paySubject.set("modify_date", new Date());
                paySubject.set("modify_user",userName);
                if(paySubject.update()){
                    setAttr("result", "success");
                }else {
                    setAttr("result", "false");
                }
            }else {
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result", "error");
        }


    }
    public void delete(){
        try {
            //取值
            String id = this.getPara("id");
            PaySubject o = new PaySubject();
            o.set("id", id);
            o.set("del_flg", ConstantField.IS_SFSCH_NO);
            if(o.update()){
                setAttr("result", "true");
            }else {
                setAttr("result", "false");
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //查询所有信息
    public void getUserAll(){
        List<PaySubject> list=new ArrayList<PaySubject>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }





}
