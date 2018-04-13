/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';
    directives
    .directive('stringFormat', ['$filter',function($filter) {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {

                function formatter(value) {
                    return value + "Ԫ";
                }
                function parser() {
                    return ctrl.$modelValue;
                }
                ctrl.$formatters.push(formatter);
                ctrl.$parsers.unshift(parser);
            }
        };
    }]);
});
