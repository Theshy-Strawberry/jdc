/**
 * @ngdoc overview
 * @name codeApp
 * @description
 * # codeApp
 *
 * Main module of the application.
 * We agreed to have 4 modules by files types:
 *                                            controllers
 *                                            directives
 *                                            filters
 *                                            services
 * we require these modules to be loaded before defining the main module.
 */

define([
    'angular',
    'jquery',
    'jquery-ui',
    'angular-css',
    'angular-route',
    'angular-aria',
    'angular-animate',
    'angular-material',
    'angular-message',
    'angular-ui-tree',
    'angular-ui-grid',
    'angular-ui-date',
    './scripts/filters/index',
    './scripts/directives/index',
    './scripts/services/index',
    './scripts/controllers/index'
], function (ng) {
    'use strict';
    return ng.module('app', [
        'ngRoute',
        'ngAnimate',
        'ngMessages',
        'door3.css',
        'ngMaterial',
        'app.filters',
        'app.directives',
        'app.services',
        'app.controllers',
        'ui.tree',
        'ui.grid',
        'ui.grid.autoResize',
        'ui.grid.exporter',
        'ui.grid.selection',
        'ui.grid.pinning',
        'ui.grid.resizeColumns',
        'ui.grid.expandable',
        'ui.grid.pagination',
        'ui.grid.edit',
        'ui.date'
    ]);
});
