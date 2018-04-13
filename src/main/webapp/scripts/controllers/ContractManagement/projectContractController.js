/**
 * Created by 冕寒 on 2016/6/25.
 */
/**
 * Created by 冕寒 on 2016/6/23.
 */
/**
 * Created by Tinkpad on 2016/6/8.
 */
/**
 * @ngdoc function渠道合同管理
 * @name codeApp.controller:projectContractCTRL
 * @description
 * # projectContractCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('projectContractCTRL',[
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
                baseService.actionName.name="projectContract";
                $scope.selectData = selectData;

                //初始化opFlg的值为0；
                $scope.opFlg=0;

                //右侧菜单方法;
                $scope.toggleRight=function(){
                    $scope.opFlg=1;
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    $scope.obj.object.has_contract =1;
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

                /*对象变量*/
                $scope.obj={
                    searchObj:{},
                    object:{}
                };



                //$scope.search=function(){
                //    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                //        if ($scope.obj.searchObj.END$create_date-$scope.obj.searchObj.START$create_date<0){
                //            baseService.alert("有效期结束时间只可选择晚于有效期开始时间的时间！");
                //            return;
                //        }
                //    }
                //    getPage();
                //};

                //结束时间只可晚于创建时间的验证判断
                $scope.search=function(){
                    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                        var endDate = $scope.obj.searchObj.END$create_date;
                        var startDate = $scope.obj.searchObj.START$create_date;
                        if (startDate > endDate){
                            baseService.alert("结束日期必须大于等于开始日期！");
                            return;
                        }
                    }
                    getPage();
                };

                //结束时间只可晚于创建时间的验证判断
                //$scope.search=function(){
                //    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                //        if ($scope.obj.searchObj.END$create_date-$scope.obj.searchObj.START$create_date<0){
                //            baseService.alert("结束时间只可选择晚于创建时间的时间！");
                //            return;
                //        }
                //    }
                //    getPage();
                //};

                //查询所有信息
                baseService.post("/projectContract/getUserAll", {}).then(
                    function(data){
                        //  $scope.userIds=data.result;
                        baseService.gridOptions.userIds=data.result;
                        //
                    }
                );
                //查询所有项目基础信息
                baseService.post("/projectContract/getProjectBaseManagementList", {}).then(
                    function (data) {
                        $scope.businessMainPart = data.result;
                        baseService.gridOptions.businessMainPart = data.result;
                    }
                )
                //查询所有渠道框架合同基础信息
                baseService.post("/projectContract/getProjectBaseManagementList1", {}).then(
                    function (data) {
                        $scope.businessMainPart1 = data.result;
                        baseService.gridOptions.businessMainPart1 = data.result;
                    }
                )

                /*保存或更改*/
                $scope.save=function(){
                    //判断验证是否通过
                    //合同编号
                    if (angular.isUndefined($scope.user_form.project_code.$modelValue)){
                        $scope.user_form.project_code.$touched = "true";
                        return;
                    }
                    //项目名称
                    if (angular.isUndefined($scope.user_form.project_id.$modelValue)){
                        $scope.user_form.project_id.$touched = "true";
                        //baseService.alert("请选择项目名称！");
                        return;
                    }
                    //框架合同
                    if($scope.user_form.has_contract != undefined){
                        if (angular.isUndefined($scope.user_form.has_contract.$modelValue)){
                            $scope.user_form.has_contract.$touched = "true";
                            //baseService.alert("请选择框架合同！");
                            return;
                        }
                    }
                    //项目地址
                    if (angular.isUndefined($scope.user_form.project_address.$modelValue)){
                        $scope.user_form.project_address.$touched = "true";
                        return;
                    }
                    //有效期开始时间
                    if (angular.isUndefined($scope.user_form.expiry_begin.$modelValue)){
                        $scope.user_form.expiry_begin.$touched = "true";
                        //baseService.alert("请输入有效期开始时间！");
                        return;
                    }
                    //有效期结束时间
                    if (angular.isUndefined($scope.user_form.expiry_end.$modelValue)){
                        $scope.user_form.expiry_end.$touched = "true";
                        //baseService.alert("请输入有效期结束时间！");
                        return;
                    }
                    //限定字段
                    if (!$scope.user_form.$valid) {
                        return;
                    }

                    //有效期结束时间大于开始时间的验证【此处是前台验证的】
                    //if ($scope.obj.object.expiry_begin != 'undefined' && $scope.obj.object.expiry_begin != null) {
                    //    if ($scope.obj.object.expiry_end != 'undefined' && $scope.obj.object.expiry_end != null) {
                    //        var endDate = $scope.obj.object.expiry_end;
                    //        var startDate = $scope.obj.object.expiry_begin;
                    //        if (startDate > endDate) {
                    //            baseService.alert("结束日期必须大于等于开始日期！");
                    //            return;
                    //        }
                    //    }
                    //}

                    //if($scope.user_form.expiry_end.$modelValue-$scope.user_form.expiry_begin.$modelValue<0){
                    //    baseService.alert("有效期结束时间只可选择晚于有效期开始时间的时间！");
                    //    return;
                    //}

                    //结束时间只可晚于创建时间的验证判断
                    //if ($scope.obj.object.expiry_end != 'undefined' && $scope.obj.object.expiry_end != null){
                    //    //if ($scope.obj.object.expiry_end != 'undefined' && $scope.obj.object.expiry_end != null) {
                    //        var endDate = $scope.obj.object.expiry_end;
                    //        var startDate = $scope.obj.object.expiry_begin;
                    //        if (startDate > endDate) {
                    //            baseService.alert("结束日期必须大于开始日期！");
                    //            return;
                    //        }
                    //    //}
                    //}

                    baseService.saveOrUpdate($scope.obj.object.id,$scope.obj.object).then(
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
                                baseService.alert("有效期结束时间需大于等于有效期开始时间的时间！");
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
                    //$scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    $scope.obj.object.modify_date=baseService.formatDatetime(new Date());
                    //给控件赋值
                    $scope.obj.object.expiry_begin = new Date(l.expiry_begin);
                    $scope.obj.object.expiry_end = new Date(l.expiry_end);
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
                    //{name: 'calculate_type', displayName:'计算方式', minWidth:120, headerCellClass:'headerCss',cellClass:'cellCss'
                    //    ,cellTemplate:
                    //'<div>' +
                    //'{{row.entity.calculate_type | calculateTypeFilter}}' +
                    //'</div>'
                    //},
                    //项目名称 项目地址 我方经理 乙方经理 创建时间 创建人 操作 修改 删除
                    //项目名称是从项目基础管理表中查出数据
                    {name: 'project_id', displayName: '项目名称', minWidth: 120,cellTooltip: true,cellFilter: 'projectFilter:grid.options.businessMainPart', headerCellClass:'headerCss'},
                    {
                        name: 'project_code',
                        displayName: '合同编号',
                        minWidth: 70,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'project_address', displayName: '项目地址', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'self_manager', displayName: '我方经理', minWidth:100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'second_manager', displayName: '乙方项目经理', minWidth:100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //从另一个表中查数据
                    //{name: 'second_party_id', displayName: '乙方名称', minWidth: 120,cellTooltip: true,cellFilter: 'companyFilter:grid.options.businessMainPart', headerCellClass:'headerCss'},
                    //{name: 'second_party_id', displayName: '乙方名称', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'effective_begin_date', displayName: '开始日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'effective_end_date', displayName: '截止日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'first_party_person', displayName: '甲方联系人', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'first_party_person_telephone', displayName: '甲方联系方式', minWidth: 150,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'create_user', displayName: '经办人', minWidth: 120,headerCellClass:'headerCss',cellTooltip: true, cellFilter: 'userFilter:grid.options.userIds'},
                    {name: 'create_date', displayName: '经办时间', minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss'},
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
