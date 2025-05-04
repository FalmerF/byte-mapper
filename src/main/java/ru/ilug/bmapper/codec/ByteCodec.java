package ru.ilug.bmapper.codec;

import ru.ilug.bmapper.ByteMapper;
import ru.ilug.bmapper.ByteMapperContext;

import java.nio.ByteBuffer;

public class ByteCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Byte.class, byte.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return buffer.get();
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        buffer.put((Byte) value);
    }
}
