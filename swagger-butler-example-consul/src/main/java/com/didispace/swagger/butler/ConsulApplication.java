package com.didispace.swagger.butler;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@EnableDiscoveryClient
@EnableSwaggerButler
@SpringBootApplication
public class ConsulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsulApplication.class);
    }

}
