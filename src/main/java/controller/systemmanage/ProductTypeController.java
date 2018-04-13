package controller.systemmanage;

import business.ProductTypeBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.ProductType;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/13.
 */
@ControllerMapping(value = "/productType")
public class ProductTypeController extends BaseController {
    private static final Logger logger = Logger.getLogger(ProductTypeController.class);
    /*注入business*/

    @Autowired
    ProductTypeBusiness business;

    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<ProductType> pageList = null;
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
        ProductType productType=null;
        Map<String,String[]>map=this.getParaMap();
        productType=(ProductType)parameter2Model(map,new ProductType(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");

        try {
            Boolean name=business.checkNameSave(productType.getStr("type_name"));

            if(name){
//                String userName=productType.getStr("user_name");
//                String user=this.getPara("user_name");
//                productType.set("create_user", user);
                productType.set("create_date", new Date());
                productType.set("create_user", userName);
                productType.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(productType.save()){
                    this.setAttr("result", "success");
                }else {
                    this.setAttr("result", "false");
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
        ProductType productType=null;
        Map<String,String[]>map=this.getParaMap();
        productType=(ProductType)parameter2Model(map,new ProductType(),null);
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        try{
            Boolean name=business.checkNameUpdate(productType.getStr("type_name"), productType.getStr("id"));
            if(name){
                productType.set("modify_date", new Date());
                productType.set("modify_user", userName);
                if(productType.update()){
                    setAttr("result", "success");
                }else {
                    setAttr("result", "false");
                }
            }else {
                this.setAttr("result", "checkNameError");
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
            ProductType o = new ProductType();
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
        List<ProductType> list=new ArrayList<ProductType>();
        try{
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }






}
