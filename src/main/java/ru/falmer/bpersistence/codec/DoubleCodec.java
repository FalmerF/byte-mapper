package ru.falmer.bpersistence.codec;

import ru.falmer.bpersistence.ByteMapper;
import ru.falmer.bpersistence.ByteMapperContext;

import java.nio.ByteBuffer;

public class DoubleCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Double.class, double.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return buffer.getDouble();
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        buffer.putDouble((Double) value);
    }
}
