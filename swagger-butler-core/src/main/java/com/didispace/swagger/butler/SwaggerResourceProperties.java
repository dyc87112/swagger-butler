package com.didispace.swagger.butler;

import lombok.Data;

@Data
public class SwaggerResourceProperties {

    /**
     * 需要获取的文档名称，对应Zuul中的Route名称
     */
    private String name;

    /**
     * 配置swaggerVersion，不配置的话采用全局默认配置：2.0
     */
    private String swaggerVersion;

    /**
     * 配置api文档的获取路径，不配置的话采用全局默认配置：/v2/api-docs
     */
    private String apiDocsPath;

}