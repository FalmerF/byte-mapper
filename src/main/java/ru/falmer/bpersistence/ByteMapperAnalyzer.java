package ru.falmer.bpersistence;

import lombok.RequiredArgsConstructor;
import ru.falmer.bpersistence.annotation.ByteEntity;
import ru.falmer.bpersistence.annotation.ByteProperty;
import ru.falmer.bpersistence.entity.ByteMapperEntity;
import ru.falmer.bpersistence.entity.ByteMapperProperty;
import ru.falmer.bpersistence.exception.ByteMapperAnalyzeException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class ByteMapperAnalyzer {

    private final ByteMapperContext context;

    public ByteMapperEntity analyzeEntity(Class<?> clazz) {
        int entityId = getEntityId(clazz);

        Constructor<?> constructor;
        Object instance;

        try {
            constructor = clazz.getDeclaredConstructor();
            instance = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ByteMapperAnalyzeException("Class " + clazz.getName() + " must have a no args constructor");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Map<Integer, ByteMapperProperty> properties = analyzeProperties(clazz, instance);

        return new ByteMapperEntity(entityId, properties, constructor);
    }

    private int getEntityId(Class<?> clazz) {
        ByteEntity entityAnnotation = clazz.getAnnotation(ByteEntity.class);

        if (entityAnnotation == null) {
            throw new ByteMapperAnalyzeException("Class " + clazz.getName() + " has no @ByteEntity annotation");
        }

        return entityAnnotation.value();
    }

    private Map<Integer, ByteMapperProperty> analyzeProperties(Class<?> entityClazz, Object instance) {
        Map<Integer, ByteMapperProperty> properties = new HashMap<>();

        for (Field field : entityClazz.getDeclaredFields()) {
            ByteProperty annotation = field.getAnnotation(ByteProperty.class);

            if (annotation == null) {
                continue;
            }

            int propertyId = annotation.value();
            ByteMapperProperty property = analyzeField(entityClazz, field, propertyId, instance);

            if (properties.containsKey(propertyId)) {
                throw new ByteMapperAnalyzeException("Property with id " + propertyId + " already exists in class " + entityClazz.getName());
            }

            properties.put(propertyId, property);
        }

        return properties;
    }

    private ByteMapperProperty analyzeField(Class<?> entityClazz, Field field, int propertyId, Object instance) {
        Class<?> fieldClazz = field.getType();

        if (context.getCodecForClass(fieldClazz) == null) {
            throw new ByteMapperAnalyzeException("Cannot find codec for class " + entityClazz);
        }

        String name = formatFieldName(field.getName());

        Function<Object, Object> getterFunction = getFieldGetter(entityClazz, field, name, fieldClazz);
        BiConsumer<Object, Object> setterFunction = getFieldSetter(entityClazz, field, name, fieldClazz);

        Object defaultValue = getterFunction.apply(instance);

        ValueCodec codec = context.getCodecForClass(fieldClazz);

        return new ByteMapperProperty(fieldClazz, propertyId, codec, defaultValue, getterFunction, setterFunction);
    }

    private String formatFieldName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private Function<Object, Object> getFieldGetter(Class<?> entityClazz, Field field, String name, Class<?> fieldClazz) {
        try {
            return getFieldMethodGetter(entityClazz, name, fieldClazz);
        } catch (Exception e) {
            return getFieldSimpleGetter(field);
        }
    }

    private Function<Object, Object> getFieldMethodGetter(Class<?> entityClazz, String name, Class<?> fieldClazz) throws NoSuchMethodException, IllegalAccessException {
        String getterName = getFieldGetterName(name, fieldClazz);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(fieldClazz);
        MethodHandle method = lookup.findVirtual(entityClazz, getterName, methodType);

        return object -> {
            try {
                return method.invoke(object);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Function<Object, Object> getFieldSimpleGetter(Field field) {
        return object -> {
            try {
                return field.get(object);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private String getFieldGetterName(String name, Class<?> type) {
        if (type.equals(Boolean.class)) {
            return "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    private BiConsumer<Object, Object> getFieldSetter(Class<?> entityClazz, Field field, String name, Class<?> fieldClazz) {
        try {
            return getFieldMethodSetter(entityClazz, name, fieldClazz);
        } catch (Exception e) {
            return getFieldSimpleSetter(field);
        }
    }

    private BiConsumer<Object, Object> getFieldMethodSetter(Class<?> entityClazz, String name, Class<?> fieldClazz) throws NoSuchMethodException, IllegalAccessException {
        String setterName = getFieldSetterName(name, fieldClazz);

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, fieldClazz);
        MethodHandle method = lookup.findVirtual(entityClazz, setterName, methodType);

        return (object, value) -> {
            try {
                method.invoke(object, value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private BiConsumer<Object, Object> getFieldSimpleSetter(Field field) {
        return (object, value) -> {
            try {
                field.set(object, value);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    private String getFieldSetterName(String name, Class<?> type) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
