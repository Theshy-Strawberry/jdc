/**
 * Created by liu on 2016/6/28.
 */
/**
 * @ngdoc function 项目人员管理
 * @name codeApp.controller:projectPersonnelCTRL
 * @description
 * # projectPersonnelCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('projectPersonnelCTRL',[
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
            function($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, $filter, uiGridConstants, baseService, $q, $mdToast, i18nService){
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
                baseService.actionName.name="projectPersonnel";

                $scope.paginationOptions2 = {
                    pageNumber: 1,
                    pageSize: 10,
                    sort: null
                };

                //右侧菜单方法;
                $scope.projectBaseId = {};
                $scope.projectBaseName = {};
                $scope.toggleRight=function(l){
                    $scope.projectBaseId = l.id;
                    $scope.projectBaseName = l.project_name;
                    baseService.post("/projectPersonnel/getPersonnelByProjectId", {
                        id: $scope.projectBaseId
                    }).then(
                        function (data) {
                            $scope.datas = data.result;
                            if($scope.datas.length == 0) {
                                $scope.datas = [{user_id: "", rowIndex: 0}];
                            }
                        }
                    )

                    //访问打开右侧菜单方法
                    $scope.openRight();
                };

                //打开右侧菜单
                $scope.openRight=baseService.buildRightToggler();

                baseService.post("/projectPersonnel/getUserAll", {}).then(
                    function(data){
                        $scope.userIds=data.result;
                        $scope.gridOptions2.userIds=data.result;
                    }
                );

                //查看人员信息
                $scope.toggleRight2=function(l){
                    //把项目ID赋给临时变量
                    $scope.projectBaseId = l.id;
                    $timeout(
                        function() {
                            angular.element(document.getElementsByClassName('grid')[0]).css('height',  '300px');
                        },
                        300
                    );
                    //查询人员信息
                    $scope.openRight2();
                    getPage2($scope.projectBaseId);

                };
                //打开人员详细菜单
                $scope.openRight2=baseService.buildRightToggler2();

                $scope.datas = [{
                    user_id:"",
                    //user_name:"",
                    rowIndex:0
                }];

                $scope.add=function(){
                    $scope.datas.push({user_id:"",rowIndex:$scope.datas.length});
                }

                function delArr(arr,index){
                    return arr.slice(0,index).concat(arr.slice(index+1,arr.length));
                }

                $scope.del=function(index){
                    $scope.datas = delArr($scope.datas,index);
                }



                //关闭方法
                $scope.close = function () {
                    //getPage();
                    baseService.closeRightToggler();
                    $scope.obj.object = {};
                };

                //关闭人员详细
                $scope.close2 = function () {
                    baseService.closeRightToggler2();
                };

                /*对象变量*/
                $scope.obj={
                    searchObj:{},
                    searchObj1:{},
                    object:{}
                };

                //查询功能
                $scope.search=function(){
                    getPage();
                };



                //查询乙方名称信息
                baseService.post("/projectPersonnel/getBusinessMainPartList", {}).then(
                    function (data) {
                        $scope.businessMainPart = data.result;
                        baseService.gridOptions.businessMainPart = data.result;
                    }
                )

                //验证人员必填
                $scope.check=function(index,val){
                    if(index == val){
                        return true;
                    }else{
                        return false;
                    }
                }

                /*保存或更改*/
                $scope.save=function(){
                    //判断验证是否通过
                    var flag = false;
                    for(var i =0;i<$scope.datas.length;i++){
                        $scope.datas[i].flag = "";
                       if($scope.datas[i].user_id == "" || $scope.datas[i].user_id == null || $scope.datas[i].user_id == undefined){
                           $scope.datas[i].flag = true;
                           var flag = true;
                       }
                    }
                    if(flag){
                        return;
                    }

                    $scope.arr = new Array();
                    for(var i =0;i<$scope.datas.length;i++){
                        $scope.arr.push($scope.datas[i].user_id);
                    }
                    for (var i = 0; i < $scope.arr.length; i++) {
                        for (var j = i+1; j < $scope.arr.length; j++) {
                            if ($scope.arr[i] == $scope.arr[j]) {
                                baseService.alert("姓名重复，请及时修改！");
                                return;
                            }
                        }
                    }
                    var r = $scope.arr.distinct();
                    $scope.obj.object.arr = r;
                    $scope.obj.object.project_base_id = $scope.projectBaseId;
                    $scope.obj.object.project_base_name = $scope.projectBaseName;
                    baseService.post("/projectPersonnel/deleteByProjectId", {
                        project_id: $scope.obj.object.project_base_id
                    }).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="true"){
                                baseService.saveOrUpdate($scope.obj.object.id, $scope.obj.object).then(
                                    function(data){
                                        $scope.data=data.result;
                                        if($scope.data=="true"){
                                            baseService.alert("保存成功！");
                                            $scope.close();
                                        }else if($scope.data=="checkNameError"){
                                            baseService.alert("人员姓名已存在！");
                                        }else{
                                            baseService.alert("保存失败！");
                                        }

                                    }
                                )
                            }else{
                                baseService.saveOrUpdate($scope.obj.object.id, $scope.obj.object).then(
                                    function(data){
                                        $scope.data=data.result;
                                        if($scope.data=="true"){
                                            baseService.alert("保存成功！");
                                            $scope.close();
                                        }else if($scope.data=="checkNameError"){
                                            baseService.alert("人员姓名已存在！");
                                        }else{
                                            baseService.alert("保存失败！");
                                        }

                                    }
                                )
                            }
                        }
                    );
                };

                /*更新方法*/
                $scope.update=function(l){
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    $scope.openRight2();
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
                    {name: 'project_name', displayName: '项目名称', minWidth:100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'first_party_name', displayName: '甲方主体', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'second_party_id', displayName: '乙方名称', minWidth: 120,cellTooltip: true,cellFilter: 'companyFilter:grid.options.businessMainPart', headerCellClass:'headerCss'},
                    {
                        name: '操作',  minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/calculation_add.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.toggleRight(row.entity)"  aria-label=”添加”><md-tooltip md-autohide>添加</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/ic_visibility_24px.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.toggleRight2(row.entity)"  aria-label=”查看”><md-tooltip md-autohide>查看</md-tooltip></md-icon>' +
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
                                getPage2($scope.projectBaseId);
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

                $scope.gridOptions2 = {
                    enableSorting: true,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions2.pageSize,
                    columnDefs: [
                        {
                            name: 'rownum',width:80,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                        },
                        {name: 'user_id', displayName: '人员姓名', minWidth: 300,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'userIdFilter:grid.options.userIds'},
                    ],
                    onRegisterApi: function( gridApi ) {
                        $scope.gridApi = gridApi;
                        $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                            if (sortColumns.length == 0) {
                                baseService.paginOpt.sort = null;
                            } else {
                                baseService.paginOpt.sort = sortColumns[0].sort.direction;
                            }
                            getPage2();
                        });
                        //gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        //    baseService.paginOpt.pageNumber = newPage;
                        //    baseService.paginOpt.pageSize = pageSize;
                        //    getPage2();
                        //});
                        //$scope.gridApi.core.on.sortChanged( $scope, function( grid, sort ) {
                        //    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
                        //})
                    }
                };

                getPage2();

                function getPage2(id) {
                    baseService.findAllWithParams2($scope.obj.searchObj1,id).then(
                        function (data) {
                            //$scope.gridOptions2.totalItems = data.result.totalRow;
                            $scope.gridOptions2.data = data.result;
                        }
                    );
                };
            }
        ])
    }
);