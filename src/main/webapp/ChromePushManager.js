var ChromePushManager = function (serviceWorkerPath, callback, userId) {
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register(serviceWorkerPath)
            .then(initialiseState(callback, userId));
    } else {
        callback('Service workers aren\'t supported in this browser.', null);
    }
};

initialiseState = function (callback, userId) {
    if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
        callback('Notifications aren\'t supported.', null);
    } else if (Notification.permission === 'denied') {
        callback('The user has blocked notifications.', null);
    } else if (!('PushManager' in window)) {
        callback('Push messaging isn\'t supported.', null);
    } else {
        //Pass user GUID to service worker
        //It is necessary because worker have to get messages for this user
        navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
            serviceWorkerRegistration.active.postMessage({id: userId});
        });
        //Make subscription
        //If subscription already made unsubscribe, and make new.
        getAliveSubscription(function (error, subscriptionId) {
            if (!subscriptionId) {
                subscribeBrowserId(callback);
            } else {
                callback(null, subscriptionId)
            }
        });
    }
};

subscribeBrowserId = function (callback) {
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        processSubscription(serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true}), callback)
    });
};
getAliveSubscription = function (callback) {
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        processSubscription(serviceWorkerRegistration.pushManager.getSubscription(), callback)
    });
};

processSubscription = function (promise, callback) {
    promise
        .then(function (subscription) {
            processSuccess(subscription, callback)
        })
        .catch(function () {
            handleError(callback)
        });
};

processSuccess = function (subscription, callback) {
    if (subscription) {
        var registrationId = getRegistrationId(subscription);
        callback(null, registrationId);
    } else {
        callback('Unable to subscribe to push.', null);
    }
};

handleError = function (callback) {
    if (Notification.permission === 'denied') {
        callback('Permission for Notifications was denied', null);
    } else {
        callback('Unable to subscribe to push.', null);
    }
};

ChromePushManager.prototype.removeSubscription = function (callback) {
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.getSubscription()
            .then(function (subscription) {
                if (subscription) {
                    subscription.unsubscribe();
                    callback(getRegistrationId(subscription))
                }
            })
    });
};

getRegistrationId = function (pushSubscription) {
    if (pushSubscription.subscriptionId) {
        return pushSubscription.subscriptionId;
    }

    var endpoint = 'https://android.googleapis.com/gcm/send/';
    parts = pushSubscription.endpoint.split(endpoint);

    if (parts.length > 1) {
        return parts[1];
    }

};
