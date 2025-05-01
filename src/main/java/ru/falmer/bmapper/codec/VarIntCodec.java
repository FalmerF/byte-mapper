package ru.falmer.bmapper.codec;

import ru.falmer.bmapper.ByteMapper;
import ru.falmer.bmapper.ByteMapperContext;
import ru.falmer.bmapper.util.ValueCodecUtil;

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
