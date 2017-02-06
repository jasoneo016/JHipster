(function() {
    'use strict';

    angular
        .module('jHipsterApp')
        .controller('MovieDialogController', MovieDialogController);

    MovieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Movie', 'Director', 'Actor'];

    function MovieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Movie, Director, Actor) {
        var vm = this;

        vm.movie = entity;
        vm.clear = clear;
        vm.save = save;
        vm.directors = Director.query({filter: 'movie-is-null'});
        $q.all([vm.movie.$promise, vm.directors.$promise]).then(function() {
            if (!vm.movie.director || !vm.movie.director.id) {
                return $q.reject();
            }
            return Director.get({id : vm.movie.director.id}).$promise;
        }).then(function(director) {
            vm.directors.push(director);
        });
        vm.actors = Actor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.movie.id !== null) {
                Movie.update(vm.movie, onSaveSuccess, onSaveError);
            } else {
                Movie.save(vm.movie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jHipsterApp:movieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
