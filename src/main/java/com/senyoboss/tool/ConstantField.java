package com.senyoboss.tool;

public class ConstantField {

    //审计方式
    public static String AUDIT_TYPE_LOCALE = "1";//现场审计
    public static String AUDIT_TYPE_REMOTE = "2";//远程审计
    public static String AUDIT_TYPE_REMOTE_LOCALE = "3";//远程审计 现场审计

    //是否上市
    public static String IS_PUBLIC_YES = "1";//已上市
    public static String IS_PUBLIC_NO = "2";//未上市

    //分配状态
    public static String IS_DIST_NO = "1";//未分配
    public static String IS_DIST_YES = "2";//已分配

    //项目来源
    public static String PROJECT_SOURCE_PLANIN = "1";//计划中
    public static String PROJECT_SOURCE_CARRYOVER = "2";//年结转
    public static String PROJECT_SOURCE_PLANOUT = "3";//年结转

    //审批状态
    public static String APPROVE_STATUS_AGREE = "1";//同意
    public static String APPROVE_STATUS_DISAGREE = "2";//不同意

    //是否审核
    public static String VIEW_STATUS_YES = "1"; //已审核
    public static String VIEW_STATUS_NO = "2"; //未审核

    //工作职务
    public static String JOB_TITLE_STAFF = "1";//科员
    public static String JOB_TITLE_CHIEF = "2";//科长
    public static String JOB_TITLE_DEPUTY = "3";//副主任
    public static String JOB_TITLE_DIRECTOR = "4";//主任

    //是否含税
    public static String IS_TAX_YES = "1";//含税
    public static String IS_TAX_NO = "2";//未含税

    //是否可整改
    public static String IS_RECTIFY_YES = "1";//可以
    public static String IS_RECTIFY_NO = "2";//不可以

    //整改状态
    public static String RECTIFY_STATUS_END = "1";//已整改
    public static String RECTIFY_STATUS_ING = "2";//正在整改
    public static String RECTIFY_STATUS_UNABLE = "3";//无法整改
    public static String RECTIFY_STATUS_NOT = "4";//未整改

    //项目分类
    public static String PROJECT_KIND_NORMAL = "1";//常规
    public static String PROJECT_KIND_SPECIAL = "2";//专项

    //写实类别
    public static String WRITE_TYPE_WEEK = "1";//周报
    public static String WRITE_TYPE_MONTH = "2";//月报

    //用车类别
    public static String USE_CAR_IN = "1";//市内
    public static String USE_CAR_OUT = "2";//市外

    //项目进度
    public static String PROJECT_PROGRESS_UNSTART = "01";//尚未开展
    public static String PROJECT_PROGRESS_PREPARE = "02";//审前准备
    public static String PROJECT_PROGRESS_EXECUTE = "03";//审计实施
    public static String PROJECT_PROGRESS_WRITE = "04";//撰写报告
    public static String PROJECT_PROGRESS_HEAR = "05";//审理阶段
    public static String PROJECT_PROGRESS_UNIT = "06";//征求被审计单位意见
    public static String PROJECT_PROGRESS_LEAD = "07";//报公司领导阶段
    public static String PROJECT_PROGRESS_OTHERLEAD = "08";//报公司其他领导阶段
    public static String PROJECT_PROGRESS_DEPT = "09";//报其他相关部门阶段
    public static String PROJECT_PROGRESS_POSITION = "10";//下达意见书
    public static String PROJECT_PROGRESS_END = "11";//完结

    //合同类别
    public static String CONTRACT_TYPE_BILL = "1";//票据
    public static String CONTRACT_TYPE_CONT = "2";//合同
    public static String CONTRACT_TYPE_ARRIVAL = "3";//到货
    public static String CONTRACT_TYPE_BALANCE = "4";//结算

    //有无审减
    public static String IS_REDUCE_NO = "1";//无
    public static String IS_REDUCE_YES = "2";//有

    //必审点来源
    public static String ORIGIN_HEADQUARTERS = "1";//总公司
    public static String ORIGIN_AUDITDEPT = "2";//审计部

    //是否删除
    public static String IS_SFSCH_YES = "1";//正常
    public static String IS_SFSCH_NO = "2";//已删除

    //代办是否完结
    public static String IS_END_NO = "1";//正常
    public static String IS_END_YES = "2";//已删除

    //价格来源
    public static String PRICE_SOURCE_SYSTEM = "1";//价格系统导入
    public static String PRICE_SOURCE_ASK = "2";//询价

    //是否已审计
    public static String IS_AUDIT_NO = "1";//否
    public static String IS_AUDIT_YES = "2";//是

    //是否已报审
    public static String IS_SEND_VIEW_NO = "1";//否
    public static String IS_SEND_VIEW_YES = "2";//是

    public static String IS_DEL_FLG_ON = "1";//使用
    public static String IS_DEL_FLG_OFF = "2";//删除

    //津地产-数据状态
    public static int IS_SUBMIT_NO = 0;
    public static int IS_SUBMIT_YES = 1;
    public static int IS_EXAMINE_NO = 0;
    public static int IS_EXAMINE_YES = 1;
    public static int IS_UPDATED_NO = 0;
    public static int IS_UPDATED_YES = 1;
    public static int IS_LOCKED_NO = 0;
    public static int IS_LOCKED_YES = 1;
    // 津地产-日志操作类型
    public static  int LOG_ADD = 1;
    public static  int LOG_EDIT = 2;
    public static  int LOG_DEl = 3;
    public static  int LOG_EXPORT = 4;
    public static  int LOG_SUBMIT = 5;
    public static  int LOG_EXAMINE = 6;
}