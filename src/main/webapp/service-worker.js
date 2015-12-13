
self.addEventListener('push', function (event) {
    getMessages(show)
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    setRead(event.notification.data);
});

self.addEventListener('notificationclose', function (event) {
    setRead(event.notification.data);
});

function setRead(id){
    fetch("users/123456/messages/v1/"+id, {
        method: "POST"
    }).catch(function(e){
        console.log(e);
    });
}

function show(title, notif) {
    self.registration.showNotification(title, notif)
}

function getMessages(calback) {
    fetch('users/123456/messages/v1/unread')
        .then(
        function (data) {
            data.json().then(function (messages) {
                messages.forEach(function (message) {
                    var title = message.title;
                    var body = message.message;
                    var icon = message.icon;

                    calback(title, {
                        body: body,
                        icon: icon,
                        data: message.id
                    });
                });
            });
        }
    )
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}

