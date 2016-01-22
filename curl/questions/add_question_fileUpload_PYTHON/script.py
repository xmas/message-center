import json


def temp():
    value = 0

    with open(question_dir + '/input.dat') as f:
        content = f.readlines()

        for v in content:
            val = int(v)
            value += val

    value = value/len(content)
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
            'details': 'New value is ' + str(value/3),
            'path': 'Path2',
            'guid': 0
        },
        {
            'title': 'Hello, ' + user,
            'details': 'New value is ' + str(value/4),
            'path': 'Path3',
            'guid': 0
        }
    ]

    json_data = json.dumps(data)

    file_out = open(question_dir + "/answers.json", "w")
    file_out.write(json_data)
    file_out.close()


temp()
