from string import Template


def temp():
    template_name = question_dir + '/template/template.json'
    filein = open(template_name)
    src = Template(filein.read())

    value = 123456798

    user = "PythonUser"

    d = {'user': user, 'value': value}

    result = src.substitute(d)

    file_out = open(question_dir + "/answer.json", "w")
    file_out.write(result)
    file_out.close()


temp()
