/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (services) {
    'use strict';
    services.factory('ajaxService', [
        '$log',
        '$http',
        '$q',
        function ($log, $http, $q) {
            var self;
            return self = {
                postWithParams: function (path, para) {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + path,
                        params: para,
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                post: function (path) {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + path,
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                }
            };
        }])
});
