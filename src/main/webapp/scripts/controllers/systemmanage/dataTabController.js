/**
 * Created by liu on 2016/6/24.
 */
/**
 * @ngdoc function 数据类型管理
 * @name codeApp.controller:dataTabCTRL
 * @description
 * # dataTabCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('dataTabCTRL',[
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
                baseService.actionName.name="dataTab";

                $scope.paginationOptions2 = {
                    pageNumber: 1,
                    pageSize: 20,
                    sort: null
                };

                $scope.paginOpt2 = $scope.paginationOptions2;

                //右侧菜单方法;
                $scope.toggleRight=function(){
                    $scope.obj.object = {};
                    $scope.userform.$setPristine(true);
                    $scope.userform.$setUntouched(false);
                    $scope.data_col=new Array();
                    //访问打开右侧菜单方法
                    $scope.openRight();
                    $scope.gridApi.selection.clearSelectedRows();
                    $timeout(
                        function() {
                            angular.element(document.getElementsByClassName('grid')[0]).css('height',  '300px');
                        },
                        300
                    );
                };

                //打开右侧菜单
                $scope.openRight=baseService.buildRightToggler();

                //关闭方法
                $scope.close = function () {
                    baseService.closeRightToggler();
                    $scope.obj.object = {};
                };

                /*对象变量*/
                $scope.obj = {
                    searchObj: {},
                    searchObj1: {},
                    object: {}
                };

                $scope.search=function(){
                    getPage();
                };

                $scope.subSearch=function(){
                    getPage2();
                };

                //查询所有信息
                baseService.post("/brokerageType/getUserAll", {}).then(
                    function(data){
                        $scope.userIds=data.result;
                        baseService.gridOptions.userIds=data.result;
                    }
                );

                /*保存或更改*/
                $scope.deleteArr=new Array();
                $scope.save = function () {
                    if (angular.isUndefined($scope.userform.tab_name.$modelValue)){
                        $scope.userform.tab_name.$touched = "true";
                        return;
                    }
                    baseService.post("/dataTab/deleteByTabCode", {
                        code: $scope.obj.object.tab_code
                    }).then();
                    //if($scope.obj.object.id != undefined){
                    //    baseService.post("/dataTab/getTabCodeById", {
                    //        id: $scope.obj.object.id
                    //    }).then(
                    //        function(data){
                    //            $scope.tabCodes=data.result;
                    //        }
                    //    );
                    //    baseService.post("/dataTab/deleteByTabCode", {
                    //        code: $scope.tabCodes[0].tab_code
                    //    })
                    //}
                    $scope.obj.object.arr = $scope.data_col;
                    $scope.obj.object.delArr = $scope.deleteArr;
                    baseService.saveOrUpdate($scope.obj.object.id,$scope.obj.object).then(
                        function (data) {
                            $scope.data = data.result;
                            if ($scope.data == "true") {
                                baseService.alert("添加类型名称成功！");
                                getPage();
                                getPage2();
                                $scope.close();
                            } else if($scope.data == "false"){
                                baseService.alert("添加类型名称失败！");
                            }else if($scope.data == "error") {
                                baseService.alert("服务器链接失败！");
                            }else if($scope.data == "checkNameError"){
                                baseService.alert("类型名称已存在！");
                            }
                        }
                    )
                };

                /*更新方法*/
                $scope.data_col=new Array();
                $scope.update=function(l){
                    //传参数l
                    $scope.obj.object = angular.copy(l);
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.data_col=new Array();
                    //查询所有复活条件的数据
                    baseService.post("/dataTab/getTabColByTabCode", {
                        code: l.tab_code
                    }).then(
                        function (data) {
                            $scope.data_col = data.result;
                            //$scope.data_col = angular.copy(l).data_col;
                            for(var i = 0;i<$scope.data_col.length;i++){
                                for(var j =0;j<$scope.gridOptions2.data.length;j++){
                                    if($scope.gridOptions2.data[j].col_code == $scope.data_col[i].col_code){
                                        $scope.gridApi.selection.selectRow($scope.gridOptions2.data[j]);
                                    }
                                }
                            }
                            $scope.openRight();
                        }
                    )

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
                            url: window.ServerURL+'/dataTab/open',
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
                        name: 'tab_name',
                        displayName: '数据类型名称',
                        minWidth: 300,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'del_flg', displayName: '状态', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellFilter: 'stateFilter:grid.options.del_flg'},
                    {
                        name: '操作', minWidth:200,headerCellClass:'headerCss',
                        cellTemplate: '<div><md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'编辑权限\',\'BUTTON\') && row.entity.del_flg==1"  aria-label=”修改” disabled><md-tooltip md-autohide>修改</md-tooltip></md-icon>'+
                        '<md-icon md-svg-src="images/icons/padlock.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.open(row.entity)" ng-if="grid.appScope.checkAuth(\'删除权限\',\'BUTTON\')" aria-label=”状态”><md-tooltip md-autohide>状态</md-tooltip></md-icon></div>'
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
                    enableSorting: true,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions2.pageSize,
                    columnDefs: [
                        {
                            name: 'rownum',width:80,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                        },
                        {
                            name: 'col_name',
                            displayName: '数据名称',
                            headerCellClass:'headerCss',
                            suppressRemoveSort: false,
                            enableSorting: false,
                            enablePinning: false
                        }
                    ],
                onRegisterApi: function( gridApi ) {
                        $scope.gridApi = gridApi;
                        gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
                            for(var i=0;i<rows.length;i++){
                                if(rows[i].isSelected){
                                    $scope.data_col.push({col_code:rows[i].entity.col_code});
                                }else{
                                    $scope.data_col = new Array();
                                    break;
                                }
                            }
                        });
                        gridApi.selection.on.rowSelectionChanged($scope,function(row){
                            if(row.isSelected){
                                var flag = true;
                                for(var i=0;i< $scope.data_col.length;i++){
                                    if(row.entity.col_code == $scope.data_col[i].col_code){
                                        flag = false;
                                        break;
                                    }else{
                                        $scope.deleteArr = delArr($scope.deleteArr,i);
                                        flag = true;
                                    }
                                }
                                if(flag){
                                    $scope.data_col.push({col_code:row.entity.col_code});
                                }
                            }else{
                                $scope.deleteArr.push({col_code:row.entity.col_code})
                                for(var i=0;i< $scope.data_col.length;i++){
                                    if(row.entity.col_code == $scope.data_col[i].col_code){
                                        $scope.data_col = delArr($scope.data_col,i);
                                    }
                                }
                            }
                        });
                        $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                            if (sortColumns.length == 0) {
                                $scope.paginOpt2.sort = null;
                            } else {
                                $scope.paginOpt2.sort = sortColumns[0].sort.direction;
                            }
                            getPage2();
                        });
                        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                            //$scope.paginOpt2.pageNumber = newPage;
                            //$scope.paginOpt2.pageSize = pageSize;
                            //getPage2();
                        });
                        //$scope.gridApi.core.on.sortChanged( $scope, function( grid, sort ) {
                        //    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
                        //})
                    }
                };
                getPage2();

                function getPage2() {
                    baseService.findSubAllWithParams($scope.obj.searchObj1).then(
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