package ru.falmer.bmapper.codec;

import ru.falmer.bmapper.ByteMapper;
import ru.falmer.bmapper.ByteMapperContext;

import java.nio.ByteBuffer;

public class ObjectCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Object.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return mapper.read(buffer);
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        mapper.write(buffer, value);
    }
}
