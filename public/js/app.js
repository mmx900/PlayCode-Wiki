'use strict';

var playcodeApp = angular.module('playcodeApp', [ 'ngRoute', 'ngResource', 'playcodeControllers', 'ui.bootstrap', 'ui.codemirror' ]);

playcodeApp.constant('USER_ROLES', {
	all: '*',
	admin: 'admin',
	user: 'user'
});

playcodeApp.service('Session', function () {
	this.create = function (userId, nickname, role) {
		this.userId = userId;
		this.nickname = nickname;
		this.role = role;
	};

	this.destroy = function () {
		this.userId = null;
		this.nickname = null;
		this.role = null;
	};

	return this;
});

playcodeApp.factory('AuthService', function ($http, Session, USER_ROLES) {
	return {
		logout: function () {
			return $http.get('/logout').then(function () {
				Session.destroy();
			});
		},
		isAuthenticated: function () {
			return !!Session.userId;
		},
		getAuthentication: function () {
			return $http.get('/loggedin').then(function (res) {
				var user = res.data;

				if (user !== '0') {
					Session.create(user.id, user.nickname, USER_ROLES.user);
				} else {
					Session.destroy();
				}

				return user;
			});
		}
	}
});

playcodeApp.controller('ApplicationCtrl', function($scope, Session, AuthService){
	$scope.Session = Session;
	$scope.AuthService = AuthService;
});

playcodeApp.config([ '$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
	var checkLoggedin = function ($q, $timeout, $window, AuthService) {
		var deferred = $q.defer();

		AuthService.getAuthentication().then(function(user){
			if (user !== '0') {
				$timeout(deferred.resolve, 0);
			} else {
				$timeout(function () {
					deferred.reject();
				}, 0);
				$window.location.href = '/login';
			}
		});

		return deferred.promise;
	};

	$httpProvider.interceptors.push(function ($rootScope, $q, $window) {
		return {
			responseError: function (response) {
				if (response.status === 401)
					$window.location.href = '/login';

				return $q.reject(response);
			}
		}
	});

	$routeProvider.when('/login', {
		title: '로그인',
		templateUrl: '/assets/tpl/login.tpl.html',
		controller: 'LoginCtrl'
	}).when('/signup', {
		title: '회원가입',
		templateUrl: '/assets/tpl/signup.tpl.html',
		controller: 'SignUpCtrl'
	}).when('/article', {
		title: '항목 목록',
		templateUrl: '/assets/tpl/article/list.tpl.html',
		controller: 'ArticleListCtrl'
	}).when('/article/create', {
		title: '항목 생성',
		templateUrl: '/assets/tpl/article/form.tpl.html',
		controller: 'ArticleCreateCtrl',
		resolve: {
			loggedin: checkLoggedin
		}
	}).when('/article/:title/update', {
		title: '항목 수정',
		templateUrl: '/assets/tpl/article/form.tpl.html',
		controller: 'ArticleUpdateCtrl',
		resolve: {
			loggedin: checkLoggedin
		}
	}).when('/article/:title/delete', {
		title: '항목 삭제',
		templateUrl: '/assets/tpl/article/delete.tpl.html',
		controller: 'ArticleDeleteCtrl',
		resolve: {
			loggedin: checkLoggedin
		}
	}).when('/article/:title/revisions', {
		title: '항목 기록',
		templateUrl: '/assets/tpl/article/revisions.tpl.html',
		controller: 'ArticleRevisionsCtrl'
	}).when('/article/:title', {
		title: '항목 보기',
		templateUrl: '/assets/tpl/article/detail.tpl.html',
		controller: 'ArticleDetailCtrl'
	});
} ]);

playcodeApp.run(['$location', '$rootScope', '$http', 'AuthService', function ($location, $rootScope, $http, AuthService) {
	AuthService.getAuthentication();

	$rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
		$rootScope.routeTitle = current.$$route.title;
	});
}]);