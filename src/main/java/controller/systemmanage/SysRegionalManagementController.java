package controller.systemmanage;

import business.SysLogBusiness;
import business.SysRegionalManagementBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.SysRegionalManagement;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/17.
 */
@ControllerMapping(value = "/sysRegionalManagement")
public class SysRegionalManagementController extends BaseController {
    public static  final Logger logger = Logger.getLogger(SysRegionalManagementController.class);

    @Autowired
    SysRegionalManagementBusiness business;
    @Autowired
    SysLogBusiness logBusiness;
    public void findAll(){
        Page<SysRegionalManagement> pageList = null;
        SysRegionalManagement sysRegionalManagement = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();

            String searchSQL = createSearchSQL(map);
            //获取每页显示的条数，采用服务器过滤时必须接受的参数
            Integer pageSize = this.getParaToInt("pageSize");
            //获取当前页数，采用服务器过滤时必须接受的参数
            Integer page = this.getParaToInt("page");
            //获取上级的id
            Integer regLevel = this.getParaToInt("regLevel");
            //获取上下查询标识符
            String selectType = this.getPara("selectType");


            String whereSql = searchSQL;

            //设置一个当前列表的code值


            //判断当前点击的是返回还是详情
            if (selectType != null && selectType.equals("up")) {
                //当选择返回时候
                //通过查找id查找到上级地区信息
                sysRegionalManagement = sysRegionalManagement.dao.findById(regLevel);
                //获取上级地区信息中的父类id用来拼接sql

                regLevel = sysRegionalManagement.getInt("reg_level");

                String supperName = "";
                //判断是否为最顶层数据
                if (regLevel != -1) {
                    //如果不是获取要显示当前列表的上级对象
                    sysRegionalManagement = sysRegionalManagement.dao.findById(regLevel);
                    //获取上级对象code
                    String supperCode = null;
                    supperCode = sysRegionalManagement.get("code");

                    supperName = sysRegionalManagement.get("name");
                    this.setAttr("supperCode", supperCode);


                }
                this.setAttr("supperName", supperName);
            }
            whereSql = whereSql + " and reg_level = '" + regLevel + "'";
            pageList = business.findByPaginate(pageSize, page, whereSql);

            this.setAttr("regLevel", regLevel);
            this.setAttr("result", pageList);







        }catch (UnsupportedEncodingException e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }

    }
    public void save(){
        SysRegionalManagement sysRegionalManagement = null;

        Map<String, String[]> map = this.getRequest().getParameterMap();
        SysUser user= this.getSessionAttr("userSession");
        String userName=user.getStr("user_name");
        sysRegionalManagement = (SysRegionalManagement)parameter2Model(map, new SysRegionalManagement(), null);
        sysRegionalManagement.set("is_enabled", ConstantField.IS_SFSCH_YES);
        sysRegionalManagement.set("del_flg",ConstantField.IS_SFSCH_YES);
        sysRegionalManagement.set("modify_date",this.getDate());
        sysRegionalManagement.set("create_date",this.getDate());



       // String locationNumber=sysRegionalManagement.get("location_number");

        String regLevelStr = sysRegionalManagement.get("reg_level");
        Integer regLevel = Integer.parseInt(regLevelStr);

        String whereSql = " where REG_LEVEL = "+ regLevel +" order by code desc";
        //获取最大code
        String maxCode = findMaxCode(whereSql);
        //新建code
        String code = createCode(maxCode,regLevel);
        sysRegionalManagement.set("code",code);
        if(sysRegionalManagement.save()){
            String name = sysRegionalManagement.getStr("name");
            try {
                logBusiness.SaveLog("地域管理",ConstantField.LOG_ADD,name,userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.setAttr("result", "success");
        }else{
            this.setAttr("result", "false");
        }

//        if(checkLocationNumber(sysRegionalManagement)){
//            if(checkName(sysRegionalManagement)){
//                //是否需要校验
//                if(checkOrderNumber(sysRegionalManagement)){
//                    if(sysRegionalManagement.save()){
//
//                        this.setAttr("result", "success");
//                    }else{
//                        this.setAttr("result", "false");
//                    }
//                }else {
//                    this.setAttr("result", "checkOrderNumberError");
//                }
//            }else {
//                this.setAttr("result", "checkNameError");
//            }
//
//        }else {
//            this.setAttr("result", "checkLocationNumberError");
//        }



    }
    /**
     * 更新地域信息
     */
    public  void update(){
        SysRegionalManagement sysRegionalManagement = null;

        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            SysUser user= this.getSessionAttr("userSession");
            String userName=user.getStr("user_name");
            sysRegionalManagement = (SysRegionalManagement)parameter2Model(map, new SysRegionalManagement(), null);
            sysRegionalManagement.set("modify_date",this.getDate());
            String id = sysRegionalManagement.get("id");

            /*SysRegionalManagement findModel = sysRegionalManagement.findById(id);
            Date createDate = findModel.getDate("create_date");
            sysRegionalManagement.set("create_date","create_date");*/
            sysRegionalManagement.set("id",Integer.valueOf(id));
            if(sysRegionalManagement.update()){
                String name = sysRegionalManagement.getStr("name");
                logBusiness.SaveLog("地域管理",ConstantField.LOG_EDIT,name,userName);
                setAttr("result","success");
            }else {
                setAttr("result", "false");
            }
//            if(checkName(sysRegionalManagement)){
//                if (checkOrderNumber(sysRegionalManagement)) {
//                    sysRegionalManagement.remove("create_date");
//                    if(sysRegionalManagement.update()){
//                        setAttr("result","success");
//                    }else {
//                        setAttr("result", "false");
//                    }
//                } else {
//                    this.setAttr("result", "checkOrderNumberError");
//                }
//            }else {
//                this.setAttr("result", "checkNameError");
//            }


        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result","error");
        }

    }

    /**
     * 获取时间戳
     * @return
     */
    public Timestamp getDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return java.sql.Timestamp.valueOf(format.format(date));


    }


    /**
     * 查询最大code
     * @param whereSql
     * @return
     */
    public String findMaxCode(String whereSql) {
        SysRegionalManagement sysRegionalManagement = null;
        sysRegionalManagement = business.findMaxCode(whereSql);
        if (sysRegionalManagement != null) {
            return sysRegionalManagement.get("code");
        } else {
            return "0";
        }
    }

    /**
     * 创建code
     */
    public  String createCode(String maxCode,Integer regLevel){
        //将最大code转换成int类型
        Integer maxCodeInt = Integer.parseInt(maxCode);
        //创建返回的code
        String code = null;
        //判断父亲的id是否为-1如果是则为最初级的列表
        if (regLevel == -1) {
            //如果maxCode为0代表列表没有数据插入数据为当前第条数据
            if (maxCodeInt == 0) {
                code = "100";
            } else {
                //否则code为最大加1
                maxCodeInt++;
                code = maxCodeInt.toString();
            }
        } else {
            //如果不是最初级列表
            //判断当前列表是已有数据如果有取最大code加1
            //如果当前列表没有数据通过父亲的id查找到父亲的code进行code拼接

            if (maxCodeInt != 0) {
                //否则code为最大加1
                maxCodeInt++;
                code = maxCodeInt.toString();
            } else {
                SysRegionalManagement sysRegionalManagement = null;
                sysRegionalManagement = sysRegionalManagement.dao.findById(regLevel);
                String supperCode = sysRegionalManagement.get("code");
                //通过父亲的code的长度来决定生成code的级别
                switch (supperCode.length()) {
                    case 3:
                        code = supperCode + "001";
                        break;
                    case 6:
                        code = supperCode + "001";
                        break;
                    case 9:
                        code = supperCode + "0001";
                        break;
                }
            }

        }
        return code;
    }

    /**
     * 修改数据启用停用
     * 获取前台数据
     * 获取启用停用标识
     *
     */
    public void editEnabled(){
        SysRegionalManagement sysRegionalManagement = null;

        try {
            Map<String,String[]> map = this.getParaMap();

            sysRegionalManagement = (SysRegionalManagement)parameter2Model(map, new SysRegionalManagement(),null);
            //设置修改日期
            sysRegionalManagement.set("modify_date",this.getDate());
            //获取传入的code值
            String code = sysRegionalManagement.get("code");
            //获取当前code的所有子数据和本身数据
            List<SysRegionalManagement> sysRegionalManagementList = business.findLikeCode(code);
            //修改数据为启用或停用
            for (SysRegionalManagement listChild:sysRegionalManagementList){
                listChild.set("is_enabled",sysRegionalManagement.get("is_enabled"));
                listChild.update();
            }

            if(sysRegionalManagement.update()){
                setAttr("result","success");
            }else {
                setAttr("result", "false");
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result", "error");
        }

    }


    public boolean checkOrderNumber(SysRegionalManagement sysRegionalManagement){
        boolean returnValue = false;
        try {
            Integer id = sysRegionalManagement.getInt("id");
            String regLevel = sysRegionalManagement.get("reg_level");
            String orderNumber = sysRegionalManagement.get("order_number");



            returnValue = business.checkOrderNumber(id, Integer.valueOf(regLevel), Integer.valueOf(orderNumber));


        } catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        return returnValue;
    }

    public boolean checkName(SysRegionalManagement sysRegionalManagement){
        boolean returnValue = false;
        try {
            String regLevel = sysRegionalManagement.get("reg_level");
            String name = sysRegionalManagement.get("name");
            Integer id = sysRegionalManagement.getInt("id");



            returnValue = business.checkName(id, name, Integer.valueOf(regLevel));



        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        return returnValue;
    }

    public boolean checkLocationNumber(SysRegionalManagement sysRegionalManagement){
        boolean returnValue = false;
        try {
            String regLevel = sysRegionalManagement.get("reg_level");
            String locationNumber = sysRegionalManagement.get("location_number");
            Integer id = sysRegionalManagement.getInt("id");



            returnValue = business.checkNameLocationNumber(id,locationNumber,Integer.valueOf(regLevel));



        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        return returnValue;
    }










}
