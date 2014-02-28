'use strict'

var playcodeApp = angular.module('playcodeApp', [ 'ngRoute', 'ngResource', 'playcodeControllers', 'ui.bootstrap' ]);

playcodeApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/article', {
		title : '항목 목록',
		templateUrl : '/assets/tpl/article/list.tpl.html',
		controller : 'ArticleListCtrl'
	}).when('/article/create', {
		title : '항목 생성',
		templateUrl : '/assets/tpl/article/create.tpl.html',
		controller : 'ArticleCreateCtrl'
	}).when('/article/:title', {
		title : '항목 보기',
		templateUrl : '/assets/tpl/article/detail.tpl.html',
		controller : 'ArticleDetailCtrl'
	});
} ]);

playcodeApp.run(['$location', '$rootScope', function($location, $rootScope) {
	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		$rootScope.title = current.$$route.title;
	});
}]);
