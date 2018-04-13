/**
 * Created by liu on 2016/6/24.
 */
/**
 * @ngdoc function 数据管理
 * @name codeApp.controller:dataColCTRL
 * @description
 * # dataColCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('dataColCTRL',[
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
                baseService.actionName.name="dataCol";

                //右侧菜单方法;
                $scope.toggleRight=function(){
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);

                    //创建时间
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
                    baseService.closeRightToggler();
                    $scope.obj.object = {};
                };

                /*对象变量*/
                $scope.obj={
                    searchObj:{},
                    object:{}
                };

                //结束时间只可晚于创建时间的验证判断
                $scope.search=function(){
                    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                        var endDate = $scope.obj.searchObj.END$create_date;
                        var startDate = $scope.obj.searchObj.START$create_date;
                        if (startDate > endDate){
                            baseService.alert("结束日期必须大于开始日期！");
                            return;
                        }
                    }
                    getPage();
                };

                baseService.post("/projectBaseManagement/getUserAll", {}).then(
                    function(data){
                        //  $scope.userIds=data.result;
                        baseService.gridOptions.userIds=data.result;
                    }
                );
                $scope.isclick = true;
                /*保存或更改*/
                $scope.save=function(){
                    $scope.isclick = !$scope.isclick;
                    //判断验证是否通过
                    if (angular.isUndefined($scope.user_form.col_name.$modelValue)){
                        $scope.user_form.col_name.$touched = "true";
                        return;
                    }
                    baseService.saveOrUpdate($scope.obj.object.id,$scope.obj.object).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="true"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("数据名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };

                /*更新方法*/
                $scope.update=function(l){
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    //设置修改时间
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    );
                    $scope.openRight();
                };

                //修改状态
                $scope.open = function (l) {
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否修改此项纪录状态?')
                        .ariaLabel('Lucky day')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL+'/dataCol/open',
                            params: {
                                id: l.id,
                                del_flg: l.del_flg
                            }
                        };
                        $http(req).success(function (data) {
                            $scope.data = data.result;
                            if ($scope.data == "true") {
                                baseService.alert("修改状态成功！");
                                getPage();
                            }else if($scope.data == "false"){
                                baseService.alert("error！");
                            }
                        })
                    }, function () {
                    });
                };


                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'col_name',
                        displayName: '数据名称',
                        minWidth: 120,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'create_user', displayName: '经办人', minWidth: 120,headerCellClass:'headerCss',cellTooltip: true, cellFilter: 'userFilter:grid.options.userIds'},
                    {name: 'create_date', displayName: '经办时间', minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'del_flg', displayName: '状态', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellFilter: 'stateFilter:grid.options.del_flg'},
                    {
                        name: '操作', minWidth: 80,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'编辑权限\',\'BUTTON\')"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.open(row.entity)" ng-if="grid.appScope.checkAuth(\'删除权限\',\'BUTTON\')"  aria-label=”状态”><md-tooltip md-autohide>状态</md-tooltip></md-icon></div>'+
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
);