/**
 * Created by Tinkpad on 2016/6/15.
 */
/**
 * @ngdoc function 门店信息
 * @name codeApp.controller:storeInfoCTRL
 * @description
 * # storeInfoCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('storeInfoCTRL',[
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
                i18nService.setCurrentLang('zh-cn');
                baseService.actionName.name="storeInfo";
                $scope.selectData = selectData;
                $scope.openFlg=0;
                $scope.toggleRight=function(){
                    $scope.openFlg=1;
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    $scope.obj.object.create_date=new Date();
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.user_name;
                        }
                    );
                    $scope.openRight();


                };
                $scope.openRight=baseService.buildRightToggler();

                $scope.close = function () {
                    baseService.closeRightToggler();
                    getPage();
                    $scope.obj.object = {};

                };
                /*对象变量*/

                $scope.obj={
                    searchObj:{},
                    object:{}
                };
                $scope.search=function(){
                    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                        if ($scope.obj.searchObj.END$create_date-$scope.obj.searchObj.START$create_date<0){
                            baseService.alert("结束时间只可选择晚于创建时间的时间！");
                            return;
                        }
                    }
                    getPage();
                };

                //查询所有医院
                baseService.post("/storeInfo/getChannelNameList", {}).then(
                    function (data) {
                        $scope.channelNames = data.result;
                        baseService.gridOptions.channelNames = data.result;
                    }
                );

                /*保存或更改*/
                $scope.save=function(){
                    if(angular.isUndefined($scope.user_form.channel_name.$modelValue)){
                        $scope.user_form.channel_name.$touched = "true";
                        return;
                    }else{
                        if(angular.isUndefined($scope.user_form.channel_id.$modelValue)){
                            $scope.user_form.channel_id.$touched = "true";
                            return;
                        }else{
                            if(angular.isUndefined($scope.user_form.channel_director.$modelValue)){
                                $scope.user_form.channel_director.$touched = "true";
                                return;
                            }else{
                                if(angular.isUndefined($scope.user_form.channel_manager.$modelValue)){
                                    $scope.user_form.channel_manager.$touched = "true";
                                    return;
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
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("本渠道下的该门店名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };
                /*更新方法*/
                $scope.update=function(l){
                    $scope.openFlg=2;
                    $scope.obj.object = angular.copy(l);
                    $scope.obj.object.modify_date=new Date();
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.modify_user=data.session.userSession.user_name;
                        }
                    );
                    $scope.openRight();
                };
                /*下侧边栏*/
                baseService.gridOptions.columnDefs = [
                    //{
                    //    name: 'rownum_',minWidth:40,displayName: '序号',headerCellClass:'headerCss',
                    //    cellTemplate: '<div style="text-align: center;">{{row.entity.rownum_}}</div>'
                    //},
                    {
                        name: 'channel_name',
                        displayName: '门店名称',
                        minWidth: 150,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'channel_id', displayName: '渠道公司', minWidth: 120,headerCellClass:'headerCss',
                        cellTooltip: true,cellFilter: 'channelIdFilter:grid.options.channelNames'},
                    {name: 'channel_director', displayName: '渠道总监', minWidth: 120,headerCellClass:'headerCss'},
                    {name: 'channel_manager', displayName: '渠道经理', minWidth: 120,headerCellClass:'headerCss'},
                    {name: 'create_date', displayName: '创建时间', minWidth: 100,headerCellClass:'headerCss', cellTemplate: '<div style="text-align: center;">{{row.entity.create_date}}</div>'},
                    {name: 'create_user', displayName: '创建人', minWidth: 100,headerCellClass:'headerCss'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 60,headerCellClass:'headerCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
                        '</div>'
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
                            $scope.gridOptions.totalItems = data.result.totalRow;
                            $scope.gridOptions.data = data.result.list;
                        }
                    );
                }





            }
        ])
    }
);