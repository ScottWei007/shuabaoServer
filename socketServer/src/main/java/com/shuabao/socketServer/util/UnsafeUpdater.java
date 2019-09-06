

package com.shuabao.socketServer.util;



public class UnsafeUpdater {

    /**
     * Creates and returns an updater for objects with the given field.
     *
     * @param tClass    the class of the objects holding the field.
     * @param fieldName the name of the field to be updated.
     */
    public static <U> UnsafeIntegerFieldUpdater<U> newIntegerFieldUpdater(Class<? super U> tClass, String fieldName) {
        try {
            return new UnsafeIntegerFieldUpdater<>(UnsafeUtil.getUnsafe(), tClass, fieldName);
        } catch (Throwable t) {
            ThrowUtil.throwException(t);
        }
        return null;
    }

    /**
     * Creates and returns an updater for objects with the given field.
     *
     * @param tClass    the class of the objects holding the field.
     * @param fieldName the name of the field to be updated.
     */
    public static <U> UnsafeLongFieldUpdater<U> newLongFieldUpdater(Class<? super U> tClass, String fieldName) {
        try {
            return new UnsafeLongFieldUpdater<>(UnsafeUtil.getUnsafe(), tClass, fieldName);
        } catch (Throwable t) {
            ThrowUtil.throwException(t);
        }
        return null;
    }

    /**
     * Creates and returns an updater for objects with the given field.
     *
     * @param tClass    the class of the objects holding the field.
     * @param fieldName the name of the field to be updated.
     */
    public static <U, W> UnsafeReferenceFieldUpdater<U, W> newReferenceFieldUpdater(Class<? super U> tClass, String fieldName) {
        try {
            return new UnsafeReferenceFieldUpdater<>(UnsafeUtil.getUnsafe(), tClass, fieldName);
        } catch (Throwable t) {
            ThrowUtil.throwException(t);
        }
        return null;
    }
}
