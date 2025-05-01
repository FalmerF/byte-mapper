package entity;

import lombok.*;
import ru.falmer.bpersistence.annotation.ByteEntity;
import ru.falmer.bpersistence.annotation.ByteProperty;

@ToString
@ByteEntity(3)
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @ByteProperty(1)
    public String name;

    @ByteProperty(2)
    public String value;

}
