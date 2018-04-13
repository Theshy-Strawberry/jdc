/**
 * Created by Tinkpad on 2016/6/13.
 */
/**
 * @ngdoc function 业务主体管理
 * @name codeApp.controller:businessMainPartCTRL
 * @description
 * # businessMainPartCTRL
 * Controller of the codeApp
 */

define([
        './../module'
    ],function(controllers){
        'use strict';

        controllers.controller('businessMainPartCTRL',[
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
                //国际化
                i18nService.setCurrentLang('zh-cn');
                //访问后台的路径名
                baseService.actionName.name="businessMainPart";
                $scope.selectData = selectData;
                //默认状态位为0
                $scope.openFlg=0;
                //添加的方法
                $scope.toggleRight=function(){
                    //把状态位改为1
                    $scope.openFlg=1;
                    $scope.obj.object = {};
                    $scope.user_form.$setPristine(true);
                    $scope.user_form.$setUntouched(false);
                    //$scope.obj.object.create_date=new Date();
                    //设置创建时间
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    //把真实姓名赋给创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    );
                    //打开右侧页面
                    $scope.openRight();

                };
                //定义打开右侧页面的方法
                $scope.openRight=baseService.buildRightToggler();

                //关闭右侧页面的方法
                $scope.close = function () {
                    baseService.closeRightToggler();
                    //getPage();
                    $scope.obj.object = {};


                };
                /*对象变量*/

                $scope.obj={
                    searchObj:{},
                    object:{}
                };
                //查询方法
                $scope.search=function(){
                    //如果创建时间不为未定义或者不为空
                    if ($scope.obj.searchObj.END$create_date != 'undefined' && $scope.obj.searchObj.END$create_date != null){
                        //获取有开始时间
                        var endDate = $scope.obj.searchObj.END$create_date;
                        //获取有结束时间
                        var startDate = $scope.obj.searchObj.START$create_date;
                        //如果开始时间大于结束时间
                        if (startDate > endDate){
                            //弹出
                            baseService.alert("结束时间必须大于等于开始时间！");
                            //返回
                            return;
                        }

                    }
                    getPage();
                };

                //查询所有信息
                baseService.post("/businessMainPart/getUserAll", {}).then(
                    function(data){
                      //  $scope.usersIds=data.result;
                          baseService.gridOptions.userIds=data.result;
                        //
                    }
                );

                /*保存或更改*/
                $scope.save=function(){
                    //验证公司名称不能为空
                    if(angular.isUndefined($scope.user_form.company_name.$modelValue)){
                        $scope.user_form.company_name.$touched = "true";
                        return;
                    }
                    //保存和更新的方法
                    baseService.saveOrUpdate($scope.obj.object.id,window.formatObj($scope.obj.object)).then(
                        function(data){
                            $scope.data=data.result;
                            if($scope.data=="success"){
                                baseService.alert("保存成功！");
                                $scope.close();
                                getPage();
                                //如果返回的是checkNameError,代表没通过重名验证
                            }else if($scope.data=="checkNameError"){
                                baseService.alert("公司名称已存在！");
                            }else{
                                baseService.alert("保存失败！");
                            }

                        }
                    )
                };


                /*更新方法*/
                $scope.update=function(l){
                    //把状态位改为1
                    $scope.openFlg=2;
                    //参数l传到对象里
                    $scope.obj.object = angular.copy(l);
                    //获取l中的开始时间转成时间类型传到前台显示出来
                    $scope.obj.object.create_date=baseService.formatDatetime(new Date());
                    //把真实姓名赋给创建人
                    baseService.getCurUser().then(
                        function(data){
                            $scope.obj.object.create_user=data.session.userSession.real_name;
                        }
                    );
                    //打开编辑页面
                    $scope.openRight();
                };
                /*下边列表栏*/
                baseService.gridOptions.columnDefs = [
                    //{
                    //    name: 'rownum_',minWidth:40,displayName: '序号',headerCellClass:'headerCss',
                    //    cellTemplate: '<div style="text-align: center;">{{row.entity.rownum}}</div>'
                    //},
                    {
                        name: 'rownum',minWidth:40,displayName: '序号',headerCellClass:'headerCss',cellClass:'cellCss'
                    },
                    {
                        name: 'company_name',
                        displayName: '公司名称',
                        minWidth: 250,
                        suppressRemoveSort: false,
                        enableSorting: false,
                        enablePinning: false,
                        headerCellClass:'headerCss'
                    },
                    {name: 'create_date', displayName: '经办时间', minWidth: 250,headerCellClass:'headerCss',cellClass:'cellCss'},
                    //{name: 'create_date', displayName: '创建时间', minWidth: 150,headerCellClass:'headerCss', cellTemplate: '<div style="text-align: center;vertical-align:middle;">{{row.entity.create_date}}</div>'},
                    {name: 'create_user', displayName: '经办人', minWidth: 250,headerCellClass:'headerCss',cellTooltip: true,cellFilter: 'userFilter:grid.options.userIds'},
                    {
                        name: '操作', pinnedRight: true, minWidth: 80,headerCellClass:'headerCss',cellClass:'cellCss',
                        cellTemplate: '<div>' +
                        '<md-icon md-svg-src="images/icons/launch.svg" style="width: 20px;height: 20px;margin-left: 20px;" ng-click="grid.appScope.update(row.entity)"  aria-label=”修改”><md-tooltip md-autohide>修改</md-tooltip></md-icon>' +
                        '<md-icon md-svg-src="images/icons/menu.svg" style="width: 20px;height: 20px;margin-left: 20px;"  ng-click="grid.appScope.showConfirm(row.entity)"  aria-label=”删除”><md-tooltip md-autohide>删除</md-tooltip></md-icon>' +
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

                //删除信息
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
            }
        ])
    }
);