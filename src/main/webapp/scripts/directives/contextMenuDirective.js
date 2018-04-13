/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';

    directives
        .directive('contextMenu', function ($window) {
            return {
                restrict: 'A',
                //require:'^?ngModel',

                link: function ($scope, element, attrs) {

                    var opened = false;

                    var menuElement = angular.element(document.getElementById(attrs.target));

                    function open(event, element) {
                        $scope.opened = true;
                        menuElement.css('top', event.clientY + 'px');
                        menuElement.css('left', event.clientX + 'px');
                    };
                    function close(element) {
                        $scope.opened = false;
                    };
                    $scope.opened = false;
                    //每个项点击的事件
                    $scope.fns = {
                        "查看": function ($event) {
                            alert('LOOK');
                        },
                        "刷新": function ($event) {
                            alert('刷新')
                        },
                        "点击": function ($event) {
                            alert('点击')
                        }
                    }
                    //模拟数据填充菜单   数据可通以点击元素过属性传递过来
                    //菜单的html 结构
                    //<ul id='a'><li ng-repeat='m in ms'>{{m.name}}</li></ul>
                    $scope.ms = $scope.model;
                    $scope.fn = function ($event, sName) {
                        /*
                         * 根据sName 来判断使用什么函数
                         */
                        $scope.fns[sName]($event);
                    }
                    //显示右键菜单
                    element.bind('contextmenu', function (event) {
                        $scope.$apply(function () {
                            event.preventDefault();
                            open(event, menuElement);
                        });
                    });
                    //窗口绑定点击事件 隐藏右键菜单
                    angular.element($window).bind('click', function (event) {
                        if ($scope.opened) {
                            $scope.$apply(function () {
                                event.preventDefault();
                                close(menuElement);
                            });
                        }
                    });
                }
            }
        })

});


//===============
//<div context-menu
//target='a'
//ng-init='model=[{name:"查看"},{name:"刷新"},{name:"点击"}]'>hello</div>
//
//  <ul id='a' ng-show='opened'>
//  <li ng-repeat='m in ms' ng-click='fn($event,m.name)'>{{m.name}}</li>
//</ul>
//=====================
