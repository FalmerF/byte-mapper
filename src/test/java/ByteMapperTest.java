import entity.Address;
import entity.Contact;
import entity.Document;
import entity.User;
import org.junit.jupiter.api.Test;
import ru.falmer.bmapper.ByteMapper;
import ru.falmer.bmapper.ByteMapperContext;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

class ByteMapperTest {

    private final ByteMapper mapper;
    private final User user;

    public ByteMapperTest() {
        ByteMapperContext context = new ByteMapperContext();
        mapper = context.getByteMapper();

        user = new User(1,
                "First Name", "Second Name", (short) 21,
                new Address("Example Street", "Example City", "Example State"),
                new Contact[]{
                        new Contact("phone", "+1 234 567 89 10"),
                        new Contact("email", "email@example.com")
                },
                new Document[]{
                        new Document(UUID.randomUUID(), "test", "Hello, world!".getBytes())
                }
        );
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