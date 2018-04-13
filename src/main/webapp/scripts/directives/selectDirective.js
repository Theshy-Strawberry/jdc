/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';

    directives
        .directive('jrSelect', function () {
            return {
                restrict: 'E',
                scope: {
                    labelName: '@',
                    jrDatas: '=',
                    showValue : '@',
                    showText : '@',
                    jrModel : '=',
                },
                replace: true,        // ʹ��ģ���滻ԭʼ���
                transclude: false,    // ������ԭʼHTML����
                templateUrl: 'partials/jr-select.tmpl.html',
                link: function (scope) {
                    scope.$watch('jrModel',function (){
                        if(scope.jrModel==undefined){
                            scope.jrModel = "";
                        }
                    })
                }
            };
        })

});
