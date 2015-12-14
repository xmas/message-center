
self.addEventListener('push', function (event) {
    getMessages(show)
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    setRead(event.notification.data);
});

self.addEventListener('message', function (evt) {
    self.userId = evt.data.id;
    console.log('postMessage received', evt.data);
});

function setRead(id){
    fetch('users/' + self.userId + '/messages/v1/'+id, {
        method: "POST"
    }).catch(function(e){
        console.log(e);
    });
}

function show(title, notif) {
    self.registration.showNotification(title, notif);
}

function getMessages(calback) {
    fetch('users/' + self.userId + '/messages/v1/unread')
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
        })
        .catch(function (err) {
            console.log('Fetch Error :-S', err);
        });
}

