# -*- coding:utf8 -*-
import time
import json
import hashlib
import string
import random

CHAR_LIST=[]
[[CHAR_LIST.append(e) for e in string.letters] for i in range(0,2)]
[[CHAR_LIST.append(e) for e in string.letters] for i in range(0,2)]
[[CHAR_LIST.append(e) for e in string.digits] for i in range(0,2)]

def GetChars(length):
    random.shuffle(CHAR_LIST)
    return "".join(CHAR_LIST[0:length])

def get_headers():
    secret = "MY-SECRET"
    pubkey = "MY-KEY"
    #秒级别时间戳
    timestamp = str(int(time.time()))
    nonce = GetChars(32)
#     nonce = "cfb7BScLHKXZljIqFaJi1zi5xUmkupHr"
#     timestamp = "1524907268"
    sign = hashlib.sha1(nonce + timestamp + secret).hexdigest()
    data = {
        "pubkey": pubkey,
        "sign": sign,
        "nonce": nonce,
        "timestamp": timestamp
    }
    headers = {}
    for k, v in data.iteritems():
        headers["Api-Auth-" + k] = v
    return headers

if __name__ == "__main__":
    print(json.dumps(get_headers(), ensure_ascii=False, indent=4))
