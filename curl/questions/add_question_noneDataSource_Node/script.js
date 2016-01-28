#!/usr/bin/nodejs
var jsforce = require('jsforce');
var fs = require('fs');

var conn = new jsforce.Connection({
  // you can change loginUrl to connect to sandbox or prerelease env.
  //loginUrl : 'https://na34.salesforce.com'
});
conn.login('rowanxmas@gmail.com', '111qqqSSS8wDvDVUSsCXWJfMViL5cSgVKx', function(err, res) {
    if (err) { return console.error(err); }
    // Now you can get the access token and instance URL information.
    // Save them to establish connection next time.
    // console.log('access token: '+conn.accessToken);
    // console.log('instanceUrl: '+conn.instanceUrl);
    // // logged in user property
    // console.log("User ID: " + res.id);
    // console.log("Org ID: " + res.organizationId);
    // ...

    getReports();
});

function getReports() {
    conn.analytics.reports(function(err, reports) {
        if (err) { return console.error(err); }

        console.log("reports length: "+reports.length);

        var lines = [];
        for (var i=0; i < reports.length; i++) {
            console.log(reports[i].id);
            console.log(reports[i].name);
            var line = [reports[i].id, reports[i].name];
            lines.push(line);
        }
        saveOutput("out.json", JSON.stringify(lines));

        var answer = {};
        answer['title'] = 'Report List';
        answer['details'] = 'Found '+lines.length + ' reports.';
        answer['path'] = 'reportList';
        answer['guid'] = 0;
        saveOutput("answers.json", JSON.stringify(answer));
    });


}



function saveOutput (filename, output) {

    fs.writeFile(filename,output, function(err) {
        if(err) {
            return console.log(err);
        }

        console.log("The file was saved!");
    });
}
