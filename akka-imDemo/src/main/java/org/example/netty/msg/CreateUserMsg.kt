package org.example.netty.msg

import io.netty.channel.Channel


class CreateUserMsg {
    var name: String? = null
    var roomid: String? = null
    var channel: Channel? = null

    constructor(name: String?, roomid: String?, channel: Channel?) {
        this.name = name
        this.roomid = roomid
        this.channel = channel
    }

    constructor()
}
