/**
 * Created by Tinkpad on 2016/6/17.
 */
/**
 * @ngdoc function 地域管理
 * @name codeApp.controller:sysRegionalManagementCTRL
 * @description
 * # SysUserManagementCTRL
 * Controller of the codeApp
 */

define([
    './../module'
], function (controllers) {
    'use strict';

    controllers.controller('sysRegionalManagementCTRL',[
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
        function ($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope, $mdUtil, $log, $http, $filter, uiGridConstants, baseService, $q, $mdToast, i18nService) {
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
            baseService.actionName.name = "sysRegionalManagement";
            $scope.level = 1;
            $scope.sysRegionalManagementInfo = {};
            $scope.obj = {
                searchObj: {},
                object: {}
            };
            $scope.toggleRight = function(){
                //baseService.alert($scope.sysRegionalManagementInfo);
                $scope.sysRegionalManagementInfo={};
                /* $scope.openRight();
                 return;*/
                //获取父辈code
                var supperCode = $scope.obj.searchObj.supperCode;
                //判断父辈code是否为null



                if($scope.reg_level==null ||$scope.reg_level==-1){
                    $scope.starForm = "";
                }else{
                    if(supperCode!=null){
                        //如果父辈code为3的话则当前为城市列表
                        if(supperCode.length==3){
                            $scope.starForm = "true";
                            $scope.sysRegionalManagementInfo.is_star = 1;
                        }else{
                            $scope.starForm = "";
                        }
                    }
                }

                //{$scope.sysRegionalManagementInfo={};

                $scope.sysregionalmanagementform.$setPristine(true);
                $scope.sysregionalmanagementform.$setUntouched(false);
                //$scope.starForm = false;

                $scope.openRight();
            };

            $scope.openRight = baseService.buildRightToggler();



            $scope.close = function () {

                $scope.starForm = "";
                getPage($scope.reg_level);
                $scope.sysRegionalManagementInfo = {};
                baseService.closeRightToggler();
            };



            $scope.search = function(){

                getPage($scope.reg_level);
            };

            $scope.save = function () {


                var name = $scope.sysregionalmanagementform.name.$modelValue;
                if(angular.isUndefined(name)){
                    $scope.sysregionalmanagementform.name.$touched = "true";
                    return ;

                }

                $scope.sysRegionalManagementInfo.reg_level = $scope.reg_level;
                baseService.saveOrUpdate($scope.sysRegionalManagementInfo.id,$scope.sysRegionalManagementInfo).then(
                    function (data){
                        $scope.data = data.result;
                        if($scope.data == "success"){
                            baseService.alert("保存成功！");
                            $scope.close();
                        }
                    }
                )

            };

            $scope.update = function (l) {

                $scope.sysRegionalManagementInfo = angular.copy(l);

                $scope.openRight();
            };


            //地域是否启用
            $scope.editEnabled = function(l,is_enabled){
                var msg = '';
                if(is_enabled==1){
                    msg = '是否停用此项纪录?';
                }else{

                    msg = "是否启用此项纪录?";
                }
                var confirm = $mdDialog.confirm()
                    .title('提示信息')
                    .content(msg)
                    .ok('确认')
                    .cancel('取消')
                    .targetEvent();
                $mdDialog.show(confirm).then(function () {

                    if(is_enabled==1){
                        l.is_enabled=2;
                        msg = '是否停用此项纪录?';
                    }else{
                        l.is_enabled=1;
                        msg = "是否启用此项纪录?";
                    }
                    baseService.post("/sysRegionalManagement/editEnabled",l).then(
                        function(data){

                            baseService.alert("保存成功！");
                            getPage($scope.reg_level);
                        }
                    );

                });




            };



            //显示详情
            $scope.particulars = function(l){
                if(l.is_enabled==2){
                    baseService.alert("该数据状态为停用，请启用后进行详情查看！");
                    return ;
                }

                /**
                 * 设置查找类型
                 * up为向上查找
                 * down为向下查找
                 * @type {string}
                 */
                $scope.obj.searchObj.selectType = "down";
                $scope.level = $scope.level+1;

                if(l!=null){

                    getPage(l.id);

                    $scope.obj.searchObj.supperCode = l.code;
                    if($scope.level==1){
                        $scope.provinceName = "";
                    }else if($scope.level==2){
                        $scope.provinceName = "/"+l.name;
                        $scope.cityName = "";
                        $scope.districtName = ""
                    }else if($scope.level==3){
                        $scope.cityName = "/"+l.name;

                        $scope.districtName = ""
                    }else if($scope.level==4){
                        $scope.districtName = "/"+l.name;
                    }



                }else{
                    getPage(-1);
                }


            };

            //返回上级页面
            $scope.returnSupperPage =  function(){
                var regLevel = Number($scope.reg_level);
                $scope.level = $scope.level-1;
                if(!angular.isUndefined($scope.reg_level)){

                    /**
                     * 设置查找类型
                     * up为向上查找
                     *
                     */

                    $scope.obj.searchObj.selectType = "up";

                    getPage(regLevel);

                }
            };

            //设置buttonform中的隐藏标签标识的值
            $scope.setButtonFormRegLevel = function(regLevel){
                // baseService.alert($scope.reg_level);
                $scope.reg_level = regLevel;

            };






            baseService.gridOptions.columnDefs = [
                {
                    name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss',width:50
                },
                {
                    name: 'name',
                    displayName: '地域名称',
                    minWidth: 100,
                    headerCellClass:'headerCss',
                    suppressRemoveSort: false,
                    enableSorting: false,
                    enablePinning: false
                },
                //{name: 'longitude', displayName: '经度',headerCellClass:'headerCss', minWidth: 100},
                //{name: 'latitude', displayName: '纬度',headerCellClass:'headerCss', minWidth: 100},
                //{name: 'order_number', displayName: '地域等级',headerCellClass:'headerCss', minWidth: 100},
                {
                    name: '操作',pinnedRight: true, headerCellClass:'headerCss', width: 140,cellClass:'cellCss',
                    cellTemplate: '<div><md-icon md-svg-src="images/icons/file.svg"  style="width: 20px;height: 20px;margin-left: 20px;"  ng-if="row.entity.code.length!=13" ng-click="grid.appScope.particulars(row.entity)"  aria-label=”详情”><md-tooltip md-autohide>详情</md-tooltip></md-icon>'+
                    '<md-icon md-svg-src="images/icons/padlock.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-if="row.entity.is_enabled == 2" ng-click="grid.appScope.editEnabled(row.entity,row.entity.is_enabled)" aria-label=”启用”><md-tooltip md-autohide>启用</md-tooltip></md-icon>'+
                    '<md-icon md-svg-src="images/icons/padlock.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-if="row.entity.is_enabled == 1" ng-click="grid.appScope.editEnabled(row.entity,row.entity.is_enabled)"  aria-label=”停用”><md-tooltip md-autohide>停用</md-tooltip></md-icon>'+
                    '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)" ng-if="grid.appScope.checkAuth(\'删除权限\',\'BUTTON\')" aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon></div>'
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

            getPage(-1);

            $scope.showConfirm = function (l) {
                baseService.deleteById(l.id).then(
                    function (data) {
                        $scope.data = data.result;
                        if ($scope.data == "true") {
                            baseService.alert("删除成功！");
                            getPage($scope.reg_level);
                        }
                        else {
                            baseService.alert("error！");
                        }
                    }
                )
            };

            function getPage(regLevel) {

                if(regLevel !=null){
                    $scope.obj.searchObj.regLevel = regLevel;
                }else{
                    $scope.obj.searchObj.regLevel = -1;
                }

                baseService.findAllWithParams($scope.obj.searchObj).then(
                    function (data) {

                        //设置buttonform中的隐藏标签标识的值
                        $scope.setButtonFormRegLevel(data.regLevel);

                        $scope.gridOptions.totalItems = data.result.totalRow;
                        $scope.gridOptions.data = data.result.list;
                        if($scope.reg_level==-1){
                            $scope.retureturnSupperPagernShow="";
                        }else{
                            $scope.retureturnSupperPagernShow="true";
                        }

                        if($scope.obj.searchObj.selectType == "up"){
                            $scope.obj.searchObj.supperCode = data.supperCode;
                            if($scope.level==1){
                                $scope.provinceName = "";
                            }else if($scope.level==2){
                                $scope.provinceName = "/"+data.supperName;
                                $scope.cityName = "";
                                $scope.districtName = ""
                            }else if($scope.level==3){
                                $scope.cityName = "/"+data.supperName;
                                $scope.districtName = ""
                            }else if($scope.level==4){
                                $scope.districtName = "/"+data.supperName;
                            }

                        }
                        $scope.obj.searchObj.selectType = null;


                    }
                );

            }




        }
    ]);
});
