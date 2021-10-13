package com.applepieme.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author applepieme
 * @date 2021/10/5 15:09
 */
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.applepieme"})
public class CmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args);
    }
}
