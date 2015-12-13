var chromePushManager;
var subscripted;
$(document).ready(function () {
    var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
    if (!is_chrome) {
        alert("No can do ... this demo requires Chrome 42+");
    }

});

function getUserId(){
    var expr = /^\d+$/;
    if(!expr.test($('#userGUID').val())){
        alert("Set user ID. Only digits !");
        return;
    }
    return $('#userGUID').val();
}

function addUser(){
    jQuery.ajax({
        url: 'users/' + getUserId(),
        method: 'PUT',
        contentType: "application/json"
    });
}

function subscribe() {
    chromePushManager = new ChromePushManager('./service-worker.js', function (error, registrationId) {
        if (error) {
            alert(error);
            console.log(error)
        } else {
            sendSubscriptionToServer(registrationId);
            subscripted = true;
            $('.subscribe').prop("disabled", true);
            $('.unsubscribe').prop("disabled", false);
            $('.sendNotification').prop("disabled", false);
        }
    });
}

function unsubscribe() {
    chromePushManager.unsubscribe();
    $('.subscribe').prop("disabled", false);
    $('.unsubscribe').prop("disabled", true);
    $('.sendNotification').prop("disabled", true);
}

function sendNotification() {
    var data = {
        "message": "Hello world!",
        "title": "Hello",
        "subTitle": "Hi",
        "icon": "http://www.google.com/someIcon.png",
        "expiration": "2015-12-12T19:20:39",
        "notificationAppURL": "http://www.somehost.com",
        "mimeType": "text/plain",
        "messageType": "Alert",
        "mediums": [{"name": "chrome"}],
        "users": [{"guid": getUserId()}]
    };

    jQuery.ajax({
        url: 'messages/v1',
        method: 'POST',
        data: JSON.stringify(data),
        contentType: "application/json",
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

function sendSubscriptionToServer(subscriptionId) {
    var data = {
        medium: {
            name: "chrome"
        },
        token: subscriptionId
    };
    jQuery.ajax({
        url: 'users/' + getUserId() + '/devices',
        method: 'POST',
        data: JSON.stringify(data),
        contentType: "application/json",
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

