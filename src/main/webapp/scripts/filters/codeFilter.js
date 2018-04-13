/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (filters) {
    'use strict';

    filters
        .filter('unitCode', function () {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                return value == 1 ? "机关部门" : "所属单位";
            };
        })
        .filter('timeFilter', function ($filter) {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                var dateFilter = $filter('date');
                return dateFilter(value, 'HH:mm:ss');
            };
        })
        .filter('subFilter', function ($sce) {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                if(value.length>20){
                    var arr = value.substring(0,20);
                    return $sce.trustAsHtml(arr+"......");
                }else{
                    return $sce.trustAsHtml(value);
                }
            };
        })
        .filter('dateFilter', function ($filter) {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                var dateFilter = $filter('date');
                return dateFilter(value, 'yyyy-MM-dd');
            };
        })
        .filter('monthFilter', function ($filter) {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                var dateFilter = $filter('date');
                return dateFilter(value, 'yyyy-MM');
            };
        })
        .filter('isListing', function () {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                return value == 1 ? "是" : "否";
            };
        })

        //渠道
        .filter('channelInfoFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                try{
                    var arr = value.split(",");
                    var str ="";
                    for(var j=0;j<arr.length;j++){
                        for(var i=0;i<items.length;i++){
                            if(arr[j] == items[i].id){
                                str +=items[i].channel_name +",";
                            }
                        }
                    }
                    return str.substr(0,str.length-1);
                }catch(e){
                    for(var i=0;i<items.length;i++){
                        if(value == items[i].id){
                            return items[i].channel_name;
                        }
                    }
                }
            };
        })
        // 门店
        .filter('storeInfoFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                try{
                    var arr = value.split(",");
                    var str ="";
                    for(var j=0;j<arr.length;j++){
                        for(var i=0;i<items.length;i++){
                            if(arr[j] == items[i].id){
                                str +=items[i].store_name +",";
                            }
                        }
                    }
                    return str.substr(0,str.length-1);
                }catch(e){
                    for(var i=0;i<items.length;i++){
                        if(value == items[i].id){
                            return items[i].store_name;
                        }
                    }
                }
            };
        })

        //人员信息
        .filter('userInfoFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].user_name){
                        return items[i].real_name;
                    }
                }
            };
        })
        //多人员信息
        .filter('userInfosFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                try{
                    var arr = value;
                    var str ="";
                    for(var j=0;j<arr.length;j++){
                        for(var i=0;i<items.length;i++){
                            if(arr[j] == items[i].user_name){
                                str +=items[i].real_name +",";
                            }
                        }
                    }
                    return str.substr(0,str.length-1);
                }catch(e){
                    for(var i=0;i<items.length;i++){
                        if(value == items[i].user_name){
                            return items[i].real_name;
                        }
                    }
                }
            };
        })

        //通过人员ID显示用户真实姓名
        .filter('userIdFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].real_user_name;
                    }
                }
            };
        })

        //合同名称
        .filter('contractNameFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].contract_name;
                    }
                }
            };
        })
        //权限类型
        .filter('authTypeFilter', function () {//
            return function (value){
                if(angular.isUndefined(value)){
                    return;
                }
                if (value == "NODE") {
                    return "菜单";
                } else if (value == "BUTTON") {
                    return "按钮";
                }else if (value == "TAB") {
                    return "TAB标签";
                }else if (value == "CARD") {
                    return "快捷菜单";
                }
            };
        })

        //公司类型分类
        .filter('companyTypeFilter', function () {//
            return function (value){
                if(angular.isUndefined(value)){
                    return;
                }
                if (value == "1") {
                    return "地产公司";
                }else if (value == "2") {
                    return "渠道公司";
                }
            };
        })


        .filter('trust', function ($sce) {
            return function (value) {
                if(angular.isUndefined(value)){
                    return;
                }
                return $sce.trustAsHtml(value);
            };
        })
        //通过渠道公司ID显示渠道公司名称
        .filter('channelIdFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].company_name;
                    }
                }
            };
        })
        //通过用户名显示真实姓名
        .filter('userFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].user_name){
                        return items[i].real_name;
                    }
                }
            };
        })

        //通过用户ID显示真实姓名
        .filter('userIdFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].real_name;
                    }
                }
            };
        })

        //通过id显示职位名称
        .filter('jobFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].title_name;
                    }
                }
            };
        })
        //通过医院ID显示省名称
        .filter('provinceIdFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].name;
                    }
                }
            };
        })

        //通过id显示公司名称
        .filter('companyFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].company_name;
                    }
                }
            };
        })
        //通过id显示项目名称
        .filter('projectFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].project_name;
                    }
                }
            };
        })
        //通过id显示甲方主体名称
        .filter('mainPartFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].company_name;
                    }
                }
            };
        })

        //通过id显示渠道名称
        .filter('channelFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].channel_name;
                    }
                }
            };
        })

        //通过id显示门店名称
        .filter('storeFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].id){
                        return items[i].store_name;
                    }
                }
            };
        })


        //开通状态
        .filter('stateFilter', function () {//
            return function (value){
                if(angular.isUndefined(value)){
                    return;
                }
                if (value == "1") {
                    return "开通";
                }else if (value == "2") {
                    return "暂停";
                }
            };
        })

        //日志类型
        .filter('logFilter', function () {//
            return function (value){
                if(angular.isUndefined(value)){
                    return;
                }
                if (value == "1") {
                    return "新增";
                }else if (value == "2") {
                    return "编辑";
                }else if (value == "3") {
                    return "删除";
                }else if (value == "4") {
                    return "导出";
                }else if (value == "5") {
                    return "提交";
                }else if (value == "6") {
                    return "审核";
                }
            };
        })

        //数据信息
        .filter('dataTabFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].tab_code){
                        return items[i].tab_name;
                    }
                }
            };
        })

        //数据类型
        .filter('dataTypeFilter', function () {
            return function (value,items){
                if(angular.isUndefined(value)){
                    return;
                }
                if(angular.isUndefined(items)){
                    return;
                }
                for(var i=0;i<items.length;i++){
                    if(value == items[i].col_code){
                        return items[i].col_name;
                    }
                }
            };
        })

});
