package ru.falmer.bpersistence.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class ByteMapperProperty {

    private final Class<?> clazz;
    private final int id;
    private final Object defaultValue;
    private final Function<Object, Object> getterFunction;
    private final BiConsumer<Object, Object> setterFunction;

}
