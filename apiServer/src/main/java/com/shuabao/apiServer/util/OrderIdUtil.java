package com.shuabao.apiServer.util;

import com.shuabao.core.manager.EnvironmentManager;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Scott Wei on 8/22/2018.
 */
public final class OrderIdUtil {
    private static final String serverId = EnvironmentManager.getEnvironment().getProperty("server.id");
    private static final String zero = "0";
    private static final AtomicInteger sequence = new AtomicInteger(1);

    public static final long getOrderId() {
        int sequenceId = sequence.getAndIncrement();
        if(sequenceId > 99) {
            synchronized (sequence) {
                sequenceId = sequence.getAndIncrement();
                if(sequenceId > 99) {
                    sequence.set(1);
                    sequenceId = sequence.getAndIncrement();
                }
            }
        }
        //服務器id + 當前時間秒 + 自增(只有一位數，意味著同一秒最多只有99個訂單)
        long orderId;
        if(sequenceId < 10) {
            orderId = Long.valueOf(serverId + (System.currentTimeMillis() / 1000) + zero + sequenceId);
        }else {
            orderId = Long.valueOf(serverId + (System.currentTimeMillis() / 1000) + sequenceId);
        }
       return orderId;
    }


    /*public static void main(String[] args){
        Map<Long, Boolean> map = new ConcurrentHashMap<>();
        Executor executor = Executors.newCachedThreadPool();
        for(int i = 0; i < 10; i ++) {
            int k = i;
            executor.execute(() -> {
                for (int j = 0; j < 100; j ++) {

                    long id = getOrderId();
                    Boolean b = map.put(id, true);
                    if(!Objects.isNull(b)) {
                        System.out.println("====id:" + id);
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(k + "====size:" + map.size());
            }
           );
        }

    }*/
}
