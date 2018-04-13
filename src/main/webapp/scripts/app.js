define(['routes','services/dependencyResolverFor'], function(config, dependencyResolverFor)
{
    var app = angular.module('app', [
        'ngRoute',
        'ngAnimate',
        'ngMessages',
        'door3.css',
        'ngMaterial',
        'ui.highlight',
        'ui.tree',
        'ui.tree-filter',
        'ui.grid',
        'ui.grid.autoResize',
        'ui.grid.exporter',
        'ui.grid.selection',
        'ui.grid.pinning',
        'ui.grid.resizeColumns',
        'ui.grid.expandable',
        'ui.grid.pagination',
        'ui.grid.edit',
        'ui.date',
        'highcharts-ng'
    ]);

    app.config(
        [
            '$routeProvider',
            '$locationProvider',
            '$controllerProvider',
            '$compileProvider',
            '$filterProvider',
            '$provide',
            '$mdThemingProvider',

            function($routeProvider, $locationProvider, $controllerProvider, $compileProvider, $filterProvider, $provide,$mdThemingProvider)
            {
                app.controller = $controllerProvider.register;
                app.directive  = $compileProvider.directive;
                app.filter     = $filterProvider.register;
                app.factory    = $provide.factory;
                app.service    = $provide.service;

                //$locationProvider.html5Mode(true);

                if(config.routes !== undefined)
                {
                    angular.forEach(config.routes, function(route, path)
                    {
                        $routeProvider.when(path, {templateUrl:route.templateUrl, resolve:dependencyResolverFor(route.dependencies)});
                    });
                }

                if(config.defaultRoutePaths !== undefined)
                {
                    $routeProvider.otherwise({redirectTo:config.defaultRoutePaths});
                }
                $mdThemingProvider.theme('docs-dark', 'default')
                    .primaryPalette('yellow')
                    .dark();
            }
        ])
        .constant(
            "selectData", {
                projectKinds: [/**项目分类**/
                    {id: 1, text: '常规'},
                    {id: 2, text: '专项'}
                ],
                auditTypes: [/**审计方式**/
                    {id: 1, text: '现场审计'},
                    {id: 2, text: '远程审计'},
                    {id: 3, text: '远程审计现场审计'}
                ],
                projectProgresses: [/**审计状态**/
                    {id: 1, text: '尚未开展'},
                    {id: 2, text: '审前准备'},
                    {id: 3, text: '审计实施'},
                    {id: 4, text: '撰写报告'},
                    {id: 5, text: '审理阶段'},
                    {id: 6, text: '征求被审计'},
                    {id: 7, text: '报公司领导阶段'},
                    {id: 8, text: '报公司其他领导阶段'},
                    {id: 9, text: '报其他相关部门阶段'},
                    {id: 10, text: '下达意见书'},
                    {id: 11, text: '完结'}
                ],
                projectSources: [/**项目来源**/
                    {id: 1, text: '计划中'},
                    {id: 2, text: '年结转'},
                    {id: 3, text: '计划外'}
                ],
                projectDivides: [/**项目归属**/
                    {id: 1, text: '基建'},
                    {id: 2, text: '财务'},
                    {id: 3, text: '服务采购'}
                ],
                isPublics: [/**是否上市**/
                    {id: 1, text: '上市'},
                    {id: 2, text: '未上市'}
                ],
                reportTypes: [/**报告类型**/
                    {id: 1, text: '周报'},
                    {id: 2, text: '月报'}
                ],
                writeTypes: [/**写实类型**/
                    {id: 1, text: '个人'},
                    {id: 2, text: '汇总'}
                ],
                isReduce: [/**有无审减**/
                    {id: 1, text: '无'},
                    {id: 2, text: '有'}
                ],
                engineeringTypes: [/**工程性质**/
                    {id: 1, text: '合同类'},
                    {id: 2, text: '结算类'}
                ],
                handleTypes: [/**操作类型**/
                    {id: 1, text: '新增'},
                    {id: 2, text: '编辑'},
                    {id: 3, text: '删除'}
                ],
                projectTypes: [/**项目类型**/
                    {id: 1, text: '项目类'},
                    {id: 2, text: '合同类'},
                    {id: 3, text: '工程类'}
                ],
                isDist: [/**是否分配**/
                    {id: 1, text: '未分配'},
                    {id: 2, text: '已分配'}
                ]
            }
        )

    return app;
});