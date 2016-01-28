import json


def temp():
    value = 0

    user = "PythonUser"
    data = [
            {
                'title': 'Hello, ' + user,
                'details': 'New value is ' + str(value/2),
                'path': 'Path1',
                'guid': 0
            },
            {
                'title': 'Hello, ' + user,
                'details': 'New value is ' + str(value/2),
                'path': 'Path2',
                'guid': 0
            },
            {
                'title': 'Hello, ' + user,
                'details': 'New value is ' + str(value/2),
                'path': 'Path3',
                'guid': 0
            }
        ]

    json_data = json.dumps(data)

    file_out = open(question_dir + "/answers.json", "w")
    file_out.write(json_data)
    file_out.close()


temp()
