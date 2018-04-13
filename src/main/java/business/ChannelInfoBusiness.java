package business;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Page;
import com.senyoboss.common.SearchCondition;
import com.senyoboss.ioc.Service;
import com.senyoboss.tool.StringUtils;
import model.ChannelInfo;
import model.StoreInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tinkpad on 2016/6/15.
 */
@Service
public class ChannelInfoBusiness {
    /**
     * 查找渠道的时候用的分页方法
     * @param pageSize
     * @param page
     * @param searchSQL
     * @return
     * @throws UnsupportedEncodingException
     */
    public Page<ChannelInfo> findByPaginate(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        //对应数据库的表名channel_info
        SearchCondition sc = new SearchCondition("channel_info");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<ChannelInfo> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = ChannelInfo.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum,"  + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    /**
     * 查找门店的时候用的分页方法
     * @param pageSize
     * @param page
     * @param searchSQL
     * @return
     * @throws UnsupportedEncodingException
     */
    public Page<StoreInfo> findByPaginate2(Integer pageSize,Integer page,String searchSQL) throws UnsupportedEncodingException {
        //对应数据库的表名store_info
        SearchCondition sc = new SearchCondition("store_info");
        sc.setPage(page);
        sc.setPageSize(pageSize);

        Page<StoreInfo> pageList = null;
        String sql = "";
        if (!StringUtils.isNullOrEmpty(sc.getSearchSql())) {
            sql = sc.getSearchSql();
        }
        else {
            sql = "from (SELECT @rownum:=0) r," + sc.getTableName();
        }
        if (sc.getPage() != null && sc.getPageSize() != null) {
            pageList = StoreInfo.dao.paginate(sc.getPage(), sc.getPageSize(), "select @rownum:=@rownum+1 AS rownum," + sc.getTableName() + ".*", sql + " " + searchSQL + " order by id desc");
        }
        return pageList;
    }

    /**
     * 校验保存后的渠道名称是否唯一
     * @param channel_name 要判断的渠道名称
     * @return Boolean false数据库中已存在相同的渠道名称，true数据库中不存在此渠道名称
     */
    public Boolean checkNameSave (String channel_name) throws Exception{
        String sql = "select id from channel_info where del_flg = 1 and channel_name ='"+ channel_name +"'";
        List<ChannelInfo> list = ChannelInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 校验编辑后的渠道名称是否唯一
     * @param channel_name 要判断的渠道名称是否唯一
     * @return Boolean false数据库中已存在相同的渠道名称，true数据库中不存在此渠道名称是否唯一
     */
    public Boolean checkNameUpdate (String channel_name,String id) throws Exception{
        String sql = "select * from channel_info where del_flg = 1 and id <> '"+id+"' and channel_name ='"+ channel_name +"'";
        List<ChannelInfo> list = ChannelInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }
    /**
     *  查询用户真实名且del_flg状态位为1为可见状态的数据
     */
    public List<ChannelInfo> getUserAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_user where del_flg = 1";
        List<ChannelInfo> list = ChannelInfo.dao.find(sql);
        return list;
    }

    /**
     * 校验保存后的门店名称是否唯一
     * @param store_name 要判断的门店名称
     * @return Boolean false数据库中已存在相同的门店名称，true数据库中不存在此门店名称
     */
    public Boolean checkNameSave2 (String store_name,String channel_id) throws Exception{
        String sql = "select id from store_info where del_flg = 1 and channel_id='"+channel_id+"' and store_name ='"+ store_name +"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }



    /**
     * 校验编辑后的门店名称是否唯一
     * @param store_name 要判断的门店名称是否唯一
     * @return Boolean false数据库中已存在相同的门店名称，true数据库中不存在门店名称是否唯一
     */
    public Boolean checkNameUpdate2 (String store_name,String channel_id,String id) throws Exception{
        String sql = "select * from store_info where del_flg = 1 and id <> '"+id+"' and channel_id='"+channel_id+"' and store_name ='"+ store_name +"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        if(list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 查找所有的省级元素信息
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getProvinceIdList()  throws UnsupportedEncodingException {
        String sql = "select * from sys_regional_management where reg_level = -1 and is_enabled = 1";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }



    /**
     * 根据id查询省下所有市、区县
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getAllList(int id)  throws UnsupportedEncodingException {
        String sql = "select * from sys_regional_management where is_enabled = 1 and reg_level = "+id;
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }



    /**
     *查询所有的地域信息
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getProvinceIdAll()  throws UnsupportedEncodingException {
        String sql = "select * from sys_regional_management";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }


    /**
     * 查询所有的渠道总监信息且del_flg状态位为1为可见状态的数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getChannelDirectorList(Integer channelId)  throws UnsupportedEncodingException {
        String sql = "select channel_director from store_info where del_flg = 1 and channel_id='"+channelId+"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }


    /**
     * 查询所有渠道专员信息且del_flg状态位为1为可见状态的数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getChannelCommissionerList(Integer channelId)  throws UnsupportedEncodingException {
        String sql = "select channel_commissioner from store_info where del_flg = 1 and channel_id='"+channelId+"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }

    /**
     * 查询所有门店信息且del_flg状态位为1为可见状态的数据
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> findByChannelId(String channelId)  throws UnsupportedEncodingException {
        String sql = "select @rownum:=@rownum+1 AS rownum,store_info.* from (SELECT @rownum:=0) r,store_info where channel_id ='"+channelId+"' and del_flg = 1";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }

    public String export(){

        //HashMap<Integer, String> cMap = this.getChannelMap();
        //HashMap<Integer, String> sMap = this.getStoreMap();
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("门店信息");
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

        String[] headers = { "序号", "渠道名称", "渠道联系人", "渠道联系方式", "店面名称","店面联系人","店面联系方式","渠道专员","专员联系方式","渠道总监" };

        HSSFRow headRow = sheet.createRow(1);
        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new Region(1, (short) (1), 1, (short) (headers.length)));
        HSSFCell headCell = headRow.createCell((short) (1));
        headCell.setCellValue("门店信息"); // 跨单元格显示的数据
        headCell.setCellStyle(style); // 样式，居中
        // 产生表格标题行
        HSSFRow row = sheet.createRow(2);

//        String sql = "select ci.channel_name,ci.channel_contact,ci.channel_contact_method,si.* " +
//                "from store_info si,channel_info ci where si.del_flg = 1 and si.channel_id = ci.id and ci.del_flg = 1";
//        String sql = "select * from store_info where del_flg = 1 group by channel_id";
//        List<StoreInfo> dataList = StoreInfo.dao.find(sql);
        String sql = "select * from channel_info where del_flg = 1 order by id desc";
        List<ChannelInfo> channelInfo = ChannelInfo.dao.find(sql);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i+1);
            String colName = headers[i];
            HSSFRichTextString text = new HSSFRichTextString(colName);
            cell.setCellStyle(cellTitleStyle);
            cell.setCellValue(text);
        }

        for (int i = 0; i < channelInfo.size(); i++) {
//            ChannelInfo channelInfo = ChannelInfo.dao.findById(dataList.get(i).getInt("channel_id"));
            String sSql = "select * from store_info where del_flg = 1 and channel_id = "+channelInfo.get(i).getInt("id");
            List<StoreInfo> dataList = StoreInfo.dao.find(sSql);
            if(dataList.size() > 0){
                for(int j = 0; j < dataList.size(); j++){
                    HSSFRow valueRow = sheet.createRow(i + 3);
                    HSSFCell cell0 = valueRow.createCell(1);
                    cell0.setCellStyle(indexDataStyle);
                    cell0.setCellValue(i+1);

                    HSSFCell cell1 = valueRow.createCell(2);
                    cell1.setCellStyle(cellDataStyle);
                    cell1.setCellValue(channelInfo.get(i).getStr("channel_name"));

                    HSSFCell cell2 = valueRow.createCell(3);
                    cell2.setCellStyle(cellDataStyle);
                    cell2.setCellValue(channelInfo.get(i).getStr("channel_contact"));

                    HSSFCell cell3 = valueRow.createCell(4);
                    cell3.setCellStyle(cellDataStyle);
                    cell3.setCellValue(channelInfo.get(i).getStr("channel_contact_method"));

                    HSSFCell cell4 = valueRow.createCell(5);
                    cell4.setCellStyle(cellDataStyle);
                    cell4.setCellValue(dataList.get(j).getStr("store_name"));

                    HSSFCell cell5 = valueRow.createCell(6);
                    cell5.setCellStyle(cellDataStyle);
                    cell5.setCellValue(dataList.get(j).getStr("store_contact"));

                    HSSFCell cell6 = valueRow.createCell(7);
                    cell6.setCellStyle(cellDataStyle);
                    cell6.setCellValue(dataList.get(j).getStr("store_contact_method"));

                    HSSFCell cell7 = valueRow.createCell(8);
                    cell7.setCellStyle(cellDataStyle);
                    cell7.setCellValue(dataList.get(j).getStr("channel_commissioner"));

                    HSSFCell cell8 = valueRow.createCell(9);
                    cell8.setCellStyle(cellDataStyle);
                    cell8.setCellValue(dataList.get(j).getStr("channel_specialist"));

                    HSSFCell cell9 = valueRow.createCell(10);
                    cell9.setCellStyle(cellDataStyle);
                    cell9.setCellValue(dataList.get(j).getStr("channel_director"));
                }
            }else{
                HSSFRow valueRow = sheet.createRow(i + 3);
                HSSFCell cell0 = valueRow.createCell(1);
                cell0.setCellStyle(indexDataStyle);
                cell0.setCellValue(i+1);

                HSSFCell cell1 = valueRow.createCell(2);
                cell1.setCellStyle(cellDataStyle);
                cell1.setCellValue(channelInfo.get(i).getStr("channel_name"));

                HSSFCell cell2 = valueRow.createCell(3);
                cell2.setCellStyle(cellDataStyle);
                cell2.setCellValue(channelInfo.get(i).getStr("channel_contact"));

                HSSFCell cell3 = valueRow.createCell(4);
                cell3.setCellStyle(cellDataStyle);
                cell3.setCellValue(channelInfo.get(i).getStr("channel_contact_method"));

                HSSFCell cell4 = valueRow.createCell(5);
                cell4.setCellStyle(cellDataStyle);
                cell4.setCellValue("");

                HSSFCell cell5 = valueRow.createCell(6);
                cell5.setCellStyle(cellDataStyle);
                cell5.setCellValue("");

                HSSFCell cell6 = valueRow.createCell(7);
                cell6.setCellStyle(cellDataStyle);
                cell6.setCellValue("");

                HSSFCell cell7 = valueRow.createCell(8);
                cell7.setCellStyle(cellDataStyle);
                cell7.setCellValue("");

                HSSFCell cell8 = valueRow.createCell(9);
                cell8.setCellStyle(cellDataStyle);
                cell8.setCellValue("");

                HSSFCell cell9 = valueRow.createCell(10);
                cell9.setCellStyle(cellDataStyle);
                cell9.setCellValue("");
            }

        }

        try {
            // 创建文件输出流，准备输出电子表格
            OutputStream out = new FileOutputStream(PathKit.getWebRootPath()+"/store.xls");
            workbook.write(out);
            out.close();
            return "store.xls";
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


    /**
     *查询所有
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<StoreInfo> getStoreAll(String id)  throws UnsupportedEncodingException {
        String sql = "select * from store_info where del_flg = 1 and channel_id='"+id+"'";
        List<StoreInfo> list = StoreInfo.dao.find(sql);
        return list;
    }





}
