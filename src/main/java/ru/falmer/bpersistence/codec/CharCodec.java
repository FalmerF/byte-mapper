package ru.falmer.bpersistence.codec;

import ru.falmer.bpersistence.ByteMapper;
import ru.falmer.bpersistence.ByteMapperContext;
import ru.falmer.bpersistence.util.ValueCodecUtil;

import java.nio.ByteBuffer;

public class CharCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Character.class, char.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return (char) ValueCodecUtil.readVarInt(buffer);
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        ValueCodecUtil.writeVarInt(buffer, (Character) value);
    }
}
