/**
 * @ngdoc function 用户管理
 * @name codeApp.controller:SysUserManagementCTRL
 * @description
 * # SysUserManagementCTRL
 * Controller of the codeApp
 */

define([
    './../module'
], function (controllers) {
    'use strict';

    controllers.controller('SysUserManagementCTRL',[
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
        function ($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, $filter, uiGridConstants, baseService, $q, $mdToast, i18nService,selectData) {
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
            i18nService.setCurrentLang('zh-cn');
            baseService.actionName.name = "sysUser";
            $scope.selectData = selectData;
            //$scope.toggleRight = baseService.buildRightToggler();
            $scope.toggleRight = function(){
                $scope.userform.$setPristine(true);
                $scope.userform.$setUntouched(false);
                $scope.openRight();
                $scope.userInfo = new Object();
            };
            $scope.openRight = baseService.buildRightToggler();
            $scope.close = function () {
                baseService.closeRightToggler();
                $scope.userInfo = {}
            };
            $scope.userInfo = {};
            $scope.obj = {
                searchObj: {},
                object: {}
            };

            $scope.search = function(){
                getPage();
            };

            $scope.save = function () {

                if (angular.isUndefined($scope.userform.user_name.$modelValue)){
                    $scope.userform.user_name.$touched = "true";
                    return;
                } else{
                    if (angular.isUndefined($scope.userform.real_name.$modelValue)){
                        $scope.userform.real_name.$touched = "true";
                        return;
                    } else{
                        if (!$scope.userform.$valid) {
                            return;
                        }
                    }
                }

                baseService.saveOrUpdate($scope.userInfo.id, $scope.userInfo).then(
                    function (data) {
                        $scope.data = data.result;

                        if ($scope.data == "true") {
                            baseService.alert("添加用户成功！");
                            getPage();
                            $scope.close();
                        } else if($scope.data == "checkNameError"){
                            baseService.alert("用户登陆名已存在！");
                        }else if($scope.data == "checkNoError"){
                            baseService.alert("内部编号已存在！");
                        }else{
                            baseService.alert("添加用户失败！");
                        }
                    }
                )
            };

            $scope.update = function (l) {
                $scope.userInfo = l;
                $scope.openRight();
            }

            baseService.post("/sysUser/getOfficeInfoList", {}).then(
                function (data) {
                    $scope.officeInfos = data.result;
                    baseService.gridOptions.officeInfos = data.result;
                }
            );

            baseService.post("/sysUser/getJobTitleList", {}).then(
                function (data) {
                    $scope.jobTitles = data.result;
                    baseService.gridOptions.jobTitles = data.result;
                }
            )

            baseService.gridOptions.columnDefs = [
                //{
                //    name: 'rownum_', minWidth: 60,displayName: '序号',headerCellClass:'headerClass',
                //    cellTemplate: '<div style="text-align: center;">{{row.entity.rownum_}}</div>'
                //},
                {
                    name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                },
                {
                    name: 'user_name',
                    displayName: '用户名称',
                    headerCellClass:'headerCss',
                    minWidth: 120,
                    suppressRemoveSort: false,
                    enableSorting: false,
                    enablePinning: false
                },
                {name: 'real_name', displayName: '真实姓名', minWidth: 120, headerCellClass:'headerCss'},
                {name: 'email', displayName: '邮箱', minWidth: 120, headerCellClass:'headerCss'},
                {name: 'telephone', displayName: '电话', minWidth: 120,headerCellClass:'headerCss'},
                ,
                {name: 'del_flg', displayName: '状态', minWidth: 100,headerCellClass:'headerCss',cellClass:'cellCss',
                    cellFilter: 'stateFilter:grid.options.del_flg'},
                {
                    name: '操作', pinnedRight: true, minWidth: 80,headerCellClass:'headerCss',
                    cellTemplate: '<div><md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 30%;"  ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'编辑权限\',\'BUTTON\')"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                    '<md-icon md-svg-src="images/icons/padlock.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.open(row.entity)" ng-if="grid.appScope.checkAuth(\'删除权限\',\'BUTTON\')"  aria-label=”状态”><md-tooltip md-autohide>状态</md-tooltip></md-icon>'+
                    '<md-icon md-svg-src="images/icons/padlock.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.reset(row.entity)" aria-label=”重置”><md-tooltip md-autohide>重置</md-tooltip></md-icon></div>'
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

            //$scope.showConfirm = function (l) {
            //    baseService.deleteById(l.id).then(
            //        function (data) {
            //            $scope.data = data.result;
            //            if ($scope.data == "true") {
            //                baseService.alert("删除成功！");
            //                getPage();
            //            }
            //            else {
            //                baseService.alert("error！");
            //            }
            //        }
            //    )
            //};

            //修改状态(启用/停用)
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
                        url: window.ServerURL+'/sysUser/open',
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

            //重置密码
            $scope.reset = function (l) {
                var confirm = $mdDialog.confirm()
                    .title('提示信息')
                    .content('是否重置密码?')
                    .ariaLabel('Lucky day')
                    .ok('确认')
                    .cancel('取消')
                    .targetEvent();
                $mdDialog.show(confirm).then(function () {
                    var req = {
                        method: 'POST',
                        url: window.ServerURL+'/sysUser/resetPwdById',
                        params: {
                            id: l.id
                        }
                    };
                    $http(req).success(function (data) {
                        $scope.data = data.result;
                        if ($scope.data == "true") {
                            baseService.alert("重置成功！");
                            getPage();
                        }else if($scope.data == "false"){
                            baseService.alert("error！");
                        }
                    })
                }, function () {
                });
            };


            function getPage() {
                baseService.findAllWithParams($scope.obj.searchObj).then(
                    function (data) {
                        $scope.gridOptions.totalItems = data.result.totalRow;
                        $scope.gridOptions.data = data.result.list;
                    }
                );
            };
        }
    ]);
});
