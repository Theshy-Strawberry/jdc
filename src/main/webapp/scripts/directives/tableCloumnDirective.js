/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';
    directives
        .directive('jrCloumn', function () {
            return {
                restrict: 'E',
                scope: {
                    jrModel : '=',
                },
                replace: true,        // ʹ��ģ���滻ԭʼ���
                transclude: false,    // ������ԭʼHTML����
                templateUrl: 'partials/jr-tableCloumn.tmpl.html',
                link: function (scope) {
                }
            };
        })
});
