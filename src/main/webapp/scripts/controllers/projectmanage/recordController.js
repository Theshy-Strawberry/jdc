/**
 * Created by zhangyongliang on 15/9/20.
 */
/**
 * @ngdoc function
 * @name codeApp.controller:projectManagementCtrl
 * @description
 * # projectManagementCtrl
 * Controller of the codeApp
 */

define([
    './../module'
], function (controllers) {
    'use strict';

    controllers
        .controller('TabRecordCTRL', [
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
            'baseService',
            '$q',
            '$mdToast',
            '$filter',
            'i18nService',
            function ($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, baseService, $q, $mdToast, $filter, i18nService) {
                //=========================Init start==================================
                //国际化
                i18nService.setCurrentLang('zh-cn');
                baseService.init($scope, {
                    name: "tabrecord"
                });
                baseService.actionName.name="tabrecord";
                $scope.localAddress = window.ServerURL;
                //$scope.obj = {
                //    object:{},
                //    searchObj:{
                //        tabCode:"",
                //        projectId:"",
                //        col_name:"",
                //        col_tel:"",
                //        col_source:"",
                //        col_channel:"",
                //        col_store:"",
                //        col_vcode:""
                //    }
                //};
                $scope.columns = [
                    {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false},
                    {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false}
                ];

                $scope.columns2 = [
                    {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false},
                    {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false}
                ];
                $scope.obj = {searchObj:{}};
                //$scope.paginationOptions = {
                //    pageNumber: 1,
                //    pageSize: 20,
                //    sort: null
                //};
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
                $scope.permission = {};
                $scope.searchObj = {
                    tabCode:"",
                    projectId:"",
                    col_name:"",
                    col_tel:"",
                    col_source:"",
                    col_channel:"",
                    col_store:"",
                    col_vcode:"",
                    page:$scope.paginationOptions2.pageNumber,
                    pageSize:$scope.paginationOptions2.pageSize

                };
                $scope.channelInfos = [];
                $scope.stores = [];
                $scope.searchChannel = [];
                $scope.searchStores = [];
                $scope.tabName = "详细内容";
                $scope.showType = "pro";

                $scope.curTabType = "tab";

                $scope.tabCode = "";
                $scope.projectId = "";

                $scope.hideTab = false;
                $scope.openTab = openTab;
                $scope.closeTab = closeTab;
                $scope.editTab = function () {
                    $scope.hideTab = !$scope.hideTab;
                }

                $scope.sysUsersInfo = {};
                $scope.userTabs = [];
                $scope.tabCnt = {};

                //$scope.columns = [];
                //$scope.columns2 = [];

                $scope.treeFilter = $filter('uiTreeFilter');

                $scope.toggleSupport = function (propertyName) {
                    return ['title'].indexOf(propertyName) > -1 ?
                        ['title'].splice(['title'].indexOf(propertyName), 1) :
                        ['title'].push(propertyName);
                };

                baseService.post("/index/getUser", {}).then(
                    function (data) {
                        $scope.sysUsersInfo = data.result;
                    }
                )

                baseService.post("/tabrecord/getUserTabCnt", {}).then(function(data){
                    $scope.tabCnt = data.result;
                })

                $scope.rm = function (node) {
                    baseService.deleteByMethod("tabrecord/delete", node).then(
                        function (data) {
                            if (data.result == "error") {
                                baseService.alert("系统异常，请稍后重试！");
                            } else {
                                if (data.result) {
                                    baseService.alert("删除数据成功！");
                                } else {
                                    baseService.alert("系统异常，请稍后重试！");
                                }
                                //getTabs();
                                $mdDialog.hide();
                            }
                        }
                    )
                };

                $scope.toggle = function (scope) {
                    scope.toggle();
                };


                function getUserTabs() {
                    baseService.post("/tabrecord/getUserTabs", {projectId:$scope.projectId}).then(
                        function (data) {
                            $scope.userTabs = data.result;
                            return data.result.length;
                        }
                    )
                }


                function delArr(arr, index) {
                    return arr.slice(0, index).concat(arr.slice(index + 1, arr.length));
                }

                $scope.search2 = function () {
                    getTabPage();
                }

                //================================================================

                baseService.post("/tabrecord/getTabPermission", {}).then(
                    function (data) {
                        $scope.permission = data.result;
                    }
                )


                //$scope.columns = [
                //    {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss'},
                //    {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false},
                //    {name: 'col_name',displayName: '姓名',width: 100,headerCellClass:'headerCss',enableCellEdit: true},
                //    {name: 'col_tel',displayName: '电话',width: 100,headerCellClass:'headerCss',enableCellEdit: true},
                //    {name: 'col_source',displayName: '客户来源',width: 100,headerCellClass:'headerCss',enableCellEdit: true},
                //    {name: 'col_channel', displayName: '渠道公司', minWidth: 200,headerCellClass:'headerCss',enableCellEdit: true,
                //        editableCellTemplate: 'ui-grid/dropdownEditor',
                //        editDropdownValueLabel: 'channel_name',
                //        editDropdownRowEntityOptionsArrayPath:'channelInfos',
                //        cellFilter: 'channelInfoFilter:grid.options.channelInfos'
                //    },
                //    {name: 'col_store', displayName: '门店', minWidth: 200,headerCellClass:'headerCss',enableCellEdit: true,
                //        editableCellTemplate: 'ui-grid/dropdownEditor',
                //        editDropdownValueLabel: 'store_name',
                //        editDropdownRowEntityOptionsArrayPath:'storeList',
                //        cellFilter: 'storeInfoFilter:grid.options.stores'
                //    },
                //    {name: 'col_vcode',displayName: '访客单编号',width: 100,headerCellClass:'headerCss',enableCellEdit: true}
                //];

                $scope.tabCols = [];
                $scope.updateCols = [];
                //$scope.lockedCols = [];
                var lockedCols = [];
                var pMap = {};
                var tmpProjectId = "";
                var tmpTabCode = "";
                $scope.initCol = function (tabCode, projectId) {

                    //$scope.columns = [
                    //    {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false},
                    //    {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false}
                    //];
                    //
                    //$scope.columns2 = [
                    //    {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false},
                    //    {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false}
                    //];

                    var cnt1 = $scope.columns.length;
                    var cnt2 = $scope.columns2.length;
                    if ($scope.columns.length > 2) {
                        $scope.columns = $scope.columns.slice(0, 2);
                    }
                    if ($scope.columns2.length > 2) {
                        $scope.columns2 = $scope.columns2.slice(0, 2);
                    }
                    baseService.post("/tabrecord/getUpdateColList", {tabCode: tabCode, projectId: projectId}).then(
                        function (data) {
                            $scope.updateCols = data.result;
                            lockedCols = data.result;
                            pMap = $scope.permission;

                            baseService.post("/tabrecord/getUserTabCols", {tabCode: tabCode,projectId: projectId}).then(
                                function (data) {
                                    $scope.tabCols = data.result;

                                    if (data.result.length > 0) {
                                        for(var i=0;i<data.result.length;i++){

                                            if (data.result[i]['filed'] == "col_channel") {
                                                $scope.columns.push({
                                                    name: data.result[i]['filed'],
                                                    displayName: data.result[i]['filed_name'],
                                                    minWidth: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    editableCellTemplate: 'ui-grid/dropdownEditor',
                                                    editDropdownValueLabel: 'channel_name',
                                                    editDropdownRowEntityOptionsArrayPath:'channelInfos',
                                                    cellFilter: 'channelInfoFilter:grid.options.channelInfos',
                                                    //cellClass:'red'
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        //if($scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        //    && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                        //    cssName = 'red';
                                                        //}
                                                        if($scope.updateCols[row.entity.id][col.name]['isLocked'] == 1
                                                            && !$scope.permission[$scope.projectId][$scope.tabCode][5]) {
                                                            cssName = 'gray';
                                                        }
                                                        return cssName;
                                                    },
                                                    cellEditableCondition: function($scope) {
                                                        if ($scope.row.entity.id == "") {
                                                            return true;
                                                        }
                                                        if ($scope.row.entity.is_submit == 1) {
                                                            return false;
                                                        }
                                                        if(lockedCols[$scope.row.entity.id][$scope.col.colDef.name]['isLocked'] == 1
                                                            && !pMap[projectId][tabCode][5]) {
                                                            return false;
                                                        }
                                                        return true;
                                                    }
                                                });

                                                $scope.columns2.push({
                                                    name: data.result[i]['filed'],
                                                    displayName: data.result[i]['filed_name'],
                                                    minWidth: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    editableCellTemplate: 'ui-grid/dropdownEditor',
                                                    editDropdownValueLabel: 'channel_name',
                                                    editDropdownRowEntityOptionsArrayPath:'channelInfos',
                                                    cellFilter: 'channelInfoFilter:grid.options.channelInfos',
                                                    //cellClass:'red'
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        //if($scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        //    && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                        //    cssName = 'red';
                                                        //}
                                                        return cssName;
                                                    }
                                                });
                                            }
                                            else if (data.result[i]['filed'] == "col_store") {
                                                $scope.columns.push({
                                                    name: data.result[i]['filed'],
                                                    displayName: data.result[i]['filed_name'],
                                                    minWidth: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    editableCellTemplate: 'ui-grid/dropdownEditor',
                                                    editDropdownValueLabel: 'store_name',
                                                    editDropdownRowEntityOptionsArrayPath:'storeList',
                                                    cellFilter: 'storeInfoFilter:grid.options.stores',
                                                    //cellClass:'red'
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        if($scope.updateCols[row.entity.id][col.name]['isLocked'] == 1
                                                            && !$scope.permission[projectId][tabCode][5]) {
                                                            cssName = 'gray';
                                                        }
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        //if($scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        //    && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                        //    cssName = 'red';
                                                        //}
                                                        return cssName;
                                                    },
                                                    cellEditableCondition: function($scope) {
                                                        if ($scope.row.entity.id == "") {
                                                            return true;
                                                        }
                                                        if ($scope.row.entity.is_submit == 1) {
                                                            return false;
                                                        }
                                                        if(lockedCols[$scope.row.entity.id][$scope.col.colDef.name]['isLocked'] == 1
                                                            && !pMap[projectId][tabCode][5]) {
                                                            return false;
                                                        }
                                                        return true;
                                                    }
                                                });
                                                $scope.columns2.push({
                                                    name: data.result[i]['filed'],
                                                    displayName: data.result[i]['filed_name'],
                                                    minWidth: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    editableCellTemplate: 'ui-grid/dropdownEditor',
                                                    editDropdownValueLabel: 'store_name',
                                                    editDropdownRowEntityOptionsArrayPath:'storeList',
                                                    cellFilter: 'storeInfoFilter:grid.options.stores',
                                                    //cellClass:'red'
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        //if($scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        //    && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                        //    cssName = 'red';
                                                        //}
                                                        return cssName;
                                                    }
                                                });

                                            }
                                            else {
                                                $scope.columns.push({
                                                    name:data.result[i]['filed'],
                                                    displayName:data.result[i]['filed_name'],
                                                    width: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    //cellClass:'red'
                                                    //editableCellTemplate:'<input type="string" uiSelect2EditableCellTemplate style="width:100%;height: 100%" ng-class="\'colt\' + col.index" ng-input="COL_FIELD" ng-model="COL_FIELD"/>',
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        //$scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        if($scope.updateCols[row.entity.id][col.name]['isLocked'] == 1
                                                            && !$scope.permission[$scope.projectId][$scope.tabCode][5]) {
                                                            cssName = 'gray';
                                                        }
                                                        return cssName;
                                                    },
                                                    cellEditableCondition: function($scope) {
                                                        if ($scope.row.entity == null || $scope.row.entity == undefined) {
                                                            return true
                                                        }
                                                        if ($scope.row.entity.id == "") {
                                                            return true;
                                                        }
                                                        if ($scope.row.entity.is_submit == 1) {
                                                            return false;
                                                        }
                                                        if(lockedCols[$scope.row.entity.id][$scope.col.colDef.name]['isLocked'] == 1
                                                            && !pMap[projectId][tabCode][5]) {
                                                            return false;
                                                        }
                                                        return true;
                                                    }
                                                });

                                                $scope.columns2.push({
                                                    name:data.result[i]['filed'],
                                                    displayName:data.result[i]['filed_name'],
                                                    width: 150,
                                                    headerCellClass:'headerCss',
                                                    enableCellEdit: true,
                                                    //cellClass:'red'
                                                    cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                                        var cssName = '';
                                                        if (row == undefined || row.entity == undefined) {
                                                            return cssName;
                                                        }
                                                        if (row.entity.id == "") {
                                                            return cssName;
                                                        }
                                                        if(row.entity.is_submit == 1 && row.entity[col.name] != ""
                                                            && $scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                            && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                            cssName = 'red';
                                                        }
                                                        //if($scope.updateCols[row.entity.id][col.name]['isUpdated'] == 1
                                                        //    && $scope.updateCols[row.entity.id][col.name]['isLocked'] == 0) {
                                                        //    cssName = 'red';
                                                        //}
                                                        return cssName;
                                                    }
                                                })
                                            }

                                        }

                                        $scope.columns.push({
                                            name: '操作', pinnedRight: true, minWidth: 150,headerCellClass:'headerCss',cellClass:'cellCss',
                                            cellTemplate: '<div>' +
                                            '<md-icon md-svg-src="images/icons/ic_person_24px.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.saveData(row.entity,0)" ng-if="grid.appScope.checkAuth(row.entity.projectId,row.entity.tabCode,2)&&(row.entity.is_submit==0 || row.entity.is_submit==undefined)" aria-label=”保存”><md-tooltip md-autohide>保存</md-tooltip></md-icon>' +
                                            '<md-icon md-svg-src="images/icons/ic_comment_24px.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.saveData(row.entity,1)" ng-if="grid.appScope.checkAuth(row.entity.projectId,row.entity.tabCode,7)&&(row.entity.is_submit==0 || row.entity.is_submit==undefined) && (!grid.appScope.checkAuth(row.entity.projectId,row.entity.tabCode,5))" aria-label=”提交”><md-tooltip md-autohide>提交</md-tooltip></md-icon>' +
                                            '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)" ng-if="grid.appScope.checkAuth(row.entity.projectId,row.entity.tabCode,3)&&(row.entity.is_submit==0 || row.entity.is_submit==undefined)" aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                                            '</div>'
                                        });
                                        $scope.columns2.push({
                                            name: '操作', pinnedRight: true, minWidth: 150,headerCellClass:'headerCss',cellClass:'cellCss',
                                            cellTemplate: '<div>' +
                                            '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.saveData(row.entity,3)" ng-if="grid.appScope.checkAuth(row.entity.projectId,row.entity.tabCode,5)" aria-label=”审核”><md-tooltip md-autohide>审核</md-tooltip></md-icon>' +
                                            '</div>'
                                        });


                                        baseService.post("/tabrecord/getChannelList", {}).then(
                                            function (channels) {
                                                $scope.channelInfos = channels.result;
                                                $scope.gridOptions2.channelInfos = channels.result;
                                                $scope.gridOptions3.channelInfos = channels.result;
                                            }
                                        )
                                        baseService.post("/tabrecord/getStoreList", {}).then(
                                            function (stores) {
                                                $scope.stores = stores.result;
                                                $scope.gridOptions2.stores = stores.result;
                                                $scope.gridOptions3.stores = stores.result;
                                            }
                                        )
                                        $scope.gridOptions2.columnDefs = $scope.columns;
                                        $scope.gridOptions3.columnDefs = $scope.columns2;
                                        getTabPage();
                                    }


                                }
                            )
                        }
                    )
                }

                $scope.gridOptions2 = {
                    //enableRowSelection: true,
                    //expandableRowHeight: 1500,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions2.pageSize,
                    //useExternalPagination: true,
                    //useExternalSorting: true,
                    enableCellEditOnFocus: true,
                    enableSorting: false,
                    columnDefs: $scope.columns,
                    //paginOpt: $scope.paginationOptions2,
                    onRegisterApi: function( gridApi ) {
                        $scope.gridApi = gridApi;
                        $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                            if (sortColumns.length == 0) {
                                $scope.gridOptions2.paginOpt.sort = null;
                            } else {
                                $scope.gridOptions2.paginOpt.sort = sortColumns[0].sort.direction;
                            }
                            //getPage();
                        });
                        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                            //$scope.gridOptions2.paginOpt.pageNumber = newPage;
                            //$scope.gridOptions2.paginOpt.pageSize = pageSize;
                            $scope.searchObj.page = newPage;
                            $scope.searchObj.pageSize = pageSize;
                            //getTabPage();
                        });
                        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, coldef, newValue, oldvalue) {
                            if (coldef.name === 'col_channel') {
                                baseService.post("/tabrecord/getStoreByChannelId", {channelId: newValue}).then(
                                    function (data) {
                                        rowEntity.storeList = data.result;
                                    }
                                )
                            }
                        });
                    }};

                $scope.gridOptions3 = {
                    //enableRowSelection: true,
                    //expandableRowHeight: 1500,
                    paginationPageSizes: [10, 20, 50],
                    paginationPageSize: $scope.paginationOptions3.pageSize,
                    //useExternalPagination: true,
                    //useExternalSorting: true,
                    enableCellEditOnFocus: true,
                    enableSorting: false,
                    columnDefs: $scope.columns2,
                    //paginOpt: $scope.paginationOptions2,
                    onRegisterApi: function( gridApi ) {
                        $scope.gridApi = gridApi;
                        //$scope.gridApi.core.refresh();
                        $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                            if (sortColumns.length == 0) {
                                $scope.gridOptions3.paginOpt.sort = null;
                            } else {
                                $scope.gridOptions3.paginOpt.sort = sortColumns[0].sort.direction;
                            }
                            //getPage();
                        });
                        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                            //$scope.gridOptions2.paginOpt.pageNumber = newPage;
                            //$scope.gridOptions2.paginOpt.pageSize = pageSize;
                            $scope.searchObj.page = newPage;
                            $scope.searchObj.pageSize = pageSize;
                            //getTabPage();
                        });
                        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, coldef, newValue, oldvalue) {
                            if (coldef.name === 'col_channel') {
                                baseService.post("/tabrecord/getStoreByChannelId", {channelId: newValue}).then(
                                    function (data) {
                                        rowEntity.storeList = data.result;
                                    }
                                )
                            }
                        });

                    }};


                $scope.addProgram = function () {
                    var dataStruct = {
                        dataindex : $scope.gridOptions2.data.length,
                        id : "",
                        projectId: $scope.projectId,
                        tabCode: $scope.tabCode
                    };
                    for (var i = 0; i < $scope.tabCols.length; i++) {
                        dataStruct[$scope.tabCols[i]['filed']] = "";
                    }
                    $scope.gridOptions2.data.push(dataStruct);

                    for(var i=0;i<$scope.gridOptions2.data.length;i++){
                        $scope.gridOptions2.data[i].channelInfos = $scope.channelInfos;
                    }
                    //baseService.post("/tabrecord/getUpdateColList", {tabCode: $scope.tabCode, projectId: $scope.projectId}).then(function(data){
                    //    lockedCols = data.result;
                    //})

                }

                $scope.export = function(){
                    baseService.post("/tabrecord/export", {projectId:$scope.projectId, tabCode:$scope.tabCode, tabType:$scope.curTabType}).then(function(data) {
                            window.open(window.ServerURL + "/" + data.result);
                        }
                )}


                var originatorEv;
                $scope.para = {};
                $scope.openMenu = function ($mdOpenMenu, ev, scope, node) {
                    document.body.oncontextmenu = function () {
                        return false;
                    }
                    //scope.toggle();
                    $scope.tabCode = node.tabCode;
                    $scope.tabName = node.title;
                    //$scope.projectId = 1;
                    //$scope.para = {tabCode: node.tabCode, projectId: $scope.projectId};
                    $scope.searchObj.tabCode = node.tabCode;
                    $scope.searchObj.projectId = $scope.projectId;
                    if ($scope.permission[$scope.projectId][$scope.tabCode][5]) {
                        $scope.curTabType = "examine";
                    }
                    $scope.initCol(node.tabCode, $scope.projectId);


                    //if (ev.button == 2) {
                    //    originatorEv = ev;
                    //    $mdOpenMenu(ev);
                    //} else if (ev.button == 0) {
                    //
                    //}

                };
                $scope.changeTab = function(tabType) {
                    //alert(tabType);
                    $scope.curTabType = tabType;
                    $scope.searchObj.col_name = "";
                    $scope.searchObj.col_tel = "";
                    $scope.searchObj.col_source = "";
                    $scope.searchObj.col_channel = "";
                    $scope.searchObj.col_store = "";
                    $scope.searchObj.col_vcode = "";
                    getTabPage();
                }
                function getTabPage() {
                    $scope.searchObj["tabType"] = $scope.curTabType;
                    baseService.post("/tabrecord/getTabRecord", $scope.searchObj).then(
                        function (data) {
                            //$scope.gridOptions.totalItems = data.result.totalRow;
                            $scope.gridOptions2.data = [];
                            $scope.gridOptions3.data = [];
                            for(var i=0;i<data.result.list.length;i++){
                                var storeArr = [];
                                data.result.list[i].channelInfos = $scope.channelInfos;
                                if (data.result.list[i]['col_channel'] != undefined && data.result.list[i]['col_channel'] != "") {
                                    for (var j = 0; j < $scope.stores.length; j++) {
                                        if ($scope.stores[j]['channel_id'] == data.result.list[i]['col_channel']) {
                                            storeArr.push($scope.stores[j]);
                                        }
                                    }
                                    data.result.list[i].storeList = storeArr;
                                }
                            }
                            if ($scope.curTabType == "tab") {
                                $scope.gridOptions2.data = data.result.list;
                            }
                            if ($scope.curTabType == "examine") {
                                $scope.gridOptions3.data = data.result.list;
                            }
                            baseService.post("/tabrecord/getUpdateColList", $scope.searchObj).then(function(data){
                                lockedCols = data.result;
                            })
                        }
                    )
                }
                $scope.announceClick = function (index) {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .title('You clicked!')
                            .content('You clicked the menu item at index ' + index)
                            .ok('Nice')
                            .targetEvent(originatorEv)
                    );
                    originatorEv = null;
                };
                $scope.showConfirm = function (l) {

                    if (l.id != "" && l.id != undefined && l.id != null) {
                        var confirm = $mdDialog.confirm()
                            .title('提示信息')
                            .content('是否删除此项纪录?')
                            .ok('确认')
                            .cancel('取消')
                            .targetEvent();
                        $mdDialog.show(confirm).then(function () {
                            baseService.post("/tabrecord/delRecord", {id: l.id}).then(
                                function (data) {
                                    $scope.data = data.result;
                                    if ($scope.data == "success") {
                                        baseService.alert("删除成功！");
                                        getTabPage();
                                    }
                                    else {
                                        baseService.alert("error！");
                                    }
                                }
                            )
                        }, function () {
                        });

                    }
                    else {
                        $scope.gridOptions2.data = delArr($scope.gridOptions2.data, l.dataindex);
                        for(var i=0;i<$scope.gridOptions2.data.length;i++){
                            $scope.gridOptions2.data[i].dataindex = i;
                        }

                    }

                };

                $scope.checkUserTab = function (projectId) {
                    $scope.projectId = projectId;
                    if ($scope.tabCnt[projectId] > 0) {
                        return true;
                    }
                    return false;
                }



                $scope.saveData = function (dataObj, flg) {
                    var req = {};
                    dataObj['tabCode'] = $scope.tabCode;
                    dataObj['projectId'] = $scope.projectId;
                    dataObj['dataStatus'] = flg;
                    dataObj['tabType'] = $scope.tabType;
                    dataObj.channelInfos = null;
                    dataObj.storeList = null;
                    //var paras = {dataObj:dataObj, tabCode: $scope.tabCode, projectId: $scope.projectId}
                    req = {
                        method: 'POST',
                        url: window.ServerURL + "/tabrecord/saveData",
                        params:  dataObj
                    };
                    $http(req).success(function (data) {
                        if(data.result=="success"){
                            if(flg == 0) {
                                baseService.alert("保存成功！");
                            }
                            if(flg == 1) {
                                baseService.alert("提交成功！");
                            }
                            if(flg == 3) {
                                baseService.alert("审核成功！");
                            }

                            //$scope.close();
                            baseService.post("/tabrecord/getUpdateColList", {tabCode: $scope.tabCode, projectId: $scope.projectId}).then(
                                function(data){
                                    $scope.updateCols = data.result;
                                    lockedCols = data.result;
                                    getTabPage();
                                }
                            )

                            //$scope.initCol(node.tabCode, $scope.projectId);

                        }else{
                            baseService.alert("保存失败！");
                        }
                    }).error(function () {
                        baseService.alert("系统异常请稍后重试");
                    })
                }

                baseService.post("/tabrecord/getChannelList", {}).then(
                    function(data){
                        $scope.searchChannel=data.result;

                    }
                );

                $scope.$watch('searchObj.col_channel', function(scope){
                    baseService.post("/tabrecord/getStoreByChannelId",{
                        channelId:$scope.searchObj.col_channel
                    }).then(
                        function(data){
                            $scope.searchStore=data.result;
                        }
                    )
                });

                //baseService.post("/tabrecord/getUpdateColList", {tabCode: tabCode, projectId: projectId})
                //================================================================



                //================================================================

                function closeTab() {
                    $timeout(function () {
                        $mdSidenav('tabLeft').close();
                    });
                }

                function openTab() {
                    $timeout(function () {
                        $mdSidenav('tabLeft').open();
                    });
                }



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
                    //{name: 'second_party_id', displayName: '乙方名称', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'effective_begin_date', displayName: '开始日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'effective_end_date', displayName: '截止日期', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'first_party_person', displayName: '甲方联系人', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {name: 'first_party_person_telephone', displayName: '甲方联系方式', minWidth: 150,headerCellClass:'headerCss',cellClass:'cellCss'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 120,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/ic_visibility_24px.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.toTab(row.entity.id)" ng-if="grid.appScope.checkUserTab(row.entity.id)"  aria-label=”明细”><md-tooltip md-autohide>明细</md-tooltip></md-icon>' +
                        //'<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
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

                /*分页*/
                function getPage() {
                    baseService.findAllWithParams($scope.obj.searchObj).then(
                        function (data) {
                            $scope.gridOptions.totalItems = data.result.totalRow;//显示多少行
                            $scope.gridOptions.data = data.result.list;//后台给前台绑定数据
                        }
                    );
                    //baseService.post("/tabrecord/findAll", $scope.obj.searchObj).then(
                    //    function(data){
                    //        $scope.gridOptions.totalItems = data.result.totalRow;//显示多少行
                    //        $scope.gridOptions.data = data.result.list;//后台给前台绑定数据
                    //    }
                    //);
                }

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

                //查询所有信息
                baseService.post("/projectBaseManagement/getUserAll", {}).then(
                    function(data){
                        //  $scope.userIds=data.result;
                        $scope.gridOptions.userIds=data.result;
                        //
                    }
                );
                baseService.post("/projectBaseManagement/getBusinessMainPartList", {}).then(
                    function (data) {
                        $scope.businessMainPart = data.result;
                        $scope.gridOptions.businessMainPart = data.result;
                    }
                )

                $scope.toTab = function(projectId) {
                    $scope.projectId = projectId;
                    $scope.showType = "tab";

                    getUserTabs();
                }
                $scope.goBack = function(projectId) {
                    $scope.projectId = "";
                    $scope.showType = "pro";
                    $scope.gridOptions2.data = [];

                    $scope.columns = [
                        {name: 'rowindex', displayName: '序号',width: 50,headerCellClass:'headerCss',cellClass:'cellCss',enableCellEdit: false},
                        {name: 'id',displayName: 'id',width: 100,enableCellEdit: false, visible: false}
                    ];
                    $scope.gridOptions2.columnDefs = $scope.columns;

                }

                $scope.checkAuth = checkAuth;
                function checkAuth(projectId, tabCode, permissionCode) {
                    if(tabCode=="") {
                        return false;
                    }
                    return $scope.permission[projectId][tabCode][permissionCode];
                }
            }
        ])

});
