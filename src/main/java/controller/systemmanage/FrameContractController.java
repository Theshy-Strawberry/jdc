package controller.systemmanage;

import business.FrameContractBusiness;
import business.SysLogBusiness;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.FrameContract;
import model.SysUser;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Tinkpad on 2016/6/25.
 */
@ControllerMapping(value = "/frameContract")
public class FrameContractController extends BaseController {
    private static final Logger logger = Logger.getLogger(FrameContractController.class);

    /*注入FrameContractBusiness*/
    @Autowired
    FrameContractBusiness business;
    @Autowired
    SysLogBusiness logBusiness;
    /**
     * 查找表格的全部方法并进行相应的分页
     */
    public void findAll(){
        Page<FrameContract> pageList = null;
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
        //渠道框架合同管理类对象为空
        FrameContract frameContract=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        frameContract= (FrameContract) parameter2Model(map,new FrameContract(),null);
        //从用户名类中获取userSession
        SysUser user= this.getSessionAttr("userSession");
        //再从userSession中获取user_name
        String userName=user.getStr("user_name");

        try{
            //合同编号的验证方法
            Boolean name=business.checkNameSave(frameContract.getStr("contract_number"));
            //如果通过合同编号验证
            if(name){
                //从前台中获取有效期开始时间
                String expiryBegin=frameContract.get("expiry_begin");
                //从前台中获取有效期结束时间
                String expiryEnd=frameContract.get("expiry_end");
                SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
                //把有效期开始时间由字符串转换成日期类型
                Date dateBegin=s.parse(expiryBegin);
                //把有效期结束时间由字符串转换成日期类型
                Date dateEnd=s.parse(expiryEnd);
                //如果开始时间小于等于有效期结束时间
                if (dateBegin.getTime() <dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
                    //设置创建人为用户名
                    frameContract.set("create_user", userName);
                    //设置创建时间为当前时间
                    frameContract.set("create_date",new Date());
                    //设置删除状态位为1
                    frameContract.set("del_flg", ConstantField.IS_SFSCH_YES);
                    //如果保存成功
                    if (frameContract.save()){
                        String contractNumber = frameContract.getStr("contract_number");
                        logBusiness.SaveLog("渠道框架合同管理",ConstantField.LOG_ADD,contractNumber,userName);
                        //返回success
                        this.setAttr("result","success");
                    }else {
                        //返回false
                        this.setAttr("result","false");
                    }
                }else {
                    //否则返回date没有通过日期验证
                    this.setAttr("result", "date");
                }
            }else {
                //否则没有通过合同编号验证
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
        //渠道框架合同管理类对象为空
        FrameContract frameContract=null;
        Map<String,String[]>map=this.getParaMap();
        //创建新的对象
        frameContract= (FrameContract) parameter2Model(map,new FrameContract(),null);
        //从用户名类中获取userSession
        SysUser user= this.getSessionAttr("userSession");
        //再从userSession中获取user_name
        String userName=user.getStr("user_name");

        try{
            //合同编号验证
            Boolean name=business.checkNameUpdate(frameContract.getStr("contract_number"), frameContract.getStr("id"));
            //如果通过验证
            if(name){
                //从前台中获取有效期开始时间
                String expiryBegin=frameContract.get("expiry_begin");
                //从前台中获取有效期结束时间
                String expiryEnd=frameContract.get("expiry_end");
                SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd");
                //把有效期开始时间由字符串转换成日期类型
                Date dateBegin=s.parse(expiryBegin);
                //把有效期结束时间由字符串转换成日期类型
                Date dateEnd=s.parse(expiryEnd);
                //如果开始时间小于等于有效期结束时间
                if (dateBegin.getTime() <dateEnd.getTime()||dateBegin.getTime()==dateEnd.getTime()) {
                    //设置创建人为用户名
                    frameContract.set("create_user",userName);
                    //设置创建时间为当前时间
                    frameContract.set("create_date", new Date());
                    //如果更新成功
                    if (frameContract.update()){
                        String contractNumber = frameContract.getStr("contract_number");
                        logBusiness.SaveLog("渠道框架合同管理",ConstantField.LOG_EDIT,contractNumber,userName);
                        //返回success
                        setAttr("result", "success");
                    }else {
                        //返回false
                        setAttr("result", "false");
                    }
                }else {
                    //返回返回date
                    this.setAttr("result", "date");
                }
            }else {
                //否则没有合同编号验证
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
            FrameContract frameContract = new FrameContract();
            //设置id
            frameContract.set("id", id);
            //设置删除标志位为0，为不可见状态
            frameContract.set("del_flg", ConstantField.IS_SFSCH_NO);
            //如果更新成功
            if(frameContract.update()){
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
     * 查询所有甲方主体信息
     */
    public void getMainPartAll(){
        List<FrameContract> list=new ArrayList<FrameContract>();
        try{
            //调用FrameContractBusiness里的getMainPartAll方法
            list=business.getMainPartAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有渠道信息
     */
    public void getChannelAll(){
        List<FrameContract> list=new ArrayList<FrameContract>();
        try{
            //调用FrameContractBusiness里的getChannelAll方法
            list=business.getChannelAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询所有门店信息
     */
    public void getStoreAll(){
        List<FrameContract> list=new ArrayList<FrameContract>();
        try{
            //调用FrameContractBusiness里的getStoreAll方法
            list=business.getStoreAll();
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }

    /**
     * 查询渠道下所有门店信息
     */
    public void getAllList(){
        //从前台获取id
        Integer id=this.getParaToInt("id");
        List<FrameContract>list=new ArrayList<FrameContract>();
        try{
            //调用FrameContractBusiness
            //里的getAllList方法将id传到getAllList方法
            list=business.getAllList(id);
        }catch (Exception e){
            logger.info(e.getMessage());
            this.setAttr("result","error");
        }
        this.setAttr("result",list);
    }





}
