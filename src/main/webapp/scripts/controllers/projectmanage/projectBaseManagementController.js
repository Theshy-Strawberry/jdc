/**
 * Created by 冕寒 on 2016/6/23.
 */
/**
 * Created by Tinkpad on 2016/6/8.
 */
/**
 * @ngdoc function 项目基础管理
 * @name codeApp.controller:projectBaseManagementCTRL
 * @description
 * # projectBaseManagementCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('projectBaseManagementCTRL',[
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
                baseService.actionName.name="projectBaseManagement";
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
                    //$scope.obj.object.effective_begin_date=new Date();
                    //$scope.obj.object.effective_end_date=new Date();
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

                    //getPage();
                    baseService.closeRightToggler();
                    $scope.obj.object = {};
                };

                /*对象变量*/zuihualideanyingzhiwu
                $scope.obj={
                    searchObj:{},
                    object:{}
                };
                ///结束时间只可晚于创建时间的验证判断
                $scope.search=function(){
                    if ($scope.obj.searchObj.END$effective_end_date != 'undefined' && $scope.obj.searchObj.END$effective_end_date != null){
                        var endDate = $scope.obj.searchObj.END$effective_end_date;
                        var startDate = $scope.obj.searchObj.START$effective_begin_date;
                        if (startDate >endDate){
                            baseService.alert("结束时间必须大于等于开始时间！");
                            return;
                        }
                    }
                    getPage();
                };
                //结束时间只可晚于创建时间的验证判断
                //$scope.search=function(){
                //    if ($scope.obj.searchObj.LIKE$effective_end_date != 'undefined' && $scope.obj.searchObj.LIKE$effective_end_date != null){
                //        if ($scope.obj.searchObj.LIKE$effective_end_date-$scope.obj.searchObj.LIKE$effective_begin_date<0){
                //            baseService.alert("结束时间只可选择晚于创建时间的时间！");
                //            return;
                //        }
                //    }
                //    getPage();
                //};

                //$scope.search=function(){
                //    if ($scope.obj.searchObj.effective_end_date != 'undefined' && $scope.obj.searchObj.effective_end_date != null){
                //        var endDate = $scope.obj.searchObj.effective_end_date;
                //        var startDate = $scope.obj.searchObj.effective_begin_date
                //        if (startDate > endDate){
                //            baseService.alert("结束日期必须大于开始日期！");
                //            return;
                //        }
                //    }
                //    getPage();
                //};

                //查询所有信息
                baseService.post("/projectBaseManagement/getUserAll", {}).then(
                    function(data){
                        //  $scope.userIds=data.result;
                        baseService.gridOptions.userIds=data.result;
                        //
                    }
                );
                baseService.post("/projectBaseManagement/getBusinessMainPartList", {}).then(
                    function (data) {
                        $scope.businessMainPart = data.result;
                        baseService.gridOptions.businessMainPart = data.result;
                    }
                )

                /*保存或更改*/
                $scope.save=function(){
                    //判断验证是否通过
                    //合同编号
                    if (angular.isUndefined($scope.user_form.project_id.$modelValue)){
                        $scope.user_form.project_id.$touched = "true";
                        return;
                    }else{ss
                        if (angular.isUndefined($scope.user_form.project_name.$modelValue)){
                            $scope.user_form.project_name.$touched = "true";
                            return;
                        }else{
                            if (angular.isUndefined($scope.user_form.first_party_name.$modelValue)){
                                $scope.user_form.first_party_name.$touched = "true";
                                return;
                            }else{
                                if (angular.isUndefined($scope.user_form.second_party_id.$modelValue)){
                                    $scope.user_form.second_party_id.$touched = "true";
                                    return;
                                }else{
                                    if (angular.isUndefined($scope.user_form.effective_begin_date.$modelValue)){
                                        $scope.user_form.effective_begin_date.$touched = "true";
                                        return;
                                    }else{
                                        if (angular.isUndefined($scope.user_form.effective_end_date.$modelValue)){
                                            $scope.user_form.effective_end_date.$touched = "true";
                                            return;
                                        }else{
                                            if (!$scope.user_form.$valid) {
                                                return;
                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }
                    baseService.saveOrUpdate($scope.obj.object.id,window.formatObj($scope.obj.object)).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                            //    此处是合同编号验证
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("合同编号已存在！");
                            }else if($scope.data=="date"){
                                baseService.alert("结束时间必须大于等于开始时间！");
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
                    //给控件赋值
                    $scope.obj.object.effective_begin_date = new Date(l.effective_begin_date);
                    $scope.obj.object.effective_end_date = new Date(l.effective_end_date);
                    //设置创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.modify_user=data.session.userSession.real_name;
                        }
                    )
                    $scope.openRight();
                };


                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:20,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'project_id',
                        displayName: '合同编号',
                        minWidth: 70,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    //{name: 'calculate_type', displayName:'计算方式', minWidth:120, headerCellClass:'headerCss',cellClass:'cellCss'
                    //    ,cellTemplate:
                    //'<div>' +
                    //'{{row.entity.calculate_type | calculateTypeFilter}}' +
                    //'</div>'
                    //},
                    {name: 'project_name', displayName: '项目名称', minWidth:100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'first_party_name', displayName: '甲方主体', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'second_party_id', displayName: '乙方名称', minWidth: 120,cellTooltip: true,cellFilter: 'companyFilter:grid.options.businessMainPart', headerCellClass:'headerCss'},
                    //{name: 'second_party_id', displayName: '乙方名称', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'effective_begin_date', displayName: '开始日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'effective_end_date', displayName: '截止日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'first_party_person', displayName: '甲方联系人', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                     {name: 'first_party_person_telephone', displayName: '甲方联系方式', minWidth: 150,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'create_user', displayName: '创建人', minWidth: 120,headerCellClass:'headerCss',cellTooltip: true, cellFilter: 'userFilter:grid.options.userIds'},
                    //{name: 'create_date', displayName: '创建时间', minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'编辑权限\',\'BUTTON\')"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
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
            ////    导出
            //    $scope.exportWord1 = function (val) {
            //        baseService.post("/export/getWord1/", {engineering_no:$scope.row.entity.engineering_no}).then(
            //            function (data) {
            //                window.open(window.ServerURL+""+data.result);
            //            }
            //        )
            //    }
            }
        ])
    }
);
