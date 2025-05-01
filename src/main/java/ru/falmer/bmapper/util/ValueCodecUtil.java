package ru.falmer.bmapper.util;

import java.nio.ByteBuffer;

public class ValueCodecUtil {

    public static void writeVarInt(ByteBuffer buffer, int value) {
        while ((value & ~0x7F) != 0) {
            buffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        buffer.put((byte) (value & 0x7F));
    }

    public static int readVarInt(ByteBuffer buffer) {
        int value = 0;
        int position = 0;

        while (true) {
            byte b = buffer.get();
            value |= (b & 0x7F) << position;

            if ((b & 0x80) == 0) {
                break;
            }

            position += 7;

            if (position >= 32) {
                throw new IllegalArgumentException("VarInt is too long");
            }
        }

        return value;
    }

    public static void writeVarLong(ByteBuffer buffer, long value) {
        while ((value & ~0x7FL) != 0L) {
            buffer.put((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        buffer.put((byte) (value & 0x7F));
    }

    public static long readVarLong(ByteBuffer buffer) {
        long value = 0;
        int position = 0;

        while (true) {
            byte b = buffer.get();
            value |= (long) (b & 0x7F) << position;

            if ((b & 0x80) == 0) {
                break;
            }

            position += 7;

            if (position >= 64) {
                throw new IllegalArgumentException("VarLong is too long");
            }
        }

        return value;
    }
}
