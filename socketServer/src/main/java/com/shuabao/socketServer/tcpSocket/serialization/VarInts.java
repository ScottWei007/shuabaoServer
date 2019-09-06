

package com.shuabao.socketServer.tcpSocket.serialization;


@SuppressWarnings("all")
public final class VarInts {

    /**
     * Compute the number of bytes that would be needed to encode a varInt. {@code value} is treated as unsigned, so it
     * won't be sign-extended if negative.
     */
    public static int computeRawVarInt32Size(final int value) {
        if ((value & (~0 << 7)) == 0) {
            return 1;
        }
        if ((value & (~0 << 14)) == 0) {
            return 2;
        }
        if ((value & (~0 << 21)) == 0) {
            return 3;
        }
        if ((value & (~0 << 28)) == 0) {
            return 4;
        }
        return 5;
    }

    /**
     * Compute the number of bytes that would be needed to encode a varInt.
     */
    public static int computeRawVarInt64Size(long value) {
        // Handle two popular special cases up front ...
        if ((value & (~0L << 7)) == 0L) {
            return 1;
        }
        if (value < 0L) {
            return 10;
        }
        // ... leaving us with 8 remaining, which we can divide and conquer
        int n = 2;
        if ((value & (~0L << 35)) != 0L) {
            n += 4;
            value >>>= 28;
        }
        if ((value & (~0L << 21)) != 0L) {
            n += 2;
            value >>>= 14;
        }
        if ((value & (~0L << 14)) != 0L) {
            n += 1;
        }
        return n;
    }

    private VarInts() {}
}
