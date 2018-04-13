/**
 * Created by 冕寒 on 2016/6/23.
 */
/**
 * Created by Tinkpad on 2016/6/8.
 */
/**
 * @ngdoc function 项目基础管理
 * @name codeApp.controller:projectDataTypeManagementCTRL
 * @description
 * # projectDataTypeManagementCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('projectDataTypeManagementCTRL',[
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
                baseService.actionName.name="projectDataTypeManagement";

                $scope.paginationOptions2 = {
                    pageNumber: 1,
                    pageSize: 10,
                    sort: null
                };
                $scope.selectData = selectData;

                //初始化opFlg的值为0；
                $scope.opFlg=0;

                //右侧菜单方法;
                $scope.toggleRight=function(l){
                    $scope.opFlg=1;
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);

                    //创建时间
                    //$scope.obj.object.create_date=new Date();
                    //$scope.obj.object.effective_begin_date=new Date();
                    //$scope.obj.object.effective_end_date=new Date();
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    $scope.obj.object.project_name = l.project_name;
                    $scope.obj.object.project_id = l.id;
                    baseService.post("/projectDataTypeManagement/getProjectUser", {project_id : l.id}).then(
                        function (data) {
                            $scope.projectUsers = data.result;
                            $scope.gridOptions2.projectUsers=data.result;
                        }
                    )
                    $scope.authInfo = new Array();
                    $scope.arr = new Array();
                    $scope.authInfo.push(
                        {id:1,name:"新增"},
                        {id:2,name:"保存"},
                        {id:3,name:"删除"},
                        {id:4,name:"查看"},
                        {id:5,name:"审核"},
                        {id:6,name:"导出"},
                        {id:7,name:"提交"}
                    )

                    //创建人的获取 访问baseService的getCurUser方法 获取当前用户名
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    )
                    $timeout(
                        function() {
                            angular.element(document.getElementsByClassName('grid')[0]).css('height',  '300px');
                        },
                        300
                    );
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
                    $scope.arr = new Array();
                    $scope.auths = new Array();
                    $scope.authInfo = new Array();
                    baseService.post("/projectDataTypeManagement/findSubAll", {
                        tab_code: ""
                    }).then(
                        function (data) {
                            $scope.gridOptions2.totalItems = data.result.length;
                            $scope.gridOptions2.data = data.result;
                        }
                    )
                };

                /*对象变量*/
                $scope.obj={
                    searchObj:{},
                    object:{}
                };

                //结束时间只可晚于创建时间的验证判断
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

                //查询所有信息
                baseService.post("/projectDataTypeManagement/getUserAll", {}).then(
                    function(data){
                        //  $scope.userIds=data.result;
                        baseService.gridOptions.userIds=data.result;
                        //
                    }
                );
                baseService.post("/projectDataTypeManagement/getBusinessMainPartList", {}).then(
                    function (data) {
                        $scope.businessMainPart = data.result;
                        baseService.gridOptions.businessMainPart = data.result;
                    }
                )
                baseService.post("/projectDataTypeManagement/getDataType", {}).then(
                    function (data) {
                        $scope.dataTypes = data.result;
                        baseService.gridOptions.dataTypes=data.result;
                    }
                )
                baseService.post("/projectDataTypeManagement/getDataTypeName", {}).then(
                    function (data) {
                        $scope.dataTypeNames = data.result;
                        $scope.gridOptions2.dataTypeNames=data.result;
                    }
                )


                /*保存或更改*/
                $scope.save=function(){
                    if (angular.isUndefined($scope.user_form.tab_code.$modelValue)){
                        $scope.user_form.tab_code.$touched = "true";
                        return;
                    }else{
                        if (angular.isUndefined($scope.user_form.user_id.$modelValue)){
                            $scope.user_form.user_id.$touched = "true";
                            return;
                        }else{
                            if (!$scope.user_form.$valid) {
                                return;
                            }

                        }

                    }
                    $scope.auths = new Array();
                    for(var i = 0;i<$scope.authInfo.length;i++){
                        if($scope.authInfo[i].selected){
                            var id = $scope.authInfo[i].id;
                            $scope.auths.push(id);
                        }
                    }
                    $scope.obj.object.arr = $scope.arr;
                    $scope.obj.object.auths = $scope.auths;
                    if($scope.obj.object.arr.length<=0){
                        baseService.alert("请选择数据类型字段！");
                        return;
                    }
                    if($scope.obj.object.auths.length<=0){
                        baseService.alert("请选择人员权限！");
                        return;
                    }
                    baseService.saveOrUpdate($scope.obj.object.id,$scope.obj.object).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="true"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                            //    此处是项目编号验证
                            }else if($scope.data=="checkNameColError"){
                                baseService.alert("该人员已经分配过台账！");
                            }else if($scope.data=="checkNameTabError"){
                                baseService.alert("该人员已经分配过台账字段！");
                            }else if($scope.data=="checkNamePermissionError"){
                                baseService.alert("该人员已经分配过台账权限！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };


                $scope.detail=function(l){
                    $rootScope.project_id = l.id;
                    window.location.href=window.ServerURL + "/index.html#/projectSubDataTypeManagement";
                };

                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:20,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'project_id',
                        displayName: '项目编号',
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
                        '<md-icon md-svg-src="images/icons/file.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.detail(row.entity)"  aria-label="台账详情"><md-tooltip md-autohide>台账详情</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/calculation_add.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.toggleRight(row.entity)"  aria-label="添加"><md-tooltip md-autohide>添加</md-tooltip></md-icon>' +
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
                function delArr(arr,index){
                    return arr.slice(0,index).concat(arr.slice(index+1,arr.length));
                }
                $scope.gridOptions2 = {
                    //enableSorting: true,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions2.pageSize,
                    columnDefs: [
                        {
                            name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false,enableSorting: false
                        },
                        {name: 'user_sort_code', displayName: '排序', minWidth: 20,type: 'number',enableCellEdit: true,enableSorting: false},
                        {
                            name: 'col_code',
                            displayName: '数据名称',
                            minWidth: 300,
                            headerCellClass:'headerCss',
                            suppressRemoveSort: false,
                            enableSorting: false,
                            enablePinning: false,
                            enableCellEdit: false,
                            cellFilter: 'dataTypeFilter:grid.options.dataTypeNames'
                        }
                    ],
                    onRegisterApi: function( gridApi ) {
                        $scope.arr = new Array();
                        $scope.gridApi = gridApi;
                        gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
                            $scope.isCheck = 0;
                            for(var i=0;i<rows.length;i++){
                                if(rows[i].isSelected){
                                    if(rows[i].entity.user_sort_code != null){
                                        var re = /^[0-9]*[1-9][0-9]*$/ ;
                                        if(!re.test(rows[i].entity.user_sort_code)){
                                            rows[i].isSelected = false;
                                            $scope.isCheck = 2;
                                        }else{
                                            if($scope.arr.length <= 0){
                                                $scope.arr.push({col_code:rows[i].entity.col_code,user_sort_code:rows[i].entity.user_sort_code});
                                            }else{
                                                for(var j = 0; j < $scope.arr.length;j++){
                                                    if(rows[i].entity.user_sort_code == $scope.arr[j].user_sort_code){
                                                        rows[0].isSelected = false;
                                                        rows[i].isSelected = false;
                                                        $scope.isCheck = 3;
                                                    }
                                                }
                                                $scope.arr.push({col_code:rows[i].entity.col_code,user_sort_code:rows[i].entity.user_sort_code});
                                            }
                                        }
                                    }else{
                                        rows[i].isSelected = false;
                                        $scope.isCheck = 1;
                                    }
                                }else{
                                    $scope.arr = new Array();
                                    break;
                                }
                            }
                            if($scope.isCheck == 1){
                                baseService.alert("请先填写排序！");
                            }
                            if($scope.isCheck == 2){
                                baseService.alert("只可以填写正整数！");
                            }
                            if($scope.isCheck == 3){
                                $scope.arr = new Array();
                                baseService.alert("排序不能重复！");
                            }
                        });
                        gridApi.selection.on.rowSelectionChanged($scope,function(row){
                            if(row.isSelected){
                                if(row.entity.user_sort_code != null){
                                    var re = /^[0-9]*[1-9][0-9]*$/ ;
                                    if(!re.test(row.entity.user_sort_code)){
                                        row.isSelected = false;
                                        baseService.alert("只可以填写正整数！");
                                        return
                                    }
                                    if($scope.arr.length > 0){
                                        for(var i = 0; i < $scope.arr.length;i++){
                                            if(row.entity.user_sort_code == $scope.arr[i].user_sort_code){
                                                row.isSelected = false;
                                                baseService.alert("排序不能重复！");
                                                return;
                                            }
                                        }
                                        $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.user_sort_code});
                                    }else{
                                        $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.user_sort_code});
                                    }
                                }else{
                                    row.isSelected = false;
                                    baseService.alert("请先填写排序！");
                                }

                            }
                            if(row.isSelected){
                                var flag = true;
                                for(var i=0;i< $scope.arr.length;i++){
                                    if(row.entity.col_code == $scope.arr[i].col_code){
                                        flag = false;
                                        break;
                                    }else{
                                        flag = true;
                                    }
                                }
                                if(flag){
                                    $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.sort_code});
                                }
                            }else{
                                for(var i=0;i< $scope.arr.length;i++){
                                    if(row.entity.col_code == $scope.arr[i].col_code){
                                        $scope.arr = delArr($scope.arr,i);
                                    }
                                }
                            }
                        });
                        //$scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        //    if (sortColumns.length == 0) {
                        //        baseService.paginOpt.sort = null;
                        //    } else {
                        //        baseService.paginOpt.sort = sortColumns[0].sort.direction;
                        //    }
                        //    //getPage2();
                        //});
                        //gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        //    baseService.paginOpt.pageNumber = newPage;
                        //    baseService.paginOpt.pageSize = pageSize;
                        //    //getPage2();
                        //});
                        //$scope.gridApi.core.on.sortChanged( $scope, function( grid, sort ) {
                        //    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
                        //})
                    }
                };
                //getPage2();

                $scope.checkDataType = function(l){
                    baseService.post("/projectDataTypeManagement/findSubAll", {
                        tab_code: l
                    }).then(
                        function (data) {
                            //$scope.gridOptions2.totalItems = data.result.length;
                            $scope.gridOptions2.data = data.result;
                        }
                    )
                }
                //function getPage2() {
                //
                //};
            }
        ])
    }
);
