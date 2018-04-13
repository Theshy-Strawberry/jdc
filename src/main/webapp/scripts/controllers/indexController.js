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

    controllers.controller('IndexCtrl', [
        '$scope',
        '$mdSidenav',
        '$timeout',
        '$mdDialog',
        'menu',
        '$location',
        '$rootScope',
        'baseService',
        '$log',
        function ($scope, $mdSidenav, $timeout, $mdDialog, menu, $location, $rootScope,baseService, $log) {
            var self = this;

            $scope.leftTitle = 'Senyoboss';
            $scope.hideMenu = false;
            $scope.sysUsersInfo = {};
            $scope.editMenu = function () {

                $scope.hideMenu = !$scope.hideMenu;
                $timeout(
                    function() {
                        angular.element(document.getElementsByClassName('grid')[0]).css('height',  '300px');
                    },
                    300
                );
            }
            $scope.searchVal = "";

            $scope.$watch('searchVal', function() {
                $log.info($scope.searchVal);
            });

            $scope.toPath = function (val) {
                if(val == 1){
                    $location.path("/edituserinfo");
                }else if(val == 2){
                    $location.path("/changePwd");
                }else if(val == 3){
                    $location.path("/dashborad");
                }
            }

            baseService.post("/index/getUser", {}).then(
                function (data) {
                    $scope.sysUsersInfo = data.result;
                }
            )
            $scope.logout = function(){
                baseService.post("/login/logout", {}).then(
                    function (data) {
                        if(data.result == "error"){
                            baseService.alert("系统异常,请重试!");
                        }else{
                            if(data.result == "success"){
                                $scope.sysUsersInfo = undefined;
                                window.location.href=window.ServerURL + "/login.html#/";
                                window.event.returnValue = false;
                            }else{
                                baseService.alert("系统异常,请重试!");
                            }
                        }
                    }
                )
            }

            $scope.menu = menu;

            $scope.path = path;
            $scope.goHome = goHome;
            $scope.openMenu = openMenu;
            $scope.closeMenu = closeMenu;
            $scope.isSectionSelected = isSectionSelected;

            $rootScope.$on('$locationChangeSuccess', openPage);
            $scope.focusMainContent = focusMainContent;

            // Methods used by menuLink and menuToggle directives
            this.isOpen = isOpen;
            this.isSelected = isSelected;
            this.toggleOpen = toggleOpen;
            this.autoFocusContent = false;


            var mainContentArea = document.querySelector("[role='main']");

            // *********************
            // Internal methods
            // *********************

            function closeMenu() {
                $timeout(function () {
                    $mdSidenav('left').close();
                });
            }

            function openMenu() {
                $timeout(function () {
                    $mdSidenav('left').open();
                });
            }

            function path() {
                return $location.path();
            }

            function goHome($event) {
                menu.selectPage(null, null);
                $location.path('/');
            }

            function openPage() {
                $scope.closeMenu();
                baseService.post("/index/getUser", {}).then(
                    function (data) {
                        $scope.sysUsersInfo = data.result;
                        if(data.result == undefined || data.result =="undefined"){
                            window.location.href=window.ServerURL + "/login.html#/dashborad";
                            window.event.returnValue = false;
                        }else{
                            if (self.autoFocusContent) {
                                focusMainContent();
                                self.autoFocusContent = false;
                            }
                        }
                    }
                )
            }

            function focusMainContent($event) {
                // prevent skip link from redirecting
                if ($event) {
                    $event.preventDefault();
                }

                $timeout(function () {
                    //mainContentArea.focus();
                }, 90);

            }

            function isSelected(page) {
                return menu.isPageSelected(page);
            }

            function isSectionSelected(section) {
                var selected = false;
                var openedSection = menu.openedSection;
                if (openedSection === section) {
                    selected = true;
                }
                else if (section.children) {
                    section.children.forEach(function (childSection) {
                        if (childSection === openedSection) {
                            selected = true;
                        }
                    });
                }
                return selected;
            }

            function isOpen(section) {
                return menu.isSectionSelected(section);
            }

            function toggleOpen(section) {
                menu.toggleSelectSection(section);
            }

        }
    ]);

});
