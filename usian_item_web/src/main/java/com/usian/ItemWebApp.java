package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication
//@EnableDiscoveryClient  // 等价于@EnableEurekaClient
//@EnableCircuitBreaker
@SpringCloudApplication //该注解可代替以上三个注解
@EnableFeignClients   // 开启feign功能
public class ItemWebApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemWebApp.class, args);
    }
}