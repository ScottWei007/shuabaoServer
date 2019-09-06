package com.shuabao.socketServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Scott Wei on 8/9/2018.
 *
 * tpc服务Server是一个bean，会被扫描之后自动启动
 * dubbo的rpc服务会在springboot的CommandLineRunner实现类InitRunner里调用RpcSocketServer.start()启动
 */

@SpringBootApplication
@ComponentScan(basePackages = {"com.shuabao"})//指定掃描包
public class SocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketApplication.class, args);
	}
}
