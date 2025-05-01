package ru.falmer.bmapper;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import ru.falmer.bmapper.codec.*;
import ru.falmer.bmapper.entity.ByteMapperEntity;
import ru.falmer.bmapper.exception.ByteMapperAnalyzeException;

import java.util.HashMap;
import java.util.Map;

public class ByteMapperContext {

    private final Map<Class<?>, ValueCodec> typeMap = new HashMap<>();
    private final Map<Class<?>, ByteMapperEntity> entityMap = new HashMap<>();
    private final Map<Integer, ByteMapperEntity> entityIdMap = new Int2ObjectOpenHashMap<>();

    private final ByteMapperAnalyzer analyzer;

    @Getter
    private final ByteMapper byteMapper;

    public ByteMapperContext() {
        this.analyzer = new ByteMapperAnalyzer(this);
        this.byteMapper = new ByteMapper(this);

        registerValueCodec(
                new ByteCodec(), new ShortCodec(),
                new VarIntCodec(), new VarLongCodec(),
                new FloatCodec(), new DoubleCodec(),
                new StringCodec(), new CharCodec(),
                new UUIDCodec(), new ObjectCodec()
        );
    }

    private void registerValueCodec(ValueCodec... codecs) {
        for (ValueCodec valueCodec : codecs) {
            for (Class<?> valueClass : valueCodec.getProvidedValueClass()) {
                typeMap.put(valueClass, valueCodec);
            }
        }
    }

    public ByteMapperEntity registerAndGetEntity(Class<?> clazz) {
        ByteMapperEntity entity = entityMap.get(clazz);

        if (entity == null) {
            entity = analyzer.analyzeEntity(clazz);

            if (entityIdMap.containsKey(entity.getId())) {
                throw new ByteMapperAnalyzeException("Entity with id " + entity.getId() + " already exists");
            }

            entityMap.put(clazz, entity);
            entityIdMap.put(entity.getId(), entity);
        }

        return entity;
    }

    public ByteMapperEntity getEntityById(int id) {
        ByteMapperEntity entity = entityIdMap.get(id);
        if (entity == null) {
            throw new ByteMapperAnalyzeException("Entity with id " + id + " not found");
        }

        return entity;
    }

    public ValueCodec getCodecForClass(Class<?> clazz) {
        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            return new ArrayCodec(getCodecForClass(componentType));
        }

        ValueCodec type = typeMap.get(clazz);
        if (type != null) {
            return type;
        }

        ByteMapperEntity entity = registerAndGetEntity(clazz);
        if (entity != null) {
            return typeMap.get(Object.class);
        }

        throw new ByteMapperAnalyzeException("Cannot find type for class " + clazz);
    }
}
