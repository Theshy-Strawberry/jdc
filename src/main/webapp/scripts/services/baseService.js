/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (services) {
    'use strict';
    services.factory('baseService', [
        '$mdUtil',
        '$mdSidenav',
        '$mdDialog',
        '$mdToast',
        '$log',
        '$http',
        '$q',
        function ($mdUtil, $mdSidenav, $mdDialog, $mdToast, $log, $http, $q) {

            var self;
            var actionName = {};
            var paginationOptions = {
                pageNumber: 1,
                pageSize: 20,
                sort: null
            };
            var para = {};
            var toastPosition = {
                bottom: false,
                top: true,
                left: false,
                right: true,
                time: 3000
            };
            var dateOptions = {
                changeYear: true,
                changeMonth: true,
                yearRange: '1900:-0'
            };
            var gridOptions = {
                //exporterMenuCsv: false,
                //enableGridMenu: false,
                //enableColumnResizing: false,
                //enablePaginationControls: false,
                //enableFiltering : true,
                enableRowSelection: true,
                expandableRowHeight: 1500,
                paginationPageSizes: [10, 20, 50],
                paginationPageSize: 20,
                useExternalPagination: true,
                useExternalSorting: true,
                columnDefs: []
            };
            var gridOptions2 = {
                //exporterMenuCsv: false,
                //enableGridMenu: false,
                //enableColumnResizing: false,
                //enablePaginationControls: false,
                //enableFiltering : true,
                enableRowSelection: true,
                expandableRowHeight: 1500,
                paginationPageSizes: [10, 20, 50],
                paginationPageSize: 20,
                useExternalPagination: true,
                useExternalSorting: true,
                columnDefs: []
            };
            return self = {

                actionName: actionName,

                paginOpt: paginationOptions,

                gridOptions: gridOptions,

                gridOptions2: gridOptions2,

                toastPosition: toastPosition,

                dateOptions: dateOptions,


                getToastPosition: function () {
                    var toastPosition = this.toastPosition;
                    return Object.keys(toastPosition)
                        .filter(function (pos) {
                            return toastPosition[pos];
                        })
                        .join(' ');
                },
                /**
                 * Build handler to open/close a SideNav; when animation finishes
                 * report completion in console
                 */
                buildToggler: function (navID) {
                    var debounceFn = $mdUtil.debounce(function () {
                        $mdSidenav(navID)
                            .toggle()
                            .then(function () {
                                $log.debug("toggle " + navID + " is done");
                            });
                    }, 300);
                    return debounceFn;
                },
                buildRightToggler: function () {
                    var debounceFn = $mdUtil.debounce(function () {
                        $mdSidenav('right')
                            .toggle()
                            .then(function () {
                                $log.debug("toggle right is done");
                            });
                    }, 300);
                    return debounceFn;
                },
                buildRightToggler2: function () {
                    var debounceFn = $mdUtil.debounce(function () {
                        $mdSidenav('right2')
                            .toggle()
                            .then(function () {
                                $log.debug("toggle right2 is done");
                            });
                    }, 300);
                    return debounceFn;
                },
                closeToggler: function (navID) {
                    $mdSidenav(navID).close()
                        .then(function () {
                            $log.debug("close RIGHT is done");
                        });
                },
                closeRightToggler: function () {
                    $mdSidenav('right').close()
                        .then(function () {
                            $log.debug("close RIGHT is done");
                        });
                },
                closeRightToggler2: function () {
                    $mdSidenav('right2').close()
                        .then(function () {
                            $log.debug("close RIGHT2 is done");
                        });
                },
                findAllWithParams: function (para) {
                    para.pageSize = this.paginOpt.pageSize;
                    para.page = this.paginOpt.pageNumber;
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/findAll",
                        params:  window.formatObj(para),
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                findAllWithParams2: function (para,id) {
                    para.pageSize = this.paginOpt.pageSize;
                    para.page = this.paginOpt.pageNumber;
                    para.id = id;
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/findAll2",
                        params:  window.formatObj(para),
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                findSubAllWithParams: function (para) {
                    para.pageSize = this.paginOpt.pageSize;
                    para.page = this.paginOpt.pageNumber;
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/findSubAll",
                        params: window.formatObj(para)
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                post: function (path, para) {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + path,
                        params:  window.formatObj(para),
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                save: function (para) {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/save",
                        params:  window.formatObj(para),
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                findAll: function () {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/findAll",
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                getCurUser: function () {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + "sysUser" + "/getUser",
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                update: function (para) {
                    var def = $q.defer();
                    $http({
                        method: 'POST',
                        url: window.ServerURL + "/" + actionName.name + "/update",
                        params:  window.formatObj(para)
                    }).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                saveOrUpdate: function (id, para) {
                    var def = $q.defer();
                    var req = {};
                    if (id == null) {
                        req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/save",
                            params:  window.formatObj(para)
                        };
                    } else {
                        req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/update",
                            params:  window.formatObj(para)
                        };
                    }
                    $http(req).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                saveOrUpdate2: function (id, para) {
                    var def = $q.defer();
                    var req = {};
                    if (id == null) {
                        req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/save2",
                            params:  window.formatObj(para)
                        };
                    } else {
                        req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/update2",
                            params:  window.formatObj(para)
                        };
                    }
                    $http(req).success(function (data) {
                        def.resolve(data);
                    }).error(function () {
                        def.reject("error");
                    })
                    return def.promise;
                },
                delete: function (para) {
                    var def = $q.defer();
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否删除此项纪录?')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/delete",
                            params:  window.formatObj(para)
                        };
                        $http(req).success(function (data) {
                            def.resolve(data);
                        }).error(function () {
                            def.reject("error");
                        })
                    }, function () {
                    });
                    return def.promise;
                },
                deleteByMethod: function (url,para) {
                    var def = $q.defer();
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否删除此项纪录?')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + url,
                            params:  window.formatObj(para)
                        };
                        $http(req).success(function (data) {
                            def.resolve(data);
                        }).error(function () {
                            def.reject("error");
                        })
                    }, function () {
                    });
                    return def.promise;
                },
                deleteById: function (para) {
                    var def = $q.defer();
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否删除此项纪录?')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/delete",
                            params: {
                                id: para
                            }
                        };
                        $http(req).success(function (data) {
                            def.resolve(data);
                        }).error(function () {
                            def.reject("error");
                        })
                    }, function () {
                    });
                    return def.promise;
                },
                deleteById2: function (para) {
                    var def = $q.defer();
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否删除此项纪录?')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/delete2",
                            params: {
                                id: para
                            }
                        };
                        $http(req).success(function (data) {
                            def.resolve(data);
                        }).error(function () {
                            def.reject("error");
                        })
                    }, function () {
                    });
                    return def.promise;
                },
                deleteByNo: function (para,para1) {
                    var def = $q.defer();
                    var confirm = $mdDialog.confirm()
                        .title('提示信息')
                        .content('是否删除此项纪录?')
                        .ok('确认')
                        .cancel('取消')
                        .targetEvent();
                    $mdDialog.show(confirm).then(function () {
                        var req = {
                            method: 'POST',
                            url: window.ServerURL + "/" + actionName.name + "/delete",
                            params: {
                                id: para,
                                aproval_no: para1
                            }
                        };
                        $http(req).success(function (data) {
                            def.resolve(data);
                        }).error(function () {
                            def.reject("error");
                        })
                    }, function () {
                    });
                    return def.promise;
                },
                alert : function (message){
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.body))
                            .title('提示信息')
                            .content(message)
                            .ok('确认')
                            .targetEvent()
                    );
                },
                onRegisterApi: function (s, gridApi) {
                    s.gridApi = gridApi;
                    s.gridApi.core.on.sortChanged(s, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            paginOpt.sort = null;
                        } else {
                            paginOpt.sort = sortColumns[0].sort.direction;
                        }
                        getPage();
                    });
                    gridApi.pagination.on.paginationChanged(s, function (newPage, pageSize) {
                        paginOpt.pageNumber = newPage;
                        paginOpt.pageSize = pageSize;
                        getPage();
                    });
                },
                getPage: function () {
                    this.findAllWithParams({}).then(
                        function (data) {
                            gridOptions.totalItems = data.result.totalRow;
                            gridOptions.data = data.result.list;
                            gridOptions2.totalItems = data.result.totalRow;
                            gridOptions2.data = data.result.list;
                        }
                    );
                },
                showToast: function (val) {
                    return $mdToast.show(
                        $mdToast.simple()
                            .content(val)
                            .position(this.getToastPosition())
                            .hideDelay(this.toastPosition.time)
                    );
                },
                autocomplete: function (query,arr,key) {
                    var results = query ? arr.filter( this.createFilterFor(query,key) ) : arr,
                        deferred;
                    if (false) {
                        deferred = $q.defer();
                        $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
                        return deferred.promise;
                    } else {
                        return results;
                    }
                },
                createFilterFor: function (query,key) {
                    return function filterFn(state) {
                        return (state[key].toUpperCase().indexOf(query.toUpperCase()) >= 0);
                    };
                },
                init: function (obj, data) {
                    obj.toggleRight = this.buildRightToggler();
                    obj.dateOptions = dateOptions;
                    actionName.name = data.name;
                    obj.gridOptions = data.gridOptions ==null ? data.gridOptions : gridOptions ;
                    obj.gridOptions2 = data.gridOptions2 ==null ? data.gridOptions2 : gridOptions2 ;
                },
                formatDatetime: function (myDate) {
                    var result=myDate.getFullYear()+'-'
                        + (myDate.getMonth()+1)+'-'
                        + myDate.getDate() + ' '
                        + myDate.getHours() + ':' + myDate.getMinutes() + ':' + myDate.getMinutes();
                    return result;
                }
            };
        }])
});
