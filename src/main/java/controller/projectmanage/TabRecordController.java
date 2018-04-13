package controller.projectmanage;

import business.SysLogBusiness;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.senyoboss.ioc.Autowired;
import com.senyoboss.tool.ConstantField;
import model.*;
import business.TabRecordBusiness;
import com.senyoboss.controller.BaseController;
import com.senyoboss.ext.annotation.ControllerMapping;
import model.ext.TreeModel;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * @author suhouxiong
 * @version V1.0
 * @ClassName: TabRecordController.java
 * @Description: TabRecordController Action处理层
 * @ProjectName: basecode
 * @Date 16/6/22
 */
@ControllerMapping(value = "/tabrecord", path = "/tabrecord")
public class TabRecordController extends BaseController {

    TabRecordBusiness business = new TabRecordBusiness();
    @Autowired
    SysLogBusiness logBusiness;

    public void index() {
        System.out.println("===index");
    }

//    public void getTab() {
//        List<DataTab> tabList = this.business.getAllTab();
//        this.renderJson(tabList);
//    }
//
//    public void getTabCol() {
//        String tabCode = this.getPara();
//        try {
//            List<VTabCol> tabColList = this.business.getTabColByTabCode(tabCode);
//            this.setAttr("result", tabColList);
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            this.setAttr("result", "error");
//        }
//    }

