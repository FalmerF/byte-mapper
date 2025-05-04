package ru.ilug.bmapper.codec;

import ru.ilug.bmapper.ByteMapper;
import ru.ilug.bmapper.ByteMapperContext;
import ru.ilug.bmapper.util.ValueCodecUtil;

import java.nio.ByteBuffer;

public class VarLongCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Long.class, long.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return ValueCodecUtil.readVarLong(buffer);
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        ValueCodecUtil.writeVarLong(buffer, (Long) value);
    }
}
