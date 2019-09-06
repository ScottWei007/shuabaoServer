package com.shuabao.apiServer.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by Scott Wei on 4/8/2018.
 */
@Component
public class TestTask {

//    @Scheduled(cron = "*/10 * * * * ?")
    public void testTask() {
        System.out.println("this is a test for task scheduled");
    }

}
