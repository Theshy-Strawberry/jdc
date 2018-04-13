package controller.systemmanage;

import business.DataColBusiness;
import business.SysLogBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.DataCol;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liu on 2016/6/23.
 */
@ControllerMapping(value = "/dataCol")
public class DataColController extends BaseController {
    private static final Logger logger = Logger.getLogger(DataCol.class);
    /*注入business*/

    @Autowired
    DataColBusiness business;

    @Autowired
    //调用日志的business
            SysLogBusiness logBusiness;

    //      查询方法+分页
    public void findAll()  {
        Page<DataCol> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql =  searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    //保存
    public void save() {
        SysUser user= this.getSessionAttr("userSession");
        DataCol dataCol = null;
        try {
            Map<String, String[]> map = this.getParaMap();
//            创建新的对象
            dataCol = (DataCol) parameter2Model(map, new DataCol(), null);
//            重名方法的验证
            Boolean name= business.checkNameSave(dataCol.getStr("col_name"));
//            判断 如果重名 把checkNameError 赋值给result
//            如果不重名继续执行
            String userName=user.getStr("user_name");
            if(name){
//                String sql = "select * from data_col order by id desc";
//                DataCol col = DataCol.dao.findFirst(sql);
//                if(col != null){
//                    Integer id = col.get("id");
//                    dataCol.set("col_code", "col_" + (id+1));
//                }else{
//                    dataCol.set("col_code", "col_1");
//                }
                String uuid = UUID.randomUUID().toString();
                dataCol.set("col_code", "col_" + uuid.substring(0,10));
                dataCol.set("del_flg", ConstantField.IS_SFSCH_YES);
                dataCol.set("create_date",new Date());
                dataCol.set("create_user", user.getStr("user_name"));
//              判断 如果保存成功 把success赋值给 result 反之把 false赋值给result
                if(dataCol.save()){
                    String colName = dataCol.getStr("col_name");
                    logBusiness.SaveLog("数据管理",ConstantField.LOG_ADD,colName,userName);
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }else {
                this.setAttr("result", "checkNameError");
            }
        }catch (Exception e) {
            logger.info(e.getMessage());
            //异常时 把error 赋值给 result
            this.setAttr("result", "error");
        }
    }

    //修改方法
    public void update() {
        SysUser user= this.getSessionAttr("userSession");
        DataCol dataCol = null;
        try {
//            从前台获取map
            Map<String, String[]> map = this.getParaMap();
            dataCol = (DataCol) parameter2Model(map, new DataCol(), null);
//            重名验证方法
            Boolean name= business.checkNameUpdate(dataCol.getStr("col_name"),dataCol.getStr("id"));
            String userName=user.getStr("user_name");
            if(name){
                dataCol.set("create_date", new Date());
                dataCol.set("create_user", user.getStr("user_name"));
                if(dataCol.update()){
                    String colName = dataCol.getStr("col_name");
                    logBusiness.SaveLog("数据管理",ConstantField.LOG_EDIT,colName,userName);
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }else{
                this.setAttr("result", "checkNameError");
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    //暂停 就是改标记
    public void open(){
        try {
            //取值
            String id = this.getPara("id");
            Integer flag = this.getParaToInt("del_flg");
            DataCol o = new DataCol();
            if(flag == 2) {
                o.set("id",id);
                o.set("del_flg", ConstantField.IS_SFSCH_YES);
                if(o.update()){
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }else if(flag == 1){
                o.set("id",id);
                o.set("del_flg", ConstantField.IS_SFSCH_NO);
                if(o.update()){
                    this.setAttr("result", "true");
                }else{
                    this.setAttr("result", "false");
                }
            }
        }catch(Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

}
