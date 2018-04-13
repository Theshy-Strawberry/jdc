define([], function()
{
    return {
        defaultRoutePath: '/',
        routes: {
            '/dashborad': {//快捷菜单
                templateUrl: './views/dashborad.html',
                dependencies: [
                    'controllers/dashController'
                ]
            },
            //===================================领导视图=============================================
            '/leaderAudit': {//领导视图--领导采购和基建常规审批
                templateUrl: './views/leaderAudit/leaderAuditManagement.html',
                dependencies: [
                    'controllers/leaderAudit/leaderAuditManagementController'
                ]
            },
            '/leaderHardAudit': {//领导视图--领导财务和基建专项审批
                templateUrl: './views/leaderAudit/leaderHardAuditManagement.html',
                dependencies: [
                    'controllers/leaderAudit/leaderHardAuditManagementController'
                ]
            },
            '/leaderContractManagement': {//领导视图--领导合同管理
                templateUrl: './views/leaderAudit/leaderContractManagement.html',
                dependencies: [
                    'controllers/leaderAudit/leaderContractManagementController'
                ]
            },
            '/leaderEngineeringManagement': {//领导视图--领导工程管理
                templateUrl: './views/leaderAudit/leaderEngineeringManagement.html',
                dependencies: [
                    'controllers/leaderAudit/leaderEngineeringManagementController'
                ]
            },
            '/leaderView': {//领导视图--领导统计分析
                templateUrl: './views/leaderAudit/leaderViewManagement.html',
                dependencies: [
                    'controllers/leaderAudit/leaderViewManagementController'
                ]
            },
            //===================================领导视图=============================================
            //===================================基建审计=============================================
            '/infrastructurespecial': {//基建审计--项目审计--专项
                templateUrl: './views/infrastructureaudit/projectmanagementspecial.html',
                dependencies: [
                    'controllers/infrastructureaudit/projectManagementControllerSpecial'
                ]
            },
            '/infrastructureroutine': {//基建审计--项目审计--常规
                templateUrl: './views/infrastructureaudit/projectmanagementroutine.html',
                dependencies: [
                    'controllers/infrastructureaudit/projectManagementControllerRoutine'
                ]
            },
            '/engineeringAudit': {//基建审计--工程审计
                templateUrl: './views/infrastructureaudit/engineeringAudit.html',
                dependencies: [
                    'controllers/infrastructureaudit/engieeringAuditController'
                ]
            },
            '/engineeringProperty': {//基建审计--工程性质
                templateUrl: './views/infrastructureaudit/engineeringProperty.html',
                dependencies: [
                    'controllers/infrastructureaudit/engineeringPropertyController'
                ]
            },
            //===================================基建审计=============================================
            //===================================采购服务审计=============================================
            '/contractType': {//采购服务审计--合同管理--合同类型管理
                templateUrl: './views/procurementservicesaudit/contract/contractType.html',
                dependencies: [
                    'controllers/procurementservicesaudit/contractTypeController'
                ]
            },
            '/contractReceive': {//采购服务审计--合同管理--合同收发台账
                templateUrl: './views/procurementservicesaudit/contract/receiveProject.html',
                dependencies: [
                    'controllers/procurementservicesaudit/contractReceiveController'
                ]
            },
            '/pprojectmanagement': {//采购服务审计--项目管理
                templateUrl: './views/procurementservicesaudit/projectmanagement.html',
                dependencies: [
                    'controllers/procurementservicesaudit/projectManagementController'
                ]
            },
            '/contractAudit': {//采购服务审计--合同审计/Add by FWH
                templateUrl: './views/procurementservicesaudit/contractAudit.html',
                dependencies: [
                    'controllers/procurementservicesaudit/contractAuditController'
                ]
            },
            '/accountAudit': {//采购服务审计--结算审计/Add by FWH
                templateUrl: './views/procurementservicesaudit/accountAudit.html',
                dependencies: [
                    'controllers/procurementservicesaudit/accountAuditController'
                ]
            },
            '/priceBaseManagement': {//采购服务审计--价格库--价格库维护
                templateUrl: './views/procurementservicesaudit/pricebase/priceBaseManagement.html',
                dependencies: [
                    'controllers/procurementservicesaudit/priceBaseController'
                ]
            },
            '/commodityTypeManagement': {//采购服务审计--价格库--物品类别管理
                templateUrl: './views/procurementservicesaudit/pricebase/commodityTypeManagement.html',
                dependencies: [
                    'controllers/procurementservicesaudit/commodityTypeController'
                ]
            },
            '/commoditySubTypeManagement': {//采购服务审计--价格库--物品子类别管理
                templateUrl: './views/procurementservicesaudit/pricebase/commoditySubTypeManagement.html',
                dependencies: [
                    'controllers/procurementservicesaudit/commoditySubTypeController'
                ]
            },
            '/workwrite': {//采购服务审计--工作写实
                templateUrl: './views/procurementservicesaudit/workwrite.html',
                dependencies: [
                    'controllers/procurementservicesaudit/WorkWriteController'
                ]
            },
            '/contractAuditAccount': {//采购服务审计--审计台帐--合同审计台帐
                templateUrl: './views/procurementservicesaudit/ledgeraudit/contractAuditAccount.html',
                dependencies: [
                    'controllers/procurementservicesaudit/contractAuditAccountController'
                ]
            },
            '/accountAuditAccount': {//采购服务审计--审计台帐--结算审计台帐
                templateUrl: './views/procurementservicesaudit/ledgeraudit/accountAuditAccount.html',
                dependencies: [
                    'controllers/procurementservicesaudit/accountAuditAccountController'
                ]
            },
            '/arrivedAuditAccount': {//采购服务审计--审计台帐--到货审计台帐
                templateUrl: './views/procurementservicesaudit/ledgeraudit/arrivedAuditAccount.html',
                dependencies: [
                    'controllers/procurementservicesaudit/arrivedAuditAccountController'
                ]
            },
            '/receiptAuditAccount': {//采购服务审计--审计台帐--票据审计台帐
                templateUrl: './views/procurementservicesaudit/ledgeraudit/receiptAuditAccount.html',
                dependencies: [
                    'controllers/procurementservicesaudit/receiptAuditAccountController'
                ]
            },
            '/arrivedInfo': {//采购服务审计--到货审计
                templateUrl: './views/procurementservicesaudit/arrived/arrivedInfo.html',
                dependencies: [
                    'controllers/procurementservicesaudit/arrived/arrivedInfoController'
                ]
            },
            //===================================采购服务审计=============================================
            //===================================财务审计=============================================
            '/financeaudit': {//财务审计--项目审计
                templateUrl: './views/financialaudit/projectmanagement.html',
                dependencies: [
                    'controllers/financialaudit/projectManagementController'
                ]
            },
            '/auditpoint': {//财务审计--财务必审点
                templateUrl: './views/financialaudit/auditPoint.html',
                dependencies: [
                    'controllers/financialaudit/auditPointController'
                ]
            },
            //===================================财务审计=============================================
            //===================================综合管理=============================================
            '/iprojectmanagement': {//综合管理--项目管理
                templateUrl: './views/integrativeManage/projectManagement.html',
                dependencies: [
                    'controllers/integrativeManage/projectManagementController'
                ]
            },
            '/natureDict': {//综合管理--定性词典
                templateUrl: './views/integrativeManage/natureDict.html',
                dependencies: [
                    'controllers/integrativeManage/natureDictController'
                ]
            },
            '/carApplyApprove': {//综合管理--用车申请审批
                templateUrl: './views/integrativeManage/carApplyApprove.html',
                dependencies: [
                    'controllers/integrativeManage/CarApprovalController'
                ]
            },
            '/contractRecon': {//综合管理--合同审计复议
                templateUrl: './views/integrativeManage/contractRecon.html',
                dependencies: [
                    'controllers/integrativeManage/contractReconController'
                ]
            },
            '/engineeringRecon': {//综合管理--工程审计复议
                templateUrl: './views/integrativeManage/engineeringRecon.html',
                dependencies: [
                    'controllers/integrativeManage/engineeringReconController'
                ]
            },
            '/iContractAudit': {//综合管理--合同审计
                templateUrl: './views/integrativeManage/integrativeContractAudit.html',
                dependencies: [
                    'controllers/integrativeManage/integrativeContractAuditController'
                ]
            },
            //===================================综合管理=============================================
            //===================================系统管理=============================================
            '/projectType': {//系统管理--项目类型管理
                templateUrl: './views/systemmanage/projectType.html',
                dependencies: [
                    'controllers/systemmanage/projectTypeController'
                ]
            },
            '/carApply': {//系统管理--用车申请
                templateUrl: './views/systemmanage/carApply.html',
                dependencies: [
                    'controllers/systemmanage/CarApplyController'
                ]
            },
            '/unitInfo': {//系统管理--二级单位
                templateUrl: './views/systemmanage/unitInfo.html',
                dependencies: [
                    'controllers/systemmanage/unitInfoController'
                ]
            },
            '/unitBusinessType': {//系统管理--业务划分
                templateUrl: './views/systemmanage/unitBusinessType.html',
                dependencies: [
                    'controllers/systemmanage/unitBusinessTypeController'
                ]
            },
            '/office': {//系统管理--科室管理
                templateUrl: './views/systemmanage/officeManage.html',
                dependencies: [
                    'controllers/systemmanage/officeManageController'
                ]
            },
            '/sysUserManagement': {//系统管理--用户管理
                templateUrl: './views/systemmanage/sysUserManagement.html',
                dependencies: [
                    'controllers/systemmanage/sysUserManagementController'
                ]
            },
            '/knowledgeBase': {//系统管理--知识库
                templateUrl: './views/systemmanage/knowledgeBase.html',
                dependencies: [
                    'controllers/systemmanage/knowledgeBaseController'
                ]
            },
            '/sysLog': {//系统管理--日志管理
                templateUrl: './views/systemmanage/sysLogManage.html',
                dependencies: [
                    'controllers/systemmanage/sysLogController'
                ]
            },
            '/systodo': {//系统管理--待办任务
                templateUrl: './views/systemmanage/sysTodoManage.html',
                dependencies: [
                    'controllers/systemmanage/sysTodoController'
                ]
            },
            '/sysroot': {//系统管理--权限管理
                templateUrl: './views/systemmanage/sysRootManage.html',
                dependencies: [
                    'controllers/systemmanage/sysRootController'
                ]
            },
            '/pwd': {//系统管理--密码修改
                templateUrl: './views/systemmanage/changePwd.html',
                dependencies: [
                    'controllers/systemmanage/changePwdController'
                ]
            },
            '/attendanceManagement': {//系统管理--考勤管理
                templateUrl: './views/systemmanage/attendanceManagement.html',
                dependencies: [
                    'controllers/systemmanage/attendanceManagementController'
                ]
            },
            //===================================系统管理=============================================
            //===================================查询分析=============================================
            '/piegraph': {//查询分析--饼状图
                templateUrl: './views/queryanalysis/PieGraph.html',
                dependencies: [
                    'controllers/queryanalysis/pieGraphController'
                ]
            },
            '/auditdictproblem': {//查询分析--审计发现问题定性
                templateUrl: './views/queryanalysis/auditDictProblem.html',
                dependencies: [
                    'controllers/queryanalysis/auditDictProblemController'
                ]
            },
            //===================================查询分析=============================================
            //===================================登录=============================================
            '/edituserinfo': {//修改用户信息
                templateUrl: './views/systemmanage/EditUserInfo.html',
                dependencies: [
                    'controllers/systemmanage/editUserInfoController'
                ]
            },
            '/login': {//登录
                templateUrl: '/login.html',
                dependencies: [
                    'controllers/loginController'
                ]
            },
            //===================================登录=============================================
        }
    };
});
