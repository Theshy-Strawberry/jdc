define([
    './app'
], function (app) {
    // 通过返回app主模块来定义配置
    return app
        .config(['$routeProvider', '$mdThemingProvider', function ($routeProvider, $mdThemingProvider) {
            $routeProvider
                .when('/dashborad',//快捷菜单
                {
                    controller: 'DashCTRL',
                    templateUrl: './views/dashborad.html'
                })
                //===================================系统管理=============================================
                .when('/projectType',//系统管理--项目类型管理
                {
                    templateUrl: './views/systemmanage/projectType.html',
                    controller: 'ProjectTypeCTRL'
                })
                .when('/carApply',//系统管理--用车申请
                {
                    templateUrl: './views/systemmanage/carApply.html',
                    controller: 'CarApplyCTRL'
                })
                .when('/unitInfo',//系统管理--二级单位
                {
                    templateUrl: './views/systemmanage/unitInfo.html',
                    controller: 'UnitInfoCTRL'
                })
                .when('/unitBusinessType',//系统管理--业务划分
                {
                    templateUrl: './views/systemmanage/unitBusinessType.html',
                    controller: 'unitBusinessTypeCTRL'
                })
                .when('/office',//系统管理--科室管理
                {
                    templateUrl: './views/systemmanage/officeManage.html',
                    controller: 'OfficeManageCTRL'
                })
                .when('/sysUserManagement',//系统管理--用户管理
                {
                    templateUrl: './views/systemmanage/sysUserManagement.html',
                    controller: 'SysUserManagementCTRL'
                })
                .when('/sysLog',//系统管理--日志管理
                {
                    templateUrl: './views/systemmanage/sysLogManage.html',
                    controller: 'SysLogCTRL'
                })
                .when('/sysroot',//系统管理--权限管理
                {
                    templateUrl: './views/systemmanage/sysRootManage.html',
                    controller: 'SysRootCTRL'
                })
                .when('/changePwd',//系统管理--密码修改
                {
                    templateUrl: './views/systemmanage/changePwd.html',
                    controller: 'ChangePwdCTRL'
                })
                .when('/brokerageType',//系统管理--佣金类型
                {
                    templateUrl: './views/systemmanage/brokerageType.html',
                    controller: 'brokerageTypeCTRL'
                })
                .when('/businessMainPart',//系统管理--业务主体管理
                {
                    templateUrl: './views/systemmanage/businessMainPart.html',
                    controller: 'businessMainPartCTRL'
                })
                .when('/productType',//系统管理--产品类型管理
                {
                    templateUrl: './views/systemmanage/productType.html',
                    controller: 'productTypeCTRL'
                })
                .when('/dealWay',//系统管理--成交途径类型管理
                {
                    templateUrl: './views/systemmanage/dealWay.html',
                    controller: 'dealWayCTRL'
                })
                .when('/visitWay',//系统管理--到访途径类型管理
                {
                    templateUrl: './views/systemmanage/visitWay.html',
                    controller: 'visitWayCTRL'
                })
                .when('/paySubject',//系统管理--科目支出管理
                {
                    templateUrl: './views/systemmanage/paySubject.html',
                    controller: 'paySubjectCTRL'
                })
                .when('/posInfo',//系统管理--pos机终端管理
                {
                    templateUrl: './views/systemmanage/posInfo.html',
                    controller: 'posInfoCTRL'
                })
                .when('/channelInfo',//系统管理--渠道管理
                {
                    templateUrl: './views/systemmanage/channelInfo.html',
                    controller: 'channelInfoCTRL'
                })
                .when('/sysRegionalManagement',//系统管理--地域管理
                {
                    templateUrl: './views/systemmanage/sysRegionalManagement.html',
                    controller: 'sysRegionalManagementCTRL'
                })
                .when('/jobTitle',//系统管理--职位管理
                {
                    templateUrl: './views/systemmanage/jobTitle.html',
                    controller: 'jobTitleCTRL'
                })
                .when('/realEstate',//系统管理--地产商管理
                {
                    templateUrl: './views/systemmanage/realEstate.html',
                    controller: 'realEstateCTRL'
                })
                .when('/dataCol',   //系统管理--数据管理
                {
                    templateUrl: './views/systemmanage/dataCol.html',
                    controller: 'dataColCTRL'
                })
                .when('/dataTab',   //系统管理--数据类型管理
                {
                    templateUrl: './views/systemmanage/dataTab.html',
                    controller: 'dataTabCTRL'
                })
                //===================================系统管理=============================================
                //===================================项目管理=============================================
                .when('/projectBaseManagement',   //项目管理--项目基础管理
                {
                    templateUrl: './views/projectmanage/projectBaseManagement.html',
                    controller: 'projectBaseManagementCTRL'
                })
                .when('/tabrecord',   //项目管理--项目数据录入
                {
                    templateUrl: './views/projectmanage/record.html',
                    controller: 'TabRecordCTRL'
                })
                .when('/projectDataTypeManagement',   //项目管理--项目数据类型管理
                {
                    templateUrl: './views/projectmanage/projectDataTypeManagement.html',
                    controller: 'projectDataTypeManagementCTRL'
                })
                .when('/projectSubDataTypeManagement',   //项目管理--项目数据类型管理
                {
                    templateUrl: './views/projectmanage/projectSubDataTypeManagement.html',
                    controller: 'projectSubDataTypeManagementCTRL'
                })
                .when('/projectPersonnel',   //项目管理--项目人员管理
                {
                    templateUrl: './views/projectmanage/projectPersonnel.html',
                    controller: 'projectPersonnelCTRL'
                })
                //===================================项目管理=============================================
                //===================================合同管理=============================================
                .when('/projectContract',   //合同管理--渠道项目合同管理
                {
                    templateUrl: './views/ContractManagement/projectContract.html',
                    controller: 'projectContractCTRL'
                })
                .when('/frameContract',   //合同管理--渠道框架合同管理
                {
                    templateUrl: './views/ContractManagement/frameContract.html',
                    controller: 'frameContractCTRL'
                })

                //===================================合同管理=============================================
                //===================================登录=============================================
                .when('/edituserinfo',//修改用户信息
                {
                    templateUrl: './views/systemmanage/EditUserInfo.html',
                    controller: 'EditUserInfoCTRL'
                })
                .when('/login',
                {
                    templateUrl: './login.html',
                    controller: 'LoginCTRL'
                })
                //===================================登录=============================================
                .otherwise(
                {
                    redirectTo: '/'
                });
            $mdThemingProvider.theme('docs-dark', 'default')
                .primaryPalette('yellow')
                .dark();

            $mdThemingProvider.definePalette('docs-green', {
                '50': '#f1f8e9',
                '100': '#dcedc8',
                '200': '#c5e1a5',
                '300': '#aed581',
                '400': '#9ccc65',
                '500': '#8bc34a',
                '600': '#7cb342',
                '700': '#689f38',
                '800': '#558b2f',
                '900': '#33691e',
                'A100': '#ccff90',
                'A200': '#8ECD47',
                'A400': '#76ff03',
                'A700': '#64dd17',
                'contrastDefaultColor': 'dark',
                'contrastLightColors': '800 900',
                'contrastStrongLightColors': '800 900'
            });

            $mdThemingProvider.theme('default')
                .primaryPalette('indigo')
                .accentPalette('docs-green');
        }])
        .config(function ($mdIconProvider) {
            $mdIconProvider
                .icon('share-arrow', 'images/icons/share-arrow.svg', 24)
                .icon('upload', 'images/icons/upload.svg', 24)
                .icon('copy', 'images/icons/copy.svg', 24)
                .icon('print', 'images/icons/print.svg', 24)
                .icon('hangout', 'images/icons/hangout.svg', 24)
                .icon('mail', 'images/icons/mail.svg', 24)
                .icon('message', 'images/icons/message.svg', 24)
                .icon('copy2', 'images/icons/copy2.svg', 24)
                .icon('facebook', 'images/icons/facebook.svg', 24)
                .icon('twitter', 'images/icons/twitter.svg', 24);
        })
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
                //{id: 3, text: '删除'},
                {id: 4, text: '导出'},
                {id: 5, text: '提交'},
                {id: 6, text: '审核'}
            ],
            projectTypes: [/**项目类型**/
                {id: 1, text: '项目类'},
                {id: 2, text: '合同类'},
                {id: 3, text: '工程类'}
            ],
            isDist: [/**是否分配**/
                {id: 1, text: '未分配'},
                {id: 2, text: '已分配'}
            ],
            contractTypes : [ /**合同类型**/
                {id: 1, text: '买卖合同'},
                {id: 2, text: '供用水电气热合同'},
                {id: 3, text: '油田工程合同'},
                {id: 4, text: '建设工程合同'},
                {id: 5, text: '承揽合同'},
                {id: 6, text: '运输合同'},
                {id: 7, text: '仓储保管合同'},
                {id: 8, text: '服务合同'},
                {id: 9, text: '技术合同和知识产权合同'},
                {id: 10, text: '租赁合同'},
                {id: 11, text: '融资保险类合同'},
                {id: 12, text: '合资合作经营合同'},
                {id: 13, text: '其他合同'}
            ],
            isEnds : [ /**审结类型**/
                {id: 1, text: '未审结'},
                {id: 2, text: '已审结'}
            ],
            typeOfBrokerageType :[ /**佣金类型计算方式**/
                {id: 1, text: '金额'},
                {id: 2, text: '点位'}
            ],
            typeOfBrokerageType2 :[ /**渠道项目合同-框架合同**/
                {id: 1, text: '有'},
                {id: 2, text: '无'}
            ]
        }
    )
});
