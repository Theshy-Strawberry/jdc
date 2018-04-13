/**
 * Created by Tinkpad on 2016/6/8.
 */
/**
 * @ngdoc function 地产商管理
 * @name codeApp.controller:JobTitleCTRL
 * @description
 * # JobTitleCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('realEstateCTRL',[
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
                //baseService.post("/index/getUser", {}).then(
                //    function (data) {
                //        baseService.post("/sysauth/getUserAuths", {
                //            user_name : data.result.user_name,
                //            menu_id : window.rootMenu.id
                //        }).then(
                //            function (data) {
                //                $scope.userAuth = data.result;
                //            }
                //        )
                //    }
                //);
                //
                //$scope.checkAuth = function (val,type) {
                //    if(!angular.isUndefined($scope.userAuth)){
                //        for(var i=0;i<$scope.userAuth.length;i++){
                //            if(val == $scope.userAuth[i][5] &&type == $scope.userAuth[i][6]){
                //                return false;
                //            }
                //        }
                //        return true;
                //    }
                //};
                //==================权限相关代码:完成================================

                //国际化
                i18nService.setCurrentLang('zh-cn');

                //访问后台的路径名
                baseService.actionName.name="realEstate";
                $scope.selectData = selectData;

                //初始化opFlg的值为0；
                $scope.opFlg=0;

                //右侧菜单方法;
                $scope.toggleRight=function(){
                    $scope.opFlg=1;

                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);

                    //创建时间
                    //$scope.obj.object.create_date=new Date();
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());


                    //创建人的获取 访问baseService的getCurUser方法 获取当前用户名
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    )
                    //访问打开右侧菜单方法
                    $scope.openRight();
                };

                //打开右侧菜单
                $scope.openRight=baseService.buildRightToggler();

                //关闭方法
                $scope.close = function () {
                    $scope.obj.object = {};
                    //getPage();
                    baseService.closeRightToggler();
                };

                /*对象变量*/
                $scope.obj={
                    searchObj:{},
                    object:{}
                };

                //结束时间只可晚于创建时间的验证判断
                $scope.search=function(){
                    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                        if ($scope.obj.searchObj.END$create_date-$scope.obj.searchObj.START$create_date<0){
                            baseService.alert("结束时间只可选择晚于创建时间的时间！");
                            return;
                        }
                    }
                    getPage();
                };

                /*保存或更改*/
                $scope.save=function(){
                    //判断验证是否通过
                    if (angular.isUndefined($scope.user_form.company_name.$modelValue)){
                        $scope.user_form.company_name.$touched = "true";
                        return;
                    }
                    //else {
                    //    if (angular.isUndefined($scope.user_form.calculate_type.$modelValue)) {
                    //        $scope.user_form.calculate_type.$touched = "true";
                    //        return;
                    //    }
                    //}
                    baseService.saveOrUpdate($scope.obj.object.id,window.formatObj($scope.obj.object)).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("地产商名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };

                /*更新方法*/
                $scope.update=function(l){
                    //设置opFlg的值为2
                    $scope.opFlg=2;
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    //设置修改时间
                    //$scope.obj.object.modify_date=new Date();
                    $scope.obj.object.modify_date=baseService.formatDatetime(new Date());
                    //设置创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.modify_user=data.session.userSession.real_name;
                        }
                    )
                    $scope.openRight();
                };
                //查询所有信息
                baseService.post("/realEstate/getUserAll", {}).then(
                    function(data){
                        baseService.gridOptions.userIds=data.result;
                    }
                );
                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    //{
                    //    name: 'rownum_',minWidth:40,displayName: '序号',headerCellClass:'headerCss',
                    //    cellTemplate: '<div style="text-align: center;">{{row.entity.rownum_}}</div>'
                    //},
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'company_name',
                        displayName: '地产商名称',
                        minWidth: 150,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'create_user', displayName: '创建人', minWidth: 150,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'userFilter:grid.options.userIds'},
                    {name: 'create_date', displayName: '创建时间', minWidth: 150,headerCellClass:'headerCss', cellClass:'cellCss'},
                        //cellTemplate: '<div style="text-align: center;">{{row.entity.create_date}}</div>' },
                    //{name: 'create_user', displayName: '创建人', minWidth: 150,headerCellClass:'headerCss'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 80,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                        '</div>'
                    }
                ];

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
                            $scope.gridOptions.totalItems = data.result.totalRow;
                            $scope.gridOptions.data = data.result.list;
                        }
                    );
                }
            }
        ])
    }
);