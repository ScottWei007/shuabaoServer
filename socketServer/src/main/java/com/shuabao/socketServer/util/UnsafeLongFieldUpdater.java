
package com.shuabao.socketServer.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;


public class UnsafeLongFieldUpdater<U> {
    private final long offset;
    private final Unsafe unsafe;

    UnsafeLongFieldUpdater(Unsafe unsafe, Class<? super U> tClass, String fieldName) throws NoSuchFieldException {
        Field field = tClass.getDeclaredField(fieldName);
        if (unsafe == null) {
            throw new NullPointerException("unsafe");
        }
        this.unsafe = unsafe;
        offset = unsafe.objectFieldOffset(field);
    }

    public void set(U obj, long newValue) {
        unsafe.putLong(obj, offset, newValue);
    }

    public long get(U obj) {
        return unsafe.getLong(obj, offset);
    }
}
