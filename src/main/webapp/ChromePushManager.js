
var ChromePushManager = function(serviceWorkerPath, callback){
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register(serviceWorkerPath)
            .then(ChromePushManager.initialiseState(callback));
    } else {
        callback('Service workers aren\'t supported in this browser.', null);
    }
};

ChromePushManager.initialiseState = function (callback) {
    if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
        callback('Notifications aren\'t supported.', null);
    } else if (Notification.permission === 'denied') {
        callback('The user has blocked notifications.', null);
    } else if (!('PushManager' in window)) {
        callback('Push messaging isn\'t supported.', null);
    } else {
        //Pass user GUID to service worker
        //It is necessary because worker have to get messages for this user
        //Sometimes navigator.serviceWorker.controller is null so setting userId will be failed
        //I've not figured out why yet
        //It can be fixed by updating page
        var userId = $('#userGUID').val();
        navigator.serviceWorker.controller.postMessage({id: userId});
        //Make subscription
        //If subscription was already created just get it else make new.
        ChromePushManager.getAliveSubscription(function(error, sbscriptionId){
            if(error){
                ChromePushManager.subscribeBrowserId(callback);
            }else{
                callback(null, sbscriptionId);
            }
        });
    }
};

ChromePushManager.subscribeBrowserId = function(callback) {
    var this_ = this;
    navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true})
            .then(function(subscription) {
                if(subscription){
                    this_.subscription = subscription;
                    var register = ChromePushManager.getRegistrationId(subscription);
                    callback(null, register);
                }else{
                    callback('Unable to subscribe to push.', null);
                }
            })
            .catch(function(e) {
                if (Notification.permission === 'denied') {
                    callback('Permission for Notifications was denied', null);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            });
    });
};
ChromePushManager.getAliveSubscription = function(callback){
    var this_ = this;
    navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.getSubscription()
            .then(function(subscription) {
                if(subscription){
                    this_.subscription = subscription;
                    var register = ChromePushManager.getRegistrationId(subscription);
                    callback(null, register);
                }else{
                    callback('Unable to subscribe to push.', null);
                }
            })
            .catch(function(e) {
                if (Notification.permission === 'denied') {
                    callback('Permission for Notifications was denied', null);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            });
    });
};

ChromePushManager.unSubscribe = function(){
    this.subscription.unsubscribe();
};

ChromePushManager.getRegistrationId = function(pushSubscription) {
    if (pushSubscription.subscriptionId) {
        return pushSubscription.subscriptionId;
    }

    var endpoint = 'https://android.googleapis.com/gcm/send/';
    parts = pushSubscription.endpoint.split(endpoint);

    if(parts.length > 1)
    {
        return parts[1];
    }

} ;
