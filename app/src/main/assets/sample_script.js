window.interface = {
    /**
      * 네이티브로 실행할 함수의 콜백 아이디
      * 고유한 아이디를 가지고, 새로고침해도 겹치지 않도록 random 값을 준다.
      */
    callbackID: Math.floor(Math.random() * 2000000000),

    /**
      * 실행한 함수가 콜백을 실행하기 전까지, 콜백을 저장한다.
      */
    callbacks: {},

    /**
      *
      * 네이티브에서 커맨드를 실행한 후, 네이티브 코드가 호출한다.
      * 인자에 따라서 콜백을 가져온다.
      * @param {number} callbackID - 실행할 때 네이티브에 전송했던 콜백 아이디
      * @param {boolean} isSuccess - 커맨드가 성공적으로 실행되었는지 여부
      * @param {Object} args - 네이티브에서 전송하는 JSON 객체
      * @param {boolean} keepCallback - 콜백을 실행할 필요가 있는지 여부
      */
    nativeCallback: function(callbackID, isSuccess, args, keepCallback) {
        console.log(`nativeCallback callbackID : ${callbackID}, isSuccess : ${isSuccess}, args : ${args}, keepCallback : ${keepCallback}`);
        var callback = window.interface.callbacks[callbackID];
        if (callback) {
            if (isSuccess) {
                if (callback.success) {
                    callback.success.apply(null, [args]);
                }
            } else if (!isSuccess) {
                if (callback.fail) {
                    callback.fail.apply(null, [args]);
                }
            }
            if (!keepCallback) {
                delete window.interface.callbacks[callbackID];
            }
        }
    },

    /**
      * 현재 플랫폼 상태
      */
    platform: function() {
        return 'aos';
    },

    /**
      * 네이티브에 필요한 액션을 실행시킨다.
      * 웹 프론트엔드에서 실행해 네이티브로 명령을 넘긴다.
      * @param {Object} successCallback - 액션이 성공했을 때 불리는 함수 객체
      * @param {Object} failCallback - 액션이 실패했을 때 불리는 함수 객체
      * @param {string} action - 어떤 액션인지 구분하는 값
      * @param {Object} actionArgs - 액션의 인자
      */
    executeInterface: function(successCallback, failCallback, action, actionArgs) {
        var callbackID = null;
        if (successCallback || failCallback) {
            callbackID = window.interface.callbackID;
            window.interface.callbackID += 1;
            window.interface.callbacks[callbackID] = {
                success: successCallback,
                fail: failCallback
            };
        }
        if (window.interface.platform() === 'ios') {
            window.interface.iosCommand(callbackID, action, actionArgs);
        } else if (window.interface.platform() === 'aos') {
            window.interface.aosCommand(callbackID, action, actionArgs);
        }
    },

    /**
      * iOS WKWebView에 스크립트 메시지를 전송하여 명령을 전송한다.
      * @param {number} callbackID - 콜백을 추적하기 위한 아이디
      * @param {string} action - 어떤 액션인지 구분하는 값
      * @param {Object} actionArgs - 액션의 인자
      */
    iosCommand: function(callbackID, action, actionArgs) {
        var callbackIDString = callbackID.toString();
        var message = {
            callbackID: callbackIDString,
            action: action,
            actionArgs: actionArgs
        };
        window.webkit.messageHandlers.interface.postMessage(message);
    },

    /**
      * AOS WebView에 Javascript Interface를 실행하여 명령을 전송한다.
      * @param {number} callbackID - 콜백을 추적하기 위한 아이디
      * @param {string} action - 어떤 액션인지 구분하는 값
      * @param {Object} actionArgs - 액션의 인자
      */
    aosCommand: function(callbackID, action, actionArgs) {
        var actionArgsStringify = JSON.stringify(actionArgs);
        var callbackIDString = callbackID.toString();
        AndroidInterface.requestFromWeb(callbackIDString, action, actionArgsStringify);
    }
};

var onResponseFromNative = function(callbackID, isSuccess, args, keepCallback) {
    window.interface.nativeCallback(callbackID, isSuccess, args, keepCallback);
}

window.showMenu = function (titles, successCallback, failCallback) {
    var action = 'showMenu';
    var args = { titles: titles };
    window.interface.executeInterface(successCallback, failCallback, action, args);
};

window.scanQRcode = function (successCallback, failCallback) {
    var action = "scanQRcode";
    window.interface.executeInterface(successCallback, failCallback, action);
}

window.showMenu(
    ['A', 'B', 'C'],
    function(response) {
        console.log("Success");
    },
    function() {
        console.log('Error');
    }
);

//window.scanQRcode(
//    function(response) {
//        console.log(`QR Code Success : ${JSON.stringify(response)}`);
//    },
//    function() {
//        console.log('Error');
//    }
//);
