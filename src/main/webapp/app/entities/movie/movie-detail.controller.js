(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('MovieDetailController', MovieDetailController);

    MovieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Movie', 'Director', 'Actor'];

    function MovieDetailController($scope, $rootScope, $stateParams, previousState, entity, Movie, Director, Actor) {
        var vm = this;

        vm.movie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jHipsterApp:movieUpdate', function(event, result) {
            vm.movie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
