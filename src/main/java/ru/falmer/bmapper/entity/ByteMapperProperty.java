package ru.falmer.bmapper.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.falmer.bmapper.codec.ValueCodec;

import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ByteMapperProperty {

    @Getter
    private final Class<?> clazz;
    @Getter
    private final int id;
    @Getter
    private final ValueCodec codec;
    @Getter
    private final Object defaultValue;

    private final Function<Object, Object> getterFunction;
    private final BiConsumer<Object, Object> setterFunction;

    public void setValue(Object target, Object value) {
        setterFunction.accept(target, value);
    }

    public Object getValue(Object target) {
        return getterFunction.apply(target);
    }

}
