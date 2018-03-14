var wsURI = "ws://" + document.location.host + "/ws";
var ws = new WebSocket(wsURI);

var $user = document.getElementById("user");
var $password = document.getElementById("password");
var $dbsource = document.getElementById("dbsource");

var $textarea = document.getElementById("messages");

ws.onopen = function() {
    console.log("WebSocket opened: " + ws);
    console.log("URI: " + wsURI);
}

ws.onclose = function (evt) {
    console.log("WebSocket closed");
}

ws.onerror = function (err) {
    console.log("Error: " + err);
}

ws.onmessage = function (evt) {
   onMessage(evt);
}

function sendCredentials() {
    var json = JSON.stringify({
        type : "messages.LoginMessage",
        receiver : $dbsource.value,
        user : $user.value,
        password: $password.value
    });
    writeMessages(json);
    ws.send(json);
}

function writeMessages(message) {
    $textarea.value = $textarea.value + message + "\n";
}

function onMessage(evt) {
    console.log("Received: " + evt.data);
    var json = JSON.parse(evt.data);
    writeMessages(evt.data);
    switch (json.type) {
        case "messages.CredentialAnswerMessage" :
            checkAdmin(json.isAdmin);
            break;
        case "messages.CacheStatusAnswerMessage":
            writeCacheData(json.hits, json.missed);
        default:
    }
}

function checkAdmin(isAdmin) {
    if (isAdmin) {
        document.getElementById("auth_block").style.display="none";
        cacheQueryStatus();

       document.getElementById("some_actions").style.display="";

    } else {
        $user.value="";
        $password.value="";
        $dbsource.value="";
    }
}

function sendActions() {
    var json = JSON.stringify({
        type : "messages.SomeActionsMessage",
        receiver : $dbsource.value
    });
    writeMessages(json);
    ws.send(json);
}

function cacheQueryStatus() {
    var json = JSON.stringify({
        type : "messages.CacheQueryMessage",
        receiver : $dbsource.value
    });
    writeMessages(json);
    ws.send(json);
}

function writeCacheData(hits, missed) {
    document.getElementById("cache_hits").innerHTML = hits;
    document.getElementById("cache_missed").innerHTML = missed;

    $cache_block = document.getElementById("cache_block");
    if (isHidden($cache_block)) {
        $cache_block.style.display = "block";
        setInterval(function(){cacheQueryStatus()}, 10000);
    }

    function isHidden (element) {
        return window.getComputedStyle(element, null).getPropertyValue("display") === "none";
    }
}