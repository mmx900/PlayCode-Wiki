'use strict'

var playcodeControllers = angular.module('playcodeControllers', ['ngSanitize']);

playcodeControllers.controller('ArticleListCtrl', [ '$scope', '$http',
	function ($scope, $http) {
		$http.get('/article').
			success(function (data, status, headers, config) {
				$scope.articles = data;
			});
	} ]);

playcodeControllers.controller('ArticleCreateCtrl', [ '$scope', '$location', '$http',
	function ($scope, $location, $http) {
		$scope.submit = function () {
			$http.post('/article/create', {
				title: $scope.title,
				content: $scope.content
			}).success(function (data, status, headers, config) {
				$location.path("/article");
			});
		}
	} ]);

playcodeControllers.controller('ArticleUpdateCtrl', ['$scope', '$routeParams', '$location', '$http',
	function ($scope, $routeParams, $location, $http) {
		$http.get('/article/' + $routeParams.title).
			success(function (data, status, headers, config) {
				$scope.articleId = data.id;
				$scope.title = data.title;
				$scope.content = data.content;
			});

		$scope.submit = function () {
			$http.post('/article/' + $scope.articleId + '/update', {
				title: $scope.title,
				content: $scope.content
			}).success(function (data, status, headers, config) {
				$location.path("/article");
			})
		}
	}]);

marked.setOptions({
	highlight: function (code) {
		return hljs.highlightAuto(code).value;
	}
});

playcodeControllers.controller('ArticleDetailCtrl', [ '$scope', '$routeParams', '$http',
	function ($scope, $routeParams, $http) {
		$http.get('/article/' + $routeParams.title).
			success(function (data, status, headers, config) {
				data.content = marked(data.content);
				$scope.article = data;
			});
	} ]);

playcodeControllers.controller('ArticleDeleteCtrl', [ '$scope', '$routeParams', '$location', '$http',
	function ($scope, $routeParams, $location, $http) {
		$scope.title = $routeParams.title;
		console.log($scope.title);

		$scope.submit = function () {
			$http.post('/article/' + $routeParams.title + '/delete').
				success(function (data, status, headers, config) {
					$location.path("/article");
				});
		}
	} ]);