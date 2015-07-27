var config 	   = require('../../config');
var pg 		     = require('pg');

var conString = config.postgresConnectionString;

// Create tokens bi table
exports.createTable = function() {

 	pg.connect(conString, function(err, client, done) {
 		
 		if (err) console.log(err);

 		client.query(config.createTableSql, function(err, result) {

			    // handle an error from the query
          if(handleError(client, err, done)) return;
        	
          done();

          return true;
 		});

	});

};

// Create tokens bi table
exports.insertToken = function(values, cb) {

	pg.connect(conString, function(err, client, done) {

		if (err) console.log(err);

		client.query(config.insertTokenSql, values, function(err, result) {

			// handle an error from the query
			if(handleError(client, err, done)) return;
          
			done();

			cb();

			return true;
		});

	});

};

var handleError = function(client, err, done) {
      // no error occurred, continue with the request
      if(!err) return false;

      // An error occurred, remove the client from the connection pool.
      // A truthy value passed to done will remove the connection from the pool
      // instead of simply returning it to be reused.
      // In this case, if we have successfully received a client (truthy)
      // then it will be removed from the pool.
      done(client);

      return true;
};
