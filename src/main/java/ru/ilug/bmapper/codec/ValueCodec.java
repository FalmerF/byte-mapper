package ru.ilug.bmapper.codec;

import ru.ilug.bmapper.ByteMapper;
import ru.ilug.bmapper.ByteMapperContext;

import java.nio.ByteBuffer;

public interface ValueCodec {

    Class<?>[] getProvidedValueClass();

    Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper);

    void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value);

}
