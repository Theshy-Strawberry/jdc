/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';
    directives
        .directive('dateFormat', ['$filter', function ($filter) {
            var dateFilter = $filter('date');
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {

                    function formatter(value) {
                        return dateFilter(value, 'yyyy-MM-dd HH:mm:ss'); //format
                    }

                    function parser() {
                        return ctrl.$modelValue;
                    }

                    ctrl.$formatters.push(formatter);
                    ctrl.$parsers.unshift(parser);
                }
            };
        }])
        .directive('timeFormat', ['$filter', function ($filter) {
            var dateFilter = $filter('date');
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {

                    function formatter(value) {
                        return dateFilter(value, 'HH:mm:ss'); //format
                    }

                    function parser() {
                        return ctrl.$modelValue;
                    }

                    ctrl.$formatters.push(formatter);
                    ctrl.$parsers.unshift(parser);
                }
            };
        }])
});
