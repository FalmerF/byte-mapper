# byte-mapper

byte-mapper provides a convenient serialiser and deserialiser of Java objects into a compact byte array.

---

## Usage

Example data class:

```java
@ByteEntity(1)
public class User {

    @ByteProperty(1)
    private int id;

    @ByteProperty(2)
    private String firstName;

    @ByteProperty(3)
    private short age;

    @ByteProperty(4)
    private Address address;

    @ByteProperty(5)
    private Contact[] contacts;
    
    // Getters and Setters...
}
```

Mapping object:

```java
// Create Byte Mapper Context
ByteMapperContext context = new ByteMapperContext();
mapper = context.getByteMapper();

// Write User object to ByteBuffer
ByteBuffer exampleBuffer = ByteBuffer.allocate(1024);
mapper.write(exampleBuffer, user);

buffer.flip();

// Read User object from ByteBuffer
user = mapper.read(exampleBuffer);
```

---

## Processing Custom Values

To add your own value processing, create a class inherited from `ru.falmer.bmapper.codec.ValueCodec`:

```java
public class FloatCodec implements ValueCodec {

    @Override
    public Class<?>[] getProvidedValueClass() {
        return new Class[]{Float.class, float.class};
    }

    @Override
    public Object read(ByteBuffer buffer, Class<?> clazz, ByteMapperContext context, ByteMapper mapper) {
        return buffer.getFloat();
    }

    @Override
    public void write(ByteBuffer buffer, ByteMapperContext context, ByteMapper mapper, Object value) {
        buffer.putFloat((Float) value);
    }
}
```

Then register it with the `ByteMapperContext`:

```java
ByteMapperContext context = new ByteMapperContext();
context.registerValueCodec(new FloatCodec());
```

> **Important!** Register `ValueCodec` before you start working with the context!

---

## Annotations

### ByteEntity

All classes marked with the `@ByteEntity` annotation can be serialised and deserialised using `ByteMapper`. Each entity must have a unique numeric identifier within the same context.

### ByteParameter

Each field marked with the `@ByteParameter` annotation will be serialised and deserialised. Fields NOT marked with this annotation will be ignored. Each field must have a unique numeric identifier within the same entity.
