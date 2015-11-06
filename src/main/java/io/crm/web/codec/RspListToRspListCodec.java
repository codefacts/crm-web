package io.crm.web.codec;

import io.crm.util.ExceptionUtil;
import io.crm.web.util.RspList;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import static io.crm.util.ExceptionUtil.toRuntimeCall;
import static io.crm.web.codec.CodecUtil.*;

/**
 * Created by someone on 28/10/2015.
 */
public class RspListToRspListCodec implements MessageCodec<RspList, RspList> {

    @Override
    public void encodeToWire(Buffer buffer, RspList rspList) {
        final byte[] encoded = toRuntimeCall(() -> mapper.writeValueAsString(rspList)).getBytes();
        writeBytes(buffer, encoded);
    }

    @Override
    public RspList decodeFromWire(int pos, Buffer buffer) {
        final byte[] bytes = readToBytes(pos, buffer);
        return toRuntimeCall(() -> mapper.readValue(bytes, RspList.class));
    }

    @Override
    public RspList transform(RspList rspList) {
        return rspList.copy();
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return myCodecId;
    }
}
