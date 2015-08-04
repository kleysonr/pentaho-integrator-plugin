
angular.module('formCtrl', ['formService', 'authService'])

.controller('formController', function($scope, Form) {

	var vm = this;
	
	var spinner_opts = {
		  lines: 13 // The number of lines to draw
		, length: 6 // The length of each line
		, width: 6 // The line thickness
		, radius: 12 // The radius of the inner circle
		, scale: 1 // Scales overall size of the spinner
		, corners: 1 // Corner roundness (0..1)
		, color: '#000' // #rgb or #rrggbb or array of colors
		, opacity: 0.25 // Opacity of the lines
		, rotate: 0 // The rotation offset
		, direction: 1 // 1: clockwise, -1: counterclockwise
		, speed: 1 // Rounds per second
		, trail: 60 // Afterglow percentage
		, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
		, zIndex: 2e9 // The z-index (defaults to 2000000000)
		, className: 'spinner' // The CSS class to assign to the spinner
		, top: '50%' // Top position relative to parent
		, left: '50%' // Left position relative to parent
		, shadow: false // Whether to render a shadow
		, hwaccel: false // Whether to use hardware acceleration
		, position: 'absolute' // Element positioning
	};

	var target = document.getElementById('principal');
	var spinner = new Spinner(spinner_opts);

			
	
	$scope.myForm = {};
	
	// set a processing variable to show loading things
	vm.processing = true;
	
    $scope.setFile = function (elem) {
        $scope.inputField = elem;
        $scope.myForm.file = elem.files[0];
    };
	
	vm.send = function() {
		
		spinner.spin(target);

		Form.get().success(function(data) 
		{
			if (data.url) 
			{
				var formData = new FormData();
			    formData.append('file', $scope.myForm.file);
			    formData.append('prefix', $scope.myForm.prefix);
			    formData.append('solution', $scope.myForm.solution);
			    formData.append('newExt', $scope.myForm.newExt);
			    formData.append('description', $scope.myForm.description);

			    var xhr = new XMLHttpRequest();
		        
		        xhr.open("POST", data.url);
		        
		        xhr.onerror = function (evt) 
		        {
		        	spinner.stop();
		            console.log("ERROR http post.")
		        };
		        
		        xhr.onload = function (evt) 
		        {
		        	spinner.stop();
		            
		            var result = JSON.parse(evt.target.responseText);
		            
		            $("#message-box").removeClass();
		            if (result.error == "true") 
		            {
		            	$("#message-box").addClass("alert alert-danger");
		            	$("#message-alert").text(result.error_message + ": " + result.message);
		            }
		            else 
		            {
		            	$("#message-box").addClass("alert alert-success");
		            	$("#message-alert").text(result.message + ": "+ result.url);
		            }
		            
		            $("#message-box").alert();
		            $("#message-box").fadeTo(10000, 500).slideUp(500, function(){
		            	$("#message-box").hide();
		            });
		            
		        };
		        
		        xhr.send(formData);
			}
		});
	}
	
	
});