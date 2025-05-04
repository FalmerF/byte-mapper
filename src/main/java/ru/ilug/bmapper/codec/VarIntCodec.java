package ru.ilug.bmapper.codec;

import ru.ilug.bmapper.ByteMapper;
import ru.ilug.bmapper.ByteMapperContext;
import ru.ilug.bmapper.util.ValueCodecUtil;

import java.nio.ByteBuffer;

public class VarIntCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Integer.class, int.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return ValueCodecUtil.readVarInt(buffer);
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        ValueCodecUtil.writeVarInt(buffer, (Integer) value);
    }
}
