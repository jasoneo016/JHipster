'use strict';

describe('Controller Tests', function() {

    describe('Movie Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMovie, MockDirector, MockActor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMovie = jasmine.createSpy('MockMovie');
            MockDirector = jasmine.createSpy('MockDirector');
            MockActor = jasmine.createSpy('MockActor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Movie': MockMovie,
                'Director': MockDirector,
                'Actor': MockActor
            };
            createController = function() {
                $injector.get('$controller')("MovieDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jHipsterApp:movieUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
