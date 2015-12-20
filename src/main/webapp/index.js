var pushManager;
var subscripted;
/*$(document).ready(function () {
    if (navigator.userAgent.toLowerCase().indexOf('chrome') > -1) {
        $.getScript( "./ChromePushManager.js" )
            .done(function( script, textStatus ) {
                console.log( textStatus );
            });
    }else if('safari' in window){
        $.getScript( "./SafariPushManager.js" )
            .done(function( script, textStatus ) {
                console.log( textStatus );
            });
    }
});*/

function getUserId(){
    var expr = /^\d+$/;
    if(!expr.test($('#userGUID').val())){
        alert("Set user ID. Only digits !");
        return;
    }
    return $('#userGUID').val();
}

function addUser(){
    if(!getUserId()) return;
    jQuery.ajax({
        url: 'users/' + getUserId(),
        method: 'PUT',
        contentType: "application/json",
        success: function(){
            alert("New user created");
        },
        error: function(e){
            alert(e.responseText);
        }
    });
}

function subscribe() {
    if(!getUserId()) return;
    pushManager = new PushManager('./service-worker.js', function (error, registrationId) {
        if (error) {
            alert(error);
            console.log(error)
        } else {
            sendSubscriptionToServer(registrationId);
            subscripted = true;
            $('.subscribe').prop("disabled", true);
            $('.unsubscribe').prop("disabled", false);
        }
    }, getUserId());
}

function getUsers(){
    $('#users').empty();
    jQuery.ajax({
        url: 'users',
        method: 'GET',
        contentType: "application/json",
        success: function(data){
            data.forEach(function(user){
                $('#users').append('<li>' + user.guid + '</li>')
            })
        },
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

function unSubscribe() {
    if(!getUserId()) return;
    if(pushManager) {
        pushManager.removeSubscription(removeSubscriptionFromServer);
    }else{
        pushManager = new ChromePushManager('./service-worker.js', function(){}, getUserId());
        pushManager.removeSubscription(removeSubscriptionFromServer);
    }
        $('.subscribe').prop("disabled", false);
        $('.unsubscribe').prop("disabled", true);
}

function sendNotification() {
    if(!getUserId()) return;
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

function removeSubscriptionFromServer(subscriptionId) {
    jQuery.ajax({
        url: 'users/' + getUserId() + '/devices/'+ subscriptionId,
        method: 'DELETE',
        contentType: "application/json",
        success: function(){
            alert("Subscription was deleted")
        },
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

function getDevices(){
    $('#devices').empty();
    jQuery.ajax({
        url: 'users/' + getUserId() + '/devices',
        method: 'GET',
        contentType: "application/json",
        success: function(data){
            data.forEach(function(device){
                $('#devices').append('<li>' + device.medium.name + '-' + device.ip + ':' + device.location +'</li>')
            })
        },
        error: function (data) {
            console.log('Error while sending to server: ' + data);
        }
    });
}

