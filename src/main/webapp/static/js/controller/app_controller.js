App.controller('AppController',[ '$scope', '$http', function($scope, $http) {
    $scope.availableSearchParams = [
          { key: "school", name: "School", placeholder: "School..." },
          { key: "job", name: "Job", placeholder: "Job..." },
          { key: "skill", name: "Skill", placeholder: "Skill..." },
        ];

    $scope.addPredefinedNameSearchParam = function(){
      $scope.list = [];
          var formData = {
                        "university" : $scope.searchParams.school,
                        "job" : $scope.searchParams.job,
                        "skill" : $scope.searchParams.skill,
                        'query': $scope.searchParams.query
        };

//        var response = $http.post('http://localhost:8080/IRProject/query/', formData);
        window.location.href="http://localhost:8080/IRProject/query/"
        var response = $http({
            url:'http://localhost:8080/IRProject/search/',
            method:'GET',
            params:formData

        });

//        var response = $http.post('http://localhost:8080/IRProject/query/', formData);
        response.success(function(data, status, headers, config) {
                $scope.list.push(data);
        });
        response.error(function(data, status, headers, config) {
                alert( "Exception details: " + JSON.stringify({data: data}));
        });
        $scope.list = [];
        };
        $scope.loadPredefinedSearchParamSet = function(){
            var response = $http.get('http://localhost:8080/IRProject/query/');
//          $scope.searchParams = {
//            name: "Max M.",
//            job: "Boss"
//          };
        };
          var self = this;
          self.name='';
          self.query = 'asdffgggggggg';
          self.message= 'Hello';
          self.changeMessage = function() {
              self.message = 'Bye';
          };
          self.resetMessage = function() {
              self.message = 'Hello';
          };
        $scope.list = [];
      }]);