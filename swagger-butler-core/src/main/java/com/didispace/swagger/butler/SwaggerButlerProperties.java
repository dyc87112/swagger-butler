package com.didispace.swagger.butler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 程序猿DD/翟永超 on 2018/5/24.
 * <p>
 * Blog: http://blog.didispace.com/
 * Github: https://github.com/dyc87112/
 */
@Data
@ConfigurationProperties("swagger.butler")
public class SwaggerButlerProperties {

    /**
     * 配置静态文档地址
     */
    private List<SwaggerResourceProperties> resources = new ArrayList<>();

    /**
     * 不显示没有实例的服务
     */
    private Boolean notShowNoInstanceService = true;

    /**
     * Swagger返回JSON文档的接口路径
     */
    private String apiDocsPath = "/v2/api-docs";

}
