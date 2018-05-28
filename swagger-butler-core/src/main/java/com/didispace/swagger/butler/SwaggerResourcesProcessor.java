package com.didispace.swagger.butler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

public class SwaggerResourcesProcessor implements SwaggerResourcesProvider {

    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private SwaggerButlerProperties swaggerButlerConfig;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        List<Route> routes = routeLocator.getRoutes();
        for (Route route : routes) {
            String routeName = route.getId();

            SwaggerResourceProperties resourceProperties = swaggerButlerConfig.getResources().get(routeName);

            // 不用根据zuul的路由自动生成，并且当前route信息没有配置resource则不生成文档
            if (swaggerButlerConfig.getAutoGenerateFromZuulRoutes() == false && resourceProperties == null) {
                continue;
            }

            // 需要根据zuul的路由自动生成，但是当前路由名在忽略清单中（ignoreRoutes）或者不在生成清单中（generateRoutes）则不生成文档
            if (swaggerButlerConfig.getAutoGenerateFromZuulRoutes() == true && swaggerButlerConfig.needIgnore(routeName)) {
                continue;
            }

            // 处理swagger文档的名称
            String name = routeName;
            if (resourceProperties != null && resourceProperties.getName() != null) {
                name = resourceProperties.getName();
            }

            // 处理获取swagger文档的路径
            String swaggerPath = swaggerButlerConfig.getApiDocsPath();
            if (resourceProperties != null && resourceProperties.getApiDocsPath() != null) {
                swaggerPath = resourceProperties.getApiDocsPath();
            }
            String location = route.getFullPath().replace("**", swaggerPath);

            // 处理swagger的版本设置
            String swaggerVersion = swaggerButlerConfig.getSwaggerVersion();
            if (resourceProperties != null && resourceProperties.getSwaggerVersion() != null) {
                swaggerVersion = resourceProperties.getSwaggerVersion();
            }

            resources.add(swaggerResource(name, location, swaggerVersion));

        }

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}