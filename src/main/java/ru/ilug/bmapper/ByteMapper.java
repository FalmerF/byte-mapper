package ru.ilug.bmapper;

import lombok.AllArgsConstructor;
import ru.ilug.bmapper.codec.ValueCodec;
import ru.ilug.bmapper.entity.ByteMapperEntity;
import ru.ilug.bmapper.entity.ByteMapperProperty;

import java.nio.ByteBuffer;
import java.util.Objects;

@AllArgsConstructor
public class ByteMapper {

    private final ByteMapperContext context;

    public Object read(ByteBuffer buffer) {
        int entityId = buffer.getInt();
        ByteMapperEntity entity = context.getEntityById(entityId);
        Object instance = entity.createInstance();

        readParameters(buffer, entity, instance);

        return instance;
    }

    private void readParameters(ByteBuffer buffer, ByteMapperEntity entity, Object instance) {
        int parameterCount = buffer.getInt();

        for (int i = 0; i < parameterCount; i++) {
            int parameterId = buffer.getInt();
            ByteMapperProperty parameter = entity.getProperty(parameterId);
            ValueCodec codec = parameter.getCodec();

            Object value = codec.read(buffer, parameter.getClazz(), context, this);
            parameter.setValue(instance, value);
        }
    }

    public void write(ByteBuffer buffer, Object object) {
        ByteMapperEntity entity = context.registerAndGetEntity(object.getClass());
        buffer.putInt(entity.getId());

        writeParameters(buffer, entity, object);
    }

    private void writeParameters(ByteBuffer buffer, ByteMapperEntity entity, Object instance) {
        int count = 0;
        int countIndex = buffer.position();
        buffer.putInt(0);

        for(ByteMapperProperty parameter : entity.getProperties()) {
            Object value = parameter.getValue(instance);

            if (Objects.equals(value, parameter.getDefaultValue())) {
                continue;
            }

            buffer.putInt(parameter.getId());
            ValueCodec codec = parameter.getCodec();
            codec.write(buffer, context, this, value);

            count++;
        }

        buffer.putInt(countIndex, count);
    }
}
