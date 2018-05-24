package com.didispace.swagger.butler;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SwaggerButlerAutoConfig.class})
@EnableZuulProxy
public @interface EnableSwaggerButler {
}
