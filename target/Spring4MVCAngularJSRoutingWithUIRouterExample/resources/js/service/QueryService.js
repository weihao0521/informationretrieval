App.factory('QueryService',function(){
    var QueryService = {}
    QueryService.set = function (data) {
        savedData = data;
    }
    QueryService.get = function () {
        return savedData;
    }

    return QueryService;
})