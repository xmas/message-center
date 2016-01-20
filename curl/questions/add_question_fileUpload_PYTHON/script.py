from string import Template


def temp():
    template_name = question_dir + '/template/template.json'
    filein = open(template_name)
    src = Template(filein.read())

    value = 0

    with open(question_dir + '/input.dat') as f:
        content = f.readlines()

        for v in content:
            val = int(v)
            value += val

    value = value/len(content)

    user = "PythonUser"

    d = {'user': user, 'value': value}

    result = src.substitute(d)

    file_out = open(question_dir + "/answer.json", "w")
    file_out.write(result)
    file_out.close()


temp()
