package controller.systemmanage;

import business.VisitWayBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.SysUser;
import model.VisitWay;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@ControllerMapping(value = "/visitWay")
public class VisitWayController extends BaseController{
    private static final Logger logger = Logger.getLogger(VisitWayController.class);
    /*注入business*/

    @Autowired
    VisitWayBusiness business;
    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<VisitWay> pageList = null;
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
        VisitWay visitWay=null;
        Map<String,String[]>map=this.getParaMap();
        visitWay=(VisitWay)parameter2Model(map,new VisitWay(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");

        try {
            Boolean name=business.checkNameSave(visitWay.getStr("way_name"));

            if(name){
//                String createDate=visitWay.getStr("create_date");
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = format.parse(createDate);
//                visitWay.set("create_date", date);
                visitWay.set("del_flg", ConstantField.IS_SFSCH_YES);
                visitWay.set("create_user", userName);
                visitWay.set("create_date",new Date());
                if(visitWay.save()){
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
        VisitWay visitWay=null;
        Map<String,String[]>map=this.getParaMap();
        visitWay=(VisitWay)parameter2Model(map,new VisitWay(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        try{

            Boolean name=business.checkNameUpdate(visitWay.getStr("way_name"), visitWay.getStr("id"));
            if(name){
                visitWay.set("modify_date", new Date());
                visitWay.set("modify_user", userName);
                if(visitWay.update()){
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
            VisitWay o = new VisitWay();
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
        List<VisitWay> list=new ArrayList<VisitWay>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

}
