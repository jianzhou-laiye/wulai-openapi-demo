var CryptoJS = require("crypto-js");


var getRandomString = function(length) {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for(var i = 0; i < length; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
};


var secret = "MY-SECRET";
var pubkey = "MY-KEY";
var timestamp = Math.round(new Date().getTime()/1000);
var nonce = getRandomString(32);
var sign = CryptoJS.SHA1(nonce + timestamp + secret, "").toString(CryptoJS.enc.Hex);

headers = {
    "Api-Auth-pubkey": pubkey,
    "Api-Auth-nonce": nonce,
    "Api-Auth-timestamp": timestamp,
    "Api-Auth-sign": sign
};

console.log(headers);
