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

    controllers.controller('ChangePwdCTRL', [
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
            i18nService.setCurrentLang('zh-cn');
            $scope.pwd = {};
            baseService.actionName.name = "changePwd";
            $scope.showThis = function (v) {
                $scope.selectId = v;
            };
            function getPage() {
                $scope.userform.$setPristine(true);
                $scope.userform.$setUntouched(false);
            };

            $scope.save = function () {
                if (angular.isUndefined($scope.userform.user_pwd.$modelValue)){
                    $scope.userform.user_pwd.$touched = "true";
                    return;
                }else{
                    if (angular.isUndefined($scope.userform.new_user_pwd.$modelValue)){
                        $scope.userform.new_user_pwd.$touched = "true";
                        return;
                    }else{
                        if (angular.isUndefined($scope.userform.confirm_user_pwd.$modelValue)){
                            $scope.userform.confirm_user_pwd.$touched = "true";
                            return;
                        }
                    }
                }
                baseService.saveOrUpdate($scope.pwd.id, $scope.pwd).then(
                    function (data) {
                        $scope.data = data.result;
                        if ($scope.data == "true") {
                            baseService.alert("密码修改成功！");
                            $scope.pwd = {};
                            getPage();
                        }
                        else if($scope.data == "false"){
                            baseService.alert("新密码与确认密码不符！");
                        }else if($scope.data == "pwdFalse") {
                            baseService.alert("原密码错误！");
                        }
                    }
                )
            };
        }
    ]);

});
