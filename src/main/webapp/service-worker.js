self.addEventListener('push', function (event) {
    createDb();
    getMessages(show)
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    setRead(event.notification.data);
});

self.addEventListener('message', function (evt) {
    createDb();
    storeUserId(self.db, evt.data.id);
    console.log('postMessage received', evt.data);
});

function setRead(id){
    getUserId(self.db, function(userId){
        fetch('users/' + userId + '/messages/v1/'+id, {
            method: "POST"
        }).catch(function(e){
            console.log(e);
        });
    });
}

function show(title, notif) {
    self.registration.showNotification(title, notif);
}

function getMessages(calback) {
    getUserId(self.db, function(userId){
        fetch('users/' + userId + '/messages/v1/unread')
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
    });
}

function createDb(){
    if(! self.db) {
        importScripts("./Dexie.js");
        self.db = new Dexie("pushDatabase");
        db.version(1).stores({values: "name, value"});
        db.open();
    }
}

function getUserId(db, callback){
    db.values.where("name").equals("userId").first(function(item){
        callback(item.value);
    })
}

function storeUserId(db, userId){
    db.values.put({name: "userId", value: userId});
}

