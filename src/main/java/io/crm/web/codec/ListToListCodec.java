package io.crm.web.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.util.List;

import static io.crm.web.codec.CodecUtil.myCodecId;
import static io.crm.web.codec.CodecUtil.readToBytes;
import static io.crm.web.codec.CodecUtil.writeBytes;
import static io.crm.util.ExceptionUtil.sallowCall;

/**
 * Created by someone on 28/10/2015.
 */
final public class ListToListCodec implements MessageCodec<List, List> {

    @Override
    public void encodeToWire(Buffer buffer, List list) {
        byte[] encoded = sallowCall(() -> CodecUtil.mapper.writeValueAsString(list)).getBytes();
        writeBytes(buffer, encoded);
    }

    @Override
    public List decodeFromWire(int pos, Buffer buffer) {
        return sallowCall(() -> CodecUtil.mapper.readValue(readToBytes(pos, buffer), List.class));
    }

    @Override
    public List transform(List list) {
        return list;
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
