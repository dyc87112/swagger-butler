package com.didispace.swagger.butler;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@EnableSwaggerButler
@SpringBootApplication
public class StaticApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaticApplication.class);
    }

}
