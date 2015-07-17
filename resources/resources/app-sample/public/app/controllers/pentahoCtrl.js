angular.module('pentahoCtrl', ['pentahoService', 'authService'])

.controller('pentahoController', function($location, $window, Pentaho, AuthToken) {

	var vm = this;

	// set a processing variable to show loading things
	vm.processing = true;

	// 
	vm.getPentahoRes = function(id) {

		vm.processing = true;

		Pentaho.get(id)
		.success(function(data) {

			// when come back, remove the processing variable
			vm.processing = false;

			if (data.url) 
			{
				$window.open(data.url);
				$location.path('/portal');
			}
		});
	};

});