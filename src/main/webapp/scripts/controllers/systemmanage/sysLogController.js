/**
 * @ngdoc function 系统日志
 * @name codeApp.controller:SysLogCTRL
 * @description
 * # SysLogCTRL
 * Controller of the idea_auditapp
 */

define([
    './../module'
], function (controllers) {
    'use strict';

    controllers.controller('SysLogCTRL', [
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
        function ($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, $filter, uiGridConstants, baseService, $q, $mdToast, i18nService, selectData) {
            i18nService.setCurrentLang('zh-cn');
            $scope.selectData = selectData;
            baseService.init($scope, {
                name: "sysLog"
            });
            $scope.handleUsers = {};
            //查询登陆用户的真实姓名
            baseService.post("/sysLog/getUserInfo", {}).then(
                function (data) {
                    $scope.handleUsers = data.result;
                    $scope.gridOptions.handleUsers = data.result;
                }
            );
            $scope.obj = {
                searchObj: {},
                object: {},
                progress :{mode : 'determinate'},
                QueryOpt :{type :false,name : "关闭"}
            };
            getPage();

            $scope.toggleRight = baseService.buildRightToggler();

            $scope.queryClose = function (){
                $scope.obj.QueryOpt.type = !$scope.obj.QueryOpt.type;
                if($scope.obj.QueryOpt.type){
                    $scope.obj.QueryOpt.name = "展开";
                }else{
                    $scope.obj.QueryOpt.name = "关闭";
                }
            }

            $scope.showThis = function (v) {
                $scope.selectId = v;
            };
            $scope.close = function () {
                getPage();
                $scope.obj.object = {}
                baseService.closeRightToggler();
            };

            $scope.search = function () {
                $scope.obj.progress.mode = "query";
                getPage();
            }

            baseService.post("/sysLog/getUserNameList", {}).then(
                function (data) {
                    $scope.handleUsers = data.result;
                    baseService.gridOptions.handleUsers = data.result;
                }
            )

            baseService.post("/sysLog/getModuleNamesList", {}).then(
                function (data) {
                    $scope.moduleNames = data.result;
                    baseService.gridOptions.moduleNames = data.result;
                }
            )


            baseService.gridOptions.columnDefs = [
                {
                    name: 'rownum',minWidth:50,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                },
                //{
                //    name: 'project_id',
                //    displayName: '项目编号',
                //    enablePinning: false,
                //    width: 200,
                //    enableColumnResizing: false
                //},
                {
                    name: 'handle_type',
                    displayName: '操作类型',
                    width: 300,
                    cellTooltip: true,
                    cellFilter: 'logFilter:grid.options.handle_type',
                    headerCellClass:'headerCss'
                },
                {
                    name: 'module_name',
                    displayName: '模块名称',
                    enablePinning: false,
                    width: 300,
                    enableColumnResizing: false,
                    headerCellClass:'headerCss'
                },
                {
                    name: 'handle_content',
                    displayName: '操作内容',
                    enablePinning: false,
                    width: 300,
                    enableColumnResizing: false,
                    headerCellClass:'headerCss'
                },
                //{name: 'handle_type', displayName: '操作类型', minWidth: 200},
                {
                    name: 'handle_user',
                    displayName: '操作用户',
                    minWidth: 200,
                    cellTooltip: true,
                    cellFilter: 'userInfoFilter:grid.options.handleUsers',
                    headerCellClass:'headerCss'
                },
                {
                    name: 'handle_date',
                    displayName: '操作时间',
                    minWidth: 200,
                    headerCellClass:'headerCss',
                    cellClass:'cellCss'
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

                //gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                //    if (row.isExpanded) {
                //        row.entity.subGridOptions = {
                //            id: row.entity.id,
                //            sub: {},
                //            init: function () {
                //                row.entity.subGridOptions = row.entity;
                //            }
                //        }
                //    }
                //});
            };
            $scope.gridOptions = baseService.gridOptions;

            function getPage() {
                baseService.findAllWithParams($scope.obj.searchObj).then(
                    function (data) {
                        $scope.gridOptions.totalItems = data.result.totalRow;
                        $scope.gridOptions.data = data.result.list;
                        $scope.obj.progress.mode = "determinate";
                    }
                );
            };
        }
    ])
});
