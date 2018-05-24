package com.didispace.swagger.butler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

public class SwaggerResourcesProcessor implements SwaggerResourcesProvider {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private SwaggerButlerProperties swaggerButlerConfig;

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();

        // 绝对路径的配置加载
        for(SwaggerResourceProperties properties : swaggerButlerConfig.getResources()) {
            resources.add(swaggerResource(properties.getName(), properties.getUrl(), properties.getSwaggerVersion()));
        }

        // 基于服务发现的配置加载
        for (String serviceName : discoveryClient.getServices()) {

            List<ServiceInstance> instances= discoveryClient.getInstances(serviceName);
            if(instances.size() == 0 && swaggerButlerConfig.getNotShowNoInstanceService()) {
                // 没有服务实例跳过
                continue;
            }

            ServiceInstance instance = instances.get(0);
            resources.add(swaggerResource(serviceName, "/" + serviceName + swaggerButlerConfig.getApiDocsPath(), "2.0"));
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