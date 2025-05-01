package ru.falmer.bpersistence.codec;

import ru.falmer.bpersistence.ByteMapper;
import ru.falmer.bpersistence.ByteMapperContext;
import ru.falmer.bpersistence.util.ValueCodecUtil;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public interface ValueCodec {

    Class<?>[] getProvidedValueClass();

    Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper);

    void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value);

}
