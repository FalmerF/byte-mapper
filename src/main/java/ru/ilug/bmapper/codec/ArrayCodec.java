package ru.ilug.bmapper.codec;

import lombok.RequiredArgsConstructor;
import ru.ilug.bmapper.ByteMapper;
import ru.ilug.bmapper.ByteMapperContext;
import ru.ilug.bmapper.util.ValueCodecUtil;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class ArrayCodec implements ValueCodec {

    private final ValueCodec codec;

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Object[].class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        Class<?> componentType = clazz.getComponentType();

        int length = ValueCodecUtil.readVarInt(buffer);
        Object array = java.lang.reflect.Array.newInstance(componentType, length);

        for (int i = 0; i < length; ++i) {
            Object value = codec.read(buffer, componentType, context, mapper);
            Array.set(array, i, value);
        }

        return array;
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        int length = Array.getLength(value);
        ValueCodecUtil.writeVarInt(buffer, length);

        for (int i = 0; i < length; i++) {
            Object object = Array.get(value, i);
            codec.write(buffer, context, mapper, object);
        }
    }
}
