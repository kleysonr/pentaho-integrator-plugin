angular.module('userApp', ['ngAnimate', 'app.routes', 'authService', 'mainCtrl', 'pentahoCtrl', 'pentahoService', 'formCtrl', 'formService'])

// application configuration to integrate token into requests
.config(function($httpProvider) {

	// attach our auth interceptor to the http requests
	$httpProvider.interceptors.push('AuthInterceptor');

});