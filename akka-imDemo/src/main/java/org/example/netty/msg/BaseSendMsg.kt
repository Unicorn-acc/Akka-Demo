package org.example.netty.msg

import io.netty.channel.Channel


class BaseSendMsg {
    var from: String? = null
    var msg: String? = null
    var channel: Channel? = null

    constructor()

    constructor(from: String?, msg: String?, channel: Channel?) {
        this.from = from
        this.msg = msg
        this.channel = channel
    }
}
