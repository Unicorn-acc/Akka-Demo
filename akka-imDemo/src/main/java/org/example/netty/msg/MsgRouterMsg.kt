package org.example.netty.msg

import io.netty.channel.Channel

class MsgRouterMsg {
    var channel: Channel? = null
    var toRoom: String? = null
    var msg: String? = null

    constructor()

    constructor(channel: Channel?, toRoom: String?, msg: String?) {
        this.channel = channel
        this.toRoom = toRoom
        this.msg = msg
    }
}
