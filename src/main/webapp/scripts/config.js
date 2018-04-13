requirejs.config({
    //Comment out the optimize line if you want
    //the code minified by Closure Compiler using
    //the "simple" optimizations mode
    optimize: "none",
    map: {
        '*': {
            'css': '../lib/require-css/css' // or whatever the path to require-css is
        }
    },
    paths: {
        // BASE SCRIPT
        'domReady': '../lib/requirejs-domready/domReady',
        'angular': '../lib/angular/angular',
        'jquery': '../lib/jquery/jquery.min',
        'jquery-ui': '../lib/jquery-ui/jquery-ui.min',
        'angular-animate': '../lib/angular-animate/angular-animate',
        'angular-aria': '../lib/angular-aria/angular-aria',
        'angular-material': '../lib/angular-material/1.1/angular-material',
        'angular-cookies': '../lib/angular-cookies/angular-cookies',
        'angular-resource': '../lib/angular-resource/angular-resource',
        'angular-route': '../lib/angular-route/angular-route',
        'angular-sanitize': '../lib/angular-sanitize/angular-sanitize',
        'angular-touch': '../lib/angular-touch/angular-touch',
        'angular-css': '../lib/angular-css/angular-css',
        'angular-message': '../lib/angular-message/angular-messages.min',
        'angular-ui-tree': '../lib/angular-ui-tree/dist/angular-ui-tree',
        'angular-ui-grid': '../lib/angular-ui-grid/ui-grid.min',
        'angular-ui-date': '../lib/angular-ui-date/date',
        'angular-ui-tree-filter': '../lib/angular-ui-tree-filter/angular-ui-tree-filter',
        'angular-ui-utils': '../lib/angular-ui-utils/highlight.min',
        'highcharts': '../lib/highcharts/highcharts',
        'highstock': '../lib/highcharts/highstock',
        'highcharts-3D': '../lib/highcharts/highcharts-3D',
        'highcharts-ng': '../lib/highcharts/highcharts-ng',
    },
    shim: {
        'angular': {
            exports: 'angular'
        },
        'jquery': {
            exports: 'jquery'
        },
        'angular-route': {
            deps: ['angular']
        },
        'angular-animate': {
            deps: ['angular']
        },
        'angular-aria': {
            deps: ['angular']
        },
        'angular-css': {
            deps: ['angular']
        },
        'angular-ui-utils': {
            deps: ['angular']
        },
        'angular-ui-tree': {
            deps: ['angular', 'css!../lib/angular-ui-tree/dist/angular-ui-tree.min.css']
        },
        'angular-ui-tree-filter': {
            deps: ['angular']
        },
        'angular-ui-grid': {
            deps: ['angular', 'css!../lib/angular-ui-grid/ui-grid.min.css']
        },
        'angular-message': {
            deps: ['angular']
        },
        'angular-material': {
            deps: ['angular', 'angular-animate', 'angular-aria']
        },
        'jquery-ui': {
            deps: ['jquery', 'css!./jquery-ui.css'],
            exports: 'jquery-ui'
        },
        'highcharts': {
            deps: ['jquery'],
            exports: 'highcharts'
        },
        'highcharts-ng': {
            deps: ['angular','highcharts']
        },
        'highcharts-3D': {
            deps: ['highcharts']
        },
        'angular-ui-date': {
            deps: ['angular', 'jquery', 'jquery-ui', 'css!./jquery-ui.css'],
            exports: 'angular-ui-date'
        },
        'app': {
            deps: ['angular', 'jquery', 'jquery-ui', 'angular-route', 'angular-animate', 'angular-aria', 'angular-material', 'angular-css',
                'angular-ui-utils','angular-ui-tree','angular-ui-tree-filter' , 'angular-ui-grid', 'angular-ui-date', 'angular-message',
                'highcharts','highcharts-3D','highcharts-ng']
        }
    },
    // kick start application
    deps: ['../scripts/conf/bootstrap', '../scripts/conf/Common'],
    //防止读取缓存，调试用
    // urlArgs: "bust=" /+ (new Date()).getTime(),
    waitSeconds: 9000,
    modules: [
        {
            name: "main",
            exclude: ["jquery"]
        }
    ]
});

