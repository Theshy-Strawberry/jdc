/**
 * bootstraps angular onto the window.document node
 */
define([
    'require',
    'angular',
    'jquery',
    'jquery-ui',
    './app',
    './routes'
], function (require, ng) {
    'use strict';
    /**
     * use domReady RequireJS plugin to make sure that DOM is ready when we start the app.
     * Note that before doing so we’re loading the app.js dependency,
     * in there the main application is defined.
     */
    require(['domReady!'], function (document) {
        ng.bootstrap(document, ['app']);
    });
});