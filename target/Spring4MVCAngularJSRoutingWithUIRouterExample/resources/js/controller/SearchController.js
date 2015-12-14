App.controller('SearchController', ['$scope', 'async', function($scope, async) {
//        $window.location.href = "http://localhost:8080/LinkedinSearchEngine/#/search";
        var self = this;
        self.items=async;
        $scope.items = async;
}]);