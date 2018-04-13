package business;

import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.tool.ConstantField;
import com.senyoboss.tool.StringUtils;
import model.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.ext.TreeModel;

import com.jfinal.kit.PathKit;
import net.sf.ehcache.config.PinningConfiguration;
import net.sf.ehcache.store.Store;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * @author suhouxiong
 * @version V1.0
 * @ClassName: DataRecordBusiness.java
 * @Description: DataRecordBusiness 业务处理层
 * @ProjectName: basecode
 * @Date 16/6/22
 */
public class TabRecordBusiness {

    public List<VTabCol> getTabColByTabCode(String tabCode) {

        return VTabCol.dao.find("select * from v_tab_col where del_flg = 1 and tab_code = '" + tabCode + "'");
    }

    public List<TabRecord> getTabRecord(String ids) {
        return TabRecord.dao.find("select * from tab_record where del_flg = 1 and record_id in (" + ids + ")");
    }

    public List<DataTab> getAllTab() {

        return DataTab.dao.find("select * from data_tab where del_flg = 1");
    }

    public List<TreeModel> getUserTabs(String projectId, String userName) {

        List<VUserTab> list = VUserTab.dao.find("select * from v_user_tab where project_id = '"
                + projectId + "' and user_name = '" + userName + "'");

        List<TreeModel> treeList = new ArrayList<TreeModel>();
        for (int i = 0; i < list.size(); i++) {
            TreeModel model = new TreeModel();
            model.setId(list.get(i).getInt("id"));
            model.setMenu(list.get(i).getInt("id") + "");
            model.setRoot(list.get(i).getInt("id") + "");
            model.setParent(list.get(i).getInt("id") + "");
            model.setTitle(list.get(i).getStr("tab_name"));
            model.setTabCode(list.get(i).getStr("tab_code"));
            treeList.add(model);
        }
        return treeList;
    }


    public List<VUserTabCol> getUserTabCols(String tabCode, String projectId, String userName, Boolean flg) {

        String sql = "";
        if (flg) {
            sql = "select distinct filed,filed_name,tab_code,col_code from v_user_tab_col where tab_code = '" + tabCode + "' and project_id = '" + projectId + "' and user_name = '" + userName + "'";
        }
        else {
            sql = "select distinct filed,filed_name,tab_code,col_code from v_user_tab_col where tab_code = '" + tabCode + "' and project_id = '" + projectId + "' and user_name = '" + userName + "'";
        }

        return VUserTabCol.dao.find(sql);
    }

    public List<TabRecordIds> getRecordIdList(String tabCode, String projectId, String userName, String strIds,
                                              HashMap<Integer,HashMap<String,HashMap<Integer, Boolean>>> pMap) {

        String sql = "";
        sql = "select * from tab_record_ids where del_flg = 1 and tab_code = '"
                + tabCode + "' and project_id = '" + projectId + "' and record_id in (" + strIds + ")";
//        if (pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5)) {
//            sql = "select * from tab_record_ids where del_flg = 1 and tab_code = '"
//                    + tabCode + "' and project_id = '" + projectId + "' and record_id in (" + strIds + ")";
//        }
//        else {
//            sql = "select * from tab_record_ids where del_flg = 1 and tab_code = '"
//                    + tabCode + "' and project_id = '" + projectId + "' and user_name = '" + userName + "' and record_id in (" + strIds + ")";
//        }
        return TabRecordIds.dao.find(sql);
    }

    public List<TabRecordIds> getRecordIdList2(String tabCode, String projectId, String strIds) {

        String sql = "";
        sql = "select * from tab_record_ids where del_flg = 1 and tab_code = '"
                + tabCode + "' and project_id = '" + projectId + "' and record_id in (" + strIds + ")";
        return TabRecordIds.dao.find(sql);
    }

    public String getRecordIds(String tabCode, String projectId, String userName,
                                HashMap<String, Object> conditionMap,
                                HashMap<Integer,HashMap<String,HashMap<Integer, Boolean>>> pMap,
                                String tabType) {

        StringBuilder selSb = new StringBuilder();
        StringBuilder conSb = new StringBuilder();

        //selSb.append("select DISTINCT tr.record_id from tab_record tr,tab_record_ids tri");
        if (pMap != null) {
            if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "tab".equals(tabType)) {
                selSb.append("select DISTINCT tr.record_id from tab_record tr,tab_record_ids tri");
            }else if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "examine".equals(tabType)) {
                selSb.append("select DISTINCT tr.record_id from tab_record tr,tab_record_ids tri");
            }else{
                selSb.append("select DISTINCT tr.record_id from tab_record tr");
            }
        }else{
            selSb.append("select DISTINCT tr.record_id from tab_record tr");
        }
        conSb.append(" where tr.del_flg = 1 and " +
                "tr.record_id in (select record_id from tab_record_ids where project_id='" + projectId
                + "' and tab_code='" + tabCode
                + "' and del_flg=1) ");
