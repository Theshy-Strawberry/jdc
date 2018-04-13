package controller.systemmanage;

import business.StoreInfoBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.StoreInfo;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@ControllerMapping(value = "/storeInfo")
public class StoreInfoController extends BaseController {
    private static final Logger logger = Logger.getLogger(StoreInfoController.class);
    /*注入business*/

    @Autowired
    StoreInfoBusiness business;

    /**
     * 查找表格的全部方法
     */
    public void findAll(){
        Page<StoreInfo> pageList = null;
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
        StoreInfo storeInfo=null;
        Map<String,String[]>map=this.getParaMap();
        storeInfo=(StoreInfo)parameter2Model(map,new StoreInfo(),null);


        try {
            Boolean name=business.checkNameSave(storeInfo.getStr("channel_name"), storeInfo.getStr("channel_id"));

            if(name){
                String createDate=storeInfo.getStr("create_date");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(createDate);
                storeInfo.set("create_date", date);
                storeInfo.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(storeInfo.save()){
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
        StoreInfo storeInfo=null;
        Map<String,String[]>map=this.getParaMap();
        storeInfo=(StoreInfo)parameter2Model(map,new StoreInfo(),null);
        try{
            storeInfo.remove("create_date");
            storeInfo.remove("modify_date");
            Boolean name=business.checkNameUpdate(storeInfo.getStr("channel_name"), storeInfo.getStr("id"), storeInfo.getStr("channel_id"));
            if(name){
                if(storeInfo.update()){
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
            StoreInfo o = new StoreInfo();
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

    //查询所有省
    public void getChannelNameList(){
        List<StoreInfo> list=new ArrayList<StoreInfo>();
        try{
            list=business.getChannelNameList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result",list);
    }








}
