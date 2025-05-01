import entity.Address;
import entity.User;
import org.junit.jupiter.api.Test;
import ru.falmer.bpersistence.ByteMapper;
import ru.falmer.bpersistence.ByteMapperContext;

import java.nio.ByteBuffer;

class ByteMapperTest {

    private final ByteMapper mapper;

    public ByteMapperTest() {
        ByteMapperContext context = new ByteMapperContext();
        mapper = context.getByteMapper();
    }

    @Test
    void mapper() {
        User user = new User(1, new String[] {"Person", "Parent"}, new double[] {0.342, 0.458},
                new Address[]{
                        new Address("Test 1.1", "Test 1.2", "Test 1.3"),
                        new Address("Test 2.1", "Test 2.2", "Test 2.3"),
                        new Address("Test 3.1", "Test 3.2", "Test 3.3")
                }, (short) 21);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        mapper.write(buffer, user);

        buffer.flip();

        user = (User) mapper.read(buffer);

        System.out.println(user.toString());
    }
}