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
            //创建一个空的 scope
            mockScope = $rootScope.$new();
            //声明 Controller并且注入已创建的空的 scope
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