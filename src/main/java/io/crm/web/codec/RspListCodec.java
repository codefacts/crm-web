package io.crm.web.codec;

import io.crm.web.util.RspList;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

/**
 * Created by someone on 28/10/2015.
 */
public class RspListCodec implements MessageCodec<RspList, RspList> {

    @Override
    public void encodeToWire(Buffer buffer, RspList rspList) {

    }

    @Override
    public RspList decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public RspList transform(RspList rspList) {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public byte systemCodecID() {
        return 0;
    }
}
