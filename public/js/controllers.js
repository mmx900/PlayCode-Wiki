'use strict'

var playcodeControllers = angular.module('playcodeControllers', ['ngSanitize']);

playcodeControllers.controller('ArticleListCtrl', [ '$scope', '$http',
	function($scope, $http) {
		$http.get('/article').
			success(function(data, status, headers, config) {
				$scope.articles = data;
			});
	} ]);

playcodeControllers.controller('ArticleCreateCtrl', [ '$scope', '$http',
	function($scope, $http) {
		$scope.submit = function() {
			$http.post('/article/create', {
					title : $scope.title,
					content : $scope.content
				}).
				success(function(data, status, headers, config) {
					//TODO
				});
		}
	} ]);

marked.setOptions({
	highlight: function(code) {
		return hljs.highlightAuto(code).value;
	}
});

playcodeControllers.controller('ArticleDetailCtrl', [ '$scope', '$routeParams', '$http',
	function($scope, $routeParams, $http) {
		$http.get('/article/' + $routeParams.title).
			success(function(data, status, headers, config) {
				data.content = marked(data.content);
				$scope.article = data;
			});
	} ]);
