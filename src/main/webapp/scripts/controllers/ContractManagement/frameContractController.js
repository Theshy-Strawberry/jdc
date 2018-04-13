/**
 * Created by Tinkpad on 2016/6/25.
 */
/**
 * @ngdoc function 渠道框架合同管理
 * @name codeApp.controller:frameContractCTRL
 * @description
 * # frameContractCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('frameContractCTRL',[
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
                //==================权限相关代码:完成================================
                //国际化
                i18nService.setCurrentLang('zh-cn');
                //访问后台的路径名
                baseService.actionName.name="frameContract";
                $scope.selectData = selectData;
                //添加的方法
                $scope.toggleRight=function(){
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    //打开右侧页面
                    $scope.openRight();

                };
                //定义打开右侧页面的方法
                $scope.openRight=baseService.buildRightToggler();

                //关闭右侧页面的方法
                $scope.close = function () {
                    baseService.closeRightToggler();
                    $scope.obj.object = {};


                };
                /*对象变量*/

                $scope.obj={
                    searchObj:{},
                    object:{}
                };
                //查询方法
                $scope.search=function(){
                    //如果有效期结束时间不为未定义或者不为空
                    if ($scope.obj.searchObj.END$expiry_end != 'undefined' && $scope.obj.searchObj.END$expiry_end != null){
                        //获取有效期结束时间
                        var endDate = $scope.obj.searchObj.END$expiry_end;
                        //获取有效期开始时间
                        var startDate = $scope.obj.searchObj.START$expiry_begin;
                        //如果有效期开始时间大于有效期结束时间
                        if (startDate >endDate){
                            //弹出
                            baseService.alert("结束时间必须大于等于开始时间！");
                            //返回
                            return;
                        }
                    }
                    //调分页查询的方法
                    getPage();
                };

                //查询所有甲方主体信息
                baseService.post("/frameContract/getMainPartAll", {}).then(
                    function(data){
                        $scope.mainPartNames=data.result;
                        baseService.gridOptions.mainPartNames=data.result;
                    }
                );
                //查询所有渠道信息
                baseService.post("/frameContract/getChannelAll", {}).then(
                    function(data){
                        $scope.channelNames=data.result;
                        baseService.gridOptions.channelNames=data.result;
                    }
                );
                //查询渠道下所有的门店
                $scope.$watch('obj.searchObj.EQ$channel_id', function(scope){
                    $scope.storeNames={};
                    baseService.post("/frameContract/getAllList",{
                        id:scope
                    }).then(
                        function(data){
                            $scope.storeNames=data.result;
                            baseService.gridOptions.storeNames=data.result;
                        }
                    )
                });

                //查询渠道下所有的门店
                $scope.$watch('obj.object.channel_id', function(scope){
                    $scope.storeNames={};
                    baseService.post("/frameContract/getAllList",{
                        id:scope
                    }).then(
                        function(data){
                            $scope.storeNames=data.result;
                            baseService.gridOptions.storeNames=data.result;
                        }
                    )
                });



             //   查询所有门店信息
                baseService.post("/frameContract/getStoreAll", {}).then(
                    function(data){
                        $scope.storeNames=data.result;
                        baseService.gridOptions.storeNames2=data.result;
                    }
                );

                /*保存或更改*/
                $scope.save=function(){
                    //验证合同编号不能为空
                    if(angular.isUndefined($scope.user_form.contract_number.$modelValue)){
                        $scope.user_form.contract_number.$touched = "true";
                        return;
                    }else{
                        //验证业务主体不能为空
                        if(angular.isUndefined($scope.user_form.main_part_id.$modelValue)){
                            $scope.user_form.main_part_id.$touched = "true";
                            return;
                        }else{
                            //验证有效期开始时间不能为空
                            if(angular.isUndefined($scope.user_form.expiry_begin.$modelValue)){
                                $scope.user_form.expiry_begin.$touched = "true";
                                return;
                            }else{
                                //验证有效期结束时间不能为空
                                if(angular.isUndefined($scope.user_form.expiry_end.$modelValue)){
                                    $scope.user_form.expiry_end.$touched = "true";
                                    return;
                                }else{
                                    //验证渠道名称不能为空
                                    if(angular.isUndefined($scope.user_form.channel_id.$modelValue)){
                                        $scope.user_form.channel_id.$touched = "true";
                                        return;
                                    }else{
                                        //验证其他可为空的字段
                                        if (!$scope.user_form.$valid) {
                                            return;
                                        }else{
                                            if($scope.user_form.expiry_end.$modelValue-$scope.user_form.expiry_begin.$modelValue<0){
                                                baseService.alert("结束时间必须大于等于开始时间！");
                                                return;
                                            }
                                        }




                                    }

                                }
                            }
                        }
                    }//保存和更新的方法
                    baseService.saveOrUpdate($scope.obj.object.id,window.formatObj($scope.obj.object)).then(
                        function(data){
                            $scope.data=data.result;
                            //如果返回的是success
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                                //如果返回的是date
                            }else if($scope.data=="date"){
                                baseService.alert("结束时间必须大于等于开始时间！");
                                //如果返回的是checkNameError,代表没通过重名验证
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("合同编号已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };


                /*更新方法*/
                $scope.update=function(l){
                    //参数l传到对象里
                    $scope.obj.object = angular.copy(l);
                    //获取l中的有效期开始时间转成时间类型传到前台显示出来
                    $scope.obj.object.expiry_begin = new Date(l.expiry_begin);
                    //获取l中的有效期结束时间转成时间类型传到前台显示出来
                    $scope.obj.object.expiry_end = new Date(l.expiry_end);
                    //调打开右侧页面的方法
                    $scope.openRight();
                };
                /*下边列表栏*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'contract_number',
                        displayName: '合同编号',
                        minWidth: 120,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss',
                        cellTooltip: true
                    },
                    {name: 'main_part_id', displayName: '甲方主体', minWidth: 120,headerCellClass:'headerCss',
                        cellTooltip: true,cellFilter: 'mainPartFilter:grid.options.mainPartNames'},
                    {name: 'channel_id', displayName: '渠道', minWidth: 120,headerCellClass:'headerCss',
                        cellTooltip: true,cellFilter: 'channelFilter:grid.options.channelNames'},
                    {name: 'store_id', displayName: '店面', minWidth: 120,headerCellClass:'headerCss',
                        cellTooltip: true,cellFilter: 'storeFilter:grid.options.storeNames2'},
                    {name: 'first_contact', displayName: '联系人', minWidth: 120,headerCellClass:'headerCss'},
                    {name: 'first_contact_info', displayName: '联系方式', minWidth: 120,headerCellClass:'headerCss'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 80,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'编辑权限\',\'BUTTON\')"   aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)" ng-if="grid.appScope.checkAuth(\'删除权限\',\'BUTTON\')"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                        '</div>'
                    }
                ];
                //分页
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
                $scope.gridOptions = baseService.gridOptions;

                getPage();

                //删除方法
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

                /*分页*/
                function getPage() {
                    baseService.findAllWithParams($scope.obj.searchObj).then(
                        function (data) {
                            $scope.gridOptions.totalItems = data.result.totalRow;//显示多少行
                            $scope.gridOptions.data = data.result.list;//后台给前台绑定数据
                        }
                    );
                }
            }
        ])
    }
)