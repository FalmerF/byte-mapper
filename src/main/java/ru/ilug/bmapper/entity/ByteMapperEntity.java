package ru.ilug.bmapper.entity;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ByteMapperEntity {

    @Getter
    private final int id;
    private final Map<Integer, ByteMapperProperty> propertiesMap;

    private final Function<Void, Object> instanceCreator;

    public ByteMapperEntity(int id, Map<Integer, ByteMapperProperty> propertiesMap, Constructor<?> constructor) {
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

    public Object createInstance() {
        return instanceCreator.apply(null);
    }

    public ByteMapperProperty getProperty(int id) {
        return propertiesMap.get(id);
    }

    public Collection<ByteMapperProperty> getProperties() {
        return propertiesMap.values();
    }
}
