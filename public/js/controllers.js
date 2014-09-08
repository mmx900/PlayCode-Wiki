'use strict'

var playcodeControllers = angular.module('playcodeControllers', ['ngSanitize']);

playcodeControllers.controller('SignUpCtrl', ['$scope', '$location', '$http', '$window',
	function ($scope, $location, $http, $window) {
		$scope.alerts = [];

		$scope.closeAlert = function (index) {
			$scope.alerts.splice(index, 1);
		};

		$scope.submit = function () {
			$scope.alerts = [];

			if ($scope.password != $scope.password_check) {
				$scope.alerts.push({msg: '비밀번호가 맞지 않습니다.', type: 'danger'});
				return;
			}

			$http.post('/signup', {
				email: $scope.email,
				nickname: $scope.nickname,
				password: $scope.password
			}).success(function (data, status, headers, config) {
				$window.location.href = '/#/article';
			}).error(function (data, status, headers, config) {
				if (status === 401) {
					//$scope.alerts.push({msg: '이미 존재하는 아이디입니다.', type: 'danger'});
				} else {
					$scope.alerts.push({msg: '오류가 발생했습니다.', type: 'danger'});
				}
			});
		}
	}]);

playcodeControllers.controller('LoginCtrl', ['$scope', '$location', '$http', '$window',
	function ($scope, $location, $http, $window) {
		$scope.alerts = [];

		$scope.closeAlert = function (index) {
			$scope.alerts.splice(index, 1);
		};

		$scope.submit = function () {
			$scope.alerts = [];

			$http.post('/login', {
				email: $scope.email,
				password: $scope.password
			}).success(function (data, status, headers, config) {
				$window.location.href = '/#/article';
			}).error(function (data, status, headers, config) {
				if (status === 401) {
					$scope.alerts.push({msg: '아이디 혹은 비밀번호가 맞지 않습니다.', type: 'danger'});
				} else {
					$scope.alerts.push({msg: '오류가 발생했습니다.', type: 'danger'});
				}
			});
		}
	}]);

playcodeControllers.controller('ArticleListCtrl', [ '$scope', '$routeParams', '$http',
	function ($scope, $routeParams, $http) {
		var url = '/article';
		if (typeof $routeParams.keyword !== 'undefined')
			url += "?keyword=" + $routeParams.keyword;

		$http.get(url).
			success(function (data, status, headers, config) {
				$scope.articles = data;
			});
	} ]);

playcodeControllers.controller('ArticleQueryCtrl', [ '$scope', '$location',
	function ($scope, $location) {
		$scope.search = function () {
			$location.search('keyword', $scope.keyword)
		}
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
			});
		}
	}]);

marked.setOptions({
	breaks: true,
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

playcodeControllers.controller('ArticleRevisionsCtrl', [ '$scope', '$routeParams', '$http',
	function ($scope, $routeParams, $http) {
		$scope.title = $routeParams.title;

		$http.get('/article/' + $routeParams.title + '/revisions').
			success(function (data, status, headers, config) {
				$scope.revisions = data;
			});
	} ]);