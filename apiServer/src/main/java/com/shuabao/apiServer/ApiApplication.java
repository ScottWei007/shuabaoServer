package com.shuabao.apiServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Scott Wei on 8/9/2018.
 */

@SpringBootApplication
@EnableScheduling //开启定时任务
@ServletComponentScan//開啓自定義filter，servlet的掃描
@ComponentScan(basePackages = {"com.shuabao"})//指定掃描包
//@MapperScan("com.shuabao.**.mapper") //mybatis掃描包
public class ApiApplication {

	//程序入口
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
}
