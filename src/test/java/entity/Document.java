package entity;

import lombok.*;
import ru.falmer.bpersistence.annotation.ByteEntity;
import ru.falmer.bpersistence.annotation.ByteProperty;

import java.util.UUID;

@Getter
@Setter
@ToString
@ByteEntity(4)
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @ByteProperty(1)
    private UUID uuid;

    @ByteProperty(2)
    private String description;

    @ByteProperty(3)
    private byte[] data;
}
