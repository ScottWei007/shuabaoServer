

package com.shuabao.socketServer.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;


public class UnsafeIntegerFieldUpdater<U> {
    private final long offset;
    private final Unsafe unsafe;

    UnsafeIntegerFieldUpdater(Unsafe unsafe, Class<? super U> tClass, String fieldName) throws NoSuchFieldException {
        Field field = tClass.getDeclaredField(fieldName);
        if (unsafe == null) {
            throw new NullPointerException("unsafe");
        }
        this.unsafe = unsafe;
        offset = unsafe.objectFieldOffset(field);
    }

    public void set(U obj, int newValue) {
        unsafe.putInt(obj, offset, newValue);
    }

    public int get(U obj) {
        return unsafe.getInt(obj, offset);
    }
}
