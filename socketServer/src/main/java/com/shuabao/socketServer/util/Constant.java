package com.shuabao.socketServer.util;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public interface Constant<T extends Constant<T>> extends Comparable<T>{
    int id();

    String name();
}
