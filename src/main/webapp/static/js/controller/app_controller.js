App.controller('AppController',[ '$scope', '$http', function($scope, $http) {
//    $scope.availableSearchParams = [
//          { key: "name", name: "Name", placeholder: "Name..." },
//          { key: "city", name: "City", placeholder: "City..." },
//          { key: "country", name: "Country", placeholder: "Country..." },
//          { key: "emailAddress", name: "E-Mail", placeholder: "E-Mail..." },
//          { key: "job", name: "Job", placeholder: "Job..." }
//        ];
//    $scope.addPredefinedNameSearchParam = function(){
//    $scope.searchParams.name = 'Max Mustermann';
//    self.query = $scope.searchParams;
//              $scope.list = [];
//          var formData = {
//                        "name" : $scope.searchParams.name,
//                        "job" : $scope.searchParams.job,
//                        "city" : $scope.searchParams.city
//        };
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

        var response = $http.post('http://localhost:8080/IRProject/query/', formData);
        response.success(function(data, status, headers, config) {
                $scope.list.push(data);
        });
        response.error(function(data, status, headers, config) {
                alert( "Exception details: " + JSON.stringify({data: data}));
        });
        };
        $scope.loadPredefinedSearchParamSet = function(){
          $scope.searchParams = {
            name: "Max M.",
            job: "Boss"
          };

        //Empty list data after process
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