    public void getTabRecord() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("===========start============="+sdf.format(new Date()));
        String tabCode = this.getPara("tabCode");
        //String tabCode = "tab_1";
        String projectId = this.getPara("projectId");
        //String projectId = "1";
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }
        String curTabType = this.getPara("tabType");
        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> pMap = this.business.getTabPermission(userName);

        // 获取每页显示条数，采用服务器端过滤时必须接受参数
        Integer pageSize = this.getParaToInt("pageSize");
        // 获取当前页数，采用服务器端过滤时必须接受参数
        Integer page = this.getParaToInt("page");

        HashMap<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("col_name", this.getPara("col_name"));
        conditionMap.put("col_tel", this.getPara("col_tel"));
        conditionMap.put("col_source", this.getPara("col_source"));
        conditionMap.put("col_channel", this.getPara("col_channel"));
        conditionMap.put("col_store", this.getPara("col_store"));
        conditionMap.put("col_vcode", this.getPara("col_vcode"));

        String strIds = this.business.getRecordIds(tabCode, projectId, userName, conditionMap, pMap, curTabType);
        System.out.println("===========1=============" + sdf.format(new Date()));
        List<TabRecord> tmpList = this.business.getTabRecord(strIds);
        System.out.println("===========2=============" + sdf.format(new Date()));
        List<TabRecordIds> idList = this.business.getRecordIdList(tabCode, projectId, userName, strIds, pMap);
        System.out.println("===========3=============" + sdf.format(new Date()));
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < idList.size(); i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            int id = idList.get(i).getInt("record_id");
            int isSubmit = idList.get(i).getInt("is_submit");
            int isExamine = idList.get(i).getInt("is_examine");
            String checker = idList.get(i).getStr("checker");
            item.put("rowindex", i+1);
            item.put("id", id);
            item.put("is_submit", isSubmit);
            item.put("is_examine", isExamine);
            item.put("checker", checker);

            for (int j = 0; j < tmpList.size(); j++) {
                TabRecord r = tmpList.get(j);
                if (id == r.getInt("record_id")) {
                    item.put(r.getStr("col_code"), r.getStr("col_val"));
                    item.put("tabCode", tabCode);
                    item.put("projectId", projectId);
                }
            }
            mapList.add(item);
        }
        System.out.println("===========4=============" + sdf.format(new Date()));
        //Integer totalRow = mapList.size();
        //Double totalPage = Math.floor(totalRow / pageSize);
        //Integer starIndex = page * (page - 1);
        //Integer len = totalRow - page * pageSize < 0 ? page * pageSize - totalRow : pageSize;
        //List<HashMap<String, Object>> pageList = mapList.subList(starIndex, len);
        HashMap<String, Object> res = new HashMap<String, Object>();
        //res.put("totalItems", totalRow);
        res.put("list", mapList);
        this.setAttr("result", res);
        System.out.println("===========end=============" + sdf.format(new Date()));
    }

    public void getUpdateColList() {

        String tabCode = this.getPara("tabCode");
        //String tabCode = "tab_1";
        String projectId = this.getPara("projectId");
        //String projectId = "1";
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }

        String curTabType = this.getPara("tabType");
        HashMap<String, Object> conditionMap = new HashMap<String, Object>();
        conditionMap.put("col_name", this.getPara("col_name"));
        conditionMap.put("col_tel", this.getPara("col_tel"));
        conditionMap.put("col_source", this.getPara("col_source"));
        conditionMap.put("col_channel", this.getPara("col_channel"));
        conditionMap.put("col_store", this.getPara("col_store"));
        conditionMap.put("col_vcode", this.getPara("col_vcode"));
        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> pMap = this.business.getTabPermission(userName);

        String strIds = this.business.getRecordIds(tabCode, projectId, userName, conditionMap, pMap, curTabType);
        List<TabRecord> tmpList = this.business.getTabRecord(strIds);
        List<TabRecordIds> idList = this.business.getRecordIdList(tabCode, projectId, userName, strIds, pMap);
        //List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        HashMap<Integer,HashMap<String, HashMap<String, Object>>> mapList = new HashMap<Integer,HashMap<String, HashMap<String, Object>>>();
        for (int i = 0; i < idList.size(); i++) {

            Integer id = idList.get(i).getInt("record_id");

            HashMap<String, HashMap<String, Object>> item = new HashMap<String, HashMap<String, Object>>();
            for (int j = 0; j < tmpList.size(); j++) {
                TabRecord r = tmpList.get(j);
                if (id.equals(r.getInt("record_id"))) {
                    HashMap<String, Object> h = new HashMap<String, Object>();
                    h.put("isUpdated", r.getInt("is_updated"));
                    h.put("isLocked", r.getInt("is_locked"));
                    item.put(r.getStr("col_code"), h);
                    //h.put(r.getStr("col_code"), r.getInt("is_updated"));
                }
            }
            mapList.put(id, item);
        }
        this.setAttr("result", mapList);
    }

    public void getUserTabs() {

        String projectId = this.getPara("projectId");
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }
        //String projectId = "1";
        //String userName = "admin";
        List<TreeModel> list = this.business.getUserTabs(projectId, userName);
        this.setAttr("result", list);
    }

    public void getUserTabCols() {

        String tabCode = this.getPara("tabCode");
        String projectId = this.getPara("projectId");
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }



