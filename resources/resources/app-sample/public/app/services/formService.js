angular.module('formService', [])

.factory('Form', function($http) {

	// create a new object
	var formFactory = {};

	// get a pentaho resource
	formFactory.get = function() 
	{
		return $http.get('/api/form');
	};

	// return our entire pentahoFactory object
	return formFactory;

});