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
                replace: true,        // ʹ��ģ���滻ԭʼ���
                transclude: false,    // ������ԭʼHTML����
                templateUrl: 'partials/jr-searchButton.tmpl.html',
                controller: function($scope, $element){

                },
                link: function (scope,element) {

                }
            };
        })
});
