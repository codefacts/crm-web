package io.crm.web.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;

import java.text.SimpleDateFormat;

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
}
