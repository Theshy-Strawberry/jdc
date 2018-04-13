/**
 * @ngdoc function 修改密码
 * @name codeApp.controller:ChangePwdCTRL
 * @description
 * # ChangePwdCTRL
 * Controller of the codeApp
 */

define([
    './../module'
], function (controllers) {
    'use strict';

    controllers.controller('EditUserInfoCTRL', [
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
            baseService.post("/sysUser/getUser", {}).then(
                function (data) {
                    $scope.sysUsersInfo = data.result;
                }
            )

            baseService.post("/sysUser/getOfficeInfoList", {}).then(
                function (data) {
                    $scope.officeInfos = data.result;
                }
            )

            $scope.save = function () {
                //if (angular.isUndefined($scope.userform.email.$modelValue)){
                //    $scope.userform.email.$touched = "true";
                //    return;
                //}else{
                //    if (angular.isUndefined($scope.userform.telephone.$modelValue)){
                //        $scope.userform.telephone.$touched = "true";
                //        return;
                //    }
                //}
                if(!$scope.userform.$valid){
                    return;
                }
                baseService.post("/sysUser/update",$scope.sysUsersInfo).then(
                    function (data) {
                        if(data.result == 'true'){
                            baseService.alert("修改成功!");
                        }else if(data.result == 'checkNoError'){
                            baseService.alert("审计部内部编号已存在!");
                        }else if(data.result == 'checkNameError'){
                            baseService.alert("用户名已存在!");
                        }else{
                            baseService.alert("修改失败!");
                        }
                        baseService.post("/sysUser/getUser", {}).then(
                            function (data) {
                                $scope.sysUsersInfo = data.result;
                            }
                        )
                    }
                )
            }
        }
    ]);

});
