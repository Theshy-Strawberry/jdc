/**
 * @ngdoc function
 * @name codeApp.controller:DashCTRL
 * @description
 * # DashCTRL
 * Controller of the codeApp
 */

define([
  './module'
], function (controllers) {
  'use strict';
  function dashCTRL($scope, baseService,$location){

    //baseService.post("/systodo/findByTopSeven", {}).then(
    //    function (data) {
    //      $scope.todo = data.result;
    //    }
    //)

    var icons = new Array();
    icons.push('images/icons/iconfont-liebiao.svg');
    icons.push('images/icons/iconfont-bianjimoban.svg');
    icons.push('images/icons/iconfont-jingjiaziyuanwei.svg');
    icons.push('images/icons/iconfont-quxiantu.svg');
    icons.push('images/icons/iconfont-licai.svg');
    icons.push('images/icons/iconfont-wenjian.svg');
    icons.push('images/icons/iconfont-tuwendianzhao.svg');
    icons.push('images/icons/iconfont-jihua.svg');
    icons.push('images/icons/up.svg');
    icons.push('images/icons/down.svg');



    $scope.toPath = function (d,val) {
      if(val == ""){
        //baseService.post("/workattendance/checkStatus", {}).then(
        //    function (data) {
        //      if(data.result == 1){
        //        baseService.post("/workattendance/singIn", {}).then(
        //            function (data) {
        //              if(data.result){
        //                baseService.alert('签到成功!');
        //                d.name = "退勤打卡";
        //                d.menu = icons[9];
        //                baseService.post("/index/getUser", {}).then(
        //                    function (data) {
        //                      baseService.post("/sysauth/getUserAuths", {
        //                        user_name : data.result.user_name,
        //                        url : $location.path()
        //                      }).then(
        //                          function (data) {
        //                            $scope.arr = new Array();
        //                            if(data.result.length > 0){
        //                              var userAuth = data.result[0];
        //                              if(userAuth[5] == '服务采购审计'){
        //                                $scope.arr.push({
        //                                  menu : icons[0],
        //                                  name : '项目信息',
        //                                  path : '/pprojectmanagement',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[1],
        //                                  name : '收发台账',
        //                                  path : '/contractReceive',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[2],
        //                                  name : '合同审计',
        //                                  path : '/contractAudit',
        //                                  color : ' #427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[3],
        //                                  name : '   价格库',
        //                                  path : '/priceBaseManagement',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[4],
        //                                  name : '工作写实',
        //                                  path : '/workwrite',
        //                                  color : '#427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[5],
        //                                  name : '用车申请',
        //                                  path : '/carApplyApprove',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[6],
        //                                  name : '   知识库',
        //                                  path : '/knowledgeBase',
        //                                  color : '#37A8AF 0%, #33AF88 100%'
        //                                });
        //
        //                              }else if(userAuth[5] == '财务审计'){
        //                                $scope.arr.push({
        //                                  menu : icons[0],
        //                                  name : '项目审计',
        //                                  path : '/financeaudit',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[1],
        //                                  name : '财务必审点',
        //                                  path : '/auditpoint',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[2],
        //                                  name : '工作写实',
        //                                  path : '/workwrite',
        //                                  color : ' #427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[3],
        //                                  name : '用车申请',
        //                                  path : '/carApplyApprove',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[4],
        //                                  name : '知识库',
        //                                  path : '/knowledgeBase',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //
        //                              }else if(userAuth[5] == '基建审计'){
        //                                $scope.arr.push({
        //                                  menu : icons[0],
        //                                  name : '专项项目审计',
        //                                  path : '/infrastructurespecial',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[1],
        //                                  name : '常规项目审计',
        //                                  path : '/infrastructureroutine',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[2],
        //                                  name : '工程审计',
        //                                  path : '/engineeringAudit',
        //                                  color : ' #427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[3],
        //                                  name : '工作写实',
        //                                  path : '/workwrite',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[4],
        //                                  name : '用车申请',
        //                                  path : '/carApplyApprove',
        //                                  color : '#427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[5],
        //                                  name : '知识库',
        //                                  path : '/knowledgeBase',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //
        //                              }else if(userAuth[5] == '综合管理'){
        //                                $scope.arr.push({
        //                                  menu : icons[0],
        //                                  name : '项目管理',
        //                                  path : '/iprojectmanagement',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[1],
        //                                  name : '用车申请审批',
        //                                  path : '/carApplyApprove',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[2],
        //                                  name : '定性词典',
        //                                  path : '/natureDict',
        //                                  color : ' #427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[3],
        //                                  name : '审计复议',
        //                                  path : '/contractRecon',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[4],
        //                                  name : '工作写实',
        //                                  path : '/workwrite',
        //                                  color : '#427c9d 0%, #42669d 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[5],
        //                                  name : '用车申请',
        //                                  path : '/carApplyApprove',
        //                                  color : ' #37A8AF 0%, #33AF88 100%'
        //                                });
        //                                $scope.arr.push({
        //                                  menu : icons[6],
        //                                  name : '知识库',
        //                                  path : '/knowledgeBase',
        //                                  color : '#0090D9  0%, #3e72c6 100%'
        //                                });
        //
        //                              }
        //                            }else{
        //                              var userAuth = new Array();
        //                            }
        //
        //                            baseService.post("/workattendance/checkStatus", {}).then(
        //                                function (data) {
        //                                  if(data.result == 1){
        //                                    $scope.arr.push({
        //                                      menu : icons[8],
        //                                      name : '出勤打卡',
        //                                      path : '',
        //                                      color : '#228B22 0%, #32CD32 100%'
        //                                    });
        //                                  }else{
        //                                    $scope.arr.push({
        //                                      menu : icons[9],
        //                                      name : '退勤打卡',
        //                                      path : '',
        //                                      color : '#FF4500 0%, #A52A2A 100%'
        //                                    });
        //                                  }
        //                                }
        //                            )
        //                          }
        //                      )
        //                    }
        //                )
        //              }
        //            }
        //        )
        //      }else if(data.result == 2){
        //        baseService.post("/workattendance/singOut", {}).then(
        //            function (data) {
        //              if(data.result){
        //                baseService.alert('签退成功!');
        //                d.name = "退勤打卡";
        //                d.menu = icons[9];
        //              }
        //            }
        //        )
        //      }else if(data.result == 3){
        //          baseService.alert('今日签到已完成,请明日再打卡!');
        //      }
        //    }
        //)
        //$scope.userShow = !$scope.userShow;
        //if(!$scope.userShow){
        //  data.name = "出勤打卡";
        //  data.menu = icons[8];
        //}else{
        //  data.name = "退勤打卡";
        //  data.menu = icons[9];
        //}
        //baseService.alert('签到成功!');
      }else{
        $location.path(val);
      }
    }

    baseService.post("/index/getUser", {}).then(
        function (data) {
          baseService.post("/sysauth/getUserAuths", {
            user_name : data.result.user_name,
            url : $location.path()
          }).then(
              function (data) {
                $scope.arr = new Array();
                if(data.result.length > 0){
                  var userAuth = data.result[0];
                  //if(userAuth[5] == '服务采购审计'){
                  //  $scope.arr.push({
                  //    menu : icons[0],
                  //    name : '项目信息',
                  //    path : '/pprojectmanagement',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[1],
                  //    name : '收发台账',
                  //    path : '/contractReceive',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[2],
                  //    name : '合同审计',
                  //    path : '/contractAudit',
                  //    color : ' #427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[3],
                  //    name : '   价格库',
                  //    path : '/priceBaseManagement',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[4],
                  //    name : '工作写实',
                  //    path : '/workwrite',
                  //    color : '#427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[5],
                  //    name : '用车申请',
                  //    path : '/carApplyApprove',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[6],
                  //    name : '   知识库',
                  //    path : '/knowledgeBase',
                  //    color : '#37A8AF 0%, #33AF88 100%'
                  //  });
                  //
                  //}else if(userAuth[5] == '财务审计'){
                  //  $scope.arr.push({
                  //    menu : icons[0],
                  //    name : '项目审计',
                  //    path : '/financeaudit',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[1],
                  //    name : '财务必审点',
                  //    path : '/auditpoint',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[2],
                  //    name : '工作写实',
                  //    path : '/workwrite',
                  //    color : ' #427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[3],
                  //    name : '用车申请',
                  //    path : '/carApplyApprove',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[4],
                  //    name : '知识库',
                  //    path : '/knowledgeBase',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //
                  //}else if(userAuth[5] == '基建审计'){
                  //  $scope.arr.push({
                  //    menu : icons[0],
                  //    name : '专项项目审计',
                  //    path : '/infrastructurespecial',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[1],
                  //    name : '常规项目审计',
                  //    path : '/infrastructureroutine',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[2],
                  //    name : '工程审计',
                  //    path : '/engineeringAudit',
                  //    color : ' #427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[3],
                  //    name : '工作写实',
                  //    path : '/workwrite',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[4],
                  //    name : '用车申请',
                  //    path : '/carApplyApprove',
                  //    color : '#427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[5],
                  //    name : '知识库',
                  //    path : '/knowledgeBase',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //
                  //}else if(userAuth[5] == '综合管理'){
                  //  $scope.arr.push({
                  //    menu : icons[0],
                  //    name : '项目管理',
                  //    path : '/iprojectmanagement',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[1],
                  //    name : '用车申请审批',
                  //    path : '/carApplyApprove',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[2],
                  //    name : '定性词典',
                  //    path : '/natureDict',
                  //    color : ' #427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[3],
                  //    name : '审计复议',
                  //    path : '/contractRecon',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[4],
                  //    name : '工作写实',
                  //    path : '/workwrite',
                  //    color : '#427c9d 0%, #42669d 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[5],
                  //    name : '用车申请',
                  //    path : '/carApplyApprove',
                  //    color : ' #37A8AF 0%, #33AF88 100%'
                  //  });
                  //  $scope.arr.push({
                  //    menu : icons[6],
                  //    name : '知识库',
                  //    path : '/knowledgeBase',
                  //    color : '#0090D9  0%, #3e72c6 100%'
                  //  });
                  //
                  //}
                }else{
                  var userAuth = new Array();
                }

                //baseService.post("/workattendance/checkStatus", {}).then(
                //    function (data) {
                //      if(data.result == 1){
                //        $scope.arr.push({
                //          menu : icons[8],
                //          name : '出勤打卡',
                //          path : '',
                //          color : '#228B22 0%, #32CD32 100%'
                //        });
                //      }else{
                //        $scope.arr.push({
                //          menu : icons[9],
                //          name : '退勤打卡',
                //          path : '',
                //          color : '#FF4500 0%, #A52A2A 100%'
                //        });
                //      }
                //    }
                //)
              }
          )
        }
    )
  }

  dashCTRL.$inject = ['$scope','baseService','$location'];
  controllers.controller('DashCTRL',dashCTRL);
});
