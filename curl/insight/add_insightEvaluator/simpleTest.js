#!/usr/local/bin/node
var fs = require('fs');

var insights = [
    {
        answers: [1, 2, 3],
        parameters: {
            param1: "value1",
            param2: 2,
            param3: true
        },
        title: "Super insight",
        details: "Some details about this insight",
        source: "www.google.com"
    },
    {
        answers: [4, 5, 6],
        parameters: {
            param1: "value3",
            param2: 4,
            param3: false
        },
        title: "Super insight 2",
        details: "Some details about this insight",
        source: "www.google.com"
    }
];

var diff = [
    {
        "kind": "E",
        "path": [
            "data",
            "aggregates",
            0,
            "label"
        ],
        "lhs": "2",
        "rhs": "1"
    },
    {
        "kind": "E",
        "path": [
            "data",
            "aggregates",
            0,
            "value"
        ],
        "lhs": 2,
        "rhs": 1
    },
    {
        "kind": "A",
        "path": [
            "data",
            "rows"
        ],
        "index": 1,
        "item": {
            "kind": "D",
            "lhs": {
                "dataCells": [
                    {
                        "label": "-",
                        "value": "00Q6100000BSOJJEA5"
                    },
                    {
                        "label": "sadssdf",
                        "value": "00Q6100000BSOJJEA5"
                    },
                    {
                        "label": "-",
                        "value": null
                    },
                    {
                        "label": "sdfsfsdf",
                        "value": "00Q6100000BSOJJEA5"
                    },
                    {
                        "label": "-",
                        "value": null
                    },
                    {
                        "label": "Rowan Christmas",
                        "value": "005610000012IXIAA2"
                    },
                    {
                        "label": "2/9/2016",
                        "value": "2016-02-09"
                    }
                ]
            }
        }
    }
];

saveOutput("insights.json", JSON.stringify(insights));
saveOutput("diff.json", JSON.stringify(diff));

function saveOutput(filename, output) {

    //noinspection JSUnresolvedFunction
    fs.writeFile(filename, output, function (err) {
        if (err) {
            return console.log(err);
        }

        console.log("The file was saved!");
    });
}
