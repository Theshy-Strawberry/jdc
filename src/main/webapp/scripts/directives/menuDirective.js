/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';

    directives
        .directive('menuLink', function () {
            return {
                scope: {
                    section: '='
                },
                templateUrl: 'partials/menu-link.tmpl.html',
                link: function ($scope, $element) {
                    var controller = $element.parent().controller();

                    $scope.isSelected = function () {
                        return controller.isSelected($scope.section);
                    };

                    $scope.focusSection = function () {
                        // set flag to be used later when
                        controller.autoFocusContent = true;
                        window.rootMenu = $scope.section;
                    };
                }
            };
        })

        .directive('menuToggle', function () {
            return {
                scope: {
                    section: '='
                },
                templateUrl: 'partials/menu-toggle.tmpl.html',
                link: function ($scope, $element) {
                    var controller = $element.parent().controller();

                    $scope.isOpen = function () {
                        return controller.isOpen($scope.section);
                    };
                    $scope.toggle = function () {
                        controller.toggleOpen($scope.section);
                    };

                    var parentNode = $element[0].parentNode.parentNode.parentNode;
                    if (parentNode.classList.contains('parent-list-item')) {
                        var heading = parentNode.querySelector('h2');
                        $element[0].firstChild.setAttribute('aria-describedby', heading.id);
                    }
                }
            };
        })

});
