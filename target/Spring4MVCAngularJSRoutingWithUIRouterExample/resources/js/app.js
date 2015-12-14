'use strict';

var App = angular.module('myApp',['ui.router','angular-advanced-searchbox']);

App.config(['$stateProvider', '$urlRouterProvider','$locationProvider' ,function($stateProvider, $urlRouterProvider, $locationProvider){
	
//	$urlRouterProvider.otherwise("/query")
	
	$stateProvider
        .state('query', {
		url: "/",
		templateUrl: 'query',
		controller : "QueryController as qController",
//		resolve: {
//            async: ['ItemService', function(ItemService) {
//                return ItemService.fetchCategoryList();
//           	}]
//        }
	})
        .state('search',{
            url:"/search",
            templateUrl:'search',
            controller : "SearchController as srController",
            resolve:{
                async:['SearchService',function(SearchService){
                        return SearchService.fetchSearchResult();
                }]
            }
        })
	.state('category', {
		url: "/category",
		templateUrl: 'category',
		controller : "CategoryController as ctgController",
		resolve: {
            async: ['ItemService', function(ItemService) {
                return ItemService.fetchCategoryList();
           	}]
        }
	})

	.state('category.list', {
		url: '/{categoryId:[A-Za-z]{0,9}}',
		templateUrl: function(params){ return 'category/' + params.categoryId; },
		controller : "ItemListController as itemListCtrl",
		resolve: {
            async: ['ItemService', '$stateParams', function(ItemService, $stateParams) {
                return ItemService.fetchAllItems($stateParams.categoryId);
           	}]
        }
	})

	.state('category.list.detail', {
		url: '/{itemId:[0-9]{1,9}}',
		templateUrl: function(params){ return 'category/' + params.categoryId +'/'+params.itemId; },
		controller : "ItemDetailsController as itemDetailsCtrl",
		resolve: {
            async: ['ItemService', '$stateParams', function(ItemService, $stateParams) {
                return ItemService.fetchSpecificItem($stateParams.categoryId, $stateParams.itemId);
           	}]
        }
	})
//        $locationProvider.html5Mode(true);
}]);

