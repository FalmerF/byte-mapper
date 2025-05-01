package ru.falmer.bmapper.codec;

import ru.falmer.bmapper.ByteMapper;
import ru.falmer.bmapper.ByteMapperContext;
import ru.falmer.bmapper.util.ValueCodecUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{String.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        int length = ValueCodecUtil.readVarInt(buffer);
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        String string = (String) value;
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        ValueCodecUtil.writeVarInt(buffer, bytes.length);
        buffer.put(bytes);
    }
}
