var allTestFiles = [];
var TEST_REGEXP = /(spec|test)\.js$/i;

// Get a list of all the test files to include
Object.keys(window.__karma__.files).forEach(function (file) {
    if (TEST_REGEXP.test(file)) {
        // Normalize paths to RequireJS module names.
        // If you require sub-dependencies of test files to be loaded as-is (requiring file extension)
        // then do not normalize the paths
        var normalizedTestModule = file.replace(/^\/base\/|\.js$/g, '');
        allTestFiles.push(normalizedTestModule);
    }
});
requirejs.config({
    // Karma serves files under /base, which is the basePath from your config file
    baseUrl: '/base',

    //Comment out the optimize line if you want
    //the code minified by Closure Compiler using
    //the "simple" optimizations mode
    paths: {
        // BASE SCRIPT
        'domReady': 'lib/requirejs-domready/domReady',
        'angular': 'lib/angular/angular',
        'jquery': 'lib/jquery/jquery.min',
        'jquery-ui': 'lib/jquery-ui/jquery-ui.min',
        'angular-animate': 'lib/angular-animate/angular-animate',
        'angular-aria': 'lib/angular-aria/angular-aria',
        'angular-material': 'lib/angular-material/1.1/angular-material',
        'angular-cookies': 'lib/angular-cookies/angular-cookies',
        'angular-resource': 'lib/angular-resource/angular-resource',
        'angular-route': 'lib/angular-route/angular-route',
        'angular-sanitize': 'lib/angular-sanitize/angular-sanitize',
        'angular-touch': 'lib/angular-touch/angular-touch',
        'angular-css': 'lib/angular-css/angular-css',
        'angular-message': 'lib/angular-message/angular-messages.min',
        'angular-ui-tree': 'lib/angular-ui-tree/dist/angular-ui-tree',
        'angular-ui-grid': 'lib/angular-ui-grid/ui-grid.min',
        'angular-ui-date': 'lib/angular-ui-date/date',
        'app' : 'scripts/conf/karmaApp'
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
        'angular-ui-tree': {
            deps: ['angular']
        },
        'angular-ui-grid': {
            deps: ['angular']
        },
        'angular-message': {
            deps: ['angular']
        },
        'app': {
            deps: ['angular', 'jquery', 'angular-route', 'angular-animate', 'angular-aria', 'angular-css',
                'angular-ui-tree', 'angular-ui-grid', 'angular-message'],
            exports: 'app'
        }
    },
    // dynamically load all test files
    deps: allTestFiles,

    // we have to kickoff jasmine, as it is asynchronous
    callback: window.__karma__.start
});

