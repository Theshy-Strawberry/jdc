/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (directives) {
    'use strict';

    directives
        .directive('jrNavigation', function () {
            return {
                scope: {
                    first: '@',
                    second: '@'
                },
                templateUrl: 'partials/jr-navigation.tmpl.html',
                link: function ($scope, $element) {

                }
            };
        })

});
