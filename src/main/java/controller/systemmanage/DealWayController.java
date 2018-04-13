package controller.systemmanage;

import business.DealWayBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.DealWay;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@ControllerMapping(value = "/dealWay")
public class DealWayController extends BaseController {
    private static final Logger logger = Logger.getLogger(DealWayController.class);
    /*注入business*/

    @Autowired
    DealWayBusiness business;

    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<DealWay> pageList = null;
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
        DealWay dealWay=null;
        Map<String,String[]>map=this.getParaMap();
        dealWay=(DealWay)parameter2Model(map,new DealWay(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");

        try {
            Boolean name=business.checkNameSave(dealWay.getStr("way_name"));

            if(name){
                dealWay.set("create_date", new Date());
                dealWay.set("create_user", userName);
                dealWay.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(dealWay.save()){
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
        DealWay dealWay=null;
        Map<String,String[]>map=this.getParaMap();
        dealWay=(DealWay)parameter2Model(map,new DealWay(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        try{

            Boolean name=business.checkNameUpdate(dealWay.getStr("way_name"), dealWay.getStr("id"));
            if(name){
                dealWay.set("modify_date", new Date());
                dealWay.set("modify_user", userName);
                if(dealWay.update()){
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
            DealWay o = new DealWay();
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
        List<DealWay> list=new ArrayList<DealWay>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }





}

