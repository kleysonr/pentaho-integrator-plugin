angular.module('pentahoService', [])

.factory('Pentaho', function($http) {

	// create a new object
	var pentahoFactory = {};

	// get a pentaho resource
	pentahoFactory.get = function(id) {
		return $http.get('/api/res/' + id);
	};

	// return our entire pentahoFactory object
	return pentahoFactory;

});