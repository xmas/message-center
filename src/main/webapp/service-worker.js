var isPushEnabled = false;

function initialiseState() {
    if (!('showNotification' in ServiceWorkerRegistration.prototype)) {
        console.warn('Notifications aren\'t supported.');
        return;
    }

    if (Notification.permission === 'denied') {
        console.warn('The user has blocked notifications.');
        return;
    }

    if (!('PushManager' in window)) {
        console.warn('Push messaging isn\'t supported.');
        return;
    }

    navigator.serviceWorker.ready.then(function (serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.getSubscription({userVisibleOnly: true})
            .then(function (subscription) {
                var pushButton = document.querySelector('.js-push-button');
                pushButton.disabled = false;

                console.log(subscription);

                if (!subscription) {
                    return;
                }

                pushButton.textContent = 'Disable Push Messages';
                isPushEnabled = true;
            });

        navigator.serviceWorker.addEventListener('push', function(event) {
            console.log('Received a push message', event);

            var title = 'Yay a message.';
            var body = 'We have received a push message.';
            var icon = '/images/icon-192x192.png';
            var tag = 'simple-push-demo-notification-tag';

            event.waitUntil(
                self.registration.showNotification(title, {
                    body: body,
                    icon: icon,
                    tag: tag
                })
            );
        });

    });
}

function sendSubscriptionToServer(subscription){
    var data = {
        medium: {
            name: "chrome"
        },
        token: subscription.endpoint
    };


    jQuery.ajax({
        url:'users/123456/devices',
        method: 'POST',
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(){
            console.log('Draw line successfully sent to server');
        },
        error: function(){
            console.log('Error occured during ');
        }
    });
}

function subscribe() {
    // Disable the button so it can't be changed while
    // we process the permission request
    var pushButton = document.querySelector('.js-push-button');
    pushButton.disabled = true;

    navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {
        serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true})
            .then(function(subscription) {
                // The subscription was successful
                isPushEnabled = true;
                pushButton.textContent = 'Disable Push Messages';
                pushButton.disabled = false;

                // TODO: Send the subscription.endpoint to your server
                // and save it to send a push message at a later date
                return sendSubscriptionToServer(subscription);
            })
            .catch(function(e) {
                if (Notification.permission === 'denied') {
                    // The user denied the notification permission which
                    // means we failed to subscribe and the user will need
                    // to manually change the notification permission to
                    // subscribe to push messages
                    console.warn('Permission for Notifications was denied');
                    pushButton.disabled = true;
                } else {
                    // A problem occurred with the subscription; common reasons
                    // include network errors, and lacking gcm_sender_id and/or
                    // gcm_user_visible_only in the manifest.
                    console.error('Unable to subscribe to push.', e);
                    pushButton.disabled = false;
                    pushButton.textContent = 'Enable Push Messages';
                }
            });
    });
}

function unsubscribe() {
    var pushButton = document.querySelector('.js-push-button');
    pushButton.disabled = true;

    navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {
        // To unsubscribe from push messaging, you need get the
        // subscription object, which you can call unsubscribe() on.
        serviceWorkerRegistration.pushManager.getSubscription().then(
            function(pushSubscription) {
                // Check we have a subscription to unsubscribe
                if (!pushSubscription) {
                    // No subscription object, so set the state
                    // to allow the user to subscribe to push
                    isPushEnabled = false;
                    pushButton.disabled = false;
                    pushButton.textContent = 'Enable Push Messages';
                    return;
                }

                var subscriptionId = pushSubscription.subscriptionId;
                // TODO: Make a request to your server to remove
                // the subscriptionId from your data store so you
                // don't attempt to send them push messages anymore

                // We have a subscription, so call unsubscribe on it
                pushSubscription.unsubscribe().then(function(successful) {
                    pushButton.disabled = false;
                    pushButton.textContent = 'Enable Push Messages';
                    isPushEnabled = false;
                }).catch(function(e) {
                    // We failed to unsubscribe, this can lead to
                    // an unusual state, so may be best to remove
                    // the users data from your data store and
                    // inform the user that you have done so

                    console.log('Unsubscription error: ', e);
                    pushButton.disabled = false;
                    pushButton.textContent = 'Enable Push Messages';
                });
            }).catch(function(e) {
                console.error('Error thrown while unsubscribing from push messaging.', e);
            });
    });
}

