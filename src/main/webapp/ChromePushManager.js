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
        //If subscription was already created just get it else make new.
        getAliveSubscription(function (error, sbscriptionId) {
            if (error) {
                subscribeBrowserId(callback);
            } else {
                callback(null, sbscriptionId);
            }
        });
    }
};

subscribeBrowserId = function (callback) {
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true})
            .then(function (subscription) {
                if (subscription) {
                    var register = getRegistrationId(subscription);
                    callback(null, register);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            })
            .catch(function (e) {
                if (Notification.permission === 'denied') {
                    callback('Permission for Notifications was denied', null);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            });
    });
};
getAliveSubscription = function (callback) {
    var this_ = this;
    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.getSubscription()
            .then(function (subscription) {
                if (subscription) {
                    var register = getRegistrationId(subscription);
                    callback(null, register);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            })
            .catch(function (e) {
                if (Notification.permission === 'denied') {
                    callback('Permission for Notifications was denied', null);
                } else {
                    callback('Unable to subscribe to push.', null);
                }
            });
    });
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
