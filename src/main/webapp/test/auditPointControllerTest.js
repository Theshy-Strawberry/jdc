define([
    'app',
    'angular',
    'angular-route',
    'angular-aria',
], function () {

    describe('auditPointCTRL', function () {

        var mockScope = {};
        var controller;
        beforeEach(angular.mock.module("app"));

        beforeEach(angular.mock.inject(function ($rootScope, $controller) {
            //����һ���յ� scope
            mockScope = $rootScope.$new();
            //���� Controller����ע���Ѵ����Ŀյ� scope
            controller = $controller('auditPointCTRL', {
                $scope: mockScope
            });

        }));

        it("Create Errors",function(){
            expect(controller).toEqual(null);
        })

        it('Increments counter', function () {
            mockScope.incrementCounter();
            expect(controller.allot1).toEqual(1);
        });

    });
});