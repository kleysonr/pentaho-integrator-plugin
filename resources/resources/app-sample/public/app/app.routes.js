angular.module('app.routes', ['ngRoute'])

.config(function($routeProvider, $locationProvider) {

	$routeProvider

		// route for the home page
		.when('/', {
			templateUrl : 'app/views/pages/home.html'
		})
		
		// login page
		.when('/login', {
			templateUrl : 'app/views/pages/login.html',
   			controller  : 'mainController',
    			controllerAs: 'login'
		})
		
		// apps
		.when('/portal', {
			templateUrl: 'app/views/pages/users/portal.html',
			controller: 'pentahoController',
			controllerAs: 'pentaho'
		})

		// Upload form
		.when('/upload', {
			templateUrl: 'app/views/pages/users/formUpload.html'
		});

	$locationProvider.html5Mode(true);

});
