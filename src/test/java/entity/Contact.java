package entity;

import lombok.*;
import ru.ilug.bmapper.annotation.ByteEntity;
import ru.ilug.bmapper.annotation.ByteProperty;

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