//        if (!pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5)) {
//            conSb.append(" where tr.del_flg = 1 and " +
//                    "tr.record_id in (select record_id from tab_record_ids where project_id='" + projectId
//                    + "' and tab_code='" + tabCode
//                    + "' and user_name='" + userName + "' and del_flg=1) ");
//        }
//        else {
//            conSb.append(" where tr.del_flg = 1 and " +
//                    "tr.record_id in (select record_id from tab_record_ids where project_id='" + projectId
//                    + "' and tab_code='" + tabCode
//                    + "' and del_flg=1) ");
//        }

        if (!"".equals(conditionMap.get("col_name")) && conditionMap.get("col_name") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_name' " +
                    "and col_val like '%" + conditionMap.get("col_name") + "%' and del_flg=1) tColName");
            conSb.append(" and tr.record_id = tColName.record_id");
        }
        if (!"".equals(conditionMap.get("col_tel")) && conditionMap.get("col_tel") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_tel' " +
                    "and col_val like '%" + conditionMap.get("col_tel") +"%' and del_flg=1) tColTel");
            conSb.append(" and tr.record_id = tColTel.record_id");
        }
        if (!"".equals(conditionMap.get("col_source")) && conditionMap.get("col_source") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_source' " +
                    "and col_val like '%" +conditionMap.get("col_source") +"%' and del_flg=1) tColSource");
            conSb.append(" and tr.record_id = tColSource.record_id");
        }
        if (!"".equals(conditionMap.get("col_channel")) && conditionMap.get("col_channel") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_channel' " +
                    "and col_val = '" + conditionMap.get("col_channel") +"' and del_flg=1) tColChannel");
            conSb.append(" and tr.record_id = tColChannel.record_id");
        }
        if (!"".equals(conditionMap.get("col_store")) && conditionMap.get("col_store") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_store' " +
                    "and col_val = '" + conditionMap.get("col_store") +"' and del_flg=1) tColStore");
            conSb.append(" and tr.record_id = tColStore.record_id");
        }
        if (!"".equals(conditionMap.get("col_vcode")) && conditionMap.get("col_vcode") != null) {
            selSb.append(",(select * from tab_record where col_code = 'col_vcode' " +
                    "and col_val like '%" + conditionMap.get("col_vcode") + "%' and del_flg=1) tColVCode");
            conSb.append(" and tr.record_id = tColVCode.record_id");
        }

        if (pMap != null) {
            if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "tab".equals(tabType)) {
                conSb.append(" and tri.record_id = tr.record_id and tri.is_submit = '" + ConstantField.IS_SUBMIT_NO + "'");
            }
            if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "examine".equals(tabType)) {
                conSb.append(" and tri.record_id = tr.record_id and tri.is_submit = '" + ConstantField.IS_SUBMIT_YES + "'");
            }
        }


        String strIds = "''";
        List<TabRecordIds> idList = TabRecordIds.dao.find(selSb.toString() + conSb.toString());
