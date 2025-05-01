package ru.falmer.bpersistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.falmer.bpersistence.entity.ByteMapperEntity;
import ru.falmer.bpersistence.entity.ByteMapperProperty;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class ByteMapper {

    private final ByteMapperContext context;

    public Object read(ByteBuffer buffer) {
        int entityId = buffer.getInt();
        ByteMapperEntity entity = context.getEntityById(entityId);
        Object instance = entity.getInstanceCreator().apply(null);

        int parameterCount = buffer.getInt();

        for (int i = 0; i < parameterCount; i++) {
            int parameterId = buffer.getInt();
            ByteMapperProperty parameter = entity.getPropertiesMap().get(parameterId);
            ValueCodec codec = context.getCodecForClass(parameter.getClazz());
            Object value = codec.read(buffer, parameter.getClazz(), context, this);
            parameter.getSetterFunction().accept(instance, value);
        }

        return instance;
    }

    public void write(ByteBuffer buffer, Object object) {
        ByteMapperEntity entity = context.getEntityForClass(object.getClass());

        buffer.putInt(entity.getId());

        List<ParameterData> parametersData = new ArrayList<>();

        for (ByteMapperProperty parameter : entity.getPropertiesMap().values()) {
            Object value = parameter.getGetterFunction().apply(object);
            if (Objects.equals(value, parameter.getDefaultValue())) {
                continue;
            }

            ValueCodec type = context.getCodecForClass(parameter.getClazz());

            parametersData.add(new ParameterData(parameter.getId(), value, type));
        }

        buffer.putInt(parametersData.size());

        for (ParameterData parameterData : parametersData) {
            buffer.putInt(parameterData.getId());
            parameterData.getType().write(buffer, context, this, parameterData.getValue());
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ParameterData {

        private final int id;
        private final Object value;
        private final ValueCodec type;

    }
}
