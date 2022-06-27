package com.kerberos.esp8266

import kotlin.properties.Delegates

class Config{

    lateinit var author:String
    lateinit var board:String
    lateinit var project :String
    lateinit var date :String
    var live by Delegates.notNull<Long>()

    //Default constructor required for calls
    constructor(){

    }

    constructor(author:String,board:String,project:String,date:String,live:Long){
        this.author = author
        this.board = board
        this.project = project
        this.date = date
        this.live = live

    }
}
