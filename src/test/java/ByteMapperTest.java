import entity.Address;
import entity.User;
import org.junit.jupiter.api.Test;
import ru.falmer.bpersistence.ByteMapper;
import ru.falmer.bpersistence.ByteMapperContext;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.fail;

class ByteMapperTest {

    private final ByteMapper mapper;
    private final User user;

    public ByteMapperTest() {
        ByteMapperContext context = new ByteMapperContext();
        mapper = context.getByteMapper();

        user = new User(1, new String[] {"Person", "Parent"}, new double[] {0.342, 0.458},
                new Address[]{
                        new Address("Test 1.1", "Test 1.2", "Test 1.3"),
                        new Address("Test 2.1", "Test 2.2", "Test 2.3"),
                        new Address("Test 3.1", "Test 3.2", "Test 3.3")
                }, (short) 21);
    }

    @Test
    void basicTest() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        mapper.write(buffer, user);

        buffer.flip();

        User deserializedUser = (User) mapper.read(buffer);

        System.out.println(deserializedUser.toString());
    }

    @Test
    void performanceTest() {
        int iterations = 1_000_000;
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        Instant start = Instant.now();
        for (int i = 0; i < iterations; i++) {
            buffer.clear();
            mapper.write(buffer, user);
            buffer.flip();
            mapper.read(buffer);
        }
        Instant end = Instant.now();
        long millis = Duration.between(start, end).toMillis();

        System.out.printf("Processed %,d iterations in %d ms (%.2f ms per operation)%n",
                iterations, millis, (millis * 1000.0) / iterations);
    }
}