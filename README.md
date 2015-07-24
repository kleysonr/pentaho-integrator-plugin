# What is it?

It is a Pentaho plugin that allows you to access from other applications, any resource from the Pentaho BA Server, 

such as report/dashboard/etc, without providing a userid and password.

# How it works?

There is a database table that can be read by the application and the BA Server.

The application insert a new record on the table, indexed by the tokenid.

Then the plugin use the tokenid to validate the request and give access to the asked resource.

# How to install

Use the Marketplace

# More information

To get more information about the configuration and how to test it using a sample application, read

http://kleysonrios.blogspot.com.br/2015/07/integrator-plugin-for-pentaho-5.html

# Contributions

If somebody sees any improvement opportunity, please feel free to suggest and contribute.

