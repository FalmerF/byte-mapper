package ru.falmer.bmapper.codec;

import ru.falmer.bmapper.ByteMapper;
import ru.falmer.bmapper.ByteMapperContext;

import java.nio.ByteBuffer;

public interface ValueCodec {

    Class<?>[] getProvidedValueClass();

    Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper);

    void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value);

}