//        List<TabRecordIds> idList = TabRecordIds.dao.find("select record_id from tab_record_ids where del_flg = 1 and tab_code = '"
//                + tabCode + "' and project_id = '" + projectId + "' and user_name = '" + userName + "'");

        if (idList.size() > 0) {
            String tmp = "";
            for (int i = 0; i < idList.size(); i++) {
                tmp += ",'" + idList.get(i).getInt("record_id") + "'";
            }
            strIds = tmp.substring(1);
        }
        return strIds;
    }

    public String getRecordIds2(String tabCode, String projectId, String tabType,
                                HashMap<Integer,HashMap<String,HashMap<Integer, Boolean>>> pMap) {

        StringBuilder selSb = new StringBuilder();
        StringBuilder conSb = new StringBuilder();

        selSb.append("select DISTINCT tr.record_id from tab_record tr,tab_record_ids tri");
        conSb.append(" where tr.del_flg = 1 and " +
                "tr.record_id in (select record_id from tab_record_ids where project_id='" + projectId
                + "' and tab_code='" + tabCode
                + "' and del_flg=1) ");

        if (pMap != null) {
            if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "tab".equals(tabType)) {
                conSb.append(" and tri.record_id = tr.record_id and tri.is_submit = '" + ConstantField.IS_SUBMIT_NO + "'");
            }
            if (pMap.get(Integer.valueOf(projectId)) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode) != null
                    && pMap.get(Integer.valueOf(projectId)).get(tabCode).get(5) && "examine".equals(tabType)) {
                conSb.append(" and tri.record_id = tr.record_id and tri.is_submit = '" + ConstantField.IS_SUBMIT_YES + "'");
            }
        }


        String strIds = "''";
        List<TabRecordIds> idList = TabRecordIds.dao.find(selSb.toString() + conSb.toString());

        if (idList.size() > 0) {
            String tmp = "";
            for (int i = 0; i < idList.size(); i++) {
                tmp += ",'" + idList.get(i).getInt("record_id") + "'";
            }
            strIds = tmp.substring(1);
        }
        return strIds;
    }

    public List<ChannelInfo> getChannelList() {
        List<ChannelInfo> channelInfoList = ChannelInfo.dao.find("select id,channel_name from channel_info where del_flg = 1");
        return channelInfoList;
    }

    public List<StoreInfo> getStoreByChannelId(String channelId) {
        channelId = channelId == null ? "0" : channelId;
        List<StoreInfo> storeList = StoreInfo.dao.find("select * from store_info where del_flg = 1 and channel_id = '" + channelId + "'");
        return storeList;
    }

    public List<StoreInfo> getStoreList() {
        List<StoreInfo> stores = StoreInfo.dao.find("select id,store_name from store_info where del_flg = 1");
        return stores;
    }

    public List<TabCol> getTabColByCode(String tabCode) {

        return TabCol.dao.find("select * from tab_col where del_flg = 1 and tab_code = '" + tabCode + "'");
    }

    public List<TabRecord> getRecordById(String id) {
        return TabRecord.dao.find("select * from tab_record where del_flg = 1 and record_id = '" + id + "'");
    }

    public HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> getTabPermission(String userName) {

        String sql = "select * from tab_permission where user_name = '" + userName + "'";
        List<TabPermission> list = TabPermission.dao.find(sql);
        HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>> permissionMap = new HashMap<Integer, HashMap<String,HashMap<Integer, Boolean>>>();
        for (int i = 0; i < list.size(); i++) {

            TabPermission tp = list.get(i);
            if (!permissionMap.containsKey(tp.getInt("project_id"))) {
                HashMap<String,HashMap<Integer, Boolean>> tabMap = new HashMap<String,HashMap<Integer, Boolean>>();

                HashMap<Integer, Boolean> item = new HashMap<Integer, Boolean>();
                item.put(1, false);
                item.put(2, false);
                item.put(3, false);
                item.put(4, false);
                item.put(5, false);
                item.put(6, false);
                item.put(7, false);
                Math.floor(3.4);
                //tabMap.put(tp.getStr("tab_code"), new HashMap<Integer, Boolean>());
                //HashMap<Integer, Boolean> item = tabMap.get(tp.getStr("tab_code"));
                if (tp.getInt("permission_code") == 1) {
                    item.put(1, true);
                }
                if (tp.getInt("permission_code") == 2) {
                    item.put(2, true);
                }
                if (tp.getInt("permission_code") == 3) {
                    item.put(3, true);
                }
                if (tp.getInt("permission_code") == 4) {
                    item.put(4, true);
                }
                if (tp.getInt("permission_code") == 5) {
                    item.put(5, true);
                }
                if (tp.getInt("permission_code") == 6) {
                    item.put(6, true);
                }
                if (tp.getInt("permission_code") == 7) {
                    item.put(7, true);
                }
                tabMap.put(tp.getStr("tab_code"), item);
                permissionMap.put(tp.getInt("project_id"), tabMap);
            }
            else {
                HashMap<String,HashMap<Integer, Boolean>> tabMap = permissionMap.get(tp.getInt("project_id"));
                if (!tabMap.containsKey(tp.getStr("tab_code"))) {
                    HashMap<Integer, Boolean> item = new HashMap<Integer, Boolean>();
                    item.put(1, false);
                    item.put(2, false);
                    item.put(3, false);
                    item.put(4, false);
                    item.put(5, false);
                    item.put(6, false);
                    item.put(7, false);
                    if (tp.getInt("permission_code") == 1) {
                        item.put(1, true);
                    }
                    if (tp.getInt("permission_code") == 2) {
                        item.put(2, true);
                    }
                    if (tp.getInt("permission_code") == 3) {
                        item.put(3, true);
                    }
                    if (tp.getInt("permission_code") == 4) {
                        item.put(4, true);
                    }
                    if (tp.getInt("permission_code") == 5) {
                        item.put(5, true);
                    }
                    if (tp.getInt("permission_code") == 6) {
                        item.put(6, true);
                    }
                    if (tp.getInt("permission_code") == 7) {
                        item.put(7, true);
                    }
                    tabMap.put(tp.getStr("tab_code"), item);
                    permissionMap.put(tp.getInt("project_id"), tabMap);
                }
                else {
                    HashMap<Integer, Boolean> item = tabMap.get(tp.getStr("tab_code"));
                    if (tp.getInt("permission_code") == 1) {
                        item.put(1, true);
                    }
                    if (tp.getInt("permission_code") == 2) {
                        item.put(2, true);
                    }
                    if (tp.getInt("permission_code") == 3) {
                        item.put(3, true);
                    }
                    if (tp.getInt("permission_code") == 4) {
                        item.put(4, true);
                    }
                    if (tp.getInt("permission_code") == 5) {
                        item.put(5, true);
                    }
                    if (tp.getInt("permission_code") == 6) {
                        item.put(6, true);
                    }
                    if (tp.getInt("permission_code") == 7) {
                        item.put(7, true);
                    }
                }
            }
        }
        return permissionMap;
    }

    public Page<VUserPro> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        SearchCondition sc = new SearchCondition("v_user_pro");
        sc.setPage(page);
        sc.setPageSize(pageSize);
        Page<VUserPro> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = VUserPro.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*",sql + " " + searchSQL+" order by id desc");
        }
        return pageList;
    }

    public String export(String projectName, String tabName, List<VUserTabCol> colList, List<HashMap<String, Object>> dataList){

        HashMap<Integer, String> cMap = this.getChannelMap();
        HashMap<Integer, String> sMap = this.getStoreMap();
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(tabName);
        //不显示网格线
        sheet.setDisplayGridlines(false);
        sheet.setDefaultColumnWidth(20);


        HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小

        HSSFFont font2 = workbook.createFont();
        font2.setFontName("仿宋_GB2312");
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font2.setFontHeightInPoints((short) 12);

        style.setFont(font);//选择需要用到的字体格式


        HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
        cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        cellTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        cellTitleStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);// 设置背景色
        cellTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle cellDataStyle = workbook.createCellStyle();
        cellDataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellDataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellDataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellDataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFCellStyle indexDataStyle = workbook.createCellStyle(); // 样式对象
        indexDataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        indexDataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        indexDataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        indexDataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        indexDataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        indexDataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        HSSFRow headRow = sheet.createRow(1);
        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new Region(1, (short) (1), 1, (short) (colList.size()+1)));
        HSSFCell headCell = headRow.createCell((short) (1));
        headCell.setCellValue(projectName + "-" + tabName); // 跨单元格显示的数据
        headCell.setCellStyle(style); // 样式，居中
        // 产生表格标题行
        HSSFRow row = sheet.createRow(2);
        HSSFCell indexCell = row.createCell(1);
        indexCell.setCellStyle(cellTitleStyle);
        HSSFRichTextString indexText = new HSSFRichTextString("序号");
        indexCell.setCellValue(indexText);
        for (int i = 0; i < colList.size(); i++) {
            HSSFCell cell = row.createCell(i+2);
            String colName = colList.get(i).getStr("filed_name");
            HSSFRichTextString text = new HSSFRichTextString(colName);
            cell.setCellStyle(cellTitleStyle);
            cell.setCellValue(text);
        }

        for (int i = 0; i < dataList.size(); i++) {
            HSSFRow valueRow = sheet.createRow(i + 3);
            HashMap<String, Object> data = dataList.get(i);
            for (int j = 0; j < colList.size(); j++) {
                HSSFCell cell = valueRow.createCell(1);
                cell.setCellStyle(indexDataStyle);
                cell.setCellValue(String.valueOf(i+1));
                String val= "";
                String key = colList.get(j).getStr("col_code");
                if ("col_channel".equals(key)) {
                    if (!"".equals(data.get(key))) {
                        val = cMap.get(Integer.valueOf(String.valueOf(data.get(key))));
                    }

                }
                else if ("col_store".equals(key)) {
                    if (!"".equals(data.get(key))) {
                        val = sMap.get(Integer.valueOf(String.valueOf(data.get(key))));
                    }
                }
                else {
                    val = String.valueOf(data.get(key));
                }
                HSSFCell cell2 = valueRow.createCell(j+2);
                cell2.setCellStyle(cellDataStyle);
                cell2.setCellValue(val);
            }
        }

        try {
            // 创建文件输出流，准备输出电子表格
            OutputStream out = new FileOutputStream(PathKit.getWebRootPath()+"/tab.xls");
            workbook.write(out);
            out.close();
            return "tab.xls";
        } catch (IOException e) {
            return null;
        }
    }

    public HashMap<Integer, String> getChannelMap() {
        List<ChannelInfo> channelList = ChannelInfo.dao.findAll();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < channelList.size(); i++) {
            map.put(channelList.get(i).getInt("id"), channelList.get(i).getStr("channel_name"));
        }
        return map;
    }
    public HashMap<Integer, String> getStoreMap() {
        List<StoreInfo> storeList = StoreInfo.dao.findAll();
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < storeList.size(); i++) {
            map.put(storeList.get(i).getInt("id"), storeList.get(i).getStr("store_name"));
        }
        return map;
    }

}