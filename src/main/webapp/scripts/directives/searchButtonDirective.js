/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';
    directives
        .directive('jrSearchButton', function () {
            return {
                restrict: 'E',
                scope: {
                    jrModel : '=',
                },
                replace: true,        // 使用模板替换原始标记
                transclude: false,    // 不复制原始HTML内容
                templateUrl: 'partials/jr-searchButton.tmpl.html',
                controller: function($scope, $element){

                },
                link: function (scope,element) {

                }
            };
        })
});
