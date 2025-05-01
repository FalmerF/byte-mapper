package entity;

import lombok.*;
import ru.falmer.bmapper.annotation.ByteEntity;
import ru.falmer.bmapper.annotation.ByteProperty;

@Getter
@Setter
@ToString
@ByteEntity(1)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @ByteProperty(1)
    private int id;

    @ByteProperty(2)
    private String firstName;

    @ByteProperty(3)
    private String secondName;

    @ByteProperty(4)
    private short age;

    @ByteProperty(5)
    private Address address;

    @ByteProperty(6)
    private Contact[] contacts;

    @ByteProperty(7)
    private Document[] documents;
}
