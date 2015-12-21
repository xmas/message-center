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
    setRead(event.notification.data);
});

self.addEventListener('notificationclose', function (event) {
    event.notification.close();
    setRead(event.notification.data);
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
    return self.registration.showNotification(title, notif).then(function (NEvent) {
        NEvent.notification.onclose = function () {
            console.log("sgh")
        }
    });
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
    var count = 0;
    messages.every(function (message) {
        if (count++ < 2) {
            showPlainNotification(message);
            return true;
        } else {
            showMoreNotification(message, messages.length - count + 1);
            return false;
        }
    })
}

function showPlainNotification(message) {
    var title = message.title;
    var body = message.message;
    var icon = message.icon;

    show(title, {
        body: body,
        icon: icon,
        data: message.id
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

