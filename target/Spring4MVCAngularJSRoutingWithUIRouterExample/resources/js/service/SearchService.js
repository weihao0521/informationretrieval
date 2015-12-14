App.factory('SearchService', ['$http', '$q' ,'QueryService', function($http, $q, QueryService){
        return{
            fetchSearchResult: function() {//Fetches category list from server.
                var formData = QueryService.get();
//                var formData = {'university':'harvard university'};
                    return $http.get('http://localhost:8080/LinkedinSearchEngine/result/',{
                        params:formData
                    })
                            .then(
                                                    function(response){
                                                            return response.data;
                                                    }, 
                                                    function(errResponse){
                                                            console.error('Error while fetching Items');
                                                            return $q.reject(errResponse);
                                                    }
                                    );
			}
//                var formData = QueryService.get();
////                var formData = $state.current.data;
//        var response = $http({
//            url:'http://localhost:8080/LinkedinSearchEngine/result',
//            method:'GET',
//            params:formData
//
//        });      
//        
//        response.success(function(data, status, headers, config) {                
//                return response.data;
//        });
//        response.error(function(data, status, headers, config) {
//                alert( "Exception details: " + JSON.stringify({data: data}));
//        });
//            }
        };
}]);