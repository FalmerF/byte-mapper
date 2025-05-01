package ru.falmer.bpersistence.entity;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;

@Getter
public class ByteMapperEntity {

    private final Class<?> clazz;
    private final int id;
    private final Map<Integer, ByteMapperProperty> propertiesMap;

    private final Function<Void, Object> instanceCreator;

    public ByteMapperEntity(Class<?> clazz, int id, Map<Integer, ByteMapperProperty> propertiesMap, Constructor<?> constructor) {
        this.clazz = clazz;
        this.id = id;
        this.propertiesMap = propertiesMap;

        instanceCreator = v -> {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
