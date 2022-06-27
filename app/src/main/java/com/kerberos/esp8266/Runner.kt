package com.kerberos.esp8266

import kotlin.properties.Delegates

class Runner {
    var bssid by Delegates.notNull<String>()
    var command by Delegates.notNull<String>()
    var msg by Delegates.notNull<String>()
    var password by Delegates.notNull<String>()
    var rtn by Delegates.notNull<String>()

    constructor(){

    }

    constructor(bssid:String,command:String,msg:String,password:String,rtn:String) {
        this.bssid = bssid
        this.command = command
        this.msg = msg
        this.password = password
        this.rtn = rtn
    }
}