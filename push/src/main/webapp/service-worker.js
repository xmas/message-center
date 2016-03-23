self.addEventListener('push', function (event) {
    event.waitUntil(createDb().then(function () {
        return getMessages(show).then(function () {
            return new Promise(function (res, rej) {
            });
        })
    }))
});

self.addEventListener('notificationclick', function (event) {

    self.registration.getNotifications().then(function(notificationsList){
        notificationsList.forEach(function(notification){
            notification.close();
        })
    });

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
            fetch('users/' + userId + '/messages/v1/', {
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
    var shown;

    self.registration.getNotifications().then(function(notificationsList){
        shown = notificationsList;
        return messages.filter(function(message){
            return ! notificationsList.some(function(notif){
                return notif.data.id == message.id || (notif.data.ids && notif.data.ids.indexOf(message.id) >=0)
            })
        })
    }).then(function(unshownMessages){
        messagesCount = unshownMessages.length;

        for(var i = 0; i < 2 - shown.length && i < unshownMessages.length; i++){
            showPlainNotification(unshownMessages[i]);
            messagesCount--;
        }

        if(messagesCount > 0){
            var ids = [];
            for(i = 0; i < messagesCount; i++){
                ids.push(unshownMessages[i].id)
            }
            if(shown[2] && shown[2].data.ids){
                addToMore(shown[2], ids)
            }else if(shown[2]){
                ids.push(shown[2].data.id);
                addToMore(shown[2], ids)
            }else if(messagesCount > 1){
                showMoreNotification(unshownMessages.slice(unshownMessages.length - messagesCount, unshownMessages.length), messagesCount);
            }else if(messagesCount == 1){
                showPlainNotification(unshownMessages[unshownMessages.length - 1]);
            }
        }

    });
}

function addToMore(notif, ids){
    var data = notif.data;
    if(! data.ids) data.ids = [];

    ids.forEach(function(id){
        if(data.ids) {
            data.ids.push(id)
        }else{
            data.ids = [data.id]
        }
    });

    var title = 'And ' + data.ids.length + ' more ...';

    show(title, {
        body: '',
        icon: notif.icon,
        data: data
    });

    notif.close();
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

function showMoreNotification(messages) {
    var title = 'And ' + messages.length + ' more ...';
    var body = '';
    var icon = messages[0].icon;
    messages[0].ids = [];
    messages.forEach(function(message){
        messages[0].ids.push(message.id)
    });

    show(title, {
        body: body,
        icon: icon,
        data: messages[0]
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

