var PushManager = function (serviceWorkerPath, callback, userId) {
    initialiseState(callback, userId)
};

initialiseState = function (callback, userId) {
    var permissionData = window.safari.pushNotification.permission('web.com.currentactions');
    checkRemotePermission(permissionData, userId);
};



var checkRemotePermission = function (permissionData, userId) {
    if (permissionData.permission === 'default') {
        window.safari.pushNotification.requestPermission(
            'https://pushserver.currentactions.com:8443/push', // The web service URL.
            'web.com.currentactions',                    // The Website Push ID.
            {"guid": userId},                                   // User guid
            checkRemotePermission                               // The callback function.
        );
    }
    else if (permissionData.permission === 'denied') {
        console.log(permissionData)
    } else if (permissionData.permission === 'granted') {
        console.log(permissionData)
    }
};



