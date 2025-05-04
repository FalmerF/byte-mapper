package entity;

import lombok.*;
import ru.ilug.bmapper.annotation.ByteEntity;
import ru.ilug.bmapper.annotation.ByteProperty;

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
