package com.didispace.swagger.butler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerButlerProperties.class)
public class SwaggerButlerAutoConfig {

    @Bean
    @Primary
    public SwaggerResourcesProcessor swaggerResourcesProcessor() {
        return new SwaggerResourcesProcessor();
    }

}
