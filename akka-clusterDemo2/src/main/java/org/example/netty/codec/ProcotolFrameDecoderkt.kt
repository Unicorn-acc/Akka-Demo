package org.example.netty.codec

import io.netty.handler.codec.LengthFieldBasedFrameDecoder

class ProcotolFrameDecoderkt @JvmOverloads constructor(
    maxFrameLength: Int = 1024,
    lengthFieldOffset: Int = 0,
    lengthFieldLength: Int = 4,
    lengthAdjustment: Int = 0,
    initialBytesToStrip: Int = 0
) : LengthFieldBasedFrameDecoder(
    maxFrameLength,
    lengthFieldOffset,
    lengthFieldLength,
    lengthAdjustment,
    initialBytesToStrip
)
