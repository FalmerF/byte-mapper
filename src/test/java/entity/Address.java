package entity;

import lombok.*;
import ru.falmer.bmapper.annotation.ByteEntity;
import ru.falmer.bmapper.annotation.ByteProperty;

@Getter
@Setter
@ToString
@ByteEntity(2)
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @ByteProperty(1)
    private String street;

    @ByteProperty(2)
    private String city;

    @ByteProperty(3)
    private String state;

}
