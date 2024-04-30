package org.example.netty.msg

import io.netty.channel.Channel
import java.io.Serializable

class MyCommand : Serializable{

    lateinit var action: String
    lateinit var username: String
    lateinit var channel: Channel
    lateinit var msg: String
    lateinit var to: String

    constructor()

    constructor(channel: Channel, username: String, action: String, msg: String) {
        this.channel = channel
        this.username = username;
        this.action = action
        this.msg = msg
    }

    companion object {
        const val LOGIN: String = "login"
        const val SEND: String = "send"
        const val LOGOUT: String = "LOGOUT"
        const val TellTo: String = "TellTo"
    }

}