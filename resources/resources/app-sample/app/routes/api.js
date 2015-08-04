var bodyParser = require('body-parser'); 	// get body-parser
var jwt        = require('jsonwebtoken');
var config     = require('../../config');
var token	   = require('./../models/token');


// super secret for creating tokens
var superSecret = config.secret;

module.exports = function(app, express) {

	var apiRouter = express.Router();

	// route to authenticate a user (POST http://localhost:<port>/api/authenticate)
	apiRouter.post('/authenticate', function(req, res) {

	    var passwordDefault = 'password';

	    // no user with that username was found
	    if (req.body.username != "admin" && req.body.username != "suzy" ) 
	    {
	    	res.json({ 
	      		success: false, 
	      		message: 'Authentication failed. User not found.' 
	    	});
	    } 
	    else
	    {
	      	// check if password matches
	      	if (passwordDefault != req.body.password) {
	        	res.json({ 
	        		success: false, 
	        		message: 'Authentication failed. Wrong password.' 
	      		});
	      	}
	      	else
	      	{
	        	// if user is found and password is right
	        	// create a token
	        	var token = jwt.sign({
	        		name: (req.body.username == "admin"?'Administrator':'Suzy Lee'),
	        		username: req.body.username
	        	}, superSecret, {
	          		expiresInMinutes: 1440 // expires in 24 hours
	        	});

	        	// return the information including token as JSON
	        	res.json({
	          		success: true,
	          		message: 'Enjoy your token!',
	          		token: token
	        	});
	      	}   
	    }
	});

	// route middleware to verify a token
	apiRouter.use(function(req, res, next) {
		// do logging
		console.log('Somebody just came to our app!');

	  // check header or url parameters or post parameters for token
	  var token = req.body.token || req.param('token') || req.headers['x-access-token'];

	  // decode token
	  if (token) {

	    // verifies secret and checks exp
	    jwt.verify(token, superSecret, function(err, decoded) {      

	      if (err) {
	        res.status(403).send({ 
	        	success: false, 
	        	message: 'Failed to authenticate token.' 
	    	});  	   
	      } else { 
	        // if everything is good, save to request for use in other routes
	        req.decoded = decoded;
	            
	        next(); // make sure we go to the next routes and don't stop here
	      }
	    });

	  } else {

	    // if there is no token
	    // return an HTTP response of 403 (access forbidden) and an error message
   	 	res.status(403).send({ 
   	 		success: false, 
   	 		message: 'No token provided.' 
   	 	});
	    
	  }
	});

	// test route to make sure everything is working 
	// accessed at GET http://localhost:8080/api
	apiRouter.get('/', function(req, res) {
		res.json({ message: 'hooray! welcome to our api!' });	
	});

	// Endpoint to Pentaho Dashboard
	// ----------------------------------------------------
	apiRouter.get('/res/:id', function(req, res) {

		var urlEncoded = new Buffer(config.resources[req.params.id]).toString('base64');
		var newToken = (new Date).getTime();

		//Create token to access
		token.insertToken([req.decoded.username, newToken, urlEncoded], function(){

			res.json({url: config.integratorPlugin + '?type=token&token=' + newToken + '&urlEncoded=' + urlEncoded});

		});


	});

	// Endpoint to Form Upload
	// ----------------------------------------------------
	apiRouter.get('/form', function(req, res) {

		var urlEncoded = new Buffer(config.resources.UploadPlugin).toString('base64');
		var newToken = (new Date).getTime();

		//Create token to access
		token.insertToken([req.decoded.username, newToken, urlEncoded], function(){

			res.json({url: config.uploadfilePlugin + '?type=token&token=' + newToken + '&urlEncoded=' + urlEncoded});

		});


	});
	

	// api endpoint to get user information
	apiRouter.get('/me', function(req, res) {
		res.send(req.decoded);
	});

	return apiRouter;
};