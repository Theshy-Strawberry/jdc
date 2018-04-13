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

        controllers.controller('projectSubDataTypeManagementCTRL',[
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
                $scope.paginationOptions3 = {
                    pageNumber: 1,
                    pageSize: 10,
                    sort: null
                };
                $scope.selectData = selectData;

                //初始化opFlg的值为0；
                $scope.opFlg=0;

                //右侧菜单方法;

                //打开右侧菜单
                $scope.openRight=baseService.buildToggler("right");

                //关闭方法
                $scope.close = function () {

                    //getPage();
                    baseService.closeToggler("right");
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

                $scope.openRight1=baseService.buildToggler("right1");

                //关闭方法
                $scope.close1 = function () {

                    //getPage();
                    baseService.closeToggler("right1");
                    $scope.obj.object = {};
                    $scope.arr = new Array();
                    $scope.auths = new Array();
                    $scope.authInfo1 = new Array();
                    baseService.post("/projectDataTypeManagement/findSubAll2", {
                        tab_code: ""
                    }).then(
                        function (data) {
                            $scope.gridOptions3.totalItems = data.result.length;
                            $scope.gridOptions3.data = data.result;
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
                    if($scope.selectedItem !=null){
                        $scope.obj.searchObj.EQ$tab_code = $scope.selectedItem.tab_code;
                    }else{
                        $scope.obj.searchObj.EQ$tab_code = "";
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
                        $scope.gridOptions3.dataTypeNames=data.result;
                    }
                )

                $scope.querySearch = function (query) {
                    return baseService.autocomplete(query,$scope.dataTypes,'tab_name');
                }


                /*保存或更改*/
                $scope.save=function(){
                    if (angular.isUndefined($scope.user_form.user_id.$modelValue)){
                        $scope.user_form.user_id.$touched = "true";
                        return;
                    }else{
                        if (!$scope.user_form.$valid) {
                            return;
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
                    //if($scope.obj.object.arr.length>1){
                    //    for(var i=0;i<$scope.obj.object.arr.length;i++){
                    //        for(var j = i+1;j<$scope.obj.object.arr.length;j++){
                    //            if($scope.obj.object.arr[i].user_sort_code == $scope.obj.object.arr[j].user_sort_code){
                    //                baseService.alert("排序不能重复！");
                    //                return;
                    //            }
                    //        }
                    //    }
                    //}else{
                    //    return;
                    //}
                    if($scope.obj.object.auths.length<=0){
                        baseService.alert("请选择人员权限！");
                        return;
                    }
                    $scope.obj.object.id = 0;
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

                /*更新方法*/
                $scope.update=function(l){
                    //设置opFlg的值为2
                    $scope.opFlg=2;
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    baseService.post("/projectDataTypeManagement/getProjectUser", {project_id : l.project_id}).then(
                        function (data) {
                            $scope.projectUsers = data.result;
                            $scope.gridOptions2.projectUsers=data.result;
                        }
                    )
                    baseService.post("/projectDataTypeManagement/getProjectName", {project_id : l.project_id}).then(
                        function (data) {
                            $scope.obj.object.project_name = data.result.project_name;
                        }
                    )
                    $scope.authInfo = new Array();
                    //$scope.arr = new Array();
                    $scope.authInfo.push(
                        {id:1,name:"新增",selected:false},
                        {id:2,name:"保存",selected:false},
                        {id:3,name:"删除",selected:false},
                        {id:4,name:"查看",selected:false},
                        {id:5,name:"审核",selected:false},
                        {id:6,name:"导出",selected:false},
                        {id:7,name:"提交",selected:false}
                    )
                    //设置创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.modify_user=data.session.userSession.real_name;
                        }
                    )
                    $scope.openRight();
                };

                $scope.detail=function(l){
                    //设置opFlg的值为2
                    $scope.opFlg=2;
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    baseService.post("/projectDataTypeManagement/getProjectUser", {project_id : l.project_id}).then(
                        function (data) {
                            $scope.projectUsers = data.result;
                            $scope.gridOptions2.projectUsers=data.result;
                        }
                    )
                    baseService.post("/projectDataTypeManagement/getProjectName", {project_id : l.project_id}).then(
                        function (data) {
                            $scope.obj.object.project_name = data.result.project_name;
                        }
                    )
                    $scope.authInfo1 = new Array();
                    $scope.authInfo1.push(
                        {id:1,name:"新增",selected:false},
                        {id:2,name:"保存",selected:false},
                        {id:3,name:"删除",selected:false},
                        {id:4,name:"查看",selected:false},
                        {id:5,name:"审核",selected:false},
                        {id:6,name:"导出",selected:false},
                        {id:7,name:"提交",selected:false}
                    )
                    //设置创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.modify_user=data.session.userSession.real_name;
                        }
                    )
                    $scope.openRight1();
                };

                $scope.goBackToDataType=function(){
                    window.location.href=window.ServerURL + "/index.html#/projectDataTypeManagement";
                };

                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    {
                        name: 'rownum',minWidth:20,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'tab_code',
                        displayName: '类型名称',
                        minWidth: 70,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss',
                        cellFilter: 'dataTabFilter:grid.options.dataTypes'
                    },
                    {
                        name: '操作', pinnedRight: true, minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/ic_visibility_24px.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.detail(row.entity)"  aria-label="查看"><md-tooltip md-autohide>查看</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.update(row.entity)" aria-label="编辑"><md-tooltip md-autohide>编辑</md-tooltip></md-icon>' +
                        '</div>'
                    }
                ];
                //分页
                baseService.gridOptions.onRegisterApi = function (gridApi) {
                    $scope.gridApi = gridApi;
                    //$scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    //    if (sortColumns.length == 0) {
                    //        baseService.paginOpt.sort = null;
                    //    } else {
                    //        baseService.paginOpt.sort = sortColumns[0].sort.direction;
                    //    }
                    //    getPage();
                    //});
                    //gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    //    baseService.paginOpt.pageNumber = newPage;
                    //    baseService.paginOpt.pageSize = pageSize;
                    //    getPage();
                    //});
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
                    $scope.project_id = $rootScope.project_id
                    baseService.findAllWithParams2($scope.obj.searchObj,$scope.project_id).then(
                        function (data) {
                            //$scope.gridOptions.totalItems = data.result.length;//显示多少行
                            $scope.gridOptions.data = data.result;//后台给前台绑定数据
                        }
                    );
                }
                function delArr(arr,index){
                    return arr.slice(0,index).concat(arr.slice(index+1,arr.length));
                }
                $scope.gridOptions2 = {
                    //enableSorting: true,
                    //useExternalPagination: true,
                    //useExternalSorting: true,
                    paginationPageSizes: [10, 20, 50],
                    enableSorting: false,
                    paginationPageSize: $scope.paginationOptions2.pageSize,
                    columnDefs: [
                        {
                            name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss',enableSorting: false,enableCellEdit: false
                        },
                        {name: 'sort_code', displayName: '排序', minWidth: 20,type: 'number',enableSorting: false,enableCellEdit: true},
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
                        $scope.gridApi1 = gridApi;
                        gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
                            $scope.isCheck = 0;
                            for(var i=0;i<rows.length;i++){
                                if(rows[i].isSelected){
                                    if(rows[i].entity.sort_code != null){
                                        var re = /^[0-9]*[1-9][0-9]*$/ ;
                                        if(!re.test(rows[i].entity.sort_code)){
                                            rows[i].isSelected = false;
                                            $scope.isCheck = 2;
                                        }else{
                                            if($scope.arr.length <= 0){
                                                $scope.arr.push({col_code:rows[i].entity.col_code,user_sort_code:rows[i].entity.sort_code});
                                            }else{
                                                for(var j = 0; j < $scope.arr.length;j++){
                                                    if(rows[i].entity.sort_code == $scope.arr[j].user_sort_code){
                                                        rows[0].isSelected = false;
                                                        rows[i].isSelected = false;
                                                        $scope.isCheck = 3;
                                                    }
                                                }
                                                $scope.arr.push({col_code:rows[i].entity.col_code,user_sort_code:rows[i].entity.sort_code});
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
                                //if(rows[i].isSelected){
                                //    $scope.arr.push({col_code:rows[i].entity.col_code,user_sort_code:rows[i].entity.sort_code});
                                //}else{
                                //    $scope.arr = new Array();
                                //    break;
                                //}
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
                                if(row.entity.sort_code != null){
                                    var re = /^[0-9]*[1-9][0-9]*$/ ;
                                    if(!re.test(row.entity.sort_code)){
                                        row.isSelected = false;
                                        baseService.alert("只可以填写正整数！");
                                        return
                                    }
                                    if($scope.arr.length > 0){
                                        for(var i = 0; i < $scope.arr.length;i++){
                                            if(row.entity.sort_code == $scope.arr[i].user_sort_code){
                                                row.isSelected = false;
                                                baseService.alert("排序不能重复！");
                                                return;
                                            }
                                        }
                                        row.isSelected = true;
                                        $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.sort_code});
                                    }else{
                                        row.isSelected = true;
                                        $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.sort_code});
                                    }
                                }else{
                                    row.isSelected = false;
                                    baseService.alert("请先填写排序！");
                                }

                            }
                            //if(row.isSelected){
                            //    $scope.arr.push({col_code:row.entity.col_code,user_sort_code:row.entity.sort_code});
                            //}
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
                        //$scope.gridApi1.core.on.sortChanged($scope, function (grid, sortColumns) {
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
                        //$scope.gridApi1.core.on.sortChanged( $scope, function( grid, sort ) {
                        //    $scope.gridApi1.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
                        //})
                    }
                };
                //getPage2();

                $scope.checkProjectUser = function(l){
                    baseService.post("/projectDataTypeManagement/findSubAll", {
                        tab_code: $scope.obj.object.tab_code
                    }).then(
                        function (data) {

                            baseService.post("/projectDataTypeManagement/findUserTabCol", {
                                tab_code: $scope.obj.object.tab_code,
                                project_id: $rootScope.project_id,
                                user_id: l
                            }).then(
                                function (data1) {
                                    $scope.arr = new Array();
                                    $scope.dataType = data.result;
                                    $scope.checkDataType = data1.result;
                                    if($scope.checkDataType.length > 0){
                                        for(var k = 0;k<$scope.checkDataType.length;k++){
                                            for(var j = 0;j<$scope.dataType.length;j++){
                                                if($scope.checkDataType[k].col_code == $scope.dataType[j].col_code){
                                                    if($scope.checkDataType[k].user_sort_code != null){
                                                        $scope.gridOptions2.data[j].sort_code = $scope.checkDataType[k].user_sort_code;
                                                    }
                                                    $scope.gridApi1.selection.selectRow($scope.gridOptions2.data[j]);
                                                }
                                            }
                                        }
                                    }

                                    //$scope.gridOptions2.totalItems = data.result.totalRow;
                                }
                            )
                            //$scope.gridOptions2.totalItems = data.result.length;
                            $scope.gridOptions2.data = data.result;
                        }
                    )

                    baseService.post("/projectDataTypeManagement/findUserPermission", {
                        tab_code: $scope.obj.object.tab_code,
                        project_id: $rootScope.project_id,
                        user_id: l
                    }).then(
                        function (data2) {
                            $scope.checkPermission = data2.result;
                            if($scope.checkPermission.length > 0){
                                for(var k = 0;k<$scope.checkPermission.length;k++){
                                    for(var j = 0;j<$scope.authInfo.length;j++){
                                        if($scope.checkPermission[k].permission_code == $scope.authInfo[j].id){
                                            $scope.authInfo[j].selected = true;
                                        }
                                    }
                                }
                            }else{
                                for(var j = 0;j<$scope.authInfo.length;j++){
                                    $scope.authInfo[j].selected = false;
                                }
                            }
                        }
                    )
                }

                $scope.gridOptions3 = {
                    //enableSorting: true,
                    //useExternalPagination: true,
                    //useExternalSorting: true,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions3.pageSize,
                    columnDefs: [
                        {
                            name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false,enableSorting: false
                        },
                        {name: 'user_sort_code', displayName: '排序',enableSorting: false, minWidth: 20},
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

                $scope.checkProjectUserDetail = function(l){
                    baseService.post("/projectDataTypeManagement/findSubAll2", {
                        tab_code: $scope.obj.object.tab_code,
                        project_id: $rootScope.project_id,
                        user_id: l
                    }).then(
                        function (data) {
                            //$scope.gridOptions3.totalItems = data.result.length;
                            $scope.gridOptions3.data = data.result;
                        }
                    )
                    baseService.post("/projectDataTypeManagement/findUserPermission", {
                        tab_code: $scope.obj.object.tab_code,
                        project_id: $rootScope.project_id,
                        user_id: l
                    }).then(
                        function (data2) {
                            $scope.checkPermission1 = data2.result;
                            if($scope.checkPermission1.length > 0){
                                for(var k = 0;k<$scope.checkPermission1.length;k++){
                                    for(var j = 0;j<$scope.authInfo1.length;j++){
                                        if($scope.checkPermission1[k].permission_code == $scope.authInfo1[j].id){
                                            $scope.authInfo1[j].selected = true;
                                        }
                                    }
                                }
                            }else{
                                for(var j = 0;j<$scope.authInfo1.length;j++){
                                    $scope.authInfo1[j].selected = false;
                                }
                            }
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
