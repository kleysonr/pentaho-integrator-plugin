module.exports = {
	'port': process.env.PORT || 1010,
	'secret': 'password',
	'postgresConnectionString': 'postgres://postgres:postgres@localhost:5432/postgres',
	'createTableSql': 'CREATE TABLE public.tokens_bi(username character varying(32) NOT NULL, token character varying(100) NOT NULL, url character varying(255) NOT NULL)',
	'insertTokenSql': 'INSERT INTO tokens_bi(username, token, url) VALUES ($1, $2, $3)',
	'integratorPlugin': 'http://localhost:8080/pentaho/plugin/integrator/api/go',
	'resources' : {
		'ctoolsDash': '/pentaho/api/repos/:public:Steel Wheels:Dashboards:CTools_dashboard.wcdf/generatedContent',
		'cdfDash': '/pentaho/api/repos/%3Apublic%3ASteel Wheels%3ADashboards%3AHome Dashboard.xcdf/generatedContent',
		'xActionGetEnv': '/pentaho/api/repos/:public:Steel Wheels:Data Integration:GetPDIEnvironment.xaction/generatedContent',
		'PrdTopCustomers': '/pentaho/api/repos/%3Apublic%3ASteel Wheels%3AReports%3ATop Customers (report).prpt/viewer'
	}
};