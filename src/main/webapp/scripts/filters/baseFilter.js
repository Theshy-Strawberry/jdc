/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (filters) {
    'use strict';

    filters
        .filter('nospace', function () {
            return function (value) {
                return (!value) ? '' : value.replace(/ /g, '');
            };
        })
        .filter('humanize', function () {
            return function (doc) {
                if (!doc) return;
                if (doc.menu_type === 'directive') {
                    return doc.menu_name.replace(/([A-Z])/g, function ($1) {
                        return '-' + $1.toLowerCase();
                    });
                }
                return doc.menu_label || doc.menu_name;
            };
        })

});
