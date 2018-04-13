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
        .controller('SysRootCTRL', [
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
            i18nService.setCurrentLang('zh-cn');
            baseService.init($scope, {
                name: "sysmenu"
            });
            $scope.obj = {
                menu: 2
            }

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
            baseService.post("/sysauth/getUserList", {}).then(
                function (data) {
                    $scope.sysUsersList = data.result;
                }
            )

           // getTreeNodes();
            //6getBaseMenu
            function getTreeNodes(){
                baseService.post("/sysmenu/getRootMenu", {}).then(
                    function (data) {
                        $scope.data = data.result;
                    }
                )
            }

            $scope.$watch('todoUser', function(scope) {
                if(!angular.isUndefined(scope)){
                    baseService.post("/sysmenu/getBaseMenu", {user_name:$scope.todoUser}).then(
                        function (data) {

                            //for(var i=0;i<data.result.length;i++){
                            //    var baseNode = data.result[i];
                            //    for(var j=0;j<baseNode.nodes.length;j++){
                            //        var secondNode = baseNode.nodes;
                            //        for(var k=0;k < secondNode.length;k++){
                            //            var auth = secondNode[k].nodes;
                            //            for(var m=0;m < auth.length;m++){
                            //                auth[m].selected = false;
                            //            }
                            //        }
                            //    }
                            //}
                            for(var i=0;i<data.result.length;i++){
                                var baseNode = data.result[i];
                                    baseNode.selected = false;
                                if(baseNode.bakNodes.length>0){
                                    baseNode.selected = true;
                                }
                                for(var j=0;j<baseNode.nodes.length;j++){
                                    var secondNode = baseNode.nodes;
                                    secondNode[j].selected = false;
                                    for(var k=0;k < secondNode.length;k++){
                                        var userAuth = secondNode[k].bakNodes;
                                        var Baseauth = secondNode[k].nodes;

                                        for(var n=0;n < Baseauth.length;n++){
                                            Baseauth[n].selected = false;
                                            for(var m=0;m < userAuth.length;m++){
                                                if(Baseauth[n].id == userAuth[m][0])
                                                Baseauth[n].selected = true;
                                            }
                                        }

                                    }
                                }
                            }
                            $scope.data = data.result;
                            console.log($scope.data);
                        }
                    )
                }
            });

            $scope.getAuth = function (scope, node) {
                if(angular.isUndefined($scope.todoUser)){
                    baseService.alert("请选择待分配权限用户!");
                    return;
                }

                baseService.post("/sysauth/getNodeAuth", {menu_id: node.id}).then(
                    function (data) {
                        var baseAuths;
                        for(var i=0;i<data.result.length;i++){
                            data.result[i].selected = false;
                        }
                        baseAuths = data.result;
                        baseService.post("/sysauth/getUserAuthList", {
                            user_name: $scope.todoUser,
                            menu_id: node.id
                        }).then(
                            function (data) {
                                for(var j=0;j<data.result.length;j++){
                                    for(var i=0;i<baseAuths.length;i++){
                                        if(baseAuths[i].id == data.result[j][0]){
                                            baseAuths[i].selected = true;
                                        }
                                    }
                                }
                                $scope.auths = baseAuths;
                            }
                        )
                    }
                )

            };
            //================================================================

            $scope.selected = [];
            $scope.toggleAll = function (root,item) {
                if(!item.selected){
                    for(var i=0;i<item.nodes.length;i++){
                        //$scope.toggle(item[i]);
                        console.log(item.nodes[i]);
                        if(item.nodes[i].selected == false){
                            item.nodes[i].selected = true;
                            baseService.post("/sysauth/saveUserAuth", {
                                user_name: $scope.todoUser,
                                auth_id : item.nodes[i]['authId']
                            }).then(
                                function (data) {
                                    console.log("saveUserAuth====="  + data.result);

                                }
                            )
                        }
                    }
                }else{
                    for(var i=0;i<item.nodes.length;i++){
                        //$scope.toggle(item[i]);
                        if(item.nodes[i].selected == true){
                            item.nodes[i].selected = false;
                            baseService.post("/sysauth/deleteUserAuth", {
                                user_name: $scope.todoUser,
                                auth_id : item.nodes[i].authId
                            }).then(
                                function (data) {
                                    console.log("saveUserAuth====="  + data.result);
                                }
                            )
                        }
                    }
                }
            }
            $scope.toggle = function (item) {
                if(angular.isUndefined($scope.todoUser)){
                    baseService.alert("请选择待分配权限用户!");
                    return;
                }
                if(!item.selected){
                    baseService.post("/sysauth/saveUserAuth", {
                        user_name: $scope.todoUser,
                        auth_id : item.authId
                    }).then(
                        function (data) {
                            console.log("saveUserAuth====="  + data.result);
                            //item.selected = !item.selected;
                        }
                    )
                }else{
                    baseService.post("/sysauth/deleteUserAuth", {
                        user_name: $scope.todoUser,
                        auth_id : item.authId
                    }).then(
                        function (data) {
                            //item.selected = !item.selected;
                            console.log("deleteUserAuth====="  + data.result);
                        }
                    )
                }
            };

        }
    ])

});
