package com.kerberos.esp8266

import kotlin.properties.Delegates

class Debug {
    var lives by Delegates.notNull<Long>()
    var trigger by Delegates.notNull<Long>()

    constructor(){

    }

    constructor(lives:Long,trigger:Long) {
        this.lives = lives
        this.trigger = trigger
    }
}