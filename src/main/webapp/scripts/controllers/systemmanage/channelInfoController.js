/**
 * Created by Tinkpad on 2016/6/15.
 */
/**
 * @ngdoc function 渠道管理
 * @name codeApp.controller:channelInfoCTRL
 * @description
 * # channelInfoCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('channelInfoCTRL',[
            '$scope',
            '$mdSidenav',
            '$timeout',
            '$mdDialog',
            'menu',
            '$location',
            '$rootScope',
            '$mdUtil',
            '$log',
            '$http',
            '$filter',
            'uiGridConstants',
            'baseService',
            '$q',
            '$mdToast',
            'i18nService',
            'selectData',
            function($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, $filter, uiGridConstants, baseService, $q, $mdToast, i18nService,selectData){

                //==================权限相关代码:开始================================
                $scope.userAuth = [];
                baseService.post("/index/getUser", {}).then(
                    function (data) {
                        baseService.post("/sysauth/getUserAuths", {
                            user_name : data.result.user_name,
                            url : $location.path()
                            //menu_id : window.rootMenu.id
                        }).then(
                            function (data) {
                                $scope.userAuth = data.result;
                            }
                        )
                    }
                );

                $scope.checkAuth = function (val,type) {
                    if(!angular.isUndefined($scope.userAuth)){
                        for(var i=0;i<$scope.userAuth.length;i++){
                            if(val == $scope.userAuth[i][7] &&type == $scope.userAuth[i][6]){
                                return true;
                            }
                        }
                        return false;
                    }
                };
                //==================权限相关代码:完成=============================
                //国际化
                i18nService.setCurrentLang('zh-cn');
                //访问后台的路径名
                baseService.actionName.name="channelInfo";
                $scope.selectData = selectData;
                //渠道页面的初始状态位为0
                $scope.open=0;
                //渠道页面的创建人创建时间状态位为0
                // $scope.openFlg=0;
                //渠道页面添加或编辑功能的触发事件
                $scope.toggleRight=function(){
                    //渠道页面的创建人创建时间状态位为1，显示创建人创建时间
                   // $scope.openFlg=1;
                    $scope.obj.object = {};
                    $scope.user_form1.$setPristine(true);
                    $scope.user_form1.$setUntouched(false);
                    //设置创建时间
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    //把真实姓名赋给创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    );
                    //打开渠道右侧页面
                    $scope.openRight();


                };
                //门店页面添加或编辑功能的触发事件
                $scope.toggleRight2=function(){
                    $scope.obj.object2 = {};
                    //把临时变量赋给渠道的id字段
                    $scope.obj.object2.channel_id=$scope.tep;
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    //打开门店右侧页面
                    $scope.openRight2();


                };
                //渠道的右侧打开事件
                $scope.openRight=baseService.buildRightToggler();

                //门店的右侧打开事件
                $scope.openRight2=baseService.buildRightToggler2();

                //关闭渠道右侧页面
                $scope.close = function () {

                    baseService.closeRightToggler();
                    $scope.obj.object = {};
                };
                //关闭门店右侧页面
                $scope.close2 = function () {
                    baseService.closeRightToggler2();
                    $scope.obj.object2 = {};

                };


                /*对象变量
                * 一个是渠道的，一个是门店的。
                 */
                $scope.obj={
                    searchObj:{},
                    object:{},
                    searchObj2:{},
                    object2:{}
                };

                //查询渠道
                $scope.search=function(){
                    //返回到对应的渠道页面
                    getPage();
                };
                //查询门店
                $scope.search2=function(){

                    //返回到对应的门店页面
                    getPage2($scope.tep);
                };


                //门店内查询所有渠道总监
                //baseService.post("/channelInfo/getChannelDirectorList", {
                //    channelId:$scope.tep
                //}).then(
                //    function(data){
                //        $scope.channelDirectors=data.result;
                //    }
                //);
                //门店内查询所有渠道专员
                //baseService.post("/channelInfo/getChannelCommissionerList", {}).then(
                //    function(data){
                //        $scope.channelCommissioners=data.result;
                //    }
                //);

                //门店内查询所有省
                baseService.post("/channelInfo/getProvinceIdList", {}).then(
                    function(data){
                        $scope.provinceIds=data.result;
                        baseService.gridOptions2.provinceIds=data.result;

                    }
                );

                //门店内查询所有信息
                baseService.post("/channelInfo/getProvinceIdAllList", {}).then(
                    function(data){
                        $scope.datas=data.result;
                        baseService.gridOptions2.datas=data.result;
                    }
                );

                //门店内查询省下的所有的市
                $scope.myCityIds = function() {
                    //$scope.obj.object2.city_id=0;
                    //$scope.obj.object2.county_id=0
                    $scope.cityIds = {};
                    $scope.countyIds = {};
                    baseService.post("/channelInfo/getAllList",{
                        id:$scope.obj.object2.province_id
                    }).then(
                        function(data){
                            $scope.cityIds=data.result;
                        }
                    )
                };

                //门店内查询市下的区县
                $scope.myCountyIds = function() {
                    //$scope.obj.object2.county_id=0
                    $scope.countyIds = {};
                    baseService.post("/channelInfo/getAllList", {
                        id:$scope.obj.object2.city_id
                    }).then(
                        function(data){
                            $scope.countyIds=data.result;
                        }
                    )
                };

                ////门店内查询省下的所有的市
                //$scope.$watch('obj.object2.province_id', function(scope){
                //    $scope.cityIds={};
                //    $scope.countyIds={};
                //    $scope.obj.object2.city_id=0;
                //    baseService.post("/channelInfo/getAllList",{
                //        id:scope
                //    }).then(
                //        function(data){
                //            $scope.cityIds=data.result;
                //            baseService.gridOptions2.cityIds=data.result;
                //        }
                //    )
                //});
                ////门店内查询市下的区县
                //$scope.$watch('obj.object2.city_id', function(scope){
                //    $scope.countyIds={};
                //    //$scope.obj.object2.city_id=0
                //    $scope.obj.object2.county_id=0
                //    baseService.post("/channelInfo/getAllList", {
                //        id:scope
                //    }).then(
                //        function(data){
                //            $scope.countyIds=data.result;
                //            baseService.gridOptions2.countyIds=data.result;
                //        }
                //    )
                //});

                //门店内查询所有信息
                baseService.post("/channelInfo/getUserAll", {}).then(
                    function(data){
                        baseService.gridOptions.userIds=data.result;
                    }
                );

                /*渠道内保存或更改*/
                $scope.save=function(){
                    //验证渠道名称不能为空
                    if(angular.isUndefined($scope.user_form1.channel_name.$modelValue)){
                        $scope.user_form1.channel_name.$touched = "true";
                        return;
                    }else{
                        //验证渠道联系人不能为空
                        if(angular.isUndefined($scope.user_form1.channel_contact.$modelValue)){
                            $scope.user_form1.channel_contact.$touched = "true";
                            return;
                        }else{
                            //验证渠道联系方式不能为空
                            if(angular.isUndefined($scope.user_form1.channel_contact_method.$modelValue)){
                                $scope.user_form1.channel_contact_method.$touched = "true";
                                return;
                            }
                        }
                    }
                    //保存和更新的方法
                    baseService.saveOrUpdate($scope.obj.object.id,window.formatObj($scope.obj.object)).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                            //如果返回的是checkNameError,代表没通过重名验证
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("渠道名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };

                /*门店内保存或更改*/
                $scope.save2=function(){
                    //验证店面名称不能为空
                    if(angular.isUndefined($scope.user_form.store_name.$modelValue)){
                        $scope.user_form.store_name.$touched = "true";
                        return;
                    }else{
                        if (!$scope.user_form.$valid) {
                            return;
                        }
                    }
                    //定义一个临时变量，把他赋给渠道id这个字段
                    $scope.obj.object2.channel_id =$scope.tep;
                    //保存和更新的方法
                    baseService.saveOrUpdate2($scope.obj.object2.id,window.formatObj($scope.obj.object2)).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                //关闭门店编辑页面
                                $scope.close2();
                                //返回到对应的门店页面
                                getPage2($scope.tep);
                                //如果返回的是checkNameError,代表没通过重名验证
                                baseService.post("/channelInfo/getChannelDirectorList", {
                                    channelId:$scope.tep
                                }).then(
                                    function(data){
                                        $scope.channelDirectors=data.result;
                                    }
                                );

                                //门店内查询所有渠道专员
                                baseService.post("/channelInfo/getChannelCommissionerList", {
                                    channelId:$scope.tep
                                }).then(
                                    function(data){
                                        $scope.channelCommissioners=data.result;
                                    }
                                );
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("门店名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };
                /*渠道的更新方法*/
                $scope.update=function(l){
                  //  $scope.openFlg=2;
                    //参数l传到对象里
                    $scope.obj.object = angular.copy(l);
                    //设置创建时间
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    //把真实姓名赋给创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    );
                    //打开渠道的编辑页面
                    $scope.openRight();
                };
                /*门店的更新方法*/
                $scope.update2=function(l){
                    //参数l传到对象里
                    $scope.obj.object2 = angular.copy(l);
                    //打开门店的编辑页面
                    $scope.openRight2();
                    //把临时变量赋给门店对象的渠道id
                    $scope.obj.object2.channel_id=$scope.tep;
                };

                /*渠道下列表*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'channel_name',
                        displayName: '渠道名称',
                        minWidth: 120,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'channel_contact', displayName: '渠道联系人', minWidth: 100,headerCellClass:'headerCss'},
                    {name: 'channel_contact_method', displayName: '渠道联系方式', minWidth: 100,headerCellClass:'headerCss'},
                    {name: 'create_user', displayName: '经办人', minWidth: 100,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'userFilter:grid.options.userIds'},
                    {name: 'create_date', displayName: '经办时间', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {
                        name: '操作', minWidth: 60,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/file.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.show(row.entity)"  aria-label=”门店”><md-tooltip md-autohide>门店</md-tooltip></md-icon>' +
                        '</div>'
                    }
                ];
                /*门店下列表*/
                baseService.gridOptions2.columnDefs = [
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'store_name',
                        displayName: '店面名称',
                        minWidth: 100,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'store_contact', displayName: '店面联系人', minWidth: 80,headerCellClass:'headerCss'},
                    {name: 'store_contact_method', displayName: '店面联系方式', minWidth: 80,headerCellClass:'headerCss'},
                    {name: 'channel_commissioner', displayName: '渠道专员', minWidth: 80,headerCellClass:'headerCss'},
                    {name: 'channel_specialist', displayName: '专员联系方式', minWidth: 80,headerCellClass:'headerCss'},
                    {name: 'channel_director', displayName: '渠道总监', minWidth: 80,headerCellClass:'headerCss'},
                    {name: 'province_id', displayName: '省', minWidth: 80,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'provinceIdFilter:grid.options.provinceIds'},
                    {name: 'city_id', displayName: '市', minWidth: 80,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'provinceIdFilter:grid.options.datas'},
                    {name: 'county_id', displayName: '区/县', minWidth: 80,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'provinceIdFilter:grid.options.datas'},
                    {
                        name: '操作',minWidth: 60,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update2(row.entity)"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm2(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                        '</div>'
                    }
                ];
                //渠道分页
                baseService.gridOptions.onRegisterApi = function (gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            baseService.paginOpt.sort = null;
                        } else {
                            baseService.paginOpt.sort = sortColumns[0].sort.direction;
                        }
                        getPage();
                    });
                    gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        baseService.paginOpt.pageNumber = newPage;
                        baseService.paginOpt.pageSize = pageSize;
                        getPage();
                    });
                };
                //门店分页
                baseService.gridOptions2.onRegisterApi = function (gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            baseService.paginOpt.sort = null;
                        } else {
                            baseService.paginOpt.sort = sortColumns[0].sort.direction;
                        }
                        getPage2();
                    });
                    gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        baseService.paginOpt.pageNumber = newPage;
                        baseService.paginOpt.pageSize = pageSize;
                        getPage2();
                    });
                };
                //渠道
                $scope.gridOptions = baseService.gridOptions;

                //门店
                $scope.gridOptions2 = baseService.gridOptions2;

                getPage();

                //删除渠道信息
                $scope.showConfirm = function (l) {
                    baseService.deleteById(l.id).then(
                        function (data) {
                            $scope.data = data.result;
                            if ($scope.data == "true") {
                                baseService.alert("删除成功！");
                                getPage();
                            }
                            else {
                                baseService.alert("error！");
                            }
                        }
                    )
                };
                //删除门店信息
                $scope.showConfirm2 = function (l) {
                    baseService.deleteById2(l.id).then(
                        function (data) {
                            $scope.data = data.result;
                            if ($scope.data == "true") {
                                baseService.alert("删除成功！");
                                //返回到对应的门店页面
                                getPage2($scope.tep);
                            }
                            else {
                                baseService.alert("error！");
                            }
                        }
                    )
                };
                //定义临时变量
                $scope.tep = {};
                //打开门店的方法
                $scope.show=function(l){
                    //打开状态位变为1
                    $scope.open=1;
                    //跳转到店面页
                    getPage2(l.id);
                    //把渠道页的id赋予临时变量
                    $scope.tep= l.id;

                    //门店内查询所有渠道总监
                    baseService.post("/channelInfo/getChannelDirectorList", {
                        channelId:$scope.tep
                    }).then(
                        function(data){
                            $scope.channelDirectors=data.result;
                        }
                    );

                    //门店内查询所有渠道专员
                    baseService.post("/channelInfo/getChannelCommissionerList", {
                        channelId:$scope.tep
                    }).then(
                        function(data){
                            $scope.channelCommissioners=data.result;
                        }
                    );



                };
                //定义返回的方法
                $scope.return=function(){
                    //返回到渠道页面，把初始状态位为0
                    $scope.open=0;
                };





                /*渠道分页*/
                function getPage() {
                    baseService.findAllWithParams($scope.obj.searchObj).then(
                        function (data) {
                            $scope.gridOptions.totalItems = data.result.totalRow;
                            $scope.gridOptions.data = data.result.list;
                        }
                    );
                }
                /*门店分页*/
                function getPage2(id) {
                    baseService.findAllWithParams2($scope.obj.searchObj2,id).then(
                        function (data) {
                            $scope.gridOptions2.totalItems = data.result.totalRow;//显示多少行
                            $scope.gridOptions2.data = data.result.list;//后台给前台绑定数据
                        }
                    );
                }

                $scope.export = function () {
                    baseService.post("/channelInfo/export", {}).then( function (data) {
                            window.open(window.ServerURL+"/"+data.result);
                        }
                    ) }

            }
        ])
    }
);