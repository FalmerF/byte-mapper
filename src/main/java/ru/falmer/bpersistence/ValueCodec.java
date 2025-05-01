package ru.falmer.bpersistence;

import ru.falmer.bpersistence.util.ValueCodecUtil;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface ValueCodec {

    Class<?>[] getProvidedValueClass();

    Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper);

    void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value);

    class ObjectCodec implements ValueCodec {

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

    class ByteCodec implements ValueCodec {

        @Override
        public Class<?>[] getProvidedValueClass() {
            return new Class[]{Byte.class, byte.class};
        }

        @Override
        public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
            return buffer.get();
        }

        @Override
        public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
            buffer.put((Byte) value);
        }
    }

    class ShortCodec implements ValueCodec {

        @Override
        public Class<?>[] getProvidedValueClass() {
            return new Class[]{Short.class, short.class};
        }

        @Override
        public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
            return buffer.getShort();
        }

        @Override
        public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
            buffer.putShort((short) value);
        }
    }

    class VarIntCodec implements ValueCodec {

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

    class VarLongCodec implements ValueCodec {

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

    class FloatCodec implements ValueCodec {

        @Override
        public Class<?>[] getProvidedValueClass() {
            return new Class[]{Float.class, float.class};
        }

        @Override
        public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
            return buffer.getFloat();
        }

        @Override
        public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
            buffer.putFloat((Float) value);
        }
    }

    class DoubleCodec implements ValueCodec {

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

    class StringCodec implements ValueCodec {

        @Override
        public Class<?>[] getProvidedValueClass() {
            return new Class[]{String.class};
        }

        @Override
        public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
            int length = ValueCodecUtil.readVarInt(buffer);
            byte[] bytes = new byte[length];
            buffer.get(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        }

        @Override
        public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
            String string = (String) value;
            byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
            ValueCodecUtil.writeVarInt(buffer, bytes.length);
            buffer.put(bytes);
        }
    }

    class CharCodec implements ValueCodec {

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

    class ArrayCodec implements ValueCodec {

        @Override
        public Class<?>[] getProvidedValueClass() {
            return new Class[]{Object[].class};
        }

        @Override
        public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
            Class<?> componentType = clazz.getComponentType();

            int length = ValueCodecUtil.readVarInt(buffer);
            Object array = java.lang.reflect.Array.newInstance(componentType, length);
            ValueCodec codec = context.getCodecForClass(componentType);

            for (int i = 0; i < length; ++i) {
                Object value = codec.read(buffer, componentType, context, mapper);
                Array.set(array, i, value);
            }

            return array;
        }

        @Override
        public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
            Class<?> clazz = value.getClass().getComponentType();
            ValueCodec codec = context.getCodecForClass(clazz);

            int length = Array.getLength(value);
            ValueCodecUtil.writeVarInt(buffer, length);

            for (int i = 0; i < length; i++) {
                Object object = Array.get(value, i);
                codec.write(buffer, context, mapper, object);
            }
        }
    }
}
