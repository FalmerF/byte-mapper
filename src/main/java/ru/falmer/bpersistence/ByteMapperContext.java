package ru.falmer.bpersistence;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import ru.falmer.bpersistence.entity.ByteMapperEntity;
import ru.falmer.bpersistence.exception.ByteMapperAnalyzeException;

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
                new ValueCodec.ByteCodec(),
                new ValueCodec.ShortCodec(),
                new ValueCodec.VarIntCodec(),
                new ValueCodec.VarLongCodec(),
                new ValueCodec.FloatCodec(),
                new ValueCodec.DoubleCodec(),
                new ValueCodec.StringCodec(),
                new ValueCodec.CharCodec(),
                new ValueCodec.ArrayCodec(),
                new ValueCodec.ObjectCodec()
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
            return typeMap.get(Object[].class);
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
