self.addEventListener('push', function (event) {
    event.waitUntil(createDb().then(function () {
        return getMessages(show).then(function () {
            return new Promise(function (res, rej) {
            });
        })
    }))
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    setRead(event.notification.data.id);

    event.waitUntil(clients.matchAll({
        type: "window"
    }).then(function(clientList) {
        for (var i = 0; i < clientList.length; i++) {
            var client = clientList[i];
            if (client.url == event.notification.data.notificationAppURL && 'focus' in client)
                return client.focus();
        }
        if (clients.openWindow)
            return clients.openWindow(event.notification.data.notificationAppURL);
    }));
});

self.addEventListener('message', function (evt) {
    createDb();
    storeUserId(self.db, evt.data.id);
    console.log('postMessage received', evt.data);
});

function setRead(id) {
    if (id)
        getUserId(self.db).then(function (userId) {
            fetch('users/' + userId + '/messages/v1/' + id, {
                method: "POST"
            }).catch(function (e) {
                console.log(e);
            });
        });
}

function show(title, notif) {
    return self.registration.showNotification(title, notif)
}

function getMessages() {
    return getUserId(self.db)
        .then(function (userId) {
            return fetch('users/' + userId + '/messages/v1/unread')
        })
        .then(function (data) {
            return data.json()
        })
        .then(showMessages);
}

function showMessages(messages) {
    for(var i = 0; i < 2 && i < messages.length; i++){
        showPlainNotification(messages[i]);
    }

    var rest = messages.length - 2;
    if(rest > 1){
        showMoreNotification(messages[2], rest);
    }else if(rest == 1){
        showPlainNotification(messages[2]);
    }

}

function showPlainNotification(message) {
    var title = message.title;
    var body = message.message;
    var icon = message.icon;

    show(title, {
        body: body,
        icon: icon,
        data: message
    });
}

function showMoreNotification(message, count) {
    var title = 'And ' + count + ' more ...';
    var body = '';
    var icon = message.icon;

    show(title, {
        body: body,
        icon: icon,
        data: message.id
    });
}

function createDb() {
    if (!self.db) {
        importScripts("./Dexie.js");
        self.db = new Dexie("pushDatabase");
        db.version(1).stores({values: "name, value"});
        return db.open();
    } else {
        return new Promise(function (resolve) {
            resolve();
        });
    }
}

function getUserId(db) {
    return new Promise(function (resolve) {
        db.values.where("name").equals("userId").first(function (item) {
            resolve(item.value);
        })
    });
}

function storeUserId(db, userId) {
    db.values.put({name: "userId", value: userId});
}