//        String tabCode = "tab_1";
//        String projectId = "1";
//        String userName = "admin";
        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> map = this.business.getTabPermission(userName);
        Boolean flg = map.get(Integer.valueOf(projectId)).get(tabCode).get(5);
        List<VUserTabCol> list = this.business.getUserTabCols(tabCode, projectId, userName, flg);

        this.setAttr("result", list);
    }

    public void getChannelList() {

        List<ChannelInfo> list = this.business.getChannelList();
        this.setAttr("result", list);
    }

    public void getStoreByChannelId() {

        String channelId = this.getPara("channelId");
        List<StoreInfo> list = this.business.getStoreByChannelId(channelId);
        this.setAttr("result", list);
    }

    public void getStoreList() {

        List<StoreInfo> list = this.business.getStoreList();
        this.setAttr("result", list);
    }

    @Before(Tx.class)
    public void saveData() throws Exception {
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }
        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> pMap = this.business.getTabPermission(userName);
        String tabCode = this.getPara("tabCode");
        String projectId = this.getPara("projectId");
        String dataStatus = this.getPara("dataStatus");
        String curTabType = this.getPara("tabType");
        String id = this.getPara("id");
        List<TabCol> colList = this.business.getTabColByCode(tabCode);
        ProjectBaseManagement pro = ProjectBaseManagement.dao.findById(projectId);
        String projectName = pro.getStr("project_name");

        String tabSql = "select tab_name from data_tab where tab_code = '" + tabCode + "'";
        DataTab dt = DataTab.dao.findFirst(tabSql);
        String tabName = dt.getStr("tab_name");
        String opContent = projectName + "-" + tabName;
        int op = ConstantField.LOG_ADD;
        if (!"".equals(id)) {
            op = ConstantField.LOG_EDIT;
        }
        if ("1".equals(dataStatus)) {
            op = ConstantField.LOG_SUBMIT;
        }
        if ("3".equals(dataStatus)) {
            op = ConstantField.LOG_EXAMINE;
        }
        if("".equals(id)) {
            TabRecordIds r = this.getModel(TabRecordIds.class);
            r.set("tab_code", tabCode);
            r.set("project_id", projectId);
            r.set("is_submit", "0".equals(dataStatus) ? ConstantField.IS_SUBMIT_NO : ConstantField.IS_SUBMIT_YES);
            r.set("is_examine", ConstantField.IS_EXAMINE_NO);
            r.set("user_name", userName);
            r.set("del_flg", ConstantField.IS_DEL_FLG_ON);
            if (r.save()) {
                logBusiness.SaveLog("项目数据录入", op,opContent,userName);
                r.set("record_id", r.getInt("id"));
                r.update();

                for (int i = 0; i < colList.size(); i++) {
                    String paraName = colList.get(i).getStr("col_code");
                    TabRecord o = this.getModel(TabRecord.class);
                    o.set("record_id", r.getInt("id"));
                    o.set("tab_code", tabCode);
                    o.set("col_code", paraName);
                    o.set("is_updated", "0".equals(dataStatus) ? ConstantField.IS_UPDATED_NO : ConstantField.IS_UPDATED_YES);
                    o.set("is_locked", ConstantField.IS_LOCKED_NO);
                    o.set("del_flg", ConstantField.IS_DEL_FLG_ON);


                    if (this.getPara(paraName) == null) {
                        o.set("col_val", "");
                    }
                    else {
                        o.set("col_val", this.getPara(paraName));
                    }
                    if (o.save()) {
                        this.setAttr("result", "success");
                    }
                    else {
                        this.setAttr("result", "error");
                    }
                }
            }
        }
        else {
            List<TabRecord> list = this.business.getRecordById(id);
            TabRecordIds r = TabRecordIds.dao.findById(id);
            for (int i = 0; i < list.size(); i++) {
                TabRecord item = list.get(i);
                String colName = item.getStr("col_code");
                if (this.getPara(colName) == null || "".equals(this.getPara(colName))) {
                    continue;
                }
                else {
                    if (!pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && item.getInt("is_locked") == 1) {
                        continue;
                    }
                    item.set("col_val", this.getPara(colName));
                    if ("1".equals(dataStatus) && !"".equals(this.getPara(colName))) {
                        item.set("is_updated", ConstantField.IS_UPDATED_YES);
                    }

                    if ("3".equals(dataStatus) && (this.getPara(colName) != null && !"".equals(this.getPara(colName)))) {
                        item.set("is_locked", ConstantField.IS_LOCKED_YES);
                    }
                    if(!item.update()) {
                        this.setAttr("result", "error");
                        return;
                    }
                }
            }
            if ("1".equals(dataStatus)) {
                r.set("is_submit", ConstantField.IS_SUBMIT_YES);
                r.set("is_examine", ConstantField.IS_EXAMINE_NO);
            }
            if ("3".equals(dataStatus)) {
                r.set("is_submit", ConstantField.IS_SUBMIT_NO);
                r.set("is_examine", ConstantField.IS_EXAMINE_YES);
                r.set("checker", userName);
            }
            r.update();
            logBusiness.SaveLog("项目数据录入", op, opContent, userName);
            this.setAttr("result", "success");
        }

    }

    @Before(Tx.class)
    public void delRecord() {
        int id = this.getParaToInt("id");
        TabRecordIds r = this.getModel(TabRecordIds.class);
        r.set("id", id);
        r.set("del_flg", ConstantField.IS_DEL_FLG_OFF);
        if (r.update()) {
            if (Db.update("update tab_record set del_flg = '2' where record_id = '" + id + "'")>0) {
                this.setAttr("result", "success");
            }
            else {
                this.setAttr("result", "error");
            }
        }
    }

    public void getTabPermission() {

        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }

        this.setAttr("result", this.business.getTabPermission(userName));
    }

    public void findAll()  {
        Page<VUserPro> pageList = null;
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }
        try {
            Map<String, String[]> map = this.getRequest().getParameterMap();
            String searchSQL = createSearchSQL(map);
            // 获取每页显示条数，采用服务器端过滤时必须接受参数
            Integer pageSize = this.getParaToInt("pageSize");
            // 获取当前页数，采用服务器端过滤时必须接受参数
            Integer page = this.getParaToInt("page");
            String whereSql = " and del_flg = '1' and user_name='" + userName + "' " + searchSQL;
            pageList = business.findByPaginate(pageSize, page, whereSql);
            this.setAttr("result", pageList);
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
            this.setAttr("result", "error");
        }
    }

    public void export() throws Exception {
        String tabCode = this.getPara("tabCode");
        String projectId = this.getPara("projectId");
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }

        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> pMap = this.business.getTabPermission(userName);
        Boolean flg = pMap.get(Integer.valueOf(projectId)).get(tabCode).get(6);
        List<VUserTabCol> colList = this.business.getUserTabCols(tabCode, projectId, userName, flg);

        String curTabType = this.getPara("tabType");

        String strIds = this.business.getRecordIds2(tabCode, projectId, curTabType, pMap);
        List<TabRecord> tmpList = this.business.getTabRecord(strIds);
        List<TabRecordIds> idList = this.business.getRecordIdList2(tabCode, projectId, strIds);
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < idList.size(); i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            int id = idList.get(i).getInt("record_id");
            item.put("rowindex", i+1);
            item.put("id", id);

            for (int j = 0; j < tmpList.size(); j++) {
                TabRecord r = tmpList.get(j);
                if (id == r.getInt("record_id")) {
                    item.put(r.getStr("col_code"), r.getStr("col_val"));
                    item.put("tabCode", tabCode);
                    item.put("projectId", projectId);
                }
            }
            mapList.add(item);
        }

        ProjectBaseManagement pro = ProjectBaseManagement.dao.findById(projectId);
        String projectName = pro.getStr("project_name");

        String tabSql = "select tab_name from data_tab where tab_code = '" + tabCode + "'";
        DataTab dt = DataTab.dao.findFirst(tabSql);
        String tabName = dt.getStr("tab_name");


        String fileName = this.business.export(projectName, tabName, colList, mapList);
        this.logBusiness.SaveLog("项目数据录入", ConstantField.LOG_EXPORT, projectName + "-" + tabName, userName);
        this.setAttr("result", fileName);
    }
    public void getUserTabCnt() {
        SysUser user= this.getSessionAttr("userSession");
        String userName = "";
        if (user != null) {
            userName = user.getStr("user_name");
        }
        String sql = "select DISTINCT ut.project_id,tcnt.cnt " +
                "from user_tab ut," +
                "(select project_id,COUNT(*) cnt from user_tab,data_tab where data_tab.tab_code=user_tab.tab_code and data_tab.del_flg=1 and user_tab.user_name='" + userName +"' GROUP BY project_id) tcnt " +
                "where ut.project_id = tcnt.project_id";
        List<UserTab> list = UserTab.dao.find(sql);
        HashMap<Integer, Long> map = new HashMap<Integer, Long>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getInt("project_id"), list.get(i).getLong("cnt"));
        }
        this.setAttr("result", map);
    }
}
