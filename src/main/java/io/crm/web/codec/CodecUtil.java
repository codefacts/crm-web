package io.crm.web.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.text.SimpleDateFormat;
import java.util.List;

import static io.crm.util.ExceptionUtil.sallowCall;

/**
 * Created by someone on 28/10/2015.
 */
final public class CodecUtil {
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final byte myCodecId = -1;

    public static void writeBytes(final Buffer buffer, final byte[] encoded) {
        buffer.appendInt(encoded.length);
        Buffer buff = Buffer.buffer(encoded);
        buffer.appendBuffer(buff);
    }

    public static byte[] readToBytes(int pos, final Buffer buffer) {
        int length = buffer.getInt(pos);
        pos += 4;
        return buffer.getBytes(pos, pos + length);
    }

    public static  <T> MessageCodec<T, T> messageCodec(Class<T> tClass) {
        return new MessageCodec<T, T>() {
            @Override
            public void encodeToWire(Buffer buffer, T list) {
                byte[] encoded = sallowCall(() -> CodecUtil.mapper.writeValueAsString(list)).getBytes();
                writeBytes(buffer, encoded);
            }

            @Override
            public T decodeFromWire(int pos, Buffer buffer) {
                return sallowCall(() -> CodecUtil.mapper.readValue(readToBytes(pos, buffer), tClass));
            }

            @Override
            public T transform(T list) {
                return list;
            }

            @Override
            public String name() {
                String name = tClass.getSimpleName();
                return name + "To" + name + "Codec";
            }

            @Override
            public byte systemCodecID() {
                return CodecUtil.myCodecId;
            }
        };
    }
}
