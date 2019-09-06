package com.shuabao.socketServer.util;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public final class Signal extends Exception implements Constant<Signal>{

    private static final class SignalConstant extends AbstractConstant<SignalConstant> {

        public SignalConstant(int id, String name) {
            super(id, name);
        }
    }

    private static final ConstantPool<Signal> pool = new ConstantPool<Signal>() {
        @Override
        protected Signal newConstant(int id, String name) {
            return new Signal(id, name);
        }
    };

    private final SignalConstant signalConstant;
    private Signal(int id, String name) {
        signalConstant = new SignalConstant(id, name);
    }

    public static Signal valueOf(String name) {
        return pool.valueOf(name);
    }

    public static Signal valueOf(Class<?> clz, String name) {
        return pool.valueOf(clz, name);
    }
    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public int id() {
        return signalConstant.id();
    }

    @Override
    public String name() {
        return signalConstant.name();
    }

    @Override
    public int compareTo(Signal o) {
        if(this == o) {
            return 0;
        }
        return signalConstant.compareTo(o.signalConstant);
    }

    @Override
    public String toString() {
        return name();
    }

}
