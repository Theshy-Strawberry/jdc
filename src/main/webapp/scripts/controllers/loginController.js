/**
 * @ngdoc function
 * @name codeApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the codeApp
 */

define([
  './module'
], function (controllers) {
  'use strict';

  controllers.controller('LoginCTRL', [
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
      $scope.tableType = {
        data : false
      };
      $scope.userInfo = {
      }

      $scope.login = function(){
        if($scope.userInfo.user_name == null && angular.isUndefined($scope.userInfo.user_name)){
          $mdDialog.show(
              $mdDialog.alert()
                  .clickOutsideToClose(true)
                  .title('提示信息')
                  .content('请填写用户名!')
                  .ariaLabel('提示信息')
                  .ok('确认')
          );
          return;
        }
        if($scope.userInfo.user_pwd == null && angular.isUndefined($scope.userInfo.user_pwd)){
          $mdDialog.show(
              $mdDialog.alert()
                  .clickOutsideToClose(true)
                  .title('提示信息')
                  .content('请填写密码!')
                  .ariaLabel('提示信息')
                  .ok('确认')
          );
          return;
        }
        var req = {
          method: 'POST',
          url: window.ServerURL+'/login/login',
          params:$scope.userInfo
        };
        $http(req).success(function (data) {
          if(data.result == "error"){
            baseService.alert("系统异常，请稍后重试！");
          }else{
            if(data.result == "success"){
              //$location.path('/index')
              window.location.href=window.ServerURL + "/index.html#/tabrecord";
              window.event.returnValue = false;
            }else{
              $mdDialog.show(
                  $mdDialog.alert()
                      .clickOutsideToClose(true)
                      .title('提示信息')
                      .content('用户名或密码不正确!')
                      .ariaLabel('提示信息')
                      .ok('确认')
              );
            }
          }
        })

      }


    }
  ]);

});
