App.controller('AppController', function($scope) {
          $scope.availableSearchParams = [
          { key: "name", name: "Name", placeholder: "Name..." },
          { key: "city", name: "City", placeholder: "City..." },
          { key: "country", name: "Country", placeholder: "Country..." },
          { key: "emailAddress", name: "E-Mail", placeholder: "E-Mail..." },
          { key: "job", name: "Job", placeholder: "Job..." }
        ];
          $scope.addPredefinedNameSearchParam = function(){
          $scope.searchParams.name = 'Max Mustermann';
          self.query = $scope.searchParams;
        };
        $scope.loadPredefinedSearchParamSet = function(){
          $scope.searchParams = {
            name: "Max M.",
            job: "Boss"
          };
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

      });

//      App.controller('AppController', function($scope) {
//        $scope.availableSearchParams = [
//          { key: "name", name: "Name", placeholder: "Name..." },
//          { key: "city", name: "City", placeholder: "City..." },
//          { key: "country", name: "Country", placeholder: "Country..." },
//          { key: "emailAddress", name: "E-Mail", placeholder: "E-Mail..." },
//          { key: "job", name: "Job", placeholder: "Job..." }
//        ];
//
//        $scope.addPredefinedNameSearchParam = function(){
//          $scope.searchParams.name = 'Max Mustermann';
//        };
//
//        $scope.loadPredefinedSearchParamSet = function(){
//          $scope.searchParams = {
//            name: "Max M.",
//            job: "Boss"
//          };
//        };
//      });
//    