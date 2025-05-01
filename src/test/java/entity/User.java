package entity;

import lombok.*;
import ru.falmer.bpersistence.annotation.ByteEntity;
import ru.falmer.bpersistence.annotation.ByteProperty;

@Getter
@Setter
@ToString
@ByteEntity(1)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ByteProperty(1)
    private Integer id;

    @ByteProperty(4)
    private String[] name;

    @ByteProperty(5)
    private double[] value;

    @ByteProperty(2)
    private Address[] address;

    @ByteProperty(3)
    private short age;
}
