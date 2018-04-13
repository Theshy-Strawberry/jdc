/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    'app'
], function (app) {
    'use strict';
    app.factory('common', [
        function () {
            return {
                formatObj : function(obj){
                    for(var p in obj){
                        if(obj[p] instanceof Date){
                            obj[p] = obj[p].getFullYear() + "-" +(obj[p].getMonth() + 1)+ "-" +obj[p].getDate();
                        }
                    }
                    return obj;
                }
            };
        }])
});
