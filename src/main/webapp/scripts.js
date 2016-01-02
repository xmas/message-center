'use strict';

function getScripts(){
    $('#scripts').empty();
    jQuery.ajax({
        url: 'R',
        method: 'GET',
        success: function(data){
            data.forEach(function(script){
                $('#scripts').append('<li>' + script.id + '</li>')
            })
        },
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

function evalScripts(){
    var id = getScriptId();
    $("#evForm").attr('action', '/push/R/' + id + '/evaluate');
    return true;
}

function getScriptId(){
    var expr = /^\d+$/;
    if(!expr.test($('#scriptId').val())){
        alert("Set script ID. Only digits !");
        return;
    }
    return $('#scriptId').val();
}