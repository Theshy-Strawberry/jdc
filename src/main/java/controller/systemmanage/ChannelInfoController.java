package controller.systemmanage;

import business.ChannelInfoBusiness;
import business.SysLogBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.*;

import java.io.UnsupportedEncodingException;

import java.util.*;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@ControllerMapping(value = "/channelInfo")
public class ChannelInfoController extends BaseController {
    private static final Logger logger = Logger.getLogger(ChannelInfoController.class);

    /*注入ChannelInfoBusiness*/
    @Autowired
    ChannelInfoBusiness business;
    @Autowired
    //调用日志的business
    SysLogBusiness logBusiness;

    /**
     * 查找全部的渠道方法
     */
    public void findAll(){
        Page<ChannelInfo> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            // 查找删除状态位为1的可见数据
            String whereSql = "and del_flg = '1'" + searchSQL;
            pageList=business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }


    }

    /**
     * 查找全部的门店方法
     */
    public void findAll2(){
        Page<StoreInfo> pageList = null;
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            //从前台获取id
            String id=this.getPara("id");
            String whereSql = "and del_flg = '1' and channel_id ='"+id+"'" + searchSQL;
            pageList=business.findByPaginate2(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }


    }

    /**
     * 保存渠道的方法
     */
    public void save(){
        //渠道类对象为空
        ChannelInfo channelInfo=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        channelInfo=(ChannelInfo)parameter2Model(map,new ChannelInfo(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过用户Session值获取用户名
        String userName=user.getStr("user_name");

        try {
            //渠道名称的验证方法
            Boolean name=business.checkNameSave(channelInfo.getStr("channel_name"));
            //如果通过名称验证
            if(name){
                //设置创建时间
                channelInfo.set("create_date", new Date());
                //设置创建人
                channelInfo.set("create_user", userName);
                //设置删除状态位
                channelInfo.set("del_flg", ConstantField.IS_SFSCH_YES);
                //如果保存成功
                if(channelInfo.save()){
                    String channelName = channelInfo.getStr("channel_name");
                    logBusiness.SaveLog("渠道管理",ConstantField.LOG_ADD,channelName,userName);
                    //返回success
                    this.setAttr("result","success");
                }else {
                    //返回false
                    this.setAttr("result","false");
                }

            }else {
                //否则没有通过渠道名称验证
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }
    /**
     * 保存门店的方法
     */
    public void save2(){
        //门店类对象为空
        StoreInfo storeInfo=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        storeInfo=(StoreInfo)parameter2Model(map,new StoreInfo(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过用户Session值获取用户名
        String userName=user.getStr("user_name");
        //获取渠道的id值
        String channelId=this.getPara("channel_id");
        try {
            //门店名称的验证方法
            Boolean name=business.checkNameSave2(storeInfo.getStr("store_name"), storeInfo.getStr("channel_id"));
           //如果通过验证
            if(name){
                //设置创建时间
                storeInfo.set("create_date", new Date());
                //设置创建人
                storeInfo.set("create_user", userName);
                //设置渠道的id
                storeInfo.set("channel_id", channelId);
                //设置删除状态位
                storeInfo.set("del_flg", ConstantField.IS_SFSCH_YES);
                //如果保存成功
                if(storeInfo.save()){
                    String storeName = storeInfo.getStr("store_name");
                    logBusiness.SaveLog("渠道管理",ConstantField.LOG_ADD,storeName,userName);
                    //返回success
                    this.setAttr("result", "success");
                }else {
                    //返回false
                    this.setAttr("result", "false");
                }

            }else {
                //否则门店名称没有通过验证
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }


    /**
     * 更新渠道的方法
     */
    public void update(){
        //渠道类对象为空
        ChannelInfo channelInfo=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        channelInfo=(ChannelInfo)parameter2Model(map,new ChannelInfo(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过Session值获取用户名
        String userName=user.getStr("user_name");
        try{
            //渠道名称验证
            Boolean name=business.checkNameUpdate(channelInfo.getStr("channel_name"), channelInfo.getStr("id"));
            //如果通过验证
            if(name){
                //设置创建时间
                channelInfo.set("create_date", new Date());
                //如设置创建人
                channelInfo.set("create_user", userName);
                //如果更新成功
                if(channelInfo.update()){
                    String channelName = channelInfo.getStr("channel_name");
                    logBusiness.SaveLog("渠道管理",ConstantField.LOG_EDIT,channelName,userName);
                    //返回success
                    setAttr("result", "success");
                }else {
                    //返回false
                    setAttr("result", "false");
                }
            }else {
                //否则没有通过渠道名称验证
                this.setAttr("result","checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result", "error");
        }


    }
    /**
     * 更新门店的方法
     */
    public void update2(){
        //门店类对象为空
        StoreInfo storeInfo=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        storeInfo=(StoreInfo)parameter2Model(map,new StoreInfo(),null);
        //获取用户的Session值
        SysUser user= this.getSessionAttr("userSession");
        //通过Session值获取用户名
        String userName=user.getStr("user_name");
        //获取渠道的id
        String channelId=this.getPara("channel_id");
        try{
            //门店名称的验证方法
            Boolean name=business.checkNameUpdate2(storeInfo.getStr("store_name"), storeInfo.getStr("channel_id"), storeInfo.getStr("id"));
            //如果通过验证
            if(name){
                //设置创建时间
                storeInfo.set("create_date", new Date());
                //设置创建人
                storeInfo.set("create_user", userName);
                //设置渠道的id
                storeInfo.set("channel_id",channelId);
                //如果保存成功
                if(storeInfo.update()){
                    String storeName = storeInfo.getStr("store_name");
                    logBusiness.SaveLog("渠道管理",ConstantField.LOG_EDIT,storeName,userName);
                    //返回success
                    setAttr("result", "success");
                }else {
                    //返回false
                    setAttr("result", "false");
                }
            }else {
                //否则门店名称没有通过验证
                this.setAttr("result", "checkNameError");
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            setAttr("result", "error");
        }


    }

    /**
     * 删除渠道的方法
     */
    public void delete(){
        try {
            //从前台获取id
            String id = this.getPara("id");
            //创建对象
            ChannelInfo o = new ChannelInfo();
            //设置id
            o.set("id", id);
            //设置删除标志位为0，为不可见状态
            o.set("del_flg", ConstantField.IS_SFSCH_NO);
            //如果更新成功
            if(o.update()){
                String sql = "select * from store_info where del_flg = 1 and channel_id='"+id+"'";
                List<StoreInfo> list = StoreInfo.dao.find(sql);
                if (list.size()>0){
                    StoreInfo info = new StoreInfo();
                    for(int i=0;i<list.size();i++){
                        info.set("id",list.get(i).getInt("id"));
                        info.set("del_flg", ConstantField.IS_SFSCH_NO);
                        info.update();
                    }
                }
                //设置删除标志位为0，为不可见状态

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
     * 删除门店的方法
     */
    public void delete2(){
        try {
            //从前台获取id
            String id = this.getPara("id");
            //创建对象
            StoreInfo o = new StoreInfo();
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
     *  查询所有用户的信息
     */
    public void getUserAll(){
        List<ChannelInfo> list=new ArrayList<ChannelInfo>();
        try{
            //调用ChannelInfoBusiness里的getUserAll方法
            list=business.getUserAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }


    /**
     * 查询所有省级元素的信息
     */
    public void getProvinceIdList(){
        List<StoreInfo> list=new ArrayList<StoreInfo>();
        try{
            //调用ChannelInfoBusiness里的getUserAll方法
            list=business.getProvinceIdList();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
        this.setAttr("result",list);
    }

    /**
     * 根据id查询省下所有市、区县
     */
    public void getAllList(){
        //从前台获取id
        Integer id=this.getParaToInt("id");
        List<StoreInfo>list=new ArrayList<StoreInfo>();
        try{
            //调用ChannelInfoBusiness里的getAllList方法将id传到getAllList方法
            list=business.getAllList(id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有的地域信息
     */
    public void getProvinceIdAllList(){
        List<StoreInfo>list=new ArrayList<StoreInfo>();
        try{
            //调用ChannelInfoBusiness里的getProvinceIdAll方法
            list=business.getProvinceIdAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有的渠道总监信息
     */
    public void getChannelDirectorList(){
        //从前台获取channelId
        Integer channelId=this.getParaToInt("channelId");
        List<StoreInfo>list=new ArrayList<StoreInfo>();
        try{
            //调用ChannelInfoBusiness里的getChannelDirectorList方法
            //将channelId传到getChannelDirectorList方法
            list=business.getChannelDirectorList(channelId);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有渠道专员信息
     */
    public void getChannelCommissionerList(){
        //从前台获取channelId
        Integer channelId=this.getParaToInt("channelId");
        List<StoreInfo>list=new ArrayList<StoreInfo>();
        try{
            //调用ChannelInfoBusiness里的getChannelCommissionerList
            //方法将channelId传到getChannelCommissionerList方法
            list=business.getChannelCommissionerList(channelId);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有门店信息
     */
    public void findByChannelId(){
        List<StoreInfo>list=new ArrayList<StoreInfo>();
        //从前台获取channelId
        String channelId=this.getPara("channelId");
        try{
            //调用ChannelInfoBusiness里的findByChannelId
            //方法将channelId传到findByChannelId方法
            list=business.findByChannelId(channelId);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    public void export() {
        String fileName = this.business.export();
        this.setAttr("result", fileName);
    }
}
