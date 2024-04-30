package org.example.netty.msg

import io.netty.channel.Channel

class RemoveUserMsg {
    var channel: Channel? = null

    constructor(channel: Channel?) {
        this.channel = channel
    }

    constructor()
}